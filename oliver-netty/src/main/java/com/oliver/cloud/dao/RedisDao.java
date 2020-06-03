package com.oliver.cloud.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

@Repository
/**
 * @Author: cpeter
 * @Desc: redis数据库封装操作类
 * @Date: cretead in 2019/10/17 18:07
 * @Last Modified: by
 * @return value
 */
public class RedisDao {
    @Autowired
    StringRedisTemplate stringRedisTemplate;

    /**
     * 保存redis的k-v数据
     * @param key
     * @param value
     */
    public void save(String key, String value) {
        ValueOperations<String, String> valOpsStr = stringRedisTemplate
                .opsForValue();
        valOpsStr.set(key, value);
    }

    /**
     * 获取redis的k-v
     * @param key
     * @return
     */
    public String getByKey(String key) {
        ValueOperations<String, String> valOpsStr = stringRedisTemplate
                .opsForValue();
        String value = valOpsStr.get(key);
        return value;
    }

    /**
     * 数据入队列
     * @param key
     * @param value
     * @return
     */
    public String lpush(String key, String value) {
        return stringRedisTemplate.opsForList().leftPush(key, value).toString();
    }

    /**
     * 数据出队列
     * @param key
     * @return
     */
    public String rpop(String key) {
        return stringRedisTemplate.opsForList().rightPop(key);
    }

    /**
     * 设置set数据结构
     * @param dorm
     * @param key
     * @return
     */
    public boolean sset(String dorm, String key) {
        stringRedisTemplate.opsForSet().add(dorm, key);
        return true;
    }

    /**
     * 获取set数据结构
     * @param dorm
     * @param key
     * @return
     */
    public boolean gset(String dorm, String key) {
        return stringRedisTemplate.opsForSet().isMember(dorm, key);

    }
}