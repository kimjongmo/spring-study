# XML/JSON

# XML/JSON 변환 처리

서비스나 데이터를 HTTP 기반 API 형태로 제공하는 곳이 많다. 이들 API들의 특징은 하나의 응답으로 XML이나 JSON 형식을 사용한다는 점이다. 스프링 MVC는 XML과 JSON 형식을 처리하기 위해 <mark>@RequestBody</mark> 어노테이션과 <mark>@ResponseBody</mark> 어노테이션을 제공한다.



## HTTP

브라우저와 서버 간에 데이터를 주고 받을 때 사용되는 HTTP는 요청과 응답으로 나뉘고 각각은 HTTP는 헤더와 바디로 나뉜다. [https://developer.mozilla.org/ko/docs/Web/HTTP/Headers](https://developer.mozilla.org/ko/docs/Web/HTTP/Headers) : HTTP 헤더

- @RequestBody 어노테이션 : 요청 몸체를 자바 객체로 변환할 때 사용
- @ResponseBody 어노테이션 : 자바 객체를 응답 몸체로 변환할 때 사용



## HttpMessageConverter

스프링 MVC는 @RequestBody 어노테이션이나 @ResponseBody 어노테이션이 있으면, <mark>HttpMessageConverter를 이용해서 자바 객체와 Http 요청 /응답 몸체 사이의 변환을 처리</mark>한다.

- 다양한 타입의 변환을 원한다면 HttpMessageConverter를 구현하여 사용한다.
- \<mvc:annotation-driven> or @EnableWebMvc 를 이용하면 다수의 HttpMessageConverter가 등록
  - StringHttpMessageConverter : 요청 몸체 <-> 문자열
  - Jaxb2RootElmentHttpMessageConverter : 자바 객체 <-> xml
  - MappingJackson2HttpMessageConverter : 자바 객체 <-> json
  - MappingJacksonHttpMessageConverter : 자바 객체 <-> json
  - ByteArrayHttpMessageConverter : 요청 몸체 <-> byte 배열
  - ResourceHttpMessageConverter : 요청 몸체 <-> resource
  - SourceHttpMessageConverter :요청  xml <-> xml source, 
  - AllEncompassingFormHttpMessageConverter : 폼 형식 요청 <-> MultiValueMap



## JAXB2를 이용한 XML 처리

- 자바 6 이후 버전에 기본으로 포함되어 있음
- JAXB2 API는 자바 객체와 XML 사이의 변환을 처리해주는 API
- Jaxb2RootElmentHttpMessageConverter
  - XML -> @XmlRootElement 객체 또는 @XmlType 객체로 읽기
  - @XmlRootElement 적용 객체 -> XML로 쓰기



## Jackson2를 이용한 JSON 처리

- Jackson2 의존 추가

  ```xml
  <dependency>
      <groupId>com.fasterxml.jackson.core</groupId>
      <artifactId>jackson-databind</artifactId>
      <version>2.9.9.3</version>
  </dependency>
  ```

- JAXB2 때와는 달리 어노테이션을 붙이는 등의 작업이 불 필요.



## 커스텀 HttpMessageConverter

- \<mvc:annotation-driven>을 추가하면 기본으로 등록되는 것들이 많다.

- 커스텀 HttpMessageConverter를 등록하게 되면 기본으로 등록되는 부분들이 등록되지 않는다. 따라서 아래와 같은 작업이 필요

  ```java
  @Configuration
  @EnableWebMvc
  public class Config extends WebMvcConfigurerAdapter {
      
      public void configureMessageConverter(List<HttpMessageConverter<?>> converters){
          converters.add(new CustomHttpMessageConverter());
          converters.add(new Jaxb2RootElementHttpMessageConverter());
          ...
          ...
     
      }
  }
  ```



# 파일 업로드

- 파일을 업로드해야 할 경우 HTML의 폼의 enctype 속성 값을 "multipart/form-data"로 사용한다.
- 인코딩 타입이 멀티파트를 스프링은 지원하고 있다.



## MultipartResolver 

- 멀티파트 형식으로 데이터가 전송된 경우, 해당 데이터를 변환해주는 역할
- 스프링이 제공하는 멀티파트리졸버
  - CommonsMultipartResolver :  Commons FileUpload API 이용
  - StandardServletMultipartResolver : 서블릿 3.0의 Part를 이용
- 등록시 이름을 "multipartResolver"로 지정해야함

### Commons FileUpload 설정

- 의존성 추가

  ```xml
  <dependency>
      <groupId>commons-fileupload</groupId>
      <artifactId>commons-fileupload</artifactId>
      <version>1.4</version>
  </dependency>
  ```

- 빈 등록

    ```xml
    <bean id="multipartResolver" class="org.springframework.web.multipart.commons.CommonsMultipartResolver"></bean>
    ```
    
    ```java
    @Bean(name = "multipartResolver")
    public CommonsMultipartResolver multipartResolver() {
        CommonsMultipartResolver multipartResolver =
                new CommonsMultipartResolver();
        return multipartResolver;
    }
    ```
    
    

### 서블릿 3.0의 Part 설정

- DistpatcherServlet에 Multipart 설정

  ```xml
  <servlet>
      <servlet-name>dispatcher</servlet-name>
      <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
      <init-param>
          <param-name>contextClass</param-name>
          <param-value>org.springframework.web.context.support.AnnotationConfigWebApplicationContext</param-value>
      </init-param>
      <init-param>
          <param-name>contextConfigLocation</param-name>
          <param-value>com.spring.mvc3.config.**</param-value>
      </init-param>
      <load-on-startup>1</load-on-startup>
      <multipart-config>
          <location>C:\temp</location>
          <max-file-size>-1</max-file-size>
          <max-request-size>-1</max-request-size>
          <file-size-threshold>1024</file-size-threshold>
      </multipart-config>
  </servlet>
  ```

- StandardServletMultipartResolver 빈 등록

  ```xml
  <bean id="multipartResolver" class="org.springframework.web.multipart.support.StandardServletMultipartResolver"></bean>
  ```



## 업로드한 파일 접근

스프링은 파일 데이터에 접근할 수 있는 다양한 방법을 제공하고 있다.

### MultipartFile 인터페이스

- 업로드 한 파일 정보 및 파일 데이터를 표현
- 업로드한 파일 데이터를 읽을 수 있다.

```java
public interface MultipartFile {
    String getName(); //파라미터 이름
    String getOriginalFilename();//업로드한 파일의 이름
    String getContentType();//파일 타입
    boolean isEmpty();// 파일 유무
    long getSize(); // 파일 사이즈
    byte[] getBytes() throws IOException;
    InputStream getInputStream() throws IOException;
    void transferTo(File var1) throws IOException, IllegalStateException;
}
```

```java
@RequestMapping(value = "/upload/multipartFile", method = RequestMethod.POST)
	public String uploadByMultipartFile(@RequestParam("f") MultipartFile multipartFile,...) throws IOException{
     	...   
    }
```



### MultipartHttpServletRequest



### 커맨드 객체를 통한 접근

