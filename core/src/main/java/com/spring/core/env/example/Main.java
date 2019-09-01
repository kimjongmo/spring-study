package com.spring.core.env.example;

import com.spring.core.env.ConnectionProvider;
import com.spring.core.env.conf.ConfigByPropSource;
import com.spring.core.env.conf.ConnectionConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
//        ConfigurableApplicationContext ctx = new GenericXmlApplicationContext();
//        ConfigurableEnvironment env = ctx.getEnvironment();
//        MutablePropertySources propertySources = env.getPropertySources();
//        propertySources.addLast(new ResourcePropertySource("classpath:/db.properties"));
//        String dbUser = env.getProperty("db.user");
//        System.out.println(dbUser);
//        GenericXmlApplicationContext ctx =
//                new GenericXmlApplicationContext("classpath:/env/spring-properties.xml");
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(ConnectionConfig.class);
        ConnectionProvider connectionProvider = ctx.getBean("connectionProvider", ConnectionProvider.class);
        System.out.println(connectionProvider);

        ctx.close();
//        String javaVersion = env.getProperty("java.version");
//        System.out.println("Java version is "+javaVersion);
    }
}
