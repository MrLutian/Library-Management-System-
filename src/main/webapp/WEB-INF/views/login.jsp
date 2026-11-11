<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html><html lang="zh-CN"><head><meta charset="UTF-8"><title>管理员登录</title><link rel="stylesheet" href="${pageContext.request.contextPath}/assets/style.css"></head>
<body class="login-bg"><form class="login-card" method="post" action="${pageContext.request.contextPath}/login">
<h1>高校图书管理系统</h1><p class="muted">管理员登录</p>
<% if(request.getAttribute("error")!=null){ %><p class="error"><%=request.getAttribute("error")%></p><% } %>
<label>用户名</label><input name="username" required value="admin">
<label>密码</label><input name="password" type="password" required placeholder="请输入密码">
<button type="submit">登 录</button><small>初始账号：admin　初始密码：admin123</small></form></body></html>
