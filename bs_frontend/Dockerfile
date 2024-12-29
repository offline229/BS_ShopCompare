# Step 1: 使用 Node.js 镜像来构建前端项目
FROM node:18 AS build

# 设置工作目录为 /app
WORKDIR /app

# 将 package.json 和 package-lock.json 复制到容器中
COPY ./bs_frontend/package*.json ./

# 安装项目依赖
RUN npm install

# 将前端源代码复制到容器中
COPY ./bs_frontend /app

# 使用 Vite 进行项目构建，输出文件到 dist 文件夹
RUN npm run build

# Step 2: 使用 Nginx 镜像提供静态文件
FROM nginx:alpine

# 删除默认的 Nginx 配置文件（可选）
RUN rm -rf /usr/share/nginx/html/*

# 将构建后的 dist 文件夹复制到 Nginx 默认的网页目录
COPY --from=build /app/dist /usr/share/nginx/html

# 暴露 80 端口
EXPOSE 80

# 启动 Nginx 服务
CMD ["nginx", "-g", "daemon off;"]
