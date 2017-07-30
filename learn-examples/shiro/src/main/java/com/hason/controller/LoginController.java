package com.hason.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 登录
 *
 * @author Huanghs
 * @since 2.0
 * @date 2017/7/30
 */
@Controller
public class LoginController {

    @RequestMapping("/loginPage")
    public String loginPage() {
        return "login";
    }

    @RequestMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        Model model) {
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        try {
            subject.login(token);
        } catch (UnknownAccountException e) {
            return "账号不存在";
        } catch (IncorrectCredentialsException e) {
            return "密码错误";
        } catch (AuthenticationException e) {
            return "其他错误";
        }

        model.addAttribute("username", username);
        return "loginSuccess";
    }

    @RequestMapping("/logout")
    public String logout() {
        SecurityUtils.getSubject().logout();
        return "logout";
    }

    @RequestMapping("/auth")
    @ResponseBody
    public String auth() {
        return "auth";
    }

    @RequestMapping("/authcBasic")
    @ResponseBody
    public String authcBasic() {
        return "auth";
    }

    @RequestMapping("/role")
    @ResponseBody
    public String role() {
        return "role";
    }

    @RequestMapping("/permission")
    @ResponseBody
    public String permission() {
        return "permission";
    }

}
