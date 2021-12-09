/**
 *  jquery ajax html loader
 *  url 	: page url
 *  aync 	:
 *  methos 	: get or post 
 *  params	: parameter
 *  fnCallback	: 
 *  	beforeSend : check before request
 *		error : request error handle function
 *		beforeLoad : before load page
 *		success : after laod page
 */
jQuery.fn.jqUtils_bcs_loadHTML = function(url, async, method, params, fnCallback){
	
	this.empty();
	var jqParent = this;
	
	
	var callback = $.extend(true, {
		beforeSend : function(xhr, settings ){},
		error : function (xhr, status, error){},
		beforeLoad : function(jqObject, ajaxData){ return true; },
		success : function (jqObject, ajaxData) {}
	}, fnCallback);
	
	
	$.ajax({
		url : url,
		async : async,
		datatype: "html",
		data : params,
		beforeSend : callback.beforeSend,
		error : function(xhr, status, error){
			callback.error(xhr, status, error);
		},
		success : function (ajaxData){
			if(callback.beforeLoad(jqParent, ajaxData) == true)
			{
				jqParent.removeClass("wait");
				jqParent.html(ajaxData);
				callback.success(jqParent, ajaxData);
			}else
			{
			
			}
		}
	});
}