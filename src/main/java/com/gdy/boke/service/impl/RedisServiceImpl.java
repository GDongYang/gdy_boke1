package com.gdy.boke.service.impl;

import com.gdy.boke.config.JedisPoolConfig;
import com.gdy.boke.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import java.util.concurrent.TimeUnit;

@Service
public class RedisServiceImpl implements RedisService {

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    private static final String X_ARTICLE = "1";
    private static final String N_ARTICLE = "0";

    private static String content = "db_article_content";

    public static String getDb(){
        return content;
    }

    @Override
    public String getArticle(String articleName) throws InterruptedException {
        String articleContent = (String) redisTemplate.opsForValue().get(articleName);
        if(articleContent==null){
            String concat = articleName.concat(":mute");
            //获取带有过期时间的互斥锁 隔一段时间后就删除锁 避免出现问题时导致线程阻塞
            Jedis jedis = JedisPoolConfig.getJedis();
            if(jedis.set(concat,"1","0","px", 50).equals("1")){
                //添加分布式锁成功 从数据库获取数据
                articleContent = getDb();
                //存入缓存redis
                redisTemplate.opsForValue().set(articleName,articleContent,30, TimeUnit.SECONDS);
                //删除分布式锁
                jedis.del(concat);
            }else {
                //未获取到锁  线程等待后尝试重新获取
                Thread.sleep(50);
                //重试
                getArticle(articleName);
            }
        }
            return articleContent;
    }
}
