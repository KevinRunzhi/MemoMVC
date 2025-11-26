<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>登录 - 备忘录系统</title>
</head>
<body>
<h2>备忘录系统 - 登录</h2>

<%
    String msg = (String) request.getAttribute("msg");
    String error = (String) request.getAttribute("error");
    if (msg != null) {
%>
<p style="color: green;"><%= msg %></p>
<% } %>
<% if (error != null) { %>
<p style="color: red;"><%= error %></p>
<% } %>

<form action="<%=request.getContextPath()%>/login" method="post">
    <label>用户名：
        <input type="text" name="username"/>
    </label><br/><br/>
    <label>密码：
        <input type="password" name="password"/>
    </label><br/><br/>
    <input type="submit" value="登录"/>
</form>

<p>还没有账号？<a href="<%=request.getContextPath()%>/register">去注册</a></p>
</body>
</html>
