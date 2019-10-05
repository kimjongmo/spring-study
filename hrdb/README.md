# 서비스 - DAO 구조

웹 어플리케이션을 개발할 때 가장 많이 사용되는 구조 .

| 구성 요소 | 역할                                                         |
| --------- | ------------------------------------------------------------ |
| 모델      | 데이터베이스 테이블과 관련된 클래스가 위치한다.  Service - DAO 간 데이터를 주고 받기 위한 객체로도 사용된다. |
| DAO       | 데이터베이스 테이블에 대한 CRUD 기능을 정의.                 |
| 서비스    | 컨트롤러를 통해서 전달받은 사용자의 요청을 구현. DB 연산이 필요한 경우 DAO를 이용 |
| 컨트롤러  | 사용자의 웹 요청을 받아 서비스나 DAO에 전달하고 결과를 뷰에 전달. |



## DAO 인터페이스 

DAO 인터페이스를 정의한다.

```java
public interface EmployeeDao {
	public Long insert(Employee emp);
	public Employee selectOne(Long id);
	public List<Employee> selectList(SearchCondition cond);
	public Employee selectByEmployeeNumber(String number);
}
```

책에서는 jdbcTemplate를 이용하는데 MyBatis나 JPA등의 동적 쿼리 기능을 위해 검색 조건들을 객체(SearchCondition)로 만들고 조건에 따라 쿼리를 생성한다.



### 인터페이스의 크기

DAO 인터페이스는 조회 기준이 되는 테이블마다 1개를 작성하는 것이 일반적.

조인을 원한다면??

테이블을 조인하는 메서드

- 두 DAO에서 읽어와 객체를 조립

- 주 테이블에 해당하는 DAO에 메서드 추가

- 조인 결과를 위한 별도 DAO 인터페이스 작성

  

## 서비스
서비스는 사용자 기능을 정의한다. 일반적으로 서비스의 메서드는 트랜잭션 단위되므로 @Transactional 애노테이션이나 스키마 기반의 트랜잭션 설정을 이용하여 실행한다.
