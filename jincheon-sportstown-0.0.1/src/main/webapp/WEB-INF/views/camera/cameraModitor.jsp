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
<jsp:include page="/include/footer"/>

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
		height: 690, // 600 이였음
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
// 	   	pager: $("#p_cameraList"),
		loadComplete : function(data){  
		    
		    // 그리드 데이터 총 갯수
		    var allRowsInGrid = jQuery('#cameraList').jqGrid('getGridParam','records');
		   
		    // 데이터가 없을 경우 (먼저 태그 초기화 한 후에 적용)
		    $("#NoData").html("");
		    if(allRowsInGrid==0){
		        $("#NoData").html("<br>데이터가 없습니다.<br>");
		    }
		    // 처음 currentPage는 널값으로 세팅 (=1)
		    initPage("cameraList",allRowsInGrid,"");
		   
		},
	   	jsonReader : {
	   		root : "cameras",
	   		id : "camId"
	   	},
	   	onSelectRow : function(id){
	   		open_cameraLive(id);
	   	}
	});
});


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
    
   
    //alert("currentPage="+currentPage+"/ totalPage="+totalSize);
    //alert("pageCount="+pageCount+"/ pageList="+pageList);
   
    // 페이지 리스트가 1보다 작으면 1로 초기화
    if(pageList<1) pageList=1;
    // 페이지 리스트가 총 페이지 리스트보다 커지면 총 페이지 리스트로 설정
    if(pageList>totalPageList) pageList = totalPageList;
    // 시작 페이지
    var startPageList=((pageList-1)*pageCount)+1;
    // 끝 페이지
    var endPageList=startPageList+pageCount-1;
   
    //alert("startPageList="+startPageList+"/ endPageList="+endPageList);
   
    // 시작 페이지와 끝페이지가 1보다 작으면 1로 설정
    // 끝 페이지가 마지막 페이지보다 클 경우 마지막 페이지값으로 설정
    if(startPageList<1) startPageList=1;
    if(endPageList>totalPage) endPageList=totalPage;
    if(endPageList<1) endPageList=1;
   
    // 페이징 DIV에 넣어줄 태그 생성변수
    var pageInner="";
   
    // 페이지 리스트가 1이나 데이터가 없을 경우 (링크 빼고 흐린 이미지로 변경)
    if(pageList<2){
       
//	         pageInner+="<a class='btn first' href='javascript:firstPage()'>111</a>";
//	         pageInner+="<a class='btn pre' href='javascript:prePage()'>222</a>";
	       
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
//alert("총페이지 갯수"+totalPageList);
//alert("현재페이지리스트 번호"+pageList);

// 다음 페이지 리스트가 있을 경우
if(totalPageList>pageList){
   
    pageInner+="<a class='btn next' href='javascript:nextPage("+totalSize+")'></a>";
    pageInner+="<a class='btn last' href='javascript:lastPage("+totalPage+")'></a>";
}
// 현재 페이지리스트가 마지막 페이지 리스트일 경우
if(totalPageList==pageList){
   
//	         pageInner+="<a class='btn first' href='javascript:firstPage()'>777</a>";
//	         pageInner+="<a class='btn pre' href='javascript:prePage()'>888</a>";
    }  
    //alert(pageInner);
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
				<div id="NoData"></div>
				<div id="paginate" class="paginate">
					
<!-- 					<a class='btn first'></a> -->
<!-- 					<a class='btn pre'></a> -->
<!-- 					<a class='btn pre none'></a> -->
					
<!-- 					<a><strong>1</strong></a> -->
<!-- 					<a>2</a> -->
					
<!-- 					<a class='btn next none'></a> -->
<!-- 					<a class='btn next'></a> -->
<!-- 					<a class='btn last'></a> -->
				</div>
<!-- 				<div id="p_cameraList" data-ctrl-view="camera_list_pager"></div> -->
			</div>

		</div>

		<!-- //contents -->

	</div>
</div>
<!-- //container -->


<!-- footer -->
<%-- <jsp:include page="./include/footer.jsp" /> --%>
<!-- //footer -->
</div>

</body>
</html>
