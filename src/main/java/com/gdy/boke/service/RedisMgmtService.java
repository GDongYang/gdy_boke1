package com.gdy.boke.service;

import redis.clients.jedis.JedisCluster;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface RedisMgmtService {
    JedisCluster getJedisCluster();

    boolean expire(String var1, long var2);

    long getExpire(String var1);

    boolean hasKey(String var1);

    void del(String... var1);

    Object get(String var1);

    boolean set(String var1, Object var2);

    boolean set(String var1, Object var2, long var3);

    long incr(String var1, long var2);

    long decr(String var1, long var2);

    Object hget(String var1, String var2);

    Map<Object, Object> hmget(String var1);

    boolean hmset(String var1, Map<String, Object> var2);

    boolean hmset(String var1, Map<String, Object> var2, long var3);

    boolean hset(String var1, String var2, Object var3);

    boolean hset(String var1, String var2, Object var3, long var4);

    void hdel(String var1, Object... var2);

    boolean hHasKey(String var1, String var2);

    double hincr(String var1, String var2, double var3);

    double hdecr(String var1, String var2, double var3);

    Set<Object> sGet(String var1);

    boolean sHasKey(String var1, Object var2);

    long sSet(String var1, Object... var2);

    long sSetAndTime(String var1, long var2, Object... var4);

    long sGetSetSize(String var1);

    long setRemove(String var1, Object... var2);

    List<Object> lGet(String var1, long var2, long var4);

    long lGetListSize(String var1);

    Object lGetIndex(String var1, long var2);

    boolean lSet(String var1, Object var2);

    boolean lSet(String var1, Object var2, long var3);

    boolean lSet(String var1, List<Object> var2);

    boolean lSet(String var1, List<Object> var2, long var3);

    boolean lUpdateIndex(String var1, long var2, Object var4);

    long lRemove(String var1, long var2, Object var4);

    String set(byte[] var1, byte[] var2);

    byte[] get(byte[] var1);

    Long expire(byte[] var1, int var2);

    Boolean exists(String var1);

    Long del(byte[] var1);

    String hmsetForhset(String var1, Map<String, Object> var2);

    byte[] hgetBytes(String var1, String var2);

    List<Object> hvalsForObject(String var1);

    Long hdel(String var1, String... var2);

    Object hmget(String var1, String var2);
}
