package com.spring.core.env.conf;

import com.spring.core.env.ConnectionProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

@Configuration
@PropertySources(@PropertySource("classpath:/db.properties"))
public class ConfigByPropSource {
    @Value("${db.driver}")
    private String driver;
    @Value("${db.user}")
    private String user;
    @Value("${db.password}")
    private String password;
    @Value("${db.url}")
    private String url;

    @Bean
    public static PropertySourcesPlaceholderConfigurer properties() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean(initMethod = "init")
    public ConnectionProvider connProvider() {
        ConnectionProvider connProvider =
                new ConnectionProvider();
        connProvider.setUser(user);
        connProvider.setUrl(url);
        connProvider.setPassword(password);
        connProvider.setDriver(driver);
        return connProvider;
    }
}
