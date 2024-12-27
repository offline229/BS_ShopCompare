import os
import logging
import mysql.connector
import requests
from bs4 import BeautifulSoup
from urllib.parse import urljoin
from datetime import datetime
from mysql.connector import Error
from PIL import Image
from io import BytesIO
from selenium import webdriver
from selenium.webdriver import Keys
from selenium.webdriver.edge.service import Service
from selenium.webdriver.edge.options import Options
from selenium.webdriver.common.by import By
import time
from urllib.parse import urljoin, urlparse, parse_qs

# 配置日志
logging.basicConfig(level=logging.INFO, format='%(asctime)s - %(levelname)s - %(message)s')

# MySQL 数据库连接配置
db_config = {
    'host': 'localhost',         # MySQL 主机
    'user': 'root',              # 用户名
    'password': '64793416zhu',   # 密码
    'database': 'bs_shopcompare' # 数据库名
}

# 定义文件保存目录
UPLOAD_DIR = r'C:\ProgramData\MySQL\MySQL Server 8.0\Uploads'

# 配置Selenium WebDriver为Edge
def get_driver():
    edge_driver_path = "C:\\Program Files (x86)\\Microsoft\\Edge\\Application\\msedgedriver.exe"
    service = Service(edge_driver_path)
    edge_options = Options()
    edge_options.add_argument('--headless')  # 启用无头模式
    edge_options.add_argument('--disable-gpu')  # 禁用 GPU 加速
    edge_options.add_argument('--no-sandbox')  # 解决 Linux 系统兼容性问题

    # 创建 Edge WebDriver 实例并启用上述设置
    driver = webdriver.Edge(service=service, options=edge_options)
    return driver

# 下载图片并保存到指定目录，同时进行压缩（如果图片大于64KB）
def download_image(image_url, save_path):
    try:
        # 如果图片链接以 "//" 开头，补充 https: 前缀
        if image_url.startswith('//'):
            image_url = 'https:' + image_url

        logging.info(f"正在下载图片: {image_url}")
        response = requests.get(image_url, stream=True)
        response.raise_for_status()

        # 将图片数据加载到内存中
        img_data = BytesIO(response.content)

        # 打开图片
        with Image.open(img_data) as img:
            # 检查图片文件的大小（单位：字节）
            if len(response.content) > 64 * 1024:  # 如果图片大于64KB
                logging.info("图片大于64KB，正在压缩图片...")
                # 压缩图片：调整质量
                img = compress_image(img)

            # 确保目录存在
            os.makedirs(os.path.dirname(save_path), exist_ok=True)

            # 保存图片
            img.save(save_path)
            logging.info(f"图片已保存到: {save_path}")
            return save_path

    except requests.exceptions.RequestException as e:
        logging.error(f"下载图片失败: {e}")
        return None
    except Exception as e:
        logging.error(f"处理图片时发生错误: {e}")
        return None


# 压缩图片，调整质量直到小于 64KB
def compress_image(img, max_size=64 * 1024):
    # 设置初始质量（最大为100）
    quality = 90
    output_img = BytesIO()

    while True:
        # 保存压缩后的图片到内存
        img.save(output_img, format="JPEG", quality=quality)
        output_size = len(output_img.getvalue())

        # 如果图片小于目标大小，退出循环
        if output_size <= max_size or quality <= 10:
            break

        # 降低质量
        quality -= 5

        # 清空内存中的 BytesIO 对象，准备重新压缩
        output_img.seek(0)
        output_img.truncate(0)

    # 读取压缩后的图片
    output_img.seek(0)
    return Image.open(output_img)

# 连接数据库
def connect_db():
    try:
        conn = mysql.connector.connect(**db_config)
        if conn.is_connected():
            logging.info("成功连接到 MySQL 数据库")
            return conn
        else:
            logging.error("无法连接到数据库")
            return None
    except Error as e:
        logging.error(f"数据库连接失败: {e}")
        return None

# 查询商品是否已存在
def check_product_exists(conn, shop_url):
    cursor = conn.cursor()
    query = "SELECT id FROM product WHERE shop_url = %s"
    cursor.execute(query, (shop_url,))
    result = cursor.fetchone()
    cursor.close()
    if result:
        logging.info(f"商品已存在，URL: {shop_url}，商品ID: {result[0]}")
        return result[0]  # 返回商品ID
    else:
        logging.info(f"商品不存在，URL: {shop_url}")
        return None

# 插入新的商品
def insert_product(conn, name, category, shop_url, img_path, latest_price):
    cursor = conn.cursor()
    created_at = datetime.now()
    query = """
        INSERT INTO product (name, category, platform, created_at, shop_url, img, latest_price)
        VALUES (%s, %s, %s, %s, %s, %s, %s)
    """
    with open(img_path, 'rb') as img_file:
        img_blob = img_file.read()
        cursor.execute(query, (name, category, '苏宁易购', created_at, shop_url, img_blob, latest_price))  # 传递 latest_price
        conn.commit()
        logging.info(f"成功插入商品: {name}，URL: {shop_url}")
        return cursor.lastrowid  # 返回插入商品的ID

# 插入价格历史
def insert_price_history(conn, product_id, price):
    cursor = conn.cursor()
    created_at = datetime.now()
    query = "INSERT INTO price_history (product_id, price, created_at) VALUES (%s, %s, %s)"
    cursor.execute(query, (product_id, price, created_at))
    conn.commit()
    logging.info(f"插入价格历史，商品ID: {product_id}, 价格: {price}")

# 更新或插入价格历史
def handle_price_history(conn, product_id, new_price):
    cursor = conn.cursor()
    query = "SELECT price FROM price_history WHERE product_id = %s ORDER BY created_at DESC LIMIT 1"
    cursor.execute(query, (product_id,))
    result = cursor.fetchone()

    if result:
        last_price = result[0]
        if last_price != new_price:
            logging.info(f"价格变动，插入新价格记录: {new_price}")
            insert_price_history(conn, product_id, new_price)
        else:
            logging.info(f"价格未变动: {new_price}")
    else:
        logging.info(f"无历史价格记录，插入新价格记录: {new_price}")
        insert_price_history(conn, product_id, new_price)

# 爬取苏宁易购商品
def fetch_product_details(keyword):
    base_url = f"https://search.suning.com/{keyword}/"
    driver = get_driver()

    try:
        logging.info(f"正在发送请求，搜索关键词：{keyword}")
        driver.get(base_url)

        # 等待页面加载
        time.sleep(3)  # 等待页面加载

        # 执行滚动操作，模拟用户浏览更多商品
        body = driver.find_element(By.TAG_NAME, 'body')
        for _ in range(5):  # 滚动三次
            body.send_keys(Keys.PAGE_DOWN)
            time.sleep(2)

        # 获取页面的 HTML 内容
        soup = BeautifulSoup(driver.page_source, 'html.parser')

        # 找到商品列表，按 class="item-wrap" 筛选
        products = soup.find_all('li', class_='item-wrap')

        if not products:
            logging.warning("没有找到商品信息")
            return []

        logging.info(f"爬取到的商品总数: {len(products)}")
        product_data = []

        for i, product in enumerate(products):
            # 获取商品标题
            title_tag = product.find('div', class_='title-selling-point')
            title = title_tag.get_text(strip=True) if title_tag else '无标题'

            # 获取商品价格
            price_tag = product.find('span', class_='def-price')
            price = price_tag.get_text(strip=True) if price_tag else '无价格'

            # 如果价格为空，跳过当前商品
            if price == '':
                logging.info(f"商品 {i + 1}: 标题 - {title}，跳过没有价格的商品")
                continue
            # 获取商品图片链接
            img_tag = product.find('img')
            img_url = img_tag['src'] if img_tag else '无图片'

            # 获取商品详情页链接
            detail_tag = product.find('a')
            shop_url = urljoin(base_url, detail_tag['href']) if detail_tag else '无详情页链接'

            # 如果 shop_url 是类似 https://th.suning.com/calCpcClicks? 的链接，进行处理
            if 'calCpcClicks' in shop_url:
                # 解析 URL，提取 clickUrl 参数
                parsed_url = urlparse(shop_url)
                query_params = parse_qs(parsed_url.query)
                click_url = query_params.get('clickUrl', [None])[0]

                if click_url:
                    # 进一步解析 click_url 以提取最终商品页面的 URL
                    parsed_click_url = urlparse(click_url)
                    # 从 click_url 中提取商品 ID
                    path_parts = parsed_click_url.path.split('/')
                    if len(path_parts) >= 3:
                        product_id = path_parts[1]
                        item_code = path_parts[2].replace('.html', '')  # 去掉 .html 部分
                        shop_url = f"https://product.suning.com/{product_id}/{item_code}.html"
                    else:
                        shop_url = '无有效详情页链接'
                else:
                    shop_url = '无 clickUrl 参数'

            product_data.append({
                'name': title,
                'category': keyword,
                'shop_url': shop_url,
                'img_url': img_url,
                'price': price
            })

            logging.info(f"商品 {i + 1}: 标题 - {title}, 价格 - {price}, 图片链接 - {img_url}, 详情页链接 - {shop_url}")

        return product_data

    finally:
        driver.quit()

# 处理商品信息和价格更新
def process_product(conn, product_info):
    # 下载图片并保存
    img_filename = product_info['name'][:20] + '.jpg'  # 文件名限制长度
    img_path = os.path.join(UPLOAD_DIR, img_filename)
    downloaded_image_path = download_image(product_info['img_url'], img_path)

    if not downloaded_image_path:
        logging.error("图片下载失败，跳过该商品")
        return

    # 检查商品是否已存在
    product_id = check_product_exists(conn, product_info['shop_url'])

    if product_id is None:
        # 插入新的商品时，先处理价格
        # 处理价格，去掉 "¥" 和 "," 等非数字字符
        latest_price = product_info['price'].replace('¥', '').replace(',', '')  # 去除 "¥" 和 "," 符号
        try:
            latest_price = float(latest_price)  # 转换为浮动数值
        except ValueError:
            logging.warning(f"价格格式不正确: {product_info['price']}, 使用默认价格 0.0")
            latest_price = 0.0  # 如果转换失败，使用默认价格 0.0

        # 插入新的商品
        product_id = insert_product(conn, product_info['name'], product_info['category'], product_info['shop_url'], downloaded_image_path, latest_price)

    # 插入或更新价格历史
    if product_info['price'] != '无价格':
        try:
            price = float(product_info['price'].replace('¥', '').replace(',', ''))
            handle_price_history(conn, product_id, price)
        except ValueError:
            logging.warning(f"价格格式不正确: {product_info['price']}, 跳过价格处理")

    # 删除本地图片
    os.remove(downloaded_image_path)
    logging.info(f"删除本地图片: {downloaded_image_path}")

# 主函数，模拟爬取商品信息并处理
def main():
    # 示例查询关键词，可以根据需求更改
    keyword = input("请输入商品关键词（例如: 猫粮）: ").strip()

    # 连接数据库
    conn = connect_db()
    if conn is None:
        return

    try:
        # 爬取商品信息
        products = fetch_product_details(keyword)

        # 处理每个商品
        for product_info in products:
            process_product(conn, product_info)

    finally:
        # 关闭数据库连接
        if conn.is_connected():
            conn.close()
            logging.info("数据库连接已关闭")

if __name__ == '__main__':
    main()
