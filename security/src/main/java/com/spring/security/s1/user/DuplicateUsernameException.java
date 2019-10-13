package com.spring.security.s1.user;

@SuppressWarnings("serial")
public class DuplicateUsernameException extends RuntimeException {

	public DuplicateUsernameException(String msg, Exception ex) {
		super(msg, ex);
	}

}
