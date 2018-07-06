window.onload=function() {
    (function(){
    	var queryBody = (location.search.split("="))[1];
    	$.get("/user.json?username=" +queryBody, function(data) {
      		$("#username").html("用户名: " +data.username);
      		$("#student_id").html("学&nbsp;号: " +data.student_id);
      		$("#phone").html("电&nbsp;话: " +data.phone);
      		$("#email").html("邮&nbsp;箱: " +data.email);
    	});
  })();
};