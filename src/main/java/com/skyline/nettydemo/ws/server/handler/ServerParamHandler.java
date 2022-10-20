package com.skyline.nettydemo.ws.server.handler;

import cn.hutool.core.net.url.UrlQuery;
import com.skyline.nettydemo.ws.common.constant.WsConstant;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;

import java.nio.charset.Charset;


/**
 * 解决 ws 协议带请求参数的问题
 *
 * @author skyline
 * @date 2022-09-20
 */
public class ServerParamHandler extends SimpleChannelInboundHandler<FullHttpRequest> {


    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        ctx.channel().attr(WsConstant.ATTRIBUTE_PARAMS).getAndSet(null);

        super.channelInactive(ctx);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {

        Channel channel = ctx.channel();

        String uri = request.uri();
        String path = uri.split("\\?")[0];

        if (WsConstant.WS_CONNECT_URI.equals(path)) {

            // 提取原始请求参数，并绑定到 channel 属性中
            UrlQuery params = UrlQuery.of(uri, Charset.defaultCharset());
            channel.attr(WsConstant.ATTRIBUTE_PARAMS).set(params.getQueryMap());

            // 解决 ws 协议带请求参数的问题，重写 uri
            request.setUri(WsConstant.WS_CONNECT_URI);
        }

        // 向 pipeline 向下传递 message，request 的引用计数 +1
        ctx.fireChannelRead(request.retain());
    }

}
