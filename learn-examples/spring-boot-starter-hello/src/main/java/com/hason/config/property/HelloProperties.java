package com.hason.config.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by Hason on 2017/8/17.
 */
@ConfigurationProperties(prefix = "hello")
public class HelloProperties {

    public static final String MSG = "world";

    private String msg = MSG;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
