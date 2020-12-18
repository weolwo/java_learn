package com.poplar.juc.lock;

import com.poplar.bean.User;
import org.openjdk.jol.info.ClassLayout;

import java.util.concurrent.TimeUnit;

/**
 * Create BY poplar ON 2020/12/10
 */
public class LockUpgradeTest {

    public static void main(String[] args) throws InterruptedException {
        User temp = new User();
        System.out.println("无锁状态（001）：" + ClassLayout.parseInstance(temp).toPrintable());
        //jvm默认演示4秒开启偏向锁，可以通过参数更改-XX:BiasedLockingStartupDelay=0 取消延时
        //如果不需要偏向锁可以通过：XX:-UseBiasedLocking=false 来设置
        TimeUnit.SECONDS.sleep(5);
        User user = new User();
        System.out.println("启用偏向锁（101）：" + ClassLayout.parseInstance(user).toPrintable());

        for (int i = 0; i < 2; i++) {
            synchronized (user) {
                System.out.println("偏向锁（101）带线程id()：" + ClassLayout.parseInstance(user).toPrintable());
            }
            System.out.println("释放偏向锁（101）带线程id()：" + ClassLayout.parseInstance(user).toPrintable());
        }

        new Thread(() -> {
            synchronized (user) {
                System.out.println("轻量级锁（00）：" + ClassLayout.parseInstance(user).toPrintable());
                try {
                    System.out.println("睡眠3秒钟-----------------");
                    TimeUnit.SECONDS.sleep(3);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("轻量 ->重量（10）：" + ClassLayout.parseInstance(user).toPrintable());
            }
        }).start();

        TimeUnit.SECONDS.sleep(1);
        new Thread(() -> {
            synchronized (user) {
                System.out.println("重量级锁（10）：" + ClassLayout.parseInstance(user).toPrintable());
            }
        }).start();
    }
}
