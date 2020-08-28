<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
	<script>
		function upDelUser(kbn, id){
			window.open('./Member?proc=' + kbn + '&id=' + id);
		}
	</script>
<title>会員リスト</title>
</head>
<body>
	<table border="1">
	<tr>
		<th>NO</th>
		<th>ログインID</th>
		<th>氏名</th>
		<th>年齢</th>
		<th>性別</th>
	</tr>

	<c:forEach var="list" items="${userList}" varStatus="status">
	    <tr>
			<td>${list.getIdUser()}</td>
			<td>${list.getIdLoginUser()}</td>
			<td>${list.getMeiUser()}</td>
			<td>${list.getAge()}</td>
	    	<td>
	    	<c:choose>
	    		<c:when test="${list.getSeibetu() == 0}">男</c:when>
	    		<c:when test="${list.getSeibetu() == 1}">女</c:when>
	    		<c:otherwise>${list.getCustom()}</c:otherwise>
	    	</c:choose>
			<td>
	  		<input type="button" value="更新" onclick="upDelUser('update', ${list.getIdUser()});">
	  		<input type="button" value="削除" onclick="upDelUser('delete', ${list.getIdUser()});">
			</td>
	    </tr>
	</c:forEach>
</table>
</body>
</html>