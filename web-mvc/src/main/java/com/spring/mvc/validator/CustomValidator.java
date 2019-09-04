package com.spring.mvc.validator;

import com.spring.mvc.dto.Address;
import com.spring.mvc.dto.MemberRegistRequest;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class CustomValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return MemberRegistRequest.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        MemberRegistRequest regReq = (MemberRegistRequest)target;

        if(regReq.getEmail() ==null || regReq.getEmail().trim().isEmpty()){
            errors.rejectValue("email","required");
        }

        ValidationUtils.rejectIfEmptyOrWhitespace(errors,"name","required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors,"password","required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors,"confirmPassword","required");

        if(!StringUtils.isEmpty(regReq.getPassword())){
            if(regReq.getPassword().length() < 5){
                errors.rejectValue("password","shortPassword");
            }else if(!regReq.getPassword().equals(regReq.getConfirmPassword()))
                errors.rejectValue("confirmPassword","not Same");
        }

        Address address = regReq.getAddress();
        if(address == null)
            errors.rejectValue("address","required");
        else {
            errors.pushNestedPath("address");
            try{
                ValidationUtils.rejectIfEmptyOrWhitespace(errors,"address1","required");
                ValidationUtils.rejectIfEmptyOrWhitespace(errors,"address2","required");
            }finally {
                errors.popNestedPath();
            }
        }

        // 아이디 , 패스워드 일치 여부
        // 도메인적인 문제라면 reject(메시지)
    }
}
