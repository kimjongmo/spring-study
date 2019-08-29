package com.spring.core.aop.conf;

import com.spring.core.aop.Checker;
import com.spring.core.aop.annotation.ProfilingAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@EnableAspectJAutoProxy
public class Config {


    @Bean
    public ProfilingAspect aspect() {
        return new ProfilingAspect();
    }

    @Bean
    public Checker checker() {
        return new Checker();
    }

}
