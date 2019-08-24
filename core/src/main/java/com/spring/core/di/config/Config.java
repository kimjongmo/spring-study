package com.spring.core.di.config;

import com.spring.core.di.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
public class Config {
    @Bean
    public User user1() {
        return new User("jongmo", "1234");
    }

    @Bean
    public User user2() {
        return new User("kim", "1234");
    }

    @Bean
    public AuthFailLogger authFailLogger() {
        AuthFailLogger authFailLogger = new AuthFailLogger();
        authFailLogger.setThreshold(3);
        return authFailLogger;
    }

    @Bean
    public UserRepository userRepository() {
        UserRepository userRepository = new UserRepository();
        userRepository.setUsers(Arrays.asList(user1(),user2()));
        return userRepository;
    }

    @Bean
    public AuthenticationService authenticationService() {
        AuthenticationService authenticationService = new AuthenticationService();
        authenticationService.setFailLogger(authFailLogger());
        authenticationService.setUserRepository(userRepository());
        return authenticationService;
    }

    @Bean
    public PasswordChangeService passwordChangeService(){
        return new PasswordChangeService(userRepository());
    }

}
