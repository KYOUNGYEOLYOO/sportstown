<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page session="false" %>
<jsp:useBean id="now" class="java.util.Date" />

<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<meta name="format-detection" content="telephone=no">

<link rel="shortcut icon" href="<c:url value="/resources/images/common/logo.ico"/>" type="image/x-icon" />


<link rel="stylesheet" href="<c:url value="/resources/share/css/default.css"/>" type="text/css" />
<!-- 전체 레이아웃 css begin -->
<link rel="stylesheet" href="<c:url value="/resources/share/css/layout.css"/>" type="text/css" />
<!-- 전체 레이아웃 css end -->
<link rel="stylesheet" href="<c:url value="/resources/share/css/contents.css"/>" type="text/css" />
<link rel="stylesheet" href="<c:url value="/resources/share/css/board.css"/>" type="text/css" />
<link rel="stylesheet" href="<c:url value="/resources/share/css/main.css"/>" type="text/css" />

<script type="text/javascript" src="<c:url value="/resources/js/jquery-1.11.2.min.js"/>"></script>
<script type="text/javascript" src="<c:url value="/resources/js/design.js"/>"></script>
<%-- <script type="text/javascript" src="<c:url value="/resources/js/jquery-ui.js"/>"></script> --%>
<script type="text/javascript" src="<c:url value="/resources/js/Selectyze.jquery.js"/>"></script>


<link href="<c:url value="/bluecap/jquery-ui-1.12.1/jquery-ui.css"/>" rel="stylesheet">
<script type="text/javascript" src="<c:url value="/bluecap/jquery-ui-1.12.1/jquery-ui.js"/>"></script>

<script src="<c:url value="/bluecap/jqUtils/messagebox.js"/>"></script>
<script src="<c:url value="/bluecap/jqUtils/ajaxHTMLLoader.js"/>"></script>
<script src="<c:url value="/bluecap/jqUtils/ctrlEvent.js"/>"></script>
<script src="<c:url value="/bluecap/jquery.form/jquery.form.min.js"/>"></script>



<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/resources/jqueryui/jquery-ui.css"/>" />
<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/resources/jqgrid/css/ui.jqgrid.css"/>" />

<%-- <script src="<c:url value="/resources/jqueryui/jquery-ui.js"/>"></script> --%>
<script src="<c:url value="/resources/jqgrid/js/i18n/grid.locale-en.js"/>"></script>
<script src="<c:url value="/resources/jqgrid/js/jquery.jqGrid.min.js"/>"></script>


<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/bluecap/css/bcsCommon.css"/>"/>

<script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>


<%--
캔버스 기능 20211213
 --%>
<script src="<c:url value="/bluecap/canvas/canvas.js"/>"></script>

<title>대한체육회</title>



<script type="text/javascript">
$(document).ready(function(){
	
	if($('.selectyze').length > 0)
		$('.selectyze').Selectyze({});
	
	
	if($(".date").length > 0)
	{
		$( ".date" ).datepicker({
			showOn: "button",
			buttonImage: "<c:url value="/resources/images/contents/icons_calendar.png"/>",
			buttonImageOnly: true,
			dateFormat: 'yy-mm-dd',
			buttonText: "Select date"
		});

	}

});
</script>
