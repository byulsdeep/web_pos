<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title> Team1 - 관리정보 </title>
<script src="js/main.js" type="text/javascript"></script>
<script src="js/pos.js" type="text/javascript"></script>
<link rel="stylesheet" href="css/main.css"/>
<style>
table	{
	margin: 0 auto;
	border-collapse:collapse; text-align:center;
}
tr	{
	height:2rem;
}
th	{
	height:2.5rem; font-weight:900; color:#000000;
}

#left	{
	float:left;
	width: 25%; height:90%; margin-right:1%;
	border:2px solid #75BC00;
	overflow: auto;
}

#middle	{
	float:left;
	width: 25%; height:90%; margin-right:1%;
	border:2px solid #75BC00;
	overflow: auto;
}

#right	{
	float:left;
	width: 46%; height:90%;
	border:2px solid #75BC00;
	overflow:auto;
}

.title	{
	width:100%; height:2rem;
	color: #FFFFFF ;background-color: #75BC00; 
	font-size: 1.2rem; font-weight:800;
}

.list	{
	width:100%; padding-bottom:1rem;
	color: #4C4C4C ; 
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
			<div class="title">금월의 일별 직원별 출퇴근 기록</div>
			<div class="list">${accessHistory}</div>
		</div>
		<div id="right">
			<div class="title">금월의 일별 매출 및 영업이익</div>
			<div class="list">${sales}</div>
		</div>
	</div>	
	<div id="footer"></div>
	<div class="modalBox">
		<div class="modalBody">
			<div class="modalTitle"></div>
			<div class="modalCommand"></div>
		</div>
	</div>
	<div id="header">
		<span class="top-span">${storeName} ${employeeName}(${levelName}) Last Access : ${accessTime}
			<input type="button" value="로그아웃" class="small-btn" onMouseOver="changeBtnCss(this, 'small-btn-over')" onMouseOut="changeBtnCss(this, 'small-btn')" onClick="accessOut('${hiddenData }')" />
		</span>
	</div>
	<form name="serverCall" method="post"></form>
</body>
</html>