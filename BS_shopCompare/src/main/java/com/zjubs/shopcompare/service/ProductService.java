package com.zjubs.shopcompare.service;

import com.zjubs.shopcompare.model.Product;
import com.zjubs.shopcompare.model.PriceHistory;
import com.zjubs.shopcompare.repository.ProductRepository;
import com.zjubs.shopcompare.repository.PriceHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private PriceHistoryRepository priceHistoryRepository;

    public List<Product> getLatestProductsWithPrice() {
        // 查询最新的前2条商品
        List<Product> products = productRepository.findTop2ByOrderByCreatedAtDesc();

        // 为每个商品添加最新价格
        for (Product product : products) {
            List<PriceHistory> priceHistoryList = priceHistoryRepository.findTop1ByProductIdOrderByCreatedAtDesc(product.getId());
            if (!priceHistoryList.isEmpty()) {
                // 设置最新价格
                product.setLatestPrice(priceHistoryList.get(0).getPrice());
            }
        }

        return products;
    }
}
