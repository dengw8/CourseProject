var ispoint = true;
//显示得到表达式
function display(obj) {
	var valu = $("#text").val(); judge(obj);
	if($("#text").val() == "0" && (obj.value == "0" || obj.value == ")")) {
		$("#text").val("0");
	} else if($("#text").val() == "0" && ((obj.value != "+" && obj.value != "-" && obj.value != "*" && obj.value != "/") || obj.value == "(")) {
		$("#text").val(obj.value);
	} else {
		$("#text").val($("#text").val()+obj.value);
	}
}

//进程逻辑判断
function judge(obj) {
	if(obj.value == "+" || obj.value == "-" || obj.value == "*" || obj.value == "/") {
		ispoint = true;
	}
}

//显示小数点
function point() {
	var obj = $("#text");
	if(ispoint) {
		obj.val(obj.val()+".");
		ispoint = false;
	}
}

//清除功能
function clean() {
    var obj = $("#text");
	obj.val("0");
	ispoint = true;
}

//删除功能
function back() {
	var obj = $("#text");
	if(obj.val().length == 1)
		obj.val("0");
	else {
		obj.val(obj.val().slice(0, obj.val().length -1));
	}
}

//表达式求值
function equal() {
	var obj = $("#text");
	try{
		obj.val(eval(obj.val()));
		ispoint = true;
	} catch(err) {
		alert("提示：算术表达式非法!");
		obj.val("0");
	}
}