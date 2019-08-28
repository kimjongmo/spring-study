package com.spring.core.aop.annotation;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Aspect
@Component
public class ProfilingAspect {
//
//    @Pointcut("execution(public * com.spring.core.aop..*(..))")
//    private void profileTarget() {
//    }
    public  ProfilingAspect(){
        System.out.println("create constructor");
    }

    @Around("execution(public * com.spring.core.aop..*(..))")
    public void trace(ProceedingJoinPoint joinPoint) throws Throwable {
        String signatureString = joinPoint.getSignature().toString();
        System.out.println(signatureString + " 시작");

        long start = System.currentTimeMillis();
        try {
            joinPoint.proceed();
        } finally {
            long end = System.currentTimeMillis();
            System.out.println(signatureString + " 종료");
            System.out.println(signatureString + " 실행시간 :" + (end - start) + "ms");
        }
    }
//
//    @Before("profileTarget()")
//    public void before(JoinPoint joinPoint) {
//
//    }
//
//    @AfterReturning(pointcut = "profileTarget()", returning = "ret")
//    public void afterReturning(Object ret) {
//
//    }
//
//    @AfterThrowing(pointcut = "profileTarget()", throwing = "throwable")
//    public void afterThrowing(Throwable throwable) {
//
//    }
//
//    @After("profileTarget()")
//    public void after() {
//
//    }
}
