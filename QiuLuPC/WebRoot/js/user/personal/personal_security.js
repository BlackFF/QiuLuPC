$(document).ready(function(){
	if (!isLogin()) {
    	new $.flavr({
		    content     : '您当前没有登录,是否跳转登陆页面？',
		    dialog      : 'confirm',
		    onConfirm   : function(){
		        location.href = path + "/login.html?backUrl="+encodeURIComponent(location.href);
		    },
		    onCancel    : function(){
		    }
		});
    	return false;
    }
	//初始化验证插件
	$(function () {
		Validator.language = 'zh';
	});
	
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
				
				
				var tel=data.object.telephone;
				var str2 = tel.substr(0,3)+"****"+tel.substr(8);
				$('#c7_password').html(str2);
				/*$('.c7_password').empty().append(data.object.username);*/
				
			   }
			},
	})

	
	//客服服务
	callCustomerService();
});
