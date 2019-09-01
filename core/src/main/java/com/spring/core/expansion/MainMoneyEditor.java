package com.spring.core.expansion;

import org.springframework.context.support.GenericXmlApplicationContext;

public class MainMoneyEditor {

    public static void main(String[] args) {
        GenericXmlApplicationContext ctx =
                new GenericXmlApplicationContext("classpath:/stockreader.xml");

        CustomDate customDate = ctx.getBean("customDate", CustomDate.class);
        System.out.println(customDate.getDate());

        ctx.close();
    }
}
