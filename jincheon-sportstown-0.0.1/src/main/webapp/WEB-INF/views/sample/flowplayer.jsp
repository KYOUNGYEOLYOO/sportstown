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

<link rel="stylesheet" href="<c:url value="/bluecap/flowplayer-7.2.1/skin/skin.css"/>">
<script type="text/javascript" src="<c:url value="/bluecap/flowplayer-7.2.1/flowplayer.min.js"/>"></script>

<script type="text/javascript">

$(document).ready(function(){
	
	var mediaUrl = "rtmp://223.26.218.116:1935/live/ccc.stream";
	flowplayer($("#player"), {
		autoplay : true,
		clip: {
			scaling: 'fit', bufferLength: 60
			,
			sources : [{ type: "application/x-mpegurl", src: mediaUrl}]
		}
	});
});



</script>

</head>
<body>


<div id="dialog" title="수정" class="ui-state-error" style="display:none;">
  <p>받아라 뿅뿅</p>
</div>

<!-- skip navi -->
<div id="skiptoContent">
	<a href="#gnb">주메뉴 바로가기</a>
	<a href="#contents">본문내용 바로가기</a>
</div>
<!-- //skip navi -->

<div id="wrapper">
<!-- header -->
<jsp:include page="/include/top"/>
<!-- //header -->

<!-- container -->
<div id="container">
	<div id="contentsWrap">

		<!-- lnbWrap -->
		<div id="lnbWrap">
			<div class="lnbWraph2">
				<h2>연구소 소개</h2>	
			</div>
			<div class="">
				<select class="selectyze psa" name="camera" id="camera">
					<option value="">카메라선택</option>
					<option value="">All Select</option>
					<option value="">T1 Camera</option>
					<option value="">T2 Camera</option>
					<option value="">T3 Camera</option>
					<option value="">T4 Camera</option>
				</select>
			</div>
			<div class="mgt30">
				<select class="selectyze psa" name="partition" id="partition">
					<option value="">화면분할</option>
					<option value="">1분할</option>
					<option value="">2분할</option>
					<option value="">4분할</option>
					<option value="">9분할</option>
				</select>
			</div>	
			
		</div>
		<!-- //lnbWrap -->
		
		<!-- contents -->
		<div id="contents">

			<!-- title -->
			<h3>영상재생</h3>
			<!-- //title -->

			<div class="vodregistBox">
				<div class="videoview ">
					<div id="ndvrPlayer"></div>	
				</div>
				
				<table class="write_type1 mgb20" summary="">
					<caption></caption>
					<colgroup>
						<col width="150">
						<col width="*">
					</colgroup>
				<tbody>
				<tr>
					<th><label for="ff6">주소</label></th>
					<td><input type="text" id="ndvrUrl" value="http://localhost:1935/live/cam1.stream/playlist.m3u8?DVR" title="제목" class="type_2"> </td>
				</tr>
				</tbody>
				</table>
				
				<div class="btnbox alignR">
					<span class="btn_typeA"><a href="#">연결</a></span>
				</div>
				
				<div class="videoview mgt30 flowplayer">
					<div id="player">
					</div>
				</div>
				
					
			</div>
		</div>
		<!-- //contents -->
	</div>
</div>
<!-- //container -->

<!-- footer -->
<jsp:include page="/include/footer"/>
<!-- //footer -->
</div>

</body>
</html>
