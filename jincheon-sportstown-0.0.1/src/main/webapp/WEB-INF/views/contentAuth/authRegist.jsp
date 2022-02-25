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

	var eventSender = new bcs_ctrl_event($("[data-ctrl-view=content_auth]"));
	
	$("[data-ctrl-view=content_auth]").dialog({
		width:620,
		modal : true,
		authOpen :true,
		resizable : false,
		buttons : {
			"닫기" : function(){
				$(this).dialog("close");
			},			
			"요청" : function(){
				var jpPopup = $(this);
				console.log($(this).find("form").serialize());
				
				
				$.ajax({
					url : "<c:url value="/service/contentAuth/registContentAuth"/>",
					async : false,
					dataType : "json",
					data : jpPopup.find("form").serialize(), 
					method : "post",
					beforeSend : function(xhr, settings ){},
					error : function (xhr, status, error){},
					success : function (ajaxData) {
						if(ajaxData.resultCode == "Success"){
							//eventSender.send("data-event-regist", ajaxData.user);
							jpPopup.dialog("close");
						}
						else{
							new bcs_messagebox().openError("승인 등록", "승인 등록중 오류 발생 [code="+ajaxData.resultCode+"]", null);
						}
					}
				});
			}

		},
		close : function(event, ui){
			$(this).dialog("destroy");
			$("[data-ctrl-view=content_auth]").empty();
		}
	});
});

</script>

<form>
	<input type="hidden" name="contentId" value="${contentId}" />
	<input type="hidden" name="userId" value="${userId}" />
	<input type="hidden" name="state" value="wait" />
	<table summary="">
		<caption></caption>
		<colgroup>
			<col width="120">
			<col width="*">
		</colgroup>
		<tbody>
			<tr>
				<th>요청 기간</th>
				<td>
					<div class="datepickerBox">
						<input type="text" id="fromDate" name="fromDate" class="inputTxt date" value="<fmt:formatDate value="${user.authFromDate}" pattern="yyyy-MM-dd"/>"/>
					</div>
					&nbsp;~&nbsp;
					<div class="datepickerBox">
						<input type="text" id="toDate" name="toDate" class="inputTxt date" value="<fmt:formatDate value="${user.authToDate}" pattern="yyyy-MM-dd"/>"/>					
					</div>
				</td>
			
			</tr>
			<tr>
				<th>사유</th>
				<td><textarea name="description" title="설명" rows="5" data-ctrl-contentAuth="description"></textarea></td>	
			</tr>
			
			
		</tbody>
	</table>
</form>
