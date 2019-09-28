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

