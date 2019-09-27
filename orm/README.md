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



# JPA(Java Persistence API)

- JPA = 오라클에서 정의한 자바 ORM 표준
- JPA 표준을 지워하는 프로바이더로는 하이버네이트, EclipseLink, OpenJPA 등이 존재



## 설정

```xml
<dependency>
    <groupId>org.hibernate</groupId>
    <artifactId>hibernate-entitymanager</artifactId>
</dependency>
```

- 앞선 Hibernate와 달라진 점이라면 아티팩트id가 hibernate-core에서 hibernate-entitymanager로 바뀜.
- hibernate-entitymanager 모듈은 하이버네이트의 JPA 프로바이더 모듈이다.



## LocalContainerEntityManagerFactoryBean 설정

JPA의 설정 파일인 persistence.xml에는 매핑 관련 정보만 설정. 

> persistence.xml 파일을 사용하는 대신 클래스 스캔 기능을 이요해서 매핑 클래스 정보를 읽어올 수 있다.
>
> LocalContainerEntityManagerFactoryBean의 packagesToScan 속성을 사용

> 스프링은 기본적으로 classpath:/META-INF/persistence.xml 경로에서 이 파일을 찾는다. 이 경로가 아닌 다른 경로에 위치한 파일을 사용하고 싶다면 LocalCotainerEntityEntityManagerFactoryBean을 설정할 때 persistenceXmlLocation 프로퍼티를 이용한다.

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<persistence xmlns="http://xmlns.jcp.org/xml/ns/persistence"
             xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
             xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/persistence
        http://java.sun.com/xml/ns/persistence/persistence_2_1.xsd" version="2.1">
    <persistence-unit name="store">
        <class>com.spring.orm.store.domain.Item</class>
        <class>com.spring.orm.store.domain.PaymentInfo</class>
        <class>com.spring.orm.store.domain.PurchaseOrder</class>
    </persistence-unit>
</persistence>
```



EntityManager, TransactionManager, JpaVenderAdapter 설정

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">
    <import resource="classpath:dataSource.xml"/>

    <bean id="jpaVenderAdapter" class="org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter">
        <property name="database" value="MYSQL"/>
        <property name="showSql" value="true"/>
        <property name="generateDdl" value="true"/>
    </bean>

    <bean id="entityManagerFactory" class="org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean">
        <property name="persistenceUnitName" value="store"/>
        <property name="dataSource" ref="dataSource"/>
        <property name="jpaVendorAdapter" ref="jpaVenderAdapter"/>
    </bean>

    <bean id="transactionManager" class="org.springframework.orm.jpa.JpaTransactionManager">
        <property name="entityManagerFactory" ref="entityManagerFactory"/>
    </bean>
</beans>
```

JpaVenderAdapter는 JPA 프로바이더에 알맞은 설정을 제공하기 위한 어댑터 클래스로 DB, SQL 출력 여부 등을 설정

> JPA 프로바이더들이 공통으로 제공하는 프로퍼티 외에 프로퍼티를 추가하고 싶다면 LocalContainerEntityManagerFactoryBean의 jpaProperties 속성을 이용한다.



## EntityManagerFactory와 EntityManager 사용

Hibernate에서는 SessionFactory를 이용하여 Session을 가져와서 사용한 것처럼, JPA에서는 EntityManagerFactory에서 EntityManager 객체를 가져와 사용한다. 

### EntityManagerFactory 객체를 주입 받는 방법

1) xml에서 직접 EntityManagerFactory를 전달

```xml
<bean id="itemRepository" class="com.spring.orm.store.persistence.JpaItemRepository">
    <property name="entityManagerFactory" ref="entityManagerFactory"/>
</bean>
```

2) 어노테이션 이용

```xml
<bean class="org.springframework.orm.jpa.support.PersistenceAnnotationBeanPostProcessor"/>
```

```java
@PersistenceUnit
private EntityManagerFactory entityManagerFactory;
```

### 현재 진행중인 트랜잭션 범위에서 EntityManager가 동작하게 만들기

1) joinTransaction()

```java
EntityManager entityManager = entityManagerFactory.createEntityManager();
entityManager.joinTransaction();// 현재 진행중인 트랜잭션에 ㅏㅁ여
entityManager.persist(paymentInfo);
```

2) @PersistenceContext

```java
@PersistenceContext
private EntityManager entityManager;//트랜잭션에 이미 연동된 EntityManager 사용
```



# MyBatis

## MyBatis란?

개발자가 지정한 SQL, 저장 프로시저 , 그리고 몇 가지 고급 매핑을 지원하는 영속 프레임워크. JDBC로 처리하는 상당 부분의 코드와 파라미터 설정 및 결과 매핑을 대신한다. 

스프링4 버전에는 Mybatis/ibatis 의 연동 기능이 포함되어 있지 않음. 대신 Mybatis가 직접 스프링과 Mybatis를 연동하기 위한 모듈을 제공하고 있다.  (MyBatis-Spring)

- 스프링 연동을 더 쉽게할 수 있도록 도와준다.
- 스프링 트랜잭션에 쉽게 연동
- 마이바티스 매퍼와 SqlSesison을 다루고 다른 빈에 주입시켜준다.
- 마이바티스 예외를 스프링의 DataAccessException으로 변환

## 의존성 설정

- MyBatis-Spring은 스프링의 버전에 따라 다르게 사용해야함.

  MyBatis-Spring 2.0 는 스프링 5.0+ 이상을 필요로 함.

  MyBatis-Spring 1.3 는 스프링 3.2.2+가 필요 

```xml
    <dependencies>
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-context</artifactId>
            <version>4.3.25.RELEASE</version>
        </dependency>
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-jdbc</artifactId>
            <version>4.3.25.RELEASE</version>
        </dependency>
        <dependency>
            <groupId>org.mybatis</groupId>
            <artifactId>mybatis-spring</artifactId>
            <version>1.3.3</version>
        </dependency>
        <dependency>
            <groupId>org.mybatis</groupId>
            <artifactId>mybatis</artifactId>
            <version>3.5.2</version>
        </dependency>
        <dependency>
            <groupId>com.mchange</groupId>
            <artifactId>c3p0</artifactId>
            <version>0.9.5.4</version>
        </dependency>
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>6.0.6</version>
        </dependency>
	</dependencies>
```

## SqlSessionFactoryBean설정

```xml
<bean id="sqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
    <property name="dataSource" ref="dataSource"/>
    <property name="mapperLocations" value="classpath:/mappers/*.xml"/>
</bean>
```

SqlSessionFactoryBean은 스프링의 FactoryBean를 구현한 클래스이다. 빈을 가져올 때 SqlSessionFactoryBean이 아닌 SqlSessionFactory가 리턴된다. 

`mapperLocations`는 매퍼와 관련된 파일들을 지정

> MyBatis-Spring 1.3 버전 이후부터는 xml 설정 파일없이도 가능하다.
>
> ```xml
> <property name="configuration">
>     <bean class="org.apache.ibatis.session.Configuration">
>         <property name="mapUnderscoreToCamelCase" value="true"/>
>     </bean>
> </property>
> ```

`databaseIdProvider` 속성은 여러 개의 데이터베이스를 사용해야 할 때 설정

```xml
<!-- 예시 -->
<bean id="databaseIdProvider" class="org.apache.ibatis.mapping.VendorDatabaseIdProvider">
  <property name="properties">
    <props>
      <prop key="SQL Server">sqlserver</prop>
      <prop key="DB2">db2</prop>
      <prop key="Oracle">oracle</prop>
      <prop key="MySQL">mysql</prop>
    </props>
  </property>
</bean>

<bean id="sqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
  <property name="dataSource" ref="dataSource" />
  <property name="mapperLocations" value="classpath*:sample/config/mappers/**/*.xml" />
  <property name="databaseIdProvider" ref="databaseIdProvider"/>
</bean>
```

## Transaction 설정

```xml
<bean id="transactionManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
    <property name="dataSource" ref="dataSource"/>
</bean>
```

여기에서 프로퍼티로 넘겨주는 `dataSource`가 위의 `SqlSesissionFactoryBean`의 `dataSource`와 같아야 한다는 것 만 주의한다.

## SqlSessionTemplate

`SqlSessionTemplate`은 `SqlSession`을 구현하고 코드에서 `SqlSession`을 대체하는 역할을 한다. 그리고 아래와 같은 특징들을 지닌다.

- 쓰레드에 안전하여 여러개의 DAO나 매퍼에서 공유할 수 있다. 
- `SqlSession`이 현재의 트랜잭션에서 사용될 수 있도록 한다.
- 필요한 시점에 세션을 닫고, 커밋하거나 롤백하는 것을 포함한 세션의 생명주기를 관리한다.
- 마이바티스 예외를 스프링의 `DataAccessException`으로 변환

```xml
<bean id="sqlSessionTemplate" class="org.mybatis.spring.SqlSessionTemplate">
    <constructor-arg ref="sqlSessionFactory"/>
</bean>
```

위와 같이 설정한 후 필요한 DAO에 주입하여 사용한다.

```xml
<bean id="itemDao" class="com.spring.orm.store.dao.MyBatisItemDao">
	<property name="sqlSession" ref="sqlSessionTemplate"/>
</bean>
```

```java
@Repository
public class MyBatisItemDao implements ItemDao {

	private SqlSession sqlSession;

	public void setSqlSession(SqlSession sqlSession) {
		this.sqlSession = sqlSession;
	}

	@Override
	public Item findById(Integer itemId) {
		Item item = (Item) sqlSession.selectOne(
				"net.madvirus.spring4.chap13.store.dao.ItemDao.findById",
				itemId);
		return item;
	}
}
```

### SqlSessionDaoSupport

SqlSessionDaoSupport는 스프링과 연동된 SqlSession을 제공하는 getSqlSession()메서드를 포함.

```xml
<bean id="itemDao2" class="com.spring.orm.store.dao.MyBatisItemDao2">
    <property name="sqlSessionTemplate" ref="sqlSessionTemplate"/>
</bean>
```

```java
public class MyBatisItemDao2 extends SqlSessionDaoSupport implements ItemDao {
	@Override
	public Item findById(Integer itemId) {
		Item item = (Item) getSqlSession().selectOne(
				"com.spring.orm.store.dao.ItemDao.findById",
				itemId);
		return item;
	}
}
```





## 매퍼 주입

데이터 접근 객체인 DAO를 만드는 것보다 매퍼를 직접 주입받아서 작성하면 `SqlSession` 세션을 선언하거나 생성하거나 열고 닫을 필요가 없다.

### 매퍼 등록

매퍼를 등록하는 방법은 `MapperFactoryBean`을 만드는 것

```xml
<bean id="itemMapper" class="org.mybatis.spring.mapper.MapperFactoryBean">
    <property name="mapperInterface" value="com.spring.orm.store.mapper.ItemMapper"/>
    <property name="sqlSessionFactory" ref="sqlSessionFactory"/>
</bean>
```

`ItemMapper`가 mapperInterface와 같은 경로의 클래스패스 혹은 `SqlSessionFactoryBean`의 `configLocation` 프로퍼티에 설정된 경로에 마이바티스 XML 매퍼 파일을 가지고 있으면 `MapperFactoryBean`이 자동으로 파싱한다. 

### 매퍼 스캔

위와 같이 매퍼를 일일히 등록하지 않고 자동스캔기능을 이용하여 사용할 수 있다.

- \<mybatis:scan/> 
  - 빈 이름은 스프링의 디폴트 명명규칙 전략 혹은 지정된 이름
- @MapperSacne 
- MapperScannerConfigurer





