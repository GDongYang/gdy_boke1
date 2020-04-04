package com.gdy.boke.constant;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.ttl.TransmittableThreadLocal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserInfoContext {

    private static final Logger logger = LoggerFactory.getLogger(UserInfoContext.class);

    private static final ThreadLocal<JSONObject> userInfoThreadLocal = new TransmittableThreadLocal<>();

    public static JSONObject get(){
        return userInfoThreadLocal.get();
    }

    public static void set(JSONObject obj){
        logger.info("set userInfoContext threadLocal success");
        userInfoThreadLocal.set(obj);
    }

    public static void remove(){
        logger.info("remove userInfoContext threadLocal success");
        userInfoThreadLocal.remove();
    }
}
