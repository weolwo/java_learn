package com.poplar.jdk7;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Create BY poplar ON 2020/12/8
 */
public class HashMapTest {

    public static void main(String[] args) {
        HashMap<String, String> map = new HashMap<>();
        ConcurrentHashMap<Object, Object> concurrentHashMap = new ConcurrentHashMap<>();
        map.put("name", "猫花");
        concurrentHashMap.put("name", "七猫");
        System.out.println(916599906%16);
    }
}
