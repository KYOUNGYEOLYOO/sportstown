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



<script type="text/javascript">

/* window.onload = maxWindow;

function maxWindow() {
    window.moveTo(0, 0);

    if (document.all) {
        top.window.resizeTo(screen.availWidth, screen.availHeight);
    }

    else if (document.layers || document.getElementById) {
        if (top.window.outerHeight < screen.availHeight || top.window.outerWidth < screen.availWidth) {
            top.window.outerHeight = screen.availHeight;
            top.window.outerWidth = screen.availWidth;
        }
    }
} */

function changeContainer(){
	var container = document.getElementById("container");
// 	container.style.width = window.screen.width;
// 	container.style.height = window.screen.height;
	if (!document.fullscreenElement) {
		container.webkitRequestFullscreen();
	  } else {
		document.webkitExitFullscreen();
	  }
}
</script>

<script type="text/javascript">

window.screen.orientation.onchange = function() {

	console.log("여기는 들어와지나??");
    if (this.type.startsWith('landscape')) {
      document.getElementById("container").webkitRequestFullscreen();
    } else {
      document.webkitExitFullscreen();
    }

};

function fullScreen(){
	if (!document.fullscreenElement) {
		container.webkitRequestFullscreen();
	  } else {
		document.webkitExitFullscreen();
	  }
}
</script>

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
	/* setTimeout(function(){
		$("#fullscreen").trigger("click");
	},4000); */
	/* window.screen.orientation.onchange(); */
	changeContainer();
	
	
	//	220214 add
	$(document).on('click', '#lnbWrap.video > p.toggle', function() {	        
		$(this).parent().toggleClass("menuopen");
	
	
});	
	
	
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

//	220214 edit
function change_player_layout()
{
	var cntPlayer = $("#playerList > li").length;
// 	var h = $(".cameraBox").height();
// 	var w = $(".cameraBox").width();
	
// 	console.log("h, w :", h,w);
	
	
	if(cntPlayer == 1)
	{
		$("#playerList").attr("class", "");
		$("#playerList").addClass("camera1pt");
	}else if(cntPlayer <= 2)	
	{
		$("#playerList").attr("class", "");
		$("#playerList").addClass("camera2pt");
	}else if(cntPlayer <= 4)	
	{
		$("#playerList").attr("class", "");
		$("#playerList").addClass("camera3pt");
	}else if(cntPlayer <=6){
		$("#playerList").attr("class", "");
		$("#playerList").addClass("camera4pt");		
	}else if(cntPlayer <=9){
		$("#playerList").attr("class", "");
		$("#playerList").addClass("camera5pt");		
	}else if(cntPlayer <=12){
		$("#playerList").attr("class", "");
		$("#playerList").addClass("camera5pt");		
		$("#playerList").addClass("camera12");		
	}else if(cntPlayer <=15){
		$("#playerList").attr("class", "");
		$("#playerList").addClass("camera5pt");		
		$("#playerList").addClass("camera15");			
	}else if(cntPlayer <=18){
		$("#playerList").attr("class", "");
		$("#playerList").addClass("camera5pt");		
		$("#playerList").addClass("camera18");			
	}else if(cntPlayer <=21){
		$("#playerList").attr("class", "");
		$("#playerList").addClass("camera5pt");		
		$("#playerList").addClass("camera21");			
	}else if(cntPlayer <=24){
		$("#playerList").attr("class", "");
		$("#playerList").addClass("camera5pt");		
		$("#playerList").addClass("camera24");			
	}else
	{
// 		$("#playerList").attr("class", "");
// 		$("#playerList").addClass("camera4pt");
		$("#playerList").attr("class", "");
		$("#playerList").addClass("camera10pt"); // 카메라 최대 24개 넘으면 수정 필요
	}
	
// 	$(".camera1pt").children('li').css({height:h, width:w});// 카메라 1개
// 	$(".camera2pt").children('li').css({width: 'calc(w/2)-5'});// 카메라 2개
// 	$(".camera3pt").children('li').css({height:h, width:w});// 카메라 3~4개
// 	$(".camera4pt").children('li').css({height:h, width:w});// 카메라 5~6개
// 	$(".camera5pt").children('li').css({height:h, width:w});// 카메라 7~개
	
	
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

function onClick_connectAll()
{
	console.log("connectAll");
	console.log("sportsEventCode ", $("#sportsEvent").val());
	var sportsEventCode = $("#sportsEvent").val();
	$.ajax({
		url : "<c:url value="/service/camera/connectStreamW/all"/>/" + sportsEventCode,
		async : false,
		dataType : "json",
		data : null, 
		method : "post",
		beforeSend : function(xhr, settings ){},
		error : function (xhr, status, error){},
		success : function (ajaxData) {
			location.reload();
			alert("연결 성공");
		}
	});
}
// var staticCameras;
// var camera;
// var camera1;

function onClick_disConnectAll()
{
	console.log("disconnectAll");
	console.log("sportsEventCode ", $("#sportsEvent").val());
	var sportsEventCode = $("#sportsEvent").val();
	$.ajax({
		url : "<c:url value="/service/camera/disconnectStreamW/all"/>/" + sportsEventCode,
		async : false,
		dataType : "json",
		data : null, 
		method : "post",
		beforeSend : function(xhr, settings ){},
		error : function (xhr, status, error){
			alert("disConnectAll Error");
		},
		success : function (ajaxData) {
			location.reload();
			alert("연결 해제 성공");
		}
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
<div id="container" class="full">
	<div class="titleWrap">
		<h2>영상녹화</h2>				
	</div>
	<div id="contentsWrap">
	
		<!-- lnbWrap -->
		<div id="lnbWrap" class="video">
			<p class="toggle"></p>
			<div class="btnContainer">
				<div class="btnWrap player">
					<a class="btn rec" href="javascript:onClick_recordAll();">Rec</a>
					<a class="btn stop" href="javascript:onClick_stopRecordAll();">Stop</a>
					<a class="btn back" href="javascript:onClick_connectAll();">연결</a>
					<a class="btn live" href="javascript:onClick_disConnectAll();">해제</a>
					 	
				</div> 	
			</div>		
			<form id="frmRecordData">
				<input type="hidden" name="recordUserId" value="${loginUser.userId}" />
				<c:choose>
					<c:when test="${loginUser.isAdmin == true or loaginUser.isDeveloper == true or loginUser.userType == 'Admin'}">
						<div class="selectWrap">
							<select id="sportsEvent" class="selectyze" name="sportsEventCode">
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

<!-- 			<div id="canvasTest" onclick="canvasTest()">캔버스테스트</div> -->
			

		</div>
		<!-- //lnbWrap -->
		

		<!-- contents -->
		<div id="contents" class="video">			
			<div class="cameraBox">
				<ul id="playerList"
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
				<p>좌측 메뉴바를 눌러<br />영상을 선택해주세요</p>	<!-- 영상 선택 전 노출 문구 또는 이미지 지정 -->	
			</div>
			<button class="fullScreen" id="fullscreen" onclick="fullScreen()" />
		</div>
		<!-- //contents -->
	</div>
</div>
<!-- //container -->


<!-- footer -->
<jsp:include page="/include/footer" />
<!-- //footer -->
</div>
</body>
</html>
