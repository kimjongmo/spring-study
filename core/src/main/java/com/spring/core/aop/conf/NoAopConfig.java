package com.spring.core.aop.conf;

import com.spring.core.aop.service.MemberServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NoAopConfig {
    @Bean
    public MemberServiceImpl memberService() {
        return new MemberServiceImpl();
    }
}
