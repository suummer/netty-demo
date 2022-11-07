package com.skyline.demo.nettydemo.ws.server.context;

import io.netty.channel.Channel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author skyline
 * @date 2022-09-21
 */
public class ClientContext {


    /**
     * key cid
     * val channel
     */
    public static final Map<String, Channel> CLIENT_CHANNEL_MAP = new ConcurrentHashMap<>();

}
