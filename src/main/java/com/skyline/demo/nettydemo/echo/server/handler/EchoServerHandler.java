package com.skyline.demo.nettydemo.echo.server.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * ChannelInboundHandlerAdapter是 ChannelInboundHandler的一种实现，ChannelInboundHandler提供了一些事件处理的方法
 *
 * @author skyline
 * @date 2022-09-19
 */
public class EchoServerHandler extends ChannelInboundHandlerAdapter {


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ctx.write("\nstart\n");
        ctx.write(msg);
        ctx.write("\nend\n");

        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        // Close the connection when an exception is raised.
        cause.printStackTrace();
        ctx.close();
    }

}
