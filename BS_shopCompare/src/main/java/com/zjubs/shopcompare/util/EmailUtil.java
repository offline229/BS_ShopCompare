package com.zjubs.shopcompare.util;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

import jakarta.mail.internet.MimeMessage;

@Component
public class EmailUtil {

    @Autowired
    private JavaMailSender mailSender;

    /**
     * 发送邮件
     */
    public void sendEmail(String to, String subject, String text) throws Exception {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setFrom("zju229_project@163.com");  // 设置发件人
        helper.setTo(to);                      // 设置收件人
        helper.setSubject(subject);            // 设置主题
        helper.setText(text);                  // 设置邮件内容

        mailSender.send(mimeMessage);
    }
}
