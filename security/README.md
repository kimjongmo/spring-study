웹 어플리케이션을 개발할 때 중요한 영역 중 하나는 '보안'이다. 보안 관련 영역 중에서 개발자의 코딩과 직결된 부분은 다음과 같다.

- 인증 처리 

  현재 사용자가 누구인지 확인하는 과정으로 아이디/암호를 이용한 로그인 처리가 이에 해당된다.

- 인가 처리

  현재 사용자가 특정 대상(URL, 기능 등)을 접근할 권한이 있는 검사하는 것.

- UI 처리

  권한이 없는 사용자가 접근했을 때, 알맞은 화면은 보여주는 것.

이러한 부분들은 웹 어플리케이션마다 매우 유사한 구조를 가지게 된다. 어플리케이션마다 다른 부분만 차이나는 부분만 몇개를 명시해두면 설계, 코드 작성 등에 드는 시간을 줄일 수 있을 것이다. 이러한 목적으로 만들어진 프로젝트가 스프링 시큐리티이다. 

스프링 시큐리티는 보편적인 인증, 인가, UI 처리에 대한 기본 구현을 제공하고 있다. 일부 변경할 수 있는 확장 지점을 제공하여, 프로그래머가 코드를 만들기보다는 시큐리티가 제공하는 틀을 재사용하고 필요한 부분만 커스터마이징함으로써 보다 빠르게 인증과 인가 부분의 구현을 마무리 할 수 있다.



## 의존 설정

스프링 시큐리티와 스프링의 버전 충돌을 조심하도록 한다. 이러한 상황을 쉽게 해결 하는 방법은 \<dependencyManagement> 태그에서 버전을 맞춰주는 것이다. spring-framework-bom 모듈은 스프링 버전을 하나로 통일할 때 사용할 수 있는 모듈이다. 책에는 안나와있지만 spring-security-bom은 스프링 시큐리티의 여러 모듈의 버전을 맞춰주는 것인 듯 싶다.

```xml
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-framework-bom</artifactId>
            <version>4.0.4.RELEASE</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
        <dependency>
            <groupId>org.springframework.security</groupId>
            <artifactId>spring-security-bom</artifactId>
            <version>3.2.4.RELEASE</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>
```



## spring-security.xml

스프링 시큐리티 설정을 만들어야 한다.

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:sec="http://www.springframework.org/schema/security"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
       http://www.springframework.org/schema/beans/spring-beans.xsd
       http://www.springframework.org/schema/security
       http://www.springframework.org/schema/security/spring-security.xsd">
    <sec:http use-expressions="true">
        <sec:intercept-url pattern="/admin/**" access="hasAuthority('ROLE_ADMIN')"/>
        <sec:intercept-url pattern="/manager/**" access="hasAuthority('ROLE_MANAGER')"/>
        <sec:intercept-url pattern="/member/**" access="isAuthenticated()"/>
        <sec:intercept-url pattern="/**" access="permitAll()"/>
        <sec:form-login/>
        <sec:logout/>
    </sec:http>
</beans>
```



### Http 태그

- 스프링 시큐리티와 관련된 거의 모든 설정을 처리
- use-expression 속성을 true로 지정하면 접근 제한 URL을 표현식으로 표현할 수 있도록 도와준다.

### Intercept-url 태그

- Http 태그에서 설정

- 접근 권한을 설정할 때 사용

- pattern 속성으로 접근 경로를 Ant 패턴으로 설정한다.

  ```xml
  pattern="/member/**"
  pattern="/memeber/**"
  pattern="/**"
  ```

- access 속성으로 해당 경로에 어떤 권한이 접근 가능한지를 설정한다.

  ```xml
  access="hasRole(ROLE_ADMIN)"
  access="isAuthentication()"
  access="permitAll"
  ```

- intercept-url 태그는 순서대로 적용이 되지 않는다. 예를 들어

  ```xml
  <sec:intercept-url pattern="/member/**" access="isAuthenticated()"/>
  <sec:intercept-url pattern="/**" access="permitAll()"/>
  ```

  /member/info 라는 경로에 접근할 경우 위의 intercept-url에 먼저 적용된다.

  만약 위의 설정의 위,아래가 바뀌게 된다면 /member/info는 누구나 접근가능하게 된다.

### form-login

- http 태그 안에서 설정
- 인증된 사용자만 허용되는 자원(경로)에 접근할 때 로그인 폼을 보여준다.
- 로그인 폼에서 아이디/암호를 전송하면, 로그인(인증) 처리를 한다.



## web.xml에 스프링 시큐리티 설정

스프링 시큐리티를 위한 스프링 설정은 어플리케이션 컨텍스트에서 사용해야 한다. 나중에 JSP용 커스텀 태그 라이브러리가 정상적으로 동작하려면 스프링 시큐리티의 주요 구성 요소가 루트 어플리케이션에 위치해야 한다.

```xml
<context-param>
    <param-name>contextConfigLocation</param-name>
    <param-value>classpath:/spring-security.xml</param-value>
</context-param>
```



스프링 시큐리티 네임스페이스를 처리하는 과정에 springSecurityFilterChain이라는 스프링 빈이 등록된다. 스프링 시큐리티의 웹 모듈은 여러 서블릿 필터를 이용해서 접근 제어, 로그인/로그아웃 등의 기능을 제공하는데, 이 스프링 빈은 이들 보안 관련 서블릿 필터들을 묶어서 실행해주는 기능을 제공한다.

```xml
<filter>
    <filter-name>springSecurityFilterChain</filter-name>
    <filter-class>
        org.springframework.web.filter.DelegatingFilterProxy
    </filter-class>
</filter>
<filter-mapping>
    <filter-name>springSecurityFilterChain</filter-name>
    <url-pattern>/*</url-pattern>
</filter-mapping>
```



### FilterChainProxy

스프링 시큐리티 네임스페이스를 사용하면 내부적으로 FilterChainProxy 객체를 스프링 빈으로 등록한다. 그리고 등록된 빈의 이름이 위의 설명한 "springSecurityFilterChain"이다. 위에서 설명했듯이 여러 보안 관련 서블릿 필터를 묶어서 실행한다

\<intercept-url> 태그로 입력 받은 설정을 사용해서 FilterSecurityInterceptor 필터를 생성,\<form-login> 설정을 이용해서 폼 기반 로그인 요청을 처리하는 UsernamePasswordAuthenticationFilter를 생성,\<logout> 설정은 LogoutFilter를 생성한다.  FilterChainProxy는 클라이언트의 요청이 오면 이 체인을 이용해서 이러한 필터체인들을 이용해서 접근제어를 하게 되는 것이다.

