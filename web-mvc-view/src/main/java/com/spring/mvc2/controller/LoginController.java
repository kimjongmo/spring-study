package com.spring.mvc2.controller;

import com.spring.mvc2.model.MemberRegistRequest;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import javax.validation.Valid;


@Controller
@SessionAttributes("registCommand")
public class LoginController {

    public static final String LOGIN_FORM = "/login/registForm";


    @ModelAttribute("registCommand")
    public MemberRegistRequest regist() {
        return new MemberRegistRequest();
    }

    @RequestMapping(value = "/login/registForm", method = RequestMethod.GET)
    public String getLoginForm() {
        return LOGIN_FORM;
    }


    @RequestMapping(value = "/login/registMember", method = RequestMethod.POST)
    public String registMember(@Valid @ModelAttribute("registCommand") MemberRegistRequest registCommand,
                               BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            System.out.println("Errors");
            return LOGIN_FORM;
        }
        return "redirect:/login/registForm";
    }
}
