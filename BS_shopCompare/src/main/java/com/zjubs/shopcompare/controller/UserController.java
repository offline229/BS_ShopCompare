package com.zjubs.shopcompare.controller;

import com.zjubs.shopcompare.model.User;
import com.zjubs.shopcompare.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserRepository userRepository;  // 用于查询用户信息

    @Autowired
    private BCryptPasswordEncoder passwordEncoder; // 用于密码加密和验证

    @PostMapping("/login")
    public String login(@RequestBody LoginRequest loginRequest) {
        // 1. 验证邮箱是否存在
        User user = userRepository.findByEmail(loginRequest.getEmail());
        if (user == null) {
            logger.warn("用户未找到，邮箱: " + loginRequest.getEmail());
            return "用户未找到";
        }

        // 2. 验证密码是否正确
        if (passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            logger.info("登录成功，用户邮箱: " + loginRequest.getEmail());
            return "登录成功";
        } else {
            logger.warn("密码错误，邮箱: " + loginRequest.getEmail());
            return "密码错误";
        }
    }
}

// LoginRequest 用于接收前端传过来的数据
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
