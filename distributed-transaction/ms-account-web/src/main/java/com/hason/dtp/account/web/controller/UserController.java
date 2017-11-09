package com.hason.dtp.account.web.controller;

import com.hason.dtp.account.entity.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;

/**
 * 用户控制器
 *
 * @author Huanghs
 * @since 2.0
 * @date 2017/11/9
 */
@Controller
public class UserController {

    /**
     * 注册页面
     *
     * @return
     */
    @GetMapping(value = "/register")
    public String registerPage() {
        return "register";
    }

    /**
     * 提交注册
     *
     * @param user 用户
     * @return
     */
    @PostMapping(value = "/register")
    @ResponseBody
    public Object register(User user) {
        user.setBalance(BigDecimal.ZERO);
        return user;
    }

}
