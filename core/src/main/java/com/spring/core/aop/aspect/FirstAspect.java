package com.spring.core.aop.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;

@Aspect
@Order(1)
public class FirstAspect {

    @Pointcut("execution(public * com.spring.core.aop.Checker.check())")
    public void checkerAspect(){

    }

    @Around("checkerAspect()")
    public void first(ProceedingJoinPoint joinPoint) throws Throwable{
        System.out.println("[FirstAspect]");
        joinPoint.proceed();
        System.out.println("[FirstAspect]");
    }

}
