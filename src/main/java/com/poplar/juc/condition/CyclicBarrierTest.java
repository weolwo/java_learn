package com.poplar.juc.condition;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * Create BY poplar ON 2020/12/14
 * CyclicBarrier更像是一个阀门，需要所有线程都到达，阀门才能打开，然后继续执行。
 */
public class CyclicBarrierTest {

    public static void main(String[] args) {
        CyclicBarrier cyclicBarrier = new CyclicBarrier(7, () -> {
            System.out.println("=====召唤神龙=====");
        });
        for (int i = 1; i <= 7; i++) {
            final int temp = i;
            new Thread(() -> {
                System.out.println( "\t收集到第" + temp + "颗龙珠");
                try {
                    cyclicBarrier.await();
                    System.out.println("llll");
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }
}
