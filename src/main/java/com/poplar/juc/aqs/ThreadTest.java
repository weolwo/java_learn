package com.poplar.juc.aqs;

import java.util.concurrent.TimeUnit;

/**
 * Create BY poplar ON 2020/12/9
 * <p>用户线程(ULT):用户程序实现，不依赖操作系统核心，应用提供创建、同步调度和管理线程的函数来控制用户线
 * 程。不需要用户态/内核态切换，速度快。内核对ULT无感知，线程阻塞则进程(包括它的所有线程)阻塞。</p>
 * <hr>
 * <p>内核线程(KLT):系统内核管理线程(KLT),内核保存线程的状态和上下文信息，线程阻塞不会引起进程阻塞。在多
 * 处理器系统上，多线程在多处理器上并行运行。线程的创建、调度和管理由内核完成，效率比ULT要慢，比进程操
 * 作快。</p>
 * <hr>
 * <p>线程是稀缺资源，它的创建与销毁是-个相对偏重且耗资源的操作，而Java线程依赖于内核线程，创建线程需
 * 要进行操作系统状态切换，为避免资源过度消耗需要设法重用线程执行多个任务。线程池就是一个线程缓存，
 * 负责对线程进行统一分配、 调优与监控。</p>
 */
public class ThreadTest {

    public static void main(String[] args) {
        //通过查看任务管理器中线程变化验证jvm属于那种类型的线程,通过测试可以得出jvm属于内核线程(KLT)
        Thread[] threads = new Thread[1000];
        for (int i = 0; i < threads.length; i++) {

            new Thread(() -> {
                try {
                    TimeUnit.SECONDS.sleep(1);
                    System.out.println("线程类型测试....");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }
}
