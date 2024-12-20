package com.zjubs.shopcompare.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    @PostMapping("/login")
    public String login(@RequestBody LoginRequest loginRequest) {
        // 假设有一个LoginRequest类来接收前端的请求体
        System.out.println("收到的请求数据: ");
        System.out.println("Email: " + loginRequest.getEmail());
        System.out.println("Password: " + loginRequest.getPassword());
        logger.info("收到的请求：Email = " + loginRequest.getEmail() + ", Password = " + loginRequest.getPassword());
        // 此处进行认证（可以是数据库验证、JWT生成等）

        // 返回登录成功的响应
        return "登录成功";
    }
}
class LoginRequest {
    private String email;
    private String password;

    // Getter 和 Setter
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}