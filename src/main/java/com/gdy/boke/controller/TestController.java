package com.gdy.boke.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class TestController {

    @RequestMapping("/test")
    public String test(){
        return "testue";
    }

    /*@RequestMapping("/blogPicture/uploadPicture")
    @ResponseBody
    public Map<String,Object> hello(MultipartFile imageFile){
        System.out.println("图片进来了");
        HashMap<String, Object> he = new HashMap<>();
        he.put("url","https://ss0.bdstatic.com/94oJfD_bAAcT8t7mm9GUKT-xh_/timg?image&quality=100&size=b4000_4000&sec=1584023256&di=41e1562e6f3ee29610cefc42ff4a2e40&src=http://a3.att.hudong.com/68/61/300000839764127060614318218_950.jpg");
        he.put("error", 0);
        return he;
    }*/
}
