<!doctype html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page session="false" %>
<jsp:useBean id="now" class="java.util.Date" />
<jsp:useBean id="ipFilter" class="com.bluecapsystem.cms.jincheon.sportstown.common.define.IPFilterConstant" />

<html lang="ko" xml:lang="ko">
<head>


<jsp:include page="/include/head"/>

<script type="text/javascript" src="<c:url value="/bluecap/jwplayer-7.11.3/jwplayer.js"/>"></script>
<script>jwplayer.key="/cDB/4s47uzPnRb3q/gtzOze04a7CABARZuQWIwYvfQ=";</script>

<%-- <script type="text/javascript" src="<c:url value="/bluecap/jwplayer/jwplayer.js"/>"></script> --%>
<!-- <script>jwplayer.key="uL+sf8LV4JfO0X1+U8YPbC7PoiiNX730vh3pnQ==";</script> -->

<style type="text/css">

</style>

<script type="text/javascript">

$(document).ready(function(){
	
	initPalyer();
	
	
	$("#frmContent").find("[data-ctrl-contentMeta=contentUserNames]").click(function(){
		$("#frmSelectedUsers").empty();
		$("#frmContent").find("[data-ctrl-contentMeta=contentUserId]").each(function(){
			console.log($(this).val());
			$("#frmSelectedUsers").append($("<input type='hidden' name='selectedUserIds' />").val($(this).val()));
		});
		
		var sportsEentCode = $("#frmContent").find("[name=sportsEventCode]").val();
		$("#frmUserSearch").find("[name=sportsEventCode]").val(sportsEentCode);
		var param = $("#frmUserSearch").serialize();
		
		console.log(param);
		$("[data-ctrl-view=user_select]").empty();
		$("[data-ctrl-view=user_select]").jqUtils_bcs_loadHTML(
				"<c:url value="/user/select"/>?" + param,
				false, "get", null, null
			);
		
	});
});


function callback_selectedUsers(sender, users)
{
	console.log(users);
	
	$("#frmContent").find("[data-ctrl-contentMeta=contentUserId]").remove();
	var userNames = "";
	
	for(var i = 0; i < users.length; i++)
	{
		$("#frmContent").append(
			$("<input type='hidden' name='contentUsers["+i+"].userId' data-ctrl-contentMeta='contentUserId' />").val(users[i].userId)
		);
		
		userNames += users[i].userName + ",";
	}
	
	$("#frmContent").find("[data-ctrl-contentMeta=contentUserNames]").val(userNames);
	
	return true;
}

</script>


<c:set var="streamUrl" value=""/>
<c:set var="streamFile" value=""/>

<c:forEach items="${contentMeta.content.instances}" var="instance" end="0">
	<c:set var="streamUrl" value="${vodStreamer}/${contentRootUri}"/>
	<c:set var="streamFile" value="${instance.file.fileName}"/>
</c:forEach>

<script type="text/javascript">


var videoTag;
var currentRate = 1;

function initPalyer()
{
	console.log("login location ==> ${loginUser.connectLocation}");
//	var  mediaUrl = "${vodStreamer}${contentRootUri}${contentMeta.content.instances[0].file.filePath}${contentMeta.content.instances[0].file.fileName}/playlist.m3u8"
	var  mediaUrl = "${ipFilter.filterAddress(loginUser.connectLocation, vodStreamer)}${contentRootUri}${contentMeta.content.instances[0].file.filePath}${contentMeta.content.instances[0].file.fileName}/playlist.m3u8"
	// mediaUrl = "<c:url value="/resources/mp4/sample.mp4"/>";
	// mediaUrl = "http://223.26.218.116:1935/vod/_definst_/mp4:./test/bcs.mp4/playlist.m3u8";
	
	console.log(mediaUrl);
	
	jwplayer("player").setup({
		"file" : mediaUrl,
		"width" : 850,
		"height" : 437,
		autostart : true
	});
	
	jwplayer("player").onReady(function() {
		// Slomo only works for HTML5 and ...
	    if (jwplayer().getRenderingMode() == "html5") {
	        videoTag = document.querySelector('video');
	        // ... browsers that support playbackRate
	        if(videoTag.playbackRate) 
	        {
	        	jwplayer("player").addButton("<c:url value="/resources/images/player/btn_slomo.170623.png"/>","Toggle Slow Motion", toggleSlomo,"slomo");
	        }
	    }
		this.addButton("<c:url value="/resources/images/player/btn_slomo.170623.png"/>","Toggle Slow Motion", toggleSlomo,"slomo");
	});
	
}


function toggleSlomo() {
	currentRate == 1 ? currentRate = 0.2: currentRate = 1;
    videoTag.playbackRate = currentRate;
    videoTag.defaultPlaybackRate = currentRate;
    if(navigator.userAgent.toLowerCase().indexOf('firefox') > -1){
        jwplayer("player").seek(jwplayer("player").getPosition());
    }
	return;
};

</script>

<script type="text/javascript">

function onClick_close()
{
	self.opener = self;
	window.close();
}

function onClick_download()
{
 	$(location).attr("href", "<c:url value="/file/download"/>/${contentMeta.content.instances[0].file.fileId}");
}


function onClick_modify(){
	
	var param = $("#frmContent").serialize();
	console.log(param);
	
	$.ajax({
		url : "<c:url value="/service/content/modifyContent"/>",
		async : false,
		dataType : "json",
		data : param, 
		method : "post",
		beforeSend : function(xhr, settings ){},
		error : function (xhr, status, error){},
		success : function (ajaxData) {
			if(ajaxData.resultCode == "Success"){
				location.reload();
			}
			else{
				new bcs_messagebox().openError("컨텐츠수정", "컨텐츠수정 오류 발생 [code="+ajaxData.resultCode+"]", null);
			}
		}
	});
	
}



function onClick_delete(){
	
	var param = $("#frmContent").serialize();
	console.log(param);
	
	$.ajax({
		url : "<c:url value="/service/content/deleteContent"/>",
		async : false,
		dataType : "json",
		data : param, 
		method : "post",
		beforeSend : function(xhr, settings ){},
		error : function (xhr, status, error){},
		success : function (ajaxData) {
			if(ajaxData.resultCode == "Success"){
				self.opener = self;
				window.close();
			}
			else{
				new bcs_messagebox().openError("컨텐츠삭제", "컨텐츠삭제 오류 발생 [code="+ajaxData.resultCode+"]", null);
			}
		}
	});
	
	
	
}
</script>




</head>
<body>

<div title="사용자조회" class="bcs_dialog_hide" data-ctrl-view="user_select" data-event-selected="callback_selectedUsers" data-param-selectedUserId="frmSelectedUsers">
</div>

<form id="frmUserSearch">
	<input type="hidden" name="sportsEventCode"/>
</form>
<form id="frmSelectedUsers">
	<input type="hidden" 	name="selectedUserIds" value="" />
</form>

<div class="popupWindow">
	<div class="popupheader">영상 상세 정보</div>
	<div class="popupcontents">
		<div class="vodregistBox">


			<div class="vodregist ">
				<div class="videoview mgb30">
					<div id="player"></div>	
				</div>
				<form id="frmContent">
					<input type="hidden" name="contentId" value="${contentMeta.contentId}" />
					<table class="write_type1 mgb20" summary="">
						<caption></caption>
						<colgroup>
						<col width="150">
						<col width="*">
						<col width="150">
						<col width="*">
						</colgroup>
						<tbody>
							<tr>
								<th>제목</th>
								<td colspan="3">
									<input type="text" name="title" title="제목" class="type_2" value="${contentMeta.title}"/>
								</td>
							</tr>
							<tr>
								<th>스포츠종목</th>
								<td>
									<input type="text" title="스포츠종목" class="type_2" value="${contentMeta.sportsEvent.name}" readonly>
									
									<input type="hidden" name="sportsEventCode" title="스포츠종목" class="type_2" value="${contentMeta.sportsEventCode}">
								</td>
								<th>소유자</th>
								<td>
									<input type="text" name="contentUserNames" title="소유자" class="type_2" data-ctrl-contentMeta="contentUserNames" value="${contentMeta.contentUserNames}" readonly/>
									
									<c:forEach items="${contentMeta.contentUsers}" var="contentUser" varStatus="status">
										<input type="hidden" name="contentUsers[${status.index}].userId" data-ctrl-contentmeta="contentUserId" value="${contentUser.userId}">
									</c:forEach>
									
											
											
	<%-- 								<input type="text" name="tagUser" title="소유자" class="type_2" value="${contentMeta.contentUserNames}" readonly> --%>
								</td>
							</tr>
							<tr>
								<th>녹화자</th>
								<td>
									<select class="td sel_type_2" name="recordUserId" title="녹화자">
										<option value="">선택하세요</option>
										<c:forEach items="${users}" var="user">
											<c:set var="isSelected" value=""/>
											<c:if test="${contentMeta.recordUserId == user.userId}">
												<c:set var="isSelected" value="selected"/>
											</c:if>
											<c:choose>
												<c:when test="${loginUser.userId == user.userId}">
													<option value="${user.userId}" ${isSelected}>${user.userName}</option>
												</c:when>
												<c:when test="${loginUser.isAdmin == true or loaginUser.isDeveloper == true or loginUser.userType == 'Admin'}">
													<option value="${user.userId}" ${isSelected}>${user.userName}</option>
												</c:when>
												<c:when test="${loginUser.sportsEventCode == user.sportsEventCode}">
													<option value="${user.userId}" ${isSelected}>${user.userName}</option>
												</c:when>
											</c:choose>
										</c:forEach>
									</select>
								</td>
								<th>녹화일자</th>
								<td>
									<input type="text" class="inputTxt date" style="height:35px; margin-right:5px; width:220px" name="recordDate"  value="<fmt:formatDate value="${contentMeta.recordDate}" pattern="yyyy-MM-dd"/>"/>
								</td>
							</tr>
							<tr>
								<th>설명</th>
								<td colspan="3">
									<textarea name="summary" title="설명" class="ta_type_1" >${contentMeta.summary}</textarea>
								</td>
							</tr>
						</tbody>
					</table>
				</form>
				<div class="btnbox alignR">
					<span class="btn_typeA t1"><a href="javascript:onClick_modify();">수정</a></span>
					<span class="btn_typeA"><a href="javascript:onClick_download();">다운로드</a></span>
					<c:choose>
						<c:when test="${loginUser.isAdmin == true or loaginUser.isDeveloper == true or loginUser.userType == 'Admin' or loginUser.userId == contentMeta.recordUserId}">
							<span class="btn_typeA t3"><a href="javascript:onClick_delete();">삭제</a></span>
						</c:when>
					</c:choose>
<!-- 					<span class="btn_typeA t1"><a href="#">저장</a></span> <span -->
					<span class="btn_typeA t2"><a href="javascript:onClick_close();">닫기</a></span>
				</div>
			</div>

		</div>
	</div>


</div>



</body>
</html>
