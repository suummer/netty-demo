package com.skyline.demo.nettydemo.ws.common.model;

import lombok.Getter;

import java.io.Serializable;

/**
 * @author skyline
 * @date 2022-09-21
 */
@Getter
public class Client implements Serializable {

    private final String cid;

    private boolean valid;


    public Client(String cid) {
        this.cid = cid;
    }

    public boolean isValid() {
        valid = valid || cid != null && cid.length() > 6;
        return valid;
    }

}
