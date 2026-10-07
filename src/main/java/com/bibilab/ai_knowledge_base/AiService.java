package com.bibilab.ai_knowledge_base;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

// ↓ 这两把就是专门用来拆 JSON 快递的美工刀
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class AiService {

    @Value("${google.gemini.api-key}")
    private String apiKey;

    public String getAnswer(String question) {
        System.out.println("大厨正在进货并准备拆快递...");

        RestTemplate restTemplate = new RestTemplate();
        String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.8-flash:generateContent?key=" + apiKey;

        String requestBody = "{\n" +
                "  \"contents\": [{\n" +
                "    \"parts\":[{\"text\": \"" + question + "\"}]\n" +
                "  }]\n" +
                "}";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

        try {
            // 1. 把大箱子（原始 JSON 字符串）扛回来
            ResponseEntity<String> response = restTemplate.postForEntity(url, requestEntity, String.class);
            String rawJson = response.getBody(); 
            
            // 2. 拿出美工刀
            ObjectMapper mapper = new ObjectMapper();
            
            // 3. 把字符串转换成一棵可以一层层剥开的“节点树”
            JsonNode rootNode = mapper.readTree(rawJson);
            
            // 4. 暴力拆箱：顺着路径 candidates -> [0] -> content -> parts -> [0] -> text，一把抓出金块！
            String realAnswer = rootNode
                    .path("candidates").get(0)
                    .path("content")
                    .path("parts").get(0)
                    .path("text").asText();
                    
            // 5. 把干干净净的金块端给客人
            return realAnswer; 
            
        } catch (Exception e) {
            // 如果拆箱的时候发现里面是空的，或者结构变了，报告异常
            return "大厨进货或拆快递失败，原因: " + e.getMessage();
        }
    }
}