from flask import Flask, request, jsonify
from crawler_snyg import fetch_product_details  # 引入爬虫函数

app = Flask(__name__)


@app.route('/crawl', methods=['POST'])
def crawl():
    # 从 POST 请求中获取关键词
    data = request.get_json()
    keyword = data.get('keyword')

    if not keyword:
        return jsonify({'status': 'error', 'message': 'No keyword provided'}), 400

    try:
        # 调用爬虫函数获取数据
        result = fetch_product_details(keyword)

        # 如果爬取成功，返回 OK 信号
        if result:
            return jsonify({'status': 'ok', 'message': 'Data crawled successfully'})
        else:
            return jsonify({'status': 'error', 'message': 'No data found'}), 404

    except Exception as e:
        return jsonify({'status': 'error', 'message': str(e)}), 500


if __name__ == '__main__':
    app.run(debug=True, port=5000)  # 运行在5000端口