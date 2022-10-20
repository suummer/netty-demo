package com.skyline.nettydemo.ws.client.handler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketClientCompressionHandler;
import io.netty.handler.ssl.SslContext;

/**
 * @author skyline
 * @date 2022-09-20
 */
public class ClientChannelInitializer extends ChannelInitializer<SocketChannel> {

    private final SslContext sslCtx;

    private final String host;

    private final int port;

    private final ClientObjectHandler handler;

    /**
     * 最大内容大小，默认为 8192 bytes
     */
    private final static int MAX_CONTENT_SIZE = 1024 * 1024 * 4;

    public ClientChannelInitializer(SslContext sslCtx, String host, int port, ClientObjectHandler handler) {
        this.sslCtx = sslCtx;
        this.host = host;
        this.port = port;
        this.handler = handler;
    }

    @Override
    protected void initChannel(SocketChannel ch) {
        ChannelPipeline pipeline = ch.pipeline();
        if (sslCtx != null) {
            pipeline.addLast(sslCtx.newHandler(ch.alloc(), host, port));
        }
        pipeline.addLast(new HttpClientCodec());
        pipeline.addLast(new HttpObjectAggregator(MAX_CONTENT_SIZE));
        pipeline.addLast(WebSocketClientCompressionHandler.INSTANCE);
        pipeline.addLast(handler);
    }

}
