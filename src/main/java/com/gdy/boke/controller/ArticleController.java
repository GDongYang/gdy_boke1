package com.gdy.boke.controller;

import com.gdy.boke.model.ArticleInfo;
import com.gdy.boke.mq.RabbitMQMessageTarget;
import com.gdy.boke.mq.RabbitmqService;
import com.gdy.boke.service.ArticleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ArticleController {
    @Autowired
    private RabbitmqService rabbitMQService;

    private static final Logger log = LoggerFactory.getLogger(ArticleController.class);

    @Autowired
    private ArticleService articleService;

    @RequestMapping("/article")
    public ModelAndView findById(@RequestParam("articleId") Long articleId, HttpServletRequest request){
        ArticleInfo articleInfo = articleService.findByIdAndSetCount(articleId,request.getRemoteAddr());
        ModelAndView mv = new ModelAndView("single-post");
        Map<String,Object> map = new HashMap<>();
        map.put("article",articleInfo);
        mv.addObject("data",map);
        log.info("comments:"+articleInfo.getCommentInfos());
        return mv;
    }

    @GetMapping("/article/toPublish")
    public String toPublish(){
        return "publish-article";
    }

    @PostMapping("/article/doAddArticle")
    @ResponseBody
    public Map addArticle(@RequestParam String articleTitle,@RequestParam String content){
        log.info("User add an new article:"+content);
        Map paramMap = new HashMap<String,Object>();
        paramMap.put("articleTitle",articleTitle);
        paramMap.put("content",content);
        Long l = articleService.addArticle(paramMap);

        Map<String,Object> map = new HashMap<>();
        if(l!=null){
            map.put("success",true);
        }else{
            map.put("success",false);
        }
        return  map;
    }

    @GetMapping("/article/findAll")
    @ResponseBody
    public List<ArticleInfo> findAll(@RequestParam Map map,@RequestParam(required = false) Long themeId){
        if(themeId!=null){
            map.put("themeId",themeId);
        }
        Page<ArticleInfo> all = articleService.findAll(map, 1,5);
        log.info("articles:"+all.getContent());
        return all.getContent();
    }



    @GetMapping("/article/findMostHot")
    @ResponseBody
    public ArticleInfo findMostHot(){

        log.info("mostHotArticle:",articleService.findMostHot());
        RabbitMQMessageTarget target = RabbitMQMessageTarget.createDirectTarget("11");
        rabbitMQService.send(target,articleService.findMostHot());
        return articleService.findMostHot();
    }

}
