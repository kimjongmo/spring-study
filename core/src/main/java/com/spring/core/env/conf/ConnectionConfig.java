package com.spring.core.env.conf;

import com.spring.core.env.ConnectionProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

@Configuration
@PropertySource("classpath:/db.properties")
public class ConnectionConfig {

    @Autowired
    private Environment env;

    @Bean
    public ConnectionProvider connectionProvider() {
        ConnectionProvider connectionProvider = new ConnectionProvider();
        connectionProvider.setDriver(env.getProperty("db.driver"));
        connectionProvider.setPassword(env.getProperty("db.password"));
        connectionProvider.setUrl(env.getProperty("db.url"));
        connectionProvider.setUser(env.getProperty("db.user"));
        return connectionProvider;
    }
}
