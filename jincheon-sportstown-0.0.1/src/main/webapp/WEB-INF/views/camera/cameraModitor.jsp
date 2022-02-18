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
	
});
</script>


<script type="text/javascript">




function onClick_search()
{
	$("#cameraList").jqGrid("setGridParam", {
		url : "<c:url value="/service/camera/getCameras"/>?" + $("#frmSearch").serialize(),
		page : 1
	});
	$("#cameraList").trigger("reloadGrid");
	
}

function onClick_searchInit()
{
	location.reload();
	
}

</script>



<script type="text/javascript">

$(document).ready(function(){
	var eventSender = new bcs_ctrl_event($("#cameraList"));
	$("#cameraList").jqGrid({
		// data: mydata,
		url: "<c:url value="/service/camera/getCameras"/>?hasNotUsed=true",
		datatype: "json",
		mtype: "get",
	   	width: "auto",
	   	key: true,
		// height: "auto",
		height: 600,
		autowidth: true,
		viewrecords: true,
		viewsortcols: [false,'vertical',false],
		rownumbers: false,
		rowNum: 20,
		rowList: [20,50,100],
	   	colNames:["카메라명",  "카메라위치",  "스포츠종목", "녹화상태", "camId"],
	   	colModel:[
	   		{name:"name",index:"name", width:180, align:"center"},
	   		{name:"location.name",index:"location_name", width:180,align:"left"},
	   		{name:"sportsEvent.name",index:"sportsEvent_name", width:180,align:"left"},
	   		{name:"state",index:"state", width:60,align:"center",
	   			formatter: function (cellvalue, options, rowObject) {
					console.log(rowObject);
					var color = "#c9c9c9";
					var text = "대기중";
					if(rowObject.state == "Wait")
					{
						color = "#c9c9c9";
						text = "대기중";
					}else{
						color = "#F90F0F";
						text = "녹화중";
					}
					
                	return "<div style='background-color:"+color+"; height:100%;' title='"+text+"'>&nbsp;</div>";
            	}
	   		},
	   		{name:"camId", index:"camId", hidden:true}
	   	],
	   	pager: $("#p_cameraList"),
	   	jsonReader : {
	   		root : "cameras",
	   		id : "camId"
	   	},
	   	onSelectRow : function(id){
	   		open_cameraLive(id);
	   	}
	});
});

function open_cameraLive(camId)
{
	window.open("<c:url value="/camera/live"/>/" + camId, "popup", "height=750,width=910,resizable=no,menubar=no,toolbar=no", true);
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



<!-- container -->
<div id="container">
	<div id="contentsWrap">
	
		<!-- lnbWrap -->
		<div id="lnbWrap">
			<form id="frmSearch" onSubmit="return false;">
				<input type="hidden" name="hasNotUsed" value="true" />
				<div class="lnbWraph2">
					<h2>카메라관리</h2>
				</div>
				<div class="datepickerBox">
					<p>
						<label for="search_keyword">카메라명</label> 
						<input type="text" class="inputTxt" id="search_keyword" name="keyword" />
					</p>
				</div>
				
				<div class="">
					<select class="selectyze psa" name="cameraType">
						<option value="">카메라유형</option>
						<option value="Static">고정</option>
						<option value="Shift">유동</option>
					</select>
				</div>
				
				<div class="">
					<select class="selectyze psa" name="locationCode">
						<option value="">카메라위치</option>
						<c:forEach items="${locations}" var="location">
							<option value="${location.codeId}">${location.name}</option>
						</c:forEach>
					</select>
				</div>
			
				<div class="">
					<select class="selectyze psa" name="sportsEventCode">
						<option value="">스포츠종목</option>
						<c:forEach items="${sprotsEvents}" var="sprotsEvent">
							<option value="${sprotsEvent.codeId}">${sprotsEvent.name}</option>
						</c:forEach>
					</select>
				</div>
				
				
			</form>
			<div class="btnbox alignC" style="text-align: center;">
				<span class="btn_typeA t3"><a href="javascript:onClick_search();">검색</a></span> 
				<span class="btn_typeA t2"><a href="javascript:onClick_searchInit();">조건초기화</a></span>
			</div>
			
			
		</div>
		<!-- //lnbWrap -->

		<!-- contents -->
		<div id="contents">
			<div class="vodlistBox">
				<table id="cameraList" class="list_type1" data-ctrl-view="camera_list" data-event-selectedRow="onSelected_cameraListItem"></table>
				<div id="p_cameraList" data-ctrl-view="camera_list_pager"></div>
			</div>

		</div>

		<!-- //contents -->

	</div>
</div>
<!-- //container -->


<!-- footer -->
<jsp:include page="./include/footer.jsp" />
<!-- //footer -->
</div>

</body>
</html>
