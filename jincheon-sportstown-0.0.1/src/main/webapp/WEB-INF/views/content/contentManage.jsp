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
	init_contentList();
});
</script>

<script type="text/javascript">

function init_contentList()
{
	var eventSender = new bcs_ctrl_event($("#contentList"));
	$("#contentList").jqGrid({
		// data: mydata,
		url : "<c:url value="/service/content/getContents"/>?" + $("#frmSearch").serialize(),
		datatype: "json",
		mtype: "get",
	   	width: "auto",
	   	key: true,
		// height: "auto",
		height: 640,
		autowidth: true,
		viewrecords: true,
		rownumbers: true,
		rowNum: 8,
		rowList: [8,20,30],
	   	colNames:["썸네일", "스포츠종류", "제목", "촬영자", "촬영일자", "등록일자", "contentId"],
	   	colModel:[
			{name:"thumbnail",index:"thumbnail", width:70,align:"center", 
				formatter: function (cellvalue, options, rowObject) {
					console.log(rowObject);
					return "<img src='<c:url value="/content/thumbnail"/>/"+rowObject.contentId+"' height='100%' width='100%'/>";
				}
			},
			{name:"sportsEvent.name",index:"sportsEventName", width:80, align:"left"},
			{name:"title",index:"title", width:180, align:"left"},
			{name:"recordUser.userName", index:"recordUserName", width:100, align:"center"},
			{name:"formatedRecordDate", index:"formatedRecordDate", width:100, align:"center"},
			{name:"content.registDate", index:"registDate", width:100, align:"center"},
			{name:"contentId", index:"contentId", hidden:true}
		],
		pager: $("#p_contentList"),
		jsonReader : {
			root : "contents",
			id : "contentId"
		},
		onSelectRow : function(id){
			onClick_detail();
		}
	});
}
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

function onClick_modify()
{
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
			$.ajax({
				url : "<c:url value="/"/>/" + contentId,
				async : false,
				dataType : "json",
				data : null, 
				method : "post",
				beforeSend : function(xhr, settings ){},
				error : function (xhr, status, error){},
				success : function (ajaxData) {
					if(ajaxData.resultCode == "Success"){
						$("#contentList").jqGrid("delRowData", ajaxData.contentId);
						mb.close();
					}else{
						new bcs_messagebox().openError("영상검색", "컨텐츠 삭제중 오류 발생 [code="+ajaxData.resultCode+"]", null);
					}
				}
			});
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

<div id="wrapper">	
<!-- header -->
<jsp:include page="/include/top">
	<jsp:param value="search" name="mainMenu"/>
	<jsp:param value="contentManage" name="subMenu"/>
</jsp:include>
<!-- //header -->

<!-- container -->
<div id="container">
	<div class="titleWrap">
		<h2>영상검색</h2>
		<div class="selectWrap">
			<!-- 	위치이동 -->
			<c:choose>					
				<c:when test="${loginUser.isAdmin == true or loaginUser.isDeveloper == true or loginUser.userType == 'Admin'}">
					<select class="selectyze" name="sportsEventCode">
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
				<!-- 	위치이동							
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
				//위치이동 -->					
				<ul>
					<li>
						<label for="search_keyword">제목</label> 
						<input type="text" class="inputTxt" id="search_keyword" name="keyword" />
					</li>
					<li>
						<p>촬영일</p>
						<div class="datepickerBox">
							<label for="registFromDate">From</label>
							<input type="text" id="recordFromDate" name="registFromDate" class="inputTxt date" value="<fmt:formatDate value="${fromDate}" pattern="yyyy-MM-dd"/>"/>
						</div>
						<div class="datepickerBox">
							<label for="registToDate">To</label>
							<input type="text" id="recordToDate" name="registToDate" class="inputTxt date" value="<fmt:formatDate value="${currentDate}" pattern="yyyy-MM-dd"/>"/>					
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
				<div id="p_contentList" data-ctrl-view="content_list_pager"></div>
			</div>

			<div>
				<form id="frmCameraDetail">
				</form>
<!-- 				<div class="btnbox alignR"> -->
<!-- 					<span class="btn_typeA t1"><a href="javascript:onClick_detail();">상세보기</a></span>  -->
<!-- 					<span class="btn_typeA t4"><a href="javascript:onClick_modify();">수정</a></span>  -->
<!-- 					<span class="btn_typeA t2"><a href="javascript:onClick_delete();">삭제</a></span> -->
<!-- 				</div> -->
			</div>

		</div>

		<!-- //contents -->
		<div class="detailContainer">
			<div class="videoview">
				<div id="player" style="background:#fafafa"></div>	
			</div>
			<div class="detailWrap">
				<dl>
					<dt>제목</dt>
					<dd class="full"><input type="text" name="title" title="제목" class="inputTxt" value="${contentMeta.title}" readonly></dd>
					<dt>종목</dt>
					<dd><input type="text" name="sportsEvent" title="종목" class="inputTxt" value="${contentMeta.sportsEvent.name}" readonly></dd>
					<dt class="ml20">소유자</dt>
					<dd><input type="text" name="tagUser" title="소유자" class="inputTxt" value="${contentMeta.contentUserNames}" readonly></dd>
					<dt>녹화자</dt>
					<dd><input type="text" name=recordUser title="녹화자" class="inputTxt" value="${contentMeta.recordUser.userName}" readonly></dd>
					<dt class="ml20">녹화일자</dt>
					<dd>
						<div class="datepickerBox">
							<input type="text" id="recordFromDate" name="recordDate" class="inputTxt date"  value="<fmt:formatDate value="${contentMeta.recordDate}" pattern="yyyy-MM-dd" />" readonly/>
						</div>					
					</dd>
					<dt>설명</dt>
					<dd class="full"><textarea name="summary" title="설명" readonly>${contentMeta.summary}</textarea></dd>
					<dt>파일</dt>
					<dd class="full">
						<input type="text" name="instances[0].orignFileName" value="" data-ctrl-contentMeta="orignFileName" class="inputTxt" readonly>
						<input type="hidden" name="instances[0].fileId" value="" data-ctrl-contentMeta="fileId">						
					</dd>						
				</dl>
				<div class="btnWrap">
					<a class="btn download">다운로드</a>		
					<div class="btnWrap">
						<a class="btn delete">삭제</a> 
						<a class="btn edit">수정</a>					
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<!-- //container -->
<!-- footer -->
<jsp:include page="/include/footer" />
<!-- //footer -->
</div>

</body>
</html>
