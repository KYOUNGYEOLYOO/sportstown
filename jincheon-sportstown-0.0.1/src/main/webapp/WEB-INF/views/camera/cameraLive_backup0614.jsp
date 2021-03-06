<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page session="false" %>
<jsp:useBean id="now" class="java.util.Date" />
<jsp:useBean id="ipFilter" class="com.bluecapsystem.cms.jincheon.sportstown.common.define.IPFilterConstant" />

<html lang="ko" xml:lang="ko">
<head>


<jsp:include page="/include/head"/>

<script type="text/javascript" src="<c:url value="/bluecap/jwplayer-7.11.3/jwplayer.js"/>"></script>
<script>jwplayer.key="/cDB/4s47uzPnRb3q/gtzOze04a7CABARZuQWIwYvfQ=";</script>
<!-- <script type='text/javascript' src="http://jwplayer.mediaserve.com/jwplayer.js"></script> -->

<style type="text/css">

</style>

<script type="text/javascript">

$(document).ready(function(){
	
	initPalyer();
});



</script>


<c:set var="streamMeta" value=""/>

<c:forEach items="${camera.streamMetaItems}" var="meta">
	<c:if test="${meta.metaClass == 'Proxy'}">
		<c:set var="streamMeta" value="${meta}"/>
		<c:set var="streamer"  value="${fn:replace(streamer, '{STREAM_SERVER}', streamMeta.streamServer.name)}"/>
	</c:if>
</c:forEach>



<script type="text/javascript">

var videoTag;
var currentRate = 1;


function initPalyer()
{
	console.log("login location ==> ${loginUser.connectLocation}");
	// var  mediaUrl = "${streamer}/${streamMeta.application.name}/${streamMeta.streamName}"
	var  mediaUrl = "${ipFilter.filterAddress(loginUser.connectLocation, streamer)}/${streamMeta.application.name}/${streamMeta.streamName}";
	// mediaUrl = "<c:url value="/resources/mp4/sample.mp4"/>";
	// mediaUrl = "http://223.26.218.116:1935/vod/_definst_/mp4:test/bcs.mp4/playlist.m3u8";
	console.log(mediaUrl);
	
	jwplayer("player").setup({
		"file" : mediaUrl,
		"width" : 850,
		"height" : 437,
		autostart : true
	});
	
	jwplayer("player").onReady(function() {
	});
	
	
	jwplayer("player").onPlay(function() {
	});
}


</script>

<script type="text/javascript">

function onClick_close(){
	
	self.opener = self;
	window.close();
}


function onClick_stopRecord()
{
	var retVal = false;
	$.ajax({
		url : "<c:url value="/service/camera/stopRecord"/>/${camera.camId}?isCoercion=true",
		async : false,
		dataType : "json",
		data : null,
		method : "post",
		beforeSend : function(xhr, settings ){},
		error : function (xhr, status, error){},
		success : function (ajaxData) {
			console.log("stop record response [camId = ${camera.camId}]");
			console.log(ajaxData);
			location.reload();
		}
	});
}

</script>


</head>
<body>


<div class="popupWindow">
	<div class="popupheader">${camera.name} ?????? ??????</div>
	<div class="popupcontents">
		<div class="vodregistBox">


			<div class="vodregist ">
				<div class="videoview mgb30">
					<video id="player"></video>	
				</div>


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
							<th>?????????</th>
							<td colspan="3">
								<input type="text" name="sportsEvent" title="?????????" class="type_2" value="${camera.name}" readonly>
							</td>
						</tr>
						<tr>
							<th>???????????????</th>
							<td colspan="3">
								<input type="text" name="sportsEvent" title="???????????????" class="type_2" value="${camera.sportsEvent.name}" readonly>
							</td>
						</tr>
					</tbody>
				</table>

				<div class="btnbox alignR">
					<c:if test="${camera.state == 'Recording'}">
						<span class="btn_typeA t3"><a href="javascript:onClick_stopRecord();">????????????</a></span>
					</c:if>
					
					<span class="btn_typeA t2"><a href="javascript:onClick_close();">??????</a></span>
				</div>
			</div>

		</div>
	</div>


</div>



</body>
</html>
