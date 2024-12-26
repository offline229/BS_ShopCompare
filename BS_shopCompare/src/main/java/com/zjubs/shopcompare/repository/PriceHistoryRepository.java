package com.zjubs.shopcompare.repository;

import com.zjubs.shopcompare.model.PriceHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PriceHistoryRepository extends JpaRepository<PriceHistory, Integer> {

    // 查询某个商品的最新价格
    List<PriceHistory> findTop1ByProductIdOrderByCreatedAtDesc(int productId);

    List<PriceHistory> findByProductId(int product_id);
}
