<!doctype html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page session="false" %>
<jsp:useBean id="now" class="java.util.Date" />
<jsp:useBean id="ipFilter" class="com.bluecapsystem.cms.jincheon.sportstown.common.define.IPFilterConstant" />

<link rel="stylesheet" href="<c:url value="/bluecap/css/video-js.css"/>"> 
<script src="https://unpkg.com/video.js/dist/video.js"></script>
<script type="text/javascript" src="<c:url value="/bluecap/jwplayer-7.11.3/jwplayer.js"/>"></script>
<script>jwplayer.key="/cDB/4s47uzPnRb3q/gtzOze04a7CABARZuQWIwYvfQ=";</script>

<html lang="ko" xml:lang="ko">
<head>

<jsp:include page="/include/head"/>



<script type="text/javascript">
$(document).ready(function(){
	init_contentList();
	set_contentDetails(null);
	
	// 캔버스 기능 추가
// 	$(document).on('click', '.player.cannvas', function() {	        
// 		$("#wrapper").addClass("canvasopen");
// 		addCanvas();
// 	});		
	$(document).on('click', '.canvasMenuWrap > ul > li > p', function() {	        
		$(this).toggleClass("open");
	});		
	$(document).on('click', '.canvasMenuWrap > ul a', function() {	
		$(".canvasMenuWrap .eraser").removeClass("on");
		$(this).parent().siblings().removeClass("on");
		$(this).parent().addClass("on");
	});
	$(document).on('click', '.canvasMenuWrap .width a', function() {
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
	});		
});
</script>



<script type="text/javascript">


function startCanvas(){

	$("#wrapper").addClass("canvasopen");
	addCanvas();
}

function init_contentList()
{
// 	var customPageInfo = ""; // 페이지 정보를 나타낼 것인지 / boolean / 생략시 false
// 	var customPageInfoType = ""; // 페이지 정보의 종류
// 	var pageCount = 10; // 한 페이지에 보여줄 페이지 수 (ex:1 2 3 4 5)
	
	var eventSender = new bcs_ctrl_event($("#contentList"));
	console.log("<c:url value="/service/content/getContents"/>?" + $("#frmSearch").serialize());
	
	$("#sportsEventCodeSelect").change(function(){
		console.log("값변경 테스트:" + $(this).val());
		$("#frmSearch").find("[name=sportsEventCode]").val($(this).val());
		console.log("111 :",$("#frmSearch").find("[name=sportsEventCode]").val());

	});
	$("#contentList").jqGrid({
		// data: mydata,
		url : "<c:url value="/service/content/getContents"/>?" + $("#frmSearch").serialize(),
		datatype: "json",
		mtype: "get",
	   	width: 800,
// 	   	width: "auto",
	   	key: true,
	   	viewsortcols: [false,'vertical',false],
		// height: "auto",
		height: 640,
// 		autowidth: true,
		viewrecords: true,
		rownumbers: false,
		rowNum: 8,
		rowList: [8,20,30],
// 	   	colNames:["썸네일", "스포츠종류", "제목", "촬영자", "촬영일자", "등록일자", "contentId"],
// 	   	colNames:["썸네일", "스포츠종류", "제목", "촬영일자 / 등록일자", "등록일자","contentId"],
		colNames:["썸네일", "스포츠종류", "스포츠 종류", "제목", "촬영자", "촬영일자", "촬영일자 / 등록일자", "등록일자","contentId"],
	   	colModel:[
			{name:"thumbnail",index:"thumbnail", width:70,align:"center", 
				formatter: function (cellvalue, options, rowObject) {
					console.log(rowObject);
					return "<img src='<c:url value="/content/thumbnail"/>/"+rowObject.contentId+"' height='100%' width='100%'/>";
				}
			},
			{name:"sportsEvent.name",index:"sportsEventName", width:80, align:"left", hidden:true},
			{name:"sportsEvent2",index:"sportsEvent2", width:80, align:"left",
				formatter: function (cellvalue, options, rowObject) {
					return rowObject.sportsEvent.name + "\n\n" + rowObject.recordUser.userName;
				}
			},
			{name:"title",index:"title", width:180, align:"left",
				formatter: function (cellvalue, options, rowObject) {
					
					var tag = rowObject.tagInfo==null?"":rowObject.tagInfo
					
					return rowObject.title + "\n\n" + tag;
				}
			},
			{name:"recordUser.userName", index:"recordUserName", width:100, align:"center", hidden:true},
			{name:"formatedRecordDate2", index:"formatedRecordDate2", width:200, align:"center",
				formatter: function (cellvalue, options, rowObject) {
					
						var registDate = rowObject.content.registDate.substring(0,19);
						return rowObject.formatedRecordDate + "\n\n" + registDate;
					}
				},
			{name:"formatedRecordDate", index:"formatedRecordDate", width:200, align:"center",hidden:true},
			{name:"content.registDate", index:"registDate", width:100, align:"center",hidden:true},
			{name:"contentId", index:"contentId", hidden:true}
		],
// 		pager: $("#p_contentList"),
		loadComplete : function(data){  
		    
		    // 그리드 데이터 총 갯수
		    var allRowsInGrid = jQuery('#contentList').jqGrid('getGridParam','records');
		   
		    // 데이터가 없을 경우 (먼저 태그 초기화 한 후에 적용)
		    $("#NoData").html("");
		    if(allRowsInGrid==0){
		        $("#NoData").html("<br>데이터가 없습니다.<br>");
		    }
		    // 처음 currentPage는 널값으로 세팅 (=1)
		    initPage("contentList",allRowsInGrid,"");
		   
		},
		jsonReader : {
			root : "contents",
			id : "contentId",
			registDate : "registDate"
		},
		onSelectRow : function(registDate){
			// 0209 onClick_detail();
			onClick_detail_video(registDate);
		}
	});
}

//그리드 페이징
function initPage(gridId,totalSize,currentPage){
   
    // 변수로 그리드아이디, 총 데이터 수, 현재 페이지를 받는다
    if(currentPage==""){
        var currentPage = $('#'+gridId).getGridParam('page');
    }
    // 한 페이지에 보여줄 페이지 수 (ex:1 2 3 4 5)
    var pageCount = 10;
    // 그리드 데이터 전체의 페이지 수
    var totalPage = Math.ceil(totalSize/$('#'+gridId).getGridParam('rowNum'));
    // 전체 페이지 수를 한화면에 보여줄 페이지로 나눈다.
    var totalPageList = Math.ceil(totalPage/pageCount);
    // 페이지 리스트가 몇번째 리스트인지
    var pageList=Math.ceil(currentPage/pageCount);
    
    console.log("totalPage :", totalPage);
    console.log("pageCount :", pageCount);
    console.log("totalPageList :",totalPageList);
    console.log("PageList :",pageList);
    
   
    // 페이지 리스트가 1보다 작으면 1로 초기화
    if(pageList<1) pageList=1;
    // 페이지 리스트가 총 페이지 리스트보다 커지면 총 페이지 리스트로 설정
    if(pageList>totalPageList) pageList = totalPageList;
    // 시작 페이지
    var startPageList=((pageList-1)*pageCount)+1;
    // 끝 페이지
    var endPageList=startPageList+pageCount-1;
   
   
    // 시작 페이지와 끝페이지가 1보다 작으면 1로 설정
    // 끝 페이지가 마지막 페이지보다 클 경우 마지막 페이지값으로 설정
    if(startPageList<1) startPageList=1;
    if(endPageList>totalPage) endPageList=totalPage;
    if(endPageList<1) endPageList=1;
   
    // 페이징 DIV에 넣어줄 태그 생성변수
    var pageInner="";
   
    // 페이지 리스트가 1이나 데이터가 없을 경우 (링크 빼고 흐린 이미지로 변경)
    if(pageList<2){
       
       
    }
    // 이전 페이지 리스트가 있을 경우 (링크넣고 뚜렷한 이미지로 변경)
    if(pageList>1){
       
        pageInner+="<a class='btn first' href='javascript:firstPage()'></a>";
        pageInner+="<a class='btn pre' href='javascript:prePage("+totalSize+")'></a>";
    }
    // 페이지 숫자를 찍으며 태그생성 (현재페이지는 강조태그)
    for(var i=startPageList; i<=endPageList; i++){
        if(i==currentPage){
            pageInner = pageInner +"<a href='javascript:goPage("+(i)+")' id='"+(i)+"'><strong>"+(i)+"</strong></a> ";
        }else{
            pageInner = pageInner +"<a href='javascript:goPage("+(i)+")' id='"+(i)+"'>"+(i)+"</a> ";
        }
       
    }
   
    // 다음 페이지 리스트가 있을 경우
    if(totalPageList>pageList){
       
        pageInner+="<a class='btn next' href='javascript:nextPage("+totalSize+")'></a>";
        pageInner+="<a class='btn last' href='javascript:lastPage("+totalPage+")'></a>";
    }
    // 현재 페이지리스트가 마지막 페이지 리스트일 경우
    if(totalPageList==pageList){
       
    }  
    // 페이징할 DIV태그에 우선 내용을 비우고 페이징 태그삽입
    $("#paginate").html("");	//220222 test
    $("#paginate").append(pageInner);	//220222 test
   
}


//그리드 첫페이지로 이동
function firstPage(){
       
        $("#contentList").jqGrid('setGridParam', {
                            page:1
                        }).trigger("reloadGrid");
       
}
// 그리드 이전페이지 이동
function prePage(totalSize){
       
        var currentPage = $('#contentList').getGridParam('page');
        var pageCount = 10;
       
        currentPage-=pageCount;
        pageList=Math.ceil(currentPage/pageCount);
        currentPage=(pageList-1)*pageCount+pageCount;
       
        initPage("contentList",totalSize,currentPage);
       
        $("#contentList").jqGrid('setGridParam', {
                            page:currentPage
                        }).trigger("reloadGrid");
       
}


// 그리드 다음페이지 이동    
function nextPage(totalSize){
       
        var currentPage = $('#contentList').getGridParam('page');
        var pageCount = 10;
       
        currentPage+=pageCount;
        pageList=Math.ceil(currentPage/pageCount);
        currentPage=(pageList-1)*pageCount+1;
       
        initPage("contentList",totalSize,currentPage);
       
        $("#contentList").jqGrid('setGridParam', {
                            page:currentPage
                        }).trigger("reloadGrid");
}
// 그리드 마지막페이지 이동
function lastPage(totalSize){
       
        $("#contentList").jqGrid('setGridParam', {
                            page:totalSize
                        }).trigger("reloadGrid");
}
// 그리드 페이지 이동
function goPage(num){
       
        $("#contentList").jqGrid('setGridParam', {
                            page:num
                        }).trigger("reloadGrid");
       
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

function onClick_search()
{
	
	$("#contentList").jqGrid("setGridParam", {
		url : "<c:url value="/service/content/getContents"/>?" + $("#frmSearch").serialize(),
		page : 1
	});
	$("#contentList").trigger("reloadGrid");
	
}

function onClick_searchInit()
{
	location.reload();
}

function onClick_detail()
{
	var contentId = $("#contentList").jqGrid("getGridParam", "selrow");
	if(typeof contentId == "undefined" || contentId == null)
	{
		new bcs_messagebox().open("영상검색", "컨텐츠를 선택해 주세요", null);
		return;
	}
	
	
	window.open("<c:url value="/content/detail"/>/" + contentId, "popup", "height=930,width=910,resizable=no,menubar=no,toolbar=no", true);
	
}

function onClick_detail_video(registDate)
{
	
	var authChk = "S";
	
	if("${loginUser.userType}" != 'Admin'){
			if("${loginUser.sportsEvent.isPartition}" == 'true'){
				
				var authFromDate = "${loginUser.authFromDate}";
				var authToDate = "${loginUser.authToDate}";
			
				
				if(authFromDate == '' && authToDate == ''){
					var contentId = $("#contentList").jqGrid("getGridParam", "selrow");
					var userId = "${loginUser.userId}"
					
					
					$.ajax({
						url : "<c:url value="/service/contentAuth/getContentAuth"/>/" + contentId +"/"+ userId+"/approval",
						async : false,
						dataType : "json",
						data : null, 
						method : "post",
						beforeSend : function(xhr, settings ){},
						error : function (xhr, status, error){},
						success : function (ajaxData) {
							console.log("====");
							console.log(ajaxData);
							if(ajaxData.resultCode == "Success"){
								
								
								if(ajaxData.contentAuth != null){
									var today = new Date();
									
									var year = today.getFullYear();
									var month = ('0' + (today.getMonth() + 1)).slice(-2);
									var day = ('0' + today.getDate()).slice(-2);

									var dateString = year + '' + month  + '' + day;
									
									var fromDateTemp = ajaxData.contentAuth.fromDate.substring(0,10).replace(/-/gi, "");
									var toDateTemp = ajaxData.contentAuth.toDate.substring(0,10).replace(/-/gi, "");
									
									if(Number(fromDateTemp) <= Number(dateString) && Number(dateString) <= Number(toDateTemp)){
										
									}else{
										authChk = "E";
										requestAuth();
										return;
									}
								}else{
									authChk = "E";
									requestAuth();
									return;
								}
								
								
							}else{
								new bcs_messagebox().openError("승인 상세조회", "승인 조회중 오류 발생 [code="+ajaxData.resultCode+"]", null);
							}
						}
					});
					
					
				}else{

					
					var today = registDate;   

					var todayTemp = today.substring(0,8);
					
					var authFromDateTemp = authFromDate.substring(0,10).replace(/-/gi, "");
					var authToDateTemp = authToDate.substring(0,10).replace(/-/gi, "");

					
					console.log(todayTemp);
					console.log(authFromDateTemp);
					console.log(authToDateTemp);
					
					if(Number(authFromDateTemp) <= Number(todayTemp) && Number(todayTemp) <= Number(authToDateTemp)){
						
					}else{
						
						var contentId = $("#contentList").jqGrid("getGridParam", "selrow");
						var userId = "${loginUser.userId}"
						
						
						$.ajax({
							url : "<c:url value="/service/contentAuth/getContentAuth"/>/" + contentId +"/"+ userId+"/approval",
							async : false,
							dataType : "json",
							data : null, 
							method : "post",
							beforeSend : function(xhr, settings ){},
							error : function (xhr, status, error){},
							success : function (ajaxData) {
								console.log("====");
								console.log(ajaxData);
								if(ajaxData.resultCode == "Success"){
									
									
									if(ajaxData.contentAuth != null){
										var today = new Date();
										
										var year = today.getFullYear();
										var month = ('0' + (today.getMonth() + 1)).slice(-2);
										var day = ('0' + today.getDate()).slice(-2);

										var dateString = year + '' + month  + '' + day;
										
										var fromDateTemp = ajaxData.contentAuth.fromDate.substring(0,10).replace(/-/gi, "");
										var toDateTemp = ajaxData.contentAuth.toDate.substring(0,10).replace(/-/gi, "");
										
										if(Number(fromDateTemp) <= Number(dateString) && Number(dateString) <= Number(toDateTemp)){
											
										}else{
											authChk = "E";
											requestAuth();
											return;
										}
									}else{
										authChk = "E";
										requestAuth();
										return;
									}
									
									
								}else{
									new bcs_messagebox().openError("승인 상세조회", "승인 조회중 오류 발생 [code="+ajaxData.resultCode+"]", null);
								}
							}
						});

					}
				}
			}
	}
	
	
	
	var contentId = $("#contentList").jqGrid("getGridParam", "selrow");
	if(typeof contentId == "undefined" || contentId == null)
	{
		new bcs_messagebox().open("영상검색", "컨텐츠를 선택해 주세요", null);
		return;
	}
	
	if(authChk == "E"){
		return false;
	}
	
	console.log("contentId : ", contentId);
	$("#frmContentDetails").empty();
	$("#frmContentDetails").jqUtils_bcs_loadHTML(
			"<c:url value="/content/detail"/>/" + contentId + "/video",
			false, "get", null, null
		);
	console.log('set_contentDetails');
// 		$.ajax({
// 			url : "<c:url value="/content/detail"/>/" + contentId +"/video",
// 			async : false,
// 			dataType : "json",
// 			data : null, 
// 			method : "post",
// 			success : function (ajaxData) {
// 			if (ajaxData.resultCode == "Success"){
// 					console.log("1111111111");
// 					console.log("ajaxData : ", ajaxData.contentMeta);

// 					contentMeta_video = ajaxData.contentMeta;
// 					vodStreamer = ajaxData.vodStreamer;
// 					contentRootUri = ajaxData.contentRootUri;
// 					test();
// 				}else{
// 					console.log("2222222222");
// 				}
// 			},
// 			error : function(request,error){
// 				console.log("why????");
// 				console.log("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
// 			}
// 		});		
}

function requestAuth(){
	var mb = new bcs_messagebox().open("영상검색 - 권한 문제", "권한 요청 하시겠습니까?", null, {
		"확인" : function(){
			onClick_auth();
			mb.close();
		},
		"닫기" : function(){ mb.close(); }
	});
	
}

function set_contentDetails(contentId)
{
	$("#frmContentDetails").empty();
	$("#frmContentDetails").jqUtils_bcs_loadHTML(
			"<c:url value="/content/detail"/>/" + contentId +"/video",
			false, "get", null, null
		);
	console.log('set_cameraDetail1111');
	console.log("/content/detail/" + contentId +"/video");
	
	/*
	console.log(camera);
	$("#frmCameraDetail").find("input[name=name]").val(camera.name);
	$("#frmCameraDetail").find("input[name=camId]").val(camera.camId);
	*/	
}

function clear_contentDetails()
{
	$("#frmContentDetails").find("input").val("");
}

function onClick_modify()
{
}

function callback_reloadList(contentId)
{
	$("#contentList").jqGrid("delRowData", contentId);
	$("#contentList").jqGrid('setGridParam', {
        page:$("#contentList").getGridParam("page")
    }).trigger("reloadGrid");
	
}

function onClick_auth(){
	
	var contentId = $("#contentList").jqGrid("getGridParam", "selrow");
	var userId = "${loginUser.userId}";
	if(typeof contentId == "undefined" || contentId == null)
	{
		new bcs_messagebox().open("영상검색", "컨텐츠를 선택해 주세요", null);
		return;
	}
	
	
	$("[data-ctrl-view=content_auth]").empty();
	$("[data-ctrl-view=content_auth]").jqUtils_bcs_loadHTML(
			"<c:url value="/contentAuth/reqAuth"/>/" + contentId+"/"+ userId,
			false, "get", null, null
		);		
}

function onClick_delete()
{
	var contentId = $("#contentList").jqGrid("getGridParam", "selrow");
	if(typeof contentId == "undefined" || contentId == null)
	{
		new bcs_messagebox().open("영상검색", "컨텐츠를 선택해 주세요", null);
		return;
	}
	
	var mb = new bcs_messagebox().open("영상검색", "삭제 하시겠습니까?", null, {
		"삭제" : function(){
			console.log("<c:url value="/"/>/" + contentId);
// 			$.ajax({
// 				url : "<c:url value="/"/>/" + contentId,
// 				async : false,
// 				dataType : "json",
// 				data : null, 
// 				method : "post",
// 				beforeSend : function(xhr, settings ){},
// 				error : function (xhr, status, error){},
// 				success : function (ajaxData) {
// 					if(ajaxData.resultCode == "Success"){
// 						$("#contentList").jqGrid("delRowData", ajaxData.contentId);
// 						mb.close();
// 					}else{
// 						new bcs_messagebox().openError("영상검색", "컨텐츠 삭제중 오류 발생 [code="+ajaxData.resultCode+"]", null);
// 					}
// 				}
// 			});
		},
		"닫기" : function(){ mb.close(); }
	});
	
}
</script>

<script type="text/javascript">

function callback_regist(sender, camera)
{
	$("#cameraList").jqGrid("addRowData", camera.camId, camera, "first");
	
	clear_cameraDetail();
	set_cameraDetail(camera.camId);
}

function callback_modify(sender, camera)
{
	$("#cameraList").jqGrid("setRowData", camera.camId, camera);
	
	clear_cameraDetail();
	set_cameraDetail(camera.camId);
}

</script>


<script type="text/javascript">
function set_cameraDetail(camId)
{
	$("#frmCameraDetail").empty();
	$("#frmCameraDetail").jqUtils_bcs_loadHTML(
			"<c:url value="/camera/detail"/>/" + camId,
			false, "get", null, null
		);
	
	/*
	console.log(camera);
	$("#frmCameraDetail").find("input[name=name]").val(camera.name);
	$("#frmCameraDetail").find("input[name=camId]").val(camera.camId);
	*/
	
}

function clear_cameraDetail()
{
	$("#frmCameraDetail").find("input").val("");
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


<div id="wrappers">	
<!-- header -->
<jsp:include page="/include/top">
	<jsp:param value="search" name="mainMenu"/>
	<jsp:param value="contentManage" name="subMenu"/>
</jsp:include>
<!-- //header -->
<div title="승인요청" class="bcs_dialog_hide" data-ctrl-view="content_auth" >
</div>
<!-- container -->
<div id="container">
	
	<!-- container -->
	<div id="container">
		<div class="titleWrap">
			<h2>영상검색</h2>
			<div class="selectWrap">
				<!-- 	위치이동 -->
				<c:choose>					
					<c:when test="${loginUser.isAdmin == true or loaginUser.isDeveloper == true or loginUser.userType == 'Admin'}">
						<select class="selectyze" name="sportsEventCode" id="sportsEventCodeSelect">
								<option value="">운동종목</option>
								<c:forEach items="${sprotsEvents}" var="sprotsEvent">
									<option value="${sprotsEvent.codeId}">${sprotsEvent.name}</option>
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
			<div id="lnbWrapT" class="searchContainer">
				<form id="frmSearch" onSubmit="return false;">
					<input type="hidden" name="hasNotUsed" value="true" />		
					<!-- 	위치이동 -->
					<div style="display:none">							
						<c:choose>					
							<c:when test="${loginUser.isAdmin == true or loaginUser.isDeveloper == true or loginUser.userType == 'Admin'}">
								<div class="">
									<select class="selectyze psa" name="sportsEventCode">
										<option value="">운동종목</option>
										<c:forEach items="${sprotsEvents}" var="sprotsEvent">
											<option value="${sprotsEvent.codeId}">${sprotsEvent.name}</option>
										</c:forEach>
									</select>
								</div>
							</c:when>
							
							<c:otherwise>
								<input type="hidden" name="sportsEventCode" value="${loginUser.sportsEventCode}"/>
							</c:otherwise>
						</c:choose>
					</div>	
					<!-- //위치이동 -->					
					<ul>
						<li>
							<label for="search_keyword">제목</label> 
							<input type="text" class="inputTxt" id="search_keyword" name="keyword" />
						</li>
						<li>
							<p>촬영일</p>
							<div class="datepickerBox">
								<label for="registFromDate">From</label>
								<input type="text" id="recordFromDate" name="recordFromDate" class="inputTxt date" value="<fmt:formatDate value="${fromDate}" pattern="yyyy-MM-dd"/>"/>
							</div>
							<div class="datepickerBox">
								<label for="registToDate">To</label>
								<input type="text" id="recordToDate" name="recordToDate" class="inputTxt date" value="<fmt:formatDate value="${currentDate}" pattern="yyyy-MM-dd"/>"/>					
							</div>
						</li>
						<li>
							<label for="search_tagUserId">소유자</label> 
							<select class="selectyze" name="tagUserId" id="search_tagUserId">
								<option value="">선택하세요</option>
								<c:forEach items="${users}" var="user">
									<c:choose>
										<c:when test="${loginUser.isAdmin == true or loaginUser.isDeveloper == true or loginUser.userType == 'Admin'}">
											<option value="${user.userId}">${user.userName}</option>
										</c:when>
										<c:otherwise>
											<c:if test="${user.sportsEventCode == loginUser.sportsEventCode}">
												<option value="${user.userId}">${user.userName}</option>
											</c:if>
										</c:otherwise>
									</c:choose>
								</c:forEach>
							</select>
						</li>
						<li>
							<label for="search_recordUserId">촬영자</label> 
							<select class="selectyze" name="recordUserId" id="search_recordUserId">
								<option value="">선택하세요</option>
								<c:forEach items="${users}" var="user">
								<%--	<option value="${user.userId}">${user.userName}</option> --%>
									<c:choose>
										<c:when test="${loginUser.isAdmin == true or loaginUser.isDeveloper == true or loginUser.userType == 'Admin'}">
											<option value="${user.userId}">${user.userName}</option>
										</c:when>
										<c:otherwise>
											<c:if test="${user.sportsEventCode == loginUser.sportsEventCode}">
												<option value="${user.userId}">${user.userName}</option>
											</c:if>
										</c:otherwise>
									</c:choose>
									
								</c:forEach>
							</select>
						</li>
						<li>
							<label for="search_keyword">태그</label> 
							<input type="text" class="inputTxt" id="tagInfo" name="tagInfo" />
						</li>
					</ul>
				</form>
				<div class="btnWrap">						
					<a class="btn reset" href="javascript:onClick_searchInit();">초기화</a>
					<a class="btn search" href="javascript:onClick_search();">검색</a> 
				</div>				
			</div>
			<!-- //lnbWrap -->
			<!-- contents -->
			<div id="contents">
				<div class="vodlistBox thum">
					<table id="contentList" class="list_type1" data-ctrl-view="content_list" data-event-selectedRow="onSelected_cameraListItem"></table>
	
					<div id="NoData"></div>
					<div id="paginate" class="paginate">
						
					</div>
				</div>
	
				<div>
					<form id="frmCameraDetail">
					</form>
	
				</div>
	
			</div>

			<!-- //contents -->
			<div class="detailContainer">
				<form id="frmContentDetails" data-ctrl-view="content_details" 
				data-event-reloadList="callback_reloadList">
				</form>
				
			</div>
			<!-- 	canvas -->
			<div class="canvasContainer">
				<div class="canvasWrap" id="canvasWrap">
					<div class="canvasMenuWrap" >
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
		</div>
	</div>
	<!-- //container -->
	<!-- footer -->
	<jsp:include page="/include/footer" />
	<!-- //footer -->
</div>

</body>
</html>
