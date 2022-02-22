<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page session="false" %>
<jsp:useBean id="now" class="java.util.Date" />

<script type="text/javascript">

$(document).ready(function(){
	var eventSender = new bcs_ctrl_event($("#${listId}"));
	$("#${listId}").jqGrid({
		// data: mydata,
		// datatype: "local",
		url: "<c:url value="/service/user/getUsers"/>?",
		datatype: "json",
		mtype: "get",
	   	width: "auto",
	   	key: true,
		// height: "auto",
		height: 640, // 원래 300이였음
		autowidth: true,
		viewrecords: true,
		viewsortcols: [false,'vertical',false],
		rownumbers: false,
		rowNum: 12, //20,
		rowList: [20,50,100],
	   	// colNames:["사용자ID", "사용자명", "종목", "등록일자", "userId"],
	   	colNames:["사용자ID", "사용자명", "종목", "사용여부", "userId"],
	   	colModel:[
	   		{name:"loginId",index:"loginId", width:180,align:"left"},
	   		{name:"userName",index:"userName", width:180, align:"left"},
	   		{name:"sportsEvent.name",index:"sportsEventName", width:180, align:"left"},
	   		{name:"isUsed",index:"isUsed", width:180, align:"center"},
	   		// {name:"registDate",index:"registDate", align:"center"},
	   		{name:"userId", index:"userId", hidden:true}
	   	],
// 	   	pager: $("#${pagerId}"),
		loadComplete : function(data){  
		    
		    // 그리드 데이터 총 갯수
		    var allRowsInGrid = jQuery('#${listId}').jqGrid('getGridParam','records');
		   
		    // 데이터가 없을 경우 (먼저 태그 초기화 한 후에 적용)
		    $("#NoData").html("");
		    if(allRowsInGrid==0){
		        $("#NoData").html("<br>데이터가 없습니다.<br>");
		    }
		    // 처음 currentPage는 널값으로 세팅 (=1)
		    initPage("${listId}",allRowsInGrid,"");
		   
		},
	   	jsonReader : {
	   		root : "users",
	   		id : "userId"
	   	},
	   	onSelectRow : function(id, status){
	   		var user = $(this).getRowData(id);
	   		eventSender.send("data-event-selectedRow", user);
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
       
    }  
    //alert(pageInner);
    // 페이징할 DIV태그에 우선 내용을 비우고 페이징 태그삽입
    $("#${pagerId}").html("");	//220222 test
    $("#${pagerId}").append(pageInner);	//220222 test
   
}


//그리드 첫페이지로 이동
function firstPage(){
       
        $("#${listId}").jqGrid('setGridParam', {
                            page:1
                        }).trigger("reloadGrid");
       
}
// 그리드 이전페이지 이동
function prePage(totalSize){
       
        var currentPage = $('#${listId}').getGridParam('page');
        var pageCount = 10;
       
        currentPage-=pageCount;
        pageList=Math.ceil(currentPage/pageCount);
        currentPage=(pageList-1)*pageCount+pageCount;
       
        initPage("${listId}",totalSize,currentPage);
       
        $("#${listId}").jqGrid('setGridParam', {
                            page:currentPage
                        }).trigger("reloadGrid");
       
}


// 그리드 다음페이지 이동    
function nextPage(totalSize){
       
        var currentPage = $('#${listId}').getGridParam('page');
        var pageCount = 10;
       
        currentPage+=pageCount;
        pageList=Math.ceil(currentPage/pageCount);
        currentPage=(pageList-1)*pageCount+1;
       
        initPage("${listId}",totalSize,currentPage);
       
        $("#${listId}").jqGrid('setGridParam', {
                            page:currentPage
                        }).trigger("reloadGrid");
}
// 그리드 마지막페이지 이동
function lastPage(totalSize){
       
        $("#${listId}").jqGrid('setGridParam', {
                            page:totalSize
                        }).trigger("reloadGrid");
}
// 그리드 페이지 이동
function goPage(num){
       
        $("#${listId}").jqGrid('setGridParam', {
                            page:num
                        }).trigger("reloadGrid");
       
}



//  //그리드 페이징
// function initPage(gridId,totalSize,currentPage){
   
//     // 변수로 그리드아이디, 총 데이터 수, 현재 페이지를 받는다
//     if(currentPage==""){
//         var currentPage = $('#'+gridId).getGridParam('page');
//     }
//     console.log("currentPage : ", currentPage);
//     // 한 페이지에 보여줄 페이지 수 (ex:1 2 3 4 5)
//     var pageCount = 10;
//     // 그리드 데이터 전체의 페이지 수
//     var totalPage = Math.ceil(totalSize/$('#'+gridId).getGridParam('rowNum'));
//     console.log("totalPage : ", totalPage);
//     // 전체 페이지 수를 한화면에 보여줄 페이지로 나눈다.
//     var totalPageList = Math.ceil(totalPage/pageCount);
//     // 페이지 리스트가 몇번째 리스트인지
//     var pageList=Math.ceil(currentPage/pageCount);
   
//     //alert("currentPage="+currentPage+"/ totalPage="+totalSize);
//     //alert("pageCount="+pageCount+"/ pageList="+pageList);
   
//     // 페이지 리스트가 1보다 작으면 1로 초기화
//     if(pageList<1) pageList=1;
//     // 페이지 리스트가 총 페이지 리스트보다 커지면 총 페이지 리스트로 설정
//     if(pageList>totalPageList) pageList = totalPageList;
//     // 시작 페이지
//     var startPageList=((pageList-1)*pageCount)+1;
//     // 끝 페이지
//     var endPageList=startPageList+pageCount-1;
   
//     //alert("startPageList="+startPageList+"/ endPageList="+endPageList);
   
//     // 시작 페이지와 끝페이지가 1보다 작으면 1로 설정
//     // 끝 페이지가 마지막 페이지보다 클 경우 마지막 페이지값으로 설정
//     if(startPageList<1) startPageList=1;
//     if(endPageList>totalPage) endPageList=totalPage;
//     if(endPageList<1) endPageList=1;
   
//     // 페이징 DIV에 넣어줄 태그 생성변수
//     var pageInner="";
   
//     // 페이지 리스트가 1이나 데이터가 없을 경우 (링크 빼고 흐린 이미지로 변경)
//     if(pageList<2){
		    	       
// // 		pageInner+="<img src='firstPage2.gif'>";
//         pageInner+="<img src='<c:url value="/resources"/>/images/common/bul_arrow.gif'>";
//         pageInner+="<img src='<c:url value="/resources"/>/images/common/bul_arrow.gif'>";
       
//     }
//     // 이전 페이지 리스트가 있을 경우 (링크넣고 뚜렷한 이미지로 변경)
//     if(pageList>1){
       
//         pageInner+="<a class='first' href='javascript:firstPage()'><img src='<c:url value="/resources"/>/images/common/bul_arrow.gif'></a>";
//         pageInner+="<a class='pre' href='javascript:prePage("+totalSize+")'><img src='<c:url value="/resources"/>/images/common/bul_arrow.gif'></a>";
       
//     }
//     // 페이지 숫자를 찍으며 태그생성 (현재페이지는 강조태그)
//     for(var i=startPageList; i<=endPageList; i++){
//         if(i==currentPage){
//             pageInner = pageInner +"<a href='javascript:goPage("+(i)+")' id='"+(i)+"'><strong>"+(i)+"</strong></a> ";
//         }else{
//             pageInner = pageInner +"<a href='javascript:goPage("+(i)+")' id='"+(i)+"'>"+(i)+"</a> ";
//         }
       
//     }
//     //alert("총페이지 갯수"+totalPageList);
//     //alert("현재페이지리스트 번호"+pageList);
   
//     // 다음 페이지 리스트가 있을 경우
//     if(totalPageList>pageList){
       
//         pageInner+="<a class='next' href='javascript:nextPage("+totalSize+")'><img src='<c:url value="/resources"/>/images/common/bul_arrow.gif'></a>";
//         pageInner+="<a class='last' href='javascript:lastPage("+totalPage+")'><img src='<c:url value="/resources"/>/images/common/bul_arrow.gif'></a>";
//     }
//     // 현재 페이지리스트가 마지막 페이지 리스트일 경우
//     if(totalPageList==pageList){
       
//         pageInner+="<img src='<c:url value="/resources"/>/images/common/bul_arrow.gif'>";
//         pageInner+="<img src='<c:url value="/resources"/>/images/common/bul_arrow.gif'>";
//     }  
//     //alert(pageInner);
//     // 페이징할 DIV태그에 우선 내용을 비우고 페이징 태그삽입
//     $("#${pagerId}").html("");
//     $("#${pagerId}").append(pageInner);
   
// }


// //그리드 첫페이지로 이동
// function firstPage(){
       
//         $("#${listId}").jqGrid('setGridParam', {
//                             page:1
//                         }).trigger("reloadGrid");
       
// }
// // 그리드 이전페이지 이동
// function prePage(totalSize){
       
//         var currentPage = $('#${listId}').getGridParam('page');
//         var pageCount = 10;
       
//         currentPage-=pageCount;
//         pageList=Math.ceil(currentPage/pageCount);
//         currentPage=(pageList-1)*pageCount+pageCount;
       
//         initPage("contentList",totalSize,currentPage);
       
//         $("#${listId}").jqGrid('setGridParam', {
//                             page:currentPage
//                         }).trigger("reloadGrid");
       
// }


// // 그리드 다음페이지 이동    
// function nextPage(totalSize){
       
//         var currentPage = $('#${listId}').getGridParam('page');
//         var pageCount = 10;
       
//         currentPage+=pageCount;
//         pageList=Math.ceil(currentPage/pageCount);
//         currentPage=(pageList-1)*pageCount+1;
       
//         initPage("contentList",totalSize,currentPage);
       
//         $("#${listId}").jqGrid('setGridParam', {
//                             page:currentPage
//                         }).trigger("reloadGrid");
// }
// // 그리드 마지막페이지 이동
// function lastPage(totalSize){
       
//         $("#${listId}").jqGrid('setGridParam', {
//                             page:totalSize
//                         }).trigger("reloadGrid");
// }
// // 그리드 페이지 이동
// function goPage(num){
       
//         $("#${listId}").jqGrid('setGridParam', {
//                             page:num
//                         }).trigger("reloadGrid");
       
// }

</script>


