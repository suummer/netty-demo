package com.skyline.nettydemo.ws.common.constant;

import com.skyline.nettydemo.ws.common.model.Client;
import io.netty.util.AttributeKey;

import java.util.Map;

/**
 * @author skyline
 * @date 2022-09-21
 */
public class WsConstant {

    public static final String WS_CONNECT_URI = "/websocket";

    public static final String PARAMS = "params";

    public static final String CLIENT = "client";

    public static final String CID = "cid";

    /**
     * channel属性：连接请求参数
     */
    public static final AttributeKey<Map<CharSequence, CharSequence>> ATTRIBUTE_PARAMS = AttributeKey.valueOf(PARAMS);

    /**
     * channel属性：客户端ID
     */
    public static final AttributeKey<String> ATTRIBUTE_CID = AttributeKey.valueOf(CID);

    /**
     * channel属性：客户端对象
     */
    public static final AttributeKey<Client> ATTRIBUTE_CLIENT = AttributeKey.valueOf(CLIENT);

}
