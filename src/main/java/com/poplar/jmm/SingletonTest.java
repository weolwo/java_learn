package com.poplar.jmm;

/**
 * Create BY poplar ON 2020/12/11
 * 查看汇编指令：-XX:+UnlockDiagnosticVMOptions -XX:+PrintAssembly -Xcomp
 */
public class SingletonTest {

    private static volatile SingletonTest instance = null;

    private static SingletonTest getInstance() {
        if (instance == null) {
            synchronized (SingletonTest.class) {
                if (instance == null) {
                    //对象创建的过程本质上可以分为三步。1.allocated（分配空间）2.给属性赋值，3.把内存地址赋值给引用
                    //但是如果在高并发情况下发生了指令重排序，导致2和3执行顺序发生改变
                    instance = new SingletonTest();
                }
            }
        }
        return instance;
    }

    public static void main(String[] args) {
        System.out.println(getInstance());
    }
}
