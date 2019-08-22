package com.spring.core.di;

public class User {
    private String id;
    private String password;

    public User(String id, String password) {
        this.id = id;
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public boolean matchPassword(String inputPw) {
        return password.equals(inputPw);
    }

    public void changePassword(String oldPw, String newPw) {
        if (!matchPassword(oldPw))
            throw new IllegalArgumentException("illegal password");
        password = newPw;
    }
}
