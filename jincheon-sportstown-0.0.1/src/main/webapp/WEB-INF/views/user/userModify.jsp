<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page session="false" %>
<jsp:useBean id="now" class="java.util.Date" />

<script type="text/javascript">

$(document).ready(function(){
	
	var eventSender = new bcs_ctrl_event($("[data-ctrl-view=user_modify]"));
	
	eventSender.sender.find("[name=userType]").val("${user.userType}").prop("selected", true);
	eventSender.sender.find("[name=isUsed]").val("${user.isUsed}").prop("selected", true);
	
	
	$("[data-ctrl-view=user_modify]").dialog({
		width:500,
		modal : true,
		authOpen :true,
		resizable : false,
		buttons : {
			"저장" : function(){
				var jpPopup = $(this);
				console.log($(this).find("form").serialize());
				$.ajax({
					url : "<c:url value="/service/user/modifyUser"/>",
					async : false,
					dataType : "json",
					data : jpPopup.find("form").serialize(), 
					method : "post",
					beforeSend : function(xhr, settings ){},
					error : function (xhr, status, error){},
					success : function (ajaxData) {
						if(ajaxData.resultCode == "Success"){
							eventSender.send("data-event-modify", ajaxData.user);
							jpPopup.dialog("close");
						}
						else{
							new bcs_messagebox().openError("사용자 수정", "사용자 수정중 오류 발생 [code="+ajaxData.resultCode+"]", null);
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
			$("[data-ctrl-view=user_modify]").empty();
		}
	});
});

</script>

<form>
	<input type="hidden" name="userId" value="${user.userId}" />
	<table class="write_type1 mgb20" summary="">
		<caption></caption>
		<colgroup>
			<col width="150">
			<col width="*">
		</colgroup>
		<tbody>
			<tr>
				<th>ID</th>
				<td><input type="text" name="loginId" value="${user.loginId}" title="로그인아이디" class="type_2" readonly></td>
			</tr>
			<tr>
				<th>Password</th>
				<td><input type="text" name="newPassword" value="" title="로그인 패스워드" class="type_2"></td>
			</tr>
			<tr>
				<th>사용자명</th>
				<td><input type="text" name="userName" value="${user.userName}" title="사용자명" class="type_2"></td>
			</tr>
			<tr>
				<th>종목</th>
				<td>
					<select class="td sel_type_2" name="sportsEventCode" title="종목선택">
						<option value="">선택하세요</option>
						<c:forEach items="${sprotsEvents}" var="code">
							<c:choose>
								<c:when test="${user.sportsEventCode == code.codeId}">
									<option value="${code.codeId}" selected>${code.name}</option>
								</c:when>
								<c:otherwise>
									<option value="${code.codeId}">${code.name}</option>
								</c:otherwise>
							</c:choose>
							
						</c:forEach>
					</select>
				</td>
			</tr>
			<tr>
				<th>사용자유형</th>
				<td>
					<select class="td sel_type_2" name="userType" title="선수명 선택">
						<option value="Athlete">선수</option>
						<option value="Coach">지도자</option>
						<option value="Admin">관리자</option>
					</select>
				</td>
			</tr>
			<tr>
				<th>사용여부</th>
				<td>
					<select class="td sel_type_2" name="isUsed" title="사용 여부">
						<option value="true">사용</option>
						<option value="false">사용암함</option>
					</select>
				</td>
			</tr>
		</tbody>
	</table>
</form>
