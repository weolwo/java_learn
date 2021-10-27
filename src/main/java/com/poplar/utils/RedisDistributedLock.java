package com.poplar.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.connection.ReturnType;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

/**
 * BY Alex CREATED 2021/10/27
 * 使用redis实现分布式锁
 */

@Component
public class RedisDistributedLock {

    private static final Logger logger = LoggerFactory.getLogger(RedisDistributedLock.class);
    private static final String UNLOCK_LUA = "if redis.call('get',KEYS[1]) == ARGV[1] then  return redis.call('del',KEYS[1]) else return 0 end ";

    @Autowired
    private StringRedisTemplate redisTemplate;

    public boolean lock(String key, String clientId, long expireMillis, long sleepMillis) {
        if (setRedis(key, clientId, expireMillis)) {
            return true;
        }
        int retryTimes = 5;
        boolean flag = false;
        while (!flag && retryTimes-- > 0) {
            try {
                TimeUnit.MILLISECONDS.sleep(sleepMillis);
            } catch (InterruptedException e) {
                logger.debug("lock failed, retrying..." + retryTimes);
                return false;
            }
            flag = setRedis(key, clientId, expireMillis);
        }
        return flag;
    }

    private boolean setRedis(final String key, String clientId, final long expire) {
        try {
            RedisCallback<Boolean> callback = (connection) -> connection.set(key.getBytes(StandardCharsets.UTF_8),
                    clientId.getBytes(StandardCharsets.UTF_8), Expiration.milliseconds(expire), RedisStringCommands.SetOption.SET_IF_ABSENT);
            return Boolean.TRUE.equals(redisTemplate.execute(callback));
        } catch (Exception e) {
            logger.error("redis lock error.", e);
        }
        return false;
    }


    public void unLock(String key, String clientId) {
        // 释放锁的时候，有可能因为持锁之后方法执行时间大于锁的有效期，此时有可能已经被另外一个线程持有锁，所以不能直接删除
        try {
            RedisCallback<Boolean> callback = (connection) -> connection.eval(UNLOCK_LUA.getBytes(), ReturnType.BOOLEAN,
                    1, key.getBytes(StandardCharsets.UTF_8), clientId.getBytes(StandardCharsets.UTF_8));
            redisTemplate.execute(callback);
        } catch (Exception e) {
            logger.error("release lock occurred an exception", e);
        }
    }
}
