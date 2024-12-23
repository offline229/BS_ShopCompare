import requests
from bs4 import BeautifulSoup
import json
import pandas as pd

# 请求头
headers = {
    'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36'
}

# 存储商品信息的列表
data_list = []

# 模拟抓取 5 页商品数据
for page in range(1, 6):
    # 发送请求，获取页面 HTML
    url = f'https://search.jd.com/Search?keyword=耳机&enc=utf-8&page={page}'
    response = requests.get(url, headers=headers)

    # 解析 HTML 内容
    soup = BeautifulSoup(response.text, 'lxml')
    goods_list = soup.find_all('li', class_='gl-item')

    for item in goods_list:
        name = item.find('div', class_='p-name').text.strip()
        price = item.find('div', class_='p-price').text.strip()
        comment = item.find('div', class_='p-commit').text.strip()
        data_list.append({
            '商品名称': name,
            '价格': price,
            '评价数': comment
        })

# 将数据保存到 CSV 文件
df = pd.DataFrame(data_list)
df.to_csv('jd_earphone_data.csv', index=False, encoding='utf-8')
print('数据保存成功！')
