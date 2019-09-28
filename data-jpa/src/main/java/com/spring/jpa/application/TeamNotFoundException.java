package com.spring.jpa.application;

public class TeamNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public TeamNotFoundException(String msg) {
		super(msg);
	}

}
