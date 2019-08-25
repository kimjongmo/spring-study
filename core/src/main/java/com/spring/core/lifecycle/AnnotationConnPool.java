package com.spring.core.lifecycle;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

public class AnnotationConnPool {

    @PostConstruct
    public void initPool(){
        System.out.println("[@PostConstruct]빈을 초기화시킨다.");
    }

    @PreDestroy
    public void destroyPool(){
        System.out.println("[@PreDestroy]빈을 소멸시킵니다.");
    }
}
