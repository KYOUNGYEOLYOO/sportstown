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

<script type="text/javascript" src="<c:url value="/bluecap/jwplayer/jwplayer.js"/>"></script>
<script>jwplayer.key="uL+sf8LV4JfO0X1+U8YPbC7PoiiNX730vh3pnQ==";</script>
<!-- <script type='text/javascript' src="http://jwplayer.mediaserve.com/jwplayer.js"></script> -->

<style type="text/css">

</style>

<script type="text/javascript">

$(document).ready(function(){
	
	initPalyer();
});
</script>


<c:set var="streamUrl" value=""/>
<c:set var="streamFile" value=""/>

<c:forEach items="${contentMeta.content.instances}" var="instance" end="0">
	<c:set var="streamUrl" value="${vodStreamer}/${contentRootUri}"/>
	<c:set var="streamFile" value="${instance.file.fileName}"/>
</c:forEach>

<script type="text/javascript">


function initPalyer()
{
	
	
	var  mediaUrl = "${vodStreamer}/mp4:${contentRootUri}${contentMeta.content.instances[0].file.filePath}${contentMeta.content.instances[0].file.fileName}"
	// mediaUrl = "<c:url value="/resources/mp4/sample.mp4"/>";
	console.log(mediaUrl);
	
	jwplayer("player").setup({
		"file" : mediaUrl,
		"width" : 850,
		"height" : 437,
		'provider': 'rtmp',
		autostart : true
	});
}


function initPalyer_org()
{
	console.log('${streamUrl} / ${streamFile}');
	var mediaUrl = "<c:url value="/resources/mp4/sample.mp4]"/>";
	jwplayer("player").setup({
		'id': 'playerID',
		'width': '850',
		'height': '437',
		'provider': 'rtmp',
		// 'streamer': '${streamUrl}',
		'file': metidUrl,
		'controlbar': 'none',
		'modes': [
			{type: 'flash', src: 'http://jwplayer.mediaserve.com/player.swf'}, 
			{type: 'html5'}
		],
		'plugins': { 'viral-2': {'oncomplete':'False','onpause':'False','functions':'All'} }

	});
	
	jwplayer("player").play();
}

</script>

<script type="text/javascript">

function onClick_close()
{
	self.opener = self;
	window.close();
}
</script>




</head>
<body>

<div class="popupWindow">
	<div class="popupheader">영상 상세 정보</div>
	<div class="popupcontents">
		<div class="vodregistBox">


			<div class="vodregist ">
				<div class="videoview mgb30">
					<div id="player"></div>	
				</div>

				<table class="write_type1 mgb20" summary="">
					<caption></caption>
					<colgroup>
					<col width="150">
					<col width="*">
					<col width="150">
					<col width="*">
					</colgroup>
					<tbody>
						<tr>
							<th>제목</th>
							<td colspan="3">
								<input type="text" name="title" title="제목" class="type_2" value="${contentMeta.title}" readonly>
							</td>
						</tr>
						<tr>
							<th>스포츠종목</th>
							<td>
								<input type="text" name="sportsEvent" title="제목" class="type_2" value="${contentMeta.sportsEvent.name}" readonly>
							</td>
							<th>소유자</th>
							<td>
								<input type="text" name="tagUser" title="제목" class="type_2" value="${contentMeta.contentUserNames}" readonly>
							</td>
						</tr>
						<tr>
							<th>녹화자</th>
							<td>
								<input type="text" name=recordUser title="제목" class="type_2" value="${contentMeta.recordUser.userName}" readonly>
							</td>
							<th>녹화일자</th>
							<td>
								<input type="text" class="type_2" name="recordDate" value="${contentMeta.recordDate}" readonly/>
							</td>
						</tr>
						<tr>
							<th>설명</th>
							<td colspan="3">
								<textarea name="summary" title="설명" class="ta_type_1" readonly>${contentMeta.summary}</textarea>
							</td>
						</tr>
					</tbody>
				</table>

				<div class="btnbox alignR">
<!-- 					<span class="btn_typeA"><a href="#">다운로드</a></span> <span -->
<!-- 					<span class="btn_typeA t1"><a href="#">저장</a></span> <span -->
						<span class="btn_typeA t2"><a href="javascript:onClick_close();">닫기</a></span>
				</div>
			</div>

		</div>
	</div>


</div>



</body>
</html>
