package com.hason.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

// 自动刷新配置
@RefreshScope
@RestController
public class ConfigClientController {

    // 绑定 Git 仓库配置文件中的 profile 属性
    @Value("${profile}")
    private String profile;

    @GetMapping("/profile")
    public String hell() {
        return this.profile;
    }

}
