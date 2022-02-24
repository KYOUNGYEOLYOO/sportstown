<!doctype html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page session="false" %>
<jsp:useBean id="now" class="java.util.Date" />
                                                      
<%-- 여기가 녹화된 video 실행시키는 페이지 20211210 --%>                                    
<html lang="ko" xml:lang="ko">                        
<head>                                                
                                                      
<jsp:include page="/include/head"/>                   


<script type="text/javascript" src="<c:url value="/bluecap/jwplayer-7.11.3/jwplayer.js"/>"></script>
<script>jwplayer.key="/cDB/4s47uzPnRb3q/gtzOze04a7CABARZuQWIwYvfQ=";</script>


<script type="text/javascript">
$(document).ready(function(){
	onClick_addMedia();
	
	$(document).on('click', '#lnbWrap.video > p.toggle', function() {	        
		$(this).parent().toggleClass("menuopen");
	});	

	// 220224 canvas
	$(document).on('click', '.player.cannvas', function() {	        
		$("#wrapper").addClass("canvasopen");
		addCanvas();
	});		
	$(document).on('click', '.canvasMenuWrap > ul > li > p', function() {	        
		$(this).toggleClass("open");
	});		
	$(document).on('click', '.canvasMenuWrap > ul a', function() {	
		$(".canvasMenuWrap .eraser").removeClass("on");
		$(this).parent().siblings().removeClass("on");
		$(this).parent().addClass("on");
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
		delCanvas($("#canvas"),$("#canvasChange"));
	});		
	
});

</script>


<script type="text/javascript">
function onClick_addMedia()
{
	var params = $("#frmFileSearch").serialize();
	$("[data-ctrl-view=file_search]").empty();
	$("[data-ctrl-view=file_search]").jqUtils_bcs_loadHTML(
			"<c:url value="/file/search"/>",
			false, "get", params, null
		);
	$(".ui-front").appendTo("#container");
}

function onClick_initMedia()
{
	location.reload();
}

//220218 edit
function change_player_layout()
{
	var cntPlayer = $("#playerList > li").length;
	
	if(cntPlayer == 1)
	{
		$("#playerList").attr("class", "");
		$("#playerList").addClass("camera1pt");
	}else if(cntPlayer <= 2)	
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

var selectedFiles = new Array();


function callback_checkSelectedItem(sender, fileId)
{
	for(var i = 0; i < selectedFiles.length; i++)
	{
		if(fileId == selectedFiles[i].fileId)
			return true;
	}
	
	return false;
}

function callback_initMedia(sender){
	location.reload();
}

function callback_addFile(sender, file)
{
	
	var isSus = false;

	if(selectedFiles.length > 3)
	{
		return isSus;
	}else
	{
		if(callback_checkSelectedItem(sender, file.fileId) == true)
			return true;
		
		$.ajax({
			url : "<c:url value="/mediaPlay/player"/>/" + file.fileId,
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
		
		if(isSus == true)
			selectedFiles.push(file);
	}
	
	return isSus;
}

function callback_unselectedFile(sender, file)
{
	for(var i = 0; i < selectedFiles.length; i++)
	{
		if(file.fileId == selectedFiles[i].fileId)
		{
			selectedFiles.splice(i, 1);
			
			$("[data-ctrl-view=vod_player_list]").find("li[data-vod-fileId="+file.fileId+"]").remove();
			change_player_layout();
			break;
		}
	}	
	return true;
}

</script>


<script type="text/javascript">

function onClick_play()
{
	var players = $(".jwplayer");
	
	var position = null;
	$.each(players, function(idx, player){
		var playerId = $(player).attr("id");
		if(idx == 0)
			position = jwplayer(playerId).getPosition();
		jwplayer(playerId).seek(position);
		jwplayer(playerId).play(true);
	});
	
}


function onClick_Pause()
{
	var players = $(".jwplayer");
	
	$.each(players, function(idx, player){
		var playerId = $(player).attr("id");
		jwplayer(playerId).pause();
		
		
	});
}


function onClick_seekBack()
{
	var players = $(".jwplayer");
	var position = null;
	$.each(players, function(idx, player){
		var playerId = $(player).attr("id");
		if(idx == 0)
		{
			position = jwplayer(playerId).getPosition();
			position -= 5;
		}
		jwplayer(playerId).seek(position);
		jwplayer(playerId).play(true);
	});
}

function onClick_seekAhead()
{
	var players = $(".jwplayer");
	var position = null;
	$.each(players, function(idx, player){
		var playerId = $(player).attr("id");
		if(idx == 0)
		{
			position = jwplayer(playerId).getPosition();
			position += 5;
		}
		jwplayer(playerId).seek(position);
		jwplayer(playerId).play(true);
	});
}

var currentRate = 1;

function onClick_slowModtion()
{
	
	var players = $(".jwplayer");
	var position = null;
	currentRate == 1 ? currentRate = 0.2: currentRate = 1;
	$.each(players, function(idx, player){
		var playerId = $(player).attr("id");
		var videoTag = player.querySelector("video");
		toggleSlomo(jwplayer(playerId), videoTag, currentRate);
	});
	
	
	
}

function toggleSlomo(player, videoTag, rate) {
	
    videoTag.playbackRate = rate;
    videoTag.defaultPlaybackRate = rate;
    if(navigator.userAgent.toLowerCase().indexOf('firefox') > -1){
    	player.seek(player.getPosition());
    }
	return;
}; 

function fullScreen(){
	if (!document.fullscreenElement) {
		container.webkitRequestFullscreen();
	  } else {
		document.webkitExitFullscreen();
	  }
}

</script>


</head>
<body>

<%-- 여기가 녹화된 video 실행시키는 페이지 
	html 콘솔창에 뜨는 부분
	20211210 --%>
<form id="frmRecordFileParams">
	<input type="hidden" name="sportsEventCode" value="${loginUser.sportsEventCode}" />	
	<input type="hidden" name="camId" value="" />
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
	<jsp:param value="mediaPlay" name="mainMenu"/>
</jsp:include>
<!-- //header -->


<div title="영상 검색" class="bcs_dialog_hide" data-ctrl-view="file_search" 
	data-event-selectedFile="callback_addFile"
	data-event-unselectedFile="callback_unselectedFile"
	
	data-event-checkSelectedFile="callback_checkSelectedItem"
	data-event-initMedia="callback_initMedia"
></div>


<!-- container -->
<div id="container" class="full">
	<div class="titleWrap">
		<h2>녹화재생</h2>				
	</div>	
	<div id="contentsWrap">
		<div id="lnbWrap" class="video">
			<p class="toggle"></p>
			<div class="btnContainer">
				<div class="btnWrap player">
					<a href="javascript:onClick_play();"><img src="<c:url value="/resources"/>/images/contents/play.png" alt="시작"></a>
					<a href="javascript:onClick_Pause();"><img src="<c:url value="/resources"/>/images/contents/stop.png" alt="정지"></a>
	
					<a href="javascript:onClick_slowModtion();">
						<img src="<c:url value="/resources"/>/images/contents/slowmo.png" alt="슬로우모션">
					</a>				
				</div>
			</div>
			<form id="frmFileSearch">
				<input type="hidden" name="recordUserId" value="${loginUser.userId}" />
				<c:choose>
					<c:when test="${loginUser.isAdmin == true or loaginUser.isDeveloper == true or loginUser.userType == 'Admin'}">
						<div class="selectWrap">
							<select id="sportsEvent" class="selectyze" name="sportsEventCode">
								<option value="">스포츠종목</option>
								<c:forEach items="${sprotsEvents}" var="sprotsEvent">
									<c:choose>
										<c:when test="${loginUser.sportsEventCode == sprotsEvent.codeId}">
											<option value="${sprotsEvent.codeId}" selected>${sprotsEvent.name}</option>
										</c:when>
										<c:otherwise>
											<option value="${sprotsEvent.codeId}">${sprotsEvent.name}</option>
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
		</div>
		<!-- contents -->
		<div id="contents" class="video">
			
			<!-- 
			<div>
				<span class="btn_typeA t3 video mgl10"><a href="javascript:onClick_addMedia();">영상추가</a></span>
				<span class="btn_typeA t2 video mgl5"><a href="javascript:onClick_initMedia();">초기화</a></span>
			</div>	
			 -->
			<!-- 
			<div class="fR playbtn">
<%-- 				<a href="javascript:onClick_seekBack();"><img src="<c:url value="/resources"/>/images/contents/prev.png" alt="이전"></a> --%>
				<a href="javascript:onClick_play();"><img src="<c:url value="/resources"/>/images/contents/play.png" alt="시작"></a>
				<a href="javascript:onClick_Pause();"><img src="<c:url value="/resources"/>/images/contents/stop.png" alt="정지"></a>
<%-- 				<a href="javascript:onClick_seekAhead();"><img src="<c:url value="/resources"/>/images/contents/next.png" alt="다음"></a> --%>
				<a href="javascript:onClick_slowModtion();">
					<img src="<c:url value="/resources"/>/images/contents/slowmo.png" alt="슬로우모션" style="width:43px; height:43px;">
				</a>
			</div>
			 -->
			<div class="cameraBox">
				<ul id="playerList"
					data-ctrl-view="vod_player_list"
					data-event-remove="callback_unselectedFile">
				</ul>	
				<p>영상을 선택해주세요</p>	<!-- 영상 선택 전 노출 문구 또는 이미지 지정 -->	
			</div>
			<button class="fullScreen" id="fullscreen" onclick="fullScreen()"></button>
			<a class="addMedia" href="javascript:onClick_addMedia();">영상추가</a>
		</div>
		<!-- 	canvas -->
		<div class="canvasContainer">
			<div class="canvasWrap" id="canvasWrap">
				<div class="canvasMenuWrap">
					<ul>
						<li class="figure">
							<p>도형</p>
							<ul>
								<li class="quadrangle"><a href="#">네모</a></li>
								<li class="circle"><a href="#">원</a></li>
								<li class="line"><a href="#">자유선</a></li>
							</ul>
						</li>
						<li class="color">
							<p>색깔</p>
							<ul>
								<li class="blue"><a href="#">파랑</a></li>
								<li class="red"><a href="#">빨강</a></li>
								<li class="green"><a href="#">초록</a></li>
								<li class="black"><a href="#">검정</a></li>
								<li class="skiblue"><a href="#">하늘색</a></li>				
							</ul>
						</li>
						<li class="width">
							<p>두께</p>
							<ul>
								<li class="thin"><a href="#">얇은 거</a></li>
								<li class="normal"><a href="#">보통</a></li>
								<li class="bold"><a href="#">두꺼운 거</a></li>
							</ul>
						</li>
						<li class="eraser">
							<a href="#">지우개</a>
						</li>		
						<li class="reset">
							<p>초기화</p>
						</li>				
					</ul>
					<p class="close">닫기</p>
				</div>			
			</div>
		</div>		
		<!-- 	//canvas -->
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
