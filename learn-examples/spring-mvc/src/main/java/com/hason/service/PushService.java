package com.hason.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;

/**
 * Created by Hason on 2017/8/16.
 */
@Service
public class PushService {

    private DeferredResult<String> deferredResult;

    /**
     * 产生 DeferredResult 给控制器使用
     */
    public DeferredResult<String> getAsyncUpdate() {
        deferredResult = new DeferredResult<>();
        return deferredResult;
    }

    @Scheduled(fixedDelay = 5000)
    public void refresh() {
        if (deferredResult != null) {
            deferredResult.setResult(Long.toString(System.currentTimeMillis()));
        }
    }

}
