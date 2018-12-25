package com.poni.cache;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;

import javax.annotation.Resource;

/*
 *@author:PONI_CHAN
 *@date:2018/12/24 15:46
 */
public class RedisCacheManager implements CacheManager {

    @Resource
    private RedisCache redisCache;

    //因为本身就是单例的，所以直接返回redisCache，s是cachename
    @Override
    public <K, V> Cache<K, V> getCache(String s) throws CacheException {
        return redisCache;
    }
}
