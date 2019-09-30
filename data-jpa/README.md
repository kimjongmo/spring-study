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

상황에 따라 다양한 조건을 조합해서 검색 조건을 생성해야 할 때 사용

