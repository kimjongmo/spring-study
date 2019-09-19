package com.spring.orm.config;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.spring.orm.store.repo.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
@PropertySource("classpath:db.properties")
public class JavaConfigXmlMapping {

    @Value("${driver}")
    private String driver;
    @Value("${url}")
    private String url;
    @Value("${password}")
    private String password;
    @Value("${user}")
    private String user;


    @Bean
    public DataSource dataSource() {
        ComboPooledDataSource ds = new ComboPooledDataSource();
        ds.setJdbcUrl(url);
        ds.setUser(user);
        ds.setPassword(password);
        try {
            ds.setDriverClass(driver);
        } catch (PropertyVetoException e) {
            e.printStackTrace();
        }
        return ds;
    }

    @Bean
    public PersistenceExceptionTranslationPostProcessor persistenceExceptionTranslationPostProcessor() {
        return new PersistenceExceptionTranslationPostProcessor();
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        HibernateTransactionManager transactionManager = new HibernateTransactionManager();

        return transactionManager;
    }

    @Bean(name = "sessionFactory")
    public LocalSessionFactoryBean sessionFactoryBean() {
        LocalSessionFactoryBean sessionFactoryBean = new LocalSessionFactoryBean();

        sessionFactoryBean.setDataSource(dataSource());
        sessionFactoryBean.setAnnotatedPackages("com.spring.org.store.domain");
        Properties properties = new Properties();
        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL57InnoDBDialect");
        sessionFactoryBean.setHibernateProperties(properties);

        return sessionFactoryBean;
    }


    @Bean
    public ItemRepository itemRepository() {
        return new HibernateItemRepository(sessionFactoryBean().getObject());
    }

    @Bean
    public PaymentInfoRepository paymentInfoRepository() {
        return new HibernatePaymentInfoRepository(sessionFactoryBean().getObject());
    }

    @Bean
    public PurchaseOrderRepository purchaseOrderRepository() {
        return new HibernatePurchaseOrderRepository(sessionFactoryBean().getObject());
    }
}
