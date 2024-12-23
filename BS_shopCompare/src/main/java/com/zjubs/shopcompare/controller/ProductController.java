package com.zjubs.shopcompare.controller;

import com.zjubs.shopcompare.model.Product;
import com.zjubs.shopcompare.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    // 定义日志记录器
    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private ProductService productService;

    // 获取最新商品数据的接口
    @GetMapping("/latest")
    public List<Product> getLatestProducts() {
        // 记录收到请求的日志
        logger.info("收到商品请求：查询最新商品数据");

        // 获取最新商品数据（包括价格）
        List<Product> products = productService.getLatestProductsWithPrice();

        // 输出查询结果的日志
        for (Product product : products) {
            logger.info("商品ID: {}, 名称: {}, 类别: {}, 平台: {}, 创建时间: {}, 商店网址: {}, 最新价格: {}",
                    product.getId(),
                    product.getName(),
                    product.getCategory(),
                    product.getPlatform(),
                    product.getCreatedAt(),
                    product.getShopUrl(),
                    product.getLatestPrice());
        }

        logger.info("查询到的商品数量: {}", products.size());

        return products;
    }
}
