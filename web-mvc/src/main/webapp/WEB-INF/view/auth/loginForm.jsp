<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%--
  Created by IntelliJ IDEA.
  User: KIM
  Date: 2019-09-04
  Time: 오후 10:44
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8"%>
<html>
<head>
    <title>로그인</title>
</head>
<body>
<%--스프링의 커스텀 태그, 커맨드 객체 이름 = loginCommand --%>
<form:form commandName="loginCommand">

    <%-- 커맨드 객체의 에러 코드 --%>
    <form:errors element="div"/>
    <label for="email">이메일</label>:
    <input type="text" name="email" id="email" value="${loginCommand.email}">

    <%-- email과 관련한 에러 출력 loginCommand.email--%>
    <form:errors path="email"/><br>


    <label for="password">암호</label>:
    <input type="password" name="password" id="password"/>
    <form:errors path="password"/>

    <input type="submit" value="로그인"/>
</form:form>
</body>
</html>
