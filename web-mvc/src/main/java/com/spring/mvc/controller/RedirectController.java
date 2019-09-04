package com.spring.mvc.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/red")
public class RedirectController {

    @RequestMapping("/hello")
    public String hello() {
        return "redirect:bye";
    }

    @RequestMapping("/bye")
    public String bye() {
        return "bye";
    }


}
