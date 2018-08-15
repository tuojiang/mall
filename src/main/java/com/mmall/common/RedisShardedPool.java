package com.mmall.common;

import com.google.common.collect.Lists;
import com.mmall.util.PropertiesUtil;
import redis.clients.jedis.*;
import redis.clients.util.Hashing;
import redis.clients.util.Sharded;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: mmall
 * @Date: 2018/8/15
 * @Author: chandler
 * @Description:
 */
public class RedisShardedPool {
    /**
     * 连接池
     */
    private static ShardedJedisPool pool;
    /**
     * 最大连接数
     */
    private static Integer maxTotal = Integer.parseInt(PropertiesUtil.getProperty("redis.max.total", "20"));
    /**
     * 最大空闲的jedis实例个数
     */
    private static Integer maxIdle = Integer.parseInt(PropertiesUtil.getProperty("redis.max.idle", "20"));
    /**
     * 最小空闲的jedis实例个数
     */
    private static Integer minIdle = Integer.parseInt(PropertiesUtil.getProperty("redis.min.idle", "20"));
    /**
     * 在borrow一个jedis实例时，是否要验证
     */
    private static Boolean testOnBorrow = Boolean.parseBoolean(PropertiesUtil.getProperty("redis.test.borrow", "true"));
    /**
     * 在return一个jedis实例时，是否要验证
     */
    private static Boolean testOnReturn = Boolean.parseBoolean(PropertiesUtil.getProperty("redis.test.return", "true"));
    /**
     * IP地址
     */
    private static String redis1Ip = PropertiesUtil.getProperty("redis1.ip");
    private static String redis2Ip = PropertiesUtil.getProperty("redis2.ip");
    /**
     * 端口地址
     */
    private static Integer redis1Port = Integer.parseInt(PropertiesUtil.getProperty("redis1.port"));
    private static Integer redis2Port = Integer.parseInt(PropertiesUtil.getProperty("redis2.port"));

    private static void initPool() {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(maxTotal);
        config.setMaxIdle(maxIdle);
        config.setMinIdle(minIdle);

        config.setTestOnBorrow(testOnBorrow);
        config.setTestOnReturn(testOnReturn);

        /**连接耗尽的时候，是否阻塞，false会抛出异常，true阻塞直到超时。默认为true*/
        config.setBlockWhenExhausted(true);

        JedisShardInfo info1 = new JedisShardInfo(redis1Ip,redis1Port,Const.RedisCacheExtime.REDIS_SESSION_EXTIME);
        JedisShardInfo info2 = new JedisShardInfo(redis2Ip,redis2Port,Const.RedisCacheExtime.REDIS_SESSION_EXTIME);

        List<JedisShardInfo> jedisShardInfoList = new ArrayList<>(2);
        jedisShardInfoList.add(info1);
        jedisShardInfoList.add(info2);

        pool = new ShardedJedisPool(config, jedisShardInfoList , Hashing.MURMUR_HASH , Sharded.DEFAULT_KEY_TAG_PATTERN);
    }

    static {
        initPool();
    }
    public static ShardedJedis getJedis() {
        return pool.getResource();
    }

    public static void returnBrokenResource(ShardedJedis jedis) {
        pool.returnBrokenResource(jedis);
    }

    public static void retunrResource(ShardedJedis jedis) {
        pool.returnResource(jedis);
    }

    public static void main(String[] args) {
        ShardedJedis jedis = pool.getResource();
        for (int i = 0; i < 10; i++) {
            jedis.set("key"+i,"value"+i);
        }
        retunrResource(jedis);
        pool.destroy();
        System.out.println("program is end");
    }
}
