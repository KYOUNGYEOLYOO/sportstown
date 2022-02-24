<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page session="false" %>
<jsp:useBean id="now" class="java.util.Date" />

<style type="text/css">
/* 	캔버스 팝업창 css 요소 */
	.transparent {
		background: transparent;
/* 		border: 1px solid black; */
		}
	#canvasId{
		border: 1px solid black;
	}
</style>

<script type="text/javascript">
$(document).ready(function(){
	$("#canvas").dialog({
		title: "캔버스",  // 타이틀은 사라지고 메뉴가 얇게 들어가면 좋겠습니다.
		modal: true,
		width: '1920',
		height: $("#contentsWrap").height(), // contentsWrap 크기로 설정하고 싶은데 height가 자꾸 auto로 설정이 됩니다...
// 		height: $("#contentsWrap").height(),
		draggable: false,
		resizable: false,
		dialogClass: 'transparent',
	});
	
	$('#canvas').dialog('widget').attr('id', 'canvasId');
	$("#canvas").find(".canvas_menu").click(function(){
		$(this).next().toggle();
	});
	
	$(".canvas_menu").css("display","inline-block");
// 	$("#canvas").css("background","transparent");
	
});
</script>

<html lang="ko" xml:lang="ko">


<body>
<div id="canvas" >
	<div id="canvas_menus" title="캔버스 메뉴">
		<div class="canvas_menu">도형
<!-- 			<li>네모</li> -->
<!-- 			<li>원</li> -->
<!-- 			<li>자유선</li> -->
		</div>
		<div class="canvas_menu">색깔
<!-- 			<li>파랑</li> -->
<!-- 			<li>빨강</li> -->
<!-- 			<li>초록</li> -->
<!-- 			<li>검정</li> -->
<!-- 			<li>하늘색</li> (light blue..?)-->
		</div>
		<div class="canvas_menu">두께
<!-- 			<li>얇은 거</li> -->
<!-- 			<li>보통</li> -->
<!-- 			<li>두꺼운 거</li> -->
		</div>
		<div class="canvas_menu">지우개</div>
		<div class="canvas_menu">초기화</div>
		<div class="canvas_menu">닫기</div>
	</div>
	<div >
	</div>
</div>
</body>
