package com.hason.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.session.ExpiringSession;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.Session;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

@RestController
public class HelloController {

    private static final String id = "3";

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private FindByIndexNameSessionRepository<? extends ExpiringSession> sessionRepository;

    @RequestMapping("/test/cookie")
    public String cookie(@RequestParam("browser") String browser) {
        // 取出session
        HttpSession session = request.getSession();
        Object sessionBrowser = session.getAttribute("browser");

        if (sessionBrowser == null) {
            System.out.println("不存在session, 设置browser=" + browser);
            session.setAttribute("browser", browser);
        } else {
            System.out.println("已经存在session, browser=" + sessionBrowser.toString());
        }

        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                System.out.println(String.format("%s : %s", cookie.getName(), cookie.getValue()));
            }
        }

        return "index " + id + " : " + session.getId();
    }

    @RequestMapping("/test/findByUsername")
    @ResponseBody
    public Map findByUsername(@RequestParam String username) {
        Map<String, ? extends ExpiringSession> userSessions = sessionRepository
                .findByIndexNameAndIndexValue(FindByIndexNameSessionRepository.PRINCIPAL_NAME_INDEX_NAME, username);
        return userSessions;
    }

}
