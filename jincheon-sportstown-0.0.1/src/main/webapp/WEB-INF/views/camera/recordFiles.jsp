<!DOCTYPE html>
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
<!-- <script type='text/javascript' src="http://jwplayer.mediaserve.com/jwplayer.js"></script> -->

<style type="text/css">

</style>

<script type="text/javascript">

$(document).ready(function(){
	
	initPalyer();
	
	$(document).on('click', '.canvasMenuWrap > ul > li > p', function() {	        
		$(this).toggleClass("open");
	});		
	$(document).on('click', '.canvasMenuWrap > ul a', function() {	
		$(".canvasMenuWrap .eraser").removeClass("on");
		$(this).parent().siblings().removeClass("on");
		$(this).parent().addClass("on");
	});
	$(document).on('click', '.canvasMenuWrap .width a', function() {
//	 	alert($(this).context.innerHTML);
		changeWidth($(this).context.innerHTML);
	});
	$(document).on('click', '.canvasMenuWrap .figure a', function() {
		drawShape($(this).context.innerHTML);
	});
	$(document).on('click', '.canvasMenuWrap .color a', function() {
		changeColor($(this).context.innerHTML);
	});
	$(document).on('click', '.canvasMenuWrap .eraser a', function() {	        
		$(".canvasMenuWrap").find("li").removeClass("on");
		eraseCanvas();
		$(this).parent().addClass("on");
	});	
	$(document).on('click', '.canvasMenuWrap .reset p', function() {	        
		$(".canvasMenuWrap").find("li").removeClass("on");
		clrCanvas($("#canvas"));
	});		
	$(document).on('click', '.canvasMenuWrap > p.close', function() {	        
		$("#wrapper").removeClass("canvasopen");
		$(".canvasMenuWrap").find("li").removeClass("on");
		delCanvas($("#canvas"),$("#canvasChange"));
		
		$("#player").attr('style','width:100%;height:65%;');
		
		
		
		document.webkitExitFullscreen();
	});
});
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
	console.log("vodStreamer ==> ${vodStreamer}");
	var  mediaUrl = "${ipFilter.filterAddress(loginUser.connectLocation, vodStreamer)}${fileRootUri}${currentFile.filePath}${currentFile.fileName}/playlist.m3u8"
	// var  mediaUrl = "${ipFilter.filterAddress(loginUser.connectLocation, 'http://119.65.245.234:1935/vod/_definst_/mp4:.')}${fileRootUri}${currentFile.filePath}${currentFile.fileName}/playlist.m3u8"
	// mediaUrl = "<c:url value="/resources/mp4/sample.mp4"/>";
	// mediaUrl = "http://223.26.218.116:1935/vod/_definst_/mp4:test/bcs.mp4/playlist.m3u8";
	console.log(mediaUrl);
	
	
	jwplayer("player").setup({
		flashplayer: "<c:url value="/bluecap"/>/jwplayer/jwplayer.flash.swf",
		"file" : mediaUrl,
		"width" : "100%",
		"height" : "65%",
		// 'provider': 'rtmp',
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

		
		
	    jwplayer("player").addButton("<c:url value="/resources/images/player/btn_slomo.170623.png"/>","Toggle Slow Motion", toggleSlomo,"slomo");
	    this.addButton("<c:url value="/resources/images/contents/btn_canvas.png"/>","canvas", startCanvas,"canvas");
// 	    jwplayer("player").addButton("","","","cannvas","");
// 	    $("#player").find("[button=cannvas]").attr("class","");
// 	    $("#player").find("[button=cannvas]").addClass("player cannvas");
	});
	
	
	jwplayer("player").onPlay(function() {
	
		var video = document.getElementById("player").querySelector("video");
		console.log(video);
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
 	$(location).attr("href", "<c:url value="/file/download"/>/${currentFileId}");
}

function startCanvas(){

	$("#wrapper").addClass("canvasopen");
	
	

	var container = document.getElementById("wrapper");

	if (!document.fullscreenElement) {
		
		addCanvas3();
		
		$("#player").attr('style','width:100%;height:100%;');
// 		$("#videoview").attr('style','height: 100%;padding: 20px 20px 0; position: relative;');
		container.webkitRequestFullscreen();
	} else {
		$("#player").attr('style','width:100%;height:65%;');
// 		$("#videoview").attr('style','height: 335px;padding: 20px 20px 0; position: relative;');
		
		
		
		document.webkitExitFullscreen();
	}
	
	
}


document.addEventListener('fullscreenchange', exitHandler);
document.addEventListener('webkitfullscreenchange', exitHandler);
document.addEventListener('mozfullscreenchange', exitHandler);
document.addEventListener('MSFullscreenChange', exitHandler);

function exitHandler() {
    if (!document.fullscreenElement && !document.webkitIsFullScreen && !document.mozFullScreen && !document.msFullscreenElement) {
// 		$("#videoview").attr('style','height: 335px;padding: 20px 20px 0; position: relative;');
// 		document.webkitExitFullscreen();
    	$('.canvasMenuWrap > p.close').trigger("click");
    }
}  
</script>




</head>
<body>
<div id="wrappers" style="width:100%; height:100%">
	<div id="wrapper" style="width:100%; height:100%">
	
	<div class="popupWindow" style="width:100%; height:100%">
		<div class="popupheader">${camera.name} ?????? ??????</div>
		<div id='container' style="width:100%; height:100%">
	<!-- 		<div class="popupcontents"> -->
	<!-- 			<div class="vodregistBox"> -->
		
	<!-- 				<div class="vodregist "> -->
	<!-- 					<div class="videobox"> -->
	<!-- 					<div class="videoview mgb30"> -->
							<video id="player"></video>
							<!-- 						//canvas -->	
							<div class="canvasContainer">
								<div class="canvasWrap" id="canvasWrap">
									<div class="canvasMenuWrap" >
										<ul>
											<li class="figure">
												<p>??????</p>
												<ul>
													<li class="grid"><a href="#">??????</a></li>
													<li class="quadrangle"><a href="#">??????</a></li>
													<li class="circle"><a href="#">???</a></li>
													<li class="line"><a href="#">?????????</a></li>
												</ul>
											</li>
											<li class="color">
												<p>??????</p>
												<ul>
													<li class="blue"><a href="#">??????</a></li>
													<li class="red"><a href="#">??????</a></li>
													<li class="green"><a href="#">??????</a></li>
													<li class="black"><a href="#">??????</a></li>
													<li class="skiblue"><a href="#">?????????</a></li>				
												</ul>
											</li>
											<li class="width">
												<p>??????</p>
												<ul>
													<li class="thin"><a href="#">?????? ???</a></li>
													<li class="normal"><a href="#">??????</a></li>
													<li class="bold"><a href="#">????????? ???</a></li>
												</ul>
											</li>
											<li class="eraser">
												<a href="#">?????????</a>
											</li>		
											<li class="reset">
												<p>?????????</p>
											</li>				
										</ul>
										<p class="close">??????</p>
									</div>			
								</div>
							</div>		
	<!-- 						//canvas -->
	<!-- 					</div> -->
	<!-- 					</div> -->
	<!-- 				</div> -->
	<!-- 			</div> -->
	<!-- 		</div> -->
		
						
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
				<span class="btn_typeA"><a href="javascript:onClick_download();">????????????</a></span>
		<!-- 					<span class="btn_typeA t1"><a href="#">??????</a></span> <span -->
				<span class="btn_typeA t2"><a href="javascript:onClick_close();">??????</a></span>
			</div>
		</div>
	</div>
	
	
	</div>
</div>
</body>
</html>
