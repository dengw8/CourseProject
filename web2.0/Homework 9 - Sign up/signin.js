var http = require("http");
var url = require("url");
var path = require("path");
var fs = require("fs");
var querystring = require("querystring");
var util = require("util");

http.createServer(function(request,response) {
		RegisterUser(request,response);
		showPage(request,response);
}).listen(8000);

function showPage(request,response) {
	var file = getRequestFile(request);
	console.log(file);
	var username = getUsername(request);
	if(file=="html/signup.html"||(file==""&&username==undefined)){
		loadFile(response,"html/signup.html");
	}
	else if(file==""||file=="html/details.html"){
		if(username){
			queryName = username;
			showDetailsPage(response,username);
		}
		else {
			showSignUpPage(response);
		}
	}
	else if (file=="user.json") {
		loadFile(response,file,username);
		queryName = undefined;
	}
	else if(file!="submit"){
		fs.exists(file,function(exists){
			if(!exists){
				showErrorPage(response);
			}
			else {
				loadFile(response,file);
			}
		});
	}
}

function getRequestFile(request) {
	var pathname = url.parse(request.url).pathname;
	return pathname.substr(1);
}

function getUsername(request){
	var query = url.parse(request.url,true).query;
	if(query){
		return query.username;
	}
}

var contentType = {
	"html":"text/html",
	"js":"text/javascript",
	"css":"text/css",
	"ico":"image/icon",
	"":"application/octet-stream",
	"json":"application/json",
};

function loadFile(response,file,name){
	var encode = {encoding:"binary"};
	if(file=="user.json"){
		encode.encoding = "utf-8";
	}
	fs.readFile(file,encode,function(err,data){
		if(err){
			response.writeHead(404,{"Content-Type":"text/html"});
			console.log(err.toString());
		} else {
			type = contentType[path.extname(file).substr(1)];
			response.writeHead(200,{"Content-Type":type});
			if(file=="user.json" && name){
				response.write(parserUserData(data,name));
			}
			else {
				response.write(data,"binary");
			}

		}
		response.end();
	});
}

function showSignUpPage(response,file){
	response.writeHead(302,{"Location":"/"});
	response.end();
}

function parserUserData(user,username){
	var data = JSON.parse(user);
	var index;
	data.every(function(e,i){
		if(e.username==username){
			index = i;
			return false;
		}
		return true;
	});
	if(index!=undefined){
		return JSON.stringify(data[index]);
	}
}

function showDetailsPage(response,username){
	fs.readFile("user.json",{encoding:"utf-8"},function(err,user){
		var data = parserUserData(user,username);
		if(data!=undefined){
			loadFile(response,"html/details.html");
		}
		else {
			showSignUpPage(response,"html/signup.html");
		}
	});
}

function RegisterUser(request,response) {
	var post="";
	request.on("data",function(chunk) {
		post += chunk;
	});
	request.on("end",function() {
		if(post){
			saveUserData(response,post);
		}
	});
}

function saveUserData(response,post) {
	post = querystring.parse(post);
	fs.readFile("user.json",{encoding:"utf-8"},function(err,data) {
		var userArr = JSON.parse(data);
		var validate = validateInfo(response,post,userArr);
		if(validate[0].duplicate.length||validate[1].badFormat.length){

		}
		else {
			userArr.push(post);
			var buffer = new Buffer(JSON.stringify(userArr));
			fs.writeFile("user.json",buffer,{encoding:"utf-8"},function(err) {
				if(err){
					response.writeHead(404,{"Content-Type":"text/html"});
					console.log(err.toString());
				}
			});
		}
		response.writeHead(200,{"Content-Type":"application/json"});
		response.write(JSON.stringify(validate));
		response.end();
	});
}

function validateInfo(response,post,userArr) {
	var validate = [{"duplicate":[]},{"badFormat":[]}];
	var duplicate = validate[0].duplicate;
	var badFormat = validate[1].badFormat;
	var attr = ["username","student_id","phone","email"];
	attr.forEach(function(e) {
		if(isBadFormat(e,post[e])){
			badFormat.push(e);
		}
		else if(isDuplicate(e,post[e],userArr)){
			duplicate.push(e);
		}
	});
	return validate;
}

function isDuplicate(attrName,attr,userArr){
	var dup = false;
	userArr.every(function(e){
		if(e[attrName]==attr){
			dup = true;
			return false;
		}
		return true;
	});
	return dup;
}

function isBadFormat(attrName,attr) {
	var pattern = {username:/^[a-zA-Z]([a-zA-Z0-9_]){5,17}$/,student_id:/^[1-9]\d{7}$/,phone:/^[1-9]\d{10}$/,email:/^[0-9a-zA-Z_\-]+@(([a-zA-Z_\-])+\.)+([a-zA-Z]){2,4}$/};
	if(pattern[attrName].test(attr)){
		return false;
	}
	return true;
}

function showErrorPage(response) {
	response.writeHead(404,{"Content-Type":"text/html"});
	response.end("404 ERROR");
}

console.log("Server is running at localhost:8000");