package com.skyline.nettydemo.ws.server.handler;

import cn.hutool.core.lang.Assert;
import com.fasterxml.jackson.core.type.TypeReference;
import com.skyline.nettydemo.ws.common.model.WsMessage;
import com.skyline.nettydemo.ws.common.constant.WsConstant;
import com.skyline.nettydemo.ws.common.util.json.JsonUtil;
import com.skyline.nettydemo.ws.server.context.ClientContext;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;


/**
 * @author skyline
 * @date 2022-09-20
 */
@Slf4j
public class ServerMessageHandler extends SimpleChannelInboundHandler<WebSocketFrame> {


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, WebSocketFrame frame) throws Exception {
        // ping and pong frames already handled

        Channel channel = ctx.channel();

        String cid = channel.attr(WsConstant.ATTRIBUTE_CID).get();

        if (frame instanceof TextWebSocketFrame) {

            String request = ((TextWebSocketFrame) frame).text();
            log.info("[received message] cid: {}, message: {}", cid, request);

            // 解析消息
            WsMessage message = parseMessage(request);
            Assert.notNull(message);

            // 处理消息
            handleMessage(channel, cid, message);
        } else {
            String message = "unsupported frame type: " + frame.getClass().getName();
            throw new UnsupportedOperationException(message);
        }
    }

    private WsMessage parseMessage(String text) {
        return JsonUtil.json2GenericObject(text, new TypeReference<WsMessage>() {
        });
    }

    private void handleMessage(Channel channel, String source, WsMessage message){

        // 回显消息
        channel.writeAndFlush(new TextWebSocketFrame("send " + message));

        // 发送消息给目标
        String target = message.getTarget();
        if (StringUtils.isNotBlank(target)) {
            Channel targetChannel = ClientContext.CLIENT_CHANNEL_MAP.get(target);
            if (targetChannel != null && targetChannel.isOpen()) {
                ChannelFuture channelFuture = targetChannel.writeAndFlush(new TextWebSocketFrame(JsonUtil.toJson(message)));
//                if(channelFuture.isDone() || channelFuture.isSuccess()){
//                    channel.writeAndFlush(new TextWebSocketFrame("send message to " + target + " succeeded"));
//                    log.info("[transferred message] from: {}, to: {}", source, target);
//                }
            } else {
                channel.writeAndFlush(new TextWebSocketFrame("send message to " + target + " failed"));
            }
        }
    }

}
