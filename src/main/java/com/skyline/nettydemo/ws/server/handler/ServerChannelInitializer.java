package com.skyline.nettydemo.ws.server.handler;

import com.skyline.nettydemo.ws.common.constant.WsConstant;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketFrameAggregator;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketServerCompressionHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * ChannelInitializer是一个特殊的处理器，用于配置新的 Channel
 *
 * @author skyline
 * @date 2022-09-19
 */
public class ServerChannelInitializer extends ChannelInitializer<SocketChannel> {

    private final SslContext sslCtx;

    public ServerChannelInitializer(SslContext sslCtx) {
        this.sslCtx = sslCtx;
    }


    @Override
    public void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        // 支持 ssl
        if (sslCtx != null) {
            pipeline.addLast(sslCtx.newHandler(ch.alloc()));
        }
        // http 编解码：HttpServerCodec 是 HttpRequestDecoder 和 HttpResponseEncoder 的组合，针对 http协议进行编解码
        pipeline.addLast(new HttpServerCodec());
        // 大文件写入：分块向客户端写数据，防止发送大文件时导致内存溢出，channel.write(new ChunkedFile(new File("bigFile.mkv")))
        pipeline.addLast(new ChunkedWriteHandler());
        // 聚合 http 的数据帧
        pipeline.addLast(new HttpObjectAggregator(65536));
        // 数据压缩扩展：此时 WebSocketServerProtocolHandler 的第三个参数需要设置成true
        pipeline.addLast(new WebSocketServerCompressionHandler());
        // 聚合 websocket 的数据帧：客户端可能分段向服务器端发送数据，https://github.com/netty/netty/issues/1112 https://github.com/netty/netty/pull/1207
        pipeline.addLast(new WebSocketFrameAggregator(10 * 1024 * 1024));


        // 处理 ws 协议带参数的问题
        pipeline.addLast(new ServerParamHandler());
        // 客户端认证
        pipeline.addLast(new ServerAuthorizeHandler());
        // 处理 websocket 握手、Close、Ping、Pong 数据帧：Text、Binary 数据帧会进行向后处理
        pipeline.addLast(new WebSocketServerProtocolHandler(WsConstant.WS_CONNECT_URI, null, true));


        // 处理 index 页的 http 请求
        pipeline.addLast(new ServerIndexPageHandler(WsConstant.WS_CONNECT_URI));
        // 处理数据帧
        pipeline.addLast(new ServerMessageHandler());
    }

}
