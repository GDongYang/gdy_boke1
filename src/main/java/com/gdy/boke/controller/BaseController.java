package com.gdy.boke.controller;

import com.gdy.boke.model.UserInfo;
import com.gdy.boke.util.CacheUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;


public class BaseController {

    @Autowired
    private CacheUtil cacheUtil;
    private static final Logger log = LoggerFactory.getLogger(BaseController.class);

    public UserInfo getCurrentUser(HttpServletRequest request){

        Cookie[] cookies = request.getCookies();
        for (Cookie cc : cookies){
           if(cc.getName().equals("userInfo")){
               String val = cc.getValue();
               UserInfo userInfo = (UserInfo) cacheUtil.getCatch(val);
               log.info("read userJson{}",userInfo);
               request.getSession().setAttribute("userInfo",userInfo);
               return userInfo;
           }
        }
        return null;
    }


}
