$(function($){
	//gnb/
	var depth2Wrap = $(".depth2Wrap");
	$("#gnb > li > span > a").on("mouseenter focusin", function(){
		$(this).parents("li").addClass("on").siblings().removeClass("on");
		//depth2Wrap.hide();
		$(this).parent().next(".depth2Wrap").show();
	});

	$("#gnbWrap").on("mouseleave", function(){
		$("#gnb > li").removeClass("on");
		//depth2Wrap.hide();
		$("#gnb > li.active").addClass("on");
	});
	
	$("#header .search .inputTxt").focus(function(){
		$("#gnb > li").removeClass("on");
		depth2Wrap.hide();
	});

	//top 버튼
	$("#footer .btnTop").on("click", function(){
		$('html, body').animate({scrollTop:0}, 300);
		return false;
	});

});