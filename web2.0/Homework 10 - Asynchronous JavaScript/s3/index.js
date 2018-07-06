window.onload = function () {
	var li = document.getElementById("control-ring").getElementsByTagName("li");
	var span = document.getElementById("control-ring").getElementsByTagName("span");
	var info = document.getElementById("info-bar").getElementsByTagName("div")[0];
	var totalNum = 0;
	var sign = 0;
	var extend = document.getElementById("extend");
	var button = document.getElementById("button");
	var flag;

	// initial
	changeBackground(li, li.length+1, 1);

	for (var i = 0; i < li.length; i++) {
		li[i].clicked = 0;
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
	button.onmouseover = function () {
		flag = 1;
	};
	button.onmouseout = function () {
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

	button.onclick = function () {
		sign = 1;
		for (var i = 0; i < li.length; i++) {
			li[i].onclick();
		}
	};

	// for click event
	for (var i = 0; i < li.length; i++) {
		li[i].onclick = function(i) {
			return function (ev) {

				var oEvent = ev || event;
				oEvent.cancelBubble = true;

				if (this.style.backgroundColor == "gray") return;

				var that = this;
				span[i].style.display = "block";
				span[i].innerHTML = "...";

				ajax("http://localhost:3000", function(returnNum) {
					span[i].innerHTML = returnNum;
					li[i].style.backgroundColor = "gray";
					if (that.clicked == 0) totalNum++;
					if (totalNum == li.length) {
						info.style.backgroundColor = "rgba(48, 63, 159, 1)";
						if (sign == 1) {
							info.onclick();
							sign = 0;
						}
					}
				}, function() {});
			};
		}(i);
	}
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

//for ajax , get online
var XMLHttp = { 
    _objPool: [], 

    _getInstance: function () { 
        for (var i = 0; i < this._objPool.length; i ++) { 
            if (this._objPool[i].readyState == 0 || this._objPool[i].readyState == 4) { 
                return this._objPool[i]; 
            } 
        } 

        // IE5中不支持push方法 
        this._objPool[this._objPool.length] = this._createObj(); 

        return this._objPool[this._objPool.length - 1]; 
    }, 

    _createObj: function () 
    {
    	var objXMLHttp;
        if (window.XMLHttpRequest) { 
            objXMLHttp = new XMLHttpRequest(); 
        } else { 
            var MSXML = ['MSXML2.XMLHTTP.5.0', 'MSXML2.XMLHTTP.4.0', 'MSXML2.XMLHTTP.3.0', 'MSXML2.XMLHTTP', 'Microsoft.XMLHTTP']; 
            for(var n = 0; n < MSXML.length; n ++) { 
                try { 
                    objXMLHttp = new ActiveXObject(MSXML[n]); 
                    break; 
                } catch(e) {} 
            }
         }

        // mozilla某些版本没有readyState属性 
        if (objXMLHttp.readyState == null) 
        { 
            objXMLHttp.readyState = 0; 

            objXMLHttp.addEventListener("load", function () 
                { 
                    objXMLHttp.readyState = 4; 

                    if (typeof objXMLHttp.onreadystatechange == "function") 
                    { 
                        objXMLHttp.onreadystatechange(); 
                    } 
                },  false); 
        } 

        return objXMLHttp; 
    }, 

    // 发送请求(方法[post,get], 地址, 数据, 回调函数) 
    sendReq: function (method, url, data, callback) 
    { 
        var objXMLHttp = this._getInstance(); 

        with(objXMLHttp) 
        { 
            try 
            { 
                // 加随机数防止缓存 
                if (url.indexOf("?") > 0) { 
                    url += "&randnum=" + Math.random(); 
                } 
                else { 
                    url += "?randnum=" + Math.random(); 
                } 

                open(method, url, true); 

                // 设定请求编码方式 
                setRequestHeader('Content-Type', 'application/x-www-form-urlencoded; charset=UTF-8'); 
                send(data); 
                onreadystatechange = function () 
                { 
                    if (objXMLHttp.readyState == 4 && (objXMLHttp.status == 200 || objXMLHttp.status == 304)) 
                    { 

                        callback(objXMLHttp.responseText); 
                    } 
                } 
            }
            catch(e) 
            {
                alert(e); 
            }
        }
    }
};

function ajax(url, successfunction, failfunction) {
	XMLHttp.sendReq('GET', url, '', successfunction);
}