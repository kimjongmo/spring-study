package com.spring.core.aop;

import javax.annotation.PostConstruct;

public class Test {

    @PostConstruct
    public void setUp(){
        System.out.println("Test 생성");
    }
    public void method() {
        System.out.println("테스트합니다.");
    }

    public Integer retInt() {
        return 3;
    }

    public void throwingNullPointerException() {
        throw new NullPointerException();
    }

    public void divide(int divide) {
        int a = 10;
        int div = a / divide;
    }

    public String getTitle(Integer id) {
        System.out.println("런닝맨 책 출간");
        return "런닝맨";
    }
}
