package com.skyline.demo.nettydemo.timev2.client.handler;

import com.skyline.demo.nettydemo.timev2.client.model.UnixTime;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * ByteToMessageDecoder是 ChannelInboundHandler的一种实现
 * 内部维护了一个累积缓冲区，每当接收到新数据时，调用 decode方法
 *
 * @author skyline
 * @date 2022-09-19
 */
public class TimeDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) { // (2)
        if (in.readableBytes() < 4) {
            return;
        }

        // 当out添加了一条数据，表示成功解码一条数据，ByteToMessageDecoder将丢弃累积缓冲区已读取的部分
        out.add(new UnixTime(in.readUnsignedInt()));

    }

}
