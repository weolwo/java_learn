package com.poplar.juc.aqs;

import sun.misc.Unsafe;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.LockSupport;

/**
 * Create BY poplar ON 2020/12/8
 * 自定义公平锁
 * 测试方法见 com.poplar.jmm.VolatileAtomicTest
 */
public class CustomLock {

    //当前加锁状态，记录加锁的次数
    private volatile int state = 0;

    //当前持有锁的线程
    private Thread lockHolder;

    private static final Queue<Thread> queue = new LinkedBlockingQueue<>();

    public int getState() {
        return state;
    }

    public void setLockHolder(Thread lockHolder) {
        this.lockHolder = lockHolder;
    }

    public boolean tryAcquire() {
        int state = getState();
        //表示还没有任何线程持有锁
        Thread t = Thread.currentThread();
        if (state == 0) {
            if ((queue.size() == 0 || queue.peek() == t) && compareAndSwapState(0, 1)) {
                //记录当前持有锁的线程
                System.out.println(String.format("线程%s释获取成功", t.getName()));
                setLockHolder(t);
                return true;
            }
        }
        return false;
    }

    //加锁
    public void lock() {
        //加锁
        if (tryAcquire()) {
            return;
        }
        Thread currentThread = Thread.currentThread();
        queue.add(currentThread);
        //阻塞
        for (; ; ) {
            if (currentThread == queue.peek() && tryAcquire()) {
                System.out.println("thread Name：" + currentThread.getName());
                queue.poll();
                System.out.println("queue size: " + queue.size());
                return;
            }
            LockSupport.park();
        }
        //锁释放后，再次获取锁
    }

    //解锁
    public void unlock() {
        Thread currentThread = Thread.currentThread();
        if (currentThread != lockHolder) {
            throw new RuntimeException("非法操作，不是当前持有锁的线程");
        }
        int state = getState();
        if (compareAndSwapState(state, 0)) {
            System.out.println(String.format("线程%s释放锁成功", currentThread.getName()));
            setLockHolder(null);
            //唤醒队列头部的线程
            Thread thread = queue.peek();
            if (thread != null) {
                LockSupport.unpark(thread);
            }
        }
    }

    private static final Unsafe unsafe = UnsafeInstance.reflectGetUnsafe();

    private static long stateOffset;

    static {
        try {
            stateOffset = unsafe.objectFieldOffset(CustomLock.class.getDeclaredField("state"));
        } catch (Exception e) {
            throw new Error(e);
        }
    }

    /**
     * 原子操作
     *
     * @param oldValue 线程工作内存中的值
     * @param newValue 要替换的新值
     * @return
     */
    public final boolean compareAndSwapState(int oldValue, int newValue) {
        return unsafe.compareAndSwapInt(this, stateOffset, oldValue, newValue);
    }
}
