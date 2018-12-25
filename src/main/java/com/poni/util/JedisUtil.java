package com.poni.util;

import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.Resource;
import java.util.Set;

/*
 *@author:PONI_CHAN
 *@date:2018/12/22 16:49
 */
@Component
//redis访问工具包
public class JedisUtil {

    @Resource
    private JedisPool jedisPool;  //获取连接

    //创建获取连接资源的方法
    private Jedis getResource() {
        return jedisPool.getResource();
    }

    //创建对应set方法
    public void set(byte[] key, byte[] value) {
        Jedis jedis = getResource();
        try {
            jedis.set(key, value);
        } finally {
            jedis.close();
        }
    }

    //创建对应超时时间expire方法
    public void expire(byte[] key, int i) {
        Jedis jedis = getResource();
        try {
            jedis.expire(key, i);
        } finally {
            jedis.close();
        }
    }

    //创建对应获得get方法
    public byte[] get(byte[] key) {
        Jedis jedis = getResource();
        try {
            return jedis.get(key);
        } finally {
            jedis.close();
        }
    }

    //创建对应获得del方法
    public void del(byte[] key) {
        Jedis jedis = getResource();
        try {
            jedis.del(key);
        } finally {
            jedis.close();
        }
    }

    //创建对应keys获取方法
    public Set<byte[]> keys(String shiro_session_prefix) {
        Jedis jedis = getResource();
        try {
            return jedis.keys((shiro_session_prefix + "*").getBytes());
        } finally {
            jedis.close();
        }
    }
}
