package com.poplar.juc.queue;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 从头部取尾部插入新的元素，典型的生产者和消费者的模式
 */
public class ArrayBlockingQueueTest {
    /**
     * 创建容量大小为1的有界队列
     */
    private BlockingQueue<Ball> blockingQueue = new ArrayBlockingQueue<>(1);

    /**
     * 将球放入队列当中,生产者
     *
     * @param ball
     * @throws InterruptedException
     */
    public void produce(Ball ball) throws InterruptedException {
        blockingQueue.put(ball);
    }

    /**
     * 将球从队列当中拿出去，消费者
     *
     * @return
     */
    public Ball consume() throws InterruptedException {
        return blockingQueue.take();
    }

    public static void main(String[] args) {
        final ArrayBlockingQueueTest box = new ArrayBlockingQueueTest();
        ExecutorService executorService = Executors.newCachedThreadPool();

        /**
         * 往箱子里面放入乒乓球
         */
        executorService.submit((Runnable) () -> {
            int i = 0;
            while (true) {
                Ball ball = new Ball();
                ball.setNumber("乒乓球编号:" + i);
                ball.setColor("yellow");
                try {
                    box.produce(ball);
                    System.out.println( "put往箱子里放入乒乓球--->" + ball.getNumber());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                i++;
            }
        });

        /**
         * consumer，负责从箱子里面拿球出来
         */
        executorService.submit((Runnable) () -> {
            while (true) {
                try {
                    Ball ball = box.consume();
                    System.out.println("take箱子中的乒乓球--->" + ball.getNumber());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

    }

}
