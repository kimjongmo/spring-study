package com.spring.core.di.hierarchyContainer;

import org.springframework.context.support.GenericXmlApplicationContext;

public class Main {
    public static void main(String[] args) {
        GenericXmlApplicationContext parent = new GenericXmlApplicationContext("classpath:conf-parent.xml");
        GenericXmlApplicationContext child = new GenericXmlApplicationContext();
        child.setParent(parent);
        child.load("classpath:conf-child.xml");
        child.refresh();
        Child instance = child.getBean("child",Child.class);
        
    }
}
