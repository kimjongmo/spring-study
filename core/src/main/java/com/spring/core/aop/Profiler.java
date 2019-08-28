package com.spring.core.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;

public class Profiler {

    public Object trace(ProceedingJoinPoint joinPoint) throws Throwable {
        String signatureString = joinPoint.getSignature().toShortString();
        System.out.println(signatureString + " 시작");
        long start = System.currentTimeMillis();
        try {
            Object result = joinPoint.proceed();
            return result;
        } finally {
            long finish = System.currentTimeMillis();
            System.out.println(signatureString + " 종료");
            System.out.println(signatureString + " 실행 시간 : " + (finish - start) + "ms");
        }
    }

    // 대상 객체 및 호출되는 메서드에 대한 정보 또는 전달되는 파라미터에 대한 정보를 JoinPoint로 만들어 전달
    public void before(JoinPoint joinPoint) {
        String signatureString = joinPoint.getSignature().toShortString();
        System.out.println(signatureString + " 시작");
    }

    public void afterReturning(Integer ret) {
        System.out.println("리턴 값 :" + ret);
    }

    public void afterThrowing(Throwable throwable) {
        System.out.println("익셉션 발생!! ");
    }

    public void afterThrowingArith(ArithmeticException ex) {
        System.out.println("ArithmeticException이 발생!!!");
    }

    public void afterThrowingNull(NullPointerException ex) {
        System.out.println("NullPointerException이 발생!!!");
    }
}
