package com.gdy.boke.config;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class JedisPoolConfig  {

    private static JedisPool jedisPool = null;

    //初始化
    static {
        if(jedisPool==null){
            redis.clients.jedis.JedisPoolConfig config = new redis.clients.jedis.JedisPoolConfig();
            config.setMaxIdle(20);
            config.setMaxTotal(20);
            config.setMaxWaitMillis(1000*100);
            config.setTestOnBorrow(true);
            config.setTestOnReturn(true);
            jedisPool=new JedisPool(config,"127.0.0.1",6379,10000);

        }
    }

    public static Jedis getJedis(){
        return jedisPool.getResource();
    }

    public static void close(Jedis jedis){
        if(jedis!=null){
            jedis.close();
        }
    }

}
