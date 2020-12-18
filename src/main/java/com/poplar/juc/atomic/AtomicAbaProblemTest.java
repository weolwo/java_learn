package com.poplar.juc.atomic;

import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 */
public class AtomicAbaProblemTest {
    static AtomicInteger atomicInteger = new AtomicInteger(1);

    public static void main(String[] args) {
        Thread main = new Thread(() -> {
            int a = atomicInteger.get();
            System.out.println("操作线程" + Thread.currentThread().getName() + "--修改前操作数值:" + a);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            boolean isCasSuccess = atomicInteger.compareAndSet(a, 2);
            if (isCasSuccess) {
                System.out.println("操作线程" + Thread.currentThread().getName() + "--Cas修改后操作数值:" + atomicInteger.get());
            } else {
                System.out.println("CAS修改失败");
            }

        }, "主线程");

        Thread other = new Thread(() -> {
            atomicInteger.incrementAndGet();// 1+1 = 2;
            System.out.println("操作线程" + Thread.currentThread().getName() + "--increase后值:" + atomicInteger.get());
            atomicInteger.decrementAndGet();// atomic-1 = 2-1;
            System.out.println("操作线程" + Thread.currentThread().getName() + "--decrease后值:" + atomicInteger.get());
        }, "干扰线程");

        main.start();
        other.start();

    }
}
