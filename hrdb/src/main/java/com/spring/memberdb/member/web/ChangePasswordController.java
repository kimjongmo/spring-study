package com.spring.memberdb.member.web;


import com.spring.memberdb.member.application.ChangePasswordRequest;
import com.spring.memberdb.member.application.ChangePasswordService;
import com.spring.memberdb.member.domain.WrongPasswordException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class ChangePasswordController {

	private ChangePasswordService changePasswordService;

	@ModelAttribute("command")
	public ChangePasswordRequest formBacking() {
		return new ChangePasswordRequest();
	}

	@RequestMapping(value = "/member/changePassword", method = RequestMethod.GET)
	public String form() {
		return "member/changePasswordForm";
	}

	@RequestMapping(value = "/member/changePassword", method = RequestMethod.POST)
	public String changePassword(@ModelAttribute("command") ChangePasswordRequest req, BindingResult errors) {
		new ChangePasswordRequestValidator().validate(req, errors);
		if (errors.hasErrors())
			return "member/changePasswordForm";

		try {
			changePasswordService.changePassword(req);
			return "member/changePasswordDone";
		} catch (WrongPasswordException ex) {
			errors.rejectValue("currentPassword", "invalidPassword");
			return "member/changePasswordForm";
		}
	}

	public void setChangePasswordService(ChangePasswordService changePasswordService) {
		this.changePasswordService = changePasswordService;
	}

}
