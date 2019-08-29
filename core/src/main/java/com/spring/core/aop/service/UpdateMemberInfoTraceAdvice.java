package com.spring.core.aop.service;

import com.spring.core.aop.domain.UpdateInfo;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;

@Aspect
public class UpdateMemberInfoTraceAdvice {

    @AfterReturning(pointcut = "args(memberId,info)")
    public void traceReturn(String memberId, UpdateInfo info) {
        System.out.printf("[TA] 정보 수정: 대상회원=%s, 수정 정보=%s\n", memberId, info);
    }
}
