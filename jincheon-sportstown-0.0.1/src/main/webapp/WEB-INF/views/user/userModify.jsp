<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page session="false" %>
<jsp:useBean id="now" class="java.util.Date" />

<script type="text/javascript">

$(document).ready(function(){
	
	$( ".date" ).datepicker({
		showOn: "button",
		buttonImage: "<c:url value="/resources/images/contents/icons_calendar.png"/>",
		buttonImageOnly: true,
		dateFormat: 'yy-mm-dd',
		buttonText: "Select date"
	});
	
	var eventSender = new bcs_ctrl_event($("[data-ctrl-view=user_modify]"));
	
	eventSender.sender.find("[name=userType]").val("${user.userType}").prop("selected", true);
	eventSender.sender.find("[name=isUsed]").val("${user.isUsed}").prop("selected", true);
	
	
	$("[data-ctrl-view=user_modify]").dialog({
		width:620,
		modal : true,
		authOpen :true,
		resizable : false,
		buttons : {
			
			"닫기" : function(){
				$(this).dialog("close");
			},			
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
	<c:set var="partitionValue" value=""/>
	<input type="hidden" name="userId" value="${user.userId}" />
	<table summary="">
		<caption></caption>
		<colgroup>
			<col width="120">
			<col width="*">
		</colgroup>
		<tbody>
			<tr>
				<th>ID</th>
				<td><input type="text" name="loginId" value="${user.loginId}" title="로그인아이디" class="inputTxt" readonly></td>
			</tr>
			<tr>
				<th>Password</th>
				<td><input type="text" name="newPassword" value="" title="로그인 패스워드" class="inputTxt"></td>
			</tr>
			<tr>
				<th>사용자명</th>
				<td><input type="text" name="userName" value="${user.userName}" title="사용자명" class="inputTxt"></td>
			</tr>
			<tr>
				<th>종목</th>
				<td>
					<select name="sportsEventCode" title="종목선택">
						<option value="">선택하세요</option>
						<c:forEach items="${sprotsEvents}" var="code">
							<c:choose>
								<c:when test="${user.sportsEventCode == code.codeId}">
									<option value="${code.codeId}" selected>${code.name}</option>
									<c:if test="${code.isPartition == true}">
										<c:set var="partitionValue" value="Y"/>
									</c:if>
								</c:when>
								<c:otherwise>
									<option value="${code.codeId}">${code.name}</option>
								</c:otherwise>
							</c:choose>
							
						</c:forEach>
					</select>
				</td>
			</tr>
			<c:if test="${partitionValue =='Y'}">
			<tr>
				<th>영상 기간</th>
				<td>
					<div class="datepickerBox">
						<input type="text" id="authFromDate" name="authFromDate" class="inputTxt date" value="<fmt:formatDate value="${user.authFromDate}" pattern="yyyy-MM-dd"/>"/>
					</div>
					&nbsp;~&nbsp;
					<div class="datepickerBox">
						<input type="text" id="authToDate" name="authToDate" class="inputTxt date" value="<fmt:formatDate value="${user.authToDate}" pattern="yyyy-MM-dd"/>"/>					
					</div>
				</td>
			</tr>
			</c:if>
			<tr>
				<th>사용자유형</th>
				<td>
					<select name="userType" title="선수명 선택">
						<option value="Athlete">선수</option>
						<option value="Coach">지도자</option>
						<option value="Admin">관리자</option>
					</select>
				</td>
			</tr>
			<tr>
				<th>사용여부</th>
				<td>
					<select name="isUsed" title="사용 여부">
						<option value="true">사용</option>
						<option value="false">사용암함</option>
					</select>
				</td>
			</tr>
		</tbody>
	</table>
</form>
