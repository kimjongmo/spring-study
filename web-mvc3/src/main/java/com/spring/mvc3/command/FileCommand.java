package com.spring.mvc3.command;

import org.springframework.web.multipart.MultipartFile;

public class FileCommand {

	private String title;
	private MultipartFile f;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public MultipartFile getF() {
		return f;
	}

	public void setF(MultipartFile f) {
		this.f = f;
	}

}
