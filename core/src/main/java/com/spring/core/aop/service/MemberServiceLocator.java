package com.spring.core.aop.service;

public class MemberServiceLocator {

    private MemberServiceImpl memberService;

    public MemberServiceLocator() {

    }

    public void setMemberService(MemberServiceImpl memberService) {
        this.memberService = memberService;
    }
}
