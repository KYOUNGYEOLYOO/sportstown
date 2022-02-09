<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page session="false" %>
<jsp:useBean id="now" class="java.util.Date" />

<script type="text/javascript">

$(document).ready(function(){
	
	var eventSender = new bcs_ctrl_event($("[data-ctrl-view=camera_regist]"));
	
	$("[data-ctrl-view=camera_regist]").dialog({
		width:760,
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
					url : "<c:url value="/service/camera/registCamera"/>",
					async : false,
					dataType : "json",
					data : jpPopup.find("form").serialize(), 
					method : "post",
					beforeSend : function(xhr, settings ){},
					error : function (xhr, status, error){},
					success : function (ajaxData) {
						if(ajaxData.resultCode == "Success"){
							eventSender.send("data-event-regist", ajaxData.camera);
							jpPopup.dialog("close");
						}
						else{
							new bcs_messagebox().openError("카메라관리", "카메라 등록중 오류 발생 [code="+ajaxData.resultCode+"]", null);
						}
					}
				});
			}

		},
		close : function(event, ui){
			$(this).dialog("destroy");
			$("[data-ctrl-view=camera_regist]").empty();
		}	
	});
	
});

</script>

<form>
	<table summary="">
		<caption></caption>
		<colgroup>
			<col width="120">
			<col width="*">
		</colgroup>
		<tbody>
			<tr>
				<th>카메라명</th>
				<td><input type="text" name="name" value="" title="카메라명" class="inputTxt"></td>
			</tr>
			<tr>
				<th>카메라유형</th>
				<td>
					<select name="cameraType" title="카메라 유형">
						<option value="Static">고정</option>
						<option value="Shift">유동</option>
					</select>
				</td>
			</tr>
			<tr>
				<th>카메라위치</th>
				<td>
					<select name="locationCode" title="카메라 위치">
						<option value="">선택안함</option>
						<c:forEach items="${locations}" var="location">
							<option value="${location.codeId}">${location.name}</option>
						</c:forEach>
					</select>
				</td>
			</tr>
			<tr>
				<th>스포츠종목</th>
				<td>
					<select name="sportsEventCode" title="스포츠종목">
						<option value="">선택안함</option>
						<c:forEach items="${sprotsEvents}" var="sportsEvent">
							<option value="${sportsEvent.codeId}">${sportsEvent.name}</option>
						</c:forEach>
					</select>
				</td>
			</tr>
			<tr>
				<th>라이브 전용</th>
				<td>
					<input type="checkbox" name="isLiveOnly" value="true"/>
				</td>
			</tr>
		</tbody>
	</table>
	<h3>HD 정보 입력</h3>
	<input type="hidden" name="streamMetaItems[0].metaClass" value="HD" />
	<input type="hidden" name="streamMetaItems[0].camId" value="" />
	<table summary="">
		<caption></caption>
		<colgroup>
			<col width="120">
			<col width="*">
			<col width="120">
			<col width="*">
		</colgroup>
		<tbody>
			<tr>
				<th>스트리밍서버</th>
				<td colspan="3">
					<select name="streamMetaItems[0].streamServerCode" title="스트리밍서버">
						<option value="">선택안함</option>
						<c:forEach items="${streamServers}" var="streamServer">
							<option value="${streamServer.codeId}">${streamServer.name}</option>
						</c:forEach>
					</select>
				</td>
			</tr>
			<tr>
				<th>Application</th>
				<td>
					<select name="streamMetaItems[0].applicationCode" title="Application 서비스 이름">
						<option value="">선택안함</option>
						<c:forEach items="${applications}" var="application">
							<option value="${application.codeId}">${application.name}</option>
						</c:forEach>
					</select>
				</td>
				<th>스트림명</th>
				<td>
					<input type="text" name="streamMetaItems[0].streamName" value="" title="스트리밍 서비스 이름" class="inputTxt">	
				</td>
			</tr>
			<tr>
				<th>Source URL</th>
				<td colspan="3"><input type="text" name="streamMetaItems[0].streamSourceUrl" value="" title="카메라명" class="inputTxt"></td>
			</tr>
			
			<tr>
				<th>아이디</th>
				<td>
					<input type="text" name="streamMetaItems[0].streamUserId" value="" title="스트리밍 소스 아이디" class="inputTxt">
				</td>
				<th>패스워드</th>
				<td>
					<input type="text" name="streamMetaItems[0].streamUserPassword" value="" title="스트리밍 소스 패스워드" class="inputTxt">
				</td>
			</tr>
			
		</tbody>
	</table>
	
	<h3>Proxy 정보 입력</h3>
	<input type="hidden" name="streamMetaItems[1].metaClass" value="Proxy" />
	<input type="hidden" name="streamMetaItems[1].camId" value="" />
	<table summary="">
		<caption></caption>
		<colgroup>
			<col width="120">
			<col width="*">
			<col width="120">
			<col width="*">
		</colgroup>
		<tbody>
			<tr>
				<th>스트리밍서버</th>
				<td colspan="3">
					<select name="streamMetaItems[1].streamServerCode" title="스트리밍서버">
						<option value="">선택안함</option>
						<c:forEach items="${streamServers}" var="streamServer">
							<option value="${streamServer.codeId}">${streamServer.name}</option>
						</c:forEach>
					</select>
				</td>
			</tr>
			<tr>
				<th>Application</th>
				<td>
					<select name="streamMetaItems[1].applicationCode" title="Application 서비스 이름">
						<option value="">선택안함</option>
						<c:forEach items="${applications}" var="application">
							<option value="${application.codeId}">${application.name}</option>
						</c:forEach>
					</select>
				</td>
				<th>스트림명</th>
				<td>
					<input type="text" name="streamMetaItems[1].streamName" value="" title="스트리밍이름" class="inputTxt">	
				</td>
			</tr>
			<tr>
				<th>Source URL</th>
				<td colspan="3"><input type="text" name="streamMetaItems[1].streamSourceUrl" value="" title="카메라명" class="inputTxt"></td>
			</tr>
			
			<tr>
				<th>아이디</th>
				<td>
					<input type="text" name="streamMetaItems[1].streamUserId" value="" title="스트리밍 소스 아이디" class="inputTxt">
				</td>
				<th>패스워드</th>
				<td>
					<input type="text" name="streamMetaItems[1].streamUserPassword" value="" title="스트리밍 소스 패스워드" class="inputTxt">
				</td>
			</tr>
			
		</tbody>
	</table>
	
</form>
