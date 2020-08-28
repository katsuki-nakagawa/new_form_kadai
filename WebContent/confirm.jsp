<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="entity.UserEntity"%>
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


<%
	UserEntity user = (UserEntity) request.getAttribute("user");

	String displaySex = "";
	if(user.getSeibetu().equals("0")){
		displaySex = "男";
	}else if(user.getSeibetu().equals("1")){
		displaySex = "女";
	}else{
		displaySex = user.getCustom();
	}
%>
<body>
	<form method="post" action="./Confirm">
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
				<th><%=displaySex %></th>
			</tr>
		</table>
  				<input type="hidden" id="proc"  name="proc" value="<c:out value="${proc}" default="" />">
  				<input type="hidden" name="userId" value="<c:out value="${user.idUser}" default="" />">
  				<input type="hidden" name="id" value="<c:out value="${user.idLoginUser}" default="" />">
  				<input type="hidden" name="pass" value="<c:out value="${user.password}" default="" />">
  				<input type="hidden" name="name" value="<c:out value="${user.meiUser}" default="" />">
  				<input type="hidden" name="age" value="<c:out value="${user.age}" default="" />">
  				<input type="hidden" name="seibetsu" value="<c:out value="${user.seibetu}" default="" />">
  				<input type="hidden" name="seibetsuText" value="<c:out value="${user.custom}" default="" />">
  				<input type="submit" name="registration" value="登録">
  				<input type="submit" name="back" value="戻る">
	</form>
</body>
</html>