<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>

<style type="text/css">
.depth2Wrap {display:none;}
</style>
<script type="text/javascript">


function onClick_pwdModify()
{
	window.open("<c:url value="/user/passwordModify"/>", "popup", "height=390,width=620,resizable=no,menubar=no,toolbar=no", true);
}



$(document).ready(function(){
	
	openPopup();
	
	
	
});


function openPopup(){



    var win = window.open('', 'win', 'width=1, height=1, scrollbars=yes, resizable=yes');
	
    if (win == null || typeof(win) == "undefined" || (win == null && win.outerWidth == 0) || (win != null && win.outerHeight == 0) || win.test == "undefined"){

		alert("팝업 차단 기능이 설정되어있습니다\n\n차단 기능을 해제(팝업허용) 한 후 다시 이용해 주십시오.\n\n만약 팝업 차단 기능을 해제하지 않으면\n시스템이 정상적으로 작동하지 않을수 있습니다.");

		if(win){
		
		    win.close();
		
		}

		return;

	}else if (win){

//     	if (win.innerWidth === 0){

//         	alert("2팝업 차단 기능이 설정되어있습니다\n\n차단 기능을 해제(팝업허용) 한 후 다시 이용해 주십시오.\n\n만약 팝업 차단 기능을 해제하지 않으면\n시스템이 정상적으로 작동하지 않을수 있습니다.");

// 		}

	}else{

    	return;

	}

	if(win){    // 팝업창이 떠있다면 close();
	
	    win.close();
	
	}
	
	changePasswordCheck();
	

}   

function changePasswordCheck(){
	var changeDate = "${loginUser.changeDate}";
	changeDate = changeDate.substring(0,10).replace(/-/gi, "");
	var today = new Date();
	
	var sixMonthAgo = new Date(today.setMonth(today.getMonth()-6));
	
	var year = sixMonthAgo.getFullYear();
	var month = ('0' + (sixMonthAgo.getMonth() + 1)).slice(-2);
	var day = ('0' + sixMonthAgo.getDate()).slice(-2);

	var dateString = year + '' + month  + '' + day;
	
	if(Number(changeDate)< Number(dateString)){
		onClick_pwdModify();
	}
}
</script>

<div title="사용자수정" class="bcs_dialog_hide" data-ctrl-view="userPassowrd_modify" data-event-modify="callback_pwdModify">
</div>
<div id="headerWrap">
	<div id="header">
		<h1><a href="<c:url value="javascript:logo_click();" />" class="img_logo"><img src="<c:url value="/resources/images/layout/logo.png"/>" alt="대한체육회" /></a></h1>
		<!-- gnb -->
		<div id="gnbWrap">
			<h2 class="blind">주메뉴</h2>
			<ul id="gnb">
				
				<li data-main-menu-id="main" style="display:none;"><span><a href="<c:url value="/hls/manage"/>">main</a></span>
				</li>
				
				<li data-main-menu-id="hls"><span><a href="<c:url value="/hls/manage"/>">영상녹화</a></span>
				</li>
	
				
				
				<li data-main-menu-id="mediaPlay"><span><a href="<c:url value="/mediaPlay"/>">녹화재생</a></span>
				</li>
				
				
				

				<li data-main-menu-id="regist"><span><a href="<c:url value="#"/>">영상등록</a></span>
					<!-- depth2 -->
					<div class="depth2Wrap sub02">
						<div class="menuWrap">
							<ul class="depth2">
								<li data-sub-menu-id="contentRegist" ><a href="<c:url value="/content/regist"/>">녹화등록</a></li>
								<li data-sub-menu-id="contentFileRegist" ><a href="<c:url value="/content/registFile"/>">파일등록</a></li>
							</ul>
						</div>
					</div>
					<!-- //depth2 -->
				</li>

				<li data-main-menu-id="search"><span><a href="<c:url value="#"/>">영상검색</a></span>

					<!-- depth2 -->
					<div class="depth2Wrap sub03">
						<div class="menuWrap">
							<ul class="depth2">
								<li data-sub-menu-id="contentManage"><a href="<c:url value="/content/manage"/>">영상검색</a></li>
								<li data-sub-menu-id="contentAuth"><a href="<c:url value="/contentAuth/manage"/>">영상승인</a></li>
							</ul>
						</div>
					</div>
					<!-- //depth2 -->
				</li>

				<c:if test="${loginUser.isAdmin == true or loginUser.isDeveloper == true or loginUser.userType == 'Admin'}">
				<li data-main-menu-id="manage"><span><a href="#">관리자기능</a></span>

					<!-- depth2 -->
					<div class="depth2Wrap sub04">
						<div class="menuWrap">
						<ul class="depth2">
							<li data-sub-menu-id="userManage" ><a href="<c:url value="/user/manage"/>">사용자</a></li>
							<li data-sub-menu-id="cameraManage" ><a href="<c:url value="/camera/manage"/>">카메라 등록</a></li>
							<li data-sub-menu-id="cameraMonitor" ><a href="<c:url value="/camera/monitor"/>">녹화상태</a></li>
							<li data-sub-menu-id="codeManage" ><a href="<c:url value="/code/manage"/>">코드관리</a></li>
<!-- 							<li><a href="../04/05.html">공지사항</a></li> -->
						</ul>
						</div>
					</div>
					<!-- //depth2 -->
				</li>
				</c:if>
				
				
				
<!-- 				<li data-ctrl-button="btn_logout"><span><a href="javascript:onLogout();">로그아웃</a></span> </li> -->
			</ul>
		</div>
		<!-- //gnb -->
		<div class="welcome">
			<p> <span>${loginUser.userName}</span>님 안녕하세요</p>		
			<div>
<!-- 				<p>사용자를 위한 기능을 제공합니다. </p> -->
				<ul>
					<li><a href="javascript:onLogout();">로그아웃</a></li>
<!-- 					<li><a href="#">사용자 정보 변경</a></li> -->
<%-- 					<li><a href="<c:url value="javascript:onClick_pwdModify();" />">비밀 번호 갱신</a></li> --%>
<%-- 					<li><a href="<c:url value="/admin/adminGraph1" />">임시로 만든 대쉬보드 페이지1</a></li> --%>
<%-- 					<li><a href="<c:url value="/admin/adminGraph2" />">임시로 만든 통계 페이지2</a></li> --%>
				</ul>
			</div>
		</div>		
	</div>
</div>


<script type="text/javascript">

function logo_click(){
	
	if('${loginUser.userType}' == "Admin"){
		$(location).attr("href", "<c:url value="/index"/>");
	}else{
		$(location).attr("href", "<c:url value="/hls/manage"/>");
	}	
}



// function onClick_pwdModify()
// {
	
// 	console.log(userId);
	
// 	if(userId == "")
// 	{
// 		new bcs_messagebox().open("사용자 수정", "사용자를 선택해 주세요", null);
// 		return;
// 	}
// 	$("[data-ctrl-view=userPassowrd_modify]").empty();
// 	$("[data-ctrl-view=userPassowrd_modify]").jqUtils_bcs_loadHTML(
// 			"<c:url value="/user/passwordModify"/>" ,
// 			false, "get", null, null
// 		);	
// }

$(document).ready(function(){
	
	console.log("========");
	console.log("${loginUser}");
	$(".welcome > p").click(function(){
		$(this).next().toggle();
	});	
	
	$("#gnb > li > span > a").click(function(){
		$(this).parents("li").addClass("on").siblings().removeClass("on");
		//depth2Wrap.hide();
		
		console.log($(this).parents("li").find(".depth2Wrap").css("display"));
		if($(this).parents("li").find("li").length == 1)
			return;
		
		if($(this).parents("li").find(".depth2Wrap").css("display") == "none")
			$(this).parents("li").find(".depth2Wrap").show();
		else
			$(this).parents("li").find(".depth2Wrap").hide();
	});
	
	
	// return;
	
	var mainMenu = $("[data-main-menu-id=${mainMenu}]");
	console.log('loginuser : ')
	console.log('${loginUser}');
	console.log('${loginUser.loginId}')
	if(mainMenu.length == 1)
	{
		mainMenu.addClass("on");
		$("#gnb > li > .depth2Wrap").hide();
		
		return null;
		var subMenu = $("[data-sub-menu-id=${subMenu}]");;
		if(subMenu.length == 1)
			mainMenu.addClass("on");
	}
	
	
	var changeDate = "${loginUser.changeDate}";
	changeDate = changeDate.substring(0,10).replace(/-/gi, "");
	var today = new Date();
	
	var sixMonthAgo = new Date(today.setMonth(today.getMonth()-6));
	console.log(">>>>>>>>>"+sixMonthAgo);
	var year = sixMonthAgo.getFullYear();
	var month = ('0' + (sixMonthAgo.getMonth() + 1)).slice(-2);
	var day = ('0' + sixMonthAgo.getDate()).slice(-2);

	var dateString = year + '' + month  + '' + day;
	
	if(Number(changeDate)< Number(dateString)){
		onClick_pwdModify();
	}
	
});

function on_mainMenu(mainMenuId)
{
	var mainMenu = $("[data-main-menu-id="+mainMenuId+"]");
	
	if(mainMenu.length==1)
	{
		if(mainMenu.hasClass("on"))
		{
			mainMenu.addClass("on");
		}else
		{
			mainMenu.removeClass("on");
		}
	}
	
}


function onLogout()
{
	$.ajax({
		url : "<c:url value="/service/user/logout"/>",
		async : false,
		dataType : "json",
		data : null,
		method : "post",
		beforeSend : function(xhr, settings ){},
		error : function (xhr, status, error){},
		success : function (ajaxData) {
			if(ajaxData.resultCode == "Success"){
				// TODO : main 화면으로 redirect
				$(location).attr("href", "<c:url value="/login"/>");
			}
			else{
				new bcs_messagebox().openError("로그아웃", "로그아웃에 실패 했습니다 [errorCode="+ajaxData.resultCode+"]", null);
			}
		}
		
	});
}
</script>