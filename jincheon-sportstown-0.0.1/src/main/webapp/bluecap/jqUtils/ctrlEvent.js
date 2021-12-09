

function bcs_ctrl_event(sender){								// 사용될 이벤트가 속해있는 html 저장
	this.sender = sender; 
}

/**
 * 이벤트를 발생 시킨다
 * 
 * event : 이벤트 명
 * parameter : 전달할 데이터
 */
bcs_ctrl_event.prototype.send = function(event, parameter) {
	
	
	console.log("find event name = " + event + " => " + this.sender.is("["+event+"]"));
	console.log(this.sender);
	if(this.sender.is("["+event+"]") == true)
	{
		var callback = this.sender.attr(event);					// 저장된 html의 event속성의 값을 callback 변수에 저장
		
		if(callback != "")
		{
			var fn = window[callback];							// window[]로 전역에 저장되있는 변수나 함수, dom 검색. 여기선 함수 검색
			return fn(this.sender, parameter);					// 찾은 함수에 파라미터값 넣어서 리턴 
		}
	}
	return null;
}

