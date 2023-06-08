<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title> Team 1 - 메인 </title>
<script src="js/main.js" type="text/javascript"></script>
<script>
function movePage(level, action) {
	const modal = document.querySelector(".modalBox");
	let modalTitle = document.querySelector(".modalTitle");
	let modalCommand = document.querySelector(".modalCommand");

	if (action == "StoreMgr" && level == "아르바이트") {
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
	
	if (action == "StatMgr" && (level == "아르바이트" || level == "매니저")) {
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
	
	

	let form = document.getElementsByName("serverCall")[0];
	form.setAttribute("action", action);
	form.submit();
}

function getMessage(message) {
	const modal = document.querySelector(".modalBox");
	let modalTitle = document.querySelector(".modalTitle");
	let modalCommand = document.querySelector(".modalCommand");

	if (message != "" && message != null) {
		modalTitle.innerHTML = "<br>시스템 메시지";
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

</script>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.1.1/css/all.min.css">
<link rel="stylesheet" href="css/main.css"/>
</head>
<body onLoad="getMessage('${param.mes}')">
	<br><br><br><br><br><br>
	<div id="logo">Group 1 POS</div>
	 <div class="container">
	${menu}
	</div>
	<div id="header">
		<span class="top-span">${employeeName}(${levelName}) Last Access : ${accessTime}
			<input type="button" value="로그아웃" class="small-btn" onMouseOver="changeBtnCss(this, 'small-btn-over')" onMouseOut="changeBtnCss(this, 'small-btn')" onClick="accessOut()" />
		</span>
	</div>
	<div class="modalBox" >
		<div class="modalBody">
			<div class="modalTitle"></div>
			<div class="modalCommand"></div>
		</div>
	</div>
	<form name="serverCall" method="post"></form>
</body>
</html>