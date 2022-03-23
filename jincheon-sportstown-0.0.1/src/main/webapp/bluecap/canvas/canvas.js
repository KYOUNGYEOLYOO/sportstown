/**
 * 
 */


/*<%--20211215 html에서 버튼 역할 하는 부분 --%>
<div id="styleBtn" style="display: flex; position:absolute; 
	top: 60px; right: 0px; z-index: 4;">
	<button style="margin-right:10px" id="drawing"
	onclick="drawCanvas()">draw</button>
	<button style="margin-right:10px" id="shape"
	onclick="drawShape()">shape</button>
	<button style="margin-right:10px" id="eraser"
	onclick="eraseCanvas()">eraser</button>
	<button style="margin-right:10px" id="clear"
	onclick="clrCanvas(document.getElementById('canvas'))">clear</button>
	<button style="margin-right:10px" id="color"
	onclick="changeColor('blue')">color</button>
	<button style="margin-right:10px" id="thickness"
	onclick="changeWidth(10)">thickness</button>
	<button style="margin-right:10px" id="close"
	onclick="delCanvas($('#canvas'))">close</button>
</div>*/

/*const canvas = document.getElementById("canvas");*/
let painting = false;
let ctx;
let ctxChange;
var color;
let x, y;
let data, reloadData;
let shape = "네모";
var myObj = window.myObj || {}
myObj = {
	history: []
};


function startPainting(event){
	x = event.offsetX;
	y = event.offsetY;
	
	ctx.beginPath();
	ctx.moveTo(x,y);
	painting = true;
}

function stopPainting(){
	ctx.closePath(); // 하위 경로의 시작점과 연결
	painting = false;
}

function onMouseMove(event){
	x = event.offsetX;
	y = event.offsetY;
	
	if(!painting){
		return;
	}
	ctx.lineTo(x,y); // 도착점을 좌표로 옮김
	ctx.stroke(); // 그림
}

function resizeCanvas(canvas){
	//canvas.width = window.innerWidth;
	//canvas.height = window.innerHeight;
	canvas.width = window.document.getElementById("container").clientWidth;
	canvas.height = window.document.getElementById("container").clientHeight;
	ctx.strokeStyle = "#ff0000";
	ctxChange.strokeStyle = "#ff0000";
}

function resizeCanvas2(canvas){
	//canvas.width = window.innerWidth;
	console.log("111뭐야 도대체 : ", window.document.getElementById("wrappers").clientWidth);
	console.log("222뭐야 도대체 : ", window.document.getElementById("wrappers").clientHeight);
	//canvas.height = window.innerHeight;
	canvas.width = window.document.getElementById("wrappers").clientWidth;
	canvas.height = window.document.getElementById("wrappers").clientHeight;
	ctx.strokeStyle = "#ff0000";
	ctxChange.strokeStyle = "#ff0000";
	console.log("ctx.strokeStyle112 : ",ctx.strokeStyle);
}

function resizeCanvas3(canvas){
	canvas.width = 1920;
	canvas.height = 1080;
	ctx.strokeStyle = "#ff0000";
	ctxChange.strokeStyle = "#ff0000";
	console.log("ctx.strokeStyle112 : ",ctx.strokeStyle);
}

function addCanvas2(){
	console.log("시작이 된거 같은데??1111111111");
	
	const canvasChange = document.createElement("canvas");
	canvasChange.setAttribute("id","canvasChange");
//	document.body.appendChild(canvasChange);
	document.getElementById("canvasWrap").appendChild(canvasChange);
	canvasChange.setAttribute("style","position: absolute; top: 0px; left: 0px;"+
	"z-index: 1; background-color: transparent;");
	
	
	const canvas = document.createElement("canvas");
	canvas.setAttribute("id", "canvas");
//	document.body.appendChild(canvas);
	document.getElementById("canvasWrap").appendChild(canvas);
	canvas.setAttribute("background-color","transparent");
	canvas.setAttribute("style","position: absolute; top: 0px; left: 0px;"+
	"z-index: 2; ");
	
		
	ctx = canvas.getContext("2d");
	ctxChange = canvasChange.getContext("2d");
	
	// default 색상 빨강으로 설정
	ctx.strokeStyle = "#ff0000";
	ctxChange.strokeStyle = "#ff0000";
	console.log("ctx.strokeStyle111 : ",ctx.strokeStyle);
	
	resizeCanvas2(canvas); 
	// 이유는 모르겠는데 실행 안됨 이 내용들을 setAttribute에서 바로 실행
	// 사실 실행되는거였는데 이거 코드 자체가 수정이 적용이 안되고 있었음.. >> 인터넷 캐시 삭제후 적용..  
	window.addEventListener("resize",resizeCanvas2(canvas), false);	
	canvas.addEventListener("mousemove",onMouseMove);
	canvas.addEventListener("mousedown",startPainting);
	canvas.addEventListener("mouseup",stopPainting);
	canvas.addEventListener("mouseleave",stopPainting);
	
}

function addCanvas3(){
	
	const canvasChange = document.createElement("canvas");
	canvasChange.setAttribute("id","canvasChange");
	document.getElementById("canvasWrap").appendChild(canvasChange);
	canvasChange.setAttribute("style","position: absolute; top: 0px; left: 0px;"+
	"z-index: 1; background-color: transparent;");
	
	
	const canvas = document.createElement("canvas");
	canvas.setAttribute("id", "canvas");
	document.getElementById("canvasWrap").appendChild(canvas);
	canvas.setAttribute("background-color","transparent");
	canvas.setAttribute("style","position: absolute; top: 0px; left: 0px;"+
	"z-index: 2; ");
	
		
	ctx = canvas.getContext("2d");
	ctxChange = canvasChange.getContext("2d");
	
	ctx.strokeStyle = "#ff0000";
	ctxChange.strokeStyle = "#ff0000";
	console.log("ctx.strokeStyle111 : ",ctx.strokeStyle);
	
	resizeCanvas3(canvas); 
	window.addEventListener("resize",resizeCanvas3(canvas), false);	
	canvas.addEventListener("mousemove",onMouseMove);
	canvas.addEventListener("mousedown",startPainting);
	canvas.addEventListener("mouseup",stopPainting);
	canvas.addEventListener("mouseleave",stopPainting);
	
}

function addCanvas(){
	
	const canvasChange = document.createElement("canvas");
	canvasChange.setAttribute("id","canvasChange");
//	document.body.appendChild(canvasChange);
	document.getElementById("canvasWrap").appendChild(canvasChange);
	canvasChange.setAttribute("style","position: absolute; top: 0px; left: 0px;"+
	"z-index: 1; background-color: transparent;");
	
	
	const canvas = document.createElement("canvas");
	canvas.setAttribute("id", "canvas");
//	document.body.appendChild(canvas);
	document.getElementById("canvasWrap").appendChild(canvas);
	canvas.setAttribute("background-color","transparent");
	canvas.setAttribute("style","position: absolute; top: 0px; left: 0px;"+
	"z-index: 2; ");
	
		
	ctx = canvas.getContext("2d");
	ctxChange = canvasChange.getContext("2d");
	
	// default 색상 빨강으로 설정
	ctx.strokeStyle = "#ff0000";
	ctxChange.strokeStyle = "#ff0000";
	console.log("ctx.strokeStyle111 : ",ctx.strokeStyle);
	
	resizeCanvas(canvas); 
	// 이유는 모르겠는데 실행 안됨 이 내용들을 setAttribute에서 바로 실행
	// 사실 실행되는거였는데 이거 코드 자체가 수정이 적용이 안되고 있었음.. >> 인터넷 캐시 삭제후 적용..  
	window.addEventListener("resize",resizeCanvas(canvas), false);	
	canvas.addEventListener("mousemove",onMouseMove);
	canvas.addEventListener("mousedown",startPainting);
	canvas.addEventListener("mouseup",stopPainting);
	canvas.addEventListener("mouseleave",stopPainting);
	
}

function delCanvas(canvas, canvasChange){
	console.log("delCanvas");
	console.log("canvasChange1111");
	canvasChange.remove();
	canvas.remove();
	myObj.history = [];
}

function clrCanvas(canvas){
	console.log("clrCanvas");
//	ctx.clearRect(0,0,canvas.width,canvas.height);
// jquery로 넣어주려다 보니 width를 함수로 써야되는듯??
	ctx.clearRect(0,0,canvas.width(),canvas.height());
}

function eraseCanvas(){
	console.log("eraseCanvas");
	/*try{
		document.getElementById("canvasChange").remove();
		
	}catch (err){
		console.error(err);
	}*/
	/*const canvas = document.getElementById("canvas")
	canvas.setAttribute("style","z-index:1");*/
	canvasChange.style.zIndex = "1";
	ctx.globalCompositeOperation = "destination-out";
}

function reloadCanvas(){
	console.log("reloadCanvas");
	
	reloadData = JSON.parse(myObj.istory[someIndex]);
	ctx.clearRect(0,0,canvas.width,canvas.height);
	ctx.putImageData(reloadData,0,0);
}

function drawCanvas(){
	console.log("drawCanvas");
	/*try{
		document.getElementById("canvasChange").remove();
		
	}catch (err){
		console.error(err);
	}*/
	/*const canvas = document.getElementById("canvas")
	canvas.setAttribute("style","z-index:1");*/
	canvasChange.style.zIndex = "1";
	ctx.globalCompositeOperation = "source-over";
}

function changeWidth(value){
	console.log("changeWidth");
	console.log(value);
	switch (value){
		case "얇은 거":
			value = "3";
			break;
		case "보통":
			value = "5";
			break;
		case "두꺼운 거":
			value = "7";
			break;
	}
	console.log(value);
	ctx.lineWidth = value;
	ctxChange.lineWidth = value;
}

function changeColor(value){
	console.log("changeColor");
	switch (value){
		case "파랑":
			value = "#0000ff";
			break;
		case "빨강":
			value = "#ff0000";
			break;
		case "초록":
			value = "#00ff00";
			break;
		case "검정":
			value = "#000";
			break;
		case "하늘색":
			value = "#00ccff";
			break;
	}
	color = value;
	ctx.strokeStyle = value;
	ctxChange.strokeStyle = value;
}

function drawShape(cShape){
	console.log("drawShape");
	shape = cShape;
	console.log("111shape:",shape);
	ctx.globalCompositeOperation = "source-over";
	let canvasShape = document.getElementById("canvasChange");
	
	//ctxChange.lineWidth = ctx.lineWidth;
	//ctxChange.strokeStyle = ctx.strokeStyle;
	canvasShape.style.zIndex = "3";
	
	resizeCanvas(canvasShape);
	window.addEventListener("resize",resizeCanvas(canvasShape),false);
	
	ctx.strokeStyle = color;
	ctxChange.strokeStyle = color;
	console.log("ctx.strokeStyle113 : ",ctx.strokeStyle);
	
	var sX,sY,cX,cY;
	var canvasX = $(canvas).offset().left;
  	var canvasY = $(canvas).offset().top;
	var draw = false;
	
	canvasShape.addEventListener("mousedown",function(e){
		sX= parseInt(e.clientX-canvasX);
		sY= parseInt(e.clientY-canvasY);
		
		draw = true;
		
		cX = sX;
		cY = sY;
		console.log("mousedown", sX, sY);
		
		
		ctxChange.lineWidth = ctx.lineWidth;
		ctxChange.strokeStyle = ctx.strokeStyle;
	})
	canvasShape.addEventListener("mousemove",function(e){
		if(draw){
			cX= parseInt(e.clientX - canvasX);
			cY= parseInt(e.clientY - canvasY);
			
			
			switch(shape){
				case "격자":
					ctxChange.clearRect(0,0,canvasShape.width,canvasShape.height);

					for (var i = 0; i < 6; i++){
					    for (var j = 0; j < 6; j++){

					      ctxChange.strokeRect(sX+j*(cX-sX+25),sY+i*(cY-sY+25),cX-sX+25,cY-sY+25);
					    }
					  }
					break;
				case "네모":
					ctxChange.clearRect(0,0,canvasShape.width,canvasShape.height);
<<<<<<< HEAD
//					ctxChange.strokeRect(sX,sY,cX-sX,cY-sY);
					for (var i = 0; i < 6; i++){
                   		for (var j = 0; j < 6; j++){
							ctxChange.strokeRect(j*(cX-sX+25),i*(cY-sY+25),cX-sX+25,cY-sY+25);
//                     		ctxChange.strokeRect(j*(cX-sX),i*(cY-sY),(cX+sX),(cY+sY));
                   }
                 }
=======
					ctxChange.strokeRect(sX,sY,cX-sX,cY-sY);
					
					
					
					
					
>>>>>>> branch 'master' of https://github.com/KYOUNGYEOLYOO/sportstown.git
					break;
				case "원":
					ctxChange.clearRect(0,0,canvasShape.width,canvasShape.height);
					ctxChange.beginPath();
					ctxChange.ellipse(parseInt((cX+sX)/2),parseInt((cY+sY)/2),
					parseInt(Math.abs(cX-sX)),parseInt(Math.abs(cY-sY)), 0,
					0, 2 * Math.PI,);
					ctxChange.closePath();
					ctxChange.stroke();
					break;
				case "자유선":
					drawCanvas();
//					ctxChange.clearRect(0,0,canvasShape.width,canvasShape.height);
//					ctxChange.beginPath();
//					ctxChange.moveTo(sX,sY);
//					ctxChange.lineTo(cX,cY);
//					ctxChange.closePath();
//					ctxChange.stroke();
					break;
			}
			/*// rectangle
			ctxChange.clearRect(0,0,canvasShape.width,canvasShape.height);
			ctxChange.strokeRect(sX,sY,cX-sX,cY-sY);*/
			
			// ellipse (타원)
//			ctxChange.clearRect(0,0,canvasShape.width,canvasShape.height);
//			ctxChange.beginPath();
//			ctxChange.ellipse(parseInt((cX+sX)/2),parseInt((cY+sY)/2),
//			parseInt(Math.abs(cX-sX)),parseInt(Math.abs(cY-sY)), 0,
//			0, 2 * Math.PI,);
//			ctxChange.closePath();
//			ctxChange.stroke();
			
			/*// line (일직선)
			ctxChange.clearRect(0,0,canvasShape.width,canvasShape.height);
			ctxChange.beginPath();
			ctxChange.moveTo(sX,sY);
			ctxChange.lineTo(cX,cY);
			ctxChange.closePath();
			ctxChange.stroke();*/
			
			 
		}
	})
	
	canvasShape.addEventListener("mouseup",function(e){
		draw = false;
		//rect
//		ctx.strokeRect(sX,sY,cX-sX,cY-sY);

		console.log("aaaa:",shape);
		switch(shape){
			case "격자":

				for (var i = 0; i < 6; i++){
				    for (var j = 0; j < 6; j++){

				      ctx.strokeRect(sX+j*(cX-sX+25),sY+i*(cY-sY+25),cX-sX+25,cY-sY+25);
				    }
				  }
				
				shape ="격자";
				break;
			case "네모":
				ctx.strokeRect(sX,sY,cX-sX,cY-sY);
				
				
				shape ="네모";
				break;
			case "원":
				ctx.beginPath();
				ctx.ellipse(parseInt((cX+sX)/2),parseInt((cY+sY)/2),
				parseInt(Math.abs(cX-sX)),parseInt(Math.abs(cY-sY)), 0,
				0, 2 * Math.PI,);
				ctx.closePath();
				ctx.stroke();
				shape ="원";
				break;
				}
//			case "자유선":
//				drawCanvas();
		//ellipse
//		ctx.beginPath();
//		ctx.ellipse(parseInt((cX+sX)/2),parseInt((cY+sY)/2),
//		parseInt(Math.abs(cX-sX)),parseInt(Math.abs(cY-sY)), 0,
//		0, 2 * Math.PI,);
//		ctx.closePath();
//		ctx.stroke();

		
		/*//line
		console.log("mouseup",sX,sY,cX,cY);
		ctx.beginPath();
		ctx.moveTo(sX,sY);
		ctx.lineTo(cX,cY);
		ctx.stroke();
		ctx.closePath();*/
		
		// 점선
		/*
		ctx.setLineDash[선 길이, 여백 길이 , 다음 선 길이, 여백 길이]
		같으면 한번만 적어줘도 됨... 저기에 적어주는 만큼이 하나의 루틴인듯
		*/

		// 뒤로가기 데이터 저장(히스토리)
		/*data = JSON.stringify(ctx.getImageData(0,0,canvasShape.width,canvasShape.height));		
		myObj.history.push(data);*/
		
		
		
		ctxChange.clearRect(0,0,canvasShape.width,canvasShape.height);
	})
}



