# 데이터베이스 연동 지원과 JDBC 지원

- 스프링은 JDBC를 비롯하여 ORM 프레임워크를 직접적으로 지원하므로 쉽게 연동가능하다



# 스프링의 데이터베이스 연동 지원

- 스프링은 JDBC를 이용한 DAO 클래스를 구현할 수 있도록 다양한 기능을 지원
  - 템플릿 클래스를 통한 데이터 접근 지원
  - 의미 있는 익셉션 타입
  - 트랜잭션 처리



## 데이터베이스 연동을 위한 템플릿 클래스

- 기본적인 JDBC를 사용할 경우 <mark>중복되는 부분</mark>이 있다.

  ```java
  Connection conn = null;
  PreparedStatement pstmt = null;
  ResultSet rs = null;
  
  try {
      
      
  }catch(){
      
  }finally{
      if(rs!=null) rs.close();
      if(pstmt!=null) pstmt.close();
      if(conn!=null)conn.close();
  }
  ```

- 이러한 반복적인 코드를 위해서 스프링은 <mark>템플릿 메서드 패턴과 전략 패턴</mark>을 함께 사용하여 JDBC 템플릿 클래스를 제공하고 있다.

- <mark>JDBC 템플릿</mark>을 이용한다면 try-catch-finally 이나 커넥션 관리를 위한 <mark>코드를 줄일 수 있다</mark>.

  ```java
  List<Message> messages = jdbcTemplate.query(
  	"select * from guestmessage order by id desc limit ?,?",
      new Object[]{start, size},
      new RowMapper<Message>(){
          public Message mapRow(ResultSet rs, int rowNum) throws SQLException{
              Message m = new Message();
              m.setId(rs.getId("id"));
              
              return m;
          }
      }
  );
  ```

  

## 스프링의 익셉션 지원

- <mark>JDBC 프로그래밍에 있어서 에러는 항상 SQLException</mark>이다. 
- 항상 SQLException 에러가 발생한다는 것은 <mark>어떤 과정에서 무슨 에러가 났는지 알 수 없다는 뜻</mark>이다.
- 예외가 발생하면 SQLException이 어디서 에러가 발생했는지 찾아야한다. 일일히..
- JdbcTemplate 클래스는 DB 연동 과정에서 SQLException이 발생하면 <mark>스프링이 제공하는 익셉션 클래스 중 알맞은 익셉션 클래스로 변환하여 발생</mark>시킨다. 
- JdbcTemplate 뿐만 아니라 JPA, Hibernate, Mybatis도 마찬가지로 스프링이 제공하는 익셉션 클래스로 알맞게 변환하여 발생시킨다. 



# DataSource 설정

- DataSource 설정을 하는 방법
  - 커넥션 풀을 이용한 DataSource 설정
  - JNDI를 이용한 DataSource 설정
  - DriverManager를 이용한 DataSource 설정(테스트 목적)



## 커넥션 풀을 이용한 DataSource

- 스프링은 커넥션 풀 구현 클래스를 제공하고 있지 않다

- 커넥션 풀 라이브러리의 의존이 필요

  ```xml
  <dependency>
      <groupId>com.mchange</groupId>
      <artifactId>c3p0</artifactId>
      <version>0.9.5.4</version>
  </dependency>
  ```



## JNDI를 이용한 DataSource 설정

- JEE 어플리케이션 서버(weblogic, jboss), 웹 컨테이너(tomcat,jetty)를 사용할 경우, JNDI를 이용해서 설정할 때가 많다. 

- \<jee:jdni-lookup> 태그 사용하기(네임스페이스 및 XML스키마 추가)

  ```xml
  <?xml version="1.0" encoding="UTF-8"?>
  <beans xmlns="http://www.springframework.org/schema/beans"
         xmlns:jee="http://www.springframework.org/schema/jee"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://www.springframework.org/schema/beans
         http://www.springframework.org/schema/beans/spring-beans.xsd
         http://www.springframework.org/schema/jee
         http://www.springframework.org/schema/jee/spring-jee.xsd">
  
      <jee:jndi-lookup id="dataSource" jndi-name="jdbc/guestbook" resource-ref="true"/>
  
  </beans>
  ```

- 태그 없이 클래스를 이용해서 DataSource 구하기

  ```xml
  
  ```

  

