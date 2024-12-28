package com.zjubs.shopcompare.service;

import com.zjubs.shopcompare.model.Product;
import com.zjubs.shopcompare.model.PriceHistory;
import com.zjubs.shopcompare.repository.ProductRepository;
import com.zjubs.shopcompare.repository.PriceHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    public List<Product> getLatestProductsWithFilters(String searchQuery, Double priceMin, Double priceMax, String platform, String priceSort, int page, int size) {


        // 创建查询条件
        Specification<Product> specification = Specification.where(null);
        System.out.println("Initial Specification: " + specification);

        // 根据搜索关键词添加查询条件
        if (searchQuery != null && !searchQuery.trim().isEmpty()) {
            System.out.println("Adding search query filter: " + searchQuery);
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get("name"), "%" + searchQuery + "%"));
            System.out.println("Updated Specification after search query: " + specification);
        }


        // 根据平台添加查询条件
        if (platform != null && !platform.trim().isEmpty()) {
            System.out.println("Adding platform filter: " + platform);
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("platform"), platform));
            System.out.println("Updated Specification after platform: " + specification);
        }

        // 根据价格上下限添加查询条件
        if (priceMin != null) {
            System.out.println("Adding priceMin filter: " + priceMin);
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.greaterThanOrEqualTo(root.get("latestPrice"), priceMin));
            System.out.println("Updated Specification after priceMin: " + specification);
        }

        if (priceMax != null) {
            System.out.println("Adding priceMax filter: " + priceMax);
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.lessThanOrEqualTo(root.get("latestPrice"), priceMax));
            System.out.println("Updated Specification after priceMax: " + specification);
        }


        // 设置排序条件（按创建时间和价格排序）
        Sort sort = Sort.by(Sort.Order.desc("createdAt"));  // 默认按创建时间降序排序
        System.out.println("Initial Sort: " + sort);

        if ("asc".equalsIgnoreCase(priceSort)) {
            System.out.println("Changing sort to ascending price");
            sort = Sort.by(Sort.Order.asc("latestPrice"));  // 按价格升序排序
        } else if ("desc".equalsIgnoreCase(priceSort)) {
            System.out.println("Changing sort to descending price");
            sort = Sort.by(Sort.Order.desc("latestPrice"));  // 按价格降序排序
        }

        System.out.println("Final Sort: " + sort);

        // 创建分页请求对象，包含排序条件
        Pageable pageableRequest = PageRequest.of(page-1, size, sort);
        System.out.println("Created Pageable: " + pageableRequest);

        // 使用 Specification 和分页查询商品数据
        Page<Product> productPage = null;
        try {
            productPage = productRepository.findAll(specification, pageableRequest);
        } catch (Exception e) {
            System.out.println("Error executing query: " + e.getMessage());
            e.printStackTrace();
        }

        // 获取查询结果
        if (productPage != null) {
            List<Product> products = productPage.getContent();
            System.out.println("Number of products found: " + products.size());
            return products;
        } else {
            System.out.println("No products found or an error occurred.");
            return new ArrayList<>();  // 返回空列表
        }
    }


    public long getProductCountWithFilters(String searchQuery, Double priceMin, Double priceMax, String platform) {

        // 创建查询条件
        Specification<Product> specification = Specification.where(null);
        System.out.println("Initial Specification: " + specification);

        // 根据搜索关键词添加查询条件
        if (searchQuery != null && !searchQuery.trim().isEmpty()) {
            System.out.println("Adding search query filter: " + searchQuery);
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get("name"), "%" + searchQuery + "%"));
            System.out.println("Updated Specification after search query: " + specification);
        }

        // 根据平台添加查询条件
        if (platform != null && !platform.trim().isEmpty()) {
            System.out.println("Adding platform filter: " + platform);
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("platform"), platform));
            System.out.println("Updated Specification after platform: " + specification);
        }

        // 根据价格上下限添加查询条件
        if (priceMin != null) {
            System.out.println("Adding priceMin filter: " + priceMin);
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.greaterThanOrEqualTo(root.get("latestPrice"), priceMin));
            System.out.println("Updated Specification after priceMin: " + specification);
        }

        if (priceMax != null) {
            System.out.println("Adding priceMax filter: " + priceMax);
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.lessThanOrEqualTo(root.get("latestPrice"), priceMax));
            System.out.println("Updated Specification after priceMax: " + specification);
        }

        // 使用 Specification 查询商品数据总数
        long totalCount = 0;
        try {
            totalCount = productRepository.count(specification);  // 使用 Specification 来查询符合条件的记录总数
            System.out.println("Total count of products found: " + totalCount);
        } catch (Exception e) {
            System.out.println("Error executing count query: " + e.getMessage());
            e.printStackTrace();
        }

        // 返回找到的商品总数
        return totalCount;
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
