<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page session="false" %>
<jsp:useBean id="now" class="java.util.Date" />


<script type="text/javascript">

$(document).ready(function(){
	
	$("ul.btnbox li").click(function(){
		var eventSender = new bcs_ctrl_event($("[data-ctrl-view=camera_list]"));
		var camId = $(this).attr("data-camera-camId");
		console.log("this",$(this));
		console.log("camId :", camId);
		if($(this).hasClass("on"))
		{
			if(eventSender.send("data-event-disableCamera", camId))
				$(this).removeClass("on")
		}else
		{
			if(eventSender.send("data-event-enableCamera", camId))
				$(this).addClass("on");
		}
	});
});

</script>

<ul class="btnbox">
<c:forEach items="${staticCameras}"  var="camera" varStatus="status">
		<li class="btn_typeA t3 ex mgt5" data-camera-camId="${camera.camId}" title="${camera.name}"	con="${camera.state}">${camera.name}</li>
<%-- 		<li class="btn_typeA t3 ex mgt5" data-camera-camId="${camera.camId}" title="${camera.name}"	<c:if test="${camera.state eq 'disCon'}">con="${camera.state}"</c:if>>${camera.name}</li> --%>
</c:forEach>
</ul>

<ul class="btnbox mgt30">
<c:forEach items="${shiftCameras}"  var="camera" varStatus="status">
	<li class="btn_typeA t3 ex mgt5" data-camera-camId="${camera.camId}" title="${camera.name}">${camera.name}</li>
</c:forEach>
</ul>
