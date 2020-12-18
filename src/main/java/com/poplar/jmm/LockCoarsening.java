package com.poplar.jmm;

/**
 * Create BY poplar ON 2020/12/12
 * <h3>锁粗化</h3>
 * 如果虚拟机探测到有这样一串零碎的操作
 * 都对同一个对象加锁，将会把加锁同步的范围扩展（粗化）到整个操作序列的外部，
 * 就是扩展到第一个append()操作之前直至最后一个append()操作之后，这样只需要加锁一次就可
 * 以了
 */
public class LockCoarsening {

    public String concatString(String s1, String s2, String s3) {
        StringBuffer sb = new StringBuffer();
        //类似于把每次调用时的加锁行为去掉，变成把这几次调用放在一个同步代码块中
        sb.append(s1);
        sb.append(s2);
        sb.append(s3);
        return sb.toString();
    }

    public static void main(String[] args) {
        LockCoarsening lockCoarsening = new LockCoarsening();
        lockCoarsening.concatString("I", "love", "you");
    }
}
