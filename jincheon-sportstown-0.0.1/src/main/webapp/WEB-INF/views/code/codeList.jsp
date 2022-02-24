<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page session="false"%>
<jsp:useBean id="now" class="java.util.Date" />

<script type="text/javascript">
	$(document).ready(function() {
		
		
		var eventSender = new bcs_ctrl_event($("#${listId}"));
		$("#${listId}").jqGrid({
			// data: mydata,
			// url: "<c:url value="/service/code/getCodes"/>?",
			// datatype: "json",
			mtype : "get",
			width : "auto",
			key : true,
			// height: "auto",
			height : 300,
			autowidth : true,
			viewrecords : true,
			rownumbers : true,

			// data:null,
			datatype : "local",

			rowNum: -1,
			// rowList: [20,50,100],
			// colNames:["사용자ID", "사용자명", "종목", "등록일자", "userId"],
			
			
			colNames : [ "코드명", "사용여부", "파티션여부","codeId" ],
			colModel : [ {
				name : "name",
				index : "loginId",
				width : 30,
				align : "left"
			}, {
				name : "isUsed",
				index : "isUsed",
				width : 30,
				align : "center"
			},{
				name : "isPartition",
				index : "isPartition",
				width : 30,
				align : "center",
				hidden : true
				
			},
			// {name:"registDate",index:"registDate", align:"center"},
			{
				name : "codeId",
				index : "codeId",
				hidden : true
			} ],
			// pager: $("#${pagerId}"),
			jsonReader : {
				root : "codes",
				id : "codeId"
			},
			onSelectRow : function(id, status) {
				var code = $(this).getRowData(id);
				eventSender.send("data-event-selectedRow", code);
			}
		});
	});
</script>


