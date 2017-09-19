package com.hason.service;

/**
 * Hello 服务
 *
 * @author Huanghs
 * @since 2.0
 * @date 2017/9/19
 */
public class HelloService {

    private String msg;

    public String sayHello() {
        return "Hello " + msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
