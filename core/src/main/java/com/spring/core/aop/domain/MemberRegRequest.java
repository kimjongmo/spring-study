package com.spring.core.aop.domain;

public class MemberRegRequest {
    private String id;
    private String password;

    public MemberRegRequest(String id, String password) {
        this.id = id;
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Member toMember() {
        return new Member(this.id, this.password);
    }
}
