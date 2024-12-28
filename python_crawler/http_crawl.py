from flask import Flask, request, jsonify
from crawler_snyg import crawl_interface_snyg  # 引入爬虫函数
from crawler_dd import crawl_interface_dd  # 引入爬虫函数

app = Flask(__name__)


@app.route('/crawl', methods=['POST'])
def crawl():
    # 从 POST 请求中获取关键词

    data = request.get_json()
    keyword = data.get('keyword')
    print("get request"+keyword)
    if not keyword:
        return jsonify({'status': 'error', 'message': 'No keyword provided'}), 400

    try:
        # 调用爬虫函数获取数据
        result1 = crawl_interface_snyg(keyword)
        result2 = crawl_interface_dd(keyword)
        # 如果爬取成功，返回 OK 信号
        if result1 and result2:
            return jsonify({'status': 'ok', 'message': 'Data crawled successfully'})
        else:
            return jsonify({'status': 'error', 'message': 'No data found'}), 404

    except Exception as e:
        return jsonify({'status': 'error', 'message': str(e)}), 500


if __name__ == '__main__':
    app.run(debug=True, port=8080)  # 端口号，确保这里没被占用
