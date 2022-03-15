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
	
	jobList();
});


function jobList(){
	$.ajax({
		url : "<c:url value="/service/job/list"/>",
		async : false,
		dataType : "json",
		data : null, 
		method : "post",
		beforeSend : function(xhr, settings ){},
		error : function (xhr, status, error){},
		success : function (ajaxData) {
			if(ajaxData.resultCode == "Success"){
				
				console.log(ajaxData.tcJobs);
				
				$('#tableDatas').html("");
				
				var html ='';

				for(var i=0; i<ajaxData.tcJobs.length; i++ ){
					var temp =ajaxData.tcJobs[i];
					
					html +='<tr style="border-bottom: 1px solid #eaeaea;">';
					html +='<td>'+ajaxData.tcJobs[i].title+'</td>';
					html +='<td>'+ajaxData.tcJobs[i].registDate.substring(0,10)+'</td>';
					html +='<td>'+ajaxData.tcJobs[i].state+'</td>';
					html +='<td><div class="btnWrap" style="margin-left: 50%;"><a class="btn delete" href="javascript:onClick_tcRequest(\''+ajaxData.tcJobs[i].tcId+'\');">재요청</a> </div></td>';
					html +='</tr>';
				}

				$('#tableDatas').html(html);
				
			}else{
				new bcs_messagebox().openError("작업 리스트", "데이터 오류 발생 [code="+ajaxData.resultCode+"]", null);
			}
		}
	});
}


function onClick_tcRequest(tcId){
	alert(tcId);
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
	<jsp:param value="jobManage" name="subMenu"/>
</jsp:include>
<!-- //header -->
<!-- container -->


<div id="container" class="dashboard">
	
	<div class="titleWrap">
		<h2>작업관리</h2>		
			
	</div>
	<div id="contentsWrap">
		<div id="contents">
			<div class="tableContainer" style="height: 790px;overflow: auto;">
				
				<table>
					<caption></caption>
					<colgroup>
						<col width="40%" />
						<col width="20%" />
						<col width="20%" />
						<col width="20%" />
					</colgroup>
				
					<thead>
						<tr>
							
							<th>제목</th>
							<th>요청일</th>
							<th>상태</th>
							<th></th>
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