package com.spring.core.aop;

import javax.annotation.PostConstruct;

public class Checker {

    @PostConstruct
    public void set(){
        System.out.println("Checker 생성");
    }
    public void check(){
        System.out.println("check 합니다.");
    }
}
