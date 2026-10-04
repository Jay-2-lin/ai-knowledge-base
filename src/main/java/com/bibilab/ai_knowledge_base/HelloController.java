package com.bibilab.ai_knowledge_base;
import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
@RestController
public class HelloController {

    @GetMapping("/hello")
    public String sayHello(){
        return"hello!welcome to your AI knowledge base backend!";

        
    }

        @GetMapping("/ai/query")
    public Map<String, String> askAi(@RequestParam String question) {
        // 创建一个 Map，它最终会被 Spring Boot 自动变成 JSON
        Map<String, String> response = new HashMap<>();
        
        // 把用户的提问原封不动地放进去
        response.put("question", question);
        
        // 暂时写死一个假回复，等后续我们再把它连上真正的 AI 模型
        response.put("answer", "这是一个占位符回复。我收到了你的问题：" + question);
        response.put("status", "success");
        
        return response;
    }


}
