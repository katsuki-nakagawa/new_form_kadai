<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<%
	String res ="";
	if(request.getAttribute("fromServlet") != null){
		res=request.getAttribute("fromServlet").toString();
	}

%>
<title>ログイン画面</title>
</head>
<body>
    <p><%=res%></p>
	<form method="post" action="./Login?exec=login">

		<div>
			<div>ユーザーID：<input type="text" name="id" value="" /></div>
			<div>パスワード：<input type="password" name="pass" value="" /></div>
			<input type="submit" value="ログイン" />
		</div>
	</form>
	<p style="color: #ff0000"><c:out value="${errmsg}" default="" /><p>
</body>
</html>