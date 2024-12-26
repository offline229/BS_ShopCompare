package com.zjubs.shopcompare.service;

import com.zjubs.shopcompare.model.PriceAlert;
import com.zjubs.shopcompare.repository.PriceAlertRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PriceAlertService {

    @Autowired
    private PriceAlertRepository priceAlertRepository;

    // 设置商品价格提醒
    public PriceAlert setPriceAlert(int userId, int productId) {
        PriceAlert priceAlert = new PriceAlert();
        priceAlert.setUserId(userId);
        priceAlert.setProductId(productId);

        // 将提醒记录保存到数据库
        return priceAlertRepository.save(priceAlert);
    }
}
