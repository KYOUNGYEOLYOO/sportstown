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
	
	loginCnt();
	
	contentCnt();
	
	drawChart();
	
	cameraList();
	
	diskInfo();
});

function diskInfo(){
	$.ajax({
		url : "<c:url value="/service/index/diskInfo"/>",
		async : false,
		dataType : "json",
		data : null, 
		method : "post",
		beforeSend : function(xhr, settings ){},
		error : function (xhr, status, error){},
		success : function (ajaxData) {
			if(ajaxData.resultCode == "Success"){
// 				console.log("====>>>>>");
// 				console.log(ajaxData);

				if(ajaxData.storageInfos.length > 0){
					$('#diskInfoTitle').html("NAS 사용량 <font color='#ff0000'>( "+ajaxData.storageInfos[0].useInfo+" TB / "+ajaxData.storageInfos[0].totalInfo+" TB )</font> 및 영상 저장 분포");
					$('#progressVar').attr('value',ajaxData.storageInfos[0].useInfo);
					$('#progressVar').attr('max',ajaxData.storageInfos[0].totalInfo);
				}
				
			}else{
				new bcs_messagebox().openError("disk 정보", "disk 정보  오류 [code="+ajaxData.resultCode+"]", null);
			}
		}
	});
}

function cameraList(){
	$.ajax({
		url: "<c:url value="/service/camera/getCameras"/>?hasNotUsed=true&stateString=All",
		async : false,
		dataType : "json",
		data : null, 
		method : "post",
		beforeSend : function(xhr, settings ){},
		error : function (xhr, status, error){},
		success : function (ajaxData) {
			if(ajaxData.resultCode == "Success"){
				
				
				var recording = 0;
				var wait = 0;
				var ready = 0;
				
				for(var i=0; i < ajaxData.cameras.length; i++){
					if(ajaxData.cameras[i].state == "Wait"){
						wait = Number(wait)+ Number(1);
					}else if(ajaxData.cameras[i].state == "Recording"){
						recording = Number(recording)+ Number(1);
				}else{
						ready = Number(ready)+ Number(1);
					}
				}
				
				$('#wait').html("<font color='#333333'>"+wait+" 대</font>");
				$('#recording').html("<font color='#ff0000'>"+recording+" 대</font>");
				$('#ready').html("<font color='#d3d3d3'>"+ready+" 대</font>");
			    
			}else{
				new bcs_messagebox().openError("cameraList 정보", "cameraList 정보 조회 오류 [code="+ajaxData.resultCode+"]", null);
			}
		}
	});
}

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

function loginCnt(){
	$.ajax({
		url : "<c:url value="/service/index/loginCnt"/>",
		async : false,
		dataType : "json",
		data : null, 
		method : "post",
		beforeSend : function(xhr, settings ){},
		error : function (xhr, status, error){},
		success : function (ajaxData) {
			if(ajaxData.resultCode == "Success"){
				
				$('#loginCnt').html(ajaxData.loginDatas.length+" 명");
			    
			}else{
				new bcs_messagebox().openError("로그인 정보", "로그인 정보 조회 오류 [code="+ajaxData.resultCode+"]", null);
			}
		}
	});
}

function selectBestCode(year){
	$.ajax({
		url : "<c:url value="/service/index/selectBestCode"/>/"+year,
		async : false,
		dataType : "json",
		data : null, 
		method : "post",
		beforeSend : function(xhr, settings ){},
		error : function (xhr, status, error){},
		success : function (ajaxData) {
			if(ajaxData.resultCode == "Success"){
				
				var code = ajaxData.dashboardData[0];
				
				if(code == null){
					selectCode("000");
				}else{
					selectCode(code[0]);
				}
				
				
				
			    
			}else{
				new bcs_messagebox().openError("스포츠코드", "스포츠코드 조회 오류 [code="+ajaxData.resultCode+"]", null);
			}
		}
	});
}
var codes= "";
var tempName = "";

function selectCode(code){
	$.ajax({
		url : "<c:url value="/service/index/selectCode"/>",
		async : false,
		dataType : "json",
		data : null, 
		method : "post",
		beforeSend : function(xhr, settings ){},
		error : function (xhr, status, error){},
		success : function (ajaxData) {
			if(ajaxData.resultCode == "Success"){
				console.log(ajaxData.codes);
				
				codes ="";
				codes=ajaxData.codes;
				
				var html="";
				for(var i = 0 ; i < codes.length ; i++){
					if(code == codes[i].codeId){
						html += '<option value="'+codes[i].codeId+'" selected>'+codes[i].name+'</option>';
						
					}else{
						html += '<option value="'+codes[i].codeId+'">'+codes[i].name+'</option>';
					}
					
				}
				
				$('#selectCode').html(html);
			    
				columnChart();
				
				
			}else{
				new bcs_messagebox().openError("종목코드", "종목코드 조회 오류 [code="+ajaxData.resultCode+"]", null);
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
				
				selectBestCode(year);
			    
			}else{
				new bcs_messagebox().openError("월별 등록 현황", "데이터 오류 발생 [code="+ajaxData.resultCode+"]", null);
			}
		}
	});
}

function drawChart() {


	$.ajax({
		url : "<c:url value="/service/index/drawChartData"/>",
		async : false,
		dataType : "json",
		data : null, 
		method : "post",
		beforeSend : function(xhr, settings ){},
		error : function (xhr, status, error){},
		success : function (ajaxData) {
			if(ajaxData.resultCode == "Success"){
				console.log(ajaxData.dashboardDatas);
				var dataValues = ajaxData.dashboardDatas; 
				var labelDatas = new Array();
				var datas = new Array();
				var colors = new Array();
				
				
				var randomColorGenerator = function () {
	                return '#' + (Math.random().toString(16) + '0000000').slice(2, 8);
	            };
	            
				for (var i = 0; i < dataValues.length; i++) {
					var temp = dataValues[i];

					labelDatas[i] = temp[0];
					datas[i] = temp[1];
					colors[i] = randomColorGenerator();
			    }
				
				
				
				var context = document.getElementById('piechart');
	            var myChart = new Chart(context, {
				    type: 'pie',
				    data: {
				      labels: labelDatas,
				      datasets: [{
				        label: "Population (millions)",
				        backgroundColor: colors,
				        data: datas
				      }]
				    },
				    options: {
				    	
				      title: {
				        display: false,
				        text: ''
				      }
				    }
				});
	            
	            
			    
			}else{
				new bcs_messagebox().openError("아카이브 등록 현황", "데이터 오류 발생 [code="+ajaxData.resultCode+"]", null);
			}
		}
	});
	
    
  }
  

  
  function columnChart(){


	var year = $('#selectYear').val();
	var code = $('#selectCode').val();
	
	for(var i =0 ; i < codes.length ; i++){
		
		if(code == codes[i].codeId){
			tempName = codes[i].name;
		}
	}
	
		
	$('#columnchart').remove(); // this is my <canvas> element
	$('#graph-container').append('<canvas id="columnchart"><canvas>');
	
	$.ajax({
		url : "<c:url value="/service/index/columnChartData"/>/"+code+"/"+year,
		async : false,
		dataType : "json",
		data : null, 
		method : "post",
		beforeSend : function(xhr, settings ){},
		error : function (xhr, status, error){},
		success : function (ajaxData) {
			if(ajaxData.resultCode == "Success"){
				
				console.log(ajaxData.dashboardDatas);
				
				var dashboardDatas = ajaxData.dashboardDatas; 
				var datas = new Array("0","0","0","0","0","0","0","0","0","0","0","0");
				
				var ySize = 1;
				
				
				for (var i = 0; i < dashboardDatas.length; i++) {
					var temp = dashboardDatas[i];
					
					var monthTemp = "";
					
					if(Number(temp[0]) < 10){
						
						monthTemp = Number(temp[0].replace("0","")) -1;
					}
					datas[monthTemp] = temp[1];
					
					if(Number(temp[1]) > 10){
						ySize = 10;
					}
					if(Number(temp[1]) > 100){
						ySize = 50;
					}
					
			    }
				
				
				
				
				
				var context = document.getElementById('columnchart').getContext('2d');
	            var myChart = new Chart(context, {
	                type: 'bar', // 차트의 형태
	                data: { // 차트에 들어갈 데이터
	                    labels: [
	                        //x 축
	                        '01','02','03','04','05','06','07','08','09','10','11','12'
	                    ],
	                    datasets: [
	                        { //데이터
	                            label: tempName, //차트 제목
	                            fill: false, // line 형태일 때, 선 안쪽을 채우는지 안채우는지
	                            data: datas,	                            
	                            borderWidth: 1 //경계선 굵기
	                        }
	                    ]
	                },
	                options: {
	                	
	                    scales: {
	                        yAxes: [
	                            {
	                                ticks: {
	                                	stepSize : ySize,
	                                    beginAtZero: true
	                                }
	                            }
	                        ]
	                    }
	                }
	            });
	            $('#columnchart').attr('style','margin-top: 60px; display: block; width: 935px; height: 467px;');

			}else{
				new bcs_messagebox().openError("종목별 영상 등록 현황", "데이터 오류 발생 [code="+ajaxData.resultCode+"]", null);
			}
		}
	});
	
  }
  
  function cameraInfo(value){
	  
	  location.href='<c:url value="/camera/monitor/'+value+'"/>';
	  
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
						<td id="01_area">0</td>
						<td id="02_area">0</td>
						<td id="03_area">0</td>
						<td id="04_area">0</td>
						<td id="05_area">0</td>
						<td id="06_area">0</td>
						<td id="07_area">0</td>
						<td id="08_area">0</td>
						<td id="09_area">0</td>
						<td id="10_area">0</td>
						<td id="11_area">0</td>
						<td id="12_area">0</td>
					</tr>
				</tbody>
				</table>
			</div>
			<div class="chartContainer">
				<div class="summary">
					<dl>
						<dt>로그인 사용자</dt>
						<dd id="loginCnt">0 명</dd>
					</dl>
					<dl>
						<dt>촬영 현황</dt>
						<dd>
							<dl>
								<dt style='cursor:pointer;' onClick="javascipt:cameraInfo('Recording');">촬영</dt>
								<dd style='cursor:pointer;' onClick="javascipt:cameraInfo('Recording');" id="recording">0 대</dd>
								<dt style='cursor:pointer;' onClick="javascipt:cameraInfo('Wait');">사용</dt>
								<dd style='cursor:pointer;' onClick="javascipt:cameraInfo('Wait');" id="wait">0 대</dd>
								<dt style='cursor:pointer;' onClick="javascipt:cameraInfo('DisCon');">미사용</dt>
								<dd style='cursor:pointer;' onClick="javascipt:cameraInfo('DisCon');" id="ready">0 대</dd>
							</dl>
						</dd>
					</dl>
				</div>
				<div class="chart01">
					<div class="title">
						<h4>종목별 영상 등록 현황</h4>
<!-- 						<span>UPLOAD / 조회</span> -->
						<select name="selectCode" id="selectCode" onChange="javascipt:columnChart();">
					
						</select>
					</div>
					<div class="" id="graph-container">
						<canvas id="columnchart" style="margin-top: 60px;"></canvas>
					</div>
				</div>
				
				<div class="chart02">
					<div class="title">
						<h4 id="diskInfoTitle">NAS 사용량( 0 TB / 0 TB ) 및 영상 저장 분포</h4>
						<progress id="progressVar" value="0" max="0" style="width:35%;margin-top: 15px;"></progress>
					</div>
					
					
					<div class="" id="">
						<canvas id="piechart" style="padding: 10px 35px 55px 35px;"></canvas>
					</div>
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