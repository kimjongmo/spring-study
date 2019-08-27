package com.spring.core.aop;

import org.springframework.context.support.GenericXmlApplicationContext;

public class MainQuickStart {
    public static void main(String[] args) {
        GenericXmlApplicationContext ctx =
                new GenericXmlApplicationContext("classpath:aop/spring-aop.xml");

        Test test = ctx.getBean(Test.class);
        test.test();

        ctx.close();
    }
}
