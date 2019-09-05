# 스프링 MVC

## 기본 흐름과 주요 컴포넌트

<img src="https://t1.daumcdn.net/cfile/tistory/99DECE355BE3C9751C">

## 스프링 MVC 설정 기초

1. web.xml에 DispatcherServlet 설정
2. web.xml에 캐릭터 인코딩 처리 위한 필터 설정
3. 스프링 MVC 설정
   1. HandlerMapping, HandlerAdapter
   2. ViewResolver



### DispatcherServlet

- 요청을 가장 먼저 받게 되는 곳

- 관련 컴포넌트를 이용해서 웹 브라우저의 요청을 처리한 뒤 결과를 전송

- 내부적으로 스프링 컨테이너 생성

- 별도의 파라미터를 지정하지 않으면 웹 어플리케이션 /WEB-INF/ 디렉토리에 [서블릿이름]-servlet.xml 파일을 설정 파일로 사용한다.

  ```xml
  <servlet>
  	<servlet-name>dispatcher</servlet-name>
      ...
  </servlet>
  
  ...
  
  /WEB-INF/dispatcher-servlet.xml 파일이 설정 파일이된다.
  ```

  

  - 한 개 이상의 설정 파일을 사용해야 한다면 초기화 파라미터로 설정 파일 목록을 지정

    ```xml
    <init-param>
    	<param-name>contextConfigLocation</param-name>
        <param-value>
        	/WEB-INF/main.xml
            /WEB-INF/bbs.xml
            classpath:/common.xml
        </param-value>
    </init-param>
    ```

  - XML 파일이 아닌 @Configuration 클래스를 이용해서 설정 정보를 작성할 때

    ```xml
    <init-param>
    	<param-name>contextClass</param-name>
        <param-value> org.springframework.web.context.support.AnnotationConfigWebApplicationConetxt
        </param-value>
    </init-param>
    ```

    contextClass는 DispatcherServlet이 스프링 컨테이너를 생성할 때 사용할 구현 클래스를 지정한다.

### CharacterEncodingFilter

- 웹 브라우저가 전송한 파라미터들을 처리할 캐릭터 인코딩 지정



### HandlerMapping, HandlerAdapter, ViewResolver

- \<mvc:annotation-driven>를 이용하면 아래의 두 개의 빈을 자동 등록해준다.
  - RequestMappingHandlerMapping
  - RequestMappingHandlerAdapter
  - 이 두 클래스는 @Controller 어노테이션이 적용된 클래스를 컨트롤러로 사용할 수 있도록 해준다.
- ViewResolver
  - 빈으로 등록을 할 때에는 이름이 항상 "viewResolver"로 해야한다.
- 자바 기반 설정을 하려면 @EnableWebMvc 어노테이션 사용
  - =\<mvc:annotation-driven>



### 서블릿 매핑

...

### Default Servlet Handler

- 요청된 경로에 대하여 매핑된 컨트롤러가 없을 경우 404응답 코드를 웹 브라우저에 전송하게 된다. 이 때 디폴트 서블릿을 설정하면 404응답 대신 디폴트 서블릿이 처리하게 된다.
- 각 WAS는 서블릿 매핑에 존재하지 않은 요청을 처리하기 위한 디폴트 서블릿을 제공
- 예시
  - 서블릿 매핑이 "*.do"로 되어 있다.
  - 브라우저에서 서버의 *.css 파일을 요청한다
  - 서블릿 매핑에 걸리지 않았으므로 *.css 요청은 WAS의 디폴트 서블릿에게 넘어간다





## 컨트롤러

### 컨트롤러 구현

- @Controller 어노테이션
- @RequestMapping
- 뷰 이름 리턴 or ModelAndView

### @RequestMapping

- 경로 지정

- Http method 지정

  - GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS, TRACE
  - 웹 브라우저는 get과 post 방식만을 지원하기 때문에 브라우저 위에서 실행되는 자바스크립트가 PUT이나 DELETE 방식의 요청을 보낼 수 없다.
  - 스프링의 HiddenHttpMethodFilter를 이용하면 컨트롤러 메소드가 PUT, DELETE등의 전송방식을 처리하도록 구현하면서 동시에 같은 메서드를 이용해서 ajax 요청을 처리할 수 있게 된다.

- @PathVariable

  - RESTful API의 증가와 함께 파라미터,값 형식의 요청이 줄어들고 URL자체를 이용해서 요청하는 경우가 많아지고 있다.

    ```
    http://~~/post/20
    ```

    경로 변수의 값을 파라미터로 전달받을 수 있도록 하는 것이 @PathVariable

  - 타입 변환에 실패하는 경우 400 에러 

  - 정규 표현식도 사용가능하다. (실패시 404 에러)

  - Ant 패턴

    - \* - 0개 또는 그 이상의 글자
    - ? - 1개의 글자
    - \*\* - 0개 또는 그 이상의 디렉토리 경로
    - "/faq/f?00.fq" =>  /faq/f100.fq
    - "/folders/**/files" => /folders/sub1/sub2/files

  - 요청 컨텐트 타입 제한

    - @RequestMapping( consumes="application/json" ....)
    - Content-Type이 application/json인 경우만 처리
    - @RequestMapping(produces="application/json"....)
    - 응답 결과로 JSON으로 요구하는 경우에만 처리

### HTTP 요청 파라미터와 폼 데이터 처리

요청 파라미터 값을 구하는 방법

- HttpServletRequest의 getParameter() 메서드를 이용
- @RequestParam 어노테이션을 이용해서 구하기
- 커맨드 객체를 이용하기



#### HttpServletRequest

```java
@RequestMapping()
public String list(HttpServletRequest request){
    String id = request.getParameter("id");
}
```

#### @RequestParam

```java
public String list2(@RequestParam("id") long eventId){
    
}
```

- 만약 RequestParam가 지정한 파라미터가 오지 않거나 비어있다면 400 에러 
- 이는 @RequestParam의 required 속성을 false로 지정해주면 지정한 파라미터가 오지 않거나 비어있다해서 400에러를 리턴하지 않는다.
- defaultValue 속성은 값이 설정되지 않을 때 디폴트값을 설정하는 것이다

#### 커맨드 객체를 이용한 폼 전송 처리

요청 파라미터로 객체를 지정하면 요청 파라미터를 이용하여 객체를 생성한다.

```java
public String regist(MemberRegistRequest memberRegistRequest){
    
}
```

- 객체의 set메서드를 호출해서 요청 파라미터 값을 전달

- 커맨드 객체는 뷰에 전달할 모델에 자동으로 포함된다.

  - 위의 경우 model.addAttribue("memberRegistRequest",memberRegistRequest)

  - 같은 이름의 요청 파라미터가 두 개 이상인 경우는 배열,컬렉션 이용

    ```java
    public class MemberRegistRequest{
        private int[] favoriteIds
    }
    ```

- 중첩 객체 프로퍼티

  ```java
  public class MemberRegistRequest{
      private Address address;
  }
  public class Address{
      private String address1;
      private String address2;
  }
  ```

  ```html
  <input type="text" name="address.address1"/>
  ```

- 배열/리스트 프로퍼티

  ```java
  public class AclModRequest{
      private List<AccessPerm> perms;
  	...
  }
  public class AccessPerm{
      private String id;
      private boolean canRead;
      ...
  }
  ```

  ```html
  <input type="text" name="perms[0].id"/>
  ```

  ```
  perms[0].id => aclModeRequest.perms.get(0).setId(id);
  ```



#### @ModelAttribute

- 커맨드 객체는 클래스 이름의 앞부분을 소문자로 바꾸어 모델에 자동으로 전달한다고 했다.

- 이때 이름이 너무 길다면 모델에 전달할 때 이름을 지정할 수 있다.

  ```java
  @ModelAttribute("memberInfo") MemberRegistRequest memRegReq
  ```

- @ModelAttribute 어노테이션을 이용한 공통 모델 처리

- @ModelAttribute 어노테이션을 메서드에 사용하면 이 메서드의 리턴 결과를 속성으로 추가한다.



#### @CookieValue , @RequestHeader

- @CookieValue : 쿠키값 구하기
  - value : 이름에 해당하는 쿠키를 가져온다.
  - required  : 필수 여부
  - 어노테이션을 붙인 타입에 바로 매핑됨.
    - @CookieValue Integer auth : 쿠키값이 Integer로 매핑
- @RequestHeader  : 요청 헤더값 구하기
  - value : 이름에 해당하는 헤더값을 가져온다.
  - required : 필수 여부



#### 리다이렉트

- 현재 요청에 대한 페이지가 아니라, 새로운 요청을 스스로 요청하는 것

  ```java
  @RequestMapping("/hello.do")
  public String hello(Model model) {
      model.addAttribute("greeting", "안녕하세요");
      return "redirect:/bye.do"; //스스로 bye.do라는 요청을 보낸다.
  }
  
  @RequestMapping("/bye.do")
  public String bye() {
      return "bye"; //스스로 bye.do라는 요청을 보낸다.
  }
  ```

- redirect: 뒤에 "/" 슬래시를 붙이면 어플리케이션 절대 경로 기준

- redirect: 뒤에 슬래시가 없이 시작하면 현재 경로의 상대 경로로 디라이렉트된다.

  ```java
  @RequestMapping("/welcome/hello")
  ..
      return "redirect:bye"; //  스스로 /welcome/bye를 요청
  
  @RequestMapping("/welcome/hello")
  ..
      return "redirect:http://localhost:8080/bye"; 
  //URL을 완전하게 입력하면 http://localhost:8080/bye를 요청
  ```

- redirect에 경로 변수를 사용할 수 있다.

  ```java
  @RequestMapping(value="/{postId}")
  ..
  return "redirect:/post/{postId}";
  ```

  

### 커맨드 객체 값 검증과 에러 메시지

- 사용자의 입력값을 검사하는 방법 2가지

  - 브라우저의 자바스크립트
  - 서버에서 직접 검사

- Validator

  ```java
  public interface Validator{
      boolean supports(Class<?> clazz);
      void validate(Object target, Errors errors);
  }
  ```

  - 구현1

    - Validator interface를 구현

      - ValidationUtils를 이용하면 좀 더 쉽게 검사할 수 있음.

    - 컨트롤러에 파라미터 BindingResult 또는 Errors 추가(<mark>BindingResult, Errors는 항상 파라미터 가장 뒤에 위치해야한다.</mark>)

      ```
      new CustomValidator().validate(memberRegReq, bindingResult); 
      ```

    - Errors

      ```
      객체의 속성에 관하여 문제가 있을시 rejectValue(속성,메시지)를 전달하게 된다. 속성 자체에는 문제가 없지만 아이디랑 비밀번호가 틀린것과 같은 도메인적인 문제는 reject(메시지)로 처리한다.
      ```

  - 구현2

    - @Valid 어노테이션과 @InitBinder 어노테이션 이용
      - @Valid : 커맨드 객체를 검사하는 코드를 스프링이 호출하도록 설정
        - 의존 설정이 필요.
      - @InitBinder
        - 폼과 커맨드 객체 사이의 매핑을 처리해주는 WebDataBinder를 설정할 수 있도록 만든다
        - WebDataBinder.setValidator()를 통해 Validator를 추가한다.

  - 구현3

    - 글로벌 Validator, 컨트롤러 Validator

      - 글로벌 validator

        - 한 개의 validator를 이용해서 모든 커맨드 객체를 검증

        - 적용 

          ```xml
          <mvc:annotation-driven validator="validator"/>
          <bean id="validator" class="custom.CommonValidator"/>
          ```

        - @Valid가 붙은 커맨드 객체는 기본적으로 CommonValidator로 검증하게 된다.

        - CommonValidator가 아닌 다른 Validator로 검증을 하고 싶다면 @InitBinder에 validator를 세팅하면 된다.

        - 실제로 사용하는 경우가 많이 않음

  - 구현4

    - 어노테이션 이용하기
    - Validator 클래스 작성 없이 어노테이션만으로 커맨드 객체를 검증가능
    - Hibernate Validator를 의존성에 추가
    - LocalValidatorFactoryBean를 글로벌 Validator로 등록
      - mvc:annotation-driven 태그를 설정하면 기본적으로 위의 동작을 함

- 에러 코드

  - 스프링 MVC는 에러 코드로부터 메시지를 가져오는 방법을 제공
  - 검증 과정에서 추가된 에러 메시지를 사용하려면
    - MessageSource를 설정
    - MessageSource에서 메시지를 가져올 때 사용할 프로퍼티 파일 작성
    - 뷰에서 스프링이 제공하는 태그를 이용해서 에러 메시지 출력
  - 스프링 MVC는 에러 코드로부터 생성된 메시지 코드를 사용해서 에러 메시지 출력



### 요청 파라미터의 값 변환 처리

- `PropertyEditor`와 `ConversionService`의 지식 필요
- WebDataBinder의 역할
  - 객체의 값 검증(validator)
  - <mark>요청 파라미터로부터 커맨드 객체를 생성</mark>



#### 단일 컨트롤러에 적용

- 같은 타입이여도 컨트롤러마다 다른 변환 규칙을 사용해야 할 때 WebDataBinder에 개별적으로 PropertyEditor를 등록할 수 있다.



#### @DateTimeFormat , @NumberFormat

- <mark>@DateTimeFormat</mark>
  - 특정 형식을 갖는 요청 파라미터를 Date타입이나 LocalDate타입과 같은 날짜/시간 타입으로 변환할 때 사용
  - DateTimeFormat에 작성한 패턴과 다르게 요청이 들어오면 에러코드로 typeMismatch가 추가된다.
  - 만약 파라미터에 Errors나 BindingResult 이 없다면 400에러를 발생
  - style, iso, pattern 등의 속성 중 한 개의 속성만 사용해야 한다.
- <mark>@NumberFormat</mark>
  - 특정 형식을 갖는 문자열을 숫자 타입으로 변환
  - @DateTimeFormat과 비슷



#### 글로벌 변환기 등록

- WebDataBinder, @InitBinder 를 이용하는 방법은 단일 컨트롤러에만 적용된다
- 전체 컨트롤러에 대해서 변환 방식을 등록하고 싶다면 <mark>ConversionService를 직접 생성하여 등록</mark>해야한다.

- ```xml
  <mvc:annotation-driven conversion-service="formattingConversionService"/>
  
  <bean id="formattingConversionService" class="org.~.FormattingConversionServiceFactoryBean">
  어저꾸 저쩌구
  </bean>
  ```



### HTTP세션

- <mark>트래픽이 작거나 단일 서버에서 동작하는 웹 어플리케이션의 경우 </mark>HttpSession을 이용해서 사용자 로그인 상태를 유지

  > 접속자가 늘어날수록 각 접속자에 대해 세션을 만들게 되면 세션 객체가 차지하는 메모리 비중이 높아진다. 그래서 트래픽이 많아지게 되면 세션 대신 데이터베이스나 외부의 캐시 서버에 임시 데이터를 보관하는 것을 고려한다.

- 파라미터에 HttpSession 추가
- 기존에 세션이 존재하면 그 세션을 전달하고, <mark>세션이 존재하지 않았으면 새로운 세션을 생성</mark>



#### @SessionAttributes

- 여러 화면에 걸쳐서 진행되는 작업을 처리할 때 사용

  - 예시
  - 1단계 : 이벤트 기본 정보 입력(이름, 기간 등)
  - 2단계 : 이벤트 참가 가능 대상
  - 3단계 : 내용 확인

- 위의 예시에서 내용 확인 단계에서 정보 수정을 위해 1단계나 2단계로 돌아갈 때 작성했던 데이터들이 리셋되기 때문에 아래와 같은 방법을 사용

  - 각 화면 마다 공유할 데이터를 위한 hidden 타입의 \<input> 태그 

  - 임시 데이터를 DB에 보관

  - 세션에 임시 데이터 보관

    - 방법1

      - 클래스에 @SessionAttributes("객체이름") 적용
      - 모델에 같은 이름을 갖는 객체 추가

    - 방법2

      - 클래스에 @SessionAttribute("객체이름")

      - @ModelAttribute(객체이름) 메서드를 이용해서 각 요청에 대해서 모델에 객체추가

      - ```java
        @ModelAttribute("eventForm")
        public EventForm formDate(){
            return new EventForm();
        }
        ```

      - 위 메서드가 매번 새로운 객체를 생성하느라 데이터가 유지되지 않을 것 같지만 <mark>스프링은 @ModelAttribute가 적용된 메서드를 실행하기 전에 세션에 동일 이름을 갖는 객체가 존재하면 그 객체를 모델 객체로 사용</mark>한다.

    - 객체 공유가 끝나면 파라미터에 SessionStatus를 추가하고 setComplete()메서드를 호출하여 <mark>세션에서 객체를 제거</mark>한다.

      - <mark>모델에서는 제거가 안됨</mark>



### 익셉션 처리

- 실행하는 도중에 익셉션이 발생했을 때 사용자에게 에러 화면을 노출시키지 않기 위해 스프링은 아래와 같은 지원을 한다
  - @ExceptionHandler 
  - @ControllerAdvice
  - @ResponseStatus



#### @ExceptionHandler

- 해당하는 익셉션에 대해서 처리할 메서드를 지정

 ```java
  @ExceptionHandler(ArithmeticException.class)
  public String handleException(){
      return "error/exception";
  }
 ```

- RuntimeException과 같은 익셉션을 지정하면 <mark>하위타입가지도 같이 처리</mark>할 수 있다

- ExceptionHandler를 통해 익셉션을 처리하면 <mark>응답코드가 200인 상태</mark>로 가게 된다. 만약 다른 응답 코드로 내려주고 싶다면 HttpServletResponse 파라미터를 추가하여 setStatus()를 통해 변경한다.

- 스프링 MVC는 메서드로 처리한 익셉션 객체를 JSP 뷰 코드에서 사용할 수 있도록 exception 기본 객체를 넘겨준다.

  ```jsp
  <%@ page contentType="text/html; charset=utf-8"%>
  <%@ page isErrorPage="true" %>
  ...
  ...
  작업 처리 도중 문제가 발생했습니다
  <%= exception %>
  ```

#### @ControllerAdvice

- 컨트롤러 클래스에 @ExceptionHandler를 적용하면 해당 컨트롤러에서만 적용된다. 

- 다수의 컨트롤러에서 동일 타입의 익셉션을 발생에 대하여 처리하고 싶을 때는 @ControllerAdvice 어노테이션을 이용해서 익셉션 처리를 할 수 있다.

- @ControllerAdvice 어노테이션이 적용된 클래스는 지정한 범위의 컨트롤러에서 공통으로 사용한다

  ```java
  @ControllerAdvice("com.spring.mvc.controller")
  public class CommonExceptionHandler{
      @ExceptionHandler(RuntimeException.class)
      public String handleRuntimeException(){
          return "error/runtimeError";
      }
  }
  ```

- `basePackages`,`annotations`,`assignableTypes` 속성이 있다

- @ControllerAdvice 적용 클래스를 만들었다면 스프링에 빈으로 등록

- 우선 순위
  - 해당 컨트롤러에 @ExceptionHandler
  - @ControllerAdvice



#### @ResponseStatus

- 익셉션 자체에 에러 응답 코드를 설정하고 싶을 때 사용
- @ResponseStatus를 사용하면 익셉션 처리를 따로 하지 않고 응답 코드를 변경할 수 있다.

> 서비스/도메인/영속성 영역의 익셉션 코드에는 @ResponseStatus를  사용하지 않는다.
>
> @ResponseStatus 자체가 HTTP요청/응답 영역인 UI 처리의 의미를 내포하고 있기 대문에 UI의 변경이 핵심 영역(서비스/도메인/영속성)의 코드에 영향을 줄 수 있다



#### HandlerExceptionResolver

- 컨트롤러에서 익셉션이 발생하면 HandlerExceptionResolver에 처리를 위임한다

- <mark>\<mvc:annotation-driven> 설정을 하면 내부적으로 ExceptionHandlerExceptionResolver를 등록</mark>한다. 

-  이 클래스는 @ExceptionHandler 가 붙은 메서드를 이용해서 익셉션을 처리하는 기능을 제공하고 있다.



#### MVC에 익셉션이 발생했을 때 순서

1. `ExceptionHandlerExceptionResolver` : 발생한 익셉션과 매칭되는 @ExceptionHandler 메서드를 이용해서 처리
2. `DefaultHandlerExceptionResolver` : 스프링이 발생시키는 익셉션에 대한 처리
3. `ResponseStatusExceptionResolver` : 익셉션 타입에 @ResponseStatus 어노테이션이 적용되어 있을 경우, 이 값을 이용해서 응답 코드를 전송
4. 3번까지도 익셉션을 처리하지 않으면, 컨테이너가 익셉션을 처리하도록 한다



## 스프링 MVC 설정

### WebMvcConfigurer

- WebMvcConfigurer는 <mark>MVC 네임스페이스(\<mvc:>)를 이용한 설정과 동일</mark>한 설정을 하는데 필요한 메서드를 정의
- WebMvcConfigurer는 인터페이스라서 이를 이용하게 되면 10개 넘는 메서드를 정의해야 한다.
- <mark>WebMvcConfigurerAdapter는 필요한 메서드만 구현할 수 있도록 만든 클래스</mark>



### 뷰 전용 컨트롤러

- <mark>특별한 구현없이 단순히 요청 경로에 알맞은 뷰를 리턴해주는 컨트롤러</mark>

- \<mvc:view-controller path="/index" view-name="index"/>

- ```java
  @Override
  public void addViewControllers(ViewControllerRegistry registry){
      registry.addViewController("/index").setViewName("index");
  }
  ```



### 디폴트 서블릿 설정과 동작 방식

- DispatcherServlet의 서블릿 매핑을 "/"로 설정하게 되면 모든 요청이 DispatcherServlet으로 가게 된다.
- js, css, html, jsp 등 자원 경로에 매핑된 컨트롤러가 존재하지 않으면 404 응답 에러를 발생시킨다.
- 이러한 <mark>자원들에 대한 요청은 WAS가 기본으로 제공하는 디폴트 서블릿이 처리</mark>하게 되어 있기 때문에, 이들 요청이 들어오면 디폴트 서블릿이 처리하도록 해야 한다.
- 앞서 \<mvc:default-servlet-handler/> 설정은 css,js,jsp 등에 대한 요청이 들어오면 WAS의 디폴트 서블릿에 다시 전달하는 핸들러이다.
- 디폴트 서블릿 핸들러를 등록하게 되면 요청은 다음의 과정을 거친다
  - 요청 경로와 일치하는 컨트롤러 검색
  - 컨트롤러 찾을 수 없다면 디폴트 서블릿 핸들에게 전달
  - WAS의 디폴트 서블릿에게 처리를 위임
  - 디폴트 서블릿의 결과를 응답으로 전송



### 정적 자원 설정

- <mark>css, js , 이미지 등의 자원은 거의 변하지 않기 때문에, 웹 브라우저에 캐시를 하면 네트워크 사용량, 서버 사용량, 웹 브라우저의 반응 속도 등을 개선할 수 잇다.</mark>

- 웹 서버의 캐시 옵션 설정을 통해 웹 브라우저 캐시를 활성화

- 만약 웹 서버를 별도로 두고 있지 않다면 \<mvc:resource>를 이용하여 웹 브라우저 캐시를 활성화시킬 수 있다.

  ```xml
  <mvc:resource mapping="/images/**" location="/img/, /WEB-INF/resources/" cache-period="60"/>
  ```



### HandlerInterceptor

- 요청 경로마다 접근 제어를 다르게 해야 할 때
- 특정 URL을 요청할 때마다 접근 내역을 기록하고 싶을 때
- 이러한 기능은 <mark>여러 컨트롤러에 공통으로 적용될 수 있는 기능</mark>이다.
- 각 컨트롤러에 작성하면 그만큼 중복 발생
- AOP는 너무 범용적이다
- <mark>@HandlerInterceptor를 사용하면 공통 기능을 다수의 URL에 적용</mark>할 수 있다.



#### HandlerInterceptor interface

- preHandle
  - 컨트롤러(핸들러) 실행 전
  - Object handle 파라미터는 웹 요청을 처리할 컨트롤러/핸들러 객체
  - 접근 권한 확인, 컨트롤러에서 필요로 하는 정보를 생성 등의 작업이 가능
  - 여기서<mark> false를 리턴할 경우 그 컨트롤러를 실행하지 않음</mark>
- postHandle
  - 컨트롤러(핸들러) 실행 후, 뷰 실행 전
  - 컨트롤러/핸들러가 정상적으로 실행된 이후에 추가 기능을 구현
  - <mark>컨트롤러에서 익셉션이 발생하면 이 메서드는 실행되지 않는다.</mark>
- afterCompletion
  - 뷰를 실행한 후
  - 클라이언트에게 뷰를 전송한 뒤 실행
  - <mark>컨트롤러에서 익셉션이 발생하면 이 메서드의 파라미터로 전달, 익셉션이 발생하지 않으면 해당 파라미터는 null</mark>
  - 따라서 이 값을 이용하여 익셉션 여부를 확인하고 로그를 남긴는 등의 후처리를 할 수 있다.

#### HandlerInterceptorAdapter

- HandlerInterceptor의 3가지 메서드를 굳이 구현할 필요가 없다면 이 클래스를 상속받아 작성하면 된다.



#### HandlerInterceptor 설정

- 인터셉터를 구현했다면 다음은 이를 웹 요청을 처리할 때 적용되도록 설정에 추가한는 것이다.

- XML기반

  ```xml
  <mvc:interceptors>
  	<bean id="measuringInterceptor"
            class="com.spring.mvc.interceptor.MeasuringInterceptor"/>
  </mvc:interceptors>
  ```

- 특정 경로에 대해 핸들러 인터셉터를 적용

  ```xml
  <mvc:interceptors>
  	<mvc:interceptor>
      	<mvc:mapping path="/event/**"/>
          <mvc:mapping path="/reservation/**"/>
          <bean class="com.spring.mvc.interceptor.MeasuringInterceptor"/>
      </mvc:interceptor>
  </mvc:interceptors>
  ```

- Java 설정

  ```java
  ~ extends WebMvcConfigurerAdapter{
      @Override
      public void addInterceptors(InterceptorRegistry registry){
          registry.addInterceptor(new MeasuringInterceptor());
          registry.addPathPatterns("/event/**","/reservation/**");
      }
  }
  ```

  

#### 여러 HandlerInterceptor 등록하기

- 한 요청 경로에 대해 여러 개의 인터셉터를 등록

- 적용하고 싶은 인터셉터의 <mark>순서대로 등록</mark>하면 된다.

  ```xml
  <mvc:interceptors>
  	<mvc:interceptor>
      	<mvc:mapping path="/event/**"/>
          <bean class="com.spring.mvc.interceptor.AuthInterceptor"/>
      </mvc:interceptor>
      <bean class="com.spring.mvc.interceptor.MeasuringInterceptor"/>
      <mvc:interceptor>
      	<mvc:mapping path="/event/**"/>
          <mvc:mapping path="/acl/**"/>
          <mvc:mapping path="/header/**"/>
          <bean class="com.spring.mvc.interceptor.CommonModelInterceptor"/>
      </mvc:interceptor>
  </mvc:interceptors>
  ```



### WebApplicationContext

- DispatcherServlet을 여러개 설정하는 것이 가능하다

  ```xml
  <servlet>
      <servlet-name>front</servlet-name>
      <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
      <init-param>
          <param-name>contextConfigLocation</param-name>
          <param-value>
              /WEB-INF/front.xml
          </param-value>
      </init-param>
  </servlet>
  <servlet>
      <servlet-name>rest</servlet-name>
      <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
      <init-param>
          <param-name>contextConfigLocation</param-name>
          <param-value>
              /WEB-INF/rest.xml
          </param-value>
      </init-param>
  </servlet>
  ```

- DispatcherServlet은 <mark>내부적으로 스프링 컨테이너를 포함</mark>한다고 했다.

  - 만약 DispatcherServlet을 위와 같이 생성하게 되면 각각의 설정된 빈을 서로 사용할 수 없게 된다.

  - <mark>공통 빈을 설정하기 위해서는 ContextLoaderListener를 사용</mark>한다

    - ContextLoaderListener 컨텍스트 파라미터를 명시하지 않으면 /WEB-INF/applicationContext.xml을 설정 파일로 사용한다.
    - XML 설정

    ```xml
    <contxt-param>
    	<param-name>contextConfigLocation</param-name>
        <param-value>classpath:service.xml, classpath:persistence.xml</param-value>
    </contxt-param>
    
    <listener>
    	<listener-class>
        	org.springframework.web.context.ContextLoaderListener
        </listener-class>
    </listener>
    
    <servlet>
    	<servlet-name>front</servlet-name>
        <servlet-class>
        	org.springframework.web.servlet.DispatcherServlet
        </servlet-class>
    </servlet>
    
    <servlet>
    	<servlet-name>rest</servlet-name>
        <servlet-class>
        	org.springframework.web.servlet.DispatcherServlet
        </servlet-class>
    </servlet>
    ```

  - ContextLoaderListener와 DispatcherServlet은 각각 WebApplicationContext 객체를 생성한다

  - 하지만<mark> ContextLoaderListener가 생성하는 WebApplicationContext는 루트 컨텍스트</mark>가 된다

  - <mark>Dispatcher에서 생성</mark>한 WebApplicationContext는 <mark>자식 컨텍스트</mark>가 되어 루트 컨텍스트의 빈을 사용할 수 있게 된다.

    - 자바 클래스 설정
    - contextClass 파라미터를 이용해서 WebApplicationContext의 구현체로 AnnotationConfigWebApplicationContext로 지정 

    ```xml
    <contxt-param>
    	<param-name>contextClass</param-name>
        <param-value>org.springframework.web.context.support.AnnotationConfigWebApplicationContext</param-value>
    </contxt-param>
    
    <context-param>
    	<param-name>contextConfigLocation</param-name>
        <param-value>
        	com.spring.mvc.config.ApplicationConfig
        </param-value>
    </context-param>
    
    <listener>
    	<listener-class>
        	org.springframework.web.context.ContextLoaderListener
        </listener-class>
    </listener>
    ```

    

### DelegatingFilterProxy를 이용한 서블릿 필터

서블릿 필터에서 스프링 컨테이너에 등록된 빈을 사용해야 하는 경우가 있다.

- WebApplicationContextUtils 
- 필터 자체를 컨테이너에 빈으로 등록
  - 스프링이 제공하는 DI를 통해 다른 빈을 사용하는 방법을 선호
- DelegatingFilterProxy는 서블릿 필터를 스프링 빈으로 등록할 때 사용하는 클래스



### Handler, HandlerMapping, HandlerAdapter

DispatcherServlet은 웹 요청을 처리하는 객체의 타입을 @Controller 애노테이션을 구현한 클래스로 제한하지 않는다. 웹 요청을 처리하는 객체를 범용적인 의미로 핸들러라고 부른다.