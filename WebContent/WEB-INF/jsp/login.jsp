<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet"type="text/css"href="./css/login.css" media="all">
<link rel="stylesheet"type="text/css"href="./css/all.css" media="all">
<link rel="stylesheet" type="text/css" href="./css/pyonpyon.css" media="all">
<title>ログイン</title>
</head>
<body id="bglogincolor">
	<h1 id ="changeTitleColor">チームAのチャット</h1>
	<div align="center" class="font">
	<h2 class="item_text">ログイン</h2>
	<form action="/chat/login" method="POST">
		<p>会員ID</p>
		<input type="text" name="userId" value="${loginBean.userId}">
		<p>パスワード</p>
		<input type="password" name="password" value="${loginBean.password}">
		<br>
		<P id="changeErrorColor">${loginBean.errorMessage}</P>
		<input type="submit" value="ログイン">
	</form>
	<form action="/chat/newInsert" method="GET">
		<input type="submit" value="新規登録">
	</form>
	</div>
</body>
</html>