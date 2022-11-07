package com.skyline.demo.nettydemo.timev2.client.handler;

import com.skyline.demo.nettydemo.timev2.client.model.UnixTime;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author skyline
 * @date 2022-09-19
 */
public class TimeEncoderV2 extends MessageToByteEncoder<UnixTime> {

    @Override
    protected void encode(ChannelHandlerContext ctx, UnixTime msg, ByteBuf out) {
        out.writeInt((int) msg.value());
    }

}
