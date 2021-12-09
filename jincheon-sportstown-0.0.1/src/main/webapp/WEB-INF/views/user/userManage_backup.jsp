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

<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/bluecap/css/bcsCommon.css"/>"/>

<script src="<c:url value="/bluecap/jqUtils/messagebox.js"/>"></script>
<script src="<c:url value="/bluecap/jqUtils/ajaxHTMLLoader.js"/>"></script>
<script src="<c:url value="/bluecap/jqUtils/ctrlEvent.js"/>"></script>


<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/resources/jqueryui/jquery-ui.css"/>" />
<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/resources/jqgrid/css/ui.jqgrid.css"/>" />

<%-- <script src="<c:url value="/resources/jqueryui/jquery-ui.js"/>"></script> --%>
<script src="<c:url value="/resources/jqgrid/js/i18n/grid.locale-en.js"/>"></script>
<script src="<c:url value="/resources/jqgrid/js/jquery.jqGrid.min.js"/>"></script>

<script type="text/javascript">
$(document).ready(function(){
	$("#userList").jqGrid({
		// data: mydata,
		// datatype: "local",
		url: "<c:url value="/service/user/getUsers"/>",
		datatype: "json",
		mtype: "get",
	   	width: "auto",
		// height: "auto",
		height: 300,
		autowidth: true,
		viewrecords: true,
		rownumbers: true,
		rowNum: 20,
		rowList: [20,50,100],
	   	colNames:["사용자ID", "사용자명", "등록일자", "userId"],
	   	colModel:[
	   		{name:"loginId",index:"loginId", width:180,align:"left"},
	   		{name:"userName",index:"userName", width:180, align:"left"},
	   		{name:"registDate",index:"registDate", align:"center"},
	   		{name:"userId", index:"userId", hidden:true}
	   	],
	   	pager: "#p_userList",
	   	jsonReader : {
	   		root : "users",
	   		id : "userId"
	   	},
	   	onSelectRow : function(id){
	   		var user = $(this).getRowData(id);
	   		console.log(user);
	   	}
	});
});

// initialize regist dialog
$(document).ready(function(){
});

// initialize modify dialog
$(document).ready(function(){
});
</script>


<script>
function onSelected_userListItem(sender, rowData)
{
	console.log(rowData);
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
}
</script>

<script type="text/javascript">

function callback_regist(sender, user)
{
	$("#userList").trigger("reloadGrid");
}

function callback_modify(sender, user)
{
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
<jsp:include page="/include/top" />
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
			<div class="lnbWraph2">
				<h2>사용자 관리</h2>
			</div>
			<div class="datepickerBox">
				<p>
					<label for="tit">사용자명</label> 
					<input type="text" class="inputTxt" id="tit" name="tit" />
				</p>
			</div>

			<div class="btnbox alignC" style="text-align: center;">
				<span class="btn_typeA t3"><a href="#">검색</a></span> 
<!-- 				<span class="btn_typeA t2"><a href="#">취소</a></span> -->
			</div>

		</div>
		<!-- //lnbWrap -->

		<!-- contents -->
		<div id="contents">

			<!-- title -->
			<h3>영상녹화</h3>
			<!-- //title -->

			<div class="vodlistBox">
				<table id="userList" class="list_type1" data-ctrl-view="user_list" data-event-selectedRow="onSelected_userListItem"></table>
				<div id="p_userList" data-ctrl-view="user_list_paging"></div>
			</div>

			<div>
				<table class="write_type1 mgt30 mgb20" summary="">
					<caption></caption>
					<colgroup>
						<col width="150">
						<col width="*">
					</colgroup>
					<tbody>
						<tr>
							<th><label for="ip1">카메라명</label></th>
							<td><input type="text" id="ip1" name="" value="" title="사용자명" class="type_2"></td>
						</tr>
						<tr>
							<th><label for="ip2">ID</label></th>
							<td><input type="text" id="ip2" name="" value="" title="ID" class="type_2"></td>
						</tr>
						<tr>
							<th><label for="ip3">Password</label></th>
							<td><input type="password" id="ip3" name="" value="" title="Password" class="type_2"></td>
						</tr>
						<tr>
							<th><label for="st1">스포츠종목</label></th>
							<td>
								<select class="td sel_type_2" id="st1" name="" title="선수명 선택">
									<option>태권도</option>
									<option>수영</option>
									<option>유도</option>
								</select>
							</td>
						</tr>
						<tr>
							<th><label for="st2">건물</label></th>
							<td>
								<select class="td sel_type_2" id="st2" name="" title="선수명 선택">
									<option>관리자</option>
								</select>
							</td>
						</tr>
						<tr>
							<th><label for="ip4">저장위치</label></th>
							<td><input type="text" id="ip4" name="" value="" title="ID" class="type_2"></td>
						</tr>
					</tbody>
				</table>

				<div class="btnbox alignR">
					<span class="btn_typeA t1"><a href="javascript:onClick_regist();">등록</a></span> 
					<span class="btn_typeA t4"><a href="javasctip:onClick_modify();">수정</a></span> 
					<span class="btn_typeA t2"><a href="#">삭제</a></span>
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
