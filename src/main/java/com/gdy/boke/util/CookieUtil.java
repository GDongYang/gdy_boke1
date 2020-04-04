package com.gdy.boke.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookieUtil {

    private static final String LOGIN_TOKEN = "userInfo";
    private static final String COOKIE_DOMAIN = "boke.com";
    private static final Logger log = LoggerFactory.getLogger(CookieUtil.class);


    public static void writeLoginToken(HttpServletResponse response,String token){
        Cookie ck = new Cookie(LOGIN_TOKEN,token);
        ck.setMaxAge(60*60);
        log.info("write cookie name{},cookie value{}"+LOGIN_TOKEN+token);
        response.addCookie(ck);
    }

    /**
     * 注销的时候删除cookie
     * @param request
     * @return
     */
    public static String deleteLoginToken(HttpServletRequest request,HttpServletResponse response){
        Cookie[] cookies = request.getCookies();
        if(cookies!=null){
            for(Cookie ck : cookies){
                if(ck.getName().equals(LOGIN_TOKEN)){
                    ck.setMaxAge(0);
                    response.addCookie(ck);
                    return ck.getValue();
                }
            }
        }
        return "";
    }

    public static String readLoginToken(HttpServletRequest request){

        Cookie[] cookies = request.getCookies();
        for (Cookie ck : cookies){
            if(ck.getName().equals(LOGIN_TOKEN)){
                return  ck.getValue();
            }
        }
        return null;
    }
}
