<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>注册 - 备忘录系统</title>
</head>
<body>
<h2>备忘录系统 - 注册</h2>

<%
    String error = (String) request.getAttribute("error");
    if (error != null) {
%>
<p style="color: red;"><%= error %></p>
<% } %>

<form action="<%=request.getContextPath()%>/register" method="post">
    <label>用户名：
        <input type="text" name="username"/>
    </label><br/><br/>
    <label>密码：
        <input type="password" name="password"/>
    </label><br/><br/>
    <label>确认密码：
        <input type="password" name="confirmPassword"/>
    </label><br/><br/>
    <label>邮箱（可选）：
        <input type="text" name="email"/>
    </label><br/><br/>
    <label>手机号（可选）：
        <input type="text" name="phone"/>
    </label><br/><br/>
    <input type="submit" value="注册"/>
</form>

<p>已有账号？<a href="<%=request.getContextPath()%>/login">去登录</a></p>
</body>
</html>
