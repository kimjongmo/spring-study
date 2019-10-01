package com.spring.jpa;

import com.spring.jpa.application.SpecEmployeeListService;
import com.spring.jpa.domain.Employee;
import org.springframework.context.support.GenericXmlApplicationContext;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        GenericXmlApplicationContext ctx = new GenericXmlApplicationContext("springconf.xml");

        SpecEmployeeListService service =
                ctx.getBean("specEmployeeListService",SpecEmployeeListService.class);

        List<Employee> list = service.getEmployee("kim",null);

        list.stream().forEach(System.out::println);

        ctx.close();
    }
}
