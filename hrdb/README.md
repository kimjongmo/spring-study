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



## 도메인 구성

도메인 영역은 두 가지 종류의 구성 요소를 갖는다.

- 엔티티
  - 엔티티, 값, 객체 등 도메인 모델을 표현하는 객체를 제공한다. 
  - 객체들은 모델은 표현하는데 필요한 프로퍼티를 포함한다. 
  - 엔티티와 DTO의 중요한 차이점은 엔티티는 도메인 기능을 함께 제공하는데 반해 DTO는 단순히 영역 간의 주고 받는 데이터를 담는 구조체라는 것이다. 
- 리파지터리
  - 리파지터리는 엔티티 객체의 생명주기를 관리(엔티티 보관, 검색, 제거)한다.
  - 도메인 영역에서 리파지터리는 엔티티 관리를 위한 인터페이스만 제공하며, 실제 구현은 영속성 영역에서 다루게 된다.



예시를 들어 회원 도메인 모델을 들면 아래와 같은 엔티티 클래스와 리파지터리로 구현해 볼 수 있다.

```java

public class Member {
	private Long id;
	private String userId;
    ...

    // 엔티티는 도메인 기능을 함께 제공하는 객체
	public void changePassword(String oldPw, String newPw) {
		if (!matchPassword(oldPw))
			throw new WrongPasswordException();
		this.encPassword = encrypt(newPw);
	}
    
    ...

}

```

```java
public interface MemberRepository extends Repository<Member, Long> {
	Member findOne(Long id);
	Member findByUserId(String userId);
	Member findByEmail(String email);
    Member save(Member m);
}
```

리파지터리의 구현은 영속성 영역에 위치하게 된다.



## 영속성 구현

- 리파지터리 인터페이스가 도메인 영역에 위치하고, 인터페이스 구현은 영속성 영역에 위치한다. 

- 영속성 영역을 구현할 때는 편리함 때문에 주로 ORM 기술을 많이 사용하지만 다른 기술들로도 구현가능하다.

- JPA를 이용해서 영속성을 구현할 경우 도메인 영역의 엔티티나 다른 모델 클래스에 JPA 애노테이션을 적용하게 된다. 

  ```java
  @Entity
  @Table(name = "MEMBER")
  public class Member {
  
  	@Id
  	@GeneratedValue
  	@Column(name = "MEMBER_ID")
  	private Long id;
  	@Column(name = "USER_ID")
  	private String userId;
  	
      ...
  
  	public void changePassword(String oldPw, String newPw) {
  		if (!matchPassword(oldPw))
  			throw new WrongPasswordException();
  		this.encPassword = encrypt(newPw);
  	}
      
      ...
  
  }
  
  ```

- 도메인 영역의 코드가 영속성과 관련된 코드(애노테이션)을 포함하고 있다. 

- 도메인 영역의 코드에서 영속성 영역의 코드에 대한 의존을 갖지 않도록 해야 하지만, 이러한 코드(애노테이션)가 일부 포함되더라도 실용적인 측면에서 문제되지 않는다고 생각한다.



## 어플리케이션 서비스 구현

어플리케이션 영역은 시스템이 제공하는 기능을 구현한다. 클라이언트의 요청을 받아, 도메인 영역의 구성 요소를 이용해서 요청을 처리하고, 그 결과를 리턴한다. 어플리케이션의 서비스 구현 코드는 보통 아래와 같은 구성을 갖는다.

- 도메인 영역의 리파지터리에서 엔티티를 구한다.
- 엔티티의 기능을 실행
- 결과를 리턴



회원 암호 변경 기능을 제공하는 어플리케이션 서비스

```java
public interface ChangePasswordSerivce {
    public Long register(Long memberId,String currentPassword, String newPassword);
}
public class ChangePasswordServiceImpl implements ChangePasswordSerivce {
    @Autowired
    private MemberRepository memberRepository;
    
    @Transactional
    @Override
    public void changePassword(Long memberId,String currentPassword, String newPassword){
        Member member = memberRepository.findById(memberId);
        if(member==null) throw new MemberNotFoundException();
        member.changePassword(currentPassword,newPassword);
    }
}
```

서비스를 실행하는데 필요한 데이터가 많다면, 데이터를 담은 클래스를 만들어 메서드의 파라미터 타입으로 사용할 수 있을 것이다.

```java
public class NewMemberRequest{
    private String userId;
    private String password;
    private String confirmPassword;
}
public interface ChangePasswordSerivce {
    public Long register(NewMemberRequest req);
}
```



## 컨트롤러와 뷰 그리고 도메인 객체 접근

클라이언트의 요청은 두 가지 종류로 나누어 볼 수 있다.

- 상태를 변화시키는 기능 : 암호를 변경한다거나 새로운 회원을 등록하는 등의 기능
- 데이터를 조회하는 기능 : 회원 정보를 화면에 보여준다거나, 회원 목록을 조회하는 기능



이 중에서 데이터를 조회하는 기능을 생각해보자. 

첫 번째 방식은 컨트롤러에서 직접 리파지터리의 기능을 이용하는 것이다.

```java
@Controller
public class MemberDetailController{
    @Autowired
    private MemberRepository memberRepository;
    
    @RequestMapping("/admin/member/detail")
    public String detail(@RequestParam("memberId") Long memberId, Model model){
        Member member = memberRepository.findById(memberId);
        if(member == null)
            return "admin/noMember";
        
        model.addAttribute("member",member);
        return "admin/memberDetail";
    }
}
```

위 코드에서 주의해야 하는 곳은 Member 클래스다. 만약  Member 클래스가 아래와 같이 지연 로딩 방식을 갖게 된다면 어떻게 될까?

```java
@Entity
public class Member{
    @Id
    @GeneratedValue
    private Long id;
    ...
    @OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="LOCKER_ID")
    private Locker locker;
    ...
}
```

JPA의 특성을 이해해야하겠지만 지연 로딩 방식은 트랜잭션 범위에서 실행된다. 그전까지는 프로퍼티에 프록시 객체가 들어가있다. 때문에 위와 같이 트랜잭션 범위 밖에서 이 프로퍼티에 접근할 경우, 익셉션이 발생하게 된다. 

```html
라커 크기 : ${member.locker.size} <!-- 프록시 객체 -->
```

그렇다고 즉시 로딩 방식을 사용하게 된다면 조회 성능에 문제가 발생할 수 있다. 이런 이유로 지연 로딩을 사용할 경우 뷰 실행 과정에서 문제가 발생하지 않도록 다음의 두 방법 중 하나를 사용한다. 

- OSIV(Open Session In View) 패턴

  OSVI 패턴은 서블릿 필터를 이용해서 웹 요청이 시작될 때 JPA 세션을 시작하고, 웹 요청 처리가 끝나면 세션을 종료한다. JPA 세션 범위에서 JSP가 실행되기 때문에 지연 로딩 대상 프로퍼티에 접근하더라도 DB로부터 알맞게 대상 객체를 로딩할 수 있게 된다.

- 뷰에서 필요한 데이터를 트랜잭션 범위 내에서 로딩

  

### OSIV 패턴

```xml
<filter>
	<filter-name>openEntityManagerFilter</filter-name>
    <filter-class>
    	org.springframework.orm.jpa.support.OpenEntityManagerViewFilter
    </filter-class>
    <init-param>
    	<param-name>entityManagerFactoryBeanName</param-name>
        <param-value>entityManagerFactory</param-value>
    </init-param>
</filter>

<filter-mapping>
	<filter-name>OpenEntityManagerFilter</filter-name>
    <url-pattern>/*</url-pattern>
</filter-mapping>
```

OpenEntityManagerInViewFilter를 사용할 때 주의할 점은이 객체가 EntityManagerFactory 객체를 검색할 때 사용하는 스프링 컨테이너는 DispatcherServlet이 사용하는 컨테이너가 아닌 서블릿 컨텍스트를 위한 컨테이너라는 점이다. 한 마디로 사용하는 컨테이너가 서로 다르다. 따라서 스프링 컨테이너를 설정할 때에는 다음과 같이 DispatcherServlet에서는 웹 MVC 관련 설정만 하고, 나머지 EntityManagerFactory를 포함한 어플리케이션과 관련된 설정은 ContextLoaderLister를 이용하는 방법을 사용해야 한다. 



```xml
<web-app>
	<listener>
    	<listener-class>
        	org.springframework.web.context.ContextLoaderListener
        </listener-class>
    </listener>
    
    <context-param>
    	<param-name>contextConfigLocation</param-name>
        <param-value>classpath:/jpa.xml,classpath:/application</param-value>
    </context-param>
    
    <servlet>
    	<servlet-name>dispatcher</servlet-name>
        <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
        <init-param>
        	<param-name>contextConfigLocation</param-name>
            <param-value>
            	/WEB-INF/spring-mvc.xml
            </param-value>
        </init-param>
    </servlet>
    
    <servlet-mapping>
    	<servlet-name>dispatcher</servlet-name>
        <url-pattern>/</url-pattern>
    </servlet-mapping>

	<filter>
    	<filter-name>openEntityManagerFilter</filter-name>
        <filter-class>
        	org.springframework.orm.jpa.support.OpenEntityManagerInViewFilter
        </filter-class>
        <init-param>
        	<param-name>entityManagerFactoryBeanName</param-name>
            <param-value>entityManagerFactory</param-value>
        </init-param>
    </filter>
    <filter-mapping>
    	<filter-name>openEntityManagerFilter</filter-name>
    	<url-pattern>/*</url-pattern>
    </filter-mapping>
</web-app>
```

