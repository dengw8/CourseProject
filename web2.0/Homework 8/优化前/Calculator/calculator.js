var clearmsg = false;
var ispoint = true;

//显示求表达式
function display(obj) 
{
	var data = obj.value, text = document.getElementById("text");
	if(clearmsg) 
	{
		clearmsg=false;
	}
	if(data == "+" || data == "-" || data == "*" || data == "/") 
	{
		ispoint = true;
	}
	if(data == "0" && text.value == "0") 
	{
		text.value = 0;	
	}
	else if(text.value == "0" && data == "(") 
	{
		text.value = "(";
	}
	else if(text.value == "0" && data == ")") 
	{
		text.value = 0;
	}
	else if(text.value == "0" && data != "+" && data != "-" && data != "*" && data != "/") 
	{
		text.value = parseInt(text.value) + parseInt(data);
	}
	else 
	{
		text.value += data;
	}
}

//计算求值
function equal() 
{
	var obj = document.getElementById("text");
	try
	{
		obj.value = eval(obj.value);
		ispoint = true;
		clearmsg = true;
	} 
	catch(err)
	{
		alert("提示：算术表达式非法!");
		obj.value = 0;
	}
}

//显示小数点
function point()
{
	var obj = document.getElementById("text");
	if(ispoint)
	{
		obj.value += ".";
		ispoint = false;
	}
}

//清除功能
function clean() 
{
    var obj = document.getElementById("text");
	obj.value = 0;
	clearmsg = false;
	ispoint = true;
}

//删除功能
function back() 
{
	var obj = document.getElementById("text");
	if(obj.value.length == 1) 
		obj.value = 0;
	else {
		obj.value = obj.value.slice(0, obj.value.length-1);
	}
}