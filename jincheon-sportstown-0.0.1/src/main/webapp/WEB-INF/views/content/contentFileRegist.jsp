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
<script type="text/javascript" src="<c:url value="/bluecap/jwplayer-7.11.3/jwplayer.js"/>"></script>
<script>jwplayer.key="/cDB/4s47uzPnRb3q/gtzOze04a7CABARZuQWIwYvfQ=";</script>

<%-- <script type="text/javascript" src="<c:url value="/bluecap/jwplayer/jwplayer.js"/>"></script> --%>
<!-- <script>jwplayer.key="uL+sf8LV4JfO0X1+U8YPbC7PoiiNX730vh3pnQ==";</script> -->

<style type="text/css">
#lnbWrap select {
    border: solid 1px #dbdbdb;
    padding: 8px 10px;
    width: 100%
}
</style>

<script type="text/javascript">
$(document).ready(function(){
});
</script>


<script type="text/javascript">

function onClick_reload()
{
	location.reload();
}


function onClick_regist()
{
	
	$("#frmContent").ajaxSubmit({
		dataType : "json",
		beforeSubmit : function(arrData, jqForm, options){
			$("[data-regist-input]").attr("disabled","disabled");
		},
		uploadProgress : function(event, position, total, percentComplete){
			console.log("upload == ");
			console.log(event);
			console.log(position);
			console.log(total);
			console.log(percentComplete);
			
			$("[data-regist-fileProgress]").text("파일 업로드 " + percentComplete + "% ( " + position + " / " + total + " )" );
		},
		success : function(data, statusText, xhr, jqForm){
			console.log(data);
			
			if(data.resultCode == "Success"){
				var msgBox = new bcs_messagebox().open("파일등록", "파일 컨텐츠 등록 성공", false, { 
					"확인" : function(){
						location.reload();
					}
				});
			}
			else{
				new bcs_messagebox().openError("파일등록", "파일등록 오류 발생 [code="+data.resultCode+"]", null);
			}
			$("[data-regist-input]").removeAttr("disabled");
			$("[data-regist-fileProgress]").text("");
		},
		error: function(err){
			new bcs_messagebox().openError("파일등록", "파일등록 오류 발생 [AJAX Error]", null);
			
			$("[data-regist-input]").removeAttr("disabled");
			$("[data-regist-fileProgress]").text("");
		}
	})
	
}

</script>


<script type="text/javascript">

function set_content(data)
{
	$.each(data, function(index, value){
		
		var input = $("#frmContent").find("[data-ctrl-contentMeta="+index+"]");
		
		if(input.length > 0)
		{
			input.val(value);
			console.log(index +"="+ value);
		}
	});
}

</script>


<script type="text/javascript">

$(document).ready(function(){
	
	$("#frmContent").find("[data-ctrl-contentMeta=contentUserNames]").click(function(){
		
		$("#frmSelectedUsers").empty();
		$("#frmContent").find("[data-ctrl-contentMeta=contentUserId]").each(function(){
			$("#frmSelectedUsers").append($("<input type='hidden' name='selectedUserIds' />").val($(this).val()));
		});
		
		var sportsEentCode = $("#frmContent").find("[name=sportsEventCode]").val();
		$("#frmUserSearch").find("[name=sportsEventCode]").val(sportsEentCode);
		var param = $("#frmUserSearch").serialize();
		
		console.log(param);
		$("[data-ctrl-view=user_select]").empty();
		$("[data-ctrl-view=user_select]").jqUtils_bcs_loadHTML(
				"<c:url value="/user/select"/>?" + param,
				false, "get", null, null
			);
		
	});
});


function callback_selectedUsers(sender, users)
{
	console.log(users);
	
	$("#frmContent").find("[data-ctrl-contentMeta=contentUserId]").remove();
	var userNames = "";
	
	for(var i = 0; i < users.length; i++)
	{
		$("#frmContent").append(
			$("<input type='hidden' name='contentUsers["+i+"].userId' data-ctrl-contentMeta='contentUserId' />").val(users[i].userId)
		);
		
		userNames += users[i].userName + ",";
	}
	
	$("#frmContent").find("[data-ctrl-contentMeta=contentUserNames]").val(userNames);
	
	return true;
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
	<jsp:param value="regist" name="mainMenu"/>
	<jsp:param value="contentRegist" name="subMenu"/>
</jsp:include>
<!-- //header -->


<div title="파일등록" class="bcs_dialog_hide" data-ctrl-view="user_select" data-event-selected="callback_selectedUsers" data-param-selectedUserId="frmSelectedUsers">
</div>

<form id="frmUserSearch">
	<input type="hidden" name="sportsEventCode" value="${loginUser.sportsEventCode}"/>
</form>
<form id="frmSelectedUsers">
	<input type="hidden" 	name="selectedUserIds" value="" />
</form>

<!-- container -->
<div id="container">
	<div id="contentsWrap">

		<!-- lnbWrap -->
		<div id="lnbWrap" class="video">
				<div class="lnbWraph2">
					<h2>컨텐츠등록</h2>
				</div>
		</div>
		<!-- //lnbWrap -->

		<!-- contents -->
		<div id="contents" class="video">

			<!-- title -->
			<h3>영상녹화</h3>
			<!-- //title -->
			<div class="vodregistBox">
				<div class="vodregist ">
					<form id="frmContent" method="post" enctype="multipart/form-data" action="<c:url value="/service/content/registContentWithFile"/>">
						<input type="hidden" name="contentType" value="VIDEO" />
						<input type="hidden" name="instances[0].fileId" value="" data-ctrl-contentMeta="fileId" class="type_2">
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
									<td colspan="3"><input type="text" name="title" value="" title="제목" class="type_2" data-ctrl-contentMeta="title" data-regist-input></td>
								</tr>
								<tr>
									<th>파일</th>
									<td colspan="2">
										<input type="file" name="file" class="type_2" style="width:100%;" data-regist-input/>
									</td>
									<td data-regist-fileProgress>
										&nbsp;
									</td>
								</tr>
								<tr>
									<th>스포츠종목</th>
									<td>
										<c:choose>
											<c:when test="${loginUser.isAdmin == true or loaginUser.isDeveloper == true or loginUser.userType == 'Admin'}">
												<select class="td sel_type_2" name="sportsEventCode" title="스포츠종목" data-regist-input>
													<option value="">선택하세요</option>
													<c:forEach items="${sprotsEvents}" var="code">
														<c:choose>
															<c:when test="${loginUser.sportsEventCode == code.codeId}">
																<option value="${code.codeId}" selected>${code.name}</option>
															</c:when>
															<c:otherwise>
																<option value="${code.codeId}" >${code.name}</option>
															</c:otherwise>
														</c:choose>
													</c:forEach>
												</select>
											</c:when>
											<c:otherwise>
												<input type="text" name="sportsEventName" class="type_2" value="${loginUser.sportsEvent.name}" readonly data-regist-input/>
												<input type="hidden" name="sportsEventCode" value="${loginUser.sportsEventCode}"/>
											</c:otherwise>
										</c:choose>
									</td>
									<th>소유자</th>
									<td>
										<input type="text" name="contentUserNames" title="소유자" class="type_2" data-ctrl-contentMeta="contentUserNames" value="${loginUser.userName}" readonly data-regist-input/>
										<input type="hidden" name="contentUsers[0].userId" data-ctrl-contentmeta="contentUserId" value="${loginUser.userId}">
									</td>
								</tr>
								<tr>
									<th>녹화자</th>
									<td>
										<select class="td sel_type_2" name="recordUserId" title="녹화자" data-regist-input>
											<option value="">선택하세요</option>
											<c:forEach items="${users}" var="user">
												<c:choose>
													<c:when test="${loginUser.userId == user.userId}">
														<option value="${user.userId}" selected >${user.userName}</option>
													</c:when>
													<c:when test="${loginUser.isAdmin == true or loaginUser.isDeveloper == true or loginUser.userType == 'Admin'}">
														<option value="${user.userId}" >${user.userName}</option>
													</c:when>
													<c:when test="${loginUser.sportsEventCode == user.sportsEventCode}">
														<option value="${user.userId}" >${user.userName}</option>
													</c:when>
												</c:choose>
											</c:forEach>
										</select>
									</td>
									<th>녹화일자</th>
									<td>
										<input type="text" class="inputTxt date" style="height:35px; margin-right:5px; width:270px" name="recordDate"  value="<fmt:formatDate value="${currentDate}" pattern="yyyy-MM-dd"/>" data-regist-input/>
									</td>
								</tr>
								<tr>
									<th>설명</th>
									<td colspan="3">
										<textarea name="summary" title="설명" class="ta_type_1" rows="5" data-ctrl-contentMeta="summary" data-regist-input></textarea>
									</td>
								</tr>
							</tbody>
						</table>
					</form>
					
					<div class="btnbox alignR">
						<span class="btn_typeA t1"><a href="javascript:onClick_regist();">등록</a></span> 
						<span class="btn_typeA t2"><a href="javascript:onClick_reload();">새로하기</a></span>
					</div>					
				</div>
			</div>
		</div>
		<!-- //contents -->
	</div>
</div>
<!-- //container -->


<!-- footer -->
<jsp:include page="/include/footer" />
<!-- //footer -->
</div>

</body>
</html>
