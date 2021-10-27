package com.poplar.controller;

import com.poplar.utils.RedisDistributedLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * BY Alex CREATED 2021/10/27
 */

@RestController
public class DistributedLockController {

    @Autowired
    private RedisDistributedLock redisDistributedLock;

    @RequestMapping("/test")
    public Object test() {
        String key = "reeeeeeeeeeeeeeee";
        String clientId = System.nanoTime() + "";
        boolean lock = redisDistributedLock.lock(key, clientId, 10000, 200);
        if (!lock) {
            return "加锁失败";
        }
        try {
            TimeUnit.MILLISECONDS.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            redisDistributedLock.unLock(key, clientId);
        }
        return "成功";
    }
}
