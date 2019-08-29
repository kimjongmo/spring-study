package com.spring.core.aop.conf;

import com.spring.core.aop.IntSample;
import com.spring.core.aop.StringSample;
import com.spring.core.aop.aspect.StringAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class SampleConfig {
    @Bean
    public StringSample stringSample() {
        return new StringSample();
    }

    @Bean
    public IntSample intSample() {
        return new IntSample();
    }

    @Bean
    public StringAspect stringAspect() {
        return new StringAspect();
    }

}
