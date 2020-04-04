package com.gdy.boke.service.impl;

import com.gdy.boke.service.RedisMgmtService;
import com.gdy.boke.util.SerializeUtil;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;
import redis.clients.util.SafeEncoder;

import java.util.*;

public class RedisClusterMgmtServiceImpl implements RedisMgmtService {
    public static final String CAHCENAME = "cache";
    public static final int CAHCETIME = 60;
    private JedisCluster jedisCluster;

    public RedisClusterMgmtServiceImpl() {
    }

    public void set(String key, String val) {
        this.jedisCluster.set(key, val);
    }

    public Object get(String key) {
        return this.jedisCluster.get(key);
    }

    public boolean hmset(String key, Map<String, Object> field) {
        if (field != null && field.size() > 0) {
            JdkSerializationRedisSerializer jdkSerializa = new JdkSerializationRedisSerializer();
            Map<byte[], byte[]> map = new HashMap();
            Iterator var5 = field.keySet().iterator();

            while(var5.hasNext()) {
                String str = (String)var5.next();
                map.put(jdkSerializa.serialize(str), jdkSerializa.serialize(field.get(str)));
            }

            byte[] serialize2 = jdkSerializa.serialize(key);
            this.jedisCluster.hmset(serialize2, map);
            return true;
        } else {
            return false;
        }
    }

    public Object hmget(String key1, String key2) {
        JdkSerializationRedisSerializer jdkSerializa = new JdkSerializationRedisSerializer();
        return jdkSerializa.deserialize(this.jedisCluster.hget(jdkSerializa.serialize(key1), jdkSerializa.serialize(key2)));
    }

    public void deleteCache(String key) {
        this.jedisCluster.del(key);
    }

    public void deleteCacheWithPattern(String pattern) {
        Set<String> keys = this.keys(pattern);
        Iterator var3 = keys.iterator();

        while(var3.hasNext()) {
            String key = (String)var3.next();
            this.jedisCluster.del(key);
        }

    }

    public void clearCache() {
        this.deleteCacheWithPattern("cache|*");
    }

    public Set<String> keys(String pattern) {
        Set<String> keys = new HashSet();
        Map<String, JedisPool> clusterNodes = this.jedisCluster.getClusterNodes();
        Iterator var4 = clusterNodes.keySet().iterator();

        while(var4.hasNext()) {
            String k = (String)var4.next();
            JedisPool jp = (JedisPool)clusterNodes.get(k);
            Jedis connection = jp.getResource();

            try {
                keys.addAll(connection.keys(pattern));
            } catch (Exception var12) {
                var12.printStackTrace();
            } finally {
                connection.close();
            }
        }

        return keys;
    }

    public JedisCluster getJedisCluster() {
        return this.jedisCluster;
    }

    public void setJedisCluster(JedisCluster jedisCluster) {
        this.jedisCluster = jedisCluster;
    }

    public boolean expire(String key, long time) {
        try {
            if (time > 0L) {
                JdkSerializationRedisSerializer jdkSerializa = new JdkSerializationRedisSerializer();
                this.jedisCluster.expire(jdkSerializa.serialize(key), (int)time);
            }

            return true;
        } catch (Exception var5) {
            var5.printStackTrace();
            return false;
        }
    }

    public long getExpire(String key) {
        JdkSerializationRedisSerializer jdkSerializa = new JdkSerializationRedisSerializer();
        Long ttlValue = this.jedisCluster.ttl(jdkSerializa.serialize(key));
        return ttlValue == null ? 0L : ttlValue.longValue();
    }

    public boolean hasKey(String key) {
        return this.jedisCluster.exists(key).booleanValue();
    }

    public void del(String... key) {
        if (key != null && key.length > 0) {
            JdkSerializationRedisSerializer jdkSerializa = new JdkSerializationRedisSerializer();
            if (key.length == 1) {
                this.jedisCluster.del(jdkSerializa.serialize(key[0]));
            } else {
                for(int i = 0; i < key.length; ++i) {
                    this.jedisCluster.del(jdkSerializa.serialize(key[i]));
                }
            }
        }

    }

    public boolean set(String key, Object value) {
        this.jedisCluster.set(key, value.toString());
        return true;
    }

    public boolean set(String key, Object value, long time) {
        return false;
    }

    public long incr(String key, long delta) {
        return 0L;
    }

    public long decr(String key, long delta) {
        return 0L;
    }

    public Object hget(String key, String item) {
        JdkSerializationRedisSerializer jdkSerializa = new JdkSerializationRedisSerializer();
        return jdkSerializa.deserialize(this.jedisCluster.hget(jdkSerializa.serialize(key), jdkSerializa.serialize(item)));
    }

    public Map<Object, Object> hmget(String key) {
        JdkSerializationRedisSerializer jdkSerializa = new JdkSerializationRedisSerializer();
        byte[] serializeKey = jdkSerializa.serialize(key);
        Map<Object, Object> map = new HashMap();
        Map<byte[], byte[]> result = this.jedisCluster.hgetAll(serializeKey);
        Iterator var6 = result.keySet().iterator();

        while(var6.hasNext()) {
            byte[] bt = (byte[])var6.next();
            map.put(jdkSerializa.deserialize(bt), jdkSerializa.deserialize((byte[])result.get(bt)));
        }

        return map;
    }

    public boolean hmset(String key, Map<String, Object> field, long time) {
        if (field != null && field.size() > 0) {
            JdkSerializationRedisSerializer jdkSerializa = new JdkSerializationRedisSerializer();
            Map<byte[], byte[]> map = new HashMap();
            Iterator var7 = field.keySet().iterator();

            while(var7.hasNext()) {
                String str = (String)var7.next();
                map.put(jdkSerializa.serialize(str), jdkSerializa.serialize(field.get(str)));
            }

            byte[] serialize2 = jdkSerializa.serialize(key);
            this.jedisCluster.hmset(serialize2, map);
            this.jedisCluster.expire(serialize2, (int)time);
            return true;
        } else {
            return false;
        }
    }

    public boolean hset(String key, String item, Object value) {
        JdkSerializationRedisSerializer jdkSerializa = new JdkSerializationRedisSerializer();
        Long ret = this.jedisCluster.hset(jdkSerializa.serialize(key), jdkSerializa.serialize(item), jdkSerializa.serialize(value));
        return ret.longValue() >= 0L;
    }

    public boolean hset(String key, String item, Object value, long time) {
        return false;
    }

    public void hdel(String key, Object... item) {
    }

    public boolean hHasKey(String key, String item) {
        return false;
    }

    public double hincr(String key, String item, double by) {
        return 0.0D;
    }

    public double hdecr(String key, String item, double by) {
        return 0.0D;
    }

    public Set<Object> sGet(String key) {
        return null;
    }

    public boolean sHasKey(String key, Object value) {
        return false;
    }

    public long sSet(String key, Object... values) {
        return 0L;
    }

    public long sSetAndTime(String key, long time, Object... values) {
        return 0L;
    }

    public long sGetSetSize(String key) {
        return 0L;
    }

    public long setRemove(String key, Object... values) {
        return 0L;
    }

    public List<Object> lGet(String key, long start, long end) {
        return null;
    }

    public long lGetListSize(String key) {
        return 0L;
    }

    public Object lGetIndex(String key, long index) {
        return null;
    }

    public boolean lSet(String key, Object value) {
        return false;
    }

    public boolean lSet(String key, Object value, long time) {
        return false;
    }

    public boolean lSet(String key, List<Object> value) {
        return false;
    }

    public boolean lSet(String key, List<Object> value, long time) {
        return false;
    }

    public boolean lUpdateIndex(String key, long index, Object value) {
        return false;
    }

    public long lRemove(String key, long count, Object value) {
        return 0L;
    }

    public String set(byte[] key, byte[] value) {
        return this.jedisCluster.set(key, value);
    }

    public byte[] get(byte[] key) {
        return this.jedisCluster.get(key);
    }

    public Long expire(byte[] key, int seconds) {
        return this.jedisCluster.expire(key, seconds);
    }

    public String hmsetForhset(String key, Map<String, Object> fields) {
        if (fields.isEmpty()) {
            return null;
        } else {
            Map<byte[], byte[]> hash = new HashMap();
            Set<Map.Entry<String, Object>> entry = fields.entrySet();
            Iterator iterator = entry.iterator();

            while(iterator.hasNext()) {
                Map.Entry<String, Object> entry2 = (Map.Entry)iterator.next();
                hash.put(SafeEncoder.encode((String)entry2.getKey()), SerializeUtil.serialize(entry2.getValue()));
            }

            return this.jedisCluster.hmset(SafeEncoder.encode(key), hash);
        }
    }

    public byte[] hgetBytes(String key, String field) {
        return this.jedisCluster.hget(SafeEncoder.encode(key), SafeEncoder.encode(field));
    }

    public List<Object> hvalsForObject(String key) {
        List<Object> voList = new ArrayList();
        Collection<byte[]> col = this.jedisCluster.hvals(SafeEncoder.encode(key));
        Iterator ite = col.iterator();

        while(ite.hasNext()) {
            byte[] bytes = (byte[])ite.next();
            voList.add(SerializeUtil.deserialize(bytes));
        }

        return voList;
    }

    public Boolean exists(String key) {
        return this.jedisCluster.exists(key);
    }

    public Long del(byte[] key) {
        return this.jedisCluster.del(key);
    }

    public Long hdel(String key, String... field) {
        long j = 0L;
        if (key != null && field.length > 0) {
            JdkSerializationRedisSerializer jdkSerializa = new JdkSerializationRedisSerializer();
            if (field.length == 1) {
                return this.jedisCluster.hdel(jdkSerializa.serialize(key), new byte[][]{jdkSerializa.serialize(field[0])});
            } else {
                for(int i = 0; i < field.length; ++i) {
                    j += this.jedisCluster.hdel(jdkSerializa.serialize(key), new byte[][]{jdkSerializa.serialize(field[i])}).longValue();
                }

                return j;
            }
        } else {
            return j;
        }
    }
}
