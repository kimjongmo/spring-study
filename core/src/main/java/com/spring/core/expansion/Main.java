package com.spring.core.expansion;

import org.springframework.context.support.GenericXmlApplicationContext;

public class Main {

    public static void main(String[] args) {
        GenericXmlApplicationContext ctx =
                new GenericXmlApplicationContext("classpath:/applicationContext.xml");

        DataCollector collector1 = ctx.getBean("collector1", DataCollector.class);
        DataCollector collector2 = ctx.getBean("collector2", DataCollector.class);
        System.out.println(collector1.getThreshold());
        System.out.println(collector2.getThreshold());

        ctx.close();
    }
}
