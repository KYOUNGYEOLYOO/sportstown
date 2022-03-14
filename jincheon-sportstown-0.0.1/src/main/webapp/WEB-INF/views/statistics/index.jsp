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
	
	yearsData();
	
	contentCnt();
});

function contentCnt(){
	$.ajax({
		url : "<c:url value="/service/index/contentCnt"/>",
		async : false,
		dataType : "json",
		data : null, 
		method : "post",
		beforeSend : function(xhr, settings ){},
		error : function (xhr, status, error){},
		success : function (ajaxData) {
			if(ajaxData.resultCode == "Success"){
				$('#allContent').html(ajaxData.allContent[0]+" 건");
				$('#todayContent').html(ajaxData.todayContent[0]+" 건");

			    
			}else{
				new bcs_messagebox().openError("컨텐츠 정보", "컨텐츠 정보 조회 오류 [code="+ajaxData.resultCode+"]", null);
			}
		}
	});
}

function yearsData(){
	var today = new Date();
	
	var year = today.getFullYear();	
	
	var html ="";
	
	
	for(var i = Number(year) ; i >= 2016 ; i--){
		if(i == Number(year)){
			html += '<option value="'+i+'" selected>'+i+'년</option>';
		}else{
			html += '<option value="'+i+'">'+i+'년</option>';
		}
		//html += '<option value="'+i+'">'+i+'년</option>';
	}
	$('#selectYear').html(html);
	
	month();
	
}

function month(){
	
	var year = $('#selectYear').val();
	
	$.ajax({
		url : "<c:url value="/service/index/monthData"/>/"+year,
		async : false,
		dataType : "json",
		data : null, 
		method : "post",
		beforeSend : function(xhr, settings ){},
		error : function (xhr, status, error){},
		success : function (ajaxData) {
			if(ajaxData.resultCode == "Success"){
				console.log(ajaxData.dashboardDatas);
				
				for(var i=1; i <=12; i++){
					
					if(i < 10){
						i = "0"+i;
					}
					
					$('#'+i+'_area').html("0");
				}
				
				var data = ajaxData.dashboardDatas;
				
				for(var i=0; i < data.length; i++){
					
					var temp = data[i];
					
					$('#'+temp[0]+'_area').html(temp[1]);
				}
				
				
			    
			}else{
				new bcs_messagebox().openError("월별 등록 현황", "데이터 오류 발생 [code="+ajaxData.resultCode+"]", null);
			}
		}
	});
}

var statList = new Array();


function monthClick(value){
	
	statList = new Array();
	var year = $('#selectYear').val();
	
	$.ajax({
		url : "<c:url value="/service/statistics/sportsEventCode"/>/"+year+"/"+value,
		async : false,
		dataType : "json",
		data : null, 
		method : "post",
		beforeSend : function(xhr, settings ){},
		error : function (xhr, status, error){},
		success : function (ajaxData) {
			if(ajaxData.resultCode == "Success"){
				
				for(var i =0; i < ajaxData.dashboardDatas.length; i++){
					
					var temp = ajaxData.dashboardDatas[i];
					statList[i] = temp;
				}
			    
				
				typeCount(value);
			}else{
				new bcs_messagebox().openError("월별 종목 현황", "데이터 오류 발생 [code="+ajaxData.resultCode+"]", null);
			}
		}
	});
	
	
}

function typeCount(value){
var year = $('#selectYear').val();
	
	$.ajax({
		url : "<c:url value="/service/statistics/typeCount"/>/"+year+"/"+value,
		async : false,
		dataType : "json",
		data : null, 
		method : "post",
		beforeSend : function(xhr, settings ){},
		error : function (xhr, status, error){},
		success : function (ajaxData) {
			if(ajaxData.resultCode == "Success"){
				
				
				
				for(var z = 0; z < statList.length; z++){
					
					var statTemp = statList[z];
					
					var arCnt = 0;
					var rgCnt = 0;
					var vCnt = 0;
					var dCnt = 0;
					
					for(var i =0; i < ajaxData.dashboardDatas.length; i++){
						
						var temp = ajaxData.dashboardDatas[i];
						if(statTemp[1] == temp[0]){
							
							if(temp[1] == 'Contents'){
								rgCnt = Number(rgCnt)+1;
							}
							if(temp[1] == 'Archive'){
								arCnt = Number(arCnt)+1;
							}
							if(temp[1] == 'View'){
								vCnt = Number(vCnt)+1;
							}
							if(temp[1] == 'Download'){
								dCnt = Number(dCnt)+1;
							}
							
// 							statList[z].push(temp[1]);
						}
					}
					
					statList[z].push(rgCnt,arCnt,vCnt,dCnt );
				}
				
				goList();
				
			}else{
				new bcs_messagebox().openError("월별 종목 현황", "데이터 오류 발생 [code="+ajaxData.resultCode+"]", null);
			}
		}
	});
}

function goList(){
	$('#tableDatas').html("");
	
	var html ='';

	for(var i=0; i<statList.length; i++ ){
		var temp =statList[i];
		
		html +='<tr style="border-bottom: 1px solid #eaeaea;">';
		html +='<td>'+temp[0]+'</td>';
		html +='<td>'+temp[3]+'</td>';
		html +='<td>'+temp[2]+'</td>';
		html +='<td>'+temp[4]+'</td>';
		html +='<td>'+temp[5]+'</td>';
		html +='</tr>';
	}

	$('#tableDatas').html(html);
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
	<jsp:param value="manage" name="mainMenu"/>
	<jsp:param value="statisticsManage" name="subMenu"/>
</jsp:include>
<!-- //header -->
<!-- container -->


<div id="container" class="dashboard">
	
	<div class="titleWrap">
		<h2>통계</h2>		
		<dl>
			<dt>오늘의 영상 등록 건수</dt>
			<dd id="todayContent">0 건</dd>
			<dt>총 등록 영상 건수</dt>
			<dd id="allContent">0 건</dd>
		</dl>		
	</div>
	<div id="contentsWrap">
		<div id="contents">
			<div class="tableContainer">
				<select name="selectYear" id="selectYear" onChange="javascipt:month();">
					
				</select>
				<table>
					<caption></caption>
					<colgroup>
						<col span="12" />
					</colgroup>
				
					<thead>
						<tr>
							<th onClick="javascript:monthClick('01')">1월</th>
							<th onClick="javascript:monthClick('02')">2월</th>
							<th onClick="javascript:monthClick('03')">3월</th>
							<th onClick="javascript:monthClick('04')">4월</th>
							<th onClick="javascript:monthClick('05')">5월</th>
							<th onClick="javascript:monthClick('06')">6월</th>
							<th onClick="javascript:monthClick('07')">7월</th>
							<th onClick="javascript:monthClick('08')">8월</th>
							<th onClick="javascript:monthClick('09')">9월</th>
							<th onClick="javascript:monthClick('10')">10월</th>
							<th onClick="javascript:monthClick('11')">11월</th>
							<th onClick="javascript:monthClick('12')">12월</th>
						</tr>
					</thead>
					<tbody>
					<tr>
						<td id="01_area" onClick="javascript:monthClick('01')">0</td>
						<td id="02_area" onClick="javascript:monthClick('02')">0</td>
						<td id="03_area" onClick="javascript:monthClick('03')">0</td>
						<td id="04_area" onClick="javascript:monthClick('04')">0</td>
						<td id="05_area" onClick="javascript:monthClick('05')">0</td>
						<td id="06_area" onClick="javascript:monthClick('06')">0</td>
						<td id="07_area" onClick="javascript:monthClick('07')">0</td>
						<td id="08_area" onClick="javascript:monthClick('08')">0</td>
						<td id="09_area" onClick="javascript:monthClick('09')">0</td>
						<td id="10_area" onClick="javascript:monthClick('10')">0</td>
						<td id="11_area" onClick="javascript:monthClick('11')">0</td>
						<td id="12_area" onClick="javascript:monthClick('12')">0</td>
					</tr>
				</tbody>
				</table>
			</div>
			
			<div class="tableContainer" style="height: 636px;overflow: auto;">
				
				<table>
					<caption></caption>
					<colgroup>
						<col span="5" />
					</colgroup>
				
					<thead>
						<tr>
							<th>종목</th>
							<th>아카이브수</th>
							<th>등록수</th>
							<th>조회수</th>
							<th>다운로드수</th>
							
						</tr>
					</thead>
					<tbody id="tableDatas">
					
					</tbody>
				</table>
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