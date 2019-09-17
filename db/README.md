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

- 스프링 빈 생성

  ```xml
  <bean id="dataSource" class="com.mchange.v2.c3p0.ComboPooledDataSource" destroy-method="close">
      <property name="driverClass" value="com.mysql.jdbc.Driver"/>
      <property name="jdbcUrl" value="jdbc:mysql://localhost/guest"/>
      <property name="password" value="1234"/>
      <property name="user" value="kim"/>
  </bean>
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





## DriverManager를 이용한 DataSource 설정

- <mark>로컬 테스트 목적</mark>으로 DataSource가 필요한 경우에만 사용한다.
- <mark>DriverManager는 커넥션 풀이 아니기 때문</mark>에 실제 운영 환경에서 사용할 경우 성능상의 문제가 발생할 수 있음.



# 스프링 JDBC 지원

- 일반적으로 JDBC를 사용하게 되었을 때 Connection을 구하거나 try-catch-finally 와 같은 문장이 반복된다.
- 스프링은 위에서 예시로 봤던 바와 같이 템플릿 클래스를 제공함으로 이러한 중복된 코드를 제거한다.
  - JdbcTemplate : 기본적인 JDBC 템플릿
  - NamedParameterJdbcTemplate : PreparedStatement에서 이름을 가진 파라미터를 사용 가능
  - SimpleJdbcInsert : 데이터 삽입을 위한 인터페이스를 제공하는 클래스
  - SimpleJdbcCall : 프로시저 호출을 위한 인터페이스를 제공하는 클래스



## JdbcTemplate

- SQL 실행을 위한 메서드를 제공하고 있음

- JdbcTemplate 클래스를 사용하기 위해서는 Datasource를 전달받아 생성한다.

  ```java
  private JdbcTemplate jdbcTemplate;
  
  public GuestMessageDAO(DataSource dataSource){
      this.jdbcTemplate = new JdbcTemplate(dataSoure);
  }
  ```

  ``` xml
  <bean id="dataSource" class="">...</bean>
  
  <bean id="guestMessageDAO" class="com.spring.db.dao.JdbcMessageDao">
  	<constructor-arg ref="dataSource"/>
  </bean>
  
  <!-- 혹은 아래와 같이 조립된 상태로 설정 -->
  
  <bean id="jdbcTemplate" class="org.springframework.jdbc.core.JdbcTemplate">
  	<property name="dataSource" ref="dataSource"/>
  </bean>
  <bean id="guestMessageDAO" class="com.spring.db.dao.GuestMessageDAO">
  	<property name="jdbcTemplate" ref="jdbcTemplate"/>	
  </bean>
  
  
  ```

  

### 조회를 위한 메서드 : query()

- <mark>쿼리 실행 결과를 객체 목록으로 가져올 때 사용</mark>

- 조회를 위한 대표적인 메서드 종류

  - query(String sql, RowMapper\<T> rowMapper);

    - `sql` : 쿼리, 위치 기반 파라미터를 이용하는 PreparedStatement 쿼리 사용 가능

    - `rowMapper` : 조회 결과에서 객체를 생성해주는 매퍼.

  - query(String sql, Object[] args, RowMapper\<T> rowMapper);

    - `args` : PreparedStatemt를 실행할 떄 사용할 파라미터 바인딩 값 목록

  - query(String sql, Object[] args, int[] argTypes, RowMapper\<T> rowMapper);

    - `argTypes` : 파라미터를 바인딩 할 때 사용할 SQL 타입 목록.

### 컬럼을 1개만 조회하기 위한 메서드 : queryForList()

- 쿼리 실행으로 가져올 데이터의 <mark>컬럼이 1개일 때 사용</mark>
- 메서드 종류
  - queryForList(String sql, Class\<T> elementType);
    - `elementType`  : 조회할 데이터 타입
  - queryForList(String sql, Object[] args ,Class\<T> elementType);
  - queryForList(String sql, Object[] args, int[] argTypes ,Class\<T> elementType);



### 데이터 튜플의 개수를 1개만 조회하기 위한 메서드 : queryForObject()

- 쿼리 실행으로 가져올 데이터의 <mark>튜플의 개수가 1개</mark>일 때 사용.
- query() 메소드와 별반 다르지 않다. 하지만 <mark>검색 결과가 1개 이상일 경우 IncorrectResultSizeDataAccessException 발생</mark>.



### 삽입/ 수정/ 삭제를 위한 메서드 : update()

- 검색 외에 삽입/ 수정/ 삭제 쿼리를 실행할 때에는 update() 메서드를 사용한다.
- update() 메서드의 리턴 값은 <mark>변경된 행의 개수</mark>를 의미한다.



## NamedParameterJdbcTemplate

- JdbcTemplate와 동일한 기능을 제공

- 단, 인덱스 기반의 파라미터가 아니라 이름 기반의 파라미터를 설정

  ```java
  String sql = "select * from guestmessage order by id limit :start,:size";
  ```



### Map을 이용한 파라미터 값 설정 메서드

- JdbcTemplate에서는 Object배열을 이용하여 파라미터를 전달했지만, NamedParameterJdbcTemplate는 Map을 이용하여 파라미터 값을 설정한다.



### SqlParameterSource를 이용한 파라미터 값 설정

- 위에서는 Map을 이용했지만 이 대신 SqlParameterSource 인터페이스를 이용
- SqlParameterSource 구현 클래스
  - BeanPropertySqlParameterSource : 동일한 이름을 갖는 자바 객체의 프로퍼티 값을 이용
  - MapSqlParameterSource : Map과 같은 방식으로 키,값 쌍으로 설정





## SimpleJdbcInsert 

- 쿼리를 사용하지 않고 데이터를 삽입할 수 있도록 해주는 클래스

  ```java
  public int insert(Message message) {
      Map<String,Object> params = new HashMap<>();
      params.put("name",message.getName());
      params.put("message",message.getMessage());
      params.put("creationTime",message.getCreationTime());
      return simpleJdbcInsert.execute(params);
  }
  ```

- SimpleJdbcInsert 기본적인 세팅

  - 테이블명 지정(withTableName())

    ```java
    private SimpleJdbcInsert simpleJdbcInsert;
    
    public SimpleInsertMessageDao(DataSource dataSource){
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource);
        this.simpleJdbcInsert.withTableName("guestmessage");
    }
    ```

  - 컬럼 지정(usingColumns()) : 지정한 칼럼에 대해서만 값을 삽입. 이 외의 컬럼은 설정되지 않는다.

    ```java
    this.simpleJdbcInsert.usingColumns("name","message","creationTime");
    ```

### execute()를 이용한 데이터 삽입

- 메서드 종류
  - execute(Map<String,Object> args)
    - Map을 이용하는 경우 키값의 대소문자를 구분하지 않고 컬럼명과 Map의 키 값이 일치하는지 검사
  - execute(SqlParameterSource parameterSource)
    - SqlParameterSource를 이용하는 경우 아래의 규칙에 따라 칼럼명의 일치 여부를 검사
    - 지정한 컬럼명과 동일한 이름을 갖는 파라미터 값이 설정되어 있는 지 검사
    - '\_'이 포함된 경우 '\_'를 제외한 나머지 문자열과 일치하는 파라미터 값이 설정되어 있는 검사





