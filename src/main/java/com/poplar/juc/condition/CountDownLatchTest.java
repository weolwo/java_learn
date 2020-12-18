package com.poplar.juc.condition;

import java.util.concurrent.CountDownLatch;

/**
 * Create BY poplar ON 2020/12/14
 * CountDownLatch 类似于打团队游戏，必须等所有人都准备好了，游戏才能开始
 */
public class CountDownLatchTest {

    private final static String[] countries = {"韩", "赵", "魏", "楚", "燕", "齐"};

    public static void main(String[] args) throws InterruptedException {

        CountDownLatch countDownLatch = new CountDownLatch(6);
        for (int i = 0; i < 6; i++) {
            int c = i;
            new Thread(() -> {
                System.out.println("\t" + countries[c] + "国被灭");
                countDownLatch.countDown();
            }, Thread.currentThread().getName()).start();
        }
        countDownLatch.await();
        System.out.println("======秦统一======");
    }

}
