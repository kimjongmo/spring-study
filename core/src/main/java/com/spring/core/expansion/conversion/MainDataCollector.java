package com.spring.core.expansion.conversion;

import com.spring.core.expansion.DataCollector;
import com.spring.core.expansion.InvestmentSimulator;
import org.springframework.context.support.GenericXmlApplicationContext;

public class MainDataCollector {
    public static void main(String[] args) {
        GenericXmlApplicationContext ctx =
                new GenericXmlApplicationContext("classpath:/conversion-service.xml");
        InvestmentSimulator simulator = ctx.getBean(InvestmentSimulator.class);
        System.out.println(simulator.getMoney());
        ctx.close();
    }
}
