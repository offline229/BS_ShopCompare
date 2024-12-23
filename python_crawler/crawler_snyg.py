import requests
from bs4 import BeautifulSoup
import re
import json

# 设置请求头，模拟浏览器请求
headers = {
    "User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36"
}

def get_suning_results(query):
    # 构造搜索 URL，商品名需要进行 URL 编码
    search_url = f"https://search.suning.com/{query}/"

    response = requests.get(search_url, headers=headers)

    # 检查请求是否成功
    if response.status_code != 200:
        print(f"Error: {response.status_code}")
        return []

    # 使用正则表达式从页面中提取商品数据
    page_content = response.text
    products = []

    # 从页面的 <script> 标签中提取商品数据（在苏宁易购页面上，商品信息通常嵌入在 script 标签中）
    try:
        # 通过正则表达式找出包含商品数据的部分
        json_data_match = re.search(r'window\.jsonData = ({.*?});', page_content, re.S)
        if json_data_match:
            json_data = json.loads(json_data_match.group(1))
            items = json_data['products']['product']

            # 输出完整的商品数据（用于调试）
            print(json.dumps(json_data, indent=4, ensure_ascii=False))

            for item in items:
                product_name = item['productName']
                product_price = item['priceInfo']['salePrice']
                products.append({"name": product_name, "price": product_price})
    except Exception as e:
        print(f"Error extracting product data: {e}")

    return products

# 用户输入商品名称
query = input("请输入商品名称：")

# 获取搜索结果
results = get_suning_results(query)

# 输出搜索结果
if results:
    print(f"搜索到的商品：")
    for i, result in enumerate(results, 1):
        print(f"{i}. {result['name']} - ￥{result['price']}")
else:
    print("没有找到相关商品。")
