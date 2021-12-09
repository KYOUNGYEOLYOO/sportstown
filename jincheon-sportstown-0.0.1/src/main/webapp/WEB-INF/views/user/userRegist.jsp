<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page session="false" %>
<jsp:useBean id="now" class="java.util.Date" />

<script type="text/javascript">

$(document).ready(function(){
	
	var eventSender = new bcs_ctrl_event($("[data-ctrl-view=user_regist]"));
	
	$("[data-ctrl-view=user_regist]").dialog({
		width:500,
		modal : true,
		authOpen :true,
		resizable : false,
		buttons : {
			"저장" : function(){
				var jpPopup = $(this);
				console.log($(this).find("form").serialize());
				$.ajax({
					url : "<c:url value="/service/user/registUser"/>",
					async : false,
					dataType : "json",
					data : jpPopup.find("form").serialize(), 
					method : "post",
					beforeSend : function(xhr, settings ){},
					error : function (xhr, status, error){},
					success : function (ajaxData) {
						if(ajaxData.resultCode == "Success"){
							eventSender.send("data-event-regist", ajaxData.user);
							jpPopup.dialog("close");
						}
						else{
							new bcs_messagebox().openError("사용자 등록", "사용자 등록중 오류 발생 [code="+ajaxData.resultCode+"]", null);
						}
					}
				});
			},
			"닫기" : function(){
				$(this).dialog("close");
			}
		},
		close : function(event, ui){
			$(this).dialog("destroy");
			$("[data-ctrl-view=user_regist]").empty();
		}
	});
});

</script>

<form>
	<table class="write_type1 mgb20" summary="">
		<caption></caption>
		<colgroup>
			<col width="150">
			<col width="*">
		</colgroup>
		<tbody>
			<tr>
				<th><label for="reg_loginId">ID</label></th>
				<td><input type="text" id="reg_loginId" name="loginId" value="" title="로그인아이디" class="type_2"></td>
			</tr>
			<tr>
				<th><label for="reg_password">Password</label></th>
				<td><input type="password" id="reg_password" name="password" value="" title="Password" class="type_2"></td>
			</tr>
			<tr>p
				<th><label for="reg_userName">사용자명</label></th>
				<td><input type="text" id="reg_userName" name="userName" value="" title="사용자명" class="type_2"></td>
			</tr>
			<tr>
				<th>종목</th>
				<td>
					<select class="td sel_type_2" name="sportsEventCode" title="종목선택">
						<option value="">선택하세요</option>
						<c:forEach items="${sprotsEvents}" var="code">
							<option value="${code.codeId}">${code.name}</option>
						</c:forEach>
					</select>
				</td>
			</tr>
			<tr>
				<th><label for="reg_userType">사용자유형</label></th>
				<td>
					<select class="td sel_type_2" id="reg_userType" name="userType" title="선수명 선택">
						<option value="Athlete">선수</option>
						<option value="Coach">지도자</option>
						<option value="Admin">관리자</option>
					</select>
				</td>
			</tr>
		</tbody>
	</table>
</form>
