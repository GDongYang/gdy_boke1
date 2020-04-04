package com.gdy.boke.aop;

import com.gdy.boke.model.UserInfo;
import com.gdy.boke.service.UserService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * order用来指定aop拦截方法时执行的顺序  aop用户权限校验
 */
@Aspect
@Component
@Order(1)
public class AuthAccess {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(AuthAccess.class);


    @Autowired
    private UserService userService;

    @Pointcut("execution (public * com.gdy.boke.service.ArticleService.*(..))")
    public void serviceAuth(){
    }

    @Before("serviceAuth()")
    public void doBefore(JoinPoint joinPoint) throws Throwable{
        UserInfo byId = userService.findById(1l);
        System.out.println("aop 拦截查询到用户信息" + byId);
        System.out.println("获取到对象的方法名称"+joinPoint.getSignature().getName());
    }
}
