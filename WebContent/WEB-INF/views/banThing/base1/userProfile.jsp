<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원정보</title>
</head>
<body>
	<h4>아이디 : ${user.id}</h4>
	<h4>닉네임 : ${user.nick}</h4>
	<h4>이름 : ${user.name}</h4>
	<h4>성별: ${user.gender}</h4>
	<h4>주소 : ${user.addr}</h4>
	<h4>이메일 : ${user.email}</h4>
	
	
	<button onclick="window.close()">닫기</button>

</body>
</html>