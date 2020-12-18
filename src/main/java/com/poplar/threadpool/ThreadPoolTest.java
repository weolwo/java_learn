package com.poplar.threadpool;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Create BY poplar ON 2020/12/9
 *  <ul>
 *  <b>什么时候使用线程池?<b>
 * <li>单个任务处理时间比较短</li>
 * <li>需要处理的任务数量很大</li>
 * </ul>
 * <ul>
 *     <b>线程池优势</b>
 *     <li>重用存在的线程，减少线程创建，消亡的开销，提高性能</li>
 *     <li>提高响应速度。当任务到达时，任务可以不需要的等到线程创建就能立即执行。</li>
 *     <li>提高线程的可管理性，可统一分配，调优和监控。</li>
 * </ul>
 * <b>线程池的五种状态</b>
 * <ul>
 *     <li>Running
 *  能接受新任务以及处理已添加的任务
 *  </li>
 *     <li>Shutdown
 *   不接受新任务，可以处理已经添加的任务
 *   </li>
 *     <li>Stop
 *  不接受新任务，不处理已经添加的任务，并且中断正在处理的任务
 *  </li>
 *     <li>Tidying
 *  所有的任务已经终止，ctl记录的”任务数量”为0，ct1负责记录线程池的运行状态与活动线程数量
 *  </li>
 *     <li>Terminated
 *   线程池彻底终止，则线程池转变为terminated状态
 *   </li>
 * </ul>
 */
public class ThreadPoolTest {

    public static void main(String[] args) {

        ThreadPoolExecutor threadPool = new ThreadPoolExecutor(1, 1, 60, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(15), Executors.defaultThreadFactory());

        for (int i = 0; i < 10; i++) {
            threadPool.execute(() -> System.out.println(Thread.currentThread().getName()));
        }
        threadPool.shutdown();
    }
}
