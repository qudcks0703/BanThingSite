<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>  
<link rel="stylesheet" href="<c:url value="/resources/css/base1/index.css?val=1"/>">  
<div id="index">
	<div class="name">
		<h1 class="bts">반띵</h1>
		<img width="300" height="300" src="<c:url value="/resources/img/logo.jpg"/>">
	</div>
	<div class="catergory">
		<button onclick="window.location.href='login.1'"
		class="btn btn-primary">
			로그인
		</button>
		<button onclick="window.location.href='map-setting.1'"
		 class="btn btn-primary">
			비회원
		</button>
	</div>
</div>