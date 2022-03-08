<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page session="false" %>
<jsp:useBean id="now" class="java.util.Date" />

<style type="text/css">
.ui-th-div .cbox {display: none;}
.ui-jqgrid .cbox {vertical-align: middle;}
</style>
<script type="text/javascript">

$(document).ready(function(){
	
	var eventSender = new bcs_ctrl_event($("[data-ctrl-view=file_search]"));
	
	
	var $grid = $("#grid_fileList");
	var $pager = $("#pager_fileList");
	
	/* 2017.07.17 : 파일 검색 제거
	
	var $ed_keyword = $form_search.find("[name=keyword]");
	$ed_keyword.keyup(function(e){
		
		var code = (e.keyCode ? e.keyCode : e.which);
	    if (code==13) {
	    	fn_reloadFiles();
	    }
		
	});
	*/
	
	
	var $form_search = $("[data-ctrl-view=file_search]").find("form");
	
	var fn_reloadFiles = function(){ 
		var params = $form_search.serialize();
		$grid.jqGrid("setGridParam", {
			url: "<c:url value="/service/file/getFileList"/>?"+params,
			datatype: "json",
		}).trigger("reloadGrid");
	};
	
	
	// 영상검색 나오는 jqgrid
	$grid.jqGrid({
		// url: "<c:url value="/service/file/getFileList"/>?"+params,
		datatype: "local",
		mtype: "get",
	   	width: "auto",
	   	key: true,
		// height: "auto",
		height: 300,
		autowidth: true,
		viewrecords: true,
		viewsortcols: [false,'vertical',false],
		rownumbers: false,
// 		rowNum: 10,
	   	// colNames:["사용자ID", "사용자명", "종목", "등록일자", "userId"],
	   	colNames:["썸네일", "파일명", "fileId", "filePath", "fileName"],
	   	colModel:[
	   		{name:"thumbnail",index:"thumbnail", width:50,align:"center", 
				formatter: function (cellvalue, options, rowObject) {
                	return "<img src='<c:url value="/file/thumbnail"/>/"+rowObject.fileId+"' height='100%' width='100%'/>";
            	}
			},
	   		{name:"orignFileName",index:"orignFileName",align:"left"},
	   		{name:"fileId", index:"fileId", hidden:true},
	   		{name:"filePath", index:"filePath", hidden:true},
	   		{name:"fileName", index:"fileName", hidden:true}
	   	],
	   	multiselect:true,
// 	   	pager: $pager, //0223 페이져 삭제
	   	jsonReader : {
	   		root : "files",
	   		id : "fileId"
	   	},
	   	
	   	loadComplete : function (){
	   		var ids = $grid.jqGrid("getDataIDs");
	   		$.each(ids, function (index, id){
	   			var isChecked = eventSender.send("data-event-checkSelectedFile", id);
	   			if(isChecked)
	   			{
		   			$grid.jqGrid("setSelection", id, isChecked);
	   			}
	   		});
	   		
	   	},
	   	
	   	onSelectRow : function(id, status){
	   		
	   		var file = $grid.jqGrid("getRowData", id);
	   		
	   		var isChecked = $("#jqg_" + $grid.attr("id") + "_" + id).is(":checked");
	   		var isSus = false;
	   		
	   		console.log(isChecked);
	   		if(isChecked)
	   			isSus = eventSender.send("data-event-selectedFile", file);
	   		else
	   			isSus = eventSender.send("data-event-unselectedFile", file);
	   		
	   		
	   		if(isSus != true)
	   		{
	   			$grid.jqGrid("setSelection", id, !isChecked);
	   		}
	   	}
	   	
	});
	
	/* 여기가 녹화재생 > 영상추가 버튼 혹은 초기화 버튼 누르면 나오는 영상 검색 페이지 조그만거 20211210 */
	$("[data-ctrl-view=file_search]").dialog({
		width:700,
		modal : true,
		authOpen :true,
		resizable : false,
		// position: "top",
		buttons : {
			
			"닫기" : function(){
				$(this).dialog("close");
			},
			"초기화" : function(){
				eventSender.send("data-event-initMedia");	//초기화 기능 삽입
			}			
		},
		close : function(event, ui){
			$(this).dialog("destroy");
			$("[data-ctrl-view=file_search]").empty();
		},
		open: function(event, ui){
			var dlgWidth = $(this).width();
			$grid.jqGrid("setGridWidth", dlgWidth);
			
			fn_reloadFiles();
		}
	});
	
	$('[data-ctrl-view=file_search]').dialog('widget').attr('id', 'fsDialogId');
});

</script>

<form onsubmit="return false;">
<!--  2017.07.17 : 파일 검색 제거 -->
	<input type="hidden" name="sportsEventCode" value="${condition.sportsEventCode}" />
<!-- 	<table class="write_type1 mgb20" summary=""> -->
<%-- 		<caption></caption> --%>
<%-- 		<colgroup> --%>
<%-- 			<col width="150"> --%>
<%-- 			<col width="*"> --%>
<%-- 		</colgroup> --%>
<!-- 		<tbody> -->
<!-- 			<tr> -->
<!-- 				<th>검색</th> -->
<!-- 				<td><input type="text" name="keyword" value="" title="키워드" class="type_2"></td> -->
<!-- 			</tr> -->
<!-- 		</tbody> -->
<!-- 	</table> -->
</form>

<div class="vodlistBox thum">
<table id="grid_fileList" class="list_type1"></table>
<div id="pager_fileList" data-ctrl-view="content_list_pager"></div>
</div>
