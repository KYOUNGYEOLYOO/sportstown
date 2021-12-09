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
		url: "<c:url value="/service/camera/getCameras"/>?hasNotUsed=true",
		datatype: "json",
		mtype: "get",
	   	width: "auto",
	   	key: true,
		// height: "auto",
		height: 300,
		autowidth: true,
		viewrecords: true,
		rownumbers: true,
		rowNum: 20,
		rowList: [20,50,100],
	   	colNames:["카메라명",  "카메라위치",  "스포츠종목", "camId"],
	   	colModel:[
	   		{name:"name",index:"name", width:180, align:"center"},
	   		{name:"location.name",index:"location_name", width:180,align:"left"},
	   		{name:"sportsEvent.name",index:"sportsEvent_name", width:180,align:"left"},
	   		// {name:"registDate",index:"registDate", align:"center"},
	   		{name:"camId", index:"camId", hidden:true}
	   	],
	   	pager: $("#${pagerId}"),
	   	jsonReader : {
	   		root : "cameras",
	   		id : "camId"
	   	},
	   	onSelectRow : function(id, status){
	   		var cam = $(this).getRowData(id);
	   		eventSender.send("data-event-selectedRow", cam);
	   	}
	});
});

</script>


