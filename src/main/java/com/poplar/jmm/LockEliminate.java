package com.poplar.jmm;

/**
 * Create BY poplar ON 2020/12/12
 * <h3>锁消除</h3>
 * 像下面这种代码jvm通过逃逸分析后，当前的对象锁不可能发生逃逸行为，所以会消除锁
 */
public class LockEliminate {

    public static void main(String[] args) {

        synchronized (new Object()) {
            //一堆逻辑代码
        }
    }
}
