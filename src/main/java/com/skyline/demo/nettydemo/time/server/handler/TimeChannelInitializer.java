package com.skyline.demo.nettydemo.time.server.handler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

/**
 * ChannelInitializer是一个特殊的处理器，用于配置新的 Channel
 *
 * @author skyline
 * @date 2022-09-19
 */
public class TimeChannelInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    public void initChannel(SocketChannel ch) throws Exception {
        // 配置 ChannelPipeline来添加一些处理器
        ch.pipeline().addLast(new TimeServerHandler());
    }

}
