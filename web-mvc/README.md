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

- 에러 코드

  - 스프링 MVC는 에러 코드로부터 메시지를 가져오는 방법을 제공
  - 검증 과정에서 추가된 에러 메시지를 사용하려면
    - MessageSource를 설정
    - MessageSource에서 메시지를 가져올 때 사용할 프로퍼티 파일 작성
    - 뷰에서 스프링이 제공하는 태그를 이용해서 에러 메시지 출력
  - 스프링 MVC는 에러 코드로부터 생성된 메시지 코드를 사용해서 에러 메시지 출력