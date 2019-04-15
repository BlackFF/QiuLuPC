$(function(){
	$('#name').on('blur',checkTelephone).on('focus',function(){
		$('#username').empty();
	}).focus();
	$('#pwd').on('blur',checkPwd).on('focus',function(){
		$('#password').empty();
	});
	$('#checkPwd').on('blur',checFinalPwd).on('focus',function(){
		$('#chPwd').empty();
	});
	$('#veryCode').on('blur',checkCode).on('focus',function(){
		$('#code_msg').empty();
	});
	$(function() {
	    	$("#regProtA").click(function() {
	    		$(".layer_box").show();
	    	});
	    	$(".layer_close").click(function() {
	    		$(".layer_box").hide();
	    	});
	    	$(".layer_btn").click(function() {
	    		$("#checkBos").attr("checked", true);
	    		$(".layer_box").hide();
	    	});
	    });
	function checkTelephone(){
		var telephone=$('#name').val();
		if(telephone==''){
			$('#username').empty().append("手机号为空")
			return false;
		}
		var falg;
		var reg=/^1[34578]\d{9}$/;
		if(reg.test(telephone)){
			$.ajax({
				type:"POST",
				url:'User/findTelephone',
				data:{'telephone':telephone},
				async:false,
				dataType:'JSON',
				success:function(result){
					if(result.status==true){
						$('#username').empty();
						falg=true;
						return true;
					}else{
						console.log(result.message);
						$('#username').empty().append(result.message);
						falg=false;
						return false;
					} 
				},
			});
			
		}
		else{
			$('#username').empty().append("手机号格式错误")
			return false;
		}
		return falg;
		
	}
	
	function checkPwd(){
		var pwd=$('#pwd').val();
		if(pwd==''){
			$('#password').empty().append("密码为空");
			return false;
		}
		var reg=/^\w{3,10}$/;
		if(reg.test(pwd)){
			$('#password').empty();
			return true;
		}
		$('#password').empty().append("3~10个字符");
		return false;
	}
	
	function checFinalPwd(){
		var finalPwd=$('#checkPwd').val();
		var pwd=$('#pwd').val();
		if(finalPwd==''){
			$('#chPwd').empty().append("确认密码空")
			return false;
		}
		var reg=/^\w{3,10}$/;
		if(reg.test(finalPwd)){
			if(finalPwd !=pwd){
				$('#chPwd').empty().append("与原密码不一致！")	
				return false;
			}
			$('#chPwd').empty();
			return true;
		}
		$('#chPwd').empty().append("3~10个字符");
		return false;
	}
	
	function checkCode(){
		var mess;
		var code=$("#veryCode").val();
		if(code==''){
			$('#code_msg').empty().append("不能为空");
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
					if(result.status==true){
						$('#code_msg').empty();
						mes=true;
						return true;
					}
					var field=result.status
					if(field==false){
						console.log(result.message);
						$('#code_msg').empty().append(result.message);
						mes=false;
						return false;
					}
				}
				});

				return mes;
			
		}
		
	
	$('.user_submit').click(function(){
		
		if(	!$('#checkBos').is(':checked')){
			 new $.flavr({
				    content     : '请仔细阅读协议后选中',
				    
				});
		}
		
		else{
	    var Tcode=$('#teleCode').val();
		var telephone=$('#name').val();
		var password=$('#pwd').val();
		var ckpwd=$('#checkPwd').val();
		
		/*var code=$('#veryCode').val();*/

		//var namepass=checkRegName();
		var telephonepass=checkTelephone();
		var pwdpass=checkPwd();
		var finalpass=checFinalPwd();
		var codepass=checkCode();
		var Telecode=verifyMessageCode(Tcode);
			if( !telephonepass ||!pwdpass || !finalpass||!codepass||!Telecode){
				return false;
			}
		/*var url="User/register";
		var data={telephone:telephone,password:str_md5(password)};*/
		$.ajax({
			type:"POST",
			url:'User/register',
			data:{'telephone':telephone,'password':str_md5(password)},
			async:false,
			dataType:'JSON',
			success:function(result){
				if(result.status==true){
					//console.log(result.data);
					//setCookie(userName,name);
					
					window.location.href='login.html';
				}else{
					$('#username').empty().append(result.message);
					console.log(result.message);
					return false;
				}
			},
		});
		
	}	
	});
	
})


	function messageCode(){
		var tele=checkTele();
		if(tele){
		var telep=$('#name').val();
		var url="message/send";
		var data={mobile:telep};
		$.post(url,data,function(result){
			console.log(result);
			if(result.status==true){
				$('#sendCode').empty().append("已发送");
				return true;
			}
		});
		}
	}


function checkTele(){
	var telephone=$('#name').val();
	if(telephone==''){
		$('#username').empty().append("手机号为空")
		return false;
	}
	var falg;
	var reg=/^1[34578]\d{9}$/;
	if(reg.test(telephone)){
		$.ajax({
			type:"POST",
			url:'User/findTelephone',
			data:{'telephone':telephone},
			async:false,
			dataType:'JSON',
			success:function(result){
				if(result.status==true){
			
					falg=true;
					return true;
				}else{
				
				
					falg=false;
					return false;
				} 
			},
		});
		
	}
	else{
		$('#username').empty().append("手机号格式错误")
		return false;
	}
	return falg;
	
}

function verifyMessageCode(messageCode){
	var falg;
	$.ajax({
		type:"POST",
		url:'message/verifyMessageCode',
		data:{"messageCode":messageCode},
		async:false,
		dataType:'JSON',
		success:function(result){
			if(result.status){
				falg=true;
			}
			else{
				$('#checkTeleCode').empty().append(result.message);
				falg=false;
			}
          },
	})
	return falg;
}