package com.zjubs.shopcompare.model;

import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    private String category;
    private String platform;

    @Column(name = "created_at")
    private Timestamp createdAt;

    private String shopUrl;
    private byte[] img;  // 图片是 BLOB 类型

    @Transient  // 不是数据库字段，用于返回的最新价格
    private double latestPrice;

    // getters and setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public String getShopUrl() {
        return shopUrl;
    }

    public void setShopUrl(String shopUrl) {
        this.shopUrl = shopUrl;
    }

    public byte[] getImg() {
        return img;
    }

    public void setImg(byte[] img) {
        this.img = img;
    }

    public double getLatestPrice() {
        return latestPrice;
    }

    public void setLatestPrice(double latestPrice) {
        this.latestPrice = latestPrice;
    }
}
