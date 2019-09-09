<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html>
<head>
    <link href="//netdna.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap.min.css" rel="stylesheet" id="bootstrap-css">
    <script src="//netdna.bootstrapcdn.com/bootstrap/3.2.0/js/bootstrap.min.js"></script>
    <script src="//code.jquery.com/jquery-1.11.1.min.js"></script>
    <title>회원가입 페이지</title>
</head>
<body>

<form:form commandName="registCommand" cssClass="form-horizontal" method="post" action="registMember">
    <div class="form-group">
        <label class="col-md-4 control-label" for="memberId">Member ID</label>
        <div class="col-md-1">
            <form:input path="memberId" cssClass="form-control input-md"/>
            <form:errors path="memberId"/>
        </div>
    </div>
    <div class="form-group">
        <label class="col-md-4 control-label" for="password">Password</label>
        <div class="col-md-1">
            <form:password path="password" cssClass="form-control input-md"/>
            <form:errors path="password"/>
        </div>
    </div>
    <div class="form-group">
        <label class="col-md-4 control-label" for="email">Email</label>
        <div class="col-md-3">
            <form:input path="email" cssClass="form-control input-md"/>
        </div>
    </div>
    <div class="form-group">
        <label class="col-md-4 control-label" for="etc">Etc</label>
        <div class="col-md-3">
            <form:textarea path="etc" cols="20" rows="3"/>
        </div>
    </div>
    <div class="form-group">
        <label class="col-md-4 control-label">Tools</label>
        <div class="col-md-3">
            <form:checkbox path="tools" value="eclipse" label="이클립스"/>
            <form:checkbox path="tools" value="intellij" label="인텔리J"/>
            <form:checkbox path="tools" value="netBeans" label="넷빈즈"/>
        </div>
    </div>
    <div class="form-group">
        <label class="col-md-4 control-label">Sex</label>
        <div class="col-md-3">
            <form:radiobutton path="sex" label="Male" value="male"/>
            <form:radiobutton path="sex" label="Female" value="female"/>
        </div>
    </div>
    <div>
        <label class="col-md-4 control-label">생일</label>
        <form:input path="birthday"/>
        <form:errors path="birthday"/>
    </div>
    <div class="form-group">
        <label class="col-md-4 control-label">등 록</label>
        <div class="col-md-1">
            <input type="submit" value="등록">
        </div>
    </div>
</form:form>
</body>
</html>
