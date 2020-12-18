package com.poplar.api;

import sun.misc.Unsafe;

import java.util.SplittableRandom;

/**
 * Create BY poplar ON 2020/11/27
 * ThreadLocalRandom 推荐使用
 * SplittableRandom 对于fork join Pool并行的stream
 */
public class RandomTest {

    public static void main(String[] args) {

        //System.out.println(ThreadLocalRandom.current().nextInt(2));
        SplittableRandom random = new SplittableRandom();
        System.out.println(random.nextInt(5));
        Unsafe unsafe = Unsafe.getUnsafe();
        unsafe.compareAndSwapInt(1,1,1,1);
    }
}
