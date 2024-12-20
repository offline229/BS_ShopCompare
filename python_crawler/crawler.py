import mysql.connector
from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.common.keys import Keys
from selenium.webdriver.chrome.service import Service as ChromeService
from selenium.webdriver.edge.service import Service as EdgeService
from selenium.webdriver.common.action_chains import ActionChains
from datetime import datetime
import time
import requests
import base64

# MySQL数据库连接配置
def get_db_connection():
    try:
        connection = mysql.connector.connect(
            host='localhost',  # 数据库主机
            database='bs_shopcompare',  # 数据库名称
            user='root',  # 数据库用户名
            password='64793416zhu'  # 数据库密码
        )
        if connection.is_connected():
            print("成功连接到数据库")
            return connection
    except mysql.connector.Error as e:
        print("数据库连接失败：", e)
        return None

# 插入品类数据到category表
def insert_category(connection, name, parent_id=None):
    cursor = connection.cursor()
    insert_query = """
    INSERT INTO category (name, parent_id)
    VALUES (%s, %s)
    """
    values = (name, parent_id)
    cursor.execute(insert_query, values)
    connection.commit()
    return cursor.lastrowid  # 返回插入的品类id

# 插入商品信息到product表
def insert_product(connection, name, category, platform, shop_url, img_data):
    cursor = connection.cursor()
    insert_query = """
    INSERT INTO product (name, category, platform, created_at, shop_url, img)
    VALUES (%s, %s, %s, %s, %s, %s)
    """
    values = (name, category, platform, datetime.now(), shop_url, img_data)
    cursor.execute(insert_query, values)
    connection.commit()
    return cursor.lastrowid  # 返回插入的产品id

# 插入价格历史记录到product_history表
def insert_product_history(connection, product_id, price):
    cursor = connection.cursor()
    insert_query = """
    INSERT INTO product_history (product_id, price, created_at)
    VALUES (%s, %s, %s)
    """
    values = (product_id, price, datetime.now())
    cursor.execute(insert_query, values)
    connection.commit()

# 检查并插入价格历史，避免重复插入相同价格
def check_and_insert_price(connection, product_id, price):
    cursor = connection.cursor()

    # 查找当前产品最新的价格
    query = "SELECT price FROM product_history WHERE product_id = %s ORDER BY created_at DESC LIMIT 1"
    cursor.execute(query, (product_id,))
    last_price = cursor.fetchone()

    if last_price and float(last_price[0]) != price:
        # 价格不同，插入新记录
        insert_product_history(connection, product_id, price)
        print("价格变动，记录新的历史价格")
    else:
        print("价格未变动，跳过插入")

# 获取品类路径
def get_category_path(category_id, connection):
    cursor = connection.cursor()
    path = []

    # 获取品类信息
    while category_id:
        cursor.execute("SELECT id, name, parent_id FROM category WHERE id = %s", (category_id,))
        category = cursor.fetchone()

        if category:
            path.insert(0, category[1])  # 插入品类名称到路径
            category_id = category[2]  # 更新为父品类id
        else:
            break

    return ' > '.join(path)

# 设置 Edge 浏览器驱动路径
driver_path = "C:\\Program Files (x86)\\Microsoft\\Edge\\Application\\msedgedriver.exe"

# 使用 Selenium 获取商品详情
def fetch_product_details(driver, product_url):
    driver.get(product_url)
    time.sleep(3)  # 等待页面加载

    # 获取商品的品类路径
    category_elements = driver.find_elements(By.CSS_SELECTOR, '.breadcrumb .item')
    category_path = ' > '.join([elem.text.strip() for elem in category_elements if elem.text.strip()])

    # 获取商品的图片
    img_element = driver.find_element(By.ID, 'spec-img')
    img_url = img_element.get_attribute('src')

    # 下载图片
    img_data = None
    if img_url:
        img_data = requests.get(img_url).content

    # 获取商品价格
    try:
        price = driver.find_element(By.CSS_SELECTOR, '.price .priceJ').text.strip().replace('¥', '')
    except:
        price = '0.00'

    return category_path, img_data, price

# 爬取京东商品信息并存入数据库
def fetch_search_results(search_query):
    # 打开京东首页
    driver = webdriver.Edge()
    driver.get("https://www.jd.com/")

    # 如果需要登录操作，模拟登录
    try:
        login_button = driver.find_element(By.CLASS_NAME, 'link-login')
        login_button.click()
        time.sleep(3)

        # 输入登录信息（用户名和密码）
        username = driver.find_element(By.ID, 'loginname')
        password = driver.find_element(By.ID, 'nloginpwd')
        username.send_keys("13107789236")  # 替换为你的用户名
        password.send_keys("64793416zhu")  # 替换为你的密码
        password.send_keys(Keys.RETURN)
        time.sleep(5)
    except Exception as e:
        print("无需登录或登录失败，继续爬取：", e)

    # 输入搜索关键词
    search_box = driver.find_element(By.ID, "key")
    search_box.send_keys(search_query)
    search_box.send_keys(Keys.RETURN)  # 模拟回车键
    time.sleep(3)  # 等待搜索页面加载

    # 获取商品列表
    items = driver.find_elements(By.CSS_SELECTOR, '.gl-i-wrap')[:10]  # 取前10个商品

    connection = get_db_connection()  # 连接数据库
    if not connection:
        driver.quit()
        return

    for item in items:
        # 提取商品名称、价格和链接
        name = item.find_element(By.CSS_SELECTOR, '.p-name').text.strip()
        shop_url = item.find_element(By.CSS_SELECTOR, 'a').get_attribute('href')

        # 获取商品详情信息（品类、图片、价格）
        category, img_data, price = fetch_product_details(driver, shop_url)

        # 插入商品信息
        product_id = insert_product(connection, name, category, '京东', shop_url, img_data)

        # 检查价格并插入历史记录
        check_and_insert_price(connection, product_id, float(price))

        print(f"商品 {name} 已存入数据库，价格: ¥{price}")

        # 返回到搜索结果页面继续处理下一个商品
        driver.back()
        time.sleep(2)  # 等待页面加载

    connection.close()  # 关闭数据库连接
    driver.quit()  # 关闭 Selenium WebDriver

# 主程序
if __name__ == "__main__":
    search_query = input("请输入要搜索的商品：")
    fetch_search_results(search_query)
