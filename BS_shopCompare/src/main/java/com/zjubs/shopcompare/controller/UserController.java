package com.zjubs.shopcompare.controller;

import com.zjubs.shopcompare.model.User;
import com.zjubs.shopcompare.repository.UserRepository;
import com.zjubs.shopcompare.service.RedisService;
import com.zjubs.shopcompare.util.CaptchaUtil;
import com.zjubs.shopcompare.util.EmailUtil;
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
    private RedisService redisService;
    @Autowired
    private UserRepository userRepository;  // 用于查询用户信息

    @Autowired
    private EmailUtil emailUtil;  // 注入 EmailUtil 发送邮件

    // 登录接口
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        logger.info("收到的请求数据: ");
        logger.info("Email: " + loginRequest.getEmail());
        logger.info("Password: " + loginRequest.getPassword());

        // 查询数据库中用户
        Optional<User> userFromDb = userRepository.findByEmail(loginRequest.getEmail());

        if (!userFromDb.isPresent()) {
            logger.info("用户未找到: " + loginRequest.getEmail());
            // 返回400错误并附带用户未找到信息
            return new ResponseEntity<>("用户未找到", HttpStatus.BAD_REQUEST);
        }

        User user = userFromDb.get();

        // 比对密码
        if (!user.getPassword().equals(loginRequest.getPassword())) {
            logger.info("密码错误: " + loginRequest.getEmail());
            // 返回400错误并附带密码错误信息
            return new ResponseEntity<>("密码错误", HttpStatus.BAD_REQUEST);
        }

        logger.info("登录成功: " + loginRequest.getEmail());
        // 返回200 OK 和登录成功信息
        return new ResponseEntity<>(user.getUsername(), HttpStatus.OK);
    }

    // 发送验证码接口
    @PostMapping("/send-captcha")
    public ResponseEntity<String> sendCaptcha(@RequestBody SendCaptchaRequest sendCaptchaRequest) {
        logger.info("收到的发送验证码请求: ");
        logger.info("Email: " + sendCaptchaRequest.getEmail());

        // 检查邮箱是否已注册
        Optional<User> userFromDb = userRepository.findByEmail(sendCaptchaRequest.getEmail());
        if (userFromDb.isPresent()) {
            logger.info("该邮箱已注册: " + sendCaptchaRequest.getEmail());
            return new ResponseEntity<>("该邮箱已注册", HttpStatus.BAD_REQUEST);
        }
        userFromDb = userRepository.findByUsername(sendCaptchaRequest.getUsername());
        if (userFromDb.isPresent()) {
            logger.info("该用户名已被注册: " + sendCaptchaRequest.getUsername());
            return new ResponseEntity<>("该用户名已被注册", HttpStatus.BAD_REQUEST);
        }


        // 发送验证码邮件
        try {
            String email = sendCaptchaRequest.getEmail();
            String subject = "注册验证码";
            String captcha = CaptchaUtil.generateCaptcha();
            String text = "您的验证码是：" + captcha + "，有效期五分钟。";

            // 调用EmailUtil发送邮件
            emailUtil.sendEmail(email, subject, text);
            System.out.println("发送验证码至邮箱 " + email + "：验证码是 " + captcha);
            // 保存验证码到 Redis 中
            redisService.saveCaptcha(email, captcha);
            // 直接返回成功的响应信息
            return new ResponseEntity<>("验证码已发送到邮箱", HttpStatus.OK);
        } catch (Exception e) {
            logger.error("验证码发送失败: ", e);
            // 发送失败时，返回失败信息
            return new ResponseEntity<>("验证码发送失败", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 注册接口
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest registerRequest) {
        String email = registerRequest.getEmail();
        String captcha = registerRequest.getCaptcha();
        // 获取 Redis 中的验证码
        String storedCaptcha = redisService.getCaptcha(email);
        // 校验验证码
        if (storedCaptcha == null || !storedCaptcha.equals(captcha)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("验证码无效或已过期");
        }
        // 假设验证码正确，进行注册逻辑（这里只是简单打印，实际可以在数据库中保存用户信息）
        redisService.deleteCaptcha(email);

        User newUser = new User();
        newUser.setEmail(email);
        newUser.setUsername(registerRequest.getUsername());
        newUser.setPassword(registerRequest.getPassword()); // 需要加密密码
        userRepository.save(newUser);

        logger.info("注册成功，用户名: " + registerRequest.getEmail());
        return new ResponseEntity<>("注册成功", HttpStatus.OK);
    }
}

class SendCaptchaRequest {
    private String email;
    private String username;  // 添加 username 字段

    // Getter 和 Setter
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}


class RegisterRequest {
    private String email;
    private String password;
    private String captcha;
    private String username;

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

    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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
