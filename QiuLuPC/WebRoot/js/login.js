$(function(){
	$("#wrong").hide();
	$('#name').on('focus',function(){
		$("#wrong").hide();
		$('#message').empty();
	});
	$('#pwd').on('focus',function(){
		$("#wrong").hide();
		$('#message').empty();
	});
	$('#Code').on('focus',function(){
		$("#wrong").hide();
		$('#message').empty();
	});
});


function checkName(){
	
	var tel=$('#name').val();
	if(tel==''){
		$("#wrong").show();
		/*$('#mess').empty().append("请输入用户名");*/
		return false;
	}
	
	var reg=/^1[34578]\d{9}$/;
	if(reg.test(tel)){
		
		/*$('#mess').empty();*/
		return true;
	}
	//$("#wrong").show();
	/*$('#mess').empty().append("3~10字符");*/
	return false;
}
//检查密码
function checkPassword(){
	
	var password=$('#password').val();
	if(password==''){ 
		//$("#wrong").show();
		/*$('#mess').empty().append("密码不能为空");*/
		return false;
	}
	var reg=/^\w{3,10}$/;
	if(reg.test(password)){
		
		//$('#mess').empty();
		return true;
	}
	//$('#mess').empty().append("3~10字符串");
		return false;
}

function checkTel(){
	var name=$('#count').val();
	if(name==''){
		//$('#count_msg').empty().append("不能空");
		return false;
	}
	var reg=/^1[34578]\d{9}$/;
	if(reg.test(name)){
		
		//$('#count_msg').empty();
		return true;
	}
	//$('#count_msg').empty().append("3~10字符");
	return false;
}

function checkCode(){
	var mess;
	var code=$("#Code").val();
	if(code==''){
		$('#message').empty().append("验证码为空");
		mes=false;
		return false;
	}
	$.ajax({
		type:"POST",
		url:"User/verifyCode",
		data:{'code':code},
		async:false,
		dataType:'JSON',
		success:function(result){
			//console.log(result);
			if(result.status==true){
				$('#message').empty();
				mes=true;
				return true;
			}else{
				$('#message').empty().append(result.message);
				mes=false;
				return false;
			}
		}
		
	});
		return mes;
}
//点击登录按钮执行
function loginAction(){
	var name=$('#name').val();
	var password=$('#pwd').val();
	
	var codepass=checkCode();
	var namePass=checkName();
	var pwdPass=checkPassword();
	if(!namePass ||!pwdPass ||!codepass){
		if(!namePass ||!pwdPass){
			$("#wrong").show();
		}
		if(!codepass){
			$("#message").empty().append("验证码错误");
		}
		//$("#wrong").show();
		return  false;
	}
	$.ajax({
		url:'User/login',
		type:'post',
		data:{'telephone':name,'password':str_md5(password)},
		dataType:'JSON',
		success:function(result){
			
			
			if(result.status==true){
		
				console.log(result);
				
				var backUrl = request("backUrl");
				if (backUrl=='') {
					window.location.href='index.html';
				} else{
					location.href=backUrl;
				}
				//return ;
			}
			//登录失败
			var field=result.status;
		
			if(field==false){
				$("#wrong").show();
				return false;
				//$('#mess').empty().append(result.message);
			}
		}
	});
	
}
//响应回车事件
document.onkeydown = function(e) {  
var theEvent = e || window.event;  
var code = theEvent.keyCode || theEvent.which || theEvent.charCode;  
if (code == 13) {
	loginAction();
	 }  
};
window.onload = function(){  
    var userNameValue = getCookie("USER_Telephone");  
    document.getElementById("name").value = userNameValue;  
    var userPassValue = getCookie("Password");  
    document.getElementById("pwd").value = userPassValue;  
}  
 function setCook(){
	var tel=$('#name').val();
	var password=$('#pwd').val();
	
		setCookie("USER_Telephone",tel);
		setCookie("Password",password);
					
		
 }