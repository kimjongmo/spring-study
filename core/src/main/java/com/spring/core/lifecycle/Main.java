package com.spring.core.lifecycle;

import org.springframework.context.support.GenericXmlApplicationContext;

public class Main {
    public static void main(String[] args) {
        GenericXmlApplicationContext ctx =
                new GenericXmlApplicationContext("classpath:lifecycle/spring-bean.xml");

        ConnPool connPool1 = ctx.getBean("connPool",ConnPool.class);
        ConnPool connPool2 = ctx.getBean("connPool",ConnPool.class);

        System.out.println(connPool1==connPool2);
        ctx.close();
    }
}
