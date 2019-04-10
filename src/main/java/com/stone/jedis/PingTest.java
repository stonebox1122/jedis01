package com.stone.jedis;

import redis.clients.jedis.Jedis;

public class PingTest {
    public static void main(String[] args) {
        Jedis jedis = new Jedis("172.30.60.64",6379);
        System.out.println(jedis.ping());
    }
}
