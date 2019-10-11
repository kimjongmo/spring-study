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



### logout

- http 태그 안에서 설정

- 별도의 설정을 하지 않으면 /logout 경로가 로그아웃

  - /logout 경로를 요청할 때에는 POST 방식으로 하며 , csrf 설정을 disable 하지 않았다면 \<sec:csrfInput> 커스텀 태그를 이용해서 요청해야한다.

    ```jsp
    <form action="<c:url value='/logout'/>" method="post">
        <sec:csrfInput/>
        <input type="submit" value="로그아웃">
    </form>
    ```

    

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



## 스프링 시큐리티 커스텀 태그

스프링 시큐리티가 제공하는 커스텀 태그 라이브러리 설정

```jsp
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
```



### sec:authorize

현재 사용자가 특정 권한이 있으면 \<sec:authorize> 안에 내용을 보여주는 기능을 제공한다.

```jsp
<sec:authorize access="isAuthenticated()">
	<sec:authentication property="name"/>님 환영합니다.
</sec:authorize>
```



### sec:authentication

현재 접속한 사용자의 인증 정보를 나타내며 property 속성을 이용해서 인증 정보의 속성을 사용할 수 있다.

```jsp
<sec:authentication property="name"/>님 환영합니다.
```



# 스프링 시큐리티 구조

스프링 시큐리티의 동작 방식을 이해하고 그중 필요한 부분의 기능을 알맞게 변경할 수 있어야 한다. 



## SecurityContext, SecurityContextHolder, Authentication, GratedAuthority

Authentication은 시큐리티에서 현재 어플리케이션에 접근한 사용자(브라우저, REST로 접근한 외부 시스템)의 보안 관련 정보를 보관하는 역할을 한다. 사용자의 인증 여부, 권한, 이름 및 principal에 대한 정보를 제공한다. 

Authentication 객체는 SecurityContext라는 객체에 보관되어 있다. 웹 브라우저로부터 요청이 들어오면 서블릿 필터를 이용해서 SecurityContext에 Authentication객체를 설정한다. 

```java
Authentication auth = securityContext.getAuthentication();
```

SecurityContext는 다시 SecurityContextHolder라는 객체에서 가져올 수 있다.

```java
SecurityContext securityContext = SecurityContextHolder.getContext();
```



### Authentication 인터페이스

시큐리티가 아닌 우리가 만든 코드에서 직접 Authentication객체를 사용해야 할 때. SecurityContextHolder를 이용해서 Authentication를 구하면 된다. 이 객체를 구한 후에는 Authentication 객체가 제공하는 메서드를 이용해서 필요한 정보를 구한다. 

Authentication을 사용하는 목적은 주로 아래와 같을 것이다.

- AuthenticationManager에 인증을 요청할 때 필요한 정보를 담는 목적
- 현재 접속한 사용자에 대한 정보를 표현하기 위한 목적



### GrantedAuthority 인터페이스

GrantedAuthority 인터페이스는 권한을 표현할 때 사용된다. Authentication의 getAuthorities()메서드는 이 인터페이스를 이용하여 사용자가 가진 권한 목록을 리턴하는 것이다. 



## 보안 필터 체인

로그인 폼을 보여준다거나, 접근 권한이 없는 경우 403 상태 코드를 응답하거나, 로그아웃 경로로 요청이 들어올 대 로그아웃을 하는 등 보안과 관련된 작업을 처리하는 것이 보안 필터 체인이다.

web.xml 에서 설정한 DelegatingFilterProxy는 스프링 시큐리티가 생성하는 filterChainProxy에 필터 처리를 위임하는데, 이 FilterChainProxy는 다시 여러 필터를 체인 형식으로 갖고 있는 SecurityFilterChain에 처리를 위임한다.

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



SecurityFilterChain(보안 필터 체인)는 여러 필터들이 모여있으며, xml 설정의 \<sec:http>에 명시해놓은 필터들과 기본적인 필터들이 등록되게 된다. 

```xml
<sec:http use-expressions="true">
    <sec:intercept-url pattern="/admin/**" access="hasAuthority('ROLE_ADMIN')"/>
    <sec:intercept-url pattern="/manager/**" access="hasAuthority('ROLE_MANAGER')"/>
    <sec:intercept-url pattern="/member/**" access="isAuthenticated()"/>
    <sec:intercept-url pattern="/webapp/WEB-INF/view" access="permitAll()"/>
    <sec:form-login/>
    <sec:logout/>
</sec:http>
```

기본적으로 등록되는 필터들은 SecurityContextPersistenceFilter, AnonymousAuthenticationFilter, ExceptionTranslationFilter 등이다.

위의 \<sec:http> 를 통해서 등록되는 필터는 FilterSecurityInterceptor, DefaultLoginPageGeneratingfilter, UsernamePasswordAuthenticationFilter, LogoutFilter가 되겠다. 

## 보안 필터 체인에 등록될 수 있는 주요 필터

| 필터                                 | 설명                                                         |
| ------------------------------------ | ------------------------------------------------------------ |
| ChannelProcessingFilter              | https 경로로 리다이렉트시키고, 이후 필터를 진행하지 않는다.  |
| SecurityContextPersistenceFilter     | SecurityContext를 SecurityContextHolder에 저장하고, 요청 처리가 끝나면 제거 |
| CsrfFilter                           | CSRF 공격을 막기 위한 프로세스를 지닌다.                     |
| LogoutFilter                         | 지정한 경로의 요청이 오면 로그아웃을 처리                    |
| UsernamePasswordAuthenticationFilter | 지정한 경로로 POST 요청이 오면 아이디/암호 기반의 인증을 수행. |
| DefaultLoginPageGeneratingFilter     | 지정한 경로로 요청이 오면 지정한 로그인 폼을 출력            |
| RememberMeAuthenticationFilter       | 시스템 간의 연동을 위한 메시지 프레임워크를 제공             |
| AnonymousAuthenticationFilter        | 현재 사용자가 인증 전일 경우, 임의 사용자(anonymous)에 해당하는 Authentication 객체를 SecurityContext에 설정. |
| SessionManagementfilter              | 세션 타임아웃, 동시 접근 제어, 세션 고정 공격 등을 처리      |
| ExceptionTranslationFilter           | FilterSecurityInterceptor에서 발생한 익셉션을 웹에 맞는 응답으로 변환. |
| FilterSecurityInterceptor            | 현재 주체가 지정한 자원에 접근할 수 있는지 여부를 검사한다. 권한이 있으면 보안 필터를 통과시켜 자원에 접근할 수 있게 하고, 권한이 없으면 익셉션 발생. |

## 필터의 등록 순서

보안 필터 체인에 필터를 등록할 때는 순서가 중요시 된다. 예를 들어 SecurityContext와 Authentication이 올바르게 생성되어 있어야 하기 때문에 , SecurityContext를 생성해주는 SecurityContextPersistenceFilter를 다른 필터보다 먼저 적용해야 한다. 

순서는 다음에 표시한 순서여야 한다.

[https://docs.spring.io/spring-security/site/docs/4.2.14.BUILD-SNAPSHOT/reference/htmlsingle/#filter-ordering](https://docs.spring.io/spring-security/site/docs/4.2.14.BUILD-SNAPSHOT/reference/htmlsingle/#filter-ordering)



## AuthenticationManager 인증 처리

시큐리티는 인증이 필요할 때 AuthenticationManager 인터페이스를 이용한다.

AuthenticatiomManager는 authenticate(Authentication auth) 메서드를 이용해서 인증을 처리하며, 인증에 성공하면 인증 정보를 담은 Authentication 객체를 리턴하고, 그렇지 않다면 익셉션을 발생시킨다.

일반적으로 사용자 이름과 암호를 사용해서 사용자가 누구인지 인증한다. 

이 인터페이스의 구현으로 ProviderManager클래스를 제공하고 있는데, 이 클래스는 인증 처리를 AuthenticationProvider에게 위임한다.  

```xml
<sec:authentication-manager>
    <sec:authentication-provider>
        ...
    </sec:authentication-provider>
</sec:authentication-manager>
```

ProviderManager는 한 개 이상의 AuthenticationProvider를 가질 수 있으며, 다음과 같은 방식으로 동작한다.

1. 등록된 AuthenticationProvider에 대해 차례대로 다음 과정을 실행한다.
   - authenticate() 메서드를 실행해서 인증 처리를 요청
2. 등록되어 있는 모든 AuthenticationProvider들이 인증에 성공하지 못할 경우, 익셉션을 발생한다.



AuthenticationProvider을 구현하여 등록하면 시큐리티 인증 부분을 커스터마이징할 수 있다. 그리고 시큐리티는 AuthenticationProdiver의 몇 가지 기본적인 구현체를 제공하고 있다.

- DaoAuthenticationProvider : DAO를 이용해서 사용자 정보를 읽어와 인증을 처리한다.
- LdapAuthenticationProvider : LDAP 서버나 액티브 디렉토리를 이용해서 인증을 처리한다.
- OpenIDAuthenticationProvider : 오픈 ID를 이용한 인증을 처리



```xml
<sec:authentication-manager>
    <sec:authentication-provider>
        <sec:user-service>
            <sec:user name="kim" password="1234" authorities="ROLE_USER"/>
            <sec:user name="manager" password="1234" authorities="ROLE_MANAGER"/>
            <sec:user name="admin" password="1234" authorities="ROLE_ADMIN,ROLE_USER"/>         </sec:user-service>
    </sec:authentication-provider>
</sec:authentication-manager>
```

위의 경우 DaoAuthenticationProvider를 생성하게 되며, DaoAuthenticationProvider는 내부적으로 UserDetailService를 이용해서 사용자 정보를 읽어오는데, 위 설정은 메모리를 이용해서 사용자 정보를 제공하는 InMemoryUserDetailsManager를 사용하도록 설정한다.



## FilterSecurityInterceptor와 AccessDecisionManager의 인가 처리

\<sec:interceptor-url> 태그는 지정한 경로 패턴별로 접근 권한을 지정한다.

```xml
<sec:interceptor-url pattern="/admin/**" access="hasAuthority(ROLE_ADMIN)"/>
```

시큐리티는 이 설정을 이용해서 다음의 세 구성 요소를 설정한다

- FilterSecurityInterceptor
- FilterInvocationSecurityMetadataSource
- AccessDecisionManager



FilterSecurityInterceptor는 체인의 가장 마지막에 위치한다. 앞쪽에 위치한 SecurityContextPersistenceFilter 혹은 AnonymousAuthenticationFilter 등의 필터가 SecurityContextHolder에 SecurityContext를 설정하면, FilterSecurityInterceptor는 이를 이용하여 접근 권한을 검사하게 된다.



### 접근 가능 여부 확인 과정

1. 보안 필터 체인을 거쳐 Authentication 객체를 Securitycontext에 저장하게 되고, FilterSecurityInterceptor가 실행
2. FilterSecurityInterceptor는 FilterInvocationSecurityMetadataSource로부터 요청 경로(FilterInvocation)에 대한 보안 설정 정보(ConfigAttribute)를 가져온다.
3. FilterSecurityInterceptor는 AccessDecisionManager의 decide(authenticated, filterInvocation,attributes)메서드를 호출해서 Authentication이 요청 경로에 대한 보안 설정을 충족하는지 검사한다.



