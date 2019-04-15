var userId;
$(function(){
	$('#tele').on('blur',checkTelephone).on('focus',function(){
		$('.remind_first').empty();
	});
	 $('#Code').on('focus',function(){
		$('.remind_first').empty();
	});
	$('#password').on('focus',function(){
		$('#checkP').empty();
		$('#checkFP').empty();
	});
	$('#checkpassword').on('focus',function(){
		$('#checkP').empty();
		$('#checkFP').empty();
	});
	
	
	
	 //页眉
	$.ajax({ url : "User/isLogin",//请求地址 
		dataType : "json",//数据格式  
		type : "post",//请求方式 
		async : false,//是否异步请求 
		success : function(data){
			var tital=$('.list').empty();
			var xs='';
			xs+='<li>欢迎光临秋露商城</li>';
			if(data.status){
				xs+='<li><a href="user/personal/personal_information.html" >'+data.object.username+'</a></li>'
				   +'<li><a href="javascript:void(0)" onclick="logout()" >退出登录</a></li>';
			}
			else{
				xs+='<li><a href="login.html" >请登录</a></li>';
			}
			xs+='<li><a href="register.html" >免费注册</a></li>'
			+'<li><a href="user/personal/personal_indent.html" >我的订单</a></li>'
			+'<li><a href="###" >服务中心</a></li>'
			+'<li><a href="javascript:void(0)" onclick="location.href =\'/QiuLuPC/user/cart.html?backUrl='+encodeURIComponent(location.href)+'\';">购物车</a></li>';
			$(tital).html(xs);
			xs='';
			
		},
	})
})


function checkTelephone(){
	var tele=$('#tele').val();
	
	//var url="User/findTele";
	//var data={telephone:tele};
	if(tele==''){
		console.log(111);
		$('.remind_first').css('display','block');
		$('.remind_first').empty().append("手机号为空");
		return false;
	}
	var falg;
	var reg=/^1[34578]\d{9}$/;
	if(reg.test(tele)){
		$.ajax({
			type:"POST",
			url:'User/findTele',
			data:{'telephone':tele},
			async:false,
			dataType:'JSON',
			success:function(result){
				if(result.status==true){
					userId=result.object;
					falg=true;
					return true;
				}else{
					console.log(result.message);
					$('.remind_first').empty().append(result.message);
					falg=false;
					return false;
				} 
			},
		});
	}else{
		$('.remind_first').css('display','block');
		$('.remind_first').empty().append("手机格式错误");
		falg=false;
		return false;
	}
	
	return falg;
}
function checkPwd(){
	var pwd=$('#password').val();
	if(pwd==''){
		$('#checkP').empty().append("新密码为空");
		return false;
	}
	var reg=/^\w{6,16}$/;
	if(reg.test(pwd)){
		
		//$('#password').empty();
		return true;
	}
	$('#checkP').empty().append("6~16个字符");
	return false;
}

function checFinalPwd(){
	var finalPwd=$('#checkpassword').val();
	var pwd=$('#password').val();
	if(finalPwd==''){
		$('#checkFP').empty().append("确认密码空")
		return false;
	}
	var reg=/^\w{6,16}$/;
	if(reg.test(finalPwd)){
		if(finalPwd !=pwd){
			$('#checkFP').empty().append("与原密码不一致！")	
			return false;
		}
		//$('#chPwd').empty();
		return true;
	}
	$('#checkFP').empty().append("6~16个字符");
	return false;
}
function checkCode(){
	var mess;
	var code=$("#Code").val();
	if(code==''){
		$('.remind_first').empty().append("验证码不能为空");
		return false;
	}
		$.ajax({
			type:"POST",
			url:"User/verifyCode",
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
					$('.remind_first').empty().append(result.message);
					mes=false;
					return false;
				}
			}
			});
			return mes;
	}
function sub(){
	var telephonePass=checkTelephone();
	var codePass=checkCode();
	if(!telephonePass||!codePass){
		$('.remind_first').css('display','block');
		return false;
		
	}else{
		var tele=$('#tele').val();
		$('#thisTele').append(tele);
		$('.nav_pwd_first').css('display','none');
		$('.nav_pwd_second').css('display','block');
		return true;
	}
	
}
function second(){
	//verifyMessgeCode();
	var codepass=verifyMessgeCode();
	if(!codepass){
		
		return false;
	}else{
		$('.nav_pwd_second').css('display','none');
		$('.nav_pwd_third').css('display','block');
		var tele=$('#tele').val();
		$('#telephone').html(tele);
		
	}
	
	
}

function third(){
	
	var checkPwdPass=checkPwd();
	var checFinalPwdPass=checFinalPwd();
	if(!checkPwdPass||!checFinalPwdPass){
		
		return false;
	}else{
		var pwd=$('#password').val();
		var url="User/updatePassword";
		var data={id:userId,password:str_md5(pwd)};
		$.post(url,data,function(result){
			if(result.status==true){
				$('.nav_pwd_third').css('display','none');
				
				$('.nav_pwd_fourth').css('display','block');
				var count = 5; 
				var countdown=setInterval(CountDown,1000)
				function CountDown() { 
					var li=$('.user_fourth').empty();
					var $ul=('页面将在<span >'+count+'秒</span>内跳转至登录首页');
					li.append($ul);
					if (count == 0) { 
						clearInterval(countdown); 
						window.location.href='index.html';
					} 
					count--; 
				}
				return true;
			}
		})
		
	}
	
}

function messageCode(){
	var tele=$('#tele').val();
	var url="message/send";
	var data={mobile:tele};
	$.post(url,data,function(result){
		console.log(result);
		if(result.status==true){
			$("#getCode").empty().append("已发送");
			var me=$("#checkMeg").empty();
			var html='';
			html+='<span></span>请输入您收到的6位手机验证码！';
			$(me).html(html);
			return true;
		}
	});
}

function verifyMessgeCode(){
	var mes;
	//var url="message/verifyMessageCode";
	var code=$('#mesCode').val();
	//var data={messageCode:code};
	$.ajax({
		type:"POST",
		url:"message/verifyMessageCode",
		async:false,
		data:{"messageCode":code},
		dataType:'JSON',
		success:function(result){
			console.log(result);
			if(result.status==true){
				mes=true
				return true;
			}else{
				var me=$("#checkMeg").empty();
				var html='';
				html+='<span></span>'+result.message+'';
				$(me).html(html);
				mes=false;
				return false;
			}
			}
		});
	return mes;
}

function getCode(i){
	$.ajax({
		type:"POST",
		url:"User/img",
		async:false,
		success:function(result){
			$("#imgCode").attr('src','User/img?x='+Math.random());
		}
	});
}
function getVCode(){
	$.ajax({
		type:"POST",
		url:"User/img",
		async:false,
		success:function(result){
			$("#imgCode").attr('src','User/img?x='+Math.random());
		}
	});
}




















