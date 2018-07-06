window.onload = function () {
	var li = document.getElementById("control-ring").getElementsByTagName("li");
	var span = document.getElementById("control-ring").getElementsByTagName("span");
	var info = document.getElementById("info-bar").getElementsByTagName("div")[0];
	var sequence = document.getElementById("sequence");	
	var extend = document.getElementById("extend");
	var buttons = document.getElementById("button");
	var totalNum = 0;
	var flag;
	var sign = 0;

	// 生成随机序列
	var list = new Array();
	var hadused = new Array();
	var reflect = new Array("A", "B", "C", "D", "E");

	// 初始状态
	changeBackground(li, li.length+1, 1);

	for (var i = 0; i < li.length; i++) {
		li[i].onmouseover = function () {
			flag = 1;
		};

		li[i].onmouseout = function () {
			flag = 0;
		};
	}
	info.onmouseover = function () {
		flag = 1;
	};
	info.onmouseout = function () {
		flag = 0;
	};
	buttons.onmouseover = function () {
		flag = 1;
	};
	buttons.onmouseout = function () {
		flag = 0;
	};

	extend.onmouseout = function() {
		setTimeout(function () {
			if (flag == 1) return;

			// reset
			totalNum = 0;
			for (var i = 0; i < span.length; i++) {
				span[i].style.display = "none";
			}
			for (var i = 0; i < li.length; i++) {
				li[i].clicked = 0;
			}
			changeBackground(li, li.length+1, 1);
			info.style.backgroundColor = "gray";
			info.innerHTML = "";
			sequence.style.display = "none";
			sequence.innerHTML = "";
		}, 0);
	};

	info.onclick = function () {
		if (info.style.backgroundColor == "gray") return;

		if (info.style.backgroundColor != "gray") {
			var sum = 0;
			for (var i = 0; i < span.length; i++) {
				sum += Number(span[i].innerHTML);
			}

			if (sum == 0 || sum == NaN) {
				info.innerHTML = "";
			} else {
				info.innerHTML = sum;
			}
			info.style.backgroundColor = "gray";
			changeBackground(li, li.length+1, 1);
			return;
		}

		for(var i = 0; i < li.length; i++) {
			if(li[i].style.backgroundColor == "gray" || li[i].clicked == 0) {
			    info.innerHTML = "";
			    return;				
			}
		}

	};

	for (var i = 0; i < li.length; i++) {
		li[i].clicked = 0;
		li[i].onclick = function(i) {
			return function (ev) {
				sequence.innerHTML += " " + reflect[i];
				var oEvent = ev || event;

				if (this.style.backgroundColor == "gray") return;

				if (this.clicked == 0) {
					totalNum++;
				// if (span[i].innerHTML == "") totalNum++;
				// alert(totalNum);
				
					changeBackground(li, i, 0);
					span[i].style.display = "block";
					span[i].innerHTML = "...";

				// 
					ajax("http://localhost:3000", function(returnNum) {
						changeBackground(li, i, 1);
						span[i].innerHTML = returnNum;
						if (totalNum == li.length)
							info.style.backgroundColor = "rgba(48, 63, 159, 1)";
						if (sign == 1) {

							list.hasclick++;
						// alert(list.hasclick);
							if (list.hasclick != list.length) {
								setTimeout(function() {
									li[list[list.hasclick]].onclick();
								},0);
							} else {
								setTimeout(function() {
									info.onclick();
								},0);
								sign = 0;
							}
						}
					}, function () {
					// function for fail state
					});
					oEvent.cancelBubble = true;
				}
			};
		}(i);
	}

	buttons.onclick = function () {
		var temp;
		sign = 1;
		list.hasclick = 0;
		totalNum = 0;
		info.innerHTML = "";
		for (var i = 0; i < li.length; i++) {
			hadused[i] = false;
		}
		for (var i = 0; i < li.length; i++) {
			while (true) {
				temp = Math.floor(Math.random() * 100) % li.length;
				if (!!hadused[temp]) continue;
				else {
					hadused[temp] = true;
					list[i] = temp;
					console.log(temp);
					break;
				}
			}
		}
		sequence.style.display = "block";
		sequence.innerHTML = "";

		setTimeout(function() {
			li[list[0]].onclick();
		},0);

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
		} else {
			// failfunction();
		}
	};
}