<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="entity.UserEntity"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">

<title>Insert title here</title>

<style>
.hidden {
	visibility: hidden;
}
.visible {
	visibility: visible;
}

</style>
<%
	String proc = request.getAttribute("proc").toString();
%>
<script>

const MEMBER_REGISTRATION = "会員登録";
const MEMBER_RENEWAL = "会員更新";
const MEMBER_DELETION = "会員消去";

	function seibetsuSelect() {
		let hiddenBox = document.getElementById("hiddenBox");
		let value = document.getElementById("changeSelect").value;
		if (value == "2") {
			hiddenBox.classList.remove("hidden");
			hiddenBox.classList.add("visible");
		} else {
			hiddenBox.classList.remove("visible");
			hiddenBox.classList.add("hidden");
		}
	}

	window.onload = function() {
		var select = document.getElementById("changeSelect");
		var value = document.getElementById("sexBox").value;
		if (value == "0") {
			select.options[1].selected = true;
		} else if (value == "1") {
			select.options[2].selected = true;
		} else if (value == "2") {
			select.options[3].selected = true;
			hyoji();
		}

		let title = document.getElementById("title");
		let tabTitle =  document.getElementById("tabTitle");



		if ("<%=proc%>" == "new") {
			document.title = MEMBER_REGISTRATION;
			title.innerHTML = MEMBER_REGISTRATION;
		} else if ("<%=proc%>" == "update") {
			document.title = MEMBER_RENEWAL;
			title.innerHTML = MEMBER_RENEWAL
		}else if ("<%=proc%>" == "delete"){
			document.title = MEMBER_DELETION;
			title.innerHTML = MEMBER_DELETION

 			document.getElementById("id").readOnly = true;
 			document.getElementById("pass").readOnly = true;
 			document.getElementById("name").readOnly = true;
 			document.getElementById("age").readOnly = true;
 			document.getElementById("changeSelect").readOnly = true;
 			document.getElementById("hiddenBox").readOnly = true;
		}
	}
</script>

</head>
<body>
<h1 id="title"></h1>
<form method="post" action="./Member">
		<input type="hidden" id="sexBox" value="<c:out value="${user.seibetu}" default="" />">
		<input type="hidden" id="proc" name="proc" value="<c:out value="${proc}" default="" />">
		<input type="hidden" name="userid" value="<c:out value="${user.idUser}" default="" />">
<table>
	<tr>
		<th>ログインユーザーID</th>
		<td><input type="text" id="id" name="id" value="<c:out value="${user.idLoginUser}" default="" />"></td>
		<td><span><c:out value="${ERROR_MSG_ID}" default="" /></span></td>
	</tr>
	<tr>
		<th>パスワード</th>
		<td><input type="text" id="pass" name="pass" value="<c:out value="${user.password}" default="" />"></td>
		<td><span><c:out value="${ERROR_MSG_PASS}" default="" /></span></td>
	</tr>
	<tr>
		<th>ユーザー名</th>
		<td><input type="text" id="name" name="name" value="<c:out value="${user.meiUser}" default="" />"></td>
		<td><span><c:out value="${ERROR_MSG_NAME}" default="" /></span></td>
	</tr>
	<tr>
		<th>年齢</th>
		<td><input type="text" id="age" name="age" value="<c:out value="${user.age}" default="" />"></td>
		<td><span><c:out value="${ERROR_MSG_AGE}" default="" /></span></td>
	</tr>
	<tr>
		<th>性別</th>
		<td><select name="seibetsu" id="changeSelect" onChange="seibetsuSelect()">
				<option value=""></option>
				<option value="0">男性</option>
				<option value="1">女性</option>
				<option value="2">カスタム</option>
		</select></td>
	</tr>
	<tr>
		<td></td>
		<td><input type="text" class="hidden" id=hiddenBox name="seibetsuText"
							class="hidden" value="<c:out value="${user.custom}" default="" />"></td>
				<td><span><c:out value="${ERROR_MSG_CUSTOM}" default="" /></span></td>
	</tr>
	<tr>
		<td></td>
		<td><input type="submit" value="確認" id="toroku"></td>
	</tr>


</table>

</form>
</body>
</html>