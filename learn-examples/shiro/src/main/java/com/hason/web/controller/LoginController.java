package com.hason.web.controller;

import com.hason.config.CasConfig;
import com.hason.entity.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.servlet.ModelAndView;

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

//    @RequestMapping("/login")
//    public String login(@RequestParam String username,
//                        @RequestParam String password,
//                        @RequestParam(defaultValue = "false") Boolean rememberMe,
//                        Model model) {
//        Subject subject = SecurityUtils.getSubject();
//        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
//        token.setRememberMe(rememberMe);    // 需要设置 RememberMeManager
//        try {
//            subject.login(token);
//        } catch (UnknownAccountException e) {
//            return "账号不存在";
//        } catch (IncorrectCredentialsException e) {
//            return "密码错误";
//        } catch (AuthenticationException e) {
//            return "其他错误";
//        }
//
//        model.addAttribute("username", username);
//        return "loginSuccess";
//    }

    @RequestMapping(value="/login",method= RequestMethod.GET)
    public String loginForm(Model model){
        model.addAttribute("user", new User());
//      return "login";
        return "redirect:" + CasConfig.loginUrl;
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

    // 使用注解标记需要认证，如果不通过时抛出异常
    @RequestMapping("/authAnno")
    @ResponseBody
    @RequiresRoles("admin")
    public String authAnno() {
        return "auth";
    }

    // 处理异常
    @ExceptionHandler(Exception.class)
    public ModelAndView processException(NativeWebRequest request, Exception ex) {
        System.err.println("进入异常处理:" + ex);
        ModelAndView mv = new ModelAndView();
        mv.setViewName("errorPage");
        mv.addObject("error", ex.getMessage());
        return mv;
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

    // 测试 SSL
    @RequestMapping("/ssl")
    public String ssl() {
        return "ssl";
    }
}
