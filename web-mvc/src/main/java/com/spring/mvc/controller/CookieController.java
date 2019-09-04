package com.spring.mvc.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.Cookie;

@Controller
@RequestMapping("/cookie")
public class CookieController {

    @RequestMapping("/welcome/cookie")
    public String cookie(@CookieValue(value = "auth",required = false, defaultValue = "") Cookie cookie){
        System.out.println(cookie.getValue());
        return "hello";
    }

}
