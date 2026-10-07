package com.bibilab.ai_knowledge_base;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
@RestController
public class HelloController {


    @Autowired 
    private AiService aiService;

    @GetMapping("/hello")
    public String sayHello(){
        return"hello!welcome to your AI knowledge base backend!";

        
    }

        @GetMapping("/ai/query")
    public Map<String, String> askAi(@RequestParam String question) {
        // 创建一个 Map，它最终会被 Spring Boot 自动变成 JSON
        Map<String, String> response = new HashMap<>();
        
        response.put("question", question);
        
        // 【关键动作】：服务员不自己炒菜了，去呼叫大厨！
        // 让 aiService 施展 getAnswer 技能，并把结果存到 chefAnswer 变量里
        String chefAnswer = aiService.getAnswer(question);
        
        // 把大厨做出来的真实回答，放到托盘里
        response.put("answer", chefAnswer);
        response.put("status", "success");
        
        return response;
    }


}
