package com.skyline.demo.nettydemo.ws.server.handler;

import com.skyline.demo.nettydemo.ws.server.util.IndexPageUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.handler.ssl.SslHandler;
import lombok.extern.slf4j.Slf4j;

import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpMethod.GET;
import static io.netty.handler.codec.http.HttpResponseStatus.*;

/**
 * @author skyline
 * @date 2022-09-20
 */
@Slf4j
public class ServerIndexPageHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    private final String websocketPath;

    public ServerIndexPageHandler(String websocketPath) {
        this.websocketPath = websocketPath;
    }


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest req) throws Exception {
        log.info("[read FullHttpRequest]");
        // Handle a bad request.
        if (!req.decoderResult().isSuccess()) {
            sendHttpResponse(ctx, req, new DefaultFullHttpResponse(req.protocolVersion(), BAD_REQUEST, ctx.alloc().buffer(0)));
            return;
        }

        // Allow only GET methods.
        if (!GET.equals(req.method())) {
            sendHttpResponse(ctx, req, new DefaultFullHttpResponse(req.protocolVersion(), FORBIDDEN, ctx.alloc().buffer(0)));
            return;
        }

        // 重定向到 index 页
        if ("/".equals(req.uri()) || "/index.html".equals(req.uri())) {
            String webSocketLocation = getWebSocketLocation(ctx.pipeline(), req, websocketPath);
            ByteBuf content = IndexPageUtil.getContent(webSocketLocation);

            FullHttpResponse res = new DefaultFullHttpResponse(req.protocolVersion(), OK, content);
            res.headers().set(CONTENT_TYPE, "text/html; charset=UTF-8");
            HttpUtil.setContentLength(res, content.readableBytes());

            sendHttpResponse(ctx, req, res);
        } else {
            sendHttpResponse(ctx, req, new DefaultFullHttpResponse(req.protocolVersion(), NOT_FOUND, ctx.alloc().buffer(0)));
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

    private static void sendHttpResponse(ChannelHandlerContext ctx, FullHttpRequest req, FullHttpResponse res) {
        // Generate an error page if response getStatus code is not OK (200).
        HttpResponseStatus responseStatus = res.status();
        if (responseStatus.code() != 200) {
            ByteBufUtil.writeUtf8(res.content(), responseStatus.toString());
            HttpUtil.setContentLength(res, res.content().readableBytes());
        }
        // Send the response and close the connection if necessary.
        boolean keepAlive = HttpUtil.isKeepAlive(req) && responseStatus.code() == 200;
        HttpUtil.setKeepAlive(res, keepAlive);
        ChannelFuture future = ctx.writeAndFlush(res);
        if (!keepAlive) {
            future.addListener(ChannelFutureListener.CLOSE);
        }
    }

    private static String getWebSocketLocation(ChannelPipeline cp, HttpRequest req, String path) {
        String protocol = "ws";
        if (cp.get(SslHandler.class) != null) {
            // SSL in use so use Secure WebSockets
            protocol = "wss";
        }
        return protocol + "://" + req.headers().get(HttpHeaderNames.HOST) + path;
    }

}
