package com.hason.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;

public class PreRequestLogFilter extends ZuulFilter {

    private static final Logger logger = LoggerFactory.getLogger(PreRequestLogFilter.class);

    @Override
    public String filterType() {
        return "pre";   // 过滤器的类型
    }

    @Override
    public int filterOrder() {
        return 1;   // 过滤器的顺序
    }

    @Override
    public boolean shouldFilter() {
        return true;    //判断该过滤器是否要执行
    }

    @Override
    public Object run() {
        RequestContext currentContext = RequestContext.getCurrentContext();
        HttpServletRequest request = currentContext.getRequest();
        logger.info(String.format("Send %s request to %s",
                request.getMethod(), request.getRequestURL().toString()));
        return null;
    }
}
