function accessOut(){
  let form = document.getElementsByName("serverCall")[0];
  form.setAttribute("action", "LogOut");
  form.submit();
}

function changeBtnCss(obj, cName){
	obj.className = cName;
}

function modalClose(){
	const modal = document.querySelector(".modalBox");
	let modalTitle = document.querySelector(".modalTitle");
	let modalCommand = document.querySelector(".modalCommand");
	
	modalTitle.innerText = "";
	modalCommand.innerText = "";
	
	modal.style.display = "none";
}

function createInput(type, name, className, value, placeholder, isRead){
	let obj = document.createElement("input");
	obj.setAttribute("type", type);
	if(name != null) obj.setAttribute("name", name);
	if(className != null) obj.setAttribute("class", className);
	if(value != null) obj.setAttribute("value", value);
	if(placeholder != null) obj.setAttribute("placeholder", placeholder);
	if(isRead != null) obj.setAttribute("readOnly", isRead);
	
	return obj;
}