var EMPTY_ROW = 300;
var EMPTY_COL = 300;
var isbegin = false;
var step = 0;

window.onload = function() {
	init();
	$("#restart").click(restart);
};

function init() {
	var count = 1;
	for (var row = 0; row < 4; row++)
		for (var col = 0; col < 4; col++) {
			if (count < 16) {
				var block = document.createElement("div");
				setAttr(block, row, col, count);
				$("#container").append(block);
			} count++;
		}
}

function setAttr(obj, row, col, count) {
	obj.setAttribute('class', 'block');
	obj.value = count;
    obj.id = "square_" + row + "_" + col;
	obj.style.top = row * 100 + "px";
	obj.style.left = col * 100 + "px";
	obj.style.backgroundPosition = col * (-100) + "px " + row * (-100) + "px";
}

function play() {
	step -= $('#hard').val();
	$("#steps").val("0");
	updatetime();
	var blocks = document.getElementById('container').getElementsByTagName('div');
	for (var i = 0; i < blocks.length; i++) {
		if (isbegin === true) {
			blocks[i].onclick = click;
		}
	}
}

function click() {
	if (isbegin === true)
		move(this);
	if (isfinish() === true && isbegin === true) {
		isbegin = false;
		alert("Congratulations!\nYou Win.");
	}
}

function move(puzzle) {
	var posTop = parseInt(window.getComputedStyle(puzzle).top);
	var posLeft = parseInt(window.getComputedStyle(puzzle).left);
	if (check(posTop, posLeft)) {
		puzzle.style.left = EMPTY_COL + "px";
		puzzle.style.top = EMPTY_ROW + "px";
		EMPTY_COL = posLeft;
		EMPTY_ROW = posTop;
		document.getElementById("steps").innerHTML = ++step;
	}
}

function check(topPos, leftPos) {
	return ((Math.abs(EMPTY_COL - leftPos) == 100 && EMPTY_ROW == topPos) || (Math.abs(EMPTY_ROW - topPos) == 100 && EMPTY_COL == leftPos));
}

function getID(row, col) {
	return document.getElementById("square_" + row + "_" + col);
}

function restart(event) {
	step = 0; isbegin = true;
	$("#times").val("00:00");
	for (var time = 0; time < $("#hard").val(); time++) {
		var blocks = new Array;
		for (var count = 0; count < 15; count++) {
			var top = parseInt(window.getComputedStyle(getID(Math.floor(count / 4), count % 4)).top);
			var left = parseInt(window.getComputedStyle(getID(Math.floor(count / 4), count % 4)).left);
			if (check(top, left)) blocks.push(getID(Math.floor(count / 4), count % 4));
		} move(blocks[parseInt((Math.random() * blocks.length))]);
	} play();
}

function updatetime() {
	var usetime = 0;
	clearInterval(this.timer);
	if (isbegin === true) {
		this.timer = setInterval(function() {
			if (isbegin === true) usetime++;
			document.getElementById("times").innerHTML = ('0' + parseInt(usetime / 60)).slice(-2) + ':' + ('0' + usetime % 60).slice(-2);
		}, 1000);
	}
}

function isfinish() {
	var blocks = document.getElementById('container').getElementsByTagName('div'), sign = 1;
	for (var count = 0; count < 15; count++) {
		var top = parseInt(window.getComputedStyle(getID(Math.floor(count / 4), count % 4)).top) / 100;
		var left = parseInt(window.getComputedStyle(getID(Math.floor(count / 4), count % 4)).left) / 100;
		if (blocks[count].value != 4 * top + left + 1) sign = 0;
	}
	if (sign == 1) return true;
	return false;
}