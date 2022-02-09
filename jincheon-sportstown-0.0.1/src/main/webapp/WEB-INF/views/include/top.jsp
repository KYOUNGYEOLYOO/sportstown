<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>

<style type="text/css">
.depth2Wrap {display:none;}
</style>

<div id="headerWrap">
	<div id="header">
		<h1><a href="#" class="img_logo"><img src="<c:url value="/resources/images/layout/logo.png"/>" alt="대한체육회" /></a></h1>
		<!-- gnb -->
		<div id="gnbWrap">
			<h2 class="blind">주메뉴</h2>
			<ul id="gnb">				
				<li data-main-menu-id="hls"><span><a href="<c:url value="/hls/manage"/>">영상녹화</a></span></li>
				<li data-main-menu-id="mediaPlay"><span><a href="<c:url value="/mediaPlay"/>">녹화재생</a></span></li>
				<li data-main-menu-id="regist">
					<span><a href="<c:url value="#"/>">영상등록</a></span>
					<!-- depth2 -->
					<div class="depth2Wrap sub02">
						<div class="menuWrap">
							<ul class="depth2">
								<li data-sub-menu-id="contentRegist" ><a href="<c:url value="/content/regist"/>">녹화등록</a></li>
								<li data-sub-menu-id="contentRegist" ><a href="<c:url value="/content/registFile"/>">파일등록</a></li>
							</ul>
						</div>
					</div>
					<!-- //depth2 -->
				</li>
				<li data-main-menu-id="search">
					<span><a href="<c:url value="/content/manage"/>">영상검색</a></span>
					<!-- depth2 -->
					<div class="depth2Wrap sub03">
						<div class="menuWrap">
						<ul class="depth2">
							<li data-sub-menu-id="contentManage"><a href="<c:url value="/content/manage"/>">영상검색</a></li>
						</ul>
						</div>
					</div>
					<!-- //depth2 -->
				</li>
				<c:if test="${loginUser.isAdmin == true or loginUser.isDeveloper == true or loginUser.userType == 'Admin'}">
				<li data-main-menu-id="manage">
					<span><a href="#">관리자기능</a></span>
					<!-- depth2 -->
					<div class="depth2Wrap sub04">
						<div class="menuWrap">
							<ul class="depth2">
								<li data-sub-menu-id="userManage" ><a href="<c:url value="/user/manage"/>">사용자</a></li>
								<li data-sub-menu-id="cameraManage" ><a href="<c:url value="/camera/manage"/>">카메라 등록</a></li>
								<li data-sub-menu-id="cameraMonitor" ><a href="<c:url value="/camera/monitor"/>">녹화상태</a></li>
								<li data-sub-menu-id="codeManage" ><a href="<c:url value="/code/manage"/>">코드관리</a></li>
								<!--	<li><a href="../04/05.html">공지사항</a></li> -->
							</ul>
						</div>
					</div>
					<!-- //depth2 -->
				</li>
				</c:if>		
				<!-- <li data-ctrl-button="btn_logout"><span><a href="javascript:onLogout();">로그아웃</a></span> </li>	 -->
			</ul>
		</div>
		<!-- //gnb -->
		<div class="welcome">
			<p>양궁 <span>홍길동</span>님 안녕하세요</p>		
			<div>
				<p>사용자를 위한 기능을 제공합니다. </p>
				<ul>
					<li><a href="javascript:onLogout();">로그아웃</a></li>
					<li><a href="#">사용자 정보 변경</a></li>
					<li><a href="#">비밀 번호 갱신</a></li>
				</ul>
			</div>
		</div>		
	</div>
</div>


<script type="text/javascript">
$(document).ready(function(){

	/*	new	*/
	
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