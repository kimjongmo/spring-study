package com.spring.core.aop;

import com.spring.core.aop.conf.AspectConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class MainQuickStart {
    public static void main(String[] args) {
//        GenericXmlApplicationContext ctx =
//                new GenericXmlApplicationContext("classpath:aop/spring-anno-aop.xml");

        AnnotationConfigApplicationContext ctx =
                new AnnotationConfigApplicationContext(AspectConfig.class);
        Test test = ctx.getBean(Test.class);
        test.method();
//
//        try {
////            test.divide(0);
////            test.getTitle(1);
//        } catch (ArithmeticException e) {
//
//        } finally {
//            ctx.close();
//        }
        ctx.close();
    }
}
