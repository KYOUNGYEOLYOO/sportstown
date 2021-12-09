<!doctype html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page session="false" %>
<jsp:useBean id="now" class="java.util.Date" />

<html lang="ko" xml:lang="ko">
<head>


<jsp:include page="/include/head"/>

<script type="text/javascript" src="<c:url value="/bluecap/jwplayer-7.11.3/jwplayer.js"/>"></script>
<script>jwplayer.key="/cDB/4s47uzPnRb3q/gtzOze04a7CABARZuQWIwYvfQ=";</script>

<script type="text/javascript">
var videoTag;
var currentRate = 1;

$(document).ready(function(){
	
	 // $( "#dialog" ).dialog();
	 initPalyer();
	 
	 
	 initNDvrPlayer();
});



function initPalyer()
{
	var  mediaUrl = "";
	mediaUrl = "<c:url value="/resources/mp4/sample.mp4"/>";
	
	mediaUrl = "http://223.26.218.116:1935/vod/_definst_/mp4:test/sample11.mp4/playlist.m3u8";
	// mediaUrl = "http://223.26.218.116:1935/vod/_definst_/mp4:test/sample11.mp4/manifest.mpd";
	console.log("vod => " + mediaUrl);
	
	jwplayer("player").setup({
		"file" : mediaUrl,
		"width" : 850,
		"height" : 437,
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
	             jwplayer().addButton("slomo.png","Toggle Slow Motion", toggleSlomo,"slomo");
	        }
	    }
		this.addButton("slomo.png","Toggle Slow Motion", toggleSlomo,"slomo");
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

function initNDvrPlayer()
{
	var mediaUrl = "http://223.26.218.116:1935/live/cam1.stream/manifest.f4m?DVR";
	mediaUrl = "http://223.26.218.116:1935/live/cam1.stream/manifest.m3u8?DVR";
	mediaUrl = "http://223.26.218.116:1935/live/cam1.stream/playlist.m3u8?DVR";
	mediaUrl = "https://wowza.jwplayer.com/live/jelly.stream/playlist.m3u8?DVR";
	
	console.log("live => " + mediaUrl);
	jwplayer("ndvrPlayer").setup({
		// "file" : mediaUrl,
		"width" : 850,
		"height" : 437,
		// 'provider': 'rtmp',
		autostart : true,
		
		sources: [{
	        file: mediaUrl
	    }, {
	        file: "rtmp://223.26.218.116:1935/live/cam1.stream"
	    }],
	    rtmp: {
	        bufferlength: 3
	    },
	    fallback: true
	});
	
	jwplayer("ndvrPlayer").onReady(function() {
		this.addButton("slomo.png","position", function(){
			console.log(jwplayer("ndvrPlayer").getPosition());
			jwplayer("ndvrPlayer").seek(-60);
		},"position");
	});
	
	
	jwplayer("ndvrPlayer").onPlay(function() {
		
		jwplayer("ndvrPlayer").seek(-60);
	});
}
</script>
</head>
<body>


<div id="dialog" title="수정" class="ui-state-error" style="display:none;">
  <p>받아라 뿅뿅</p>
</div>

<!-- skip navi -->
<div id="skiptoContent">
	<a href="#gnb">주메뉴 바로가기</a>
	<a href="#contents">본문내용 바로가기</a>
</div>
<!-- //skip navi -->

<div id="wrapper">
<!-- header -->
<jsp:include page="/include/top"/>
<!-- //header -->

<!-- container -->
<div id="container">
	<div id="contentsWrap">

		<!-- lnbWrap -->
		<div id="lnbWrap">
			<div class="lnbWraph2">
				<h2>연구소 소개</h2>	
			</div>
			<div class="">
				<select class="selectyze psa" name="camera" id="camera">
					<option value="">카메라선택</option>
					<option value="">All Select</option>
					<option value="">T1 Camera</option>
					<option value="">T2 Camera</option>
					<option value="">T3 Camera</option>
					<option value="">T4 Camera</option>
				</select>
			</div>
			<div class="mgt30">
				<select class="selectyze psa" name="partition" id="partition">
					<option value="">화면분할</option>
					<option value="">1분할</option>
					<option value="">2분할</option>
					<option value="">4분할</option>
					<option value="">9분할</option>
				</select>
			</div>	
			
		</div>
		<!-- //lnbWrap -->
		
		<!-- contents -->
		<div id="contents">

			<!-- title -->
			<h3>영상녹화</h3>
			<!-- //title -->

			<div class="vodregistBox">
			
				<div class="vodregist ">
					<div class="videoview mgb30">
						<div id="player"></div>	
					</div>
					
					
					<div class="videoview mgb30">
						<div id="ndvrPlayer"></div>	
					</div>
					
					<table class="write_type1 mgb20" summary="">
						<caption></caption>
						<colgroup>
							<col width="150">
							<col width="*">
						</colgroup>
					<tbody>
					<tr>
						<th><label for="ff6">제목</label></th>
						<td><input type="text" id="ff6" name="" value="" title="제목" class="type_2"> </td>
					</tr>
					<tr>
						<th><label for="ff7">설명</label></th>
						<td>
							<textarea title="설명" id="ff7" class="ta_type_1"></textarea>
						</td>
					</tr>
					<tr>
						<th><label for="ff7">소유</label></th>
						<td>
							<select class="td sel_type_2" title="선수명 선택">
								<option>선수명1</option>
								<option>선수명2</option>
								<option>선수명3</option>
							</select>
						</td>
					</tr>
					</tbody>
					</table>

					<div class="btnbox alignR">
						<span class="btn_typeA"><a href="#">다운로드</a></span>
						<span class="btn_typeA t1"><a href="#">저장</a></span>
						<span class="btn_typeA t2"><a href="#">취소</a></span>
					</div>						
				</div>
			</div>
		</div>
		<!-- //contents -->
	</div>
</div>
<!-- //container -->

<!-- footer -->
<jsp:include page="/include/footer"/>
<!-- //footer -->
</div>

</body>
</html>
