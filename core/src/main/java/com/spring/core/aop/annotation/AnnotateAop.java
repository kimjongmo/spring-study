package com.spring.core.aop.annotation;

import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Pointcut;

public class AnnotateAop {

    @AfterThrowing(pointcut = "execution(public * get*() && execution(public void set*(..))")
    public void throwingLogging() {
        System.out.println("exception 발생 !!");
    }

    @Pointcut("execution(public * *(..))")
    private void publicMethod() {
    }

    @Pointcut("within(com.spring.core.aop)")
    private void inAop() {
    }

    @Pointcut("publicMethod() && inAop()")
    private void inAopPublicMethod() {
    }
}
