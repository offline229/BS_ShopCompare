package com.zjubs.shopcompare.util;
import java.util.Random;

public class CaptchaUtil {

    // 生成6位随机验证码
    public static String generateCaptcha() {
        Random random = new Random();
        StringBuilder captcha = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            captcha.append(random.nextInt(10));  // 生成 0-9 之间的数字
        }
        return captcha.toString();
    }
}
