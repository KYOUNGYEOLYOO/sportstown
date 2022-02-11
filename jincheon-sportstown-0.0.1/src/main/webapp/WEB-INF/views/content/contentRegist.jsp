<!doctype html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page session="false" %>
<jsp:useBean id="now" class="java.util.Date" />
<jsp:useBean id="ipFilter" class="com.bluecapsystem.cms.jincheon.sportstown.common.define.IPFilterConstant" />

<html lang="ko" xml:lang="ko">
<head>



<jsp:include page="/include/head"/>
<script type="text/javascript" src="<c:url value="/bluecap/jwplayer-7.11.3/jwplayer.js"/>"></script>
<script>jwplayer.key="/cDB/4s47uzPnRb3q/gtzOze04a7CABARZuQWIwYvfQ=";</script>

<%-- <script type="text/javascript" src="<c:url value="/bluecap/jwplayer/jwplayer.js"/>"></script> --%>
<!-- <script>jwplayer.key="uL+sf8LV4JfO0X1+U8YPbC7PoiiNX730vh3pnQ==";</script> -->

<script type="text/javascript">

$(document).ready(function(){
	
	var params = $("#frmSearch").serialize();
	
	$("#fileList").jqGrid({
		// data: params,
		// datatype: "local",
		url: "<c:url value="/service/file/getFileList"/>?"+params,
		datatype: "json",
		mtype: "get",
	   	width: "auto",
	   	key: true,
		// height: "auto",
		height: 525,
		autowidth: true,
		viewrecords: true,
		rownumbers: false,
	   	// colNames:["사용자ID", "사용자명", "종목", "등록일자", "userId"],
	   	colNames:["파일명", "fileId", "filePath", "fileName"],
	   	colModel:[
	   		{name:"orignFileName",index:"orignFileName",align:"left"},
	   		{name:"fileId", index:"fileId", hidden:true},
	   		{name:"filePath", index:"filePath", hidden:true},
	   		{name:"fileName", index:"fileName", hidden:true}
	   	],
	   	// pager: $("#p_fileList"),
	   	jsonReader : {
	   		root : "files",
	   		id : "fileId"
	   	},
	   	onSelectRow : function(id, status){
	   		var file = $(this).getRowData(id);
	   		onSelected_fileList(file);
	   	}
	});
	
	
	init_searchCtrls();
	
});
</script>



<script type="text/javascript">

var videoTag;
var currentRate = 1;

function initPalyer(mediaUrl)
{
	jwplayer("player").stop();
	jwplayer("player").remove();
	
	jwplayer("player").setup({
		"file" : mediaUrl,
		"width" : "100%",
		aspectratio: "16:9",
		autostart : true
	});
	
	jwplayer("player").onReady(function() {
		// Slomo only works for HTML5 and ...
	    if (jwplayer().getRenderingMode() == "html5") {
	        videoTag = document.querySelector('video');
	        // ... browsers that support playbackRate
	        if(videoTag.playbackRate) 
	        {
	        	jwplayer("player").addButton("<c:url value="/resources/images/player/btn_slomo.170623.png"/>","Toggle Slow Motion", toggleSlomo,"slomo");
	        }
	    }
	    jwplayer("player").addButton("<c:url value="/resources/images/player/btn_slomo.170623.png"/>","Toggle Slow Motion", toggleSlomo,"slomo");
	});
	
	// jwplayer("player").play();
}

function toggleSlomo() {
	currentRate == 1 ? currentRate = 0.2: currentRate = 1;
    videoTag.playbackRate = currentRate;
    videoTag.defaultPlaybackRate = currentRate;
    if(navigator.userAgent.toLowerCase().indexOf('firefox') > -1){
        jwplayer("player").seek(jwplayer("player").getPosition());
    }
	return;
};
</script>


<script type="text/javascript">
function onSelected_fileList(file)
{
	console.log("login location ==> ${loginUser.connectLocation}");
	// var  mediaUrl = "${vodStreamer}${uploadRootUri}" + file.filePath + file.fileName + "/playlist.m3u8";
	var  mediaUrl = "${ipFilter.filterAddress(loginUser.connectLocation, vodStreamer)}${uploadRootUri}" + file.filePath + file.fileName + "/playlist.m3u8";
	
	console.log('test mediaUrl : '  + mediaUrl)
	console.log(file);
	
	initPalyer(mediaUrl);
	set_content(file);
} 	

function onClick_regist() {
	
	var param = $("#frmContent").serialize();
	console.log(param);
	
	$.ajax({
		url : "<c:url value="/service/content/registContent"/>",
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
				new bcs_messagebox().openError("컨텐츠등록", "컨텐츠등록 오류 발생 [code="+ajaxData.resultCode+"]", null);
			}
		}
	});

}

function onClick_download()
{
 	var fileId = $("#frmContent").find("[data-ctrl-contentMeta=fileId]").val();
 	$(location).attr("href", "<c:url value="/file/download"/>/" + fileId);
}

function onClick_reload()
{
	location.reload();
}


function onClick_searchFiles() {
	var params = $("#frmSearch").serialize();
	console.log(params);
	$("#fileList").jqGrid("setGridParam", {
		url: "<c:url value="/service/file/getFileList"/>?"+params
	});
	$("#fileList").trigger("reloadGrid");
}

function onClick_searchFilesInit()
{
	location.reload();
}

</script>


<script type="text/javascript">

function set_content(data)
{
	console.log('test-set_content : ')
	
	$.each(data, function(index, value){
		
		var input = $("#frmContent").find("[data-ctrl-contentMeta="+index+"]");
		
		if(input.length > 0)
		{
			input.val(value);
			console.log(index +"="+ value);
		}
	});
}

</script>


<script type="text/javascript">

function init_searchCtrls()
{
	$("#frmSearch").find("[name=sportsEventCode]").change(function(){
		var sportEventCode = $(this).val();
		$("#frmSearch").find("[name=camId]").children(":not(:first)").remove();
		$.ajax({
			url : "<c:url value="/service/camera/getAllCameras"/>/" + sportEventCode,
			async : false,
			dataType : "json",
			method : "post",
			beforeSend : function(xhr, settings ){},
			error : function (xhr, status, error){},
			success : function (ajaxData) {
				for(var i = 0; i < ajaxData.staticCameras.length; i++)
				{
					var $opt = $("<option/>").val(ajaxData.staticCameras[i].camId).text(ajaxData.staticCameras[i].name);
					$("#frmSearch").find("[name=camId]").append($opt);
				}
				
				
				for(var i = 0; i < ajaxData.shiftCameras.length; i++)
				{
					var $opt = $("<option/>").val(ajaxData.shiftCameras[i].camId).text(ajaxData.shiftCameras[i].name);
					$("#frmSearch").find("[name=camId]").append($opt);
				}
			}
		});
	}).trigger("change");
}

</script>


<script type="text/javascript">

$(document).ready(function(){
	
	$("#frmContent").find("[data-ctrl-contentMeta=contentUserNames]").click(function(){
		
		$("#frmSelectedUsers").empty();
		$("#frmContent").find("[data-ctrl-contentMeta=contentUserId]").each(function(){
			$("#frmSelectedUsers").append($("<input type='hidden' name='selectedUserIds' />").val($(this).val()));
		});
		
		var sportsEentCode = $("#frmContent").find("[name=sportsEventCode]").val();
		$("#frmUserSearch").find("[name=sportsEventCode]").val(sportsEentCode);
		var param = $("#frmUserSearch").serialize();
		
		console.log(param);
		$("[data-ctrl-view=user_select]").empty();
		$("[data-ctrl-view=user_select]").jqUtils_bcs_loadHTML(
				"<c:url value="/user/select"/>?" + param,
				false, "get", null, null
			);
		
	});
});


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
</script>

</head>
<body>

<!-- skip navi -->
<div id="skiptoContent">
	<a href="#gnb">주메뉴 바로가기</a>
	<a href="#contents">본문내용 바로가기</a>
</div>
<!-- //skip navi -->

<div id="wrapper">	
<!-- header -->
<jsp:include page="/include/top">
	<jsp:param value="regist" name="mainMenu"/>
	<jsp:param value="contentRegist" name="subMenu"/>
</jsp:include>
<!-- //header -->


<div title="사용자조회" class="bcs_dialog_hide" data-ctrl-view="user_select" data-event-selected="callback_selectedUsers" data-param-selectedUserId="frmSelectedUsers">
</div>

<form id="frmUserSearch">
	<input type="hidden" name="sportsEventCode"/>
</form>
<form id="frmSelectedUsers">
	<input type="hidden" 	name="selectedUserIds" value="" />
</form>

<!-- container -->
<div id="container">
	<div class="titleWrap">
		<h2>영상등록 - 녹화등록</h2>
		<div class="selectWrap">
		<!-- 	위치이동
			<c:choose>
				<c:when test="${loginUser.isAdmin == true or loaginUser.isDeveloper == true or loginUser.userType == 'Admin'}">					
					<select class="selectyze" name="sportsEventCode">						
						<option value="">운동종목</option>
						<c:forEach items="${sprotsEvents}" var="sprotsEvent">
							<c:set var="isSelected" value="" />
							<c:if test="${loginUser.sportsEventCode == sprotsEvent.codeId}">
								<c:set var="isSelected" value="selected" />
							</c:if>
							<option value="${sprotsEvent.codeId}" ${isSelected}>${sprotsEvent.name}</option>
						</c:forEach>
					</select>
				</c:when>
				<c:otherwise>
					<input type="hidden" name="sportsEventCode" value="${loginUser.sportsEventCode}"/>
				</c:otherwise>
			</c:choose>		
		<!-- 	//위치이동 -->
		</div>
	</div>
	<div id="contentsWrap">

		<!-- lnbWrap -->
		<div id="lnbWrap" class="searchContainer">
			<form id="frmSearch" onSubmit="return false;">
				<input type="hidden" name="fileTypeCode" value=""/>
				<input type="hidden" name="recordUserId" value="${loginUser.userId}" />
				<!-- <input type="hidden" name="locationRootCode" value="UPLOAD" /> -->
				<input type="hidden" name="locationRootCode" value="INGEST" />
				<input type="hidden" name="hasNotUsed" value="true" />
				<!-- 	위치이동	-->
				<c:choose>
					<c:when test="${loginUser.isAdmin == true or loaginUser.isDeveloper == true or loginUser.userType == 'Admin'}">
						<div class="">
						
							<select class="selectyze psa" name="sportsEventCode">
								
								<option value="">스포츠종목</option>
								<c:forEach items="${sprotsEvents}" var="sprotsEvent">
									<c:set var="isSelected" value="" />
									<c:if test="${loginUser.sportsEventCode == sprotsEvent.codeId}">
										<c:set var="isSelected" value="selected" />
									</c:if>
									<option value="${sprotsEvent.codeId}" ${isSelected}>${sprotsEvent.name}</option>
								</c:forEach>
							</select>
						</div>
					</c:when>
					<c:otherwise>
						<input type="hidden" name="sportsEventCode" value="${loginUser.sportsEventCode}"/>
					</c:otherwise>
				</c:choose>
				
				
<!-- 				<div class=""> -->
<!-- 					<select class="selectyze psa" name="cameraType"> -->
<!-- 						<option value="">카메라유형</option> -->
<!-- 						<option value="Static">고정</option> -->
<!-- 						<option value="Shift">유동</option> -->
<!-- 					</select> -->
<!-- 				</div> -->

				<ul>				
					<li>
						<select class="selectyze" name="camId">
							<option value="">카메라목록</option>
							<c:forEach items="${shiftCameras}" var="camera">
								<option value="${camera.camId}" >${camera.name}</option>
							</c:forEach>
						</select>
					</li>

					<li>
						<p>촬영일</p>
						<div class="datepickerBox">
							<label for="registFromDate">From</label>
							<input type="text" id="recordFromDate" name="recordFromDate" class="inputTxt date" value="<fmt:formatDate value="${currentDate}" pattern="yyyy-MM-dd"/>"/>
						</div>
						<div class="datepickerBox">
							<label for="registToDate">To</label>
							<input type="text" id="recordToDate" name="recordToDate" class="inputTxt date" value="<fmt:formatDate value="${currentDate}" pattern="yyyy-MM-dd"/>"/>				
						</div>
					</li>
					<li>
						<label for="tit">파일명</label>
						<input type="text" class="inputTxt" name="tit" />
					</li>
				</ul>
			
			<!-- 버튼 검색, 초기화 시작 -->
			<div class="btnWrap">
				<a class="btn reset" href="javascript:onClick_searchFilesInit();">초기화</a>
				<a class="btn search" href="javascript:onClick_searchFiles();">검색</a>
				
			</div>	
			<!-- 버튼 검색, 초기화 끝 -->
			
			</form>	
			
			

				
			
		</div>
		<!-- //lnbWrap -->

		<!-- contents -->
		<div id="contents">
			<div class="vodlistBox">
				<table id="fileList" class="list_type1" data-ctrl-view="file_list" data-event-selectedRow="onSelected_cameraListItem"></table>
				<div id="p_fileList" data-ctrl-view="file_list_pager"></div>
			</div>		
		</div>
		<div class="detailContainer">	
			<div class="vodregistBox">
				<div class="vodregist">
					<div class="videoview">
<!-- 						<video width="100%" controls src=""> -->
<!-- 							<source src="../../mp4/sample.mp4" type="video/mp4"> -->
<!-- 						</video> -->
						<div id="player" style="background:#fafafa"></div>	
					</div>
				<div class="detailWrap">					
					<form id="frmContent">
						<input type="hidden" name="contentType" value="VIDEO" />
						<dl>
							<dt>제목</dt>
							<dd class="full"><input type="text" name="title" value="" title="제목" class="inputTxt" data-ctrl-contentMeta="title" ></dd>
							<dt>종목</dt>
							<dd>
								<c:choose>
									<c:when test="${loginUser.isAdmin == true or loaginUser.isDeveloper == true or loginUser.userType == 'Admin'}">
										<select class="selectyze" name="sportsEventCode" title="스포츠종목">
											<option value="">선택하세요</option>
											<c:forEach items="${sprotsEvents}" var="code">
												<c:choose>
													<c:when test="${loginUser.sportsEventCode == code.codeId}">
														<option value="${code.codeId}" selected>${code.name}</option>
													</c:when>
													<c:otherwise>
														<option value="${code.codeId}" >${code.name}</option>
													</c:otherwise>
												</c:choose>
											</c:forEach>
										</select>
									</c:when>
									<c:otherwise>
										<input type="text" name="sportsEventName" class="inputTxt" value="${loginUser.sportsEvent.name}" readonly/>
										<input type="hidden" name="sportsEventCode" value="${loginUser.sportsEventCode}"/>
									</c:otherwise>
								</c:choose>								
								
							</dd>
							<dt class="ml20">소유자</dt>
							<dd>
								<input type="text" name="contentUserNames" title="소유자" class="inputTxt" data-ctrl-contentMeta="contentUserNames" value="${loginUser.userName}" readonly/>
								<input type="hidden" name="contentUsers[0].userId" data-ctrl-contentmeta="contentUserId" value="${loginUser.userId}">																		
							</dd>
							<dt>녹화자</dt>
							<dd>
								<select class="selectyze" name="recordUserId" title="녹화자">
									<option value="">선택하세요</option>
									<c:forEach items="${users}" var="user">
										<c:choose>
											<c:when test="${loginUser.userId == user.userId}">
												<option value="${user.userId}" selected >${user.userName}</option>
											</c:when>
											<c:when test="${loginUser.isAdmin == true or loaginUser.isDeveloper == true or loginUser.userType == 'Admin'}">
												<option value="${user.userId}" >${user.userName}</option>
											</c:when>
											<c:when test="${loginUser.sportsEventCode == user.sportsEventCode}">
												<option value="${user.userId}" >${user.userName}</option>
											</c:when>
										</c:choose>
									</c:forEach>
								</select>
							</dd>
							<dt class="ml20">녹화일자</dt>
							<dd>
								<div class="datepickerBox">									
									<input type="text" class="inputTxt date" name="recordDate"  value="<fmt:formatDate value="${currentDate}" pattern="yyyy-MM-dd"/>"/>										
								</div>
											
							</dd>
							<dt>설명</dt>
							<dd class="full">
								<textarea name="summary" title="설명" rows="5" data-ctrl-contentMeta="summary"></textarea>
							</dd>
							<dt>파일</dt>
							<dd class="full">
								<input type="text" name="instances[0].orignFileName" value="" data-ctrl-contentMeta="orignFileName" class="inputTxt" readonly>
								<input type="hidden" name="instances[0].fileId" value="" data-ctrl-contentMeta="fileId">																							
							</dd>						
						</dl>												
					</form>
					<div class="btnWrap">
						<a class="btn download" href="javascript:onClick_download();">다운로드</a>		
						<a class="btn reset" href="javascript:onClick_reload();">초기화</a></span>
						<div class="btnWrap">
							<a class="btn write" href="javascript:onClick_regist();">등록</a> 				
						</div>
					</div>				
				</div>
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
