package com.skyline.nettydemo.ws.server.util;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.handler.codec.http.*;

/**
 * @author skyline
 * @date 2022-09-21
 */
public class ResponseUtil {

    public static void responseAndClose(Channel channel, HttpVersion version, HttpResponseStatus status) {
        HttpResponse resp = new DefaultHttpResponse(version, status);
        channel.writeAndFlush(resp).addListener(ChannelFutureListener.CLOSE);
    }

    public static void responseAndClose(Channel channel, HttpVersion version, HttpResponseStatus status, ByteBuf content) {
        HttpResponse resp = new DefaultFullHttpResponse(version, status, content);
        channel.writeAndFlush(resp).addListener(ChannelFutureListener.CLOSE);
    }

}
