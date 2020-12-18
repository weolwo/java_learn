package com.poplar.juc.queue;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 *
 */
public class MovieTicket implements Delayed {
    //延迟时间
    private final long delay;
    //到期时间
    private final long expire;
    //数据
    private final String msg;

    public String getMsg() {
        return msg;
    }

    //创建时间
    private final long now;

    /**
     * @param msg   消息
     * @param delay 延期时间
     */
    public MovieTicket(String msg, long delay) {
        this.delay = delay;
        this.msg = msg;
        expire = System.currentTimeMillis() + delay;    //到期时间 = 当前时间+延迟时间
        now = System.currentTimeMillis();
    }

    /**
     * @param msg
     */
    public MovieTicket(String msg) {
        this(msg, 1000);
    }

    public MovieTicket() {
        this(null, 1000);
    }

    /**
     * 获得延迟时间   用过期时间-当前时间,时间单位毫秒
     *
     * @param unit
     * @return
     */
    public long getDelay(TimeUnit unit) {
        return unit.convert(this.expire
                - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
    }

    /**
     * 用于延迟队列内部比较排序  当前时间的延迟时间 - 比较对象的延迟时间
     * 越早过期的时间在队列中越靠前
     *
     * @param delayed
     * @return
     */
    public int compareTo(Delayed delayed) {
        return (int) (this.getDelay(TimeUnit.MILLISECONDS)
                - delayed.getDelay(TimeUnit.MILLISECONDS));
    }

    @Override
    public String toString() {
        return "MovieTicket{" +
                "delay=" + delay +
                ", expire=" + expire +
                ", msg='" + msg + '\'' +
                ", now=" + now +
                '}';
    }
}
