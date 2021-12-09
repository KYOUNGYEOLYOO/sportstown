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
.camera1pt li .videocontents { min-height:712px;}
.camera1pt li .video-js { width : 1266px; min-height:712px;}

.camera2pt li .videobox { width : 617px;}
.camera2pt li .videocontents { min-height:347px;}
.camera2pt li:nth-child(2n+1) {margin-left: 15px; margin-bottom:10px;}
.camera2pt li .video-js { width : 617px; min-height:347px;}

.camera3pt li { margin-bottom:10px; margin-left : 10px;}
.camera3pt li:nth-child(3n+1) { margin-left: 10px; margin-bottom:10px; }
.camera3pt li:nth-child(3n+2) { margin-left: 10px; margin-bottom:10px; }
.camera3pt li .videobox {width : 410px;}
.camera3pt li .videocontents { min-height:230px;}
.camera3pt li .video-js { width : 410px; min-height:230px;}

.videobox .videohead .videotitle { width : 255px; padding-left : 10px; }
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
var videoJsInterFace;
var hlsInfo;

$(document).ready(function(){
	/* videoJsInterFace = {
            player : null,
            id : null,
            init : function(id, isDelete = false){
                this.player = videojs(id, this.playerOption);
                this.id = id
                var obj = this
                var hlsObj = hlsInfo
                if (isDelete) {
                    this.player.on('dblclick', function(e) {
                        if (obj.doubleCall != null ) {
                            obj.doubleCall(obj.id);
                        }
                      
                    });
                }
            },
            changeLayout : function(id, width, height){
                console.log(id);
                this.player = videojs(id);
                this.player.width(width);
                this.player.height(height);
            },
            doubleCall : null,
            playerOption : {
                controls: true,
                autoplay: 'muted', // 크롬 정책상 사용자한테 볼륨잇는채로 자동재생되는건안나옴. mute나 클릭이벤트가 잇엇을시에만 재생됨
                preload: 'auto',
                width: '500',
                height: '300',
                liveui: true,
                sources: [{
                    src: "https://moctobpltc-i.akamaihd.net/hls/live/571329/eight/playlist.m3u8",
                  
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
                userActions: {
                    doubleClick: false,
                }
            }
        }
        hlsInfo ={
            playerObjs : null,
            players : [],
            initCount : 5,
            cntPlayer : 0,
            playerForm : $("#copy").children("video"),
            liveTime : 0,
            
            
            formObj: $("[data-hls-info-form]"),
            playerOption : {
                controls: true,
                autoplay: 'muted', // 크롬 정책상 사용자한테 볼륨잇는채로 자동재생되는건안나옴. mute나 클릭이벤트가 잇엇을시에만 재생됨
                preload: 'auto',
                width: '500',
                height: '300',
                liveui: true,
                sources: [{
                    src: "https://moctobpltc-i.akamaihd.net/hls/live/571329/eight/playlist.m3u8",
                    type: "application/x-mpegURL"
                }],
                controlBar: {
                    //playToggle: false,
                    pictureInPictureToggle: false,
                    currentTimeDisplay: true,
                    timeDivider: false,
                    durationDisplay: true,
                    remainingTimeDisplay: true,
                    volumePanel: {
                        inline: true
                    }

                },
                userActions: {
                    doubleClick: false,
                }
            },


            onCreateHls : function(camId){
            	console.log('onCreateHls');

                //var id = 'my_video' + this.cntPlayer++;
                var id = camId;
                var playerForm = this.playerForm.clone().attr("id", id)
                $("#playerList").append(playerForm);

                var videoObj = Object.create(videoJsInterFace);
                videoObj.doubleCall = this.setDblClick
                videoObj.init(id, true);
                this.players.push(videoObj)

                this.liveTime = videoObj.player.currentTime();
                this.changePlayerLayout();
            },
            setDblClick : function(id){
            	
                var hlsObj = hlsInfo;
                var obj = videojs(id);
               
                obj.dispose();
                
                $("ul.btnbox li[data-camera-camId="+id+"]").removeClass("on");
                hlsObj.changePlayerLayout(); 

            },
            onChangeLayout : function(){
                console.log(this.players);
                var videoObj = videoJsInterFace;
                for(var i=0; i< this.players.length; i++){
                    videoObj.changeLayout(this.players[i].id, 600, 400);

                }
            },
            changePlayerLayout: function () {
                var cntPlayer = $("#playerList > .video-js").length;
                console.log('cntPlayer : ' + cntPlayer);

                if (cntPlayer == 1) {
                    $("#playerList").attr("class", "video_content video_record");
                    $("#playerList").addClass("camera1pt");
                } else if (cntPlayer <= 4) {
                    $("#playerList").attr("class", "video_content video_record");
                    $("#playerList").addClass("camera2pt");
                } else {
                    $("#playerList").attr("class", "video_content video_record");
                    $("#playerList").addClass("camera3pt");
                }
            }


        }
 */


	
	$("ul.btnbox li").click(function(){
		var eventSender = new bcs_ctrl_event($("[data-ctrl-view=camera_list]"));	// bcs_ctrl_event 객체에  [data-ctrl-view=camera_list] html로 생성(선언된 함수의 prototype으로 생성)
		var camId = $(this).attr("data-camera-camId");								// 속성 camId 의 값을 가져옴
		
		if($(this).hasClass("on"))													// 클릭한 html의 속성중 class 값 on 검사, 
		{
			if(eventSender.send("data-event-disableCamera", camId))					// bcs_ctrl_event의 prototype 속성에 정의된 send함수 에 전송
				$(this).removeClass("on")
		}else
		{
			if(eventSender.send("data-event-enableCamera", camId))
				$(this).addClass("on");
		}
	});
});


function callback_addPlayer(sender, camId)
{
	console.log('callback_addPlayer');
	console.log(videojs.getPlayers());
	console.log(camId);
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
	console.log('callback_removePlayer : '+ camId);
	
	var $playerLi = $("[data-ctrl-view=live_player_list]").find("li[data-camera-camId="+camId+"]");
	$playerLi.remove();
	
	var deleveVideo = videojs('player_'+camId);
	deleveVideo.dispose();
	console.log(videojs.getPlayers());
	
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
}

function stopRecord(camId)
{
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
				<ul class="btnbox">

						<li class="btn_typeA t3 ex mgt5" data-camera-camid="Hls01" title="Hls01">Hls01</li>				
						<li class="btn_typeA t3 ex mgt5" data-camera-camid="Hls02" title="Hls02">Hls02</li>				
						<li class="btn_typeA t3 ex mgt5" data-camera-camid="Hls03" title="Hls03">Hls03</li>				
						<li class="btn_typeA t3 ex mgt5" data-camera-camid="Hls04" title="Hls04">Hls04</li>
						<li class="btn_typeA t3 ex mgt5" data-camera-camid="Hls05" title="Hls05">Hls05</li>				
						<li class="btn_typeA t3 ex mgt5" data-camera-camid="Hls06" title="Hls06">Hls06</li>				
						<li class="btn_typeA t3 ex mgt5" data-camera-camid="Hls07" title="Hls07">Hls07</li>				
						<li class="btn_typeA t3 ex mgt5" data-camera-camid="Hls08" title="Hls08">Hls08</li>
						
				</ul>
			</div>
			
			<div class="btnbox alignC" style="text-align: center;">
				<span class="btn_typeA t1 mgb10"><a href="javascript:onClick_recordAll();">전체녹화</a></span>
				<span class="btn_typeA t2"><a href="javascript:onClick_stopRecordAll();">전체스톱</a></span>
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

 <div id="copy" style="display: none;">
     <video
             id="my_"
             class="video-js"
     ></video>
 </div>

<!-- footer -->
<jsp:include page="/include/footer" />
<!-- //footer -->
</div>

</body>
</html>
