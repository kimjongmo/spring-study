package com.spring.core.lifecycle;

public class CustomConnPool {
    public void initPool(){
        System.out.println("[커스텀]빈을 초기화시킨다.");
    }

    public void destroyPool(){
        System.out.println("[커스텀]빈을 소멸시킵니다.");
    }
}
