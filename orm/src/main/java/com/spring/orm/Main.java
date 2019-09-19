package com.spring.orm;

import com.spring.orm.config.JavaConfigXmlMapping;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(JavaConfigXmlMapping.class);



        ctx.close();
    }
}
