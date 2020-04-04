package com.gdy.boke.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;

@Configuration
public class CacheUtil {

    private  static final Logger log = LoggerFactory.getLogger(CacheUtil.class);

    @Autowired
    private RedisTemplate redisTemplate;

    public void catchObject(String name,Object data){
        log.info("write redis catche"+name+":"+data.toString());
        redisTemplate.expire(name,10, TimeUnit.SECONDS);
        redisTemplate.opsForValue().set(name,data);
    }

    public void catchObject(String name,Long seconds,Object data){
        redisTemplate.expire(name,seconds,TimeUnit.SECONDS);
        redisTemplate.opsForValue().set(name,data);
    }


    public Object getCatch(String name){
        Object ob = redisTemplate.opsForValue().get(name);
        log.info("get redis catch",name,ob);
        return ob;
    }

    public void deleteCatch(String name){
        redisTemplate.delete(name);
    }
}
