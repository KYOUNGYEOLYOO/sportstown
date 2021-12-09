<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page session="false" %>
<jsp:useBean id="now" class="java.util.Date" />


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
		width: "218px",
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
	
	// liveStreamUrl	= "<c:url value="/resources/mp4/sample1.mp4"/>";
	// dvrStreamUrl 	= "<c:url value="/resources/mp4/sample2.mp4"/>";
	
	// dvrStreamUrl = "http://223.26.218.116:1935/live/aaa.stream/playlist.m3u8?DVR";
	// dvrStreamUrl = "http://223.26.218.116:1935/live/aaa.stream/manifest.f4m?DVR";
	// manifest.f4m?DVR
	
	console.log("liveStreamUrl -> " + liveStreamUrl);
	console.log("dvrStreamUrl -> " + dvrStreamUrl);
	
	$playerLi.attr("data-value-liveStreamUrl", liveStreamUrl);
	$playerLi.attr("data-value-dvrStreamUrl", dvrStreamUrl);
	
	jwplayer(playerId).setup({
		// "file": liveStreamUrl,
		"width": "100%",
		"height": "100%",
		"playlist" : [
			// {file : liveStreamUrl},
			{file : dvrStreamUrl , type: "hls", perload: "none", minDvrWindow: 3}
		],
		//  hlshtml: true,
		// primary: "html5",
		controls : true,
		autostart : true,
		aspectratio: "16:9",
		rtmp: {
	        bufferlength: 0.1
	    }
		// 'provider': 'rtmp',
		// "streamer": '${streamer}',
	});
	eventSender.send("data-event-initilizePlayerButton", "${camera.camId}");
	
	jwplayer(playerId).onReady(function(){
		$("#" + playerId).removeClass("jw-flag-small-player").removeClass("jw-flag-time-slider-above");
	});
	
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
							<img src="<c:url value="/resources/images"/>/player/status_play.png"/>
						</c:otherwise>
					</c:choose>
				</div>
				<select id="sample" data-ctrl-select="selectRecordFiles">
					<option disabled selected>${camera.name}</option>
				</select>
			</div>
			
			<div class="videoicons" style="width:150px;">
				<a href="#"> DVR </a>
<!-- 				<div class="rec_status">Rec...</div> -->
<%-- 				<img src="<c:url value="/resources/images"/>/contents/icons_cam1.png" data-camera-camId="${camera.camId}" data-ctrl-button="btnRecord"/>  --%>
				<img src="<c:url value="/resources/images"/>/contents/icons_cam3.png" data-camera-camId="${camera.camId}" data-ctrl-button="btnStopRecord"/>
<%-- 				<a href="#"><img src="<c:url value="/resources/images"/>/contents/icons_cam2.png" /></a>  --%>
			</div>
		</div>
		<div class="videocontents">
			<div id="player_${camera.camId}" data-ctrl-view="live_player" data-camera-camId="${camera.camId}" >
				Wait loading
			</div>	
		</div>
	</div>
</li>
