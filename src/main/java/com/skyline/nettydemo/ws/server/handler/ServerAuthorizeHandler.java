package com.skyline.nettydemo.ws.server.handler;

import com.skyline.nettydemo.ws.common.constant.WsConstant;
import com.skyline.nettydemo.ws.common.model.Client;
import com.skyline.nettydemo.ws.server.context.ClientContext;
import com.skyline.nettydemo.ws.server.util.ResponseUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Objects;

/**
 * 客户端校验
 *
 * @author skyline
 * @date 2022-09-20
 */
@Slf4j
public class ServerAuthorizeHandler extends SimpleChannelInboundHandler<FullHttpRequest> {


    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {

        Channel channel = ctx.channel();

        channel.attr(WsConstant.ATTRIBUTE_CLIENT).getAndSet(null);
        String cid = channel.attr(WsConstant.ATTRIBUTE_CID).getAndSet(null);

        ClientContext.CLIENT_CHANNEL_MAP.remove(cid);
        System.out.println("[client disconnected] cid: " + cid + ", total: " + ClientContext.CLIENT_CHANNEL_MAP.size());

        super.channelInactive(ctx);
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {

        Channel channel = ctx.channel();

        String path = request.uri().split("\\?")[0];

        if (WsConstant.WS_CONNECT_URI.equals(path)) {

            Map<CharSequence, CharSequence> paramMap = channel.attr(WsConstant.ATTRIBUTE_PARAMS).get();

            String cid = Objects.requireNonNull(paramMap.get(WsConstant.CID)).toString();

            Client client = new Client(cid);
            // todo 进行客户端校验
            if (!client.isValid()) {
                ResponseUtil.responseAndClose(channel, request.protocolVersion(), HttpResponseStatus.FORBIDDEN);
                return;
            }

            channel.attr(WsConstant.ATTRIBUTE_CID).set(cid);
            channel.attr(WsConstant.ATTRIBUTE_CLIENT).set(client);

            ClientContext.CLIENT_CHANNEL_MAP.put(cid, channel);
            log.info("[client connected] cid: {}, total: {}", cid, ClientContext.CLIENT_CHANNEL_MAP.size());
        }

        ctx.fireChannelRead(request.retain());
    }

}
