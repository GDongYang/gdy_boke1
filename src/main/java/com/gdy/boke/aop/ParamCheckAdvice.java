package com.gdy.boke.aop;

import com.gdy.boke.constant.LoginException;
import com.gdy.boke.service.UserService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * aop  sql注入校验
 */
/*@Component
@Aspect
@Order(2)*/
public class ParamCheckAdvice {

    private static final String INJECT_SQL =  "\\b(and|exec|insert|select|drop|grant|alter|delete|update|count|chr|mid|master|truncate|char|declare|or)\\b|(\\*|;|\\+|'|%)";

    private static final Logger logger = LoggerFactory.getLogger(ParamCheckAdvice.class);

    @Pointcut("execution (public * com.gdy.boke.service.ArticleService.*(..))")
    public void checkParamInject(){}

    @Autowired
    private UserService userService;

    @Before("checkParamInject()")
    public void doBefore(JoinPoint joinPoint){
        logger.info("param check advice has been aop");
        Map param = (Map) joinPoint.getArgs()[0];
        for(Object key : param.keySet()){
            Object value =  param.get(key);
            if(containsInjection(value)){
                throw new LoginException("unsafe sql has been captured");
            }else {
                logger.info("dont have any sql injection");
            }
        }
    }

    private boolean containsInjection(Object value) {
        String obj = value.toString();
        if(isArray0(obj)){
            boolean a = containsInjectionArray((Object[]) value,0);
        }
        Pattern pattern = Pattern.compile(INJECT_SQL);
        Matcher matcher = pattern.matcher(obj);
        return matcher.find();
    }

    /**
     * 递归  注意程序的返回位置
     * @param value
     * @param root
     * @return
     */
    private static boolean containsInjectionArray(Object[] value,int root) {
        if(value.length==root){
            return false;
        }
        Pattern pattern = Pattern.compile(INJECT_SQL);
        Matcher matcher = pattern.matcher(value[root].toString());
        if(matcher.find()){
            return true;
        }else {
            return containsInjectionArray(value,++root);
        }
    }

    public static void main(String[] args) {
        Object[] arr = {"","andqq",""};
        boolean b = containsInjectionArray(arr,0);
        System.out.println(b);
    }

    public boolean isArray0(String val){
        if(val==null){
            return false;
        }
        return val.getClass().isArray();
    }

}
