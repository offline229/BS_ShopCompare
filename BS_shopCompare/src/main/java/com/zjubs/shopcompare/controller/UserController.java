package com.zjubs.shopcompare.controller;

import com.zjubs.shopcompare.model.User;
import com.zjubs.shopcompare.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserRepository userRepository;  // 用于查询用户信息

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        logger.info("收到的请求数据: ");
        logger.info("Email: " + loginRequest.getEmail());
        logger.info("Password: " + loginRequest.getPassword());

        // 直接查询 user 表的一条数据，用于检查数据库连接是否正常
        Optional<User> userOptional = userRepository.findById(1L);  // 这里假设查询 id 为 1 的用户
        if (userOptional.isPresent()) {
            logger.info("查询到的用户数据: ");
            logger.info("User ID: " + userOptional.get().getId());
            logger.info("User Name: " + userOptional.get().getUsername());
            logger.info("User Email: " + userOptional.get().getEmail());
            logger.info("User Password: " + userOptional.get().getPassword());
        } else {
            logger.warn("未查询到用户数据（id = 1）");
        }

        // 从数据库中查询用户
        Optional<User> userFromDb = userRepository.findByEmail(loginRequest.getEmail());

        if (!userFromDb.isPresent()) {
            logger.info("用户未找到: " + loginRequest.getEmail());
            // 返回400错误并附带用户未找到信息
            return new ResponseEntity<>("用户未找到", HttpStatus.BAD_REQUEST);  // 400 错误
        }

        User user = userFromDb.get();

        // 比对密码
        if (!user.getPassword().equals(loginRequest.getPassword())) {
            logger.info("密码错误: " + loginRequest.getEmail());
            // 返回400错误并附带密码错误信息
            return new ResponseEntity<>("密码错误", HttpStatus.BAD_REQUEST);  // 400 错误
        }

        logger.info("登录成功: " + loginRequest.getEmail());
        // 如果验证成功，返回200 OK和登录成功信息
        return new ResponseEntity<>(user.getUsername(), HttpStatus.OK);  // 200 OK
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
