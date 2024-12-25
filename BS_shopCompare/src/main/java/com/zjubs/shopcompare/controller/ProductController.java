package com.zjubs.shopcompare.controller;

import com.zjubs.shopcompare.model.Product;
import com.zjubs.shopcompare.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class ProductController {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private ProductService productService;

    // 获取最新商品数据的接口，支持分页
    @PostMapping("/api/products/latest")
    public Map<String, Object> getLatestProducts(@RequestBody ProductRequest request) {
        // 记录收到请求的日志，输出分页参数
        logger.info("收到商品请求：查询最新商品数据，页码：{}, 每页数量：{}", request.getPage(), request.getLimit());

        // 调用服务层获取分页后的商品数据
        List<Product> products = productService.getLatestProductsWithPrice(request.getPage(), request.getLimit());

        // 获取商品的总数量
        long totalCount = productService.getTotalProductCount();

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

        // 将商品列表和总数量封装到一个Map中返回
        Map<String, Object> response = new HashMap<>();
        response.put("products", products);
        response.put("totalCount", totalCount);

        return response;  // 返回Map类型，包含商品列表和总数量
    }
}
class ProductRequest {

    private int page;
    private int limit;

    // Getter 和 Setter
    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }
}