package com.spring.core.env.conf;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.core.env.Environment;

@Configuration
@PropertySources({
        @PropertySource("classpath:/db.properties"),
        @PropertySource(value = "classpath:/app.properties", ignoreResourceNotFound = true)
})

public class ConfigByEnv {

    @Autowired
    private Environment env;
}
