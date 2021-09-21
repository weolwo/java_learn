package com.poplar.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * BY Alex CREATED 2021/7/12
 * netty 測試
 */


public class NettyServerTest {

    public static void main(String[] args) {

        EventLoopGroup boss = new NioEventLoopGroup(1);
        EventLoopGroup workers = new NioEventLoopGroup(3);

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(boss, workers).channel(NioServerSocketChannel.class).childHandler(new ChannelInitializerTest());
            ChannelFuture channelFuture = bootstrap.bind(9000).sync();
            System.out.println("netty started success");
            channelFuture.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //表示优雅关闭，表示关闭Channel和清除服务端的资源
            boss.shutdownGracefully();
            workers.shutdownGracefully();
        }
    }
}
