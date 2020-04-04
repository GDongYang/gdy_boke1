package com.gdy.boke.util;

import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;

/**
 * 序列化和反序列化  redis存储
 */
public class SerializeUtil {

    public static byte[] serialize(Object obj){
        byte[] serialize = new JdkSerializationRedisSerializer().serialize(obj);
        return serialize;
    }

    public static Object deserialize(byte[] bytes){
        Object obj = new JdkSerializationRedisSerializer().deserialize(bytes);
        return obj;
    }

}
