package com.zjubs.shopcompare.service;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.concurrent.TimeUnit;

@Service
public class RedisService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    // 存储验证码
    public void saveCaptcha(String email, String captcha) {
        // 存储验证码，并设置 5 分钟有效期
        redisTemplate.opsForValue().set(email, captcha, 5, TimeUnit.MINUTES);
    }

    // 获取验证码
    public String getCaptcha(String email) {
        return redisTemplate.opsForValue().get(email);
    }

    // 删除验证码（可选）
    public void deleteCaptcha(String email) {
        redisTemplate.delete(email);
    }
}
