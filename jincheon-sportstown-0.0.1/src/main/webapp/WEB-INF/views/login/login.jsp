<!doctype html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page session="false" %>
<jsp:useBean id="now" class="java.util.Date" />

<html lang="ko" xml:lang="ko">
<head>

<jsp:include page="/include/head"/>


<script type="text/javascript">


function onClick_login()
{

	$.ajax({
		url : "<c:url value="/service/user/login"/>",
		async : false,
		dataType : "json",
		data : $("#frmLogin").serialize(),
		method : "post",
		beforeSend : function(xhr, settings ){},
		error : function (xhr, status, error){
		},
		success : function (ajaxData) {
		
			if(ajaxData.resultCode == "Success"){
				// TODO : main 화면으로 redirect
				
				if(ajaxData.user.userType == "Admin"){
					$(location).attr("href", "<c:url value="/index"/>");
				}else{
					$(location).attr("href", "<c:url value="/hls/manage"/>");
				}
				//
			}
			else{
				new bcs_messagebox().openError("로그인", "로그인에 실패 했습니다 [errorCode="+ajaxData.resultCode+"]", null);
			}
		}
		
	});

}

</script>
</head>

<body>

<div id="wrapper">
	
	<div class="loginWrap">
		<h1><a href="#" class="img_logo"><img src="<c:url value="/resources/images/contents/logo.png"/>" alt="대한체육회" /></a></h1>
		
		<div class="tabWrap">
			<div class="tabW">
				<ul class="tabTypeC">
					<li class="on"><a href="#"><span>로그인</span></a></li>
<!-- 					<li><a href="#" ><span>아이디찾기</span></a></li> -->
<!-- 					<li><a href="#" ><span>비밀번호 찾기</span></a></li> -->
				</ul>
			</div>
		</div>
		
		<div id="info_01">
			<div class="infoWrap ex">
				<h3>영상분석시스템 로그인</h3>
				<form id="frmLogin" onsubmit="onClick_login(); return false;">
					<fieldset>
					<legend>로그인</legend>

					<div class="login_input">
						<div class="inp">
							<label for="loginId">아이디</label>
							<input type="text" name="loginId" id="loginId" title="아이디" placeholder="아이디">
						</div>
						<div class="inp">
							<label for="password">비밀번호</label>
							<input type="password" name="password" id="password" title="비밀번호 입력" placeholder="비밀번호" >
						</div>
						<span><input type="submit" title="로그인" value="로그인" class="tit"></span>
<!-- 						<div style="padding:0 0 30px 105px;margin-bottom:30px; border-bottom:dotted 1px #ccc; "> -->
<!-- 							<input tabindex="1" type="checkbox" id="input-1"> -->
<!-- 							<label for="input-1"> 비밀번호 입력시 문자 보이기</label>													 -->
<!-- 						</div> -->
					</div>
					</fieldset>
				</form>
			</div>
			<p class="copyright">Copyrightⓒ 2016 Korean Sport & Olympic Committee. all rights reserved.</p>
		</div>
	</div>

</div>	

</body>
</html>
