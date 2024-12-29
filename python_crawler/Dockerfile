# 使用 Python 官方的轻量级镜像
FROM python:3.12-slim

# 设置工作目录
WORKDIR /app

# 复制 requirements.txt 文件到容器中
COPY ./requirements.txt /app/requirements.txt

# 安装 Python 项目的依赖
RUN pip install --no-cache-dir -r requirements.txt

# 将爬虫代码复制到容器中
COPY ./python_crawler /app

# 启动爬虫脚本
CMD ["python", "htmp_crawl.py"]

EXPOSE 8080
