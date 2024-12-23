package com.zjubs.shopcompare.repository;

import com.zjubs.shopcompare.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {

    // 查询按创建时间降序排列的前2条记录
    List<Product> findTop2ByOrderByCreatedAtDesc();
}
