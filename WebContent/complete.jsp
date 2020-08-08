<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="entity.User"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<%
	String result = request.getAttribute("result").toString();
	String proc   = request.getAttribute("proc").toString();
%>

<script>
	window.onload = function() {
		let title = document.getElementById("title");

		if ("<%=proc%>" == "new") {
			title.innerHTML = "会員登録"
		} else if ("<%=proc%>" == "update") {
			title.innerHTML = "会員更新";
		}
	}
</script>
<title>Insert title here</title>
</head>
<body>
	<h1 id="title"></h1>
	<b><%=result%></b>
	<br>
	<button onclick="window.close()">閉じる</button>
</body>
</html>