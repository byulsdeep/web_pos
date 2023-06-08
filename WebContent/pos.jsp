<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title> Team1 - POS </title>
<script src="js/main.js" type="text/javascript"></script>
<script src="js/pos.js" type="text/javascript"></script>
<link rel="stylesheet" href="css/main.css"/>
<style>
body {
	top: 0;
	left: 0;
	font-size: 1.5rem;
	color: black;
}

.top-span {
	line-height: 3rem;
	padding-right: 20px;
}

#contents {
	position: relative;
	margin: 0 auto;
	width: 100%;
	height: 40rem;
	top: 3rem;
	left: 0;
	right: 0;
	padding-left: 0.5%;
	text-align: center;
}

#salesInfo {
	float: left;
	width: 70%;
	height: 85%;
	margin-right: 1%;
	border: 2px solid #487BE1;
}

#paymentInfo {
	float: right;
	width: 25%;
	height: 85%;
	margin-right: 1%;
	border: 2px solid #487BE1;
}

.title {
	width: 100%;
	height: 4rem;
	color: #FFFFFF;
	background-color: #487BE1;
	font-size: 2rem;
	font-weight: 800;
}

#list {
	float: left;
	width: 70%;
	height: 70%;
	margin-right: 1%;
	border: 2px solid #487BE1;
	overflow: auto;
}

table {	
	width: 100%; 	
}

th {
	background-color: #487BE1;
	color: #FFFFFF;
}

td {
	max-width: 300px;
	white-space: nowrap;
  	text-overflow: ellipsis;
  	overflow: hidden;
  	cursor: pointer;
}



.stock {
	margin: 2%;
	float: right;
	background-color: #487BE1;
	width: 11%;
	height: 14%;
	margin-right: 1%;
	font-size: 3rem;
	cursor: pointer;
}

.function {
	float: right;
	background-color: #487BE1;
	width: 25%;
	height: 8%;
	margin: 1%;
	font-size: 2rem;
	color: white;
	cursor: pointer;
}

#goodsCode {
	float: left;
	background-color: #487BE1;
	width: 65%;
	height: 10%;
	margin: 3%;
	font-size: 3rem;
}

#del {
	cursor: pointer;
}

#hold {
	cursor: pointer;
}

#holdList {
	cursor: pointer;
}

#payment {
	cursor: pointer;
}

#search {
	cursor: pointer;
}

#amount {
	cursor: pointer;
}

#received {
	cursor: pointer;
}

.payBox {
	font-size : 2rem;
	padding : 1rem;
	width: 80%;
}

#changee {
	cursor: pointer;
}

#exit {
	cursor: pointer;
}



</style>
</head>
<body>
	<div id="contents">
		<div id="salesInfo">
			<div class="title">Sale</div>
			<div id="list">				
					<table id="order">
						<tr>
							<th>&nbsp#&nbsp</th>
							<th>&nbspPLU&nbsp</th>
							<th>&nbsp상품&nbsp</th>
							<th>&nbsp가격&nbsp</th>
							<th>&nbsp수량&nbsp</th>
							<th>&nbsp합계&nbsp</th>
						</tr>
					</table>										
			</div>
			<div class="func-btn">
				<div id="down" class="stock" onClick="updownQty(-1)">▼</div>
				<div id="up" class="stock" onClick="updownQty(1)">▲</div>
				<div id="del" class="function" onClick="delRecord()">삭제</div>
				<div id="hold" class="function" onClick="holdOrder()">보류</div>
				<div id="holdList" class="function" onClick="retrieveOrder('보류목록')" >가져오기</div>
				<div id="payment" class="function" onClick="orderComplete()" >주문결제</div>
			</div>

			<div class = "barcodeBox">
				<input id="goodsCode" type='text' name="goodsCode" placeholder="PLU" onKeyUp="searchGoods(event)" />
			</div>

			<div id="search" class="function">Enter</div>
			</div>
			<div id="paymentInfo">
				<div class="title">결제정보</div>
				</br>
				<div class="title">주문금액</div>
				<div id="amount" name ="amount" class="payBox"></div>

				<div class="title">받은돈</div>
				<div id="received">
					<input type='text' name= "received" class="payBox" onKeyUp="setChange(this, event)">
				</div>

				<div class="title" id="changee">거스름돈</div>
				<div id="change" name="change" class = "payBox"></div>
				<div id="exit" name = "exit" class="title" onClick="payment()">결제완료</div>
			</div>
		</div>
		<div id="header">
			<span class="top-span">${storeName}${employeeName}(${levelName})
				Last Access : ${accessTime} <input type="button" value="로그아웃"
				class="small-btn" onMouseOver="changeBtnCss(this,'small-btn-over')"
				onMouseOut="changeBtnCss(this,'small-btn')" onClick="accessOut()" />
			</span>
		</div>
		<div class="modalBox">
			<div class="modalBody">
				<div class="modalTitle"></div>
				<div class="modalCommand"></div>
			</div>
		</div>
		<form name="serverCall" method="post"></form>
</body>
</html>