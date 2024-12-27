package com.zjubs.shopcompare.service;

import com.zjubs.shopcompare.model.PriceAlert;
import com.zjubs.shopcompare.model.User;
import com.zjubs.shopcompare.repository.PriceAlertRepository;
import com.zjubs.shopcompare.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;
import com.zjubs.shopcompare.model.PriceHistory;
import com.zjubs.shopcompare.model.Product;
import com.zjubs.shopcompare.repository.PriceHistoryRepository;
import com.zjubs.shopcompare.repository.ProductRepository;
import com.zjubs.shopcompare.util.EmailUtil;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;
import java.util.Optional;

@EnableScheduling  // 启用定时任务

@Service
public class PriceAlertService {

    @Autowired
    private PriceAlertRepository priceAlertRepository;

    @Autowired
    private PriceHistoryRepository priceHistoryRepository;  // 注入 PriceHistoryRepository

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;


    @Autowired
    private EmailUtil emailUtil;

    // 设置商品价格提醒
    public PriceAlert setPriceAlert(int userId, int productId) {
        // 创建 PriceAlert 对象
        PriceAlert priceAlert = new PriceAlert();
        priceAlert.setUserId(userId);
        priceAlert.setProductId(productId);

        // 根据商品ID查找商品信息
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found for the given ID"));

        // 获取商品的最新价格（从 Product 表中获取）
        double latestPrice = product.getLatestPrice();
        priceAlert.setPriceThreshold(latestPrice);

        // 将提醒记录保存到数据库
        return priceAlertRepository.save(priceAlert);
    }
    // 定时任务：每隔一段时间检查价格提醒
    @Scheduled(fixedRate = 3600000) // 执行的时间间隔，单位ms
    public void checkPriceAlerts() {
        System.out.println("开始定时检查");
        // 获取所有未处理的价格提醒
        List<PriceAlert> priceAlerts = priceAlertRepository.findAll();

        for (PriceAlert priceAlert : priceAlerts) {
            int productId = priceAlert.getProductId();
            int userId = priceAlert.getUserId();
            double priceThreshold = priceAlert.getPriceThreshold();

            // 获取商品的最新价格
            List<PriceHistory> latestPriceHistories = priceHistoryRepository.findTop1ByProductIdOrderByCreatedAtDesc(productId);

            if (latestPriceHistories != null && !latestPriceHistories.isEmpty()) {
                // 获取最新价格
                PriceHistory latestPriceHistory = latestPriceHistories.get(0);
                double latestPrice = latestPriceHistory.getPrice();


                // 获取商品名称
                Optional<Product> productOptional = productRepository.findById(productId);  // 查询产品
                String productName = "";
                if (productOptional.isPresent()) {
                    productName = productOptional.get().getName();  // 获取商品名称
                }
                // 如果最新价格低于阈值，发送邮件提醒
                if (latestPrice < priceThreshold) {
                    Optional<User> userOptional = userRepository.findById((long) userId);
                    if (userOptional.isPresent()) {
                        User user = userOptional.get();
                        String subject = "商品价格降价提醒";
                        String text = "您好，您特别关心的商品 " + productName + " 的价格已降至 " + latestPrice + "，低于您上次看到的价格： " + priceThreshold + "。";

                        try {
                            // 发送邮件
                            emailUtil.sendEmail(user.getEmail(), subject, text);
                            System.out.println("价格提醒已发送至用户邮箱：" + user.getEmail());
                            // 发送邮件后，更新价格阈值为最新价格
                            priceAlert.setPriceThreshold(latestPrice); // 更新阈值
                            priceAlertRepository.save(priceAlert);    // 保存更新后的价格提醒
                            System.out.println("价格提醒后，阈值已更新为最新价格：" + latestPrice);

                        } catch (Exception e) {
                            System.out.println("发送邮件失败：" + e.getMessage());
                        }

                    }
                }
            }
        }
    }


    // 获取用户的提醒记录
    public List<PriceAlert> getPriceAlertsForUser(int userId) {
        System.out.println("检查 getPriceAlertsForUser 方法，传入的 userId: " + userId);

        // 调用 repository 方法并获取结果
        List<PriceAlert> alerts = priceAlertRepository.findAlertsByUserId(userId);

        // 打印查询结果
        if (alerts != null && !alerts.isEmpty()) {
            System.out.println("找到的提醒记录数量: " + alerts.size());
            for (PriceAlert alert : alerts) {
                System.out.println("提醒记录 - ID: " + alert.getId() + ", 用户ID: " + alert.getUserId() +
                        ", 商品ID: " + alert.getProductId() +
                        ", 价格阈值: " + alert.getPriceThreshold());
            }
        } else {
            System.out.println("没有找到任何提醒记录.");
        }

        return alerts;
    }
    // 删除提醒记录
    public boolean deletePriceAlert(int Id) {
        // 删除记录的逻辑
        int deletedCount = priceAlertRepository.deleteById(Id);
        return deletedCount > 0;
    }
}