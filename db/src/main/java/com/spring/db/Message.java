package com.spring.db;

import java.util.Date;

public class Message {
	private int id;
	private String name;
	private String message;
	private Date creationTime;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Date getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	@Override
	public String toString() {
		return "Message{" +
				"id=" + id +
				", name='" + name + '\'' +
				", message='" + message + '\'' +
				", creationTime=" + creationTime +
				'}';
	}
}
