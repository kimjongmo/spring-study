package com.spring.jpa;

import com.spring.jpa.application.EmployeeListService;
import com.spring.jpa.application.SpecEmployeeListService;
import com.spring.jpa.domain.Employee;
import org.springframework.context.support.GenericXmlApplicationContext;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        GenericXmlApplicationContext ctx = new GenericXmlApplicationContext("springconf.xml");

        EmployeeListService service =
                ctx.getBean("specEmployeeListService",EmployeeListService.class);

        List<Employee> list = service.getEmployee("kim",null);

        list.stream().forEach(System.out::println);

        ctx.close();
    }
}
