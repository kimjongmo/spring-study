package com.spring.core.env.conf;


import com.spring.core.env.ConnectionProvider;
import org.springframework.context.annotation.*;

@Configuration
@ImportResource(value = "classpath:/confprofile/datasource-dev.xml")
@Profile("prod")
public class DataSourceProdConfig {
}
