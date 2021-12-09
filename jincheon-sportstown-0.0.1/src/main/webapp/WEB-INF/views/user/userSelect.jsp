<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page session="false" %>
<jsp:useBean id="now" class="java.util.Date" />

<script type="text/javascript">

$(document).ready(function(){
	
	var eventSender = new bcs_ctrl_event($("[data-ctrl-view=user_select]"));
	
	// 이미 선택된 사용자의 정보를 가져온다
	
	var frmParam = $("[data-ctrl-view=user_select]").attr("data-param-selectedUserId");
	$("#" + frmParam).find("[name=selectedUserIds]").each(function(){
		var selectedUserId = $(this).val();
		$("[data-ctrl-view=user_select] > form").find("[name=checkedUser][value="+selectedUserId+"]").prop("checked", "checked");
	});
	
	$("[data-ctrl-view=user_select]").dialog({
		width:500,
		modal : true,
		authOpen :true,
		resizable : false,
		buttons : {
			"전체선택" : function(){
				$(this).find("[name=checkedUser]").prop("checked", true);
			},
			"전체해제" : function(){
				$(this).find("[name=checkedUser]").prop("checked", false);
			},
			"선택" : function(){
				var jpPopup = $(this);
				
				var selectedUsers = new Array();
				jpPopup.find("[name=checkedUser]:checked").each(function(){
					
					selectedUsers.push({
						"userId" : $(this).attr("data-user-userId"),
						"loginId" : $(this).attr("data-user-loginId"),
						"userName" : $(this).attr("data-user-userName"),
					});
				});
				
				
				if(eventSender.send("data-event-selected", selectedUsers ) == true)
				{
					$(this).dialog("close");	
				}
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
	<table class="write_type1 mgt20" summary="">
		<colgroup>
			<col width="80px">
			<col width="150px">
			<col width="*">
		</colgroup>
		<thead>
			<th>&nbsp;</th>
			<th>사용자ID</th>
			<th>사용자명</th>
		</thead>
	</table>
	<div class="mgb20" style="overflow: hidden; overflow-y: scroll; max-height: 300px">
	<table class="write_type1" summary="">
		<caption></caption>
		<colgroup>
			<col width="80px">
			<col width="150px">
			<col width="*">
		</colgroup>
		<tbody>
			<c:forEach items="${users}" var="user">
				<tr>
					<td>
						<input type="checkbox" name="checkedUser" value="${user.userId}" 
							data-user-userId="${user.userId}" 
							data-user-loginId="${user.loginId}"
							data-user-userName="${user.userName}"/>
					</td>
					<td>${user.loginId}</td>
					<td>${user.userName}</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	</div>
</form>
