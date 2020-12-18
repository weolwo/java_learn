package com.poplar.jmm;

import java.util.concurrent.TimeUnit;

/**
 * Create BY poplar ON 2020/12/8
 * <h1>验证java jmm模型,引出volatile关键字和jmm</h1>
 *
 * <h2>JMM数据的原子操作：</h2>
 *
 * <p> read (读取) :从主内存读取数据</p>
 * <p> load (载入) :将主内存读取到的数据写入工作内存</p>
 * <p> use(使用) :从工作内存读取数据来计算</p>
 * <p> assign (赋值) :将计算好的值重新赋值到工作内存中</p>
 * <p> store (存储) :将工作内存数据写入主内存</p>
 * <p> write(写入) :将store过去的变量值赋值给主内存中的变量</p>
 * <p> lock (锁定) :将主内存变量加锁，标识为线程独占状态</p>
 * <p> unlock (解锁) :将主内存变量解锁，解锁后其他线程可以锁定该变量</p>
 *
 * <h2>总线加锁(性能太低)</h2>
 * <p>
 * cpu从主内存读取数据到高速缓存，会在总线对这个数据加锁，这样其它cpu没法
 * 去读或写这个数据，直到这个cpu使用完数据释放锁之后其它cpu才能读取该数据
 * <h2>MESI缓存一致性协议</h2>
 * <p>
 * 多个cpu从主内存读取同一个数据到各自的高速缓存，当其中某个cpu修改了缓存
 * 里的数据，该数据会马上同步回主内存，其它cpu通过总线嗅探机制(类似于消息队列)可以感知到数
 * 据的变化从而将自己缓存里的数据失效
 *
 * <h2>Volatile缓存可见性实现原理</h2>
 * 底层实现主要是通过汇编lock前缀指令，它会锁定这块内存区域的缓存(缓存行锁定)
 * 并回写到主内存
 * IA-32架构软件开发者手册对lock指令的解释:
 * <p> 1)会将当前处理器缓存行的数据立即写回到系统内存。</p>
 * <p> 2)这个写回内存的操作会引起在其他CPU里缓存了该内存地址的数据无效(MESI协议)</p>
 * <h2>Java程序汇编代码查看</h2>
 * -server -Xcomp -XX:+UnlockDiagnosticVMOptions -XX:+PrintAssembly
 * -XX:CompileCommand=compileonly,*VolatileVisibleTest.prepareData
 * 注意：jre需要选择放有 hsdis-amd64.dll
 */
public class VolatileVisibleTest {

    private /*volatile*/ static boolean initFlag = false;

    public static void main(String[] args) throws InterruptedException {
        new Thread(() -> {
            System.out.println("wait data......");
            while (!initFlag) {
                /*输出语句会触发线程上下文切换，打印语句会导致打印设备的线程被调用，从而当线程0重新得到cpu执行权时，
                会恢复之前的线程上下文数据，就有可能触发线程0重新从主存加载数据，*/
                System.out.println("dd" + Thread.currentThread().getName());
            }
            System.out.println("...........success");
        }).start();

        TimeUnit.SECONDS.sleep(2);

        new Thread(() -> {
            System.out.println("prepare data......");
            prepareData();
            System.out.println("end data......");
        }).start();
    }

    private static void prepareData() {
        initFlag = true;
    }
}
