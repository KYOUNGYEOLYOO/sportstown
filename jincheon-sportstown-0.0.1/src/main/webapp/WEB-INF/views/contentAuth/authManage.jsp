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
</script>


<script type="text/javascript">

function onClick_search()
{
	clearAuthDetail();
	
	console.log($("#frmSearch").serialize());
	$("#authList").jqGrid("setGridParam", {
		url : "<c:url value="/service/contentAuth/getContentAuths"/>?" + $("#frmSearch").serialize(),
		page : 1
	});
	
	$("#authList").trigger("reloadGrid");
	
}



function onSelected_authListItem(sender, rowData)
{
	
	clearAuthDetail();
	$.ajax({
		url : "<c:url value="/service/contentAuth/getContentAuth"/>/" + rowData.contentId +"/"+ rowData.userId+"/"+ rowData.state+"/"+ rowData.contentAuthId,
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
				setContentAuthDetail(ajaxData.contentAuth);
			}else{
				new bcs_messagebox().openError("승인 상세조회", "승인 조회중 오류 발생 [code="+ajaxData.resultCode+"]", null);
			}
		}
	});
	
}



function onClick_modify()
{
	var userId = $("#frmAuthDetail").find("input[name=userId]").val();
	var contentId = $("#frmAuthDetail").find("input[name=contentId]").val();
	
	console.log(userId);
	console.log(contentId);
	
	if(userId == "")
	{
		new bcs_messagebox().open("승인", "목록를 선택해 주세요", null);
		return;
	}
	$("[data-ctrl-view=auth_modify]").empty();
	$("[data-ctrl-view=auth_modify]").jqUtils_bcs_loadHTML(
			"<c:url value="/auth/modify"/>/" +contentId+"/"+ userId,
			false, "get", null, null
		);	
}

function onClick_return()
{
	var userId = $("#frmAuthDetail").find("input[name=userId]").val();
	var contentId = $("#frmAuthDetail").find("input[name=contentId]").val();
	var state = $("#frmAuthDetail").find("input[name=state]").val();
	var contentAuthId = $("#frmAuthDetail").find("input[name=contentAuthId]").val();
	
	if(userId == "")
	{
		new bcs_messagebox().open("승인 반려", "목록를 선택해 주세요", null);
		return;
	}
	
	
	var mb = new bcs_messagebox().open("승인 반려", "정말로 반려 하시겠습니까?", null, {
		
		"닫기" : function(){ mb.close(); },
		"반려" : function(){
			$.ajax({
				url : "<c:url value="/service/contentAuth/returnContentAuth"/>/" +contentId+"/"+ userId+"/"+state+"/"+contentAuthId,
				async : false,
				dataType : "json",
				data : null, 
				method : "post",
				beforeSend : function(xhr, settings ){},
				error : function (xhr, status, error){},
				success : function (ajaxData) {
					if(ajaxData.resultCode == "Success"){
						//$("#authList").jqGrid("delRowData", ajaxData.userId);
						
						onClick_search();
						mb.close();
					}else{
						new bcs_messagebox().openError("승인 반려", "승인 반려중 오류 발생 [code="+ajaxData.resultCode+"]", null);
					}
				}
			});
		},		
	});
	
	
}

function onClick_approval()
{
	var userId = $("#frmAuthDetail").find("input[name=userId]").val();
	var contentId = $("#frmAuthDetail").find("input[name=contentId]").val();
	var state = $("#frmAuthDetail").find("input[name=state]").val();
	var contentAuthId = $("#frmAuthDetail").find("input[name=contentAuthId]").val();

	if(userId == "")
	{
		new bcs_messagebox().open("승인", "목록를 선택해 주세요", null);
		return;
	}
	
	
	var mb = new bcs_messagebox().open("승인", "정말로 승인 하시겠습니까?", null, {
		
		"닫기" : function(){ mb.close(); },
		"승인" : function(){
			$.ajax({
				url : "<c:url value="/service/contentAuth/approvalContentAuth"/>/" +contentId+"/"+ userId+"/"+state+"/"+contentAuthId,
				async : false,
				dataType : "json",
				data : null, 
				method : "post",
				beforeSend : function(xhr, settings ){},
				error : function (xhr, status, error){},
				success : function (ajaxData) {
					if(ajaxData.resultCode == "Success"){
						//$("#authList").jqGrid("delRowData", ajaxData.userId);
						onClick_search();
						mb.close();
					}else{
						new bcs_messagebox().openError("승인", "승인 중 오류 발생 [code="+ajaxData.resultCode+"]", null);
					}
				}
			});
		},		
	});
	
	
}
</script>

<script type="text/javascript">



function callback_modify(sender, contentAuth)
{
	
	console.log(user);
	$("#authList").jqGrid("setRowData", contentAuth.contentAuthId, contentAuth);
	setContentAuthDetail(contentAuth);
}

</script>


<script type="text/javascript">
function setContentAuthDetail(contentAuth)
{
	console.log(contentAuth);
	$("#frmAuthDetail").find("input[name=title]").val(contentAuth.content.title);
	$("#frmAuthDetail").find("input[name=userName]").val(contentAuth.user.userName);
	
	$("#frmAuthDetail").find("input[name=fromDate]").val(contentAuth.fromDate.substring(0,10));
	$("#frmAuthDetail").find("input[name=toDate]").val(contentAuth.toDate.substring(0,10));
	
	$("#frmAuthDetail").find("textarea[name=description]").val(contentAuth.description);
	
	$("#frmAuthDetail").find("input[name=userId]").val(contentAuth.userId);
	$("#frmAuthDetail").find("input[name=contentId]").val(contentAuth.contentId);
	$("#frmAuthDetail").find("input[name=state]").val(contentAuth.state);
	$("#frmAuthDetail").find("input[name=contentAuthId]").val(contentAuth.contentAuthId);
	
}

function clearAuthDetail()
{
	$("#frmAuthDetail").find("input").val("");
	$("#frmAuthDetail").find("textarea[name=description]").val("");
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
	<jsp:param value="contentAuth" name="subMenu"/>
</jsp:include>
<!-- //header -->


<div title="사용자등록" class="bcs_dialog_hide" data-ctrl-view="user_regist" data-event-regist="callback_regist">
</div>


<div title="사용자수정" class="bcs_dialog_hide" data-ctrl-view="user_modify" data-event-modify="callback_modify">
</div>

<!-- container -->
<div id="container">
	<div class="titleWrap">
		<h2>영상승인 </h2>
	</div>
	<div id="contentsWrap">

		<!-- lnbWrap -->
		<div id="lnbWrap" class="searchContainer">
			<form id="frmSearch" onSubmit="return false;">
				<c:if test="${loginUser.userType != 'Admin'}">
					<input type="hidden" name="userId" value="${loginUser.userId }" />	
				</c:if>
							
				<ul>				
					<li>
						<label for="search_keyword">상태</label> 
						<select class="selectyze" name="state" id="search_state">
							<option value="">선택하세요</option>
							<option value="wait">대기</option>
							<option value="approval">승인</option>
							<option value="return">반려</option>
						</select>
					</li>
				</ul>
			</form>
			<div class="btnWrap">
				
				<a class="btn search" href="javascript:onClick_search();">검색</a></span> 
			</div>
		</div>
		<!-- //lnbWrap -->

		<!-- contents -->
		<div id="contents">
			<div class="vodlistBox">
				<jsp:include page="/contentAuth/list">
					<jsp:param value="authList" name="listId"/>
					<jsp:param value="p_authList" name="pagerId"/>
				</jsp:include>
				<table id="authList" class="list_type1" data-ctrl-view="auth_list" data-event-selectedRow="onSelected_authListItem"></table>
<!-- 				<div id="p_userList" data-ctrl-view="user_list_pager"></div> -->
<!-- 				<div id="NoData"></div> -->
				<div class="paginate" id="p_authList" data-ctrl-view="auth_list_pager" style="text-align: center; "></div>
				
			</div>
		</div>
		<div class="detailContainer">
			<form id="frmAuthDetail">
				<input type="hidden" name="userId" value="" />
				<input type="hidden" name="contentId" value="" />
				<input type="hidden" name="state" value="" />
				<input type="hidden" name="contentAuthId" value="" />
				<div class="detailWrap">
					<dl>
						<dt>컨텐츠명</dt>
						<dd class="full">
							<input type="text" name="title" id="title" value="" title="사용자명" class="inputTxt" readonly />
						</dd>
						<dt>요청 사용자</dt>
						<dd class="full">
							<input type="text" name="userName" id="userName" value="" title="사용자명" class="inputTxt" readonly>
						</dd>
						
						<dt id="partition_area1"">기간</dt>
						<dd id="partition_area2" class="full">
							<input type="text" name="fromDate" id="fromDate"  value="" title="종목" class="inputTxt" style="width: 47%;" readonly>&nbsp;~&nbsp;
							<input type="text" name="toDate" id="toDate" value="" title="종목" class="inputTxt" style="width: 47%;"readonly>
						</dd>
						<dt>사유</dt>
						<dd class="full">
							<textarea name=description id=description title="설명" class="ta_type_1" readonly></textarea>
						</dd>
					</dl>
				</div>
			</form>
			<div class="btnWrap">	
				<div class="btnWrap">	 
					<c:if test="${loginUser.userType == 'Admin'}">
						<a class="btn edit" href="javascript:onClick_return();">반려</a>
						<a class="btn write" href="javascript:onClick_approval();">승인</a>
					</c:if>
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
