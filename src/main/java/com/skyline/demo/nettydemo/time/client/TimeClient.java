package com.skyline.demo.nettydemo.time.client;

import com.skyline.demo.nettydemo.time.client.handler.TimeClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * Netty中服务器和客户端之间最大和唯一的区别在于使用了不同的 Bootstrap 和 Channel 实现
 *
 * @author skyline
 * @date 2022-09-19
 */
public class TimeClient {

    public static void main(String[] args) throws Exception {
        String host = args[0];
        int port = Integer.parseInt(args[1]);

        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            // Bootstrap类似于 ServerBootstrap，区别在于 Bootstrap用于非服务端 Channel，如客户端 Channel或无连接 Channel
            Bootstrap b = new Bootstrap();
            // 如果只指定一个 EventLoopGroup，它将同时用于 boss group 和 worker group，但 boss group不用于客户端
            b.group(workerGroup);
            // NioSocketChannel类似于 NioServerSocketChannel，区别是 NioSocketChannel用于创建客户端 Channel
            b.channel(NioSocketChannel.class);
            // 客户端不使用 childOption，因为客户端的 SocketChannel没有父级
            b.option(ChannelOption.SO_KEEPALIVE, true);
            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new TimeClientHandler());
                }
            });

            // Start the client.
            // 启动客户端调用 connect()，而不是bind()
            ChannelFuture f = b.connect(host, port).sync();

            // Wait until the connection is closed.
            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
        }
    }

}
