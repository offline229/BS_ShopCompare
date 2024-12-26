package com.zjubs.shopcompare.repository;

import com.zjubs.shopcompare.model.PriceAlert;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PriceAlertRepository extends JpaRepository<PriceAlert, Integer> {
}
