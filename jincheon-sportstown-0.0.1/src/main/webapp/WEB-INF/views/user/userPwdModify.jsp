<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page session="false" %>
<jsp:useBean id="now" class="java.util.Date" />
<jsp:include page="/include/head"/>


<script type="text/javascript">

function passwordView(){
	
// 	alert($('input:checkbox[id="pwview"]').is(":checked"));
	if($('input:checkbox[id="pwview"]').is(":checked")== true){
		$('#newPassword').attr('type',"text");
		$('#pw_change_confirm').attr('type',"text");
	}else{
		$('#newPassword').attr('type',"password");
		$('#pw_change_confirm').attr('type',"password");
	}
}


function popupClose(){
	self.close();
}

function change_password(){
	if($('#newPassword').val() == ""){
		alert("비밀번호를 입력하세요");
		
		return false;
	}
	if($('#newPassword').val() != $('#pw_change_confirm').val()){
		alert("비밀번호가 일치하지 않습니다.");
		
		return false;
	}
	
	console.log(">>>>>>>>>");
	console.log($('#userId').val());
	
	$.ajax({
		url : "<c:url value="/service/user/modifyUserPassword"/>/"+$('#userId').val()+"/"+$('#newPassword').val(),
		async : false,
		dataType : "json",
		data : "", 
		method : "post",
		beforeSend : function(xhr, settings ){},
		error : function (xhr, status, error){},
		success : function (ajaxData) {
			if(ajaxData.resultCode == "Success"){
				opener.parent.onLogout();
				self.close();
				
			}
			else{
				new bcs_messagebox().openError("사용자 수정", "사용자 수정중 오류 발생 [code="+ajaxData.resultCode+"]", null);
			}
		}
	});
	
}

function onLogout()
{
	$.ajax({
		url : "<c:url value="/service/user/logout"/>",
		async : false,
		dataType : "json",
		data : null,
		method : "post",
		beforeSend : function(xhr, settings ){},
		error : function (xhr, status, error){},
		success : function (ajaxData) {
			if(ajaxData.resultCode == "Success"){
				// TODO : main 화면으로 redirect
				$(location).attr("href", "<c:url value="/login"/>");
			}
			else{
				new bcs_messagebox().openError("로그아웃", "로그아웃에 실패 했습니다 [errorCode="+ajaxData.resultCode+"]", null);
			}
		}
		
	});
}
</script>
<div class="popupContainer">
<form id="userForm">
	<input type="hidden" id="userId" name="userId" value="${loginUser.userId}" />
	<input type="hidden" id="loginId" name="loginId" value="${loginUser.loginId}" />
	
	
	<header>비밀번호 변경</header>
	<table summary="">
		<caption></caption>
		<colgroup>
			<col width="160">
			<col width="*">
		</colgroup>
		<tbody>
			<tr>
				<th><label for="id">이름</label></th>
				<td><input type="text" title="이름" id="userName" class="inputTxt" value="${loginUser.userName }" readonly/></td>
			</tr>
<!-- 			<tr> -->
<!-- 				<th><label for="pw">비밀번호</label></th> -->
<!-- 				<td><input type="password" title="비밀번호" id="pw" class="inputTxt" placeholder="비밀번호" /></td> -->
<!-- 			</tr> -->
			<tr>
				<th><label for="pw_change">변경 비밀번호</label></th>
				<td><input type="password" title="변경 비밀번호" name="newPassword" id="newPassword" class="inputTxt" placeholder="변경 비밀번호" /></td>
			</tr>
			<tr>
				<th><label for="pw_change_confirm">변경 비밀번호 확인</label></th>
				<td><input type="password" title="변경 비밀번호 확인" id="pw_change_confirm" class="inputTxt" placeholder="변경 비밀번호 확인" /></td>
			</tr>
		</tbody>
	</table>
	<p>
		<input type="checkbox" id="pwview" onClick="javascript:passwordView();"/><label for="pwview">비밀번호 문자보이기</label>
	</p>
	<div class="infoContainer">
		<p style="font-size:13px;color:#ff0000;">비밀번호 변경 후 자동 로그아웃 됩니다. 다시 로그인 해주세요.</p>
<!-- 		<ul> -->
<!-- 			<li>연속된 문자 또는 숫자 사용 불가</li> -->
<!-- 			<li>영문 대문자, 영문 소문자, 숫자, 특수문자 중 3개 이상</li> -->
<!-- 			<li>8자리 ~ 20자리 이내 혼합</li> -->
<!-- 		</ul> -->
	</div>
	<div class="btnWrap">
		<a class="btn reset" onClick="javascript:popupClose();">닫기</a>
		<a class="btn save" onClick="javascript:change_password();">저장</a>
	</div>
</form>
</div>