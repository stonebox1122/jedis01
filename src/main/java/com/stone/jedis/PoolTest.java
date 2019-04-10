package com.stone.jedis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class PoolTest {
    public static void main(String[] args) {

        JedisPool jedisPool = JedisPoolUtil.getJedisPoolInstance();
        Jedis jedis = null;

        try {
            jedis = jedisPool.getResource();
            jedis.set("aa","bb");
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            JedisPoolUtil.release(jedisPool,jedis);
        }
    }
}
