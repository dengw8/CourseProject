window.onload=function(){
    (function() {
        $("#reset").click(function(){
            $(".content").val("");
            $(".validate").text("");
        });

    $("#form").submit(function(e) {
        var postdata = {username:null,student_id:null,phone:null,email:null};
        postdata.username = $("#username").val();
        postdata.student_id = $("#student_id").val();
        postdata.phone = $("#phone").val();
        postdata.email = $("#email").val();
        $(".content").focus(function() {
            $(this).next().text("");
        });
        var badFormat = validateBadFormat(postdata);
        if(!badFormat) {
            validateDuplicate(postdata);
        } else {
            e.preventDefault();
        }
    });

    function validateDuplicate(postdata) {
        var duplicateText = {username:"用户名重复",student_id:"学号重复",phone:"电话号码重复",email:"邮箱重复"};
        $.post("/submit",postdata,function(validate,status ){
            var duplicate = validate[0].duplicate;
            duplicate.forEach(function(e) {
                $("#"+e+"Validate").text(duplicateText[e]);
            });
            if(validate[0].duplicate.length==0 && validate[1].badFormat.length == 0) {
                location.href = "/?username="+postdata["username"];
            }
        });
    }

    function validateBadFormat(postdata) {
        var badFormat = false;
        var badFormatText = {username:"用户名格式错误",student_id:"学号格式错误",phone:"电话号码格式错误",email:"邮箱格式错误"};
        var pattern = {username:/^[a-zA-Z]([a-zA-Z0-9_]){5,17}$/,student_id:/^[1-9]\d{7}$/,phone:/^[1-9]\d{10}$/,email:/^[0-9a-zA-Z_\-]+@(([0-9a-zA-Z_\-])+\.)+([a-zA-Z]){2,4}$/};
        var attr=["username","student_id","phone","email"];
        attr.forEach(function(e){
            if(!pattern[e].test(postdata[e])){
            $("#"+e+"Validate").text(badFormatText[e]);
            badFormat = true;
            }
        });
        return badFormat;
    }})();
};