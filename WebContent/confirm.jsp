<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="entity.User"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>

<script>
 window.onload = function(){

 }
</script>
<body>
	<form method="post" action="./Conflrm">
		<table>
			<tr>
				<th>氏名</th>
				<th><c:out value="${user.idLoginUser}" /></th>
			</tr>
			<tr>
				<th>年齢</th>
				<th><c:out value="${user.age}"  /></th>
			</tr>
			<tr>
				<th>性別</th>
				<th><c:out value="${user.idLoginUser}"  /></th>
			</tr>
		</table>
	</form>
</body>
</html>