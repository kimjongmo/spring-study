package com.spring.mvc3.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {"com.spring.mvc3.controller"})
public class ControllerConfig {
}
