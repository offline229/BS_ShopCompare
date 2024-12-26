package com.zjubs.shopcompare.service;

import com.zjubs.shopcompare.model.Product;
import com.zjubs.shopcompare.model.PriceHistory;
import com.zjubs.shopcompare.repository.ProductRepository;
import com.zjubs.shopcompare.repository.PriceHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private PriceHistoryRepository priceHistoryRepository;

    /**
     * 获取最新商品数据（分页）并包含最新价格
     * @param page 页码，从0开始
     * @param size 每页的商品数量
     * @return 商品列表
     */
    public List<Product> getLatestProductsWithPrice(int page, int size) {
        // 创建分页请求对象
        Pageable pageable = PageRequest.of(page, size);

        // 查询商品数据（分页）
        Page<Product> productPage = productRepository.findAllByOrderByCreatedAtDesc(pageable);

        // 为每个商品添加最新价格
        List<Product> productsWithPrice = productPage.getContent().stream().map(product -> {
            // 获取该商品的最新价格
            List<PriceHistory> priceHistoryList = priceHistoryRepository.findTop1ByProductIdOrderByCreatedAtDesc(product.getId());
            if (!priceHistoryList.isEmpty()) {
                // 设置商品的最新价格
                product.setLatestPrice(priceHistoryList.get(0).getPrice());
            }
            return product;
        }).collect(Collectors.toList());

        return productsWithPrice;
    }
    /**
     * 获取商品的总条目数
     * @return 商品的总数量
     */
    public long getTotalProductCount() {
        return productRepository.count();  // 返回商品的总数量
    }

    // 获取商品的历史价格数据
    public List<PriceHistory> getProductHistoryPrice(int productId) {
        // 查询数据库中对应商品 ID 的历史价格
        return priceHistoryRepository.findByProductId(productId);
    }
}
