<%@ page contentType="text/html;charset=UTF-8" %><%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html><html lang="zh-CN"><head><meta charset="UTF-8"><title>系统首页</title><link rel="stylesheet" href="${pageContext.request.contextPath}/assets/style.css"></head><body>
<header><b>高校图书管理系统</b><span>欢迎，${sessionScope.realName}　<a href="${pageContext.request.contextPath}/logout">退出</a></span></header>
<nav><a class="active" href="${pageContext.request.contextPath}/dashboard">首页</a><a href="${pageContext.request.contextPath}/books">图书管理</a><a href="${pageContext.request.contextPath}/readers">读者管理</a><a href="${pageContext.request.contextPath}/borrows">借阅管理</a></nav>
<main><h2>系统概览</h2><div class="cards"><div class="stat"><span>图书种类/记录数</span><strong>${bookCount}</strong></div><div class="stat"><span>读者人数</span><strong>${readerCount}</strong></div><div class="stat"><span>当前借出</span><strong>${borrowCount}</strong></div></div>
<section class="panel"><h3>使用说明</h3><p>请通过顶部导航进入相应模块。首次使用建议先维护图书和读者信息，再进行借阅登记。</p></section></main></body></html>
