package com.zjubs.shopcompare.repository;

import com.zjubs.shopcompare.model.PriceAlert;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PriceAlertRepository extends JpaRepository<PriceAlert, Integer> {
    // 根据用户ID查询提醒记录
    List<PriceAlert> findAlertsByUserId(int userId);

    // 根据用户ID和提醒ID删除记录
    void deleteById(int Id);
}
