<!-- <!DOCTYPE html> -->
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



<c:set var="proxyMeta" value="" />
<c:set var="enablePlayer" value="false" />
<c:forEach items="${camera.streamMetaItems}" var="streamMetas">
	<c:if test="${streamMetas.metaClass == 'Proxy'}">
		<c:set var="proxyMeta" value="${streamMetas}" />
		<c:set var="enablePlayer" value="true" />
		<c:set var="liveStreamer"  value="${fn:replace(liveStreamer, '{STREAM_SERVER}', proxyMeta.streamServer.name)}"/>
		<c:set var="dvrStreamer"  value="${fn:replace(dvrStreamer, '{STREAM_SERVER}', proxyMeta.streamServer.name)}"/>
	</c:if>
</c:forEach> 




<!-- <script type='text/javascript' src="http://jwplayer.mediaserve.com/jwplayer.js"></script> -->

<style type="text/css">
.vjs-tech {width:850px; height:437px}
</style>

<script type="text/javascript">
/////// document.ready 있던 곳
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

$(document).ready(function(){
	
	initPalyer();
});


function initPalyer()
{
	console.log("login location ==> ${loginUser.connectLocation}");
	console.log("streamer ==> ${streamer}");
	// var  mediaUrl = "${streamer}/${streamMeta.application.name}/${streamMeta.streamName}"
	var  mediaUrl = "${ipFilter.filterAddress(loginUser.connectLocation, streamer)}/${streamMeta.application.name}/${streamMeta.streamName}";
	// mediaUrl = "<c:url value="/resources/mp4/sample.mp4"/>";
	// mediaUrl = "http://223.26.218.116:1935/vod/_definst_/mp4:test/bcs.mp4/playlist.m3u8";
	console.log(mediaUrl);
	
	//var liveStreamUrl = "${ipFilter.filterAddress(loginUser.connectLocation, liveStreamer)}/${proxyMeta.application.name}/${proxyMeta.streamName}/playlist.m3u8";
	//var dvrStreamUrl = "${ipFilter.filterAddress(loginUser.connectLocation, dvrStreamer)}/${proxyMeta.application.name}/${proxyMeta.streamName}/playlist.m3u8?DVR";
	var streamUrl = mediaUrl + "/playlist.m3u8"
		
	dvrStreamUrl = streamUrl;
	console.log('dvrStreamUrl : ' + dvrStreamUrl);
	
	var videoObj = videojs("player", {
		
		controls: false,
        autoplay: 'muted', // 크롬 정책상 사용자한테 볼륨잇는채로 자동재생되는건안나옴. mute나 클릭이벤트가 잇엇을시에만 재생됨
        preload: 'auto',
        liveui: true,

        fill : true,
        sources: [{
            //src: "https://moctobpltc-i.akamaihd.net/hls/live/571329/eight/playlist.m3u8",
            //src : "http://223.26.218.115:1935/live/bcs4/playlist.m3u8",
            src : dvrStreamUrl,
            type: "application/x-mpegURL"
        }],
        // 0307 비디오 크기 조절
        width : 560,
        height : 315,
        controlBar: {
            pictureInPictureToggle: false,
            currentTimeDisplay: true,
            timeDivider: false,
            durationDisplay: true,
            remainingTimeDisplay: true,

            volumePanel: {
                inline: true
            }

        },
        liveTracker: {
        	trackingThreshold: 3,
//         	liveTolerance: 15,
        	liveTolerance: 2,
        },
        userActions: {
            doubleClick: false,
        }
	});
	
	/* jwplayer("player").setup({
		"file" : mediaUrl,
		"width" : 850,
		"height" : 437,
		autostart : true
	});
	
	jwplayer("player").onReady(function() {
	});
	
	
	jwplayer("player").onPlay(function() {
	}); */
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





<!-- <div class="popupWindow"> -->
<%-- 	<div class="popupheader">${camera.name} 녹화 보기</div> --%>
<!-- 	<div class="popupcontents"> -->
<!-- 		<div class="vodregistBox"> -->

	<div class="videoview">
		<video id="player" style="background:#000000"></div>	
	</div>
<!-- 			<div class="vodregist "> -->
<!-- 				<div class="videoview mgb30"> -->
<!-- 					<video id="player" width="850" height="437"></video>	 -->
<!-- 				</div> -->


	<div class="detailWrap">
		<dl>
			<dt>제목</dt>
			<dd class="full"><input type="text" name="sportsEvent" title="카메라" class="type_2" value="${camera.name}" readonly></dd>
			<dt>종목</dt>
			<dd class="full"><input type="text" name="sportsEvent" title="스포츠종목" class="type_2" value="${camera.sportsEvent.name}" readonly></dd>
			<dd>	
			<c:if test="${camera.state == 'Recording'}">
 				<span class="btn_typeA t3"><a href="javascript:onClick_stopRecord();">녹화중지</a></span>
			</c:if>
			</dd>
		</dl> 
	</div>
<!-- 	<table class="write_type1 mgb20" summary=""> -->
<%-- 		<caption></caption> --%>
<%-- 		<colgroup> --%>
<%-- 		<col width="150"> --%>
<%-- 		<col width="*"> --%>
<%-- 		<col width="150"> --%>
<%-- 		<col width="*"> --%>
<%-- 		</colgroup> --%>
<!-- 		<tbody> -->
<!-- 			<tr> -->
<!-- 				<th>카메라</th> -->
<!-- 				<td colspan="3"> -->
<%-- 					<input type="text" name="sportsEvent" title="카메라" class="type_2" value="${camera.name}" readonly> --%>
<!-- 				</td> -->
<!-- 			</tr> -->
<!-- 			<tr> -->
<!-- 				<th>스포츠종목</th> -->
<!-- 				<td colspan="3"> -->
<%-- 					<input type="text" name="sportsEvent" title="스포츠종목" class="type_2" value="${camera.sportsEvent.name}" readonly> --%>
<!-- 				</td> -->
<!-- 			</tr> -->
<!-- 		</tbody> -->
<!-- 	</table> -->

<!-- 	<div class="btnbox alignR"> -->
<%-- 		<c:if test="${camera.state == 'Recording'}"> --%>
<!-- 			<span class="btn_typeA t3"><a href="javascript:onClick_stopRecord();">녹화중지</a></span> -->
<%-- 		</c:if> --%>
<!-- 	</div>		 -->
						<!-- 2022 0307 녹화상태 닫기 버튼 삭제 ( 윈도우 팝업 > 3단 분할 ) -->
<!-- 					<span class="btn_typeA t2"><a href="javascript:onClick_close();">닫기</a></span> -->
<!-- 				</div> -->
<!-- 			</div> -->

<!-- 		</div> -->
<!-- 	</div> -->


<!-- </div> -->



