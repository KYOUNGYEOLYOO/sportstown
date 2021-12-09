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



<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/resources/jqueryui/jquery-ui.css"/>" />
<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/resources/jqgrid/css/ui.jqgrid.css"/>" />

<%-- <script src="<c:url value="/resources/jqueryui/jquery-ui.js"/>"></script> --%>
<script src="<c:url value="/resources/jqgrid/js/i18n/grid.locale-en.js"/>"></script>
<script src="<c:url value="/resources/jqgrid/js/jquery.jqGrid.min.js"/>"></script>


<script type="text/javascript">
$(document).ready(function(){
	// grid init sample code
	var mydata = [
			{cname:"사용자1",userid:"UserID1",event:"태권도",onoff:"사용"} ,
			{cname:"사용자2",userid:"UserID2",event:"태권도",onoff:"미사용"} ,
			{cname:"사용자3",userid:"UserID3",event:"태권도",onoff:"사용"} ,
			{cname:"사용자4",userid:"UserID4",event:"태권도",onoff:"사용"} ,
			{cname:"사용자5",userid:"UserID5",event:"태권도",onoff:"미사용"} ,
			{cname:"사용자5",userid:"UserID5",event:"태권도",onoff:"미사용"} ,
			{cname:"사용자5",userid:"UserID5",event:"태권도",onoff:"미사용"} ,
			{cname:"사용자5",userid:"UserID5",event:"태권도",onoff:"미사용"} ,
			{cname:"사용자5",userid:"UserID5",event:"태권도",onoff:"미사용"} ,
			{cname:"사용자5",userid:"UserID5",event:"태권도",onoff:"미사용"} ,
			{cname:"사용자5",userid:"UserID5",event:"태권도",onoff:"미사용"} ,
			{cname:"사용자6",userid:"UserID6",event:"태권도",onoff:"사용"} 

		];
	$("#list_type1").jqGrid({
		data: mydata,
		datatype: "local",
		height: 'auto',
		rowNum: 10,
		rowList: [10,20,30],
	   	//colNames:['Inv No','Date', 'Client', 'Amount','Tax','Total','Notes'],
	   	colNames:['카메라명','ID','종목', '사용여부'],
	   	colModel:[
	   		{name:'cname',index:'cname', width:100,align:"center"},
	   		{name:'userid',index:'userid'},
	   		{name:'event',index:'event', width:100,align:"center"},
	   		{name:'onoff',index:'onoff', width:100,align:"center"}		
	   	],
	   	pager: "#plist_type1",
	   	autowidth: true,
	   	width: 'auto',
	   	viewrecords: true
	});
	
});
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

<!-- container -->
<div id="container">
	<div id="contentsWrap">

		<!-- lnbWrap -->
		<div id="lnbWrap">
			<div class="lnbWraph2">
				<h2>연구소 소개</h2>
			</div>

			<div class="datepickerBox">
				<p>
					<label for="tit">카메라명</label> <input type="text" class="inputTxt" id="tit" name="tit" />
				</p>
			</div>

			<div class="">
				<select class="selectyze psa" name="partition" id="partition">
					<option value="">화면분할</option>
					<option value="">1분할</option>
					<option value="">2분할</option>
					<option value="">4분할</option>
					<option value="">9분할</option>
				</select>
			</div>

			<div class="btnbox ">
				<span class="btn_typeA t3"><a href="#">검색</a></span> 
				<span class="btn_typeA t2"><a href="#">취소</a></span>
			</div>

		</div>
		<!-- //lnbWrap -->

		<!-- contents -->
		<div id="contents">

			<!-- title -->
			<h3>영상녹화</h3>
			<!-- //title -->

			<div class="vodlistBox">
				<table id="list_type1" class="list_type1"></table>
				<div id="plist_type1"></div>
			</div>

			<div class="regist">
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
					<span class="btn_typeA t1"><a href="#">저장</a></span> 
					<span class="btn_typeA t4"><a href="#">삭제</a></span> 
					<span class="btn_typeA t2"><a href="#">취소</a></span>
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
