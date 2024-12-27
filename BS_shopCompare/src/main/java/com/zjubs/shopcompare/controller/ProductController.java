package com.zjubs.shopcompare.controller;

import com.zjubs.shopcompare.model.PriceHistory;
import com.zjubs.shopcompare.model.Product;
import com.zjubs.shopcompare.service.ProductService;
import com.zjubs.shopcompare.service.PythonSpiderService;
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

    @Autowired
    private PythonSpiderService pythonSpiderService;

    // 获取首页的数据的接口，支持分页
    @PostMapping("/api/products/shop_display")
    public Map<String, Object> getLatestProducts(@RequestBody ProductRequest request) {
        // 记录收到请求的日志，输出分页参数
        logger.info("收到商品请求：查询最新商品数据，页码：{}, 每页数量：{}", request.getPage(), request.getLimit());
        // 调用服务层获取分页后的商品数据
        List<Product> products = productService.getLatestProductsWithFilters(
                request.getSearchQuery(),   // 搜索关键词
                request.getPriceMin(),      // 最低价格
                request.getPriceMax(),      // 最高价格
                request.getPlatform(),      // 平台
                request.getPriceSort(),     // 价格排序（升序或降序）
                request.getPage(),         // 页码
                request.getLimit()         // 每页数量
        );

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



    // 获取某个商品的历史价格
    @PostMapping("/api/products/history_price")
    public Map<String, Object> getProductHistoryPrice(@RequestBody ProductHistoryRequest request) {
        // 记录收到请求的日志，输出商品ID
        logger.info("收到商品请求：查询商品历史价格，商品ID：{}", request.getProductId());

        // 调用服务层获取商品的历史价格数据
        List<PriceHistory> historyPrices = productService.getProductHistoryPrice(request.getProductId());

        // 输出查询结果的日志
        for (PriceHistory history : historyPrices) {
            logger.info("商品ID: {}, 日期: {}, 历史价格: {}",
                    history.getProduct().getId(),
                    history.getCreatedAt(),
                    history.getPrice());
        }

        logger.info("查询到的历史价格记录数量: {}", historyPrices.size());

        // 将历史价格列表封装到一个Map中返回
        Map<String, Object> response = new HashMap<>();
        response.put("historyPrices", historyPrices);

        return response;  // 返回Map类型，包含历史价格记录
    }




}


class SearchProductRequest {

    private String keyword; // 搜索字段
    private int page; // 页码
    private int limit; // 每页数量

    // Getters and Setters
    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

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


class ProductRequest {

    private int page;
    private int limit;
    private String searchQuery;  // 搜索关键字
    private Double priceMin;     // 最低价格
    private Double priceMax;     // 最高价格
    private String platform;     // 平台
    private String priceSort;    // 价格排序（升序或降序）

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

    public String getSearchQuery() {
        return searchQuery;
    }

    public void setSearchQuery(String searchQuery) {
        this.searchQuery = searchQuery;
    }

    public Double getPriceMin() {
        return priceMin;
    }

    public void setPriceMin(Double priceMin) {
        this.priceMin = priceMin;
    }

    public Double getPriceMax() {
        return priceMax;
    }

    public void setPriceMax(Double priceMax) {
        this.priceMax = priceMax;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getPriceSort() {
        return priceSort;
    }

    public void setPriceSort(String priceSort) {
        this.priceSort = priceSort;
    }
}


class ProductHistoryRequest {

    private int productId;  // 商品 ID

    // Getter 和 Setter
    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }
}
