window.onload = function() {
	var tables = getAllTables();
	makeAllTablesSortable(tables);
};

function getAllTables() {
	tables = $("table");
	return tables;
}

function makeAllTablesSortable(tables) {
	for (var i = 0; i < tables.length; i++) {
		for (var j = 0; j < tables[i].rows[0].cells.length; j++) {
			$(tables[i].rows[0].cells[j]).click(isInitial);
			$(tables[i].rows[0].cells[j]).click(set_sign);
			$(tables[i].rows[0].cells[j]).click(mySort);
		}
	}
}

function mySort() {
	var sign = set_sign(), atable = new Array(), col = this.cellIndex, tbody = this.parentNode.parentNode.nextSibling.nextSibling;
	for (var i = 0; i < tbody.rows.length; i++) {
		atable[i] = new Array();
		for (var j = 0; j < tbody.rows[i].cells.length; j++) {
			atable[i][j] = tbody.rows[i].cells[j].innerHTML;}
	} atable.sort(function (a, b) {
		return (a[col] == b[col]) ? 0 : ((a[col] > b[col]) ? sign : -1 * sign);});
	for (var i = 0; i < tbody.rows.length; i++) {
		tbody.rows[i].innerHTML = "<td>" + atable[i].join("</td><td>") + "</td>";}
}

function isInitial() {
	for (var i = 0; i < this.parentNode.cells.length; i++) {
		if (this.parentNode.cells[i] != this) {
			this.parentNode.cells[i].className = "";
		}
	}
}

function set_sign() {
	if (this.className == "ascend_style") {
		this.className = "descend_style";
		return -1;
	} else {
		this.className = "ascend_style";
		return 1;
	}
}