<!doctype html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page session="false" %>
<jsp:useBean id="now" class="java.util.Date" />

<html lang="ko" xml:lang="ko">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<script type="text/javascript" src="http://code.jquery.com/jquery-latest.min.js"></script>
<title>진전선수촌 :: sample </title>
<style type="text/css">
	body {color:#666; font:12px/18px 'verdana', 'dotum';}

	h1 {padding:30px 0 15px; color:#000; font-size:22px; text-align:center; font-family:'Malgun Gothic';}
	h2 {padding-left:5px; color:#000; font-size:18px; font-family:'Malgun Gothic';}

	table {clear:both; width:100%; border-collapse:collapse; border-spacing:0; word-break:break-all; word-wrap:break-word;}
	caption {visibility:hidden; width:0; height:0; overflow:hidden; font:0/0 'arial';}
	table thead th {padding:10px 0 9px; color:#fff; border:1px solid #999; background:#666;}
	table tbody th {padding:4px 5px; border:1px solid #999;}
	table tbody td {padding:4px 5px; border:1px solid #999;}
	table tbody td.link {color:#999;font-style:italic;}
	table tbody td.path {font-weight:bold; text-align:center;}
	table tbody td.date {font-style:italic; text-align:center;}
	table tbody td.date del {color:#999;}
	table tbody td.date span {color:#ff005a;}
	table tbody td.note {font-style:italic; color:#ed145b;}
	table tbody td.note span {color:#ff005a;}
	table tbody tr.cate th, table tbody tr.cate td {padding:6px 0; color:#000; background:#d8d8d8 !important;}
	table tbody tr.section th {padding-left:10px; text-align:left;}
	table tbody tr.section th, table tbody tr.section td {background:#eee !important;}
	table tbody tr.on th, table tbody tr.on td {background:#f6f6f6;}

	a {color:#2277ee; text-decoration:none;}
	a:active, a:visited, a:focus, a:hover {color:#2277ee; text-decoration:underline;}
	
	p.txtLT {text-decoration:line-through; padding:0; margin:0;}
</style>

<script type="text/javascript">
	$(document).ready(function() {
		$('table tbody tr').each(function() {
			$(this).mouseover(function(){
				$(this).addClass("on");
			});
			$(this).mouseleave(function(){
				$(this).removeClass("on");
			});
		});
	});
</script>
</head>
<body>

<h1>▶ 진전선수촌 :: sample 목록 ◀</h1>

<table>
  <caption>배치 관련</caption>
	<colgroup>
		<col width="160" />
		<col width="300" />
		<col width="300" />
		<col width="150" />
		<col width="*" />
	</colgroup>
	<thead>
		<tr>
			<th>섹션명</th>
			<th>구분</th>
			<th>URL</th>
			<th>비고</th>
			<th>비고상세</th>
		</tr>
	</thead>
	<tbody>
		<tr class="cate">
			<th># 화면샘플</th>
			<td></td>
			<td class="path"></td>
			<td class="date"></td>
			<td class="note"></td>
		</tr>
		<tr>
			<th></th>
			<td>검색 화면</td>
			<td><a href="<c:url value="/sample/layoutSearch"/>"><c:url value="/sample/layoutSearch"/></a></td>
			<td class="date"></td>
			<td class="note"></td>
		</tr>
		<tr>
			<th></th>
			<td>등록/상세 화면 </td>
			<td><a href="<c:url value="/sample/layoutDetail"/>"><c:url value="/sample/layoutDetail"/></a></td>
			<td class="date"></td>
			<td class="note"></td>
		</tr>
		<tr>
			<th></th>
			<td>영상재생 </td>
			<td><a href="<c:url value="/sample/player"/>"><c:url value="/sample/player"/></a></td>
			<td class="date"></td>
			<td class="note"></td>
		</tr>
		<tr>
			<th></th>
			<td>Flow player</td>
			<td><a href="<c:url value="/sample/flowplayer"/>"><c:url value="/sample/flowplayer"/></a></td>
			<td class="date"></td>
			<td class="note"></td>
		</tr>
		
		<tr class="cate">
			<th># </th>
			<td></td>
			<td class="path"></td>
			<td class="date"></td>
			<td class="note"></td>
		</tr>
	</tbody>
</table>

</body>
</html>