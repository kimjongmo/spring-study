package com.spring.mvc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.Cookie;

@Controller
@RequestMapping("/example")
public class ExampleController {

    @RequestMapping("/welcome")
    public String welcome() {
        return "hello";
    }


}
