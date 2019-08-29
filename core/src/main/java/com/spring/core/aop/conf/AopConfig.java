package com.spring.core.aop.conf;

import com.spring.core.aop.service.MemberServiceImpl;
import com.spring.core.aop.service.UpdateMemberInfoTraceAdvice;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@EnableAspectJAutoProxy
public class AopConfig {
    @Bean
    public UpdateMemberInfoTraceAdvice traceAspect(){
        return new UpdateMemberInfoTraceAdvice();
    }

    @Bean
    public MemberServiceImpl memberService(){
        return new MemberServiceImpl();
    }
}
