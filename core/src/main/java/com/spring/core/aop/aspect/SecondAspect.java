package com.spring.core.aop.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.annotation.Order;

@Aspect
@Order(2)
public class SecondAspect{

    @Around("FirstAspect.checkerAspect()")
    public void second(ProceedingJoinPoint joinPoint) throws Throwable{
        System.out.println("[SecondAspect]");
        joinPoint.proceed();
        System.out.println("[SecondAspect]");
    }
}
