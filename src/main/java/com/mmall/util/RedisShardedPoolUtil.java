package com.mmall.util;

import com.mmall.common.RedisShardedPool;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.ShardedJedis;

/**
 * @program: mmall
 * @Date: 2018/8/15
 * @Author: chandler
 * @Description:
 */
@Slf4j
public class RedisShardedPoolUtil {
    /**
     * 设置key的有效期
     * @param key
     * @param exTime
     * @return
     */
    public static Long expire(String key,int exTime){
        ShardedJedis jedis=null;
        Long result=null;
        try {
            jedis = RedisShardedPool.getJedis();
            result = jedis.expire(key,exTime);
        }catch (Exception e){
            log.error("expire key:{} error",key,e);
            RedisShardedPool.returnBrokenResource(jedis);
            return result;
        }
        RedisShardedPool.retunrResource(jedis);
        return result;
    }

    /**
     * 设置key的值和有效期
     * @param key
     * @param value
     * @param exTime
     * @return
     */
    public static String setEx(String key,String value,int exTime){
        ShardedJedis jedis=null;
        String result=null;
        try {
            jedis = RedisShardedPool.getJedis();
            result = jedis.setex(key,exTime,value);
        }catch (Exception e){
            log.error("expire key:{} value:{} error",key,value,e);
            RedisShardedPool.returnBrokenResource(jedis);
            return result;
        }
        RedisShardedPool.retunrResource(jedis);
        return result;
    }

    /**
     * 如果不存在就设置key和value
     * @param key
     * @param value
     * @return
     */
    public static Long setnx(String key,String value){
        ShardedJedis jedis=null;
        Long result=null;
        try {
            jedis = RedisShardedPool.getJedis();
            result = jedis.setnx(key,value);
        }catch (Exception e){
            log.error("expire key:{} value:{} error",key,value,e);
            RedisShardedPool.returnBrokenResource(jedis);
            return result;
        }
        RedisShardedPool.retunrResource(jedis);
        return result;
    }
    /**
     * 获取key对应的值
     * @param key
     * @return
     */
    public static String get(String key){
        ShardedJedis jedis = null;
        String result = null;
        try {
            jedis = RedisShardedPool.getJedis();
            result = jedis.get(key);
        } catch (Exception e) {
            log.error("get key:{} error",key,e);
            RedisShardedPool.returnBrokenResource(jedis);
            return result;
        }
        RedisShardedPool.retunrResource(jedis);
        return result;
    }

    /**
     * 设置新值并且返回旧值
     * @param key
     * @param value
     * @return
     */
    public static String getSet(String key,String value){
        ShardedJedis jedis = null;
        String result = null;
        try {
            jedis = RedisShardedPool.getJedis();
            result = jedis.getSet(key,value);
        } catch (Exception e) {
            log.error("get key:{} error",key,value,e);
            RedisShardedPool.returnBrokenResource(jedis);
            return result;
        }
        RedisShardedPool.retunrResource(jedis);
        return result;
    }
    /**
     * 修改key中的值
     * @param key
     * @param value
     * @return
     */
    public static String set(String key,String value){
        ShardedJedis jedis=null;
        String result=null;
        try {
            jedis = RedisShardedPool.getJedis();
            result = jedis.set(key,value);
        }catch (Exception e){
            log.error("set key:{} value:{} error",key,value,e);
            RedisShardedPool.returnBrokenResource(jedis);
            return result;
        }
        RedisShardedPool.retunrResource(jedis);
        return result;
    }

    /**
     * 删除某个key值
     * @param key
     * @return
     */
    public static Long del(String key){
        ShardedJedis jedis=null;
        Long result=null;
        try {
            jedis = RedisShardedPool.getJedis();
            result = jedis.del(key);
        }catch (Exception e){
            log.error("del key:{}error",key,e);
            RedisShardedPool.returnBrokenResource(jedis);
            return result;
        }
        RedisShardedPool.retunrResource(jedis);
        return result;
    }

    public static void main(String[] args) {
        ShardedJedis jedis = RedisShardedPool.getJedis();

        RedisShardedPoolUtil.set("keyTest","value");

        String value = RedisShardedPoolUtil.get("keyTest");

        RedisShardedPoolUtil.setEx("keyex","valueex",60*10);

        RedisShardedPoolUtil.expire("keyTest",60*20);

        RedisShardedPoolUtil.del("keyTest");


    }
}
