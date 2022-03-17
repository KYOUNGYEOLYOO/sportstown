<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page session="false" %>
<jsp:useBean id="now" class="java.util.Date" />
<jsp:useBean id="ipFilter" class="com.bluecapsystem.cms.jincheon.sportstown.common.define.IPFilterConstant" />

<link rel="stylesheet" href="<c:url value="/bluecap/css/video-js.css"/>"> 
<script src="https://unpkg.com/video.js/dist/video.js"></script>
<script type="text/javascript" src="<c:url value="/bluecap/jwplayer-7.11.3/jwplayer.js"/>"></script>
<script>jwplayer.key="/cDB/4s47uzPnRb3q/gtzOze04a7CABARZuQWIwYvfQ=";</script>
<style type="text/css">
.vjs-tech {width:auto; height:"39vh"}
</style>


<c:set var="contentMeta_video" value=""/>
<%-- <c:set var="vodStreamer" value=""/> --%>
<%-- <c:set var="contentRootUri" value=""/> --%>

<script type="text/javascript">
var videoTag;
var currentRate = 1;
var eventSender = new bcs_ctrl_event($("[data-ctrl-view=content_details]"));

//alert("111");
test();
//$(document).ready(function(){
//	alert("tttt");
//	test();
	
//});

// <c:set var="streamUrl" value=""/>
// <c:set var="streamFile" value=""/>

<c:forEach items="${contentMeta.content.instances}" var="instance" end="0">
	<c:set var="streamUrl" value="${vodStreamer}/${contentRootUri}"/>
	<c:set var="streamFile" value="${instance.file.fileName}"/>
</c:forEach>

function test(){
// 	alert("111");

	console.log("login location ==> ${loginUser.connectLocation}");
	console.log("contentRootUri ==> ${contentRootUri}");
	console.log("vodStreamer ==> ${vodStreamer}");
	console.log("streamUrl ==> ${streamUrl}");
	console.log("streamFile ==> ${streamFile}");
		//var  mediaUrl = "${vodStreamer}${contentRootUri}${contentMeta.content.instances[0].file.filePath}${contentMeta.content.instances[0].file.fileName}/playlist.m3u8"
		var  mediaUrl = "${ipFilter.filterAddress(loginUser.connectLocation, vodStreamer)}${contentRootUri}${contentMeta.content.instances[0].file.filePath}${contentMeta.content.instances[0].file.fileName}/playlist.m3u8"

		
		
// 		mediaUrl = "<c:url value="/resources/mp4/sample.mp4"/>";
// 		mediaUrl = "http://223.26.218.116:1935/vod/_definst_/mp4:./test/bcs.mp4/playlist.m3u8";
		
		console.log("mediaURL", mediaUrl);
		
		jwplayer("player").setup({
			"file" : mediaUrl,
			"width" : 560,
			"height" : 315,
			autostart : true
		});
		
		jwplayer("player").onReady(function() {
			// Slomo only works for HTML5 and ...
		    if (jwplayer().getRenderingMode() == "html5") {
		        videoTag = document.querySelector('video');
		        // ... browsers that support playbackRate
// 		        if(videoTag.playbackRate) 
// 		        {
// 		        	jwplayer("player").addButton("<c:url value="/resources/images/player/btn_slomo.170623.png"/>","Toggle Slow Motion", toggleSlomo,"slomo");
// 		        }
		    }
			this.addButton("<c:url value="/resources/images/player/btn_slomo.170623.png"/>","Toggle Slow Motion", toggleSlomo,"slomo");
			this.addButton("<c:url value="/resources/images/contents/btn_canvas.png"/>","canvas", startCanvas,"canvas");
		});	
		
}

$(document).ready(function(){
	
	
	$("#frmContent").find("[data-ctrl-contentMeta=contentUserNames]").click(function(){
		$("#frmSelectedUsers").empty();
	
		$("#frmContent").find("[data-ctrl-contentMeta=contentUserId]").each(function(){
			
			$("#frmSelectedUsers").append($("<input type='hidden' name='selectedUserIds' />").val($(this).val()));
		});
		
	
		var sportsEentCode = $("#frmContent").find("[name=sportsEventCode]").val();
		
		$("#frmUserSearch").find("[name=sportsEventCode]").val(sportsEentCode);
		var param = $("#frmUserSearch").serialize();
		
		$("[data-ctrl-view=user_select]").empty();
		$("[data-ctrl-view=user_select]").jqUtils_bcs_loadHTML(
				"<c:url value="/user/select"/>?" + param,
				false, "get", null, null
			);
		
	});
});


function onClick_download()
{
	
	var contentId = "${contentId}";
	
	registDownloadLog(contentId);
 	$(location).attr("href", "<c:url value="/file/download"/>/${contentMeta.content.instances[0].file.fileId}");
 	
}

function registDownloadLog(value){
	
	var param = $("#frmContent").serialize();
	console.log(param);
	
	$.ajax({
		url : "<c:url value="/service/content/registDownloadLog"/>",
		async : false,
		dataType : "json",
		data : param, 
		method : "post",
		beforeSend : function(xhr, settings ){},
		error : function (xhr, status, error){},
		success : function (ajaxData) {
			if(ajaxData.resultCode == "Success"){
				//location.reload();
			}
			else{
				//new bcs_messagebox().openError("컨텐츠수정", "컨텐츠수정 오류 발생 [code="+ajaxData.resultCode+"]", null);
			}
		}
	});
}

function onClick_modify()
{
	// 이전부터 구현되어 있는 기능이 없었음...
	console.log("이전부터 구현되어 있는 기능이 없었음...");
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
function onClick_delete()
{
	var contentId = "${contentId}";
	if(typeof contentId == "undefined" || contentId == null)
	{
		new bcs_messagebox().open("영상검색", "컨텐츠를 선택해 주세요", null);
		return;
	}
	
	var mb = new bcs_messagebox().open("영상검색", "삭제 하시겠습니까?", null, {
		"삭제" : function(){
			console.log("<c:url value="/service/content/deleteContent"/>/" + contentId);
			$.ajax({
// 				url : "<c:url value="/"/>/" + contentId,
				url : "<c:url value="/service/content/deleteContent"/>" ,
				async : false,
				dataType : "json",
// 				data : null,
				data : {
					contentId: contentId
				},
				method : "post",
				beforeSend : function(xhr, settings ){},
				error : function (xhr, status, error){},
				success : function (ajaxData) {
					if(ajaxData.resultCode == "Success"){
						console.log("ajaxData.resultCode : ", ajaxData.resultCode);
// 						$("#contentList").jqGrid("delRowData", ajaxData.contentId);
						eventSender.send("data-event-reloadList",contentId); // contentDetails에서 삭제시 contentManage에 jqgrid selrow 삭제
						mb.close();
					}else{
						new bcs_messagebox().openError("영상검색", "컨텐츠 삭제중 오류 발생 [code="+ajaxData.resultCode+"]", null);
					}
				}
			});
		},
		"닫기" : function(){ mb.close(); }
	});
	
}


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

	<div class="videobox">
		<div class="videoview">
			<div id="player" style="background:#fafafa"></div>
<!-- 			<button class="player cannvas"></button> -->
		</div>
	</div>
	
	<div title="사용자조회" class="bcs_dialog_hide" data-ctrl-view="user_select" data-event-selected="callback_selectedUsers" data-param-selectedUserId="frmSelectedUsers">
	</div>
	
	<form id="frmUserSearch">
		<input type="hidden" name="sportsEventCode"/>
	</form>
	<form id="frmSelectedUsers">
		<input type="hidden" 	name="selectedUserIds" value="" />
	</form>
	
	
	<div class="detailWrap">
		<form id="frmContent">
			<dl>
				<input type="hidden" name="contentId" value="${contentMeta.contentId}" />
				<dt>제목</dt>
				<dd class="full"><input type="text" name="title" title="제목" class="inputTxt" value="${contentMeta.title}" ></dd>
				<dt>종목</dt>
				<dd><input type="text" title="종목" class="inputTxt" value="${contentMeta.sportsEvent.name}" readonly></dd>
<%-- 				<dd><input type="text" name="sportsEventCode" title="종목" class="inputTxt" value="${contentMeta.sportsEvent.name}" readonly></dd> --%>
				<dd  style="display:none;">
					<input type="hidden" name="sportsEventCode" title="스포츠종목" class="inputTxt" value="${contentMeta.sportsEventCode}">
				</dd>
				
				<dt class="ml20">소유자</dt>
				<dd>
<%-- 					<input type="text" name="tagUser" title="소유자" class="inputTxt" value="${contentMeta.contentUserNames}" readonly> --%>
					<input type="text" name="contentUserNames" title="소유자" class="inputTxt" data-ctrl-contentMeta="contentUserNames" value="${contentMeta.contentUserNames}" readonly>
					
					<c:forEach items="${contentMeta.contentUsers}" var="contentUser" varStatus="status">
						<input type="hidden" name="contentUsers[${status.index}].userId" data-ctrl-contentmeta="contentUserId" value="${contentUser.userId}">
					</c:forEach>
				</dd>
				
				<dt>녹화자</dt>
				<dd>
<%-- 					<input type="text" name=recordUser title="녹화자" class="inputTxt" value="${contentMeta.recordUser.userName}" readonly> --%>
					<select class="inputTxt" name="recordUserId" title="녹화자">
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
				</dd>
				<dt class="ml20">녹화일자</dt>
				<dd>
					<div class="datepickerBox">
						<input type="text" id="recordFromDate" name="recordDate" class="inputTxt date"  value="<fmt:formatDate value="${contentMeta.recordDate}" pattern="yyyy-MM-dd" />" readonly/>
					</div>					
				</dd>
				<dt>설명</dt>
				<dd class="full"><textarea name="summary" title="설명" >${contentMeta.summary}</textarea></dd>
<!-- 				<dt>파일</dt> -->
<!-- 				<dd class="full"> -->
<!-- 					<input type="text" name="instances[0].orignFileName" value="" data-ctrl-contentMeta="orignFileName" class="inputTxt" readonly> -->
<!-- 					<input type="hidden" name="instances[0].fileId" value="" data-ctrl-contentMeta="fileId">						 -->
<!-- 				</dd>						 -->
			</dl>
		</form>
		<div class="btnWrap">
<!-- 			<a class="btn download">다운로드</a>		 -->
			<a class="btn download" href="javascript:onClick_download();">다운로드</a>		
<!-- 			<a class="btn write" href="javascript:onClick_auth();">승인요청</a>		 -->
			<div class="btnWrap">
				<a class="btn delete" href="javascript:onClick_delete();">삭제</a> 
				<a class="btn edit" href="javascript:onClick_modify();">수정</a>					
			</div>
		</div>
	</div>

	
