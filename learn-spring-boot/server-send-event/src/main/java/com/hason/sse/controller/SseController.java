package com.hason.sse.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

/**
 * 演示 SSE 服务器推送技术
 *
 * @author Huanghs
 * @since 2.0
 * @date 2017/12/1
 */
@Controller
public class SseController {

    private static final List<SseEmitter> EMITTERS =
            new CopyOnWriteArrayList<>();

    @Autowired
    private HttpServletResponse response;

    /**
     * SSE Emitter 页面
     */
    @RequestMapping("/sse-emitter")
    public String page() {
        return "sse-emitter";
    }

    @Scheduled(cron = "0/1 * * * * ?")
    public void task() {
        for (SseEmitter emitter : EMITTERS) {
            try {
                emitter.send(SseEmitter.event().data(LocalDateTime.now().toString()));
            } catch (IOException | IllegalStateException e) {
//                e.printStackTrace();
            }
        }
    }

    /**
     * 使用 Spring SseEmitter 实现推送，SseEmitter 会被包装成 DeferredResult
     *
     * 异步请求，连接超时后，客户端重新发送请求，重连间隔由浏览器决定，重新连接成功前无法收到推送的消息(会丢失)
     */
    @RequestMapping(value = "/sse/emitter")
    public SseEmitter emitter() {
        SseEmitter emitter = new SseEmitter(40_000L);
        emitter.onTimeout(() -> EMITTERS.remove(emitter));
        emitter.onCompletion(() -> EMITTERS.remove(emitter));
        EMITTERS.add(emitter);
        return emitter;
    }


    // ====================== 分隔线 ======================== //


    /**
     * SSE Emitter 页面
     */
    @RequestMapping("/sse-response")
    public String responsePage() {
        return "sse-response";
    }

    /**
     * 直接返回 SSE 数据，格式： data:自定义字符串数据\n\n
     * 详细说明： https://www.ibm.com/developerworks/cn/web/1307_chengfu_serversentevent/#major3
     *
     * 返回数据后，客户端的连接会结束并重新请求，重连间隔由浏览器决定
     */
    @RequestMapping(value = "/sse/response", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @ResponseBody
    public String sse() throws InterruptedException {
        return "data:"
                + LocalDateTime.now().toString()
                + "\n\n";
    }

    /**
     * 这里手动填写 response 实现返回数据
     * 问题：没有异步超时，占用着线程，是否伪 SSE？
     */
    private String fuxkSseResponse() {
        response.setHeader("Content-Type", MediaType.TEXT_EVENT_STREAM_VALUE);
        try {
            ServletOutputStream out = response.getOutputStream();
            while (true) {
                String data = "data:"
                        + LocalDateTime.now().toString()
                        + "\n\n";
                out.print(data);
                out.flush();
                TimeUnit.SECONDS.sleep(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "data:"
                + LocalDateTime.now().toString()
                + "\n\n";
    }

}
