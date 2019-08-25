package com.spring.core.di.factoryMethod;

import com.spring.core.di.factoryMethod.conf.Config;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

public class Main {
    public static void main(String[] args) {
//        GenericXmlApplicationContext ctx =
//                new GenericXmlApplicationContext("classpath:factory-method.xml");
        AnnotationConfigApplicationContext ctx =
                new AnnotationConfigApplicationContext(Config.class);

        SearchClientFactory searchClientFactory = ctx.getBean("orderSearchClientFactory",SearchClientFactory.class);

    }
}
