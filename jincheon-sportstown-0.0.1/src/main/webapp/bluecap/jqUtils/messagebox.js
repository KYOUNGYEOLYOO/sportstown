

function bcs_messagebox(){
	this.messagebox = null; 
}


/**
 * 메시지 박스를 보여 준다
 * 
 * title : 메시지 박스 제목
 * message : 메시지 내용
 * btnMap : 버튼
 * isError : 오류 메시지 여부
 */
bcs_messagebox.prototype.open = function(title, message, isError, btnMap) {
	
	if(btnMap == null)
	{
		btnMap = { "확인" : function(){ $(this).dialog("close"); }}
	}
	
	this.messagebox = $("<div/>").attr("title", title).text(message).dialog({
		modal : true,
		autoOpen : true,
		draggable : false,
		close : function(event, ui) {
			if (isError == true) {
				$(this).parent().find(".ui-dialog-titlebar")
						.removeClass("ui-state-error");
				$(this).dialog("destroy");
			}
		},
		open : function(event, ui) {

			if (isError == true) {
				$(this).parent().find(".ui-dialog-titlebar").addClass(
						"ui-state-error");
			}
		},
		buttons : btnMap
	});
	return this;
}


bcs_messagebox.prototype.openError = function(title, message, btnMap)
{
	this.open(title, message, true, btnMap);
}

/**
 * 메시지 박스를 닫는다
 */
bcs_messagebox.prototype.close = function() {
	this.messagebox.dialog("close");
	return this;
}


