<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title> Team1 - 매장관리 </title>
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js">
</script>
<script src="js/main.js" type="text/javascript"></script>
<script>
function regEmp(level) {
	const modal = document.querySelector(".modalBox");
	let modalTitle = document.querySelector(".modalTitle");
	let modalCommand = document.querySelector(".modalCommand");

	modalCommand.innerHTML = "";
	
	if (level == "아르바이트") {
		modalTitle.innerHTML = "<br>시스템 메시지";
		modalCommand.innerHTML = "<br><br>권한이 부족합니다"
		+ "<div class=\'closeBox\' onMouseOver=\"changeBtnCss(this, 'closeBox-over')\" onMouseOut=\"changeBtnCss(this, 'closeBox')\" onClick=\'modalClose()\'> Close </div>";
		
		modal.style.display = "block";
		
		window.onclick = function(event) {
			  if (event.target == modal) {
			    modal.style.display = "none";
			  }
			}
		return;		
	}		
	
	modalTitle.innerHTML = "직원등록" + "<div class=\'closeBox\' onClick=\'modalClose()\'> X </div>";
	
	/*Table 추가*/
	let table = "<table><tr><th>직원이름</th><th>                 직원등급</th></tr>";
	//table += "<tr><td>" + "흠 "+ "</td><td>" + "망" + "</td><td>" + "히" + "</td></tr>";
	modalCommand.innerHTML = table;
	//innerHTML은 덮어쓴다
	/* HTML Object 추가 : js 영역에 개체 생성 후 --> */
	/* HTML Object 추가 :  JS영역에 개체 생성 후 --> HTML 영역에 추가 : appendChild() */
	let obj = [];
	
	obj[0] = createInput("text", "employeeName", "box", null, "이름", null);
	modalCommand.appendChild(obj[0]);
	obj[1]= createInput("text", "employeeLevel", "box", null, "등급", null);
	modalCommand.appendChild(obj[1]);
	
	/* 줄바꿈 */
	modalCommand.appendChild(document.createElement("br"));
	
	/* HTML Object 추가 :  JS영역에 개체 생성 후 --> HTML 영역에 추가 : appendChild() */
	let objBtn = createInput("button", null, "small-btn", "직원등록", null, null);
	// 동적으로 생성된 개체에 이벤트 추가  addEventListener(event, function)
	objBtn.addEventListener("click", function(){
		serverTransfer2(obj, "EmpReg");
	});
	modalCommand.appendChild(objBtn);
	
	
	modal.style.display = "block";
	
	window.onclick = function(event) {
		  if (event.target == modal) {
		    modal.style.display = "none";
		  }
		}	
}

function changePassword(title, data){
		const modal = document.querySelector(".modalBox");
		let modalTitle = document.querySelector(".modalTitle");
		let modalCommand = document.querySelector(".modalCommand");
		
		modalCommand.innerHTML = "";
		
		modalTitle.innerHTML = title + "<div class=\'closeBox\' onClick=\'modalClose()\'> X </div>";
		
		let info = data.split(":");	
		
	//Table 추가
	let table = "<table><tr><th>직원코드</th><th>직원이름</th><th>직원등급</th></tr>";
	table += "<tr><td>"+info[0]+"</td><td>"+info[1]+"</td><td>"+info[2]+"</td></tr></table>";
	modalCommand.innerHTML = table;
	
	//HTML 오브젝트 추가 : js영역에 개체 생성 후 ->HTML 영역에 추가 : appendChild()	
	let obj = []; 
	obj[0]= createInput("password" , "employeePassword" , "box" , null,"변경 할 패스워드",null );
	modalCommand.appendChild(obj[0]);
	
	obj[1] = createInput("hidden" , "employeeCode" , "box" , info[0], null, null );
	modalCommand.appendChild(obj[1]);
	
	//HTML 오브젝트 추가 : js영역에 개체 생성 후 ->HTML 영역에 추가 : appendChild()	
	let objBtn = createInput("button", null , "small-btn", "업데이트", null, null);
		
	//동적으로 생성된 개체에 이벤트 추가 addEventListener(event,funtion)
	objBtn.addEventListener("click", function(){
		serverTransfer(obj,"UpdPassword");
	});
	
	modalCommand.appendChild(objBtn);
	
	objBtn = createInput("button", null , "small-btn", "초기화", null, null);
	modalCommand.appendChild(objBtn);
		
	modal.style.display = "block";	
	
	window.onclick = function(event) {
		  if (event.target == modal) {
		    modal.style.display = "none";
		  }
		}	
	}
	
function changeLevel(title, data){
	const modal = document.querySelector(".modalBox");
	let modalTitle = document.querySelector(".modalTitle");
	let modalCommand = document.querySelector(".modalCommand");
	
	modalCommand.innerHTML = "";
	
	modalTitle.innerHTML = title + "<div class=\'closeBox\' onClick=\'modalClose()\'> X </div>";
	
	let info = data.split(":");	
	

//Table 추가
let table = "<table><tr><th>직원코드</th><th>직원이름</th><th>직원등급</th></tr>";
table += "<tr><td>"+info[0]+"</td><td>"+info[1]+"</td><td>"+info[2]+"</td></tr></table>";
modalCommand.innerHTML = table;

//HTML 오브젝트 추가 : js영역에 개체 생성 후 ->HTML 영역에 추가 : appendChild()	
let obj = []; 
obj[0]= createInput("text" , "employeeLevel" , "box" , null,"변경 할 등급",null );
modalCommand.appendChild(obj[0]);

obj[1] = createInput("hidden" , "employeeCode" , "box" , info[0], null, null );
modalCommand.appendChild(obj[1]);

//HTML 오브젝트 추가 : js영역에 개체 생성 후 ->HTML 영역에 추가 : appendChild()	
let objBtn = createInput("button", null , "small-btn", "업데이트", null, null);


//동적으로 생성된 개체에 이벤트 추가 addEventListener(event,funtion)
objBtn.addEventListener("click", function(){
	serverTransfer(obj,"UpdLevel");
});

modalCommand.appendChild(objBtn);

objBtn = createInput("button", null , "small-btn", "초기화", null, null);
modalCommand.appendChild(objBtn);
	
modal.style.display = "block";

window.onclick = function(event) {
	  if (event.target == modal) {
	    modal.style.display = "none";
	  }
	}	
}


function changePhone(title, data){
	const modal = document.querySelector(".modalBox");
	let modalTitle = document.querySelector(".modalTitle");
	let modalCommand = document.querySelector(".modalCommand");
	
	modalCommand.innerHTML = "";
	
	modalTitle.innerHTML = title + "<div class=\'closeBox\' onClick=\'modalClose()\'> X </div>";
	
	let info = data.split(":");
	
	modal.style.display = "block";
	
	//table 추가
	let table = "<table><tr><th>회원코드</th><th>회원이름</th><th>전화번호</th></tr>";
	table += "<tr><td>" + info[0] + "</td><td>" + info[1] + "</td><td>" + info[2] + "</td></tr></table>";
	modalCommand.innerHTML = table;
	
	//Html Object 추가.
	let obj = [];
	obj[0] = createInput("text" , "memberPhone" , "box" , null , "변경 할 전화번호", null);
	modalCommand.appendChild(obj[0]);
	obj[1] = createInput("hidden" , "memberCode" , "box" , info[0] , null , null);
	modalCommand.appendChild(obj[1]);
	
	//줄바꿈
	modalCommand.appendChild(document.createElement("br"));
	
	//HTML Object 추가 :  JS영역에 개체 생성 후 --> HTML 영역에 추가 : appendChild()
	let objBtn = createInput("button" , null , "small-btn" , "업데이트" , null , null);
	//동적으로 생성된 개체에 이벤트 추가 addEnventListener(event , function)
	objBtn.addEventListener("click" , function(){
		serverTransfer(obj , "UpdPhone")
	});
	
	modalCommand.appendChild(objBtn);
	
	objBtn = createInput("button" , null , "small-btn" , "초기화" , null , null);
	modalCommand.appendChild(objBtn);
	
	modal.style.display = "block";
	
	window.onclick = function(event) {
		  if (event.target == modal) {
		    modal.style.display = "none";
		  }
		}	
}

function changeGoods(title , data){
	const modal = document.querySelector(".modalBox");
	let modalTitle = document.querySelector(".modalTitle");
	let modalCommand = document.querySelector(".modalCommand");
	
	modalCommand.innerHTML = "";
	
	modalTitle.innerHTML = title + "<div class=\'closeBox\' onClick=\'modalClose()\'> X </div>";
	
	let info = data.split(":");
	
	modal.style.display = "block";
	
	//table 추가
	let table = "<table><tr><th>상품코드</th><th>상품이름</th><th>상품매입가</th><th>상품가격</th><th>상품재고</th><th>상품카테고리</th></tr>";
	table += "<tr><td>" + info[0] + "</td><td>" + info[1] + "</td><td>" + info[2] + "</td><td>" + info[3] + "</td><td>" + info[4] + "</td><td>"  + info[5] + "</tr></table>";
	modalCommand.innerHTML = table;
	
	//Html Object 추가.
		let obj = [];
		obj[0] = createInput("hidden" , "goodsCode" , "box" , info[0] , "변경 할 상품코드", null);
		modalCommand.appendChild(obj[0]);
		obj[1] = createInput("text" , "goodsName" , "box" , null , "변경 할 상품 이름" , null);
		modalCommand.appendChild(obj[1]);
		obj[2] = createInput("text" , "goodsCost" , "box" , null , "변경 할 상품 매입가" , null);
		modalCommand.appendChild(obj[2]);
		obj[3] = createInput("text" , "goodsPrice" , "box" , null , "변경 할 상품 가격" , null);
		modalCommand.appendChild(obj[3]);
		obj[4] = createInput("text" , "goodsStock" , "box" , null , "변경 할 상품 재고" , null);
		modalCommand.appendChild(obj[4]);
	
	//줄바꿈
	modalCommand.appendChild(document.createElement("br"));
	
	//HTML Object 추가 :  JS영역에 개체 생성 후 --> HTML 영역에 추가 : appendChild()
	let objBtn = createInput("button" , null , "small-btn" , "업데이트" , null , null);
	//동적으로 생성된 개체에 이벤트 추가 addEnventListener(event , function)
	objBtn.addEventListener("click" , function(){
		serverTransfer(obj , "UpdGoods")
	});
	
	modalCommand.appendChild(objBtn);
	
	objBtn = createInput("button" , null , "small-btn" , "초기화" , null , null);
	modalCommand.appendChild(objBtn);
	
	modal.style.display = "block";
	
	window.onclick = function(event) {
		  if (event.target == modal) {
		    modal.style.display = "none";
		  }
		}	
}

function addGoods()	{
	const modal = document.querySelector(".modalBox");
	let modalTitle = document.querySelector(".modalTitle");
	let modalCommand = document.querySelector(".modalCommand");
	
	modalCommand.innerHTML = "";

	modalTitle.innerHTML = "상품추가" + "<div class=\'closeBox\' onClick=\'modalClose()\'> X </div>";

	modal.style.display = "block";

	//Html Object 추가.
		let obj = [];
		obj[0] = createInput("text" , "goodsCode" , "box" , null, "추가 할 상품코드", null);
		modalCommand.appendChild(obj[0]);
		obj[1] = createInput("text" , "goodsName" , "box" , null, "추가 할 상품 이름" , null);
		modalCommand.appendChild(obj[1]);
		obj[2] = createInput("text" , "goodsCost" , "box" , null, "추가 할 상품 매입가" , null);
		modalCommand.appendChild(obj[2]);
		obj[3] = createInput("text" , "goodsPrice" , "box" , null, "추가 할 상품 가격" , null);
		modalCommand.appendChild(obj[3]);
		obj[4] = createInput("text" , "goodsStock" , "box" , null, "추가 할 상품 재고" , null);
		modalCommand.appendChild(obj[4]);
		obj[5] = createInput("text" , "categoryCode" , "box" , null, "추가 할 상품 카테고리" , null);
		modalCommand.appendChild(obj[5]);

	//줄바꿈
	modalCommand.appendChild(document.createElement("br"));

	//HTML Object 추가 :  JS영역에 개체 생성 후 --> HTML 영역에 추가 : appendChild()
	let objBtn = createInput("button" , null , "small-btn" , "상품 추가하기" , null , null);
	//동적으로 생성된 개체에 이벤트 추가 addEnventListener(event , function)
	objBtn.addEventListener("click" , function(){
		serverTransfer(obj , "InsGoods")
	});

	modalCommand.appendChild(objBtn);

	objBtn = createInput("button" , null , "small-btn" , "초기화" , null , null);
	modalCommand.appendChild(objBtn);

	modal.style.display = "block";
	
	window.onclick = function(event) {
		  if (event.target == modal) {
		    modal.style.display = "none";
		  }
		}	
	}

function serverTransfer(obj , action){
	let form = document.getElementsByName("serverCall")[0];
	form.action = action;
	
	for(idx=0; idx<obj.length; idx++){
		if(obj[idx].value.length <=0){
			alert("입력값 없음");
			return;
		} 
		form.appendChild(obj[idx]);		
	}
	form.submit();
}

function serverTransfer2(obj, action){
	let form = document.getElementsByName("serverCall")[0];
	form.action = action;
	
	for(idx=0; idx<obj.length; idx++){
		if(obj[idx].value.length <= 0) {
			alert("입력값 없음");
			return;
		}form.appendChild(obj[idx]);
	}if(obj[1].value == "PR" || obj[1].value == "MA"||obj[1].value == "AR"){
				
		}else {alert("등급값 확인")
			return;}
		
		form.submit();
}


</script>	
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.1.1/css/all.min.css">
<link rel="stylesheet" href="css/main.css"/>
<style>
table	{
	margin: 0 auto;
	border: 2px solid black;
	border-collapse:collapse; text-align:center;
	width="100%"
}
tr	{
	height:2rem;
	width="100%"
}
th	{
	height:2.5rem; font-weight:900; color: black;
	background-color: #ffa1f9;
	font-weight:900;
	border:1px solid #000000;
	width="100%"
}

td {
	border: 1px solid black;
	width="100%"	
}

#left	{
	float:left;
	width: 25%; height:90%; margin-right:1%;
	border:2px solid black;
	background-color: #ffffff;
	overflow: auto;
}

#middle	{
	float:left;
	width: 25%; height:90%; margin-right:1%;
	border:2px solid black;
	background-color: #ffffff;
		overflow: auto;
}

#right	{
	float:left;
	width: 46%; height:90%;
	border:2px solid black;
	overflow:auto;
	background-color: #ffffff;:auto;
		overflow: auto;
}

.title	{
	width:100%; height:2rem;
	color: black; background-color: #00f0ff; 
	font-size: 1.2rem; font-weight:800;
}

.list	{
	width:100%; padding-bottom:1rem;
	color: black; 
	font-size: 0.9rem; font-weight:600;
}

.box		{
	width:10rem; height:2rem; margin: 0 auto;
	text-align : center;
}
</style>
</head>
<body>
		<div id="contents">
		<div id="left">
			<div class="title">직원정보</div>
			<div class="list">${empList}</div>
			<div id = regEmp onClick=regEmp() >
	직원등록
</div>
		</div>	
		<div id="middle">
			<div class="title">회원정보</div>
			<div class="list">${memberList}</div>
		</div>
		<div id="right">
			<div class="title">상품정보</div>
			<div class="list">${goodsList}</div>
			<input type = "button" value = "상품추가하기" class = "small-btn" onClick = "addGoods()">
		</div>
	</div>	
	<div id = "header">
		<span class = "top-span">${storeName}${emloyeeName }(${levelName }) Last Access : ${accessTime }
	 	<input type="button" value="로그아웃" class= "small-btn" onMouseOver="changeBtnCss(this,'small-btn-over')" onMouseOut="changeBtnCss(this,'small-btn')" onClick="accessOut()" />
	 	</span>
	</div>
	<div id="footer"></div>
	<div class = "modalBox">
		<div class = "modalBody">
			<div class = "modalTitle"></div>
			<div name="requestData" class = "modalCommand"></div>
		</div>
	</div>
	<form name="serverCall" method="post"> </form>
</body>
</html>