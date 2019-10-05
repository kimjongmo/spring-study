package com.spring.hrdb.app.web;

import com.spring.hrdb.app.model.Employee;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class NewEmployeeValidator implements Validator {

	@Override
	public boolean supports(Class<?> klass) {
		return klass == Employee.class;
	}

	@Override
	public void validate(Object object, Errors errors) {
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "number", "required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "required");
		ValidationUtils.rejectIfEmpty(errors, "joinedDate", "required");
	}

}
