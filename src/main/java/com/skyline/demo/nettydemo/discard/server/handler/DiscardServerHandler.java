package com.skyline.demo.nettydemo.discard.server.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCountUtil;

/**
 * ChannelInboundHandlerAdapter是 ChannelInboundHandler的一种实现，ChannelInboundHandler提供了一些事件处理的方法
 *
 * @author skyline
 * @date 2022-09-19
 */
public class DiscardServerHandler extends ChannelInboundHandlerAdapter {


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("read msg");
        try {
            System.out.println(((ByteBuf) msg).toString(CharsetUtil.UTF_8));
        } finally {
            ReferenceCountUtil.release(msg);
            System.out.println("released msg");
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        // Close the connection when an exception is raised.
        cause.printStackTrace();
        ctx.close();
    }

}
