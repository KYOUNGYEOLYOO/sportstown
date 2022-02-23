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


<script src="<c:url value="/bluecap/jqUtils/messagebox.js"/>"></script>
<script src="<c:url value="/bluecap/jqUtils/ajaxHTMLLoader.js"/>"></script>
<script src="<c:url value="/bluecap/jqUtils/ctrlEvent.js"/>"></script>


<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/resources/jqueryui/jquery-ui.css"/>" />
<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/resources/jqgrid/css/ui.jqgrid.css"/>" />

<%-- <script src="<c:url value="/resources/jqueryui/jquery-ui.js"/>"></script> --%>
<script src="<c:url value="/resources/jqgrid/js/i18n/grid.locale-en.js"/>"></script>
<script src="<c:url value="/resources/jqgrid/js/jquery.jqGrid.min.js"/>"></script>

<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/bluecap/css/bcsCommon.css"/>"/>


<script type="text/javascript">
$(document).ready(function(){
	$("#groupList").change(function(event, handle){
		clear_groupDetail();
		var groupCode = $(this).val();
		if(groupCode != "")
			onSelected_group(groupCode);
	});
});
</script>


<script type="text/javascript">

function onSelected_group(groupCode)
{
	$.ajax({
		url : "<c:url value="/service/code/getCodeGroup"/>/" + groupCode,
		async : false,
		dataType : "json",
		data : null, 
		method : "post",
		beforeSend : function(xhr, settings ){},
		error : function (xhr, status, error){},
		success : function (ajaxData) {
			if(ajaxData.resultCode == "Success"){
				set_groupDetail(ajaxData.group);
			}else{
				new bcs_messagebox().openError("그룹코드조회", "그룹 정보 조회중 오류 발생 [code="+ajaxData.resultCode+"]", null);
			}
		}
	});
	
	$("#codeList").jqGrid("setGridParam", {
		url : "<c:url value="/service/code/getCodes"/>/" + groupCode + "?hasNotUsed=true",
		datatype: "json"
	});
	$("#codeList").trigger("reloadGrid");
}

function onSelected_codeListItem(sender, rowData)
{
}

function onClick_regist()
{
	var groupCode = $("#groupList").val();
	if(groupCode == "")
	{
		new bcs_messagebox().open("코드관리", "그룹 코드를 선택해 주세요", null);
		return;
	}
	
	$("[data-ctrl-view=code_regist]").empty();
	$("[data-ctrl-view=code_regist]").jqUtils_bcs_loadHTML(
			"<c:url value="/code/regist"/>/" + groupCode,
			false, "get", null, null
		);
}

function onClick_modify()
{
	var codeId = $("#codeList").jqGrid("getGridParam", "selrow");
	if(typeof codeId == "undefined" || codeId == null)
	{
		new bcs_messagebox().open("코드관리", "코드를 선택해 주세요", null);
		return;
	}
	
	console.log($("[data-ctrl-view=code_modify]"));
	$("[data-ctrl-view=code_modify]").empty();
	$("[data-ctrl-view=code_modify]").jqUtils_bcs_loadHTML(
			"<c:url value="/code/modify"/>/" + codeId,
			false, "get", null, null
		);
	
}

function onClick_delete()
{
	var codeId = $("#codeList").jqGrid("getGridParam", "selrow");
	if(typeof codeId == "undefined" || codeId == null)
	{
		new bcs_messagebox().open("코드관리", "코드를 선택해 주세요", null);
		return;
	}
	
	
	var mb = new bcs_messagebox().open("코드관리", "삭제 하시겠습니까?", null, {
		"삭제" : function(){
			$.ajax({
				url : "<c:url value="/service/code/removeCode"/>/" + codeId,
				async : false,
				dataType : "json",
				data : null, 
				method : "post",
				beforeSend : function(xhr, settings ){},
				error : function (xhr, status, error){},
				success : function (ajaxData) {
					if(ajaxData.resultCode == "Success"){
						$("#codeList").jqGrid("delRowData", ajaxData.codeId);
						mb.close();
					}else{
						new bcs_messagebox().openError("코드관리", "코드 삭제중 오류 발생 [code="+ajaxData.resultCode+"]", null);
					}
				}
			});
		},
		"닫기" : function(){ mb.close(); }
	});
	
	
}
</script>

<script type="text/javascript">

function callback_regist(sender, code)
{
	console.log(code);
	$("#codeList").jqGrid("addRowData", code.codeId, code, "last");
}

function callback_modify(sender, code)
{
	$("#codeList").jqGrid("setRowData", code.codeId, code);
}

</script>


<script type="text/javascript">
function set_groupDetail(group)
{
	$("#frmCodeGroupDetail").find("input[name=name]").val(group.name);
	$("#frmCodeGroupDetail").find("input[name=description]").val(group.description);
	
	$("#frmCodeGroupDetail").find("input[name=groupCode]").val(group.groupCode);
	
}

function clear_groupDetail()
{
	$("#frmCodeGroupDetail").find("input").val("");
	
	$("#codeList").jqGrid("setGridParam", {
		url : null,
		datatype: "local"
	});
	$("#codeList").trigger("reloadGrid");
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
	<jsp:param value="codeManage" name="subMenu"/>
</jsp:include>
<!-- //header -->


<div title="코드등록" class="bcs_dialog_hide" data-ctrl-view="code_regist" data-event-regist="callback_regist">
</div>


<div title="코드수정" class="bcs_dialog_hide" data-ctrl-view="code_modify" data-event-modify="callback_modify">
</div>

<!-- container -->
<div id="container">
	<div class="titleWrap">
		<h2>관리자기능 - 코드관리</h2>
	</div>
	<div id="contentsWrap">

		<!-- lnbWrap -->
		<div id="lnbWrap" class="searchContainer">
			<ul>
				<li>
					<select class="selectyze" id="groupList">
						<option value="">그룹 선택</option>
						<c:forEach items="${groups}" var="group">
							<option value="${group.groupCode}">${group.name}</option>
						</c:forEach>
					</select>
				</li>
			</ul>
		</div>
		<!-- //lnbWrap -->

		<!-- contents -->
		<div id="contents" class="detailContainer">
		
			<form id="frmCodeGroupDetail">
				<input type="hidden" name="groupCode" value="" />
				<div class="detailWrap code">
					<dl>
						<dt>코드그룹명</dt>
						<dd><input type="text" name="name" value="" title="코드그룹명" class="inputTxt" readonly></dd>
						<dt>설명</dt>
						<dd><input type="text" name="description" value="" title="설명" class="inputTxt" readonly></dd>
					</dl>
				</div>
			</form>

			<div class="vodlistBox">
				<table id="codeList" class="list_type1" data-ctrl-view="code_list" data-event-selectedRow="onSelected_codeListItem"></table>
				<jsp:include page="/code/codeList">
					<jsp:param value="codeList" name="listId"/>
				</jsp:include>
				
				<div class="btnWrap">
					<div class="btnWrap">
						<a class="btn delete" href="javascript:onClick_delete();">삭제</a>
						<a class="btn edit" href="javascript:onClick_modify();">수정</a>
						<a class="btn write" href="javascript:onClick_regist();">등록</a>			
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
