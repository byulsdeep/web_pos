<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Team1 - 로그인 </title>
<script src="js/main.js" type="text/javascript"></script>
<script>
function accessCtl(){
	/* employeeCode.length == 5 */
	if(document.getElementsByName("employeeCode")[0].value.length != 5){
		return;
	}
	
	/* Server 요청 : request << form */
	const form = document.getElementsByName("accessForm")[0];
	form.appendChild(document.getElementsByName("employeeCode")[0]);
	form.appendChild(document.getElementsByName("employeePassword")[0]);
	form.submit();
}

function getMessage(message) {
	const modal = document.querySelector(".modalBox");
	let modalTitle = document.querySelector(".modalTitle");
	let modalCommand = document.querySelector(".modalCommand");

	if (message != "" && message != null) {
		modalTitle.innerHTML = "<br>로그인";
		modalCommand.innerHTML = message
		+ "<div class=\'closeBox\' onMouseOver=\"changeBtnCss(this, 'closeBox-over')\" onMouseOut=\"changeBtnCss(this, 'closeBox')\" onClick=\'modalClose()\'> Close </div>";
		
		modal.style.display = "block";
		
		window.onclick = function(event) {
			  if (event.target == modal) {
			    modal.style.display = "none";
			  }
			}
	}
}

function logInModal() {
	const modal = document.querySelector(".modalBox");
	let modalTitle = document.querySelector(".modalTitle");
	let modalCommand = document.querySelector(".modalCommand");
	modalTitle.innerHTML = "<br>로그인" + "<div class=\'closeBox\' onClick=\'modalClose()\'> Close </div>";
	modalCommand.innerHTML = "";
	let obj = [];
	obj[0] = createInput("text", "employeeCode", "box", null, "00001", null);
	modalCommand.appendChild(obj[0]);
	modalCommand.appendChild(document.createElement("br"));
	
	obj[1] = createInput("password", "employeePassword", "box", null, "1111", null);
	modalCommand.appendChild(obj[1]);
	modalCommand.appendChild(document.createElement("br"));
	
	let objBtn = createInput("button", null, "small-btn", "로그인", null, null);
	modalCommand.appendChild(objBtn);
	objBtn.addEventListener("click", function(){
		accessCtl();
	});
	
	modal.style.display = "block";
	
}
</script>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.1.1/css/all.min.css">
<link rel="stylesheet" href="css\main.css" />
</head>
<body onLoad="getMessage('${param.mes}')">
		<br><br><br><br><br><br>
	<div id="logo">Group 1 POS</div>
	<div id="outline">
		<br><br><br><br><br><br>
		<i class="fa-solid fa-arrow-right-to-bracket" id="logInBtn" onClick="logInModal()">로그인</i>
		<form name="accessForm" action="LogIn" method="post">	

		</form>
	</div>
	<div class="modalBox" >
		<div class="modalBody">
			<div class="modalTitle"></div>
			<div class="modalCommand"></div>
		</div>
	</div>
</body>
</html>