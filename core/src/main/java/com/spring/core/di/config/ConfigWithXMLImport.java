package com.spring.core.di.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@Configuration
@ImportResource("classpath:spring-di.xml")
public class ConfigWithXMLImport {
}
