package com.spring.core.di;

import com.spring.core.di.config.Config;
import com.spring.core.di.exception.UserNotFoundException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

public class Main {
    public static void main(String[] args) {

        GenericXmlApplicationContext ctx =
                new GenericXmlApplicationContext("classpath:spring-di.xml");
//        AnnotationConfigApplicationContext ctx =
//                new AnnotationConfigApplicationContext(Config.class);
//        AnnotationConfigApplicationContext ctx =
//                new AnnotationConfigApplicationContext("com.spring.core.di.config");


        AuthFailLogger logger = ctx.getBean("authFailLogger",AuthFailLogger.class);
        AuthenticationService authenticationService = ctx.getBean("authenticationService",AuthenticationService.class);

        logger.insertPw("kim","12345");
        logger.insertPw("kim","12345");
        logger.insertPw("kim","12345");
        logger.insertPw("kim","12345");

        try{
            authenticationService.authenticate("kimm","1234");
        }catch(UserNotFoundException e){

        }
    }
}
