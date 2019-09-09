# 뷰 리졸버

- 컨트롤러가 지정한 뷰 이름으로부터 응답 결과 화면을 생성하는 View 객체를 구할 때 사용

## ViewResolver 인터페이스

```java
public interface ViewResolver {
    View resolveViewName(String viewName, Locale locale) throws Exception;
}
```

### 스프링에서 사용되는 뷰 리졸버 구현체

- InternalResourceViewResolver : 뷰 이름으로부터 JSP 연동을 위한 View 객체를 리턴
- BeamNameViewResolver : 뷰 이름과 동일한 이름을 갖는 빈 객체를 View객체로 사용



## View 인터페이스

```java
public interface View {
    String RESPONSE_STATUS_ATTRIBUTE = 
        View.class.getName() + ".responseStatus";
    String PATH_VARIABLES = 
        View.class.getName() + ".pathVariables";
    String SELECTED_CONTENT_TYPE = 
        View.class.getName() + ".selectedContentType";

    String getContentType();

    void render(Map<String, ?> var1, HttpServletRequest var2, HttpServletResponse var3) throws Exception;
}
```

- 모든 뷰 객체는 뷰 인터페이스를 구현하고 있다.
- 뷰 객체는 응답 결과를 생성하는 역할을 한다.
- render가 실제로 응답 결과를 생성하며, var1파라미터에 실제로 모델 객체가 전달된다.



## InternalResourceViewResolver

- ViewResolver의 구현체

- InternalResourceView 타입의 뷰 객체를 리턴한다. 이 뷰는 JSP나 HTML 파일과 같이 웹 어플리케이션의 내부 자원을 이용해서 응답 결과를 생성한다. 

- JSTL이 존재할 경우 JstlView객체를 리턴.

- InternalResourceViewResolver 설정

  ```xml
  <bean id="viewResolver" class="org.springframework.web.servlet.view.InternalResourceViewResolver">
      <property name="prefix" value="/WEB-INF/view/"/>
      <property name="suffix" value=".jsp"/>
  </bean>
  ```

  컨트롤러에서 리턴한 뷰의 이름이 hello라면  InternalResourcesViewResolver가 사용하는 자원의 경로는 `/WEB-INF/view/hello.jsp`



> 위의 설정에서 뷰를 /WEB-INF/view로 지정한 것이 보인다. 이렇게 /WEB-INF의 하위 디렉토리를 만들어서 지정하는 이유는 웹 컨테이너의 특징 때문이다. 웹 컨테이너는 /WEB-INF에 사용자가 직접 접근하는 것을 허용한다. 



## BeanNameViewResolver

- 뷰 이름과 동일한 이름을 갖는 빈을 뷰로 사용

  ```xml
  <bean id="viewResolver" class="org.springframework.web.servlet.view.BeanNameViewResolver"/>
  <bean id="download" class="com.spring.mvc2.view.DownloadView"/>
  ```

  ```java
  @Controller
  public class DownloadController {
      @RequestMapping("/download.do")
      public ModelAndView download(HttpServletRequest request, 
                                  HttpServletResponse response){
          File downloadFile = new File(request);
          return new ModelAndView("download","downloadFile",downloadFile);
      }
  }
  ```

- 커스텀 View 클래스를 뷰로 사용해야 할 때 이용



## ViewResolver 여러개 설정하기

- 하나의 DispatcherServlet은 두 개 이상의 ViewResolver를 가질 있다.

- 어느 ViewResolver를 사용할지 여부는 ViewResolver 구현 클래스에 우선 순위값에 따라 다르다

  - Ordered 인터페이스, @Order 어노테이션

- 스프링이 제공하는 모든 ViewResolver는 Ordered 인터페이스를 상속받고 있기 때문에 order프로퍼티를 이용해서 우선 순위를 지정할 수 있다.

  - 지정하지 않는다면 Integer.MAX_VALUE를 우선 순위 값으로 가진다.

    ```xml
    <bean class="org.springframework.web.servlet.view.InternalResourceViewResolver">
        <property name="prefix" value="/WEB-INF/view/"/>
        <property name="suffix" value=".jsp"/>
        <property name="order" value="2"/>
    </bean>
    <bean class="org.springframework.web.servlet.view.BeanNameViewResolver">
        <property name="order" value="1"/>
    </bean>
    ```

  - 우선 순위가 높은 뷰 리졸버에게 먼저 View객체를 요청하고, null을 리턴할 시 다음 우선 순위의 뷰리졸버에게 View객체를 요청한다.

> InternalResourceViewResolver는 마지막 우선 순위를 갖도록 지정해야한다.
>
> InternalResourceViewResolver는 항상 뷰 이름에 매핑되는 View 객체를 리턴해서 null을 리턴하지 않기 때문에 InternalResourceViewResolver보다 낮은 뷰리졸버는 사용되지 않게 된다.



# HTML 특수 문자 처리 방식 설정

- JSP와 같은 템플릿 엔진을 위해 메시지나 커맨드 객체의 값을 출력할 수 있는 기능을 제공

- JSP 사용 예

  ```html
  <title><spring:message code="login.form.title"/></title>
  ```

- HTML에서 특수 문자로 여겨지는 것들은 `&lt;`과 같은 엔티티 레퍼런스로 변환해주어야 한다.

  - defaultHtmlEscape 컨텍스트 파라미터의  값을 true로 지정하면 HTML의 특수 문자를 엔티티 레퍼런스로 치환해준다.
  - \<spring:message>의 htmlEscape속성의 값을 true로 지정한다.



# JSP를 이용한 뷰 구현

스프링은 JSP에서 사용할 수 있는 커스텀 태그를 제공한다.

## 메시지 출력을 위한 \<spring:message> 커스텀 태그

### MessageSource 설정

```xml
<bean id="messageSource" class="org.springframework.context.support.ResourceBundleMessageSource">
    <property name="basenames">
        <list>
            <value>message.label</value>
            <value>message.error</value>
        </list>
    </property>
    <property name="defaultEncoding" value="UTF-8"/>
</bean>
```

### 태그 라이브러리

```jsp
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
```

### \<spring:message>  code 속성

```html
<ul>
    <li><spring:message code="login.form.help"/></li>
</ul>
```

```properties
login.form.help = 이메일/암호로 입력하세요
```

- 만약 code에 명시된 메시지가 존재하지 않으면 익셉션을 발생시키게 된다.

- text 속성을 이용한다면 메시지가 존재하지 않을 경우 해당 메시지로 대체한다.

  ```html
  <spring:message code="greeting" text="Hello world"/>
  ```

### \<spring:message> arguments 속성

```html
<title><spring:message code="greeting" arguments="${member},${greeting}"/></title>
```

```properties
greeting={0}회원님 {1}
```

### \<spring:message> var 속성과 scope 속성

- 스프링 태그가 생성한 메시지를 출력하지 않고 request나 session 같은 기본 객체의 속성에 저장할 수 도있다.

  ```html
  <spring:message code="login.form.password" var="label" scope="request"/>
  ${label}: <input type=.../>
  ```

  - var 속성은 메시지를 저장할 변수 이름
  - scope 속성은 메시지를 저장할 범위 지정



## 스프링이 제공하는 폼 관련 커스텀 태그

스프링은 커맨드 객체의 값을 입력 폼에 출력해주는 JSP 커스텀 태그를 제공

### 태그 라이브러리

```jsp
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
```



### \<form> 태그를 위한 커스텀 태그\<form:form>

- \<form:form> 태그는 \<form>태그를 생성할 때 사용된다.

  ```html
  <form:form commandName="loginCommand">
  
  </form:form>
  
  <!--위에처럼 만들면 아래와 같이 form이 생성-->
  
  <form id="loginCommand" action="현재 URL" method="post">
      
  </form>
  ```

  - method 속성을 지정하지 않으면 `post`로 설정
  - action 속성을 지정하지 않으면 현재 요청 URL로 설정
  - commnadName  속성은 폼의 값을 저장하는 커맨드 객체의 이름 지정하지 않으면 `command`


### \<form:input>,\<form:password>,\<form:hidden>

- \<form:form> 몸체에는 input, select 태그와 같이 입력 폼을 출력하는데 필요한 html 태그를 입력할 수 있다.

  ```html
  <form:form commandName="loginCommand">
  	<input type="text" name="id" value="${id}"/>
  </form:form>
  ```

  - 입력했던 커맨드 객체의 값을 다시 입력 폼에 출력해주어야 하는 경우 유용
  
- 스프링이 제공하는 커스텀 태그를 이용하면 좀 더 쉽게 값을 폼에 설정할 수 있다.

  ```html
  <form:form commandName="memberInfo">
  	<form:input path="userId"/>
      <form:password path="userPw"/>
      <form:hidden path="userHd"/>
  </form:form>
  <!--위에처럼 만들면 아래와 같이 form이 생성-->
  <form id="memberInfo" action=".." method="post">
      <!-- value에는 path 속성에서 지정한 값 -->
      <input id="userId" name="userId" type="text" value="..."/>
      <input id="userPw" name="userPw" type="password" value="..."/>
      <input id="userHd" name="userHd" type="hidden" value="..."/>
  </form>
  ```

  

## \<select> 태그를 위한 커스텀 태그 \<form:select>,\<form:options>,\<form:option>

```java
@ModelAttribute("loginTypes")
protected List<String> selectData() throws Exception{
    List<String> loginTypes = new ArryaList<>();
    loginTypes.add("일반 회원");
    loginTypes.add("기업 회원");
	return loginTypes;
}
```



### \<form:select>

```html
<form:form commandName="loginForm">
	<form:select path="loginType" items="${loginTypes}"/>
</form:form>

<!-- -->

<form>
    <select id="loginType" name="loginType">
        <option value="일반 회원">일반 회원</option>
        <option value="기업 회원">기업 회원</option>
    </select> 
</form>

```

- path 속성은 바인딩 될 커맨드 객체의 프로퍼티 이름
- items 속성은 \<option> 태그를 생성할 때 사용될 콜렉션 객체 



### \<form:options>

```html
<form:form commandName="loginForm">
	<form:select path="loginTypes">
        <option value="">---선택하세요---</option>
        <form:options items="${loginTypes}"/>
    </form:select>
</form:form>
```

- form:options 태그는 주로 콜렉션에 포함되지 않은 값을 option 태그로 함께 추가할 때 사용된다.

- option 태그를 생성하는 데 사용되는 콜렉션의 객체가 String이 아닐 때

  ```java
  @Getter
  @Setter
  class Code{
      private String code;
      private String label;
      public Code(String code,String label){
          this.code = code;
          this.label = label;
      }
  }
  
  @ModelAttribute("jobCodes")
  protected List<Code> selectData() throws Exception{
      List<Code> loginTypes = new ArryaList<>();
      loginTypes.add(new Code("일반","일반회원"));
      loginTypes.add(new Code("기업","기업회원"));
      loginTypes.add(new Code("헤드","헤드헌터회원"));
  	return loginTypes;
  }
  ```

  ```html
  <form:select path="jobCode">
  	<option value="">---선택---</option>
      <form:options items="${jobCodes}" itemLabel="label" itemValue="code"/>
  </form:select>
  
  <!-- 생성 -->
  <form>
      <select id="jobCode" name="jobCode">
          <option value="일반">일반 회원</option>
          <option value="기업">기업 회원</option>
          <option value="헤드">헤드 헌터 회원</option>
      </select> 
  </form>
  ```

  - 객체의 프로퍼티를 지정한다.

### \<form:checkbox>

```html
<form:checkbox path="alloow" label="이메일을 수신합니다."/>

<!-- 생성 allow가 true일 경우-->
<input id="allow" name="allow" type="checkbox" value="true" checked="checked"/>

<!-- 생성 allow가 false일 경우-->
<input id="allow" name="allow" type="checkbox" value="true"/>
```



### \<form:radiobuttons>, \<form:radiobutton>

```html
<form:label path="tool">주로 사용하는 개발툴</form:label>
<form:radiobuttons items="${tools}" path="tool"/>

<!-- 생성 -->

<input id="tool1" name="tool" type="radio" value="Eclipse"/>
<label for="tool1">Eclipse</label>
<input id="tool2" name="tool" type="radio" value="IntelliJ"/>
<label for="tool2">IntelliJ</label>
<input id="tool3" name="tool" type="radio" value="NetBeans"/>
<label for="tool3">NetBeans</label>
```



### \<form:textarea>

```html
<form:textarea path="etc" cols="20" rows="3"/>

<!-- -->

<textarea id="etc" name="etc" rows="3" cols="3"></textarea>
```



## \<spring:htmlEscape> 

- defaultHtmlEscape 컨테스트 파라미터를 사용하면 웹 어플리케이션 전반에 걸쳐 HTML의 특수 문자를 엔티티 레퍼런스로 치환해준다.

- Page 별로 특수 문자 치환 여부를 설정하고 싶다면 아래의 코드를 페이지의 상단에 설정 해주면된다.

  ```html
  <spring:htmlEscape defaultHtmlEscape="true"/>
  ```



## \<form:form> RESTful

- 스프링 MVC는 HTTP method의 `GET`,`POST`,`PUT`,`DELETE` 방식을 지원하고 있다.

- 웹 브라우저는 GET방식과 POST 방식만을 지원하고 있다.

- 따라서 웹 브라우저에서 PUT, DELETE 방식을 이용할 수 있도록 몇 가지 작업이 필요하다

  - web.xml 파일에 HiddenHttpMethodFilter 적용

    ```xml
    <filter>
            <filter-name>httpMethodFilter</filter-name>
            <filter-class>org.springframework.web.filter.HiddenHttpMethodFilter</filter-class>
        </filter>
        <filter-mapping>
            <filter-name>httpMethodFilter</filter-name>
            <servlet-name>dispatcher</servlet-name>
        </filter-mapping>
    ```

  - \<form:form> 의 method 속성 이용

    ```html
    <form:form method="delete">
    ...
    </form:form>
    
    <!-- 생성 -->
    <form>
    	<input type="hidden" name="_method" value="delete"/>    
        ...
    </form>
    ```

    다음과 같이 메소드가 `DELETE`, `PUT` 일 경우 hidden 타입의 input 태그가 하나 생긴다. HiddenHttpMethodFilter는 요청 파라미터의 _method 파라미터가 존재할 경우 이 값을 요청 방식으로 사용하도록 만들어준다.

    ```java
    public class HiddenHttpMethodFilter extends OncePerRequestFilter {
        private String methodParam = "_method";
    
        protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
            String paramValue = request.getParameter(this.methodParam);
            if ("POST".equals(request.getMethod()) && StringUtils.hasLength(paramValue)) {
                ....
            } else {
                filterChain.doFilter(request, response);
            }
    
        }
    }
    ```

    

    