/* Server 요청
*	- Get 
        data :	?itemName=itemValue&itemName=itemValue
*	- Post
        data :	itemName=itemValue&itemName=itemValue
        data : (itemName, itemValue) --> FormData
    	
        Ajax
        : XhlHttpRequest
          ajax process
          1. XHL 활성화
                 new XHLHttpRequest();
          2. 콜백함수 연결
                 콜 --> 전송방식(post | get), URL설정, 비동기 | 동기 여부 설정
                 XMR.onreadystatechange = 콜백함수 연결
                  - readyState : 0 : .UNSET
                               : 1 : .OPENED
                               : 2 : .HEADERS_RECEIVED
                               : 3 : .LOADING
                               : 4 : .DONE
                  - STATUS     : 200 : OK
                                   : 300 : loop
                               : 400 : Bad Request
                               : 500 : Internal Server Error                
          3. 서버 요청
                 XHL open              
          4. 콜백함수를 이용해 문서에 적용 : responseText        
*/
let list = [];
let items = ["goodsNumber", "goodsCode", "goodsName", "goodsPrice", "goodsQuantity", "goodsAmount"];
let selectedIdx = -1;
let totalAmountF = 0;
let amount = 0;
let changeValue = 0;
let membercode = "99999";

function searchGoods(event) {
    if (event.key != "Enter") return;

    let goodsCode = document.getElementsByName("goodsCode")[0];

    /* 바코드 유효성 검사: 입력 문자의 길이가 4자리 --> isCode(){} */
    if (isCode(goodsCode.value, 10)) {
        // Ajax 이용
        const jobCode = "Search";
        const formData = goodsCode.name + "=" + goodsCode.value;
        sendAjax(jobCode, formData, "setOrderList");

    } else {
        alert("10자리 상품코드를 입력해주세요");
        goodsCode.focus;
    }
}
function sendAjax(jobCode, formData, funcName) {
    const ajax = new XMLHttpRequest();
    ajax.open('POST', jobCode);
    // ajax시작 ajax.send(formData);
    ajax.setRequestHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
    ajax.send(formData);

    ajax.onreadystatechange = function () {
        if (ajax.readyState == ajax.DONE && ajax.status == "200") {
            window[funcName](ajax.responseText);//3111:Xenoblade:44
        }
    }
}
function setOrderList(data) {
    const search = document.getElementsByName("goodsCode")[0];
    let isCheck = false;
    //3111:Xenoblade:44
    //goodsCode:goodsName:goodsPrice:1:O
    if (data != "success" && data != "error") {
        if (data != "none") {
            if (data.length > 0) {
                let obj = document.getElementsByName("received")[0];
                obj.value = "";
                obj.placeholder = "";
                let change = document.getElementById("change");
                change.innerText = "";
                if (list.length > 0) {
                    const goodsCode = data.substring(0, data.indexOf(":"));
                    isCheck = increaseQty(goodsCode);
                }
                if (!isCheck) {//3111:Xenoblade:44:1:0 //3111 Xenoblade 44 1 0
                    let record = (data += (":1:0")).split(":");
                    list.push(record);//list = {{3111,Xenoblade,44,1,0},{},{}}
                }
                makeOrder();
                
            }
        } else {
            alert("등록되지 않은 상품입니다");
        }
    } else {
		if(data == "success") {
			
        for (j = list.length - 1; j >= 0; j--) {
            if (list[j][4] == 0) {
                list.splice(j, 1);
            }
        }
        amount = totalAmountF/10;
        makeOrder();
        isAccrue();
        } else {
	alert("시스템 에러");
}
    }
    search.value = "";
    search.focus;
}

function isAccrue() {
	const modal = document.querySelector(".modalBox");
		let modalTitle = document.querySelector(".modalTitle");
		let modalCommand = document.querySelector(".modalCommand");
	
		modalTitle.innerHTML = "<br><br>시스템 메시지" + "<div class=\'closeBox\' onClick=\'modalClose()\'> X </div>";
		modalCommand.innerHTML = "<br>결제가 정상적으로 처리돼었습니다. 포인트를 적립하시겠습니까?<br><br><br>";
		
		let yesBtn = createInput("button", null, "small-btn", "네", null, null);
		yesBtn.addEventListener("click", function(){
		yesMemberCode();
	});
		modalCommand.appendChild(yesBtn);
	
	
		let noBtn = createInput("button", null, "small-btn", "아니오", null, null);
		noBtn.addEventListener("click", function(){
		noMemberCode();
	});
		
	modalCommand.appendChild(noBtn);
	
		modal.style.display = "block";
}

function yesMemberCode() {
	const modal = document.querySelector(".modalBox");
	let modalTitle = document.querySelector(".modalTitle");
	let modalCommand = document.querySelector(".modalCommand");
	
	modalTitle.innerHTML = "<br><br>시스템 메시지" + "<div class=\'closeBox\' onClick=\'modalClose()\'> X </div>";
	modalCommand.innerHTML = "회원번호를 입력해주세요<br><br><br>";
	let memberBox = createInput("text", "memberCode", "box", null, "회원번호", null);
	modalCommand.appendChild(memberBox);
	
	let yesBtn = createInput("button", null, "small-btn", "확인", null, null);
		yesBtn.addEventListener("click", function(){
		setMemberCode();
	});
		modalCommand.appendChild(yesBtn);
	modal.style.display = "block";
}

function noMemberCode() {
	const modal = document.querySelector(".modalBox");
	let modalTitle = document.querySelector(".modalTitle");
	let modalCommand = document.querySelector(".modalCommand");
	
	modalTitle.innerHTML = "<br><br>시스템 메시지" + "<div class=\'closeBox\' onClick=\'modalClose()\'> X </div>";
	
	modalCommand.innerText = "감사합니다 또 오세요"
}

function setMemberCode() {
	let modalTitle = document.querySelector(".modalTitle");
	let modalCommand = document.querySelector(".modalCommand");
	
	modalTitle.innerHTML = "<br><br>시스템 메시지" + "<div class=\'closeBox\' onClick=\'modalClose()\'> X </div>";
		
	memberCode = document.getElementsByName("memberCode")[0].value;
	if(isCode(memberCode,5)) {
        let formData = "memberCode=" + memberCode;
        sendAjax("CheckMember", formData, "checkMember");		
	} else {
		modalCommand.innerHTML = "5자리 회원코드를 입력해주세요";
		
		let yesBtn = createInput("button", null, "small-btn", "확인", null, null);
		yesBtn.addEventListener("click", function(){
		yesMemberCode();
			});
		modalCommand.appendChild(yesBtn);
	}
}

function checkMember(data) {
	let modalTitle = document.querySelector(".modalTitle");
	let modalCommand = document.querySelector(".modalCommand");
	modalTitle.innerHTML = "<br><br>시스템 메시지" + "<div class=\'closeBox\' onClick=\'modalClose()\'> X </div>";
	
	if(data !== "no") {
		modalCommand.innerHTML = "<br>" + data + "님";
		
		let yesBtn = createInput("button", null, "small-btn", "확인", null, null);
		yesBtn.addEventListener("click", function(){
		accruePoints();
	});
		modalCommand.appendChild(yesBtn);
		
	} else {
		modalCommand.innerText = "등록되지 않은 회원입니다."
		
		let yesBtn = createInput("button", null, "small-btn", "확인", null, null);
		yesBtn.addEventListener("click", function(){
		yesMemberCode();
			});
		modalCommand.appendChild(yesBtn);
		
	}
}

function accruePoints() {
	let formData = "memberCode=" + memberCode + "&amount=" + amount;
	sendAjax("AccruePoints", formData, "final");	
}

function final(data) {
	let modalTitle = document.querySelector(".modalTitle");
	let modalCommand = document.querySelector(".modalCommand");
	modalTitle.innerHTML = "<br><br>시스템 메시지" + "<div class=\'closeBox\' onClick=\'modalClose()\'> X </div>";
	
	if(data !== "error") {
		modalCommand.innerText = data + "포인트가 적립되었습니다";
	} else {
		modalCommand.innerText = "포인트 적립 실패";
	}
	
	let yesBtn = createInput("button", null, "small-btn", "확인", null, null);
		yesBtn.addEventListener("click", function(){
		noMemberCode();
	});
		modalCommand.appendChild(yesBtn);
}

function increaseQty(goodsCode) {
    let isCheck = false;
    for (j = 0; j < list.length; j++) {
        if (list[j][4] == 0) {
            if (list[j][0] == goodsCode) {
                list[j][3] = Number(list[j][3]) + 1;
                isCheck = true;
                break;
            }
        }
    }
    return isCheck;
}
function makeOrder() {
    const search = document.getElementsByName("goodsCode")[0];
    const frame = document.getElementById("order");
    let tot = document.getElementById("amount");
    frame.innerText = "";
    let th = [];
    let thText = [" # ", " PLU ", " Item ", " Value ", " Quantity ", " Amount "];
    const topRow = document.createElement("tr");
    for (i = 0; i < 6; i++) {
        th.push(document.createElement("th"));
        topRow.appendChild(th[i]);
        th[i].innerText = thText[i];
    }
    frame.appendChild(topRow);
    let recordNum = 0;
    let totalAmount = 0;
    if (list.length > 0) {
        for (j = 0; j < list.length; j++) {
            if (list[j][4] == 0) {
                recordNum++;
                const goodsInfo = document.createElement("tr");
                goodsInfo.setAttribute("onClick", "selectIdx(" + j + ")");
                let record = [];
                for (i = 0; i < 6; i++) {
                    record.push(document.createElement("td")); ``
                    record[i].setAttribute("class", items[i]);
                    //items = ["goodsNumber", "goodsCode", "goodsName", "goodsPrice", "goodsQuantity"
                    goodsInfo.appendChild(record[i]);
                }
                record[0].innerText = recordNum;                       		    //순번
                record[1].innerText = list[j][0];	               	 	    //goodsCode
                record[2].innerText = list[j][1];	              		    //goodsName
                record[3].innerText = Number(list[j][2]).toLocaleString();	//goodsPrice
                record[4].innerText = Number(list[j][3]).toLocaleString();	//Quantity
                record[5].innerText = (Number(list[j][2]) * Number(list[j][3])).toLocaleString();  //amount
                totalAmount += (Number(list[j][2]) * Number(list[j][3]));
                frame.appendChild(goodsInfo);
            }
        }
    }
    
    totalAmountF = totalAmount;
    tot.innerText = totalAmount.toLocaleString();
    selectedIdx = list.length - 1;
    search.value = "";
    search.focus;
}
function selectIdx(i) {
    selectedIdx = i;
}
function updownQty(num) {
    if (num == -1 && list[selectedIdx][3] == 1) {
    } else {
        list[selectedIdx][3] = Number(list[selectedIdx][3]) + num;
        let selectedIdx2 = selectedIdx;
        makeOrder();
        selectedIdx = selectedIdx2;
    }
}
function delRecord() {
    selectedIdx = (selectedIdx != -1) ? selectedIdx : list.length - 1;
    list.splice(selectedIdx, 1);
    makeOrder();
}
function holdOrder() {
    let maxNumber = findMaxNumber();
    for (j = 0; j < list.length; j++) {
        if (list[j][4] == 0) {
            list[j][4] = maxNumber;
        }
    }
    makeOrder();
}
function findMaxNumber() {
    let maxNumber = 0;
    for (j = 0; j < list.length; j++) {
        if (Number(list[j][4]) > maxNumber) {
            maxNumber = Number(list[j][4]);
        }
    }
    return maxNumber + 1;
}
function isCode(data, num) {
    return (data.length == num) ? true : false;
}
function retrieveOrder(title) {
    const modal = document.querySelector(".modalBox");
    let modalTitle = document.querySelector(".modalTitle");
    let modalCommand = document.querySelector(".modalCommand");
    modalTitle.innerHTML = title + "<div class=\'closeBox\' onClick=\'modalClose()\'> X </div>";
    /* 보류리스트 출력
        1. 현재 처리되지 않은 주문리스트 여부 확인
        2. 보류된 주문의 존재 여부 확인
    */
    if (list.length > 0) {
        if (list[list.length - 1][4] != 0) {
            if (findMaxNumber() > 1) {
                let maxNumber = findMaxNumber();
                let html = "<table><tr><th>번호</th><th>내용</th></tr>";
                for (number = 1; number < maxNumber; number++) {
                    let count = -1;
                    let info;
                    for (j = 0; j < list.length; j++) {
                        if (Number(list[j][4]) == number) {
                            count++;
                            if (count == 0) {
                                info = list[j][1];
                            }
                        }
                    }
                    info += ("외 " + count + "개의 품목");
                    html += ("<tr onClick='moveOrderList(" + number + ", " + count + ")'><td>" + number + "</td><td>" + info + "</td></tr>");
                }
                html += "</table>";
                modalCommand.innerHTML = html;
            } else {
                modalCommand.innerText = "보류 목록이 존재하지 않습니다.";
            }
        } else {
            modalCommand.innerText = "현재 주문을 완료하신 후 목록을 불러올 수 있습니다.";
        }
    } else {
        modalCommand.innerText = "보류 목록이 존재하지 않습니다.";
    }
    modal.style.display = "block";
}
function moveOrderList(number, count) { //[1,2,3,4,5]
    // 1. 주문상태를 number에서 0으로 업데이트
    // 2. number보다 큰 다른 보류번호를 -1을 더한 값으로 업데이트
    for (j = 0; j < list.length; j++) {
        list[j][4] = (list[j][4] == number) ? 0 : (list[j][4] > number) ? list[j][4] - 1 : list[j][4];
    }
    // 3. 보류번호가 number에 해당되는 상품리스트는 배열의 맨마지막으로 이동
    let i = -1;
    for (j = list.length - 1; j >= 0; j--) {
        if (list[j][4] == 0) {
            i = j
            let record = list[j];
            list.push(record);
        }
    }
    list.splice(i, count + 1);
    // 4. makeOrder()
    makeOrder();
    modalClose();
}
function orderComplete() {
    if (Number(document.getElementById("amount").innerText) != "" ) {
        let obj = document.getElementsByName("received")[0];
        obj.placeholder = "Enter value";
        obj.focus();
    } else {
        alert("상품을 선택해주세요");
    }
}
function setChange(obj, event) {
    if (event.key != "Enter") return;
    let change = document.getElementById("change");
    if (obj.value < totalAmountF) {
        alert("잔액이 부족합니다");
    } else {
        changeValue = Number(obj.value) - totalAmountF;
        change.innerText = (Number(obj.value) - totalAmountF).toLocaleString();
    }
}
function payment() {
    if (Number(changeValue) >= 0) {
        const jobCode = "Payment";
        let formData = "";
        /* formData */
        
        for (j = 0; j < list.length; j++) {
        formData += "goodsCode=" + list[j][0] + "&quantity=" + list[j][3];
        if(j != list.length-1) {
			formData += "&";
			}
        }
        sendAjax(jobCode, formData, "setOrderList");
    } else {
        alert("거래 실패");
        let obj = document.getElementsByName("received")[0];
        obj.placeholder = "Enter value";
        obj.focus();
    }
}
/* Javascript Array
    : 가변사이즈 = 동적할당
    : 자바의 ArrayList와 비슷하다
	
    let array_name  = []; --> 1차원배열 = 레코드
    let array_name2 = []; --> 1차원배열 = 레코드
	
    ** push --> 입력
    array_name2.push(array_name); --> 2차원배열 = 리스트
	
    ** array address --> 수정
    ** 삭제 --> shift : 배열의 요소중에 맨 앞에 있는 요소를 삭제 : index == 0
               pop: 배열의 요소중에 맨 뒤에 있는 요소를 삭제 : index == length-1
               splice: 특정 위치의 요소를 삭제 --> array.splice(index,삭제하고자 하는 방의 개수)
       let array = ["a", "b", "c", "d", "e"];
       array.splice(2,2);
       array.splice(2,2, "f", "g");		["a", "b", "f", "g", "e"] 
       /* shift, pop */