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
	clearUserDetail();
	console.log($("#frmSearch").serialize());
	$("#userList").jqGrid("setGridParam", {
		url : "<c:url value="/service/user/getUsers"/>?" + $("#frmSearch").serialize(),
		page : 1
	});
	
	$("#userList").trigger("reloadGrid");
	
}

function onSelected_userListItem(sender, rowData)
{
	clearUserDetail();
	$.ajax({
		url : "<c:url value="/service/user/getUser"/>/" + rowData.userId,
		async : false,
		dataType : "json",
		data : null, 
		method : "post",
		beforeSend : function(xhr, settings ){},
		error : function (xhr, status, error){},
		success : function (ajaxData) {
			if(ajaxData.resultCode == "Success"){
				setUserDetail(ajaxData.user);
			}else{
				new bcs_messagebox().openError("사용자 상세조회", "사용자 조회중 오류 발생 [code="+ajaxData.resultCode+"]", null);
			}
		}
	});
	
}

function onClick_regist()
{
	$("[data-ctrl-view=user_regist]").empty();
	$("[data-ctrl-view=user_regist]").jqUtils_bcs_loadHTML(
			"<c:url value="/user/regist"/>",
			false, "get", null, null
		);
}

function onClick_modify()
{
	var userId = $("#frmUserDetail").find("input[name=userId]").val();
	
	console.log(userId);
	
	if(userId == "")
	{
		new bcs_messagebox().open("사용자 수정", "사용자를 선택해 주세요", null);
		return;
	}
	$("[data-ctrl-view=user_modify]").empty();
	$("[data-ctrl-view=user_modify]").jqUtils_bcs_loadHTML(
			"<c:url value="/user/modify"/>/" + userId,
			false, "get", null, null
		);	
}

function onClick_delete()
{
	var userId = $("#frmUserDetail").find("input[name=userId]").val();
	if(userId == "")
	{
		new bcs_messagebox().open("사용자 삭제", "사용자를 선택해 주세요", null);
		return;
	}
	
	
	var mb = new bcs_messagebox().open("사용자 삭제", "정말로 삭제 하시겠습니까?", null, {
		
		"닫기" : function(){ mb.close(); },
		"삭제" : function(){
			$.ajax({
				url : "<c:url value="/service/user/removeUser"/>/" + userId,
				async : false,
				dataType : "json",
				data : null, 
				method : "post",
				beforeSend : function(xhr, settings ){},
				error : function (xhr, status, error){},
				success : function (ajaxData) {
					if(ajaxData.resultCode == "Success"){
						$("#userList").jqGrid("delRowData", ajaxData.userId);
						clearUserDetail();
						mb.close();
					}else{
						new bcs_messagebox().openError("사용자 삭제", "사용자 삭제중 오류 발생 [code="+ajaxData.resultCode+"]", null);
					}
				}
			});
		},		
	});
	
	
}
</script>

<script type="text/javascript">

function callback_regist(sender, user)
{
	$("#userList").jqGrid("addRowData", user.userId, user, "first");
	setUserDetail(user);
}

function callback_modify(sender, user)
{
	
	console.log(user);
	$("#userList").jqGrid("setRowData", user.userId, user);
	setUserDetail(user);
}

</script>


<script type="text/javascript">
function setUserDetail(user)
{
	console.log(user);
	if(user.sportsEvent.isPartition == true){
		$('#partition_area1').show();
		$('#partition_area2').show();
		$("#frmUserDetail").find("input[name=authFromDate]").val(user.authFromDate.substring(0,10));
		$("#frmUserDetail").find("input[name=authToDate]").val(user.authToDate.substring(0,10));
	}else{
		$('#partition_area1').hide();
		$('#partition_area2').hide();
	}
	
	$("#frmUserDetail").find("input[name=userId]").val(user.userId);
	$("#frmUserDetail").find("input[name=loginId]").val(user.loginId);
	$("#frmUserDetail").find("input[name=userName]").val(user.userName);
	
	if(user.sportsEvent != null)
		$("#frmUserDetail").find("input[name=sportsEventName]").val(user.sportsEvent.name);
	else
		$("#frmUserDetail").find("input[name=sportsEventName]").val("");
	
	
	var tempVal = "";
	switch(user.userType)
	{
	case "Admin" : tempVal = "관리자"; break;
	case "Coach" : tempVal = "지도자"; break;
	case "Athlete" : tempVal = "선수"; break;
	}
	$("#frmUserDetail").find("input[name=userType]").val(tempVal);
	
}

function clearUserDetail()
{
	$("#frmUserDetail").find("input").val("");
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
	<jsp:param value="userManage" name="subMenu"/>
</jsp:include>
<!-- //header -->


<div title="사용자등록" class="bcs_dialog_hide" data-ctrl-view="user_regist" data-event-regist="callback_regist">
</div>


<div title="사용자수정" class="bcs_dialog_hide" data-ctrl-view="user_modify" data-event-modify="callback_modify">
</div>

<!-- container -->
<div id="container">
	<div class="titleWrap">
		<h2>관리자기능 - 사용자</h2>
	</div>
	<div id="contentsWrap">

		<!-- lnbWrap -->
		<div id="lnbWrap" class="searchContainer">
			<form id="frmSearch" onSubmit="return false;">
				<input type="hidden" name="hasNotUsed" value="true" />				
				<ul>				
					<li>
						<label for="search_keyword">사용자명</label> 
						<input type="text" class="inputTxt" id="search_keyword" name="keyword" />
					</li>
				</ul>
			</form>
			<div class="btnWrap">
				<a class="btn reset">초기화</a>	<!-- 초기화 기능 추가 -->
				<a class="btn search" href="javascript:onClick_search();">검색</a></span> 
			</div>
		</div>
		<!-- //lnbWrap -->

		<!-- contents -->
		<div id="contents">
			<div class="vodlistBox">
				<jsp:include page="/user/list">
					<jsp:param value="userList" name="listId"/>
					<jsp:param value="p_userList" name="pagerId"/>
				</jsp:include>
				<table id="userList" class="list_type1" data-ctrl-view="user_list" data-event-selectedRow="onSelected_userListItem"></table>
<!-- 				<div id="p_userList" data-ctrl-view="user_list_pager"></div> -->
<!-- 				<div id="NoData"></div> -->
				<div class="paginate" id="p_userList" data-ctrl-view="user_list_pager" style="text-align: center; "></div>
				
			</div>
		</div>
		<div class="detailContainer">
			<form id="frmUserDetail">
				<input type="hidden" name="userId" value="" />
				<div class="detailWrap">
					<dl>
						<dt>ID</dt>
						<dd class="full">
							<input type="text" name="loginId" value="" title="사용자명" class="inputTxt" readonly />
						</dd>
						<dt>사용자명</dt>
						<dd class="full">
							<input type="text" name="userName" value="" title="사용자명" class="inputTxt" readonly>
						</dd>
						<dt>종목</dt>
						<dd class="full">
							<input type="text" name="sportsEventName" value="" title="종목" class="inputTxt" readonly>
						</dd>
						<dt id="partition_area1" style="display:none;">기간</dt>
						<dd id="partition_area2" style="display:none;" class="full">
							<input type="text" name="authFromDate" value="" title="종목" class="inputTxt" style="width: 47%;" readonly>&nbsp;~&nbsp;
							<input type="text" name="authToDate" value="" title="종목" class="inputTxt" style="width: 47%;"readonly>
						</dd>
						<dt>사용자유형</dt>
						<dd class="full">
							<input type="text" name="userType" value="" title="사용자종류" class="inputTxt" readonly>
						</dd>
					</dl>
				</div>
			</form>
			<div class="btnWrap">	
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
