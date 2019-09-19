# ORM

- DB 연동을 할 때 JDBC API를 직접 사용하는 경우보다는 MyBatis와 같은 SQL 매퍼나 하이버네이트,JPA와 같은 ORM 프레임워크를 사용.
- 스프링은 이러한 프레임워크들을 사용하면서 동시에 스프링이 제공하는 DB지원 기능(트랜잭션, DataSource 설정)을 적용할 수 있도록 하고 있다.



# @Repository 이용한 익셉션 변환 처리

- JdbcTempalte이 JDBC API를 이용하면서 SQLException이 발생하면 스프링의 DataAccessException으로 변환해서 발생
- 이와 비슷하게 @Repository 어노테이션을 이용하면 하이버네이트나 JPA API가 발생시키는 익셉션을 DataAccessException으로 변환할 수 있다.
- @Repository 어노테이션이 적용된 클래스의 메서드가 발생시킨 익셉션을 스프링의 DataAccessException으로 변환하려면 `PersistenceExceptionTranslationPostProcessor` 빈을 등록해주어야 한다.



## PersistenceExceptionTranslationPostProcessor의 동작

- `PersistenceExceptionTranslationPostProcessor`는 @Repository 적용된 빈 객체의 프록시를 생성한다.

- 예시

  ```xml
  <bean class="org.springframework.dao.annotaion.PersistenceExceptionTranslationPostProcessor" />
  
  <bean id="itemRepository" class="com.spring.orm.repo.HibernateItemRepository">
  	<property name="sessionFactory" ref="sessionFactory"/>
  </bean>
  ```

  - `PersistenceExceptionTranslationPostProcessor`에 의해 `HibernateItemRepository`에 대한 프록시 객체가 생성
  - 이 프록시 객체는 원래 객체에서 익셉션이 발생하게 되면 이것을 다시 스프링 익셉션으로 변환하도록 `PersistenceExceptionTranslator`에  요청 하고 결과를 받아 리턴한다.

- `PersistenceExceptionTranslation`도 그렇다면 빈을 등록해야 하는가?

  - 스프링이 제공하는 연동 모듈은 자체적으로 이러한 부분을 구현하고 있기 때문에 따로 설정하지 않는다.

  - 예를 들면 하이버네이트의 LocalSessionFactoryBean은 이미 PersistenceExceptionTranslator 인터페이스를 구현하고 있다.

    ```xml
    <!-- LocalSessionFactoryBean에는 이미 PersistenceExceptionTranslator를 구현 -->
    <bean id="sessionFactory" class="org.springframework.org.hibernate4.LocalsessionFactoryBean">
    	...
    </bean>
    ```



# 하이버네이트 연동 지원



## hibernate 4 연동 설정

- 의존에 하이버네이트4 추가

  ```xml
  <dependency>
      <groupId>org.springframework</groupId>
      <artifactId>spring-orm</artifactId>
      <version>4.3.25.RELEASE</version>
  </dependency>
  <dependency>
      <groupId>org.hibernate</groupId>
      <artifactId>hibernate-core</artifactId>
      <version>4.3.11.Final</version>
  </dependency>
  ```

- 스프링 설정

  - LocalSessionFactoryBean으로 SessionFactory 설정
  - HibernateTransactionManager로 트랜잭션 관리자 설정
  - DAO에서 SessionFactory 사용





### LocalSessionFactoryBean과 트랜잭션 관리자 설정

LocalSessionFactoryBean을 이용해서 DB 연결 정보, 트랜잭션 관련 정보, 매핑 목록 등 이들 정보를 설정

- LocalSessionFactoryBean 
  - 하이버네이트의 SessionFactory를 생성하기 위한 팩토리 빈
- HibernateTransactionManager
  - 스프링의 트랜잭션 관리 기능과 하이버네이트의 트랜잭션을 연동해주는 트랜잭션 관리자.

```xml
<bean class="org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor"/>

<bean id="sessionFactory" class="org.springframework.orm.hibernate4.LocalSessionFactoryBean">
    <property name="dataSource" ref="dataSource"/>
    <property name="mappingResources">
        <list>
            <value>hibernate/*.hbm.xml</value>
        </list>
    </property>
    <property name="hibernateProperties">
        <value>
            hibernate.dialect = org.hibernate.dialect.MySQL57InnoDBDialect
        </value>
    </property>
</bean>

<bean id="transactionManager" class="org.springframework.orm.hibernate4.HibernateTransactionManager">
    <property name="sessionFactory" ref="sessionFactory"/>
</bean>
```

```java
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

}
```



LocalSessionFactoryBean은 다음의 세 프로퍼티를 이용해서 하이버네이트의 SessionFactory를 생성하는데 필요한 값을 받는다.

- dataSource 
  - 하이버네이트가 DB에 연결할 때 사용할 DataSource를 전달
- mappingResources
  - 하이버네이트 XML 매핑 설정 파일 목록
- annotatedClasses
  - 하이버네이트 XML 매핑 설정 클래스 목록
- hibernateProperties
  - SessionFactory의 설정 프로퍼티 목록(dialect, show_sql, format_sql 등)



### DAO에서 SessionFactory 사용하기

- 스프링으로 SessionFactory를 주입받는다.

- sessionFactory의 session을 가져와 쿼리 호출

  ```java
  (Item) sessionFactory.getCurrentSession().get(Item.class, itemId);
  ```

- sessionFactory는 스프링이 제공하는 트랜잭션 관리 기능과 연동되어, 스프링이 제공하는 트랜잭션 관리 기능을 그대로 사용하면서 하이버네이트 코드를 스프링이 관리하는 트랜잭션 범위 내에서 실행할 수 있다.



## LocalSessionFactoryBean의 주요 프로퍼티

























