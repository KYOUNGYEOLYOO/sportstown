<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page session="false" %>
<jsp:useBean id="now" class="java.util.Date" />
<jsp:useBean id="ipFilter" class="com.bluecapsystem.cms.jincheon.sportstown.common.define.IPFilterConstant" />

<link rel="stylesheet" href="<c:url value="/bluecap/css/video-js.css"/>"> 
<script src="https://unpkg.com/video.js/dist/video.js"></script>
<script type="text/javascript" src="<c:url value="/bluecap/jwplayer-7.11.3/jwplayer.js"/>"></script>
<script>jwplayer.key="/cDB/4s47uzPnRb3q/gtzOze04a7CABARZuQWIwYvfQ=";</script>
<style type="text/css">
.vjs-tech {width:auto; height:"39vh"}
</style>


<c:set var="contentMeta_video" value=""/>
<%-- <c:set var="vodStreamer" value=""/> --%>
<%-- <c:set var="contentRootUri" value=""/> --%>

<script type="text/javascript">

$(document).on('click', '.canvasMenuWrap > ul > li > p', function() {	        
	$(this).toggleClass("open");
});		
$(document).on('click', '.canvasMenuWrap > ul a', function() {	
	$(".canvasMenuWrap .eraser").removeClass("on");
	$(this).parent().siblings().removeClass("on");
	$(this).parent().addClass("on");
});
$(document).on('click', '.canvasMenuWrap .width a', function() {
// 	alert($(this).context.innerHTML);
	changeWidth($(this).context.innerHTML);
});
$(document).on('click', '.canvasMenuWrap .figure a', function() {
	drawShape($(this).context.innerHTML);
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
	$(".canvasMenuWrap").find("li").removeClass("on");
	delCanvas($("#canvas"),$("#canvasChange"));
	
	$("#player").attr('style','width:560;height:315;');
	$("#videoview").attr('style','height: 335px;padding: 20px 20px 0; position: relative;');
	
	
	
	document.webkitExitFullscreen();
});	


var videoTag;
var currentRate = 1;
var eventSender = new bcs_ctrl_event($("[data-ctrl-view=content_details]"));

//alert("111");
test();
//$(document).ready(function(){
//	alert("tttt");
//	test();
	
//});

// <c:set var="streamUrl" value=""/>
// <c:set var="streamFile" value=""/>

<c:forEach items="${contentMeta.content.instances}" var="instance" end="0">
	<c:set var="streamUrl" value="${vodStreamer}/${contentRootUri}"/>
	<c:set var="streamFile" value="${instance.file.fileName}"/>
</c:forEach>

function test(){
// 	alert("111");

	console.log("login location ==> ${loginUser.connectLocation}");
	console.log("contentRootUri ==> ${contentRootUri}");
	console.log("vodStreamer ==> ${vodStreamer}");
	console.log("streamUrl ==> ${streamUrl}");
	console.log("streamFile ==> ${streamFile}");
		//var  mediaUrl = "${vodStreamer}${contentRootUri}${contentMeta.content.instances[0].file.filePath}${contentMeta.content.instances[0].file.fileName}/playlist.m3u8"
		var  mediaUrl = "${ipFilter.filterAddress(loginUser.connectLocation, vodStreamer)}${contentRootUri}${contentMeta.content.instances[0].file.filePath}${contentMeta.content.instances[0].file.fileName}/playlist.m3u8"

		
		
// 		mediaUrl = "<c:url value="/resources/mp4/sample.mp4"/>";
// 		mediaUrl = "http://223.26.218.116:1935/vod/_definst_/mp4:./test/bcs.mp4/playlist.m3u8";
		
		console.log("mediaURL", mediaUrl);
		
		jwplayer("player").setup({
			"file" : mediaUrl,
			"width" : 560,
			"height" : 315,
			autostart : true
		});
		
		jwplayer("player").onReady(function() {
			// Slomo only works for HTML5 and ...
		    if (jwplayer().getRenderingMode() == "html5") {
		        videoTag = document.querySelector('video');
		        // ... browsers that support playbackRate
// 		        if(videoTag.playbackRate) 
// 		        {
// 		        	jwplayer("player").addButton("<c:url value="/resources/images/player/btn_slomo.170623.png"/>","Toggle Slow Motion", toggleSlomo,"slomo");
// 		        }
		    }
			this.addButton("<c:url value="/resources/images/player/btn_slomo.170623.png"/>","Toggle Slow Motion", toggleSlomo,"slomo");
			this.addButton("<c:url value="/resources/images/contents/btn_canvas.png"/>","canvas", startCanvas,"canvas");
		});	
		
}

$(document).ready(function(){
	
	
	$("#frmContent").find("[data-ctrl-contentMeta=contentUserNames]").click(function(){
		$("#frmSelectedUsers").empty();
	
		$("#frmContent").find("[data-ctrl-contentMeta=contentUserId]").each(function(){
			
			$("#frmSelectedUsers").append($("<input type='hidden' name='selectedUserIds' />").val($(this).val()));
		});
		
	
		var sportsEentCode = $("#frmContent").find("[name=sportsEventCode]").val();
		
		$("#frmUserSearch").find("[name=sportsEventCode]").val(sportsEentCode);
		var param = $("#frmUserSearch").serialize();
		
		$("[data-ctrl-view=user_select]").empty();
		$("[data-ctrl-view=user_select]").jqUtils_bcs_loadHTML(
				"<c:url value="/user/select"/>?" + param,
				false, "get", null, null
			);
		
	});
});


function onClick_download()
{
	
	var contentId = "${contentId}";
	
	registDownloadLog(contentId);
 	$(location).attr("href", "<c:url value="/file/download"/>/${contentMeta.content.instances[0].file.fileId}");
 	
}

function registDownloadLog(value){
	
	var param = $("#frmContent").serialize();
	console.log(param);
	
	$.ajax({
		url : "<c:url value="/service/content/registDownloadLog"/>",
		async : false,
		dataType : "json",
		data : param, 
		method : "post",
		beforeSend : function(xhr, settings ){},
		error : function (xhr, status, error){},
		success : function (ajaxData) {
			if(ajaxData.resultCode == "Success"){
				//location.reload();
			}
			else{
				//new bcs_messagebox().openError("???????????????", "??????????????? ?????? ?????? [code="+ajaxData.resultCode+"]", null);
			}
		}
	});
}

function onClick_modify()
{
	// ???????????? ???????????? ?????? ????????? ?????????...
	console.log("???????????? ???????????? ?????? ????????? ?????????...");
	var param = $("#frmContent").serialize();
	console.log(param);
	
	$.ajax({
		url : "<c:url value="/service/content/modifyContent"/>",
		async : false,
		dataType : "json",
		data : param, 
		method : "post",
		beforeSend : function(xhr, settings ){},
		error : function (xhr, status, error){},
		success : function (ajaxData) {
			if(ajaxData.resultCode == "Success"){
				location.reload();
			}
			else{
				new bcs_messagebox().openError("???????????????", "??????????????? ?????? ?????? [code="+ajaxData.resultCode+"]", null);
			}
		}
	});
}
function onClick_delete()
{
	var contentId = "${contentId}";
	if(typeof contentId == "undefined" || contentId == null)
	{
		new bcs_messagebox().open("????????????", "???????????? ????????? ?????????", null);
		return;
	}
	
	var mb = new bcs_messagebox().open("????????????", "?????? ???????????????????", null, {
		"??????" : function(){
			console.log("<c:url value="/service/content/deleteContent"/>/" + contentId);
			$.ajax({
// 				url : "<c:url value="/"/>/" + contentId,
				url : "<c:url value="/service/content/deleteContent"/>" ,
				async : false,
				dataType : "json",
// 				data : null,
				data : {
					contentId: contentId
				},
				method : "post",
				beforeSend : function(xhr, settings ){},
				error : function (xhr, status, error){},
				success : function (ajaxData) {
					if(ajaxData.resultCode == "Success"){
						console.log("ajaxData.resultCode : ", ajaxData.resultCode);
// 						$("#contentList").jqGrid("delRowData", ajaxData.contentId);
						eventSender.send("data-event-reloadList",contentId); // contentDetails?????? ????????? contentManage??? jqgrid selrow ??????
						mb.close();
					}else{
						new bcs_messagebox().openError("????????????", "????????? ????????? ?????? ?????? [code="+ajaxData.resultCode+"]", null);
					}
				}
			});
		},
		"??????" : function(){ mb.close(); }
	});
	
}


function callback_selectedUsers(sender, users)
{
	console.log(users);
	
	$("#frmContent").find("[data-ctrl-contentMeta=contentUserId]").remove();
	var userNames = "";
	
	for(var i = 0; i < users.length; i++)
	{
		$("#frmContent").append(
			$("<input type='hidden' name='contentUsers["+i+"].userId' data-ctrl-contentMeta='contentUserId' />").val(users[i].userId)
		);
		
		userNames += users[i].userName + ",";
	}
	
	$("#frmContent").find("[data-ctrl-contentMeta=contentUserNames]").val(userNames);
	
	return true;
}

function startCanvas(){

	$("#wrapper").addClass("canvasopen");
	
	

	var container = document.getElementById("wrapper");

	if (!document.fullscreenElement) {
		
		addCanvas2();
		
		$("#player").attr('style','width:100%;height:100%;');
		$("#videoview").attr('style','height: 100%;padding: 20px 20px 0; position: relative;');
		container.webkitRequestFullscreen();
	} else {
		$("#player").attr('style','width:560;height:315;');
		$("#videoview").attr('style','height: 335px;padding: 20px 20px 0; position: relative;');
		
		
		
		document.webkitExitFullscreen();
	}
	
	
}


document.addEventListener('fullscreenchange', exitHandler);
document.addEventListener('webkitfullscreenchange', exitHandler);
document.addEventListener('mozfullscreenchange', exitHandler);
document.addEventListener('MSFullscreenChange', exitHandler);

function exitHandler() {
    if (!document.fullscreenElement && !document.webkitIsFullScreen && !document.mozFullScreen && !document.msFullscreenElement) {
    	$('.canvasMenuWrap > p.close').trigger("click");
    }
}  
</script>

	<div id="wrapper">
<!-- 		<div id="container2" style="width:100%;height:100%;"> -->
			<div class="videoview" id="videoview" >
				<div id="player" style="background:#fafafa"></div>	
			</div>
			
			<!-- 	canvas -->
			<div class="canvasContainer">
				<div class="canvasWrap" id="canvasWrap">
					<div class="canvasMenuWrap" >
						<ul>
							<li class="figure">
								<p>??????</p>
								<ul>
									<li class="grid"><a href="#">??????</a></li>
									<li class="quadrangle"><a href="#">??????</a></li>
									<li class="circle"><a href="#">???</a></li>
									<li class="line"><a href="#">?????????</a></li>
								</ul>
							</li>
							<li class="color">
								<p>??????</p>
								<ul>
									<li class="blue"><a href="#">??????</a></li>
									<li class="red"><a href="#">??????</a></li>
									<li class="green"><a href="#">??????</a></li>
									<li class="black"><a href="#">??????</a></li>
									<li class="skiblue"><a href="#">?????????</a></li>				
								</ul>
							</li>
							<li class="width">
								<p>??????</p>
								<ul>
									<li class="thin"><a href="#">?????? ???</a></li>
									<li class="normal"><a href="#">??????</a></li>
									<li class="bold"><a href="#">????????? ???</a></li>
								</ul>
							</li>
							<li class="eraser">
								<a href="#">?????????</a>
							</li>		
							<li class="reset">
								<p>?????????</p>
							</li>				
						</ul>
						<p class="close">??????</p>
					</div>			
				</div>
<!-- 			</div> -->
		</div>		
		<!-- 	//canvas -->
			
	</div>
	
	<div title="???????????????" class="bcs_dialog_hide" data-ctrl-view="user_select" data-event-selected="callback_selectedUsers" data-param-selectedUserId="frmSelectedUsers">
	</div>
	
	<form id="frmUserSearch">
		<input type="hidden" name="sportsEventCode"/>
	</form>
	<form id="frmSelectedUsers">
		<input type="hidden" 	name="selectedUserIds" value="" />
	</form>
	
	
	<div class="detailWrap_hits">
		<form id="frmContent">
			<dl>
				<input type="hidden" name="contentId" value="${contentMeta.contentId}" />
				<dt>??????</dt>
				<dd class="full"><input type="text" name="title" title="??????" class="inputTxt" value="${contentMeta.title}" ></dd>
				<dt>??????</dt>
				<dd><input type="text" title="??????" class="inputTxt" value="${contentMeta.sportsEvent.name}" readonly></dd>
<%-- 				<dd><input type="text" name="sportsEventCode" title="??????" class="inputTxt" value="${contentMeta.sportsEvent.name}" readonly></dd> --%>
				<dd  style="display:none;">
					<input type="hidden" name="sportsEventCode" title="???????????????" class="inputTxt" value="${contentMeta.sportsEventCode}">
				</dd>
				
				<dt class="ml20">?????????</dt>
				<dd>
<%-- 					<input type="text" name="tagUser" title="?????????" class="inputTxt" value="${contentMeta.contentUserNames}" readonly> --%>
					<input type="text" name="contentUserNames" title="?????????" class="inputTxt" data-ctrl-contentMeta="contentUserNames" value="${contentMeta.contentUserNames}" readonly>
					
					<c:forEach items="${contentMeta.contentUsers}" var="contentUser" varStatus="status">
						<input type="hidden" name="contentUsers[${status.index}].userId" data-ctrl-contentmeta="contentUserId" value="${contentUser.userId}">
					</c:forEach>
				</dd>
				
				<dt>?????????</dt>
				<dd>
<%-- 					<input type="text" name=recordUser title="?????????" class="inputTxt" value="${contentMeta.recordUser.userName}" readonly> --%>
					<select class="inputTxt" name="recordUserId" title="?????????">
						<option value="">???????????????</option>
						<c:forEach items="${users}" var="user">
							<c:set var="isSelected" value=""/>
							<c:if test="${contentMeta.recordUserId == user.userId}">
								<c:set var="isSelected" value="selected"/>
							</c:if>
							<c:choose>
								<c:when test="${loginUser.userId == user.userId}">
									<option value="${user.userId}" ${isSelected}>${user.userName}</option>
								</c:when>
								<c:when test="${loginUser.isAdmin == true or loaginUser.isDeveloper == true or loginUser.userType == 'Admin'}">
									<option value="${user.userId}" ${isSelected}>${user.userName}</option>
								</c:when>
								<c:when test="${loginUser.sportsEventCode == user.sportsEventCode}">
									<option value="${user.userId}" ${isSelected}>${user.userName}</option>
								</c:when>
							</c:choose>
						</c:forEach>
					</select>	
				</dd>
				<dt class="ml20">????????????</dt>
				<dd>
					<div class="datepickerBox">
						<input type="text" id="recordFromDate" name="recordDate" class="inputTxt date"  value="<fmt:formatDate value="${contentMeta.recordDate}" pattern="yyyy-MM-dd" />" readonly/>
					</div>					
				</dd>
				<dt>??????</dt>
				<dd class="full"><textarea name="summary" title="??????" >${contentMeta.summary}</textarea></dd>
<!-- 				<dt>??????</dt> -->
<!-- 				<dd class="full"> -->
<!-- 					<input type="text" name="instances[0].orignFileName" value="" data-ctrl-contentMeta="orignFileName" class="inputTxt" readonly> -->
<!-- 					<input type="hidden" name="instances[0].fileId" value="" data-ctrl-contentMeta="fileId">						 -->
<!-- 				</dd>						 -->
				<dt>??????</dt>
				<dd class="full">
					<input name="tagInfo" type="text" value="${contentMeta.tagInfo}" title="??????" class="inputTxt" >
				</dd>
			</dl>
		</form>
		<dl style="padding-top: 0px;">
			<c:choose>
				<c:when test="${loginUser.isAdmin == true or loaginUser.isDeveloper == true or loginUser.userType == 'Admin'}">
					<dt>?????????</dt>
					<dd class="full">
						<input name="viewCount" type="text" value="${viewCount}" title="?????????" class="inputTxt" readonly>
					</dd>
				</c:when>
			</c:choose>
		</dl>
		<div class="btnWrap">
<!-- 			<a class="btn download">????????????</a>		 -->
			<a class="btn download" href="javascript:onClick_download();">????????????</a>		
<!-- 			<a class="btn write" href="javascript:onClick_auth();">????????????</a>		 -->
			<div class="btnWrap">
			<c:choose>
				<c:when test="${loginUser.isAdmin == true or loaginUser.isDeveloper == true or loginUser.userType == 'Admin'}">
					<a class="btn delete" href="javascript:onClick_delete();">??????</a>
					<a class="btn edit" href="javascript:onClick_modify();">??????</a>
				</c:when>
				<c:otherwise>
					<a class="btn edit" href="javascript:onClick_modify();">??????</a>
				</c:otherwise>
			</c:choose>			
			</div>
		</div>
	</div>

	
