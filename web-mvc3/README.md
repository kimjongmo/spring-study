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