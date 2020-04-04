package com.gdy.boke.intercepter;

import com.alibaba.fastjson.JSONObject;
import com.gdy.boke.constant.UserInfoContext;
import com.gdy.boke.controller.UserController;
import com.gdy.boke.model.UserInfo;
import com.gdy.boke.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AuthIntercepter implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(AuthIntercepter.class);

    @Autowired
    private UserService userService;

    @Autowired
    private UserController userController;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("拦截器进来起作用了：--------------------");
        UserInfo currentUser = userController.getCurrentUser(request);
        if(currentUser!=null){
            UserInfoContext.set((JSONObject) JSONObject.toJSON(currentUser));
            log.info("get user message:"+currentUser);
        }else {
            log.info("cant get any user message------------");
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {

    }
}
