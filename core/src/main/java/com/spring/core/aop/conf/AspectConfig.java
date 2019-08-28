package com.spring.core.aop.conf;


import com.spring.core.aop.Test;
import com.spring.core.aop.annotation.ProfilingAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

@Configuration
@EnableAspectJAutoProxy
@ComponentScan(basePackages = "com.spring.core.aop.annotation")
public class AspectConfig {
//
//    @Bean
//    public Test test1() {
//        return new Test();
//    }
//
//    @Bean
//    public ProfilingAspect profilingAspect() {
//        return new ProfilingAspect();
//    }
}
