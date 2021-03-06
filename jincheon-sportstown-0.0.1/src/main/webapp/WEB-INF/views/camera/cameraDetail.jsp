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

<!-- <style type="text/css"> -->
<!-- .vjs-tech {width:auto; height:"39vh"}  -->
<!-- </style> -->

<c:if test="${camera != null}">
<c:set var="streamMeta" value=""/>


	<c:forEach items="${camera.streamMetaItems}" var="meta">
		<c:if test="${meta.metaClass == 'HD'}">
			<c:set var="streamMeta" value="${meta}"/>
			<c:set var="enablePlayer" value="true" />
			<c:set var="streamer"  value="${fn:replace(streamer, '{STREAM_SERVER}', streamMeta.streamServer.name)}"/>
		</c:if>
	</c:forEach>

<script type="text/javascript">
var videoTag;
var currentRate = 1;

//alert("111");
test();
//$(document).ready(function(){
//	alert("tttt");
//	test();
	

function test()
{
// 	console.log("${streamer}");
	console.log("login location ==> ${loginUser.connectLocation}");
	console.log("streamer ==> ${streamer}");
	// var  mediaUrl = "${streamer}/${streamMeta.application.name}/${streamMeta.streamName}"
	var  mediaUrl = "${ipFilter.filterAddress(loginUser.connectLocation, streamer)}/${streamMeta.application.name}/${streamMeta.streamName}";
	// mediaUrl = "<c:url value="/resources/mp4/sample.mp4"/>";
	// mediaUrl = "http://223.26.218.116:1935/vod/_definst_/mp4:test/bcs.mp4/playlist.m3u8";
	console.log(mediaUrl);
	var videoUrl = mediaUrl + "/playlist.m3u8";
	console.log("videoUrl : " + videoUrl);
	
	var videoObj = videojs("player", {
		

		controls: false,
        autoplay: 'muted', // ?????? ????????? ??????????????? ?????????????????? ??????????????????????????????. mute??? ?????????????????? ?????????????????? ?????????
        preload: 'auto',
        liveui: true,

        fill : true,
        sources: [{
			src : videoUrl,
			type: "application/x-mpegURL"
		}],
//         width : 400,
//         height : 400,
        height : 100,
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
        	liveTolerance: 2,
//         	liveTolerance: 15,
        },
        userActions: {
            doubleClick: false,
        }
	});
	
	

}
</c:if>

</script>
<div class="detailWrap">
	<div class="videoview">
		<video id="player"></video>	
	</div>
	<input type="hidden" name="camId" value="${camera.camId}" />
	<div class="scrollWrap">
		<dl>
			<dt>????????????</dt>
			<dd class="full">
				<input type="text" name="name" value="${camera.name}" title="????????????" class="inputTxt" readonly>
			</dd>
			<dt>???????????????</dt>
			<dd class="full">
				<c:set var="cameraTypeName"  value="" />
						
				<c:choose>
					<c:when test="${camera.cameraType == 'Static'}">
						<c:set var="cameraTypeName"  value="??????" />
					</c:when>
					<c:when test="${camera.cameraType == 'Shift'}">
						<c:set var="cameraTypeName"  value="??????" />
					</c:when>
				</c:choose>
				<input type="text" name="cameraType" value="${cameraTypeName}" title="???????????????" class="inputTxt" readonly>
			</dd>
			<dt>???????????????</dt>
			<dd class="full">
				<input type="text" name="locationCodeName" value="${camera.location.name}" title="???????????????" class="inputTxt" readonly>
			</dd>
			<dt>???????????????</dt>
			<dd class="full">
				<input type="text" name="sportsEventCodeName" value="${camera.sportsEvent.name}" title="???????????????" class="inputTxt" readonly>
			</dd>
			<dt>????????? ??????</dt>
			<dd class="full">
				<p>
					<c:choose>
						<c:when test="${camera.isLiveOnly == true}">???????????????</c:when>
						<c:otherwise>?????????/DVR ??????</c:otherwise>
					</c:choose>
				</p>
			</dd>
		</dl>	
		
		<c:forEach items="${camera.streamMetaItems}" var="streamMeta" varStatus="st">
			<h3>${streamMeta.metaClass} ?????? ??????</h3>
			<input type="hidden" name="streamMetaItems[${st.index}].camId" value="${streamMeta.camId}" />
			<input type="hidden" name="streamMetaItems[${st.index}].metaClass" value="${streamMeta.metaClass}" />
			
			
			<dl>
				<dt>??????????????????</dt>
				<dd class="full">
					<input type="text" name="streamMetaItems[${st.index}].applicationCodeName" value="${streamMeta.streamServer.name}" title="??????????????????" class="type_2" readonly>
				</dd>
				<dt>Application</dt>
				<dd>
					<input type="text" name="streamMetaItems[${st.index}].applicationCodeName" value="${streamMeta.application.name}" title="Application ????????? ??????" class="type_2" readonly>
				</dd>
				<dt class="ml20">????????????</dt>
				<dd>
					<input type="text" name="streamMetaItems[${st.index}].streamName" value="${streamMeta.streamName}" title="???????????? ????????? ??????" class="type_2" readonly>	
				</dd>
	
				<dt>Source URL</dt>
				<dd class="full"><input type="text" name="streamMetaItems[${st.index}].streamSourceUrl" value="${streamMeta.streamSourceUrl}" title="????????????" class="type_2" readonly></dd>
	
				<dt>?????????</dt>
				<dd>
					<input type="text" name="streamMetaItems[${st.index}].streamUserId" value="${streamMeta.streamUserId}" title="???????????? ?????? ?????????" class="type_2" readonly>
				</dd>
				<dt class="ml20">????????????</dt>
				<dd>
					<input type="text" name="streamMetaItems[${st.index}].streamUserPassword" value="${streamMeta.streamUserPassword}" title="???????????? ?????? ????????????" class="type_2" readonly>
				</dd>
			</dl>
		</c:forEach>
	</div>
</div>
