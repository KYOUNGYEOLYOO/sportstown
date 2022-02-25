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
<c:set var="contentRootUri" value=""/>

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
		});	
		
}

function onClick_download()
{
 	$(location).attr("href", "<c:url value="/file/download"/>/${contentMeta.content.instances[0].file.fileId}");
}
function onClick_modify()
{
	// 이전부터 구현되어 있는 기능이 없었음...
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


</script>


	<div class="videoview">
		<div id="player" style="background:#fafafa"></div>	
	</div>
	<div class="detailWrap">
		<dl>
			<dt>제목</dt>
			<dd class="full"><input type="text" name="title" title="제목" class="inputTxt" value="${contentMeta.title}" readonly></dd>
			<dt>종목</dt>
			<dd><input type="text" name="sportsEvent" title="종목" class="inputTxt" value="${contentMeta.sportsEvent.name}" readonly></dd>
			<dt class="ml20">소유자</dt>
			<dd><input type="text" name="tagUser" title="소유자" class="inputTxt" value="${contentMeta.contentUserNames}" readonly></dd>
			<dt>녹화자</dt>
			<dd><input type="text" name=recordUser title="녹화자" class="inputTxt" value="${contentMeta.recordUser.userName}" readonly></dd>
			<dt class="ml20">녹화일자</dt>
			<dd>
				<div class="datepickerBox">
					<input type="text" id="recordFromDate" name="recordDate" class="inputTxt date"  value="<fmt:formatDate value="${contentMeta.recordDate}" pattern="yyyy-MM-dd" />" readonly/>
				</div>					
			</dd>
			<dt>설명</dt>
			<dd class="full"><textarea name="summary" title="설명" readonly>${contentMeta.summary}</textarea></dd>
			<dt>파일</dt>
			<dd class="full">
				<input type="text" name="instances[0].orignFileName" value="" data-ctrl-contentMeta="orignFileName" class="inputTxt" readonly>
				<input type="hidden" name="instances[0].fileId" value="" data-ctrl-contentMeta="fileId">						
			</dd>						
		</dl>
		<div class="btnWrap">
<!-- 			<a class="btn download">다운로드</a>		 -->
			<a class="btn download" href="javascript:onClick_download();">다운로드</a>		
			<a class="btn write" href="javascript:onClick_auth();">승인요청</a>		
			<div class="btnWrap">
				<a class="btn delete" href="javascript:onClick_delete();">삭제</a> 
				<a class="btn edit">수정</a>					
			</div>
		</div>
	</div>

	
