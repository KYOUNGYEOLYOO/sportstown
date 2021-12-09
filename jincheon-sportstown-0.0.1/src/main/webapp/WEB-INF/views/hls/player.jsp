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
	
	console.log("STREAM META : ${proxyMeta}");
	var playerId = $player.attr("id");
	
	var obj = this;
	
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
	
	/* $playerLi.find("[data-ctrl-select=selectDelayTime]").selectmenu({
		width: "90px",
		change : function(event, ui){
			
			var offsetTime = 3;
			var realDelayTime = Number(delayTime) - offsetTime; 
			
			if(videoObj.liveTracker.liveCurrentTime() > 0){
				videoObj.currentTime(videoObj.liveTracker.liveCurrentTime() - realDelayTime);
			}
		},
		select : function(event, ui){
			delayTime = $(this).val();
		}
	}); */
	
	$playerLi.find("[data-ctrl-button=btnStopRecord]").click(function(){
		eventSender.send("data-event-disableCamera", "${camera.camId}");
	});
	
	
<c:if test="${enablePlayer == true}">

	var liveStreamUrl = "${ipFilter.filterAddress(loginUser.connectLocation, liveStreamer)}/${proxyMeta.application.name}/${proxyMeta.streamName}/playlist.m3u8";
	var dvrStreamUrl = "${ipFilter.filterAddress(loginUser.connectLocation, dvrStreamer)}/${proxyMeta.application.name}/${proxyMeta.streamName}/playlist.m3u8?DVR";
	
	var isLiveOnly = "${camera.isLiveOnly}" == "true";
	
	console.log("isLiveOnly -> " + isLiveOnly);
	
	if(isLiveOnly) {
		dvrStreamUrl = liveStreamUrl;
	}
	
	console.log("liveStreamUrl -> " + liveStreamUrl);
	console.log("dvrStreamUrl -> " + dvrStreamUrl);
	
	
	$playerLi.attr("data-value-liveStreamUrl", liveStreamUrl);
	$playerLi.attr("data-value-dvrStreamUrl", dvrStreamUrl);
	//var videoObj = videojs( $player[0], {
	var videoObj = videojs(playerId, {
		
		controls: true,
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
        	liveTolerance: 15,
        },
        userActions: {
            doubleClick: false,
        }
	});
	
	
		
	/* rec 버튼 event */
	$playerLi.find("button.player-btn.player-btn-record").click(function(){
		var isRec = $(this).hasClass("on");
		var isDisable = $(this).hasClass("disable");
		//if(isDisable) return;
		if(isRec){
			eventSender.send("data-event-stopRecord", "${camera.camId}");
		}else {
			eventSender.send("data-event-record", "${camera.camId}");
		}
	});
	
	/* live 버튼 event */
	$playerLi.find("button.player-btn.player-btn-live").click(function(){
		var $dvrBtn = $playerLi.find("button.player-btn.player-btn-dvr");
		var isLive = $(this).hasClass("on");
		var isDisable = $(this).hasClass("disable");
		console.log($dvrBtn);
		
		//if(isDisable) return;
		
		console.log('live 버튼 click');

		
		liveCheck();
		return;
		
		console.log('isDisable 체크 off, isDisable : ' + isDisable);
		console.log( 'seekableStart : ' + videoObj.liveTracker.seekableStart());
		console.log( 'seekableEnd : ' + videoObj.liveTracker.seekableEnd());
		console.log('liveCurrentTime : ' + videoObj.liveTracker.liveCurrentTime());
		console.log('currentTime : ' + videoObj.currentTime());
		
		
		
		//videoObj.addClass('vjs-liveui');
		videoObj.liveTracker.handleDurationchange();
		console.log('duration : ' + videoObj.duration());
		console.log('liveWindow : ' + videoObj.liveTracker.liveWindow());
		console.log('trackingThreshold : ' + videoObj.liveTracker.options_.trackingThreshold);
		console.log('liveui : ' + videoObj.options_.liveui);
		
		videoObj.liveTracker.seekableStart(0);
		if(videoObj.liveTracker.liveCurrentTime() > 0){
			//videoObj.currentTime(videoObj.liveTracker.liveCurrentTime());
			videoObj.liveTracker.seekToLiveEdge();
		
		}
		
		if(isLive){
			$(this).removeClass("on");
			$dvrBtn.addClass("on");
		}else {
			$(this).addClass("on");
			$dvrBtn.removeClass("on");
		} 
		
		//eventSender.send("data-event-liveRecord", "${camera.camId}");
		//eventSender.send("data-event-liveRecord", playerId);
	});
	
	/* dvr 버튼 event */
	$playerLi.find("button.player-btn.player-btn-dvr").click(function(){
		var $liveBtn = $playerLi.find("button.player-btn.player-btn-live");
		var delayTime = 2;
		
		console.log($liveBtn);
		
		liveCheck();
		return;
		
		if(videoObj.liveTracker.liveCurrentTime() > 0){
			videoObj.currentTime(videoObj.liveTracker.liveCurrentTime() -delayTime);
		}
		
		var isDvr = $(this).hasClass("on");
		
		if(isDvr){
			$(this).removeClass("on");
			$liveBtn.addClass("on");
		}else {
			$(this).addClass("on");
			$liveBtn.removeClass("on");
		} 
		
		//var isDisable = $(this).hasClass("disable");
		//if(isDisable) return;
		
		/* var isDvr = $(this).hasClass("on");
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
		} */
	}); 
	
	function liveCheck(){
		
		var $dvrBtn = $playerLi.find("button.player-btn.player-btn-dvr");
		var $liveBtn = $playerLi.find("button.player-btn.player-btn-live");
				
		var isDvr = $dvrBtn.hasClass("on");
		var isLive = $liveBtn.hasClass("on");
		var delayTime = 2;
		
		if(isLive){
			if(videoObj.liveTracker.liveCurrentTime() > 0){
				videoObj.currentTime(videoObj.liveTracker.liveCurrentTime() -delayTime);
			}
			
			$liveBtn.removeClass("on");
			$dvrBtn.addClass("on");
		}else{
			
			videoObj.liveTracker.handleDurationchange();
			
			videoObj.liveTracker.seekableStart(0);
			if(videoObj.liveTracker.liveCurrentTime() > 0){
				//videoObj.currentTime(videoObj.liveTracker.liveCurrentTime());
				videoObj.liveTracker.seekToLiveEdge();
			
			}
			
			$dvrBtn.removeClass("on");
			$liveBtn.addClass("on");
		}
		
		
		
		
		
	};
	
	
</c:if>


});


</script>
	<div class="videobox">
		<div class="videohead">
			<div class="videotitle">
				<div class="rec_status">
							<img src="<c:url value="/resources/images"/>/player/status_play.170623.png"/>
				</div>
				<select id="sample" data-ctrl-select="selectRecordFiles">
					<option disabled selected>${camera.name}</option>
				</select>
				
			</div>
			
			<div class="videoicons" style="width:240px; padding-left : 0px;">
				<c:if test="${camera.isLiveOnly != true}">
					<!-- <button class="player-btn disable player-btn-dvr on" >dLive</button> -->
				</c:if>
				<button class="player-btn player-btn-record" >Rec</button>
				<button class="player-btn player-btn-live on" player-button-liveId="live_${camera.camId}" >live</button>
				<button class="player-btn player-btn-dvr" >dLive</button>
				<%-- <button class="player-btn player-btn-back" player-button-backId="back_${camera.camId}">delay</button> --%>
				
				<img src="<c:url value="/resources/images"/>/contents/icons_cam3.png" data-camera-camId="player_${camera.camId}" data-ctrl-button="btnStopRecord"/>
			</div>
		</div>
		<div class="videocontents">
		
			<video id="player_${camera.camId}" class="video-js" data-ctrl-view="live_player" data-camera-camId="${camera.camId}" >
			</video>
		
		</div>
		
	</div>
</li>
