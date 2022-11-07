package com.skyline.demo.nettydemo.ws.common.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author skyline
 * @date 2022-09-20
 */
@Data
public class WsMessage implements Serializable {

    private String target;

    private String content;

    @Override
    public String toString() {
        return "WsMessage[" +
                "target='" + target + ", '" +
                "content='" + content + '\'' +
                ']';
    }

}
