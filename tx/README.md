# 트랜잭션

- 여러 과정을 하나의 행위로 묶어서 사용하는 것
- 행위 안에 하나라도 실패하면 전체 과정을 취소시킴( 데이터의 무결성 )



## ACID

- 원자성(Atomicity) : 트랜잭션 범위에 있는 모든 동작이 실행되거나 , 모두 취소를 보장한다.
- 일관성(Consistency) : 
- 고립성(Isolation) : 트랜잭션은 다른 트랜잭션과 독립적으로 실행되어야 한다. 서로 다른 트랜잭션이 같은 데이터에 대하여 접근할 때는 동시 접근에 제어해야한다.
- 지속성(Durability) : 트랜잭션이 완료되었을 때, 그 결과는 영구적으로 저장된다.



# 스프링의 트랜잭션 

- 코드 기반의 트랜잭션 처리(Programmatic Transaction)
- 선언적 트랜잭션(Declarative Transaction)
- 개발자가 원하는 트랜잭션의 범위 및 규칙을 정의할 수 있다.
- 스프링은 데이터베이스 연동 기술에 상관없이 동일한 방식으로 트랜잭션을 처리한다.
  - JDBC, JPA, JTA, Hibernate 중 어떠한 연동 기술에 상관없이 동일한 코드를 이용해서 트랜잭션 관리
  - DB연동 구현 기술에 알맞은 트랜잭션 관리자를 등록해주어야 한다.
  - 트랜잭션 관리자 구현 클래스는 관련된 데이터베이스 기술에 따라 알맞은 트랜잭션 처리


## JDBC 기반 트랜잭션 관리자 설정

...

## JPA 트랜잭션 관리자 설정

...

## 하이버네이트 트래내잭션 관리자 설정

...

## JTA 트랜잭션 관리자 설정

...

## 트랜잭션 전파와 격리 레벨

진행중인 트랜잭션이 있는 상태에서 새로운 트랜잭션을 시작하려면 아래와 같은 방식으로 할 수 있다.

```java
Connection conn1 = getConnection();
conn.setAutoCommit(false);
...
   Connection conn2 = getConnection();
   conn2.setAutoCommit(false);

   conn2.close();
...
conn1.close();
```

- 스프링의 전파와 관련된 부분들을 지원하고 있다.

  | 트랜잭션 전파 | 설명                                                         |
  | ------------- | ------------------------------------------------------------ |
  | REQUIRED      | 현재 진행 중인 트랜잭션이 존재하면, 해당 트랜잭션을 사용. 없다면 새로 생성 |
  | MANDATORY     | 현재 진행 중인 트랜잭션이 존재하지 않는다면 익셉션 발생      |
  | REQUIRED_NEW  | 현재 진행 중인 상태와 상관없이 항상 새로운 트랜잭션 시작     |
  | SUPPORTS      | 진행 중인 트랜잭션이 없더라도 메서드를 그냥 실행             |
  | NOT_SUPPORTED | 진행 중인 트랜잭션이 있다면 일시 중지시키고 메서드 실행. 후에 트랜잭션 진행 |
  | NEVER         | 진행 중인 트랜잭션이 있을 경우 익셉션 발생                   |
  | NESTED        | 기존 트랜잭션이 존재하면, 기존 트랜잭션에 중첩된 트랜잭션에서 메서드를 실행. 기존 트랜잭션이 존재하지 않으면 REQUIRED와 동일하게 동작. |

- 스프링은 격리와 관련된 부분들도 지원

  | 격리 레벨        | 설명                                                         |
  | ---------------- | ------------------------------------------------------------ |
  | DEFAULT          | 기본 설정을 사용                                             |
  | READ_UNCOMMITTED | 다른 트랜잭션에서 커밋하지 않은 데이터를 읽을 수 있다.       |
  | READ_COMMITTED   | 다른 트랜잭션에 의해 커밋된 데이터를 읽을 수 잇다.           |
  | REPEATABLE_READ  | 처음에 읽어 온 데이터와 두 번째 읽어 온 데이터가 동일한 값을 갖는다 |
  | SERIALIZABLE     | 동일한 데이터에 대해서 동시에 두 개 이상의 트랜잭션이 수행될 수 없다. |



# TransactionTemplate 

- 트랜잭션과 관련된 작업(트랜잭션 시작, 커밋, 롤백 등)을 처리해주는 템플릿 클래스



## 설정

```xml
<bean id="transactionManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
    <property name="dataSource" ref="dataSource"/>
</bean>
    
<bean id="transactionTemplate" class="org.springframework.transaction.support.TransactionTemplate">
    <property name="transactionManager" ref="transactionManager"/>
</bean>
```



## execute(TransactionCallBack\<T>)

- 내부적으로 PlatformTransactionManager를 이용하여 트랜잭션을 가져옴
- 전달받은 TransactionCallback 의 doInTransaction() 실행
- 메서드가 정상적으로 실행되었다면 PlatformTransactionManger에게 커밋 요청



##  TransactionCallBack\<T> 

```java
public interface TransactionCallback<T> {
    T doInTransaction(TransactionStatus var1);
}
```

- doInTransaction() 메서드는 throws를 통해서 발생시킬 수 있는 익셉션을 설정하고 있지 않다.
- 때문에 메서드는 RuntimeException이나 Error타입의 예외만 발생시킬 수 있다.



## TransactionTemplate의 트랜잭션 설정

- 트랜잭션템플릿의 기본 설정

  - 트랜잭션 전파 속성 : REQUIRED
  - 트랜잭션 격리 레벨 : 사용하는 데이터베이스의 기본 값
  - 트랜잭션 타임아웃 : 없음
  - 읽기 전용 : false

- 빈을 설정할 때 위의 설정값 변경 가능

  ```xml
  <bean id="transactionTemplate" class="org.springframework.transaction.support.TransactionTemplate">
      <property name="transactionManager" ref="transactionManager"/>
      <property name="isolationLevelName" value="ISOLATION_SERIALIZABLE"/>
      <property name="propagationBehaviorName" value="PROPAGATION_REQUIRES_NEW"/>
  </bean>
  ```



# 트랜잭션과 DataSource

- JDBC의 경우 동일한 Connection을 사용해야 한 트랜잭션을 묶을 수 있다.

- 그렇다면 아래와 같은 코드에 있어 각각의 JdbcTemplate가 따로 존재하는데 쿼리가 실행될 때마다 새로운 Connection이 생성되어 있지 않을까?

  - JdbcTemplate의 쿼리 실행 메서드는 내부적으로 아래의 코드를 이용해 Connection을 구한다.

    ```java
    Connection con = DataSourceUtils.getConnection(getDataSource());
    ```

    DataSourceUtils.getConnection()는 현재 코드가 트랜잭션 범위에서 실행되고 있으면, 트랜잭션과 엮여 있는 Connection을 리턴한다.

  - DataSourceUtils.getConnection()는 어떻게 같은 트랜잭션 범위에 있는지 알수 있을까?

    좀 더 정확히 말하면 이는 DataSourceTransactionManager가 처리한다.

    - 트랜잭션 매니저가 트랜잭션을 시작을 할 때 DataSourceUtils에 트랜잭션이 시작되었음을 알린다.



# 선언적 트랜잭션 처리

- 선언적 트랜잭션이란 TransactionTemplate과 달리 트랜잭션 처리를 코드에서 직접 수행하지 않고, 설정 파일이나 어노테이션을 이용해서 트랜잭션의 범위, 롤백 규칙 등을 정의하는 것이다.
- 선언적 트랜잭션의 두 가지 방식
  - \<tx:advice> 태그를 이용한 트랜잭션 처리
  - @Transaction 어노테이션을 이용한 트랜잭션 설정



## tx 네임 스페이스 이용한 트랜잭션 설정

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:aop="http://www.springframework.org/schema/aop"
       xmlns:tx="http://www.springframework.org/schema/tx"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
        http://www.springframework.org/schema/beans/spring-beans.xsd
        http://www.springframework.org/schema/aop
        http://www.springframework.org/schema/tx
        http://www.springframework.org/schema/aop/spring-aop.xsd
        http://www.springframework.org/schema/tx/spring-tx.xsd">
    <import resource="classpath:datasource.xml"/>

    <bean id="transactionManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
        <property name="dataSource" ref="dataSource"/>
    </bean>

    <tx:advice id="txAdvice" transaction-manager="transactionManager">
        <tx:attributes>
            <tx:method name="order" propagation="REQUIRED"/>
            <tx:method name="get*" read-only="true"/>
        </tx:attributes>
    </tx:advice>
</beans>
```



- \<tx:advice> 태그는 트랜잭션을 적용할 때 사용될 Advisor를 생성

  - id 속성은 Advisor 간의 식별 값
  - transaction-manager 속성에는 PlatformTransactionManager 빈을 설정

- \<tx:attributes>

- \<tx:method> 태그는 트랜잭션을 `적용할 메서드` 및 `트랜잭션 속성`을 설정

  | 속성            | 설명                                      |
  | --------------- | ----------------------------------------- |
  | name            | 트랜잭션이 적용될 메서드 이름 혹은 패턴   |
  | propagation     | 트랜잭션 전파 규칙                        |
  | isolation       | 트랜잭션 격리 레벨                        |
  | read-only       | 읽기 전용 여부                            |
  | no-rollback-for | 트랜잭션을 롤백하지 않을 익셉션 타입 지정 |
  | rollback-for    | 트랜잭션을 롤백할 익셉션 타입 지정        |
  | timeout         | 트랜잭션의 타임 아웃 시간을 지정          |

- 실제로 트랜잭션을 적용하는 것은 AOP를 통해서 이루어진다.

  ```xml
  <aop:config>
      <aop:pointcut id="publicMethod" expression="execution(public * com.spring.tx..*Service.*(..))"/>
      <aop:advisor advice-ref="txAdvice" pointcut-ref="publicMethod"/>
  </aop:config>
  ```

  













