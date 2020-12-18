package com.poplar.threadpool;

/**
 * Create BY poplar ON 2020/12/9
 */
public class Task implements Runnable {

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName());
    }
}
