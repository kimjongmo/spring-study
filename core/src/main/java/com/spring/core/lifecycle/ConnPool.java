package com.spring.core.lifecycle;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

public class ConnPool implements InitializingBean, DisposableBean {
    @Override
    public void destroy() throws Exception {
        System.out.println("빈을 소멸시킨다.");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("빈을 초기화시킨다.");
    }
}
