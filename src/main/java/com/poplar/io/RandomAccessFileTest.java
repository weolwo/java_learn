package com.poplar.io;

import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.groupingBy;

/**
 * Create BY poplar ON 2020/11/19
 * 空间分配的三种方式
 * 堆外的数据，如果想写磁盘，通过系统调用后，经历，数据从用户空间内存拷贝到内核
 * 雄外MappedByteBuffer的数据内核直接处理
 */
public class RandomAccessFileTest {

    //堆内堆外数据区别，推内可以用对象的方式操作，但是堆外就只能按字节数组操作
    public static void main(String[] args) throws Exception {
        RandomAccessFile rf = new RandomAccessFile("hello.txt", "rw");
        rf.write("Hello pop".getBytes());
        FileChannel channel = rf.getChannel();
        //空间分配堆外，直接映射，内核可以直接访问
        //内核方法 mmap()
        MappedByteBuffer buffer = channel.map(FileChannel.MapMode.READ_WRITE, 10, 100);
        //ByteBuffer.allocate(1024);//分配到堆上
        //ByteBuffer.allocateDirect(1024);//分配到堆外
        buffer.put("Hello World".getBytes());
        Map<String, Long> collect = Stream.of("hello", "world").collect(groupingBy(String::toLowerCase, Collectors.counting()));
        Iterator<Map.Entry<String, Long>> iterator = collect.entrySet().iterator();
        System.out.println(ThreadLocalRandom.current().nextInt(10));

        Thread.sleep(11);
    }
}
