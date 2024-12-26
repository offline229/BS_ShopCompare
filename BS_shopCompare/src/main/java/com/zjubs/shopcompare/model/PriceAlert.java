package com.zjubs.shopcompare.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class PriceAlert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int userId;  // 用户 ID
    private int productId;  // 商品 ID
    private double priceThreshold;  // 价格阈值

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public double getPriceThreshold() {
        return priceThreshold;
    }

    public void setPriceThreshold(double priceThreshold) {
        this.priceThreshold = priceThreshold;
    }
}
