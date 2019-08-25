package com.spring.core.di.factoryMethod.conf;

import com.spring.core.di.factoryMethod.SearchClientFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {

    @Bean
    public SearchClientFactoryBean orderSearchClientFactory(){
        SearchClientFactoryBean searchClientFactoryBean = new SearchClientFactoryBean();
        searchClientFactoryBean.setServer("1.2.3.4");
        searchClientFactoryBean.setPort(8888);
        searchClientFactoryBean.setContentType("json");
        searchClientFactoryBean.setEncoding("utf-8");
        return searchClientFactoryBean;
    }
}
