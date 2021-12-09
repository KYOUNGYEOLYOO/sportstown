<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page session="false" %>
<jsp:useBean id="now" class="java.util.Date" />
<jsp:useBean id="ipFilter" class="com.bluecapsystem.cms.jincheon.sportstown.common.define.IPFilterConstant" />


<c:set var="proxyMeta" value="" />
<c:set var="enablePlayer" value="false" />
<c:forEach items="${camera.streamMetaItems}" var="streamMeta">
	<c:if test="${streamMeta.metaClass == 'Proxy'}">
		<c:set var="proxyMeta" value="${streamMeta}" />
		<c:set var="enablePlayer" value="true" />
		<c:set var="liveStreamer"  value="${fn:replace(liveStreamer, '{STREAM_SERVER}', proxyMeta.streamServer.name)}"/>
		<c:set var="dvrStreamer"  value="${fn:replace(dvrStreamer, '{STREAM_SERVER}', proxyMeta.streamServer.name)}"/>
	</c:if>
</c:forEach>




<li data-camera-camId="${camera.camId}">

<script type="text/javascript">
$(document).ready(function(){
	
	
	var eventSender = new bcs_ctrl_event($("[data-ctrl-view=live_player_list]"));
	var $playerLi = $("[data-ctrl-view=live_player_list]").find("li[data-camera-camId=${camera.camId}]");
	var $player = $playerLi.find("[data-ctrl-view=live_player][data-camera-camId=${camera.camId}]");
	var playerId = $player.attr("id");
	
	$playerLi.find("[data-ctrl-select=selectRecordFiles]").selectmenu({
		width: "190px",
		open : function(event, ui){
			$(this).find("option:not(:first)").remove();
			
			var files = eventSender.send("data-event-expendRecordFiles", "${camera.camId}");
			
			for(var i = 0; i < 5 && i < files.length; i++)
			{
				var $option = $("<option/>").val(files[i].fileId).text(files[i].registDate);
			
				$(this).append($option);
			}
			$(this).selectmenu("refresh");
		}, 
		select : function(event, ui){
			var fileId = $(this).val();
			
			if(fileId == null)
				return;
			eventSender.send("data-event-selectedRecordFile", {"camId" : "${camera.camId}", "fileId" : fileId});
			
			$(this).find("option:not(:first)").remove();
			$(this).find("option:first").prop("selected", true);
			$(this).selectmenu("refresh");
		}
	});
	
	$playerLi.find("[data-ctrl-button=btnStopRecord]").click(function(){
		eventSender.send("data-event-disableCamera", "${camera.camId}");
	});
	
	
<c:if test="${enablePlayer == true}">
	
	var liveStreamUrl = "${liveStreamer}/${proxyMeta.application.name}/${proxyMeta.streamName}";
	var dvrStreamUrl = "${dvrStreamer}/${proxyMeta.application.name}/${proxyMeta.streamName}/playlist.m3u8?DVR";
	
	console.log("liveStreamUrl -> " + liveStreamUrl);
	console.log("dvrStreamUrl -> " + dvrStreamUrl);
	
	
	$playerLi.attr("data-value-liveStreamUrl", liveStreamUrl);
	$playerLi.attr("data-value-dvrStreamUrl", dvrStreamUrl);
	
	flowplayer($player, {
		autoplay : true,
		clip: {
			sources : [{ type: "application/x-mpegurl", src: dvrStreamUrl}]
		}
	}).on("ready", function(){
		$playerLi.find("button.player-btn").removeClass("disable");
		eventSender.send("data-event-initilizePlayerButton", "${camera.camId}");
	});
	
	
	
	/* dvr 버튼 event */
	$playerLi.find("button.player-btn.player-btn-dvr").click(function(){
		var isDvr = $(this).hasClass("on");
		var isDisable = $(this).hasClass("disable");
		if(isDisable) return;
		if(isDvr){
			console.log("play ==> " + liveStreamUrl);
			flowplayer($player).load(liveStreamUrl);
			$(this).removeClass("on");
			$(this).text("LIVE");
		}else {
			console.log("play ==> " + dvrStreamUrl);
			flowplayer($player).load(dvrStreamUrl);
			$(this).addClass("on");
			
			$(this).text("DLIVE");
		}
	});
	
	
	/* rec 버튼 event */
	$playerLi.find("button.player-btn.player-btn-record").click(function(){
		var isRec = $(this).hasClass("on");
		var isDisable = $(this).hasClass("disable");
		if(isDisable) return;
		if(isRec){
			eventSender.send("data-event-stopRecord", "${camera.camId}");
		}else {
			eventSender.send("data-event-record", "${camera.camId}");
		}
	});
	
	// $playerLi.find("button.player-btn").removeClass("disable");
	// eventSender.send("data-event-initilizePlayerButton", "${camera.camId}");
	
	
</c:if>

});
</script>
	<div class="videobox">
		<div class="videohead">
			<div class="videotitle">
				<div class="rec_status">
					<c:choose>
						<c:when test="${camera.state == 'Recording'}">
							<img src="<c:url value="/resources/images"/>/player/status_record.170623.png"/>
						</c:when>
						<c:otherwise>
							<img src="<c:url value="/resources/images"/>/player/status_play.170623.png"/>
						</c:otherwise>
					</c:choose>
				</div>
				<select id="sample" data-ctrl-select="selectRecordFiles">
					<option disabled selected>${camera.name}</option>
				</select>
			</div>
			
			<div class="videoicons" style="width:150px;">
			
				<button class="player-btn disable player-btn-dvr on" >dLive</button>
				<button class="player-btn disable player-btn-record" >REC</button>
				<img src="<c:url value="/resources/images"/>/contents/icons_cam3.png" data-camera-camId="${camera.camId}" data-ctrl-button="btnStopRecord"/>
<%-- 				<a href="#"><img src="<c:url value="/resources/images"/>/contents/icons_cam2.png" /></a>  --%>
			</div>
		</div>
		<div class="videocontents">
		
			<div id="player_${camera.camId}" class="fp-fat" data-ctrl-view="live_player" data-camera-camId="${camera.camId}" >
			</div>
		
		</div>
		
	</div>
</li>
