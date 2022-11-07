package com.skyline.demo.nettydemo.ws.server;

import com.skyline.demo.nettydemo.ws.server.handler.ServerChannelInitializer;
import com.skyline.demo.nettydemo.ws.server.util.SslUtil;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;

/**
 * @author skyline
 * @date 2022-09-19
 */
public class WsServer {

    private final int port;

    public WsServer(int port) {
        this.port = port;
    }


    public void run() throws Exception {
        // NioEventLoopGroup 为处理 I/O 操作的多线程事件循环
        // 第一个通常被称为 boss，接受传入的连接
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        // 第二个通常称为 worker，一旦 boss接受连接并将该连接注册到 worker， worker就处理该连接
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        final SslContext sslCtx = SslUtil.buildSslContext();

        try {
            // ServerBootstrap是一个设置服务器的助手类
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    // 使用 NioServerSocketChannel接收传入的连接并实例化新的 Channel
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ServerChannelInitializer(sslCtx))
                    // 设置 Channel的参数
//                    .option(ChannelOption.SO_BACKLOG, 128)
//                    .childOption(ChannelOption.SO_KEEPALIVE, true)
            ;

            // Bind and start to accept incoming connections.
            // 绑定端口并启动服务器
            ChannelFuture f = b.bind(port).sync();

            // Wait until the server socket is closed.
            // In this example, this does not happen, but you can do that to gracefully
            // shut down your server.
            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {
        int port = 8080;
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        }

        new WsServer(port).run();
    }

}
