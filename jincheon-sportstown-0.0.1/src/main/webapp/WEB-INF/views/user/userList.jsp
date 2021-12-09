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
		height: 300,
		autowidth: true,
		viewrecords: true,
		rownumbers: true,
		rowNum: 20,
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
	   	pager: $("#${pagerId}"),
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

</script>


