<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="entity.UserEntity"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>会員メニュー</title>
<%
	UserEntity user = (UserEntity) request.getAttribute("user");
%>
</head>
<body>
	<p>会員メニュー</p>
	<div>
		ようこそ${sessionScope.loginUser.idLoginUser}様
	</div>
	<form method="get" name="insert" action="./Menu" target="_new">
		<div>
			<input type="hidden" name="proc" value="new">
			<input type="submit" value="会員登録">
		</div>
	</form>
	<form method="post" action="./MemberList" target="_new">
		<div>
			<input type="submit" value="会員検索">
		</div>
	</form>
	<form method="get" action="./Menu">
		<div>
			<input type="submit" name="logout" value="ログアウト">
		</div>
	</form>
</body>
</html>