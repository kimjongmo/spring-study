package com.spring.mvc.controller;

import com.spring.mvc.dto.LoginCommand;
import com.spring.mvc.validator.AuthenticationException;
import com.spring.mvc.validator.LoginCommandValidator;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

@Controller
@RequestMapping("/auth/login")
public class LoginController {

    private static final String LOGIN_FORM = "auth/loginForm";


    @RequestMapping(method = RequestMethod.GET)
    public String loginForm(LoginCommand loginCommand) {
        return LOGIN_FORM;
    }

    @RequestMapping(method = RequestMethod.POST)
    public String login(@Valid LoginCommand loginCommand, Errors errors) {
        if (errors.hasErrors()) {
            return LOGIN_FORM;
        }

        try {
            return "redirect:/hello.jsp";
        } catch (AuthenticationException ex) {
            errors.reject("invalidIdOrPassword");
            return LOGIN_FORM;
        }
    }

    @InitBinder
    protected void initBinder(WebDataBinder webDataBinder) {
        webDataBinder.setValidator(new LoginCommandValidator());
    }


}
