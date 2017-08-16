package com.hason.controller;

import com.hason.service.PushService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.async.DeferredResult;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * 演示服务端推送技术（SSE，Server Send Event）
 *
 * @author Huanghs
 * @since 2.0
 * @date 2017/8/16
 */
@Controller
public class SSEController {

    @Autowired
    private PushService pushService;

    @RequestMapping(value = "/push", produces = "text/event-stream;charset=utf-8")
    @ResponseBody
    public String push() {
        Random r = new Random();
        // 每5秒向浏览器推送随机消息
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "data:Testing 1,2,3" + r.nextInt() + "\n\n";
    }

    @RequestMapping(value = "/defer")
    @ResponseBody
    public DeferredResult<String> defer() {
        return pushService.getAsyncUpdate();
    }
}
