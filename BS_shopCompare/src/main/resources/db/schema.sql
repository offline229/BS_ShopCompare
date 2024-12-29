CREATE DATABASE IF NOT EXISTS bs_shopcompare;
USE bs_shopcompare;

-- 创建 product 表
CREATE TABLE IF NOT EXISTS product (
                                       id INT AUTO_INCREMENT PRIMARY KEY,            -- 自增主键
                                       name VARCHAR(255) NOT NULL,                    -- 商品名称
                                       category VARCHAR(255) NOT NULL,                -- 商品品类
                                       platform VARCHAR(100) NOT NULL,                -- 商品平台
                                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- 创建时间，默认当前时间
                                       shop_url VARCHAR(255) NOT NULL,                -- 商店 URL
                                       img BLOB,                                      -- 商品图片 (BLOB 类型用于存储二进制数据)
                                       latest_price DECIMAL(10, 2) NOT NULL,          -- 商品最新价格
                                       CONSTRAINT chk_price CHECK (latest_price >= 0) -- 价格非负的约束
);

-- 创建 price_history 表
CREATE TABLE IF NOT EXISTS price_history (
                                             id INT AUTO_INCREMENT PRIMARY KEY,          -- 自增主键
                                             product_id INT,                             -- 商品 ID（外键）
                                             price DECIMAL(10, 2) NOT NULL,              -- 历史价格
                                             created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- 创建时间，默认当前时间
                                             FOREIGN KEY (product_id) REFERENCES product(id) ON DELETE CASCADE, -- 外键，删除商品时删除历史记录
                                             CONSTRAINT chk_price_history CHECK (price >= 0) -- 历史价格非负的约束
);

-- 创建 category 表
CREATE TABLE IF NOT EXISTS category (
                                        id INT AUTO_INCREMENT PRIMARY KEY,          -- 自增主键
                                        name VARCHAR(255) NOT NULL,                  -- 品类名称
                                        parent_id INT DEFAULT NULL,                  -- 父品类 ID（如果是顶级品类则为 NULL）
                                        FOREIGN KEY (parent_id) REFERENCES category(id) ON DELETE CASCADE -- 自引用外键，删除父品类时删除子品类
);

-- 创建 user 表
CREATE TABLE IF NOT EXISTS user (
                                    id INT AUTO_INCREMENT PRIMARY KEY,          -- 自增主键
                                    username VARCHAR(255) NOT NULL,              -- 用户名
                                    email VARCHAR(255) NOT NULL,                 -- 邮箱
                                    password VARCHAR(255) NOT NULL               -- 密码
);

-- 创建 price_alert 表
CREATE TABLE IF NOT EXISTS price_alert (
                                           id INT AUTO_INCREMENT PRIMARY KEY,          -- 自增主键
                                           user_id INT,                                -- 用户 ID（外键）
                                           product_id INT,                             -- 商品 ID（外键）
                                           price_threshold INT NOT NULL,               -- 价格阈值
                                           FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE, -- 外键，删除用户时删除该用户的价格提醒
                                           FOREIGN KEY (product_id) REFERENCES product(id) ON DELETE CASCADE -- 外键，删除商品时删除该商品的价格提醒
);

