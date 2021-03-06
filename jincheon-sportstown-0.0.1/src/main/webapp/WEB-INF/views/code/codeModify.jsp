<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page session="false" %>
<jsp:useBean id="now" class="java.util.Date" />

<script type="text/javascript">

$(document).ready(function(){
	
	var eventSender = new bcs_ctrl_event($("[data-ctrl-view=code_modify]"));
	
	eventSender.sender.find("[name=isUsed]").val("${code.isUsed}").prop("checked", true);
	eventSender.sender.find("[name=isPartition]").val("${code.isPartition}").prop("checked", true);
	
	
	
	$("[data-ctrl-view=code_modify]").dialog({
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
					url : "<c:url value="/service/code/modifyCode"/>",
					async : false,
					dataType : "json",
					data : jpPopup.find("form").serialize(), 
					method : "post",
					beforeSend : function(xhr, settings ){},
					error : function (xhr, status, error){},
					success : function (ajaxData) {
						if(ajaxData.resultCode == "Success"){
							eventSender.send("data-event-modify", ajaxData.code);
							jpPopup.dialog("close");
						}
						else{
							new bcs_messagebox().openError("코드관리", "코드 수정중 오류 발생 [code="+ajaxData.resultCode+"]", null);
						}
					}
				});
			}

		},
		close : function(event, ui){
			$(this).dialog("destroy");
			$("[data-ctrl-view=code_modify]").empty();
		}
	});
});

</script>

<form>
	<input type="hidden" name="codeId" value="${code.codeId}"/>
	<table summary="">
		<caption></caption>
		<colgroup>
			<col width="120">
			<col width="*">
		</colgroup>
		<tbody>
			<tr>
				<th>코드명</th>
				<td><input type="text" name="name" value="${code.name}" title="코드명" class="inputTxt"></td>
			</tr>
			<tr>
				<th>사용여부</th>
				<td>
					<select name="isUsed" title="사용 여부">
						<option value="true">사용</option>
						<option value="false">사용안함</option>
					</select>
				</td>
			</tr>
			<c:if test="${code.groupCode == 'SPORTS_EVENT'}">
			<tr>
				<th>파티션여부</th>
				<td>
					<select name="isPartition" title="파티션여부">
						<option value="true">사용</option>
						<option value="false">사용안함</option>
					</select>
				</td>
			</tr>
			</c:if>
		</tbody>
	</table>
</form>
