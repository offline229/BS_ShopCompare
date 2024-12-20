package com.zjubs.shopcompare.controller;

import com.zjubs.shopcompare.service.EmailVerificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/email")
public class EmailController {

    @Autowired
    private EmailVerificationService emailVerificationService;

    /**
     * 发送验证码
     */
    @PostMapping("/send")
    public String sendCaptcha(@RequestParam String email) {
        return emailVerificationService.generateAndSendCaptcha(email);
    }

    /**
     * 验证验证码
     */
    @PostMapping("/validate")
    public String validateCaptcha(@RequestParam String email, @RequestParam String captcha) {
        return emailVerificationService.validateCaptcha(email, captcha);
    }
}
