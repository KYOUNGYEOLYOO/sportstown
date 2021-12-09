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
		"닫기" : function(){ mb.close(); }
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
	<div id="contentsWrap">

		<!-- lnbWrap -->
		<div id="lnbWrap">
			<form id="frmSearch" onSubmit="return false;">
				<input type="hidden" name="hasNotUsed" value="true" />
				<div class="lnbWraph2">
					<h2>사용자관리</h2>
				</div>
				<div class="datepickerBox">
					<p>
						<label for="search_keyword">사용자명</label> 
						<input type="text" class="inputTxt" id="search_keyword" name="keyword" />
					</p>
				</div>
			</form>
			<div class="btnbox alignC" style="text-align: center;">
				<span class="btn_typeA t3"><a href="javascript:onClick_search();">검색</a></span> 
			</div>
		</div>
		<!-- //lnbWrap -->

		<!-- contents -->
		<div id="contents">

			<!-- title -->
			<h3>사용자관리</h3>
			<!-- //title -->

			<div class="vodlistBox">
				<jsp:include page="/user/list">
					<jsp:param value="userList" name="listId"/>
					<jsp:param value="p_userList" name="pagerId"/>
				</jsp:include>
				<table id="userList" class="list_type1" data-ctrl-view="user_list" data-event-selectedRow="onSelected_userListItem"></table>
				<div id="p_userList" data-ctrl-view="user_list_pager"></div>
			</div>

			<div>
				<form id="frmUserDetail">
					<input type="hidden" name="userId" value="" />
					<table class="write_type1 mgt30 mgb20" summary="사용자 상세 정보">
						<caption>사용자 상세 정보</caption>
						<colgroup>
							<col width="150">
							<col width="*">
						</colgroup>
						<tbody>
							<tr>
								<th>ID</th>
								<td><input type="text" name="loginId" value="" title="사용자명" class="type_2" readonly></td>
							</tr>
							<tr>
								<th>사용자명</th>
								<td><input type="text" name="userName" value="" title="사용자명" class="type_2" readonly></td>
							</tr>
							<tr>
								<th>종목</th>
								<td><input type="text" name="sportsEventName" value="" title="종목" class="type_2" readonly></td>
							</tr>
							<tr>
								<th>사용자유형</th>
								<td><input type="text" name="userType" value="" title="사용자종류" class="type_2" readonly></td>
							</tr>
						</tbody>
					</table>
				</form>
				<div class="btnbox alignR">
					<span class="btn_typeA t1"><a href="javascript:onClick_regist();">등록</a></span> 
					<span class="btn_typeA t4"><a href="javascript:onClick_modify();">수정</a></span> 
					<span class="btn_typeA t2"><a href="javascript:onClick_delete();">삭제</a></span>
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
