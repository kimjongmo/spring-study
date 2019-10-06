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

책에서는 jdbcTemplate를 이용하는데 MyBatis나 JPA등의 동적 쿼리 기능을 위해 검색 조건들을 객체(SearchCondition)로 만들고 조건에 따라 쿼리를 생성한다.k5



### 인터페이스의 크기

DAO 인터페이스는 조회 기준이 되는 테이블마다 1개를 작성하는 것이 일반적.

조인을 원한다면??

테이블을 조인하는 메서드

- 두 DAO에서 읽어와 객체를 조립

- 주 테이블에 해당하는 DAO에 메서드 추가

- 조인 결과를 위한 별도 DAO 인터페이스 작성

  

## 서비스
서비스는 사용자 기능을 정의한다. 일반적으로 서비스의 메서드는 트랜잭션 단위되므로 @Transactional 애노테이션이나 스키마 기반의 트랜잭션 설정을 이용하여 실행한다.



### 서비스의 크기

- 구현하는 메서드의 개수가 많아지면, 필요한 의존 객체를 참조하기 위한 필드 개수도 증가하게 된다. 
- 객체 지향 원칙 SOLID 중 Single Responsbility Principle, Interface Segregation Principle와 같이 여러 메서드를 하나의 서비스 인터페이스에 두는 것 보다 구분되는 기능을 위한 인터페이스를 따로 작성하기를 제안



### 서비스의 메서드 파라미터 타입

```java
public class Employee{
    ...
    private String modificationReason;
}

public class UpdateEmployeeInfoService{
    private EmployeeDao empDao;
    private ModificationHistoryDao historyDao;
    
    @Transactional
    public void updateInfo(Employee emp){
        Employee old = empDao.selectOne(emp.getId());
        if(old == null) throw new EmployeeNotFouncException();
        old.setAddress(emp.getAddress);
        ...
        empDao.update(old);
        
        historyDao.insert(new ModificationHistory(emp.getId()),emp.getModificationReason());
    }
}
```

위와 같은 방식으로 수정하는 것은 시간이 흐를수록 코드 유지보수를 힘들게 만드는 원인이 된다. Employee 클래스에  프로퍼티가 계속 추가되고 한 테이블과 일치하지 않는다. 

서비스의 파라미터가 모델 타입과 일치하지 않으면, 모델 타입을 사용하는 대신 서비스 메서드를 위한 별도 요청 타입을 작성하는 것이 좋다. 

```java
public class UpdateEmpRequest{
    private Long id;
    private String modificationReason;
}

public interface UpdateEmployeeService {
    public void updateInfo(UpdateEmpRequest updateRequest);
}
```



### 조회 기능과 서비스

조회 기능을 위한 서비스의 구현 코드는 다음과 같이 DAO의 메서드를 호출하고 끝나는 경우가 많다.

```java
public class ListEmployeeServiceImpl implements ListEmployeeService{
    @Transactional
    public List<Employee> getEmployeeList(int startRow, int size){
        return empDao.selectList(startRow,size);
    }
}
```

이렇게 DAO에 단순 위임만 하고 끝나는 서비스를 작성하는 것은 사실상 필요 없는 인터페이스를 만드는 것이다.

```java
public class EmployeeController {
    private EmployeeDao empDao;
    
    @RequestMapping("/hr/employee/list")
    public String list(@RequestParam("page")int pageNum, Model model){
        int startRow = (pageNum - 1)* PAGE_SIZE;
        model.addAttribute("employeeList",empDao.selectList(startRow,PAGE_SIZE));
        return "hr/employee/list";
    }
}
```



# 어플리케이션-도메인-영속성 구조

어플리케이션 - 도메인 - 영속성 구조는 DDD(Domain-Driven-Design)를 비롯해 도메인 모델을 중심으로 설계할 때 사용하는 구조이다. 이를 사용하면 복잡한 업무를 다루는 웹 어플리케이션을 개발할 때 객체 지향의 장점을 잘 살릴 수 있게 된다. 

| 영역         | 구성 요소      | 역할                                                         |
| ------------ | -------------- | ------------------------------------------------------------ |
| 도메인       | 엔티티         | 핵심 도메인 모델로서 구분되는 식별값을 가지며, 도메인 로직을 실행한다. 엔티티 외에 값 객체 등이 존재한다. |
| 도메인       | 리파지터리     | 엔티티 객체를 보관하고 제공하는 기능                         |
| 영속성       | 리파지터리구현 | 도메인 영역의 리파지터리 인터페이스의 구현을 제공한다. 보통 JPA나 하이버네이트와 같은 ORM 기술을 이용해서 구현 |
| 어플리케이션 | 서비스         | 도메인 영역의 리파지터리와 엔티티를 이용해서 클라이언트가 요청한 기능을 실행 |

