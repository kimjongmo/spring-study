package com.spring.core.aop.domain;

public class UpdateInfo {
    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "UpdateInfo{" +
                "password='" + password + '\'' +
                '}';
    }
}
