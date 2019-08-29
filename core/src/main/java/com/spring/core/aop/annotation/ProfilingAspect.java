package com.spring.core.aop.annotation;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

import javax.annotation.PostConstruct;

@Aspect
public class ProfilingAspect {

    @PostConstruct
    public void set(){
        System.out.println("Aspect 생성");
    }

    @Pointcut("execution(public * c*(..))")
    public void method() {

    }

    @Before("method()")
    public void logMethod(JoinPoint joinPoint){
        System.out.println(joinPoint.getSignature().toShortString()+"이 호출되었습니다.");
    }
//    @Around("execution(public * *(..))")
//    public void trace(ProceedingJoinPoint joinPoint) throws Throwable {
//        String signatureString = joinPoint.getSignature().toString();
//        System.out.println(signatureString + " 시작");
//
//        long start = System.currentTimeMillis();
//        try {
//            joinPoint.proceed();
//        } finally {
//            long end = System.currentTimeMillis();
//            System.out.println(signatureString + " 종료");
//            System.out.println(signatureString + " 실행시간 :" + (end - start) + "ms");
//        }
//    }
//
//    @Before("profileTarget()")
//    public void before(JoinPoint joinPoint) {
//
//    }
//
//    @AfterReturning(pointcut = "*(ret)")
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
