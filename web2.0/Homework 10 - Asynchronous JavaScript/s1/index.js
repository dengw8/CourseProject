window.onload = function () {
	var li = document.getElementById("control-ring").getElementsByTagName("li");
	var span = document.getElementById("control-ring").getElementsByTagName("span");
	var info = document.getElementById("info-bar").getElementsByTagName("div")[0];
	var totalNum = 0;
	var extend = document.getElementById("extend");
	var buttons = document.getElementById("button");
	var flag;

	for (var i = 0; i < li.length; i++) {
		li[i].onmouseover = function() {
			flag = 1;
		};
		li[i].onmouseout = function() {
			flag = 0;
		};
	}
	info.onmouseover = function() {
		flag = 1;
	};
	info.onmouseout = function() {
		flag = 0;
	};
	buttons.onmouseover = function() {
		flag = 1;
	};
	buttons.onmouseout = function() {
		flag = 0;
	};

	extend.onmouseout = function() {
		setTimeout(function() {
			if (flag == 1) return;

			// reset
			totalNum = 0;
			for (var i = 0; i < span.length; i++) {
				span[i].style.display = "none";
			}
			for (var i = 0; i < li.length; i++) {
				li[i].clicked = 0;
				li[i].style.backgroundColor = "rgba(48, 63, 159, 1)";

			}
			changeBackground(li, li.length+1, 1);
			info.style.backgroundColor = "gray";
			info.innerHTML = "";
		}, 0);
	};

	for (var i = 0; i < li.length; i++) {
		li[i].clicked = 0;
		li[i].style.backgroundColor = "rgba(48, 63, 159, 1)";
		li[i].onclick = function(i) {
			return function () {
				if (this.clicked == 1 || this.style.backgroundColor == "gray") return;

				if (this.clicked == 0) {
					totalNum++;
					li[i].clicked = 1;
					changeBackground(li, i, 0);
					span[i].style.display = "block";
					span[i].innerHTML = "...";

					ajax("http://localhost:3000", function(returnNum) {
						changeBackground(li, i, 1);
						span[i].innerHTML = returnNum;
						if (totalNum == li.length)
						info.style.backgroundColor = "rgba(48, 63, 159, 1)";
					}, function () {});
				}
			};
		}(i);
	}

	info.onclick = function () {
		for(var i = 0; i < li.length; i++) {
			if(li[i].clicked == 0) {
			    info.innerHTML = "";
			    return;				
			}
		}

		if (info.style.backgroundColor != "gray") {
			var sum = 0;
			for (var i = 0; i < span.length; i++) {
				sum += Number(span[i].innerHTML);
			}

			if (sum == 0) {
				info.innerHTML = "";
			} else {
				info.innerHTML = sum;
			}
			info.style.backgroundColor = "gray";
			changeBackground(li, li.length+1, 1);
			return;
		}
	};
};

function changeBackground(li, index, status) {
	if (status == 0) {
		for (var i = 0; i < li.length; i++) {
			if (i == index) {
				li[i].style.backgroundColor = "rgba(48, 63, 159, 1)";
			} else {
				li[i].style.backgroundColor = "gray";
			}
		}
	} else if (status == 1) {
		for (var i = 0; i < li.length; i++) {
			if (i == index) {
				li[i].style.backgroundColor = "gray";
			} else {
				li[i].style.backgroundColor = "rgba(48, 63, 159, 1)";
			}
		}
	}
}

function ajax(url, successfunction, failfunction) {
	var oAjax;
	// 创建Ajax对象
	if (window.XMLHttpRequest) {
		oAjax = new XMLHttpRequest();
	} else {
		oAjax = new ActiveXObject("Microsoft.XMLHTTP");
	}
	// 连接服务器
	oAjax.open("GET", url+"/?Random="+Math.random(), true);
	// 发送请求
	oAjax.send();
	// 接受返回
	oAjax.onreadystatechange = function () {
		if (oAjax.readyState == 4 && oAjax.status == 200) {
			successfunction(oAjax.responseText);
		}
	};
}