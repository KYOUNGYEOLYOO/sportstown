<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page session="false" %>
<jsp:useBean id="now" class="java.util.Date" />

<script type="text/javascript">



</script>

<html lang="ko" xml:lang="ko">
<head>

<jsp:include page="/include/head"/>
</head>
<body>
<div id="wrapper">	
<!-- header -->
<jsp:include page="/include/top">
	<jsp:param value="manage" name="mainMenu"/>
	<jsp:param value="cameraManage" name="subMenu"/>
</jsp:include>
<!-- //header -->
<div id="container" class="dashboard">
	<div class="titleWrap">
		<h2>통계</h2>		
		<dl>
			<dt>오늘의 영상 등록 건수</dt>
			<dd>123 건</dd>
			<dt>총 등록 영상 건수</dt>
			<dd>12,123 건</dd>
		</dl>		
	</div>
	<div id="contentsWrap">
		<div id="contents">
			<div class="tableContainer">
				<select>
					<option>2021년</option>
				</select>
				<table>
					<caption></caption>
					<colgroup>
						<col span="12" />
					</colgroup>
				
					<thead>
						<tr>
							<th>1월</th>
							<th>2월</th>
							<th>3월</th>
							<th>4월</th>
							<th>5월</th>
							<th>6월</th>
							<th>7월</th>
							<th>8월</th>
							<th>9월</th>
							<th>10월</th>
							<th>11월</th>
							<th>12월</th>
						</tr>
					</thead>
					<tbody>
					<tr>
						<td>123</td>
						<td>123</td>
						<td>123</td>
						<td>123</td>
						<td>123</td>
						<td>123</td>
						<td>123</td>
						<td>123</td>
						<td>123</td>
						<td>123</td>
						<td>123</td>
						<td>123</td>
					</tr>
				</tbody>
				</table>
			</div>			
			<div class="listBox"></div>
		</div>
	</div>
</div>
<!-- footer -->
<jsp:include page="/include/footer" />
<!-- //footer -->
</div>
</body>
