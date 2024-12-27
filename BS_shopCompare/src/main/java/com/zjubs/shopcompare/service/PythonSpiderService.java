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
        String url = "http://localhost:5000/crawl";  // Flask 服务的 URL

        // 构建请求头，指定 Content-Type 为 application/json
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 构建请求体，包含 JSON 数据
        Map<String, String> body = new HashMap<>();
        body.put("keyword", keyword);
        HttpEntity<Map<String, String>> entity = new HttpEntity<>(body, headers);

        // 发送 POST 请求
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

        // 输出响应
        System.out.println(response.getBody());
        return response.getBody();
    }
}
