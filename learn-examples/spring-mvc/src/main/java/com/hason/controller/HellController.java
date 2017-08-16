package com.hason.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Hason on 2017/8/16.
 */
@Controller
public class HellController {

    @Autowired
    private HttpServletRequest request;

    @RequestMapping("/index")
    public String index() {
        return "index";
    }
}
