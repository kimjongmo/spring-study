package com.spring.mvc.controller;

import com.spring.mvc.dto.MemberRegistRequest;
import com.spring.mvc.validator.CustomValidator;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/validation")
public class ValidationController {

    public static final String MEMBER_REGISTRATION_FORM = "registForm";

    @RequestMapping("/regist")
    public String reigst(@ModelAttribute("memberInfo") MemberRegistRequest memberRegReq
            , BindingResult bindingResult) {
        new CustomValidator().validate(memberRegReq, bindingResult);

        if(bindingResult.hasErrors()){
            return MEMBER_REGISTRATION_FORM;
        }


        return "member/registerd";
    }
}
