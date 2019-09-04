package com.spring.mvc.chap07.quickstart;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.Cookie;

@Controller
public class HelloController {

    @RequestMapping("/welcome/hello")
    public String hello(Model model) {
        model.addAttribute("greeting", "안녕하세요");
        return "redirect:bye";
    }

    @RequestMapping("/welcome/bye")
    public String bye() {
        return "bye";
    }

}
