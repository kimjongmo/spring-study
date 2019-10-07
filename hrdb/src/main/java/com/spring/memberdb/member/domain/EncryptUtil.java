package com.spring.memberdb.member.domain;

public abstract class EncryptUtil {

	public static String encrypt(String plain) {
		byte[] bytes = plain.getBytes();
		StringBuilder builder = new StringBuilder();
		for (byte b : bytes) {
			builder.append(Integer.toHexString(b));
		}
		return builder.toString();
	}
}
