package com.zjubs.shopcompare.model;

public class PriceAlertWithProductInfo {
    private PriceAlert priceAlert;
    private Product product;

    public PriceAlertWithProductInfo(PriceAlert priceAlert, Product product) {
        this.priceAlert = priceAlert;
        this.product = product;
    }

    // Getters and Setters
    public PriceAlert getPriceAlert() {
        return priceAlert;
    }

    public void setPriceAlert(PriceAlert priceAlert) {
        this.priceAlert = priceAlert;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}