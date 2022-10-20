package com.skyline.nettydemo.time.server.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * ChannelInboundHandlerAdapter是 ChannelInboundHandler的一种实现，ChannelInboundHandler提供了一些事件处理的方法
 *
 * @author skyline
 * @date 2022-09-19
 */
public class TimeServerHandler extends ChannelInboundHandlerAdapter {

    /**
     * 连接建立时调用的方法
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // 分配一个新的 buffer
        // netty中的 ByteBuf，不需要通过 java.nio.ByteBuffer中 flip()的方式来切换读写模式 http://t.zoukankan.com/wzj4858-p-8205587.html
        // 因为 ByteBuf区分了读写的指针
        final ByteBuf time = ctx.alloc().buffer(4);
        time.writeInt((int) (System.currentTimeMillis() / 1000L + 2208988800L));

        // ChannelFuture代表了一个还没有发生的 I/O操作，因为 netty中的所有操作都是异步的，以下操作可能导致 message发送前 Channel被关闭
        //   Channel ch = ...;
        //   ch.writeAndFlush(message);
        //   ch.close();
        //
        // write()操作返回一个 ChannelFuture，同时通知 ChannelFuture的 listeners
        final ChannelFuture f = ctx.writeAndFlush(time);
        // 添加一个 预定义的监听器，在操作完成时关闭 Channel
        // 该监听器等价于以下匿名监听器
        //    f.addListener(new ChannelFutureListener() {
        //       @Override
        //       public void operationComplete(ChannelFuture future) {
        //         assert f == future;
        //         // close()方法也是异步的，ctx
        //         ctx.close();
        //       }
        //    });
        f.addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        // Close the connection when an exception is raised.
        cause.printStackTrace();
        ctx.close();
    }

}
