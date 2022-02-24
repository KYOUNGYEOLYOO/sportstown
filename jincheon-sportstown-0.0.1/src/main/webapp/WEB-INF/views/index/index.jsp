<!doctype html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page session="false"%>
<jsp:useBean id="now" class="java.util.Date" />

<html lang="ko" xml:lang="ko">
<head>

<jsp:include page="/include/head" />


<script type="text/javascript">
$(document).ready(function(){
	
	//alert(${loginUser.userType});	
	//$(location).attr("href", "<c:url value="/record/manage"/>");
	
	columnChartData();
	
	google.charts.load('current', {'packages':['corechart']});
      google.charts.setOnLoadCallback(drawChart);
      google.charts.setOnLoadCallback(columnChart);
      
      function drawChart() {

        var data = google.visualization.arrayToDataTable([
          ['Task', 'Hours per Day'],
          ['Work',     11],
          ['Eat',      2],
          ['Commute',  2],
          ['Watch TV', 2],
          ['Sleep',    7],
          ['Work2',     11],
          ['Eat2',      2],
          ['Commute2',  2],
          ['Watch TV2', 2],
          ['Sleep2',    7]
        ]);

        var options = {
          title: '',
          height : 570,
          width : '100%'

        };

        var chart = new google.visualization.PieChart(document.getElementById('piechart'));

        chart.draw(data, options);
      }
      
      
      function columnChart(){
		var data = google.visualization.arrayToDataTable([
		      ['Genre', 'Fantasy & Sci Fi', 'Romance', 'Mystery/Crime', 'General',
		       'Western', 'Literature', { role: 'annotation' } ],
		      ['2010', 10, 24, 20, 32, 18, 5, ''],
		      ['2020', 16, 22, 23, 30, 16, 9, ''],
		      ['2030', 28, 19, 29, 30, 12, 13, '']
		    ]);

	    var options = {
   		  height : 570,
          width : '100%',
	      legend: { position: 'top', maxLines: 3 },
	      bar: { groupWidth: '75%' },
	      isStacked: true,
	    };
    
  		var chart = new google.visualization.ColumnChart(document.getElementById('columnchart'));

     	chart.draw(data, options);
      }

});

function columnChartData(){
	$.ajax({
		url : "<c:url value="/columnChartData"/>",
		async : false,
		dataType : "json",
		data : null, 
		method : "post",
		beforeSend : function(xhr, settings ){},
		error : function (xhr, status, error){},
		success : function (ajaxData) {
			if(ajaxData.resultCode == "Success"){
				console.log(ajaxData.dashboardDatas);
				
			}else{
				new bcs_messagebox().openError("월별 영상 등록 현황", "데이터 오류 발생 [code="+ajaxData.resultCode+"]", null);
			}
		}
	});
}
</script>
</head>
<body>
<!-- skip navi -->
<div id="skiptoContent">
	<a href="#gnb">주메뉴 바로가기</a>
	<a href="#contents">본문내용 바로가기</a>
</div>
<!-- //skip navi -->

<div id="wrapper">	
<!-- header -->
<jsp:include page="/include/top">
	<jsp:param value="main" name="mainMenu"/>
	<jsp:param value="" name="subMenu"/>
</jsp:include>
<!-- //header -->
<!-- container -->


<div id="container" class="dashboard">
	<div class="titleWrap">
		<h2>대쉬보드</h2>		
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
			<div class="chartContainer">
				<div class="summary">
					<dl>
						<dt>로그인 사용자</dt>
						<dd>123 명</dd>
					</dl>
					<dl>
						<dt>촬영 현황</dt>
						<dd>
							<dl>
								<dt>촬영</dt>
								<dd>123 명</dd>
								<dt>사용</dt>
								<dd>123 명</dd>
								<dt>미사용</dt>
								<dd>123 명</dd>
							</dl>
						</dd>
					</dl>
				</div>
				<div class="chart01">
					<div class="title">
						<h4>월별 영상 등록 현황</h4>
<!-- 						<span>UPLOAD / 조회</span> -->
					</div>
					<div class="" id="columnchart"></div>
				</div>
				<div class="chart02">
					<div class="title">
						<h4>아카이브 영상 저장 분포</h4>
<!-- 						<span>2021년 1월 29일</span> -->
					</div>
					<div class="" id="piechart"></div>
				</div>
			</div>
		</div>
	</div>
</div>
<!-- footer -->
<jsp:include page="/include/footer" />
<!-- //footer -->
</div>	
</body>
</html>