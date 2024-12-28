package com.zjubs.shopcompare.service;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.HttpEntity;

import java.util.HashMap;
import java.util.Map;
@Service
public class PythonSpiderService {

    private final RestTemplate restTemplate;

    public PythonSpiderService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String callPythonSpider(String keyword) {
        String url = "http://localhost:8080/crawl";  // Flask 服务的 URL

        // 构建请求头，指定 Content-Type 为 application/json
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 构建请求体，包含 JSON 数据
        Map<String, String> body = new HashMap<>();
        body.put("keyword", keyword);
        HttpEntity<Map<String, String>> entity = new HttpEntity<>(body, headers);

        // 发送 POST 请求
        System.out.println("callPythonSpider: " + url);
        try {
            // 使用 RestTemplate 发送请求
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

            // 打印响应的状态码和内容
            System.out.println("Response Status: " + response.getStatusCode());
            System.out.println("Response Body: " + response.getBody());

            return response.getBody();
        } catch (Exception e) {
            // 捕获异常并输出详细信息
            System.err.println("Error occurred while calling Python Spider API: " + e.getMessage());
            e.printStackTrace();  // 打印堆栈信息
            return null;
        }
    }
}
