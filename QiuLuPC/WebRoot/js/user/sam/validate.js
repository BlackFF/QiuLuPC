$(function(){
	$('#messCode').on('focus',function(){
		$('#codemessage').empty();
		$('#messageCodeType').empty();
	});
	$('#Code').on('focus',function(){
		$('#codemessage').empty();
		$('#messageCodeType').empty();
	});
	$('#password').on('blur',checkPwd).on('focus',function(){
		$('#firstPassword').empty();
		$('#secondPassword').empty();
	});
	$('#checkPassword').on('blur',checFinalPwd).on('focus',function(){
		$('#firstPassword').empty();
		$('#secondPassword').empty();
	});
	$('.main_submenu_two').hide();
	$('.main_submenu_three').hide();
	$.ajax({
		type:"POST",
		url:"../../User/findTelephoneByUserId",
		async:true,
		dataType:'JSON',
		success:function(result){
			if(result.status==true){
				//console.log(result);
				var tel=result.object;
				var str2 = tel.substr(0,3)+"****"+tel.substr(8);
				$('.va_tele').html(str2);
				window.telephone=tel;
				}
			}
	});
	 //页眉
	 $.ajax({ url : "../../User/isLogin",//请求地址 
			dataType : "json",//数据格式  
			type : "post",//请求方式 
			async : false,//是否异步请求 
			success : function(data){
				console.log(data)
				if(data.status){
					var tital=$('.list li:nth-of-type(2)').empty();
					var xs='';
					if(data.status){
						xs+='<li><a href="../../user/personal/personal_information.html" >'+data.object.username+'</a></li>'
						   +'<li><a href="javascript:void(0)" onclick="logout()" >退出登录</a></li>';
					}
					else{
						xs+='<li><a href="../../login.html" >请登录</a></li>';
					}
					$(tital).html(xs);
					xs='';
				   }
				},
		})

		//客服服务
		callCustomerService();
	});
	
//注销登录
function loggout(){
$.ajax({
url:"../../User/logout",
type:"post",
dataType:"json",
async:false,
success: function(data){
	if(data.status){
		var tital=$('.list li:nth-of-type(2)').empty();
		var xs='';
			xs+='<li><a href="../../login.html" >请登录</a></li>';
		
		$(tital).html(xs);
		xs='';
	   }
},
})

}


function oneSunmit(){
	var messageCodePass=verifyMessgeCode();
	var codePass=checkCode();
	if(!messageCodePass||!codePass){
		return false;
	}else{
		$('.main_submenu').hide();
		$('.main_submenu_two').show();
	}
}
function twoSubmit(){
	var checkPwdPass=checkPwd();
	var checFinalPwdPass=checFinalPwd();
	if(!checkPwdPass||!checFinalPwdPass){
		
		return false;
	}else{
		var pwd=$('#password').val();
		var url="../../User/updatePassword";
		var data={password:str_md5(pwd)};
		$.post(url,data,function(result){
			if(result.status==true){
				$('.main_submenu_two').hide();
				$('.main_submenu_three').show();
				loggout();
				
				return true;
			}else{
				return false;
			}
		})
		
	}
}
function checkPwd(){
	var pwd=$('#password').val();
	if(pwd==''){
		$('#firstPassword').empty().append("密码为空");
		return false;
	}
	var reg=/^\w{6,20}$/;
	if(reg.test(pwd)){
		
		$('#firstPassword').empty();
		return true;
	}
	$('#firstPassword').empty().append("请设置6~20个字符");
	return false;
}
function checFinalPwd(){
	var finalPwd=$('#checkPassword').val();
	var pwd=$('#password').val();
	if(finalPwd==''){
		$('#secondPassword').empty().append("确认密码空")
		return false;
	}
	var reg=/^\w{6,20}$/;
	if(reg.test(finalPwd)){
		if(finalPwd !=pwd){
			$('#secondPassword').empty().append("与原密码不一致！")	
			return false;
		}
		$('#chPwd').empty();
		return true;
	}
	$('#secondPassword').empty().append("6~20个字符");
	return false;
}

function twocheckCode(){
	var mess;
	var code=$("#twoCode").val();
	if(code==''){
		$('#code_msg').empty().append("不能为空");
		return false;
	}
		$.ajax({
			type:"POST",
			url:"../../User/verifyCode",
			data:{'code':code},
			async:false,
			dataType:'JSON',
			success:function(result){
				if(result.status==true){
					mes=true;
					return true;
				}
				var field=result.status
				if(field==false){
					console.log(result.message);
					getVCode();
					$('#code_msg').empty().append(result.message);
					mes=false;
					return false;
				}
			}
			});
			return mes;
	}
function checkCode(){
	var mess;
	var code=$("#Code").val();
	if(code==""){
		$('#codemessage').empty().append("不能为空");
		return false;
	}
		$.ajax({
			type:"POST",
			url:"../../User/verifyCode",
			data:{'code':code},
			async:false,
			dataType:'JSON',
			success:function(result){
				if(result.status==true){
					mes=true;
					return true;
				}
				var field=result.status
				if(field==false){
					console.log(result.message);
					getVCode();
					$('#codemessage').empty().append(result.message);
					mes=false;
					return false;
				}
			}
			});
			return mes;
	}
function getMessageCode(){
	var tele=window.telephone;
	console.log(tele);
	var url="../../message/send";
	var data={mobile:tele};
	$.post(url,data,function(result){
		console.log(result);
		if(result.status==true){
			$("#getPhCode").empty().append("已发送");
			return true;
		}
	});
}

function verifyMessgeCode(){
	var mes;
	var code=$('#messCode').val();
	var data={messageCode:code};
	if(code==""){
	$('#messageCodeType').empty().append("短信校验码为空");
	mes=false;
	}
	else{
	$.ajax({
		type:"POST",
		url:"../../message/verifyMessageCode",
		async:false,
		data:{"messageCode":code},
		dataType:'JSON',
		success:function(result){
			if(result.status==true){
				
				mes=true
				return true;
			}else{
				$('#messageCodeType').empty().append(result.message);
				mes=false;
				return false;
			}
			}
		});
	}
	return mes;
}
function getCode(i){
	$.ajax({
		type:"POST",
		url:"../../User/img",
		async:false,
		success:function(result){
			$("#imgCode").attr('src','../../User/img?x='+Math.random());
		}
	});
}
function getVCode(){
	$.ajax({
		type:"POST",
		url:"../../User/img",
		async:false,
		success:function(result){
			$("#imgCode").attr('src','../../User/img?x='+Math.random());
		}
	});
}