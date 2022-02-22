<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page session="false" %>
<jsp:useBean id="now" class="java.util.Date" />
<jsp:useBean id="ipFilter" class="com.bluecapsystem.cms.jincheon.sportstown.common.define.IPFilterConstant" />


<script type="text/javascript">

$(document).ready(function(){
	var eventSender = new bcs_ctrl_event($("[data-ctrl-view=vod_player_list]"));
	
	var $playerLi = $("[data-ctrl-view=vod_player_list]").find("li[data-vod-fileId=${file.fileId}]");
	var $player = $playerLi.find("#player_${file.fileId}");
	var playerId = $player.attr("id");
	
	
	$playerLi.find("[data-ctrl-button=btnClosePlayer]").click(function(){
		eventSender.send("data-event-remove", {fileId : "${file.fileId}", orignFileName : "${file.orignFileName}" });
	});
	
	$player.empty();
	
	console.log("login location ==> ${loginUser.connectLocation}");
	var streamUrl = "${ipFilter.filterAddress(loginUser.connectLocation, vodStreamer)}${rootUri}${file.filePath}${file.fileName}/playlist.m3u8";
	var isInit = true;
	
	// streamUrl = "http://223.26.218.116:1935/vod/_definst_/mp4:${rootUri}/${file.filePath}${file.fileName}/playlist.m3u8";
	// streamUrl = "${vodStreamer}/mp4:${rootUri}/${file.filePath}${file.fileName}";
	// streamUrl = "<c:url value="/resources/mp4/sample.mp4"/>";
	
	console.log(streamUrl);
	jwplayer(playerId).setup({
		"file" : streamUrl,
		"width" : "100%",
		aspectratio: "16:9",
		// "height" : 437,
		// 'provider': 'rtmp',
		controls: false,
		autostart : true
	});
	jwplayer(playerId).onPlay(function() {
		if(isInit == true) this.pause();
		 isInit = false;
	});

	
});


// 캔버스 호출하는 함수
function canvasTest()
{
	$("#canvas").empty();
	$("#canvas").jqUtils_bcs_loadHTML(
			"<c:url value="/canvas/canvasPop"/>" ,
			false, "get", null, null
		);
	console.log('canvas_pop');
	
	$(".ui-front").appendTo("#container");
}


</script>
<form id="canvas"></form>

<li data-vod-fileId="${file.fileId}">
	<div class="videobox">
		<div class="videohead">
			<div class="videotitle">
				${file.orignFileName}
			</div>
			<div class="videoicons">
				<img src="<c:url value="/resources/images"/>/contents/icons_cam3.png" data-vod-fileId="${file.fileId}" data-ctrl-button="btnClosePlayer"/>
			</div>
		</div>
		<div class="videocontents">
			<div id="player_${file.fileId}" data-ctrl-view="vod_player" data-vod-fileId="${file.fileId}" >
				Wait loading
			</div>
			<button class="player cannvas" onclick="canvasTest()"></button>
		</div>
	</div>
</li>
