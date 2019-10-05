package com.spring.hrdb.config;

import java.beans.PropertyVetoException;

import javax.sql.DataSource;


import com.spring.hrdb.app.dao.EmployeeDao;
import com.spring.hrdb.app.dao.JdbcEmployeeDao;
import com.spring.hrdb.app.service.EmployeeRegistryService;
import com.spring.hrdb.app.service.EmployeeRegistryServiceImpl;
import com.spring.hrdb.app.web.EmployeeRegController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.mchange.v2.c3p0.ComboPooledDataSource;

@Configuration
@EnableTransactionManagement
public class SpringAppConfig {

    @Bean
    public PersistenceExceptionTranslationPostProcessor postProcessor() {
        return new PersistenceExceptionTranslationPostProcessor();
    }

    @Bean
    public DataSource dataSource() {
        ComboPooledDataSource ds = new ComboPooledDataSource();
        try {
            ds.setDriverClass("com.mysql.cj.jdbc.Driver");
        } catch (PropertyVetoException e) {
            throw new RuntimeException(e);
        }
        ds.setJdbcUrl("jdbc:mysql://localhost/hrdb?characterEncoding=utf8");
        ds.setUser("kim");
        ds.setPassword("1234");
        return ds;
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        DataSourceTransactionManager tm = new DataSourceTransactionManager();
        tm.setDataSource(dataSource());
        return tm;
    }

    @Bean
    public EmployeeDao employeeDao() {
        return new JdbcEmployeeDao(dataSource());
    }

    @Bean
    public EmployeeRegistryService employeeRegistryService() {
        EmployeeRegistryServiceImpl regService = new EmployeeRegistryServiceImpl();
        regService.setEmpDao(employeeDao());
        return regService;
    }
	@Bean
	public EmployeeRegController employeeRegController() {
		EmployeeRegController controller = new EmployeeRegController();
		controller.setEmployeeRegistryService(employeeRegistryService());
		return controller;
	}
}
