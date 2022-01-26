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

<%-- <link rel="stylesheet" href="<c:url value="/bluecap/flowplayer-7.2.4/skin/skin.css"/>">
<script type="text/javascript" src="<c:url value="/bluecap/flowplayer-7.2.4/flowplayer.min.js"/>"></script> --%>

<link rel="stylesheet" href="<c:url value="/bluecap/css/video-js.css"/>"> 
<script src="https://unpkg.com/video.js/dist/video.js"></script>

<style type="text/css">
.videobox .videocontents {background-color: #000; color: #CFCFCF;}

.camera1pt li .videobox { width : 1266px; }
.camera1pt li .videocontents { min-height:800px;}
.camera1pt li .video-js { width : 1266px ; min-height:800px;}

.camera2pt li .videobox { width : 617px;}
.camera2pt li .videocontents { min-height:381px;}
.camera2pt li:nth-child(2n+1) {margin-left: 15px; margin-bottom:10px;}
.camera2pt li .video-js { width : 617px; min-height:381px;}

.camera3pt li { margin-bottom:10px; margin-left : 10px;}
.camera3pt li:nth-child(3n+1) { margin-left: 10px; margin-bottom:10px; }
.camera3pt li:nth-child(3n+2) { margin-left: 10px; margin-bottom:10px; }
.camera3pt li .videobox {width : 410px;}
.camera3pt li .videocontents { min-height:237px;}
.camera3pt li .video-js { width : 410px; min-height:237px;}

.videobox .videohead .videotitle { width : 245px; padding-left : 10px; }
.videobox .videohead .videotitle .rec_status{ float : left; margin-right:10px;}
.videobox .videohead .videotitle .rec_status img { vertical-align: middle; }
.videobox .videohead .videoicons { width : 50px; }
.videobox .videohead .videotitle .ui-selectmenu-button.ui-button {margin-bottom : 3px;}

.videoicons .player-btn {height : 33px;
	min-width: 35px;
	border: 1px solid;
	color: #f9f9f9;
	font-weight: bold;
}


.videoicons .player-btn.disable {
	color: gray;
}

.videoicons .player-btn.on {
	color: #d50000;
}

.videoicons .player-btn.on.player-btn-dvr {
	color: #22cd22;
}


</style>



<script type="text/javascript">
$(document).ready(function(){
	
	$("#sportsEvent").change(function(){
			var sportsEventCode = $(this).val();
			console.log('sportsEventCode : ' + sportsEventCode);
			$("#cameraBtn").jqUtils_bcs_loadHTML(
					"<c:url value="/record/cameraList"/>/" + sportsEventCode,
					false, "get", null, null
				);
		});
		
	$("#sportsEvent").trigger("change");
});

</script>


<script type="text/javascript">
function callback_addPlayer(sender, camId)
{
	var isSus = false;
	
	$.ajax({
		url : "<c:url value="/hls/player"/>/" + camId,
		async : false,
		datatype: "html",
		data : null,
		error : function(xhr, status, error){
			isSus = false;
		},
		success : function (ajaxData){
			$("#playerList").append(ajaxData);
			change_player_layout();
			isSus = true;
		}
	}); 
	//hlsInfo.onCreateHls(camId);
	
	return isSus;
}

function callback_removePlayer(sender, camId)
{
	
	var $playerLi = $("[data-ctrl-view=live_player_list]").find("li[data-camera-camId="+camId+"]");
	$playerLi.remove();
	
	///////////////////////////////////////////////
	var deleveVideo = videojs('player_'+camId);
	deleveVideo.dispose();
	////////////////////////////////////////////////
	
	$("ul.btnbox li[data-camera-camId="+camId+"]").removeClass("on");
	
	change_player_layout();
	
	return true;
}


function change_player_layout()
{
	var cntPlayer = $("#playerList > li").length;

	if(cntPlayer == 1)
	{
		$("#playerList").attr("class", "");
		$("#playerList").addClass("camera1pt");
	}else if(cntPlayer <= 4)
	{
		$("#playerList").attr("class", "");
		$("#playerList").addClass("camera2pt");
	}else
	{
		$("#playerList").attr("class", "");
		$("#playerList").addClass("camera3pt");
	}
}

</script>

<script type="text/javascript">

function startRecord(camId)
{
	var retVal = false;
	
	var param = $("#frmRecordData").serialize();
	console.log('startReford');
	console.log(camId);
	console.log(param);
	
	$.ajax({
		url : "<c:url value="/service/camera/record"/>/" + camId,
		async : false,
		dataType : "json",
		data : param,
		method : "post",
		beforeSend : function(xhr, settings ){},
		error : function (xhr, status, error){
			console.log('error');
			console.log(xhr);
			console.log(status);
			console.log(error);
		},
		success : function (ajaxData) {
			console.log("start record response [camId = " + camId + "]");
			console.log(ajaxData);
			retVal = ajaxData.isSuccess;
			
		}
	});
	return retVal;
}


function stopRecord(camId)
{
	var retVal = false;
	$.ajax({
		url : "<c:url value="/service/camera/stopRecord"/>/" + camId,
		async : false,
		dataType : "json",
		data : null,
		method : "post",
		beforeSend : function(xhr, settings ){},
		error : function (xhr, status, error){},
		success : function (ajaxData) {
			console.log("stop record response [camId = " + camId + "]");
			console.log(ajaxData);
			retVal = ajaxData.isSuccess;
		}
	});
	
	return retVal;
	
}


function onExpend_recordFiles(sender, camId)
{
	$("#frmRecordFileParams").find("input[name=camId]").val(camId);
	$("#frmRecordFileParams").find("input[name=sportsEventCode]").val($("#sportsEvent").val());
	
	var pData = $("#frmRecordFileParams").serialize();
	var files = new Array();
	
	console.log('onExpend_recordFiles data: ');
	console.log(pData);
	
	$.ajax({
		url : "<c:url value="/service/file/getFileList"/>",
		async : false,
		dataType : "json",
		data : pData,
		method : "post",
		beforeSend : function(xhr, settings ){},
		error : function (xhr, status, error){},
		success : function (ajaxData) {
			console.log('onExpend_recordFiles success: ');
			console.log(ajaxData);
			files = ajaxData.files;
		}
	});
	
	return files;
}

function onSelected_recordFile(sender, params)
{	
	window.open(
			"<c:url value="/camera/recordFiles"/>/" + params.camId + "?currentFileId=" + params.fileId,
			"recordFiles", 
			"height=750,width=910,resizable=no,menubar=no,toolbar=no", true);
}


function callback_initPlayerButton(sender, camId)
{
	var $playerLi = sender.find("li[data-camera-camId="+camId+"]");
	//var playerId = $playerLi.find("[data-ctrl-view=live_player]").attr("id");
	var playerId = $playerLi.find("[data-ctrl-view=live_player]").attr("id");
	
	$.ajax({
		url : "<c:url value="/service/camera/getCamera/"/>" + camId,
		async : false,
		dataType : "json",
		data : null,
		method : "post",
		beforeSend : function(xhr, settings ){},
		error : function (xhr, status, error){},
		success : function (ajaxData) {
			console.log(ajaxData);
			var camera = ajaxData.camera;
			if(camera.state == 'Wait'){
				$playerLi.find(".rec_status img").attr("src", "<c:url value="/resources"/>/images/player/status_play.170623.png");
			}else{
				$playerLi.find(".rec_status img").attr("src", "<c:url value="/resources"/>/images/player/status_record.170623.png");
				
				$playerLi.find("button.player-btn.player-btn-record").addClass("on");
			}
		}
	});
	
}
function onClick_stopRecord(sender, camId)
{
	 var $playerLi = sender.find("li[data-camera-camId="+camId+"]");
	// var streamUrl = $playerLi.attr("data-value-liveStreamUrl");
	var streamUrl = $playerLi.attr("data-value-dvrStreamUrl");
	var playerId = $playerLi.find("[data-ctrl-view=live_player]").attr("id");
	
	if(stopRecord(camId) == false)
	{
		return false;
	}
	
	$playerLi.find("button.player-btn.player-btn-record").removeClass("on");
	callback_initPlayerButton(sender, camId); 
	return true;
}

function onClick_record(sender, camId)
{
	var $playerLi = sender.find("li[data-camera-camId="+camId+"]");
	//var playerId = $playerLi.find("[data-ctrl-view=live_player]").attr("id");
	var playerId = $playerLi.find("[data-ctrl-view=live_player]").attr("id");
	console.log($playerLi);
	console.log(playerId);
	console.log(camId);
	if(startRecord(camId) == false)
	{
		return false;
	}
	
	console.log($playerLi.find("button.player-btn.player-btn-record"));
	$playerLi.find("button.player-btn.player-btn-record").addClass("on");
	$playerLi.find(".rec_status img").attr("src", "<c:url value="/resources"/>/images/player/status_record.170623.png");
	
	return true;
}

function onClick_liveRecord(sender, camId)
{
	var playerId = $playerLi.find("[data-ctrl-view=live_player]").attr("id");
	
	console.log('onClick_liveRecord sender : ' + sender + 'camId : ' + camId);
	
	return true;
}

function onClick_backRecord(sender, camId)
{
	var playerId = $playerLi.find("[data-ctrl-view=live_player]").attr("id");
	
	console.log('onClick_backRecord sender : ' + sender + 'camId : ' + camId);
	
	return true;
}


</script>

<script type="text/javascript">

function onClick_recordAll()
{	
	//$("[data-ctrl-view=live_player_list]").find("li").each(function(idx, playerLi){
	$("[data-ctrl-view=live_player_list]").find("li[data-camera-camid]").each(function(idx, playerLi){
		var $player = $(playerLi).find("div.jwplayer");
		var playerId = $player.attr("id");
		var camId = $(playerLi).attr("data-camera-camId");
		
		console.log('onclickrecordAll')
		console.log(playerLi);
		console.log('idx : ' + idx + 'camId : ' + camId);
		
		onClick_record($("[data-ctrl-view=live_player_list]"), camId);
		
	});
}

function onClick_stopRecordAll()
{
	console.log('onClick_stopRecordAll');
	
	//$("[data-ctrl-view=live_player_list]").find("li").each(function(idx, playerLi){
	$("[data-ctrl-view=live_player_list]").find("li[data-camera-camid]").each(function(idx, playerLi){
		var $player = $(playerLi).find("div.jwplayer");
		var playerId = $player.attr("id");
		var camId = $(playerLi).attr("data-camera-camId");
		onClick_stopRecord($("[data-ctrl-view=live_player_list]"), camId);
	});
	
}

function onClick_backRecordAll()
{
	$("[player-button-backId]").each(function(idx, playerLi){
		$(playerLi).trigger("click");
	});
}

function onClick_liveRecordAll()
{
	$("[player-button-liveId]").each(function(idx, playerLi){
		$(playerLi).trigger("click");
	});
}
</script>



</head>
<body>

<form id="frmRecordFileParams">
	<input type="hidden" name="sportsEventCode" value="${loginUser.sportsEventCode}" />	
	<input type="hidden" name="camId" value="" />
	<input type="hidden" name="page" value="0" />
	<input type="hidden" name="rows" value="5" />
</form>

<!-- skip navi -->
<div id="skiptoContent">
	<a href="#gnb">주메뉴 바로가기</a>
	<a href="#contents">본문내용 바로가기</a>
</div>
<!-- //skip navi -->

<div id="wrapper">	
<!-- header -->
<jsp:include page="/include/top">
	<jsp:param value="hls" name="mainMenu"/>
	<jsp:param value="hlsManage" name="subMenu"/>
</jsp:include>
<!-- //header -->


<!-- container -->
<div id="container">
	<div id="contentsWrap">
	
		<!-- lnbWrap -->
		<div id="lnbWrap" class="video">
			<div class="lnbWraph2">
				<h2>영상녹화</h2>
			</div>
			<form id="frmRecordData">
				<input type="hidden" name="recordUserId" value="${loginUser.userId}" />
				<c:choose>
					<c:when test="${loginUser.isAdmin == true or loaginUser.isDeveloper == true or loginUser.userType == 'Admin'}">
						<div class="">
							<select id="sportsEvent" class="selectyze psa" name="sportsEventCode">
								<option value="">스포츠종목</option>
								<c:forEach items="${sprotsEvents}" var="sprotsEvent">
									<c:choose>
										<c:when test="${loginUser.sportsEventCode == sprotsEvent.codeId}">
											<option value="${sprotsEvent.codeId}" selected >${sprotsEvent.name}</option>
										</c:when>
										<c:otherwise>
											<option value="${sprotsEvent.codeId}" >${sprotsEvent.name}</option>
										</c:otherwise>
									</c:choose>
									
								</c:forEach>
							</select>
						</div>
				</c:when>
					<c:otherwise>
						<input type="hidden" id="sportsEvent" name="sportsEventCode" value="${loginUser.sportsEventCode}"/>
					</c:otherwise>
				</c:choose>
				
			</form>
			
			<div id="cameraBtn" data-ctrl-view="camera_list" 
				data-event-enableCamera="callback_addPlayer"
				data-event-disableCamera="callback_removePlayer">
				
			</div>
			<!-- 캔버스 생성 버튼을 만들어봅세다.. 20211213 -->
			<div id="canvasBtn" onclick="addCanvas();">캔버스</div>
			
			<div class="btnbox alignC" style="text-align: center;">
				<span class="btn_typeA t1 mgb10"><a href="javascript:onClick_recordAll();">전체녹화</a></span>
				<span class="btn_typeA t2 mgb10"><a href="javascript:onClick_stopRecordAll();">전체스톱</a></span>
				<span class="btn_typeA t4 mgb10"><a href="javascript:onClick_backRecordAll();">전체뒤로</a></span>
				<span class="btn_typeA t4"><a href="javascript:onClick_liveRecordAll();">전체라이브</a></span>
			</div> 
		</div>
		<!-- //lnbWrap -->
		

		<!-- contents -->
		<div id="contents" class="video">

			<h3>영상녹화</h3>
	
			<div class="cameraBox">
				<ul id="playerList" class="camera1pt"
					data-ctrl-view="live_player_list"
					
					data-event-initilizePlayerButton="callback_initPlayerButton"
					data-event-record="onClick_record"
					data-event-dvrRecord="onClick_dvrRecord"
					data-event-stopRecord="onClick_stopRecord"
					data-event-liveRecord="onClick_liveRecord"
					data-event-backRecord="onClick_backRecord"
					
					data-event-expendRecordFiles="onExpend_recordFiles"
					data-event-selectedRecordFile="onSelected_recordFile"
					data-event-remove="callback_removeCamera"
					data-event-disableCamera="callback_removePlayer">
					
				</ul>
	
			</div>

		</div>
		<!-- //contents -->
	</div>
</div>
<!-- //container -->


<!-- footer -->
<jsp:include page="/include/footer" />
<!-- //footer -->
</div>
<%--20211215 --%>
<div id="styleBtn" style="display: flex; position:absolute; 
	top: 60px; right: 0px; z-index: 4;">
	<button style="margin-right:10px" id="drawing"
	onclick="drawCanvas()">draw</button>
	<button style="margin-right:10px" id="shape"
	onclick="drawShape()">shape</button>
	<button style="margin-right:10px" id="eraser"
	onclick="eraseCanvas()">eraser</button>
	<button style="margin-right:10px" id="clear"
	onclick="clrCanvas(document.getElementById('canvas'))">clear</button>
	<button style="margin-right:10px" id="color"
	onclick="changeColor('blue')">color</button>
	<button style="margin-right:10px" id="thickness"
	onclick="changeWidth(10)">thickness</button>
	<button style="margin-right:10px" id="close"
	onclick="delCanvas($('#canvas'))">close</button>
</div>

</body>
</html>
