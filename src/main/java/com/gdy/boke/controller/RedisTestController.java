package com.gdy.boke.controller;

import com.gdy.boke.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class RedisTestController {

    @Autowired
    private RedisService redisService;


    @RequestMapping("/testRedis")
    @ResponseBody
    public String testRedis() throws InterruptedException {
        String articleContent = redisService.getArticle("articleName");
        return articleContent;
    }
}
