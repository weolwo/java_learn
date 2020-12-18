package com.poplar.juc.queue;

import java.util.concurrent.DelayQueue;

/**
 *
 */
public class DelayedQueueTest {

    public static void main(String[] args) {
        DelayQueue<MovieTicket> delayQueue = new DelayQueue<>();
        MovieTicket ticket = new MovieTicket("电影票0", 10000);
        delayQueue.put(ticket);
        MovieTicket ticket1 = new MovieTicket("电影票1", 5000);
        delayQueue.put(ticket1);
        MovieTicket ticket2 = new MovieTicket("电影票2", 8000);
        delayQueue.put(ticket2);
        System.out.println("message:--->入队完毕");

        while (delayQueue.size() > 0) {
            try {
                ticket = delayQueue.take();
                System.out.println("电影票出队:" + ticket.getMsg());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
