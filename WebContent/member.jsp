<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="entity.UserEntity"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<!-- Required meta tags -->
<meta charset="utf-8">
<meta name="viewport"
	content="width=device-width, initial-scale=1, shrink-to-fit=no">

<!-- Bootstrap CSS -->
<link rel="stylesheet"
	href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css"
	integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T"
	crossorigin="anonymous">

<!-- Optional JavaScript -->
<!-- jQuery first, then Popper.js, then Bootstrap JS -->
<script src="https://code.jquery.com/jquery-3.3.1.slim.min.js"
	integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo"
	crossorigin="anonymous"></script>
<script
	src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js"
	integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1"
	crossorigin="anonymous"></script>
<script
	src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"
	integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM"
	crossorigin="anonymous"></script>
<meta charset="UTF-8">

<title>Insert title here</title>

<style>


.characterColor {
	color: #ff0000;
}
</style>
<%
	String proc = request.getAttribute("proc").toString();
%>
<script>
	$(function() {
		console.log($("#error_mgs").val())
		//	初期表示は隠す
		$("#input_seibetus").hide();

		// 性別選択でカスタムならテキストボックスを表示する
		$("#changeSelect").on("click", function() {
			let seibetsu_val = $('#changeSelect').val();
			console.log(seibetsu_val);
			if (parseInt(seibetsu_val) === 2) {
				$("#input_seibetus").show();
			} else {
				$("#input_seibetus").hide();
			}
		});
	});

	const MEMBER_REGISTRATION = "会員登録";
	const MEMBER_RENEWAL = "会員更新";
	const MEMBER_DELETION = "会員消去";

	window.onload = function() {
		var select = document.getElementById("changeSelect");
		var value = document.getElementById("sexBox").value;
		if (value == "0") {
			select.options[1].selected = true;
		} else if (value == "1") {
			select.options[2].selected = true;
		} else if (value == "2") {
			select.options[3].selected = true;
			$("#input_seibetus").show();

		}

		let title = document.getElementById("title");
		let tabTitle = document.getElementById("tabTitle");

	}
</script>


</head>
<body>
	<h1 id="title"></h1>
	<form method="post" action="./Member">
		<input type="hidden" id="sexBox"
			value="<c:out value="${user.seibetu}" default="" />"> <input
			type="hidden" id="proc" name="proc"
			value="<c:out value="${proc}" default="" />"> <input
			type="hidden" name="userid"
			value="<c:out value="${user.idUser}" default="" />">
		<table>
			<tr>
				<th>ログインユーザーID</th>
				<td><input type="text" id="id" name="id"
					value="<c:out value="${user.idLoginUser}" default="" />"></td>
				<td><span class="characterColor"><c:out
							value="${ERROR_MSG_ID}" default="" /></span></td>
			</tr>
			<tr>
				<th>パスワード</th>
				<td><input type="text" id="pass" name="pass"
					value="<c:out value="${user.password}" default="" />"></td>
				<td><span class="characterColor"><c:out
							value="${ERROR_MSG_PASS}" default="" /></span></td>
			</tr>
			<tr>
				<th>ユーザー名</th>
				<td><input type="text" id="name" name="name"
					value="<c:out value="${user.meiUser}" default="" />"></td>
				<td><span class="characterColor"><c:out
							value="${ERROR_MSG_NAME}" default="" /></span></td>
			</tr>
			<tr>
				<th>年齢</th>
				<td><input type="text" id="age" name="age"
					value="<c:out value="${user.age}" default="" />"></td>
				<td><span class="characterColor"><c:out
							value="${ERROR_MSG_AGE}" default="" /></span></td>
			</tr>
			<tr>
				<th>性別</th>
				<td><select name="seibetsu" id="changeSelect">
						<option value="0">男性</option>
						<option value="1">女性</option>
						<option value="2">カスタム</option>
				</select></td>
			</tr>
			<tr id="input_seibetus">
				<td></td>
				<td><input type="text" class="hidden" id=hiddenBox
					name="seibetsuText" class="hidden"
					value="<c:out value="${user.custom}" default="" />"></td>
				<td id="error_mgs"><span class="characterColor"><c:out
							value="${ERROR_MSG_CUSTOM}" default="" /></span></td>
			</tr>
			<tr>
				<td></td>
				<td><input type="submit" value="確認" id="toroku"></td>
			</tr>


		</table>

	</form>
</body>
</html>