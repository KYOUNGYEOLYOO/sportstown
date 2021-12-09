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
<style type="text/css">


ul.camera1pt li {margin-bottom:10px;}
ul.camera1pt li .videobox {width:100%;}
ul.camera1pt li .videobox .videocontents { height:540px; background-color: #000; color: #FFF;}

ul.camera2pt li {margin-bottom:10px; }
ul.camera2pt li .videobox {width:460px;}
ul.camera2pt li .videobox .videocontents{ height:235px; width:460px; background-color: #000; color: #FFF;}

ul.camera3pt li {margin-bottom:10px; }
ul.camera3pt li .videobox {width:320px;}
ul.camera3pt li .videobox .videocontents{ height:170px; width:320px; background-color: #000; color: #FFF;}

ul.btnbox {margin-top : 5px;}
ul.btnbox:first-child {margin-top : 20px;}


.videohead .rec_status {
    float: left;
	margin-right: 10px;
    font-size: 16pt;
    line-height: 30px;
    color: #e4e4e4;
    /*
    	display: none;
    */
}


.videohead .media_list {
	float : left;
	margin-right: 10px;
}

.videobox .videohead .videotitle{
	width : 87%;
	line-height: 36px;
	padding-left: 10px;
	
}

.videobox .videohead .videotitle img {
	width : 33px;
	margin : 4px 4px 4px 0px;
} 

.videobox .videohead .videoicons { 
	padding : 4px 4px 4px 0px;
	width : 39px;
}

</style>


<style type="text/css">
.jw-controlbar { display : block !important; }
</style>

<script type="text/javascript" src="<c:url value="/bluecap/jwplayer-7.11.3/jwplayer.js"/>"></script>
<script>jwplayer.key="/cDB/4s47uzPnRb3q/gtzOze04a7CABARZuQWIwYvfQ=";</script>


<script type="text/javascript">
$(document).ready(function(){
	
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
}

function onClick_initMedia()
{
	location.reload();
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

var selectedFiles = new Array();


function callback_checkSelectedItem(sender, file)
{

	for(var i = 0; i < selectedFiles.length; i++)
	{
		if(file.fileId == selectedFiles[i].fileId)
			return true;
	}
	
	return false;
}

function callback_addFile(sender, file)
{
	
	var isSus = false;

	if(selectedFiles.length > 3)
	{
		return isSus;
	}else
	{
		
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

</script>


</head>
<body>

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


<div title="파일 검색" class="bcs_dialog_hide" data-ctrl-view="file_search" 
	data-event-selectedFile="callback_addFile"
	data-event-unselectedFile="callback_unselectedFile"
	
	data-event-checkSelectedFile="callback_checkSelectedItem"
></div>


<!-- container -->
<div id="container">
	<div id="contentsWrap">
	
		<!-- lnbWrap -->
		<div id="lnbWrap">
			<div class="lnbWraph2">
				<h2>영상녹화</h2>
			</div>
			<form id="frmFileSearch">
				<input type="hidden" name="recordUserId" value="${loginUser.userId}" />
				<c:choose>
					<c:when test="${loginUser.isAdmin == true or loaginUser.isDeveloper == true or loginUser.userType == 'Admin'}">
						<div class="">
							<select id="sportsEvent" class="selectyze psa" name="sportsEventCode">
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
			
			
			<div class="btnbox">
				<span class="btn_typeA t3 ex3"><a href="javascript:onClick_addMedia();">영상추가</a></span><!--수정-->
				<span class="btn_typeA t2 ex"><a href="javascript:onClick_initMedia();">초기화</a></span><!--수정-->
			</div>	
			
			
			
			<div class="btnbox mgt50">
				<a href="javascript:onClick_seekBack();">◀◀</a> 
				<a href="javascript:onClick_play();">재생</a> | 
				<a href="javascript:onClick_Pause();">일시정지</a>		
				<a href="javascript:onClick_seekAhead();">▶▶</a>
			</div>
			
		</div>
		<!-- //lnbWrap -->
		

		<!-- contents -->
		<div id="contents">

			<h3>녹화재생</h3>
	
			<div class="cameraBox">
				<ul id="playerList" class="camera1pt" 
					data-ctrl-view="vod_player_list"
					data-event-startRecord="startRecord" 
					data-event-stopRecord="stopRecord"
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

</body>
</html>
