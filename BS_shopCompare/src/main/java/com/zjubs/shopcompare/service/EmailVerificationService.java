package com.zjubs.shopcompare.service;

import com.zjubs.shopcompare.util.EmailUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class EmailVerificationService {

    @Autowired
    private StringRedisTemplate redisTemplate; // 用于操作 Redis

    @Autowired
    private EmailUtil emailUtil; // 邮件发送工具类

    private static final long CAPTCHA_EXPIRATION_TIME = 5L; // 验证码有效期 5 分钟

    /**
     * 生成验证码并发送邮件
     */
    public String generateAndSendCaptcha(String email) {
        // 生成六位数的验证码
        String captcha = generateCaptcha();

        // 将验证码存入 Redis，键值为 email，值为 captcha，设置 5 分钟有效期
        redisTemplate.opsForValue().set(email, captcha, CAPTCHA_EXPIRATION_TIME, TimeUnit.MINUTES);

        try {
            // 发送验证码到用户邮箱
            String subject = "您的验证码";
            String text = "您的验证码是：" + captcha + "，有效期为 5 分钟。";
            emailUtil.sendEmail(email, subject, text);
        } catch (Exception e) {
            return "邮件发送失败：" + e.getMessage();
        }

        return "验证码已发送";
    }

    /**
     * 验证验证码
     */
    public String validateCaptcha(String email, String inputCaptcha) {
        // 从 Redis 获取验证码
        String storedCaptcha = redisTemplate.opsForValue().get(email);

        if (storedCaptcha == null) {
            return "验证码已过期或未发送";
        }

        if (!storedCaptcha.equals(inputCaptcha)) {
            return "验证码错误";
        }

        // 验证通过后，删除 Redis 中的验证码
        redisTemplate.delete(email);

        return "验证码验证成功";
    }

    /**
     * 生成六位数验证码
     */
    private String generateCaptcha() {
        Random random = new Random();
        int captcha = 100000 + random.nextInt(900000);  // 生成 6 位数验证码
        return String.valueOf(captcha);
    }
}
