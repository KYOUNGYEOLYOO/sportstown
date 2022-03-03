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

<script type="text/javascript">
$(document).ready(function(){
	set_cameraDetail(null);
});
</script>


<script type="text/javascript">

var applicationName = "";
var serverName = "";
var streamName = "";



function onClick_search()
{
	console.log('onClick_search');
	clear_cameraDetail();
	$("#cameraList").jqGrid("setGridParam", {
		url : "<c:url value="/service/camera/getCameras"/>?" + $("#frmSearch").serialize(),
		page : 1
	});
	console.log('onClick_search searchafter');
	$("#cameraList").trigger("reloadGrid");
	
}

function onClick_searchInit()
{
	location.reload();
	
}

function onSelected_cameraListItem(sender, rowData)
{	
	clear_cameraDetail();
	$.ajax({
		url : "<c:url value="/service/camera/getCamera"/>/" + rowData.camId,
		async : false,
		dataType : "json",
		data : null, 
		method : "post",
		beforeSend : function(xhr, settings ){},
		error : function (xhr, status, error){},
		success : function (ajaxData) {
			if(ajaxData.resultCode == "Success"){
				set_cameraDetail(ajaxData.camera.camId);			
			}else{
				new bcs_messagebox().openError("카메라관리", "카메라 상세 조회중 오류 발생 [code="+ajaxData.resultCode+"]", null);
			}
		}
	});
	
}

function onClick_regist()
{
	$("[data-ctrl-view=camera_regist]").empty();
	$("[data-ctrl-view=camera_regist]").jqUtils_bcs_loadHTML(
			"<c:url value="/camera/regist"/>",
			false, "get", null, null
		);
}

function onClick_modify()
{
	var camId = $("#cameraList").jqGrid("getGridParam", "selrow");
	if(typeof camId == "undefined" || camId == null)
	{
		new bcs_messagebox().open("카메라관리", "카메라를 선택해 주세요", null);
		return;
	}
	$("[data-ctrl-view=camera_modify]").empty();
	$("[data-ctrl-view=camera_modify]").jqUtils_bcs_loadHTML(
			"<c:url value="/camera/modify"/>/" + camId,
			false, "get", null, null
		);	
}
function onClick_connect(){
	var camId = $("#cameraList").jqGrid("getGridParam", "selrow");
	if(typeof camId == "undefined" || camId == null)
	{
		new bcs_messagebox().open("카메라관리", "카메라를 선택해 주세요", null);
		return;
	}
	
	$.ajax({
		url : "<c:url value="/service/camera/connectStreamW"/>/" + camId,
		async : false,
		dataType : "json",
		data : null,
		method : "post",
		beforeSend : function(xhr, settings ){},
		error : function (xhr, status, error){},
		success : function (ajaxData) {
			if(ajaxData.resultCode == "Success"){
				console.log("success");
			}else{
// 				new bcs_messagebox().openError("카메라관리", "카메라 삭제중 오류 발생 [code="+ajaxData.resultCode+"]", null);
			}
		}
	});
}

function onClick_disconnect(){
	var camId = $("#cameraList").jqGrid("getGridParam", "selrow");
	if(typeof camId == "undefined" || camId == null)
	{
		new bcs_messagebox().open("카메라관리", "카메라를 선택해 주세요", null);
		return;
	}
	
	console.log("camID: ", camId);
	
	$.ajax({
		url : "<c:url value="/service/camera/disconnectStreamW"/>/" + camId,
		async : false,
		dataType : "json",
		data : null,
		method : "post",
		beforeSend : function(xhr, settings ){},
		error : function (xhr, status, error){},
		success : function (ajaxData) {
			console.log("ajaxData.finalUrl : ", ajaxData);
			if(ajaxData.resultCode == "Success"){
				console.log("success");
			}else{
// 				new bcs_messagebox().openError("카메라관리", "카메라 연결해제중 오류 발생 [code="+ajaxData.resultCode+"]", null);
			}
		}
	});
}

function onClick_delete()
{
	var camId = $("#cameraList").jqGrid("getGridParam", "selrow");
	if(typeof camId == "undefined" || camId == null)
	{
		new bcs_messagebox().open("카메라관리", "카메라를 선택해 주세요", null);
		return;
	}
	
	
	var mb = new bcs_messagebox().open("카메라관리", "삭제 하시겠습니까?", null, {
		"닫기" : function(){ mb.close(); },		
		"삭제" : function(){
			$.ajax({
				url : "<c:url value="/service/camera/removeCamera"/>/" + camId,
				async : false,
				dataType : "json",
				data : null, 
				method : "post",
				beforeSend : function(xhr, settings ){},
				error : function (xhr, status, error){},
				success : function (ajaxData) {
					if(ajaxData.resultCode == "Success"){
						$("#cameraList").jqGrid("delRowData", ajaxData.camId);
						clear_cameraDetail();
						mb.close();
					}else{
						new bcs_messagebox().openError("카메라관리", "카메라 삭제중 오류 발생 [code="+ajaxData.resultCode+"]", null);
					}
				}
			});
		}

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
	console.log("=== callback_modify" );
	console.log(camera);
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
	console.log('set_cameraDetail');
	
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

<div id="wrapper">	
<!-- header -->
<jsp:include page="/include/top">
	<jsp:param value="manage" name="mainMenu"/>
	<jsp:param value="cameraManage" name="subMenu"/>
</jsp:include>
<!-- //header -->


<div title="카메라등록" class="bcs_dialog_hide" data-ctrl-view="camera_regist" data-event-regist="callback_regist">
</div>


<div title="카메라수정" class="bcs_dialog_hide" data-ctrl-view="camera_modify" data-event-modify="callback_modify">
</div>

<!-- container -->
<div id="container">
	<div class="titleWrap">
		<h2>관리자기능 - 카메라 등록</h2>
	</div>
	<div id="contentsWrap">
	
		<!-- lnbWrap -->
		<div id="lnbWrap" class="searchContainer">
			<form id="frmSearch" onSubmit="return false;">
				<input type="hidden" name="hasNotUsed" value="true" />

				<ul>				
					<li>
						<label for="search_keyword">카메라명</label> 
						<input type="text" class="inputTxt" id="search_keyword" name="keyword" />
					</li>
					<li>
						<select class="selectyze" name="cameraType">
							<option value="">카메라유형</option>
							<option value="Static">고정</option>
							<option value="Shift">유동</option>
						</select>
					</li>
					<li>
						<select class="selectyze" name="locationCode">
							<option value="">카메라위치</option>
							<c:forEach items="${locations}" var="location">
								<option value="${location.codeId}">${location.name}</option>
							</c:forEach>
						</select>
					</li>
					<li>
						<select class="selectyze" name="sportsEventCode">
							<option value="">스포츠종목</option>
							<c:forEach items="${sprotsEvents}" var="sprotsEvent">
								<option value="${sprotsEvent.codeId}">${sprotsEvent.name}</option>
							</c:forEach>
						</select>
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
			<div class="vodlistBox">
				<jsp:include page="/camera/list">
					<jsp:param value="cameraList" name="listId"/>
					<jsp:param value="p_cameraList" name="pagerId"/>
				</jsp:include>
				<table id="cameraList" class="list_type1" data-ctrl-view="camera_list" data-event-selectedRow="onSelected_cameraListItem"></table>
<!-- 				<div id="p_cameraList" data-ctrl-view="camera_list_pager"></div> -->
<!-- 				<div id="NoData"></div> -->
				<div class="paginate" id="p_cameraList" data-ctrl-view="camera_list_pager" style="text-align: center;"></div>
				
			</div>
		</div>
		<div class="detailContainer camera">
			<form id="frmCameraDetail">
			</form>
			<div class="btnWrap cameraSearch">
				<a class="btn" href="javascript:onClick_connect();">연결</a>
				<a class="btn" href="javascript:onClick_disconnect();">연결해제</a>
				<div class="btnWrap">				
					<a class="btn delete" href="javascript:onClick_delete();">삭제</a>
					<a class="btn edit" href="javascript:onClick_modify();">수정</a>				
					<a class="btn write" href="javascript:onClick_regist();">등록</a>			
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
