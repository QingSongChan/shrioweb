<%--
  Created by IntelliJ IDEA.
  User: 45570
  Date: 2018/12/19
  Time: 17:21
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>登录</title>
</head>
<body>
<form action="subLogin" method="post">
    用户名：<input type="text" name="username"/><br>
    密码：  <input type="text" name="password"/><br>
    <input type="checkbox" name="remeberMe"/>记住密码<br>
    <input type="submit" value="登录"/>
</form>
</body>
</html>
