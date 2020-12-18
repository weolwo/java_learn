package com.poplar.jmm;

import com.poplar.juc.aqs.CustomLock;

/**
 * Create BY poplar ON 2020/12/8
 * <p>volatile不能保证原子性</p>
 * <p>并发编程三大特性:可见性，原子性，有序性</p>
 * <p>volatile保证可见性与有序性，但是不保证原子性，保证原子性需要借助
 * synchronized这样的锁机制</p>
 * <p>不同CPU硬件对于JVM的内存屏障规范实现指令不一样</p>
 * <b>Intel CPU硬件级内存屏障实现指令</b>
 * <ul>
 *     <li>Ifence: 是一种Load Barrier读屏障，实现LoadLoad屏障</li>
 *     <li>sfence:是一种Store Barrier 写屏障，实现StoreStore屏障</li>
 *     <li>mfence:是一 种全能型的屏障，具备lfence和sfence的能力， 具有所有屏障能力</li>
 * </ul>
 *
 * <p>JVM底层简化了内存屏障硬件指令的实现</p>
 * <p>lock前缀: lock指令不是一种内存屏障， 但是它能完成类似内存屏障的功能</p>
 */
public class VolatileAtomicTest {

    public static volatile int num = 0;

    public static void increase() {
        num++;
    }

    public static void main(String[] args) throws InterruptedException {
        CustomLock lock = new CustomLock();
        Thread[] threads = new Thread[10];
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < 1000; j++) {
                    lock.lock();
                    increase();
                    lock.unlock();
                }
            });

            threads[i].start();
        }

        for (Thread thread : threads) {
            thread.join();
        }
        System.out.println(num);
    }
}
