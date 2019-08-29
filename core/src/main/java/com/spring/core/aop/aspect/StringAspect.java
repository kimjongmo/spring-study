package com.spring.core.aop.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class StringAspect {

    @Pointcut("execution(public * *(..))")
    public void publicMethod() {
    }


    @Before(value = "publicMethod() && args(id)",argNames = "id")
    public void aspect(String id){
        System.out.println("param is "+id);
    }

}
