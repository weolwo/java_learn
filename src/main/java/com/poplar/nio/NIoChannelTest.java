package com.poplar.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Create BY poplar ON 2020/11/20
 * nio相关方法与系统调用的关系
 */
public class NIoChannelTest {

    public static void main(String[] args) throws Exception {
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.configureBlocking(false);
        ssc.bind(new InetSocketAddress(8089));//listen()
        Selector selector = Selector.open();//epoll_create()
        ssc.register(selector, SelectionKey.OP_ACCEPT);//epoll_ctl(int epfd)

        while (true) {
            int select = selector.select();//epoll_wait
            if (select == 0) continue;
            ArrayList<SelectionKey> list = new ArrayList<>(selector.keys());
            Iterator<SelectionKey> iterator = list.iterator();

            while (iterator.hasNext()) {

                SelectionKey key = iterator.next();

                try {
                    if (key.isAcceptable()) {
                        ServerSocketChannel channel = (ServerSocketChannel) key.channel();
                        SocketChannel sc = channel.accept();
                        if (sc != null) {
                            sc.configureBlocking(false);
                            sc.register(selector, SelectionKey.OP_READ);
                        }

                    } else if (key.isReadable()) {
                        SocketChannel sc = (SocketChannel) key.channel();
                        ByteBuffer buffer = ByteBuffer.allocate(1024);
                        int num = sc.read(buffer);//>0, 0, -1
                        if (num > 0) {
                            buffer.flip();
                            byte[] bytes = new byte[buffer.limit()];
                            buffer.get(bytes);
                            String string = new String(bytes);
                            System.out.println("receive form " + sc.socket().getPort() + "data: " + string);
                            buffer.clear();
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    key.cancel();
                }

                iterator.remove();
            }
        }
    }
}
