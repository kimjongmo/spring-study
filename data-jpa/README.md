# Spring Data Jpa

DB 연동을 위해 사용되는 코드는 중복된 코드를 갖는 경우가 많다.(보일러 플레이트 코드) SessionFactory 라던지 EntityManager 객체를 받기 위해 필드를 선언하고 메소드에서 이 필드를 이용하여 쿼리를 호출하는 식으로 일어나는 부분은 반드시 선언해야 하지만 중복되는 부분들이다. 

```java
public class JpaItemRepository implements ItemRepository{
    @PersistContext
    private EntityManager entityManager;
    
    @Override
    public Item findById(Long Id){
        return entityManager.find(Item.class,id)
    }
}
```

스프링 데이터 모듈은 이런 <u>반복되는 코드의 양을 줄이는 것을 목표</u>로 한다. 규칙에 따라 인터페이스를 만들기만 하면 런타임시에 알맞은 구현 객체를 생성해주는 기능을 제공한다.



## 스프링 데이터 JPA 설정

```xml
    <dependencies>
        <dependency>
            <groupId>org.springframework.data</groupId>
            <artifactId>spring-data-jpa</artifactId>
            <version>1.6.6.RELEASE</version>
        </dependency>
        <dependency>
            <groupId>org.hibernate</groupId>
            <artifactId>hibernate-entitymanager</artifactId>
            <version>4.3.11.Final</version>
        </dependency>
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-orm</artifactId>
            <version>4.3.25.RELEASE</version>
        </dependency>
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-tx</artifactId>
            <version>4.3.25.RELEASE</version>
        </dependency>
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-context</artifactId>
            <version>4.3.25.RELEASE</version>
        </dependency>
        <dependency>
            <groupId>org.aspectj</groupId>
            <artifactId>aspectjweaver</artifactId>
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

> 뜬금없지만 이런거 보면 spring-boot-starter 패키지는 참 편하다.



# 리파지터리 인터페이스 메서드 작성 규칙

## Repository 인터페이스

스프링 데이터 JPA 모듈이 리파지터리로 사용할 인터페이스는 Repository 인터페이스를 상속받아야 한다.

```java
public interface UserRepository extends Repostiory<User,Long>{
    
}
```

## 조회 메서드

인터페이스에 메서드이름을 어떻게 만드냐에 따라서 조회쿼리가 달라진다. 기본적으로 조회 메서드는 find로 시작

```java
User findByLastNameAndFirstName(String lastName,String firstName);
//select * from user where lastname =?1 and firstname=?2

List<User> findByNameList(String search); //select * from user where name like ?1
```

### 개수 조회 메서드

개수를 구하는 메서드는 count로 시작한다.

```java
long count();
long countByTeamId(Long teamId);
```

### 쿼리 메서드의 중첩 프로퍼티 접근

클래스의 중첩 프로퍼티를 쿼리 메서드에서 사용하려면 단순히 중첩 프로퍼티 이름을 연속해서 붙여주면 된다.

```java
class Employee{
    private Team team;
}

//
public Iterable<Employee> findByTeamName(String teamName);
// select * from Employee where team.name = ?1
```

만약 같은 이름의 프로퍼티가 존재한다면 아래와 같다

```java
class Employee{
    private String teamName;
    private Team team;
}

public List<Employee> findByTeam_Name(String teamName);//_는 중첩 프로퍼티임을 나타냄
```

### 정렬과 페이징

- OrderBy (findBy로 시작해야함.)

  ```java
  public List<Employee> findByNameOrderByIdAsc(String name);
  ```

- Sort

  ```java
  public List<Employee> findAll(Sort sort);
  public List<Employee> findByTeam(Team team, Sort sort);
  ```

  ```java
  Sort sort = new Sort(new Order(Direction.DESC,"team_id"),
                      new Order(Direction.ASC,"name"));
  Sort sort = new Sort(new Order("team_id"),new Order("name"));
  Sort sort = new Sort(Direction.ASC,"team_id","name")';'
  ```

- Pageable

  ```java
  public List<Employee> findByBirthYearThan(int birthYear,Pageable pageable);
  ```

  ```java
  //Pageable 인터페이스를 구현한 클래스를 만들수 있음
  //아니면 Pageable을 구현하고 있는 PageRequest를 이용
  PageRequest(int page, int size)
  PageRequest(int page, int size, Sort sort)
  PageRequest(int page, int size, Direction direction, String... properties)
  ```

  - Pageable 타입 파라미터를 갖는 쿼리 메서드는 리턴타입으로 Page<T> 타입을 사용할 수 있다.

    ```java
    public Page<Employee> findByTeam(Team team, Pageable pageable);
    ```

    Page 인터페이스가 제공하는 메서드를 사용할 수 있다는 점.(페이지 크기, 페이지 번호 등)

> Pageable과 SQL 쿼리
>
> Pageable을 파라미터로 사용하면 Iterable, List, Page 등을 사용가능하게 된다. 각 타입에 따라 실행되는 쿼리가 달라진다.
>
> 예시)
>
> Pageable pageable = new PageRequest(1,4,new Sort("birthYear"));
>
> Team team = ..;
>
> List<Employee> emps = employeeRepository.findByTeamId(1L,pageable);
>
> /*
>
> select * 
>
> from employee e 
>
> left outer join Team t 
>
> on e.team_id = t.team_id 
>
> where t.team_id = ?
>
> order by e.birth_year
>
> */
>
> Page<Employee> pageEmp = employeeRepository.findByTeam(team,pageable);
>
> /*
>
> select count(e.employee_id) 
>
> from employee e
>
> left outer join team t
>
> on e.team_id = t.team_id
>
> where t.team_id = ?
>
> */
>
> 전체 개수나 전체 페이지 개수 등의 정보가 필요없다면 리턴 타입으로 Page를 사용하지 않아야 불필요하게 count 쿼리를 실행하지 않는다. 

## 저장 메서드 save()

## 삭제 메서드 delete()



# @Query를 이용한 JPQL/네이티브 쿼리 사용

## 조회

@Query 에노테이션을 이용하면 조회 메서드에서 실행할 쿼리를 직접 지정할 수 있다. @Query 에노테이션은 실행할 JPQL을 값으로 갖는다. 

```java
@Query("select * from Employee e where e.employeeNuber=?1 or e.name like %?2%")
public Employee findByEmployeeNumberOrNameLike(String empNum, String name);

@Query("select * from Employee e where e.birthYear < :year order by e.birthYear")
public List<Employee> findEmployeeBornBefore(@Param("year") int year);

@Query("select * from Employee e where e.birthYear < :year")
public List<Employee> findEmployeeBornBefore(@Param("year") int year,Sort sort);

//orderby 절 뒤에 Sort로 지정한 정렬 순서를 추가한다.
@Query("select * from Employee e where e.birthYear < :year order by e.birthYear")
public List<Employee> findEmployeeBornBefore2(@Param("year") int year,Sort sort);
```

## 수정

@Modifying 에노테이션을 추가.

```java
@Modifying
@Query("update Team t set t.name = ?1")
public int updateName(String name);
```

수정 쿼리 메서드는 쿼리 실행 결과로 수정된 행의 개수를 리턴한다.

트랜잭션이 끝나기 전 수정쿼리를 날리면 DB에는 이가 적용되지만 기존의 엔티티는 변경 전인 상태이므로, 이 엔티티를 조회해도 수정된 내용이 적용되어 있지 않다. 이 상황을 해결하는 방법으로 clearAutomatically 속성을 이용하면 쿼리를 실행 후 영속성 컨텍스트에 존재하는 엔티티를 삭제함으로 다시 DB에 요청을 하게 될 것이다.

```java
@Modifying(clearAutomatically=true)
```

## 네이티브 쿼리

네이티브 쿼리를 실행하고 싶다면 ,@Query 에노테이션의 값으로 네이티브 쿼리를 입력하고 nativeQuery 속성의 값을 true로 지정한다

```java
@Query(value="select * from Team where Name like %?1%",nativeQuery=true)
List<Team> findByNameLike(Strin name);
```

# Specification

다양한 조건을 조합해서 검색 조건을 생성해야할 때가 있다.

- from Employee e where (e.name = 검색어 or e.employeeNumber = 검색어)
- from Employee e where (e.name = 검색어 or e.employeeNumber = 검색어) and e.team.id = 팀ID

이러한 쿼리들을 일일이 만들게 된다면 복잡해질수록 메서드의 수가 증가하게 된다. 그리고 코드도 매우 복잡해질 가능성이 크다. 이러한 상황 해결하기 위해 `JPA` 에서는 `Criterial API`를 사용한다.

```java
public class JpaEmployeeListService implements EmployeeListService{
    @PersistenceUnit
    private EntityManagerFactory entityManagerFactory;
    private EmployeeRepository employeeRepository;
    
    @Transactional
    @Override
    public List<Employee> getEmployee(String keyword, Long teamId){
        CriteriaBuilder cb = entityManagerFactory.getCriteriaBuilder();
        
        CriteriaQuery<Employee> query = cb.createQuery(Employee.class);
        Root<Employee> employee = query.from(Employee.class);
        query.select(employee);
        
        if(hasValue(keyword) || hasValue(teamId)){
            if(hasValue(keyword) && !hasValue(teamId)){
                query.where(cb.or(
                    cb.equal(employee.get("name"),keyword),
                    cb.equal(employee.get("employeeNumber"),keyword)));
            }else if(!hasValue(keyword)&& hasValue(teamId)){
                query.where(cb.equal(employee.get("team"),teamId));
            }else {
                query.where(cb.and(
                	cb.or(cb.equal(employee.get("name"),keyword),
                    	cb.equal(employee.get("employeeNumber"),keyword)),
                    cb.equal(employee.get("team").get("id"),teamId)
                ));
            }
        }else {
            Calendar cal = Calendar.getInstance();
            cal.add(Cal..)
        	/****/
        }
        
        return employeeRepository.findAll(query);
    }
}
```

위와 같은 코드를 통해서 동적인 쿼리를 만들어 낼 수 있지만 아래와 같은 단점이 존재한다.

- DB에 대한 직접 접근이 필요 없음에도 `Criteria AP`I를 사용하기 위해 `EntityManagerFactory`를 참조해야한다.
- 스프링 데이터 JPA는 피라지터리 메서드의 파라미터로 `CriteriaQuery` 타입을 지원하지 않는다

두 번째 단점이 중요하다. 일단 CriteriaQuery를 파라미터로서 지원안한다. 대신 `Specification` 타입이 존재한다. 이걸 사용하면 `Criteria API` 와 같은 검색 조건 조합을 만들 수 있으면서도 `EntityManagerFactory`라던지 `CriteriaBuilde`r 등 직접 사용할 필요가 없다.



`Specification`을 이용해서 검색 조건을 지정하려면 다음과 같은 작접을 한다

- `Specification`을 입력 받도록 `Repostiroy` 인터페이스 정의
- 검색 조건을 모아 놓은 클래스 믄들기
- 검색 조건을 조합한 Specification 인스턴스를 이용해서 검색



## 리파지터리 인터페이스에 파라미터 추가

```java
public List<Employee> findAll(Specification<Employee> spec, Sort sort);
```



## Specification을 생성해주는 클래스 만들기

Specification의 정의는 아래와 같이 되어 있다.

```java
public interface Specification<T>{
    Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb);
}
```

이 Specification을 생성하는 클래스는 아래와 같이 만들 수 있다.

```java
public class EmployeeSpec{
    public static Specification<Employee> nameEq(final String name){
       	return (employee,cq,cb)->cb.equal(root.get("name"),name);
    }
    public static Specification<Employee> employeeNumberEq(final String num){
        return (employee,cq,cb)->cb.equal(root.get("employeeNumber"),num);
    }
}
```



## 검색 조건 조합해서 리파지터리 사용

검색 조건별로 Sepcification 객체를 생성해주는 클래스를 만들면 다음과 같이 Repository에 전달할 Specification객체를 생성할 수 있다.

```java
List<Employee> empList = employeeRepository.findAll(EmployeeSpec.nameEq(name));
```

검색 조건을 복합적으로 할 때에는 아래와 같이 And, or로 조합할 수 있다.

```java
Specifications<Employee> specs = Specifications.where(spec1);
Specifications<Employee> andSpecs = specs.and(spec2);
List<Employee> empList = employeeRepository.findAll(andSpecs);
```

`Specifications.where`메서드는 Sepcification을 파라미터로 전달받고 검색 조건을 조합할 수 있는 `Specifications`을 객체를 리턴한다. 

> Specifications 을 실제로 사용하려고 보니 @Deprecated 되어 있다.
>
> 이제는 Specification 클래스에 들어있는 것을 확인했다.

`and()`메서드는 검색 조건을 AND로 조합한 새로운 Specifications 객체를 리턴한다.

`or()` 메서드는 검색 조건을 OR로 조합할 때 사용



# 리파지터리 기본 제공 인터페이스

리파지터리의 인터페이스는 기본인 메서드(save, findOne, findAll)를 이미 구현한 인터페이스를 제공해주고 있다. 



## CrudRepository 인터페이스

```java
public interface CrudRepository<T, ID> extends Repository<T, ID> {
    <S extends T> S save(S var1);
    <S extends T> Iterable<S> saveAll(Iterable<S> var1);
    Optional<T> findById(ID var1);
    boolean existsById(ID var1);
    Iterable<T> findAll();
    Iterable<T> findAllById(Iterable<ID> var1);
    long count();
    void deleteById(ID var1);
    void delete(T var1);
    void deleteAll(Iterable<? extends T> var1);
    void deleteAll();
}
```

`CrudRepository`는 위와 같이 이미 메서드를 정의되어 있다.

## PagingAndSortingRepository 인터페이스

```java
public interface PagingAndSortingRepository<T, ID> extends CrudRepository<T, ID> {
    Iterable<T> findAll(Sort var1);
    Page<T> findAll(Pageable var1);
}
```

`PagingAndSortingRepository`인터페이스는 `CrudRepository`인터페이스에 위와 같은 메서드가 추가되었다.



## JpaRepository 인터페이스

```java
public interface JpaRepository<T, ID> extends PagingAndSortingRepository<T, ID>, QueryByExampleExecutor<T> {
    List<T> findAll();
    List<T> findAll(Sort var1);
    List<T> findAllById(Iterable<ID> var1);
    <S extends T> List<S> saveAll(Iterable<S> var1);
    void flush();
    <S extends T> S saveAndFlush(S var1);
    void deleteInBatch(Iterable<T> var1);
    void deleteAllInBatch();
    T getOne(ID var1);
    <S extends T> List<S> findAll(Example<S> var1);
    <S extends T> List<S> findAll(Example<S> var1, Sort var2);
}
```

리턴 타입이 `List`인 조회 메서드와 `flush()`와 같은 메서드가 추가되어있다.



## JpaSpecificationExecutor 인터페이스

방금 배운 Specification도 선언된 인터페이스가 있다.

```java
public interface JpaSpecificationExecutor<T> {
    Optional<T> findOne(@Nullable Specification<T> var1);
    List<T> findAll(@Nullable Specification<T> var1);
    Page<T> findAll(@Nullable Specification<T> var1, Pageable var2);
    List<T> findAll(@Nullable Specification<T> var1, Sort var2);
    long count(@Nullable Specification<T> var);
}
```



