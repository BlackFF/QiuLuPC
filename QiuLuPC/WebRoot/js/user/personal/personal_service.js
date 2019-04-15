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
	getService();
	
	
	$.ajax({ url : "../../User/isLogin",//请求地址 
		dataType : "json",//数据格式  
		type : "post",//请求方式 
		async : false,//是否异步请求 
		success : function(data){
			console.log(data)
			var tital=$('.list').empty();
			var xs='';
			xs+='<li>欢迎光临秋露商城</li>';
			if(data.status){
				xs+='<li><a href="../../user/personal/personal_information.html" >'+data.object.username+'</a></li>'
				   +'<li><a href="javascript:void(0)" onclick="logout()" >退出登录</a></li>';
			}
			else{
				xs+='<li><a href="../../login.html" >请登录</a></li>';
			}
			xs+='<li><a href="../../register.html" >免费注册</a></li>'
			+'<li><a href="personal_indent.html" >我的订单</a></li>'
			+'<li><a href="###" >服务中心</a></li>'
			+'<li><a href="../cart.html" ><!--<img src="images/shoppingcart.png">-->购物车</a></li>';
			
			$(tital).html(xs);
			xs='';
			
		},
	})

	//客服服务
	callCustomerService();
});

function getService(){
	$.ajax({
		url:"../../order/queryAbandonOrder",
		success:function(data){
			if (data.status) {
				$("#serviceBox").empty();
				var html = '<table width="825" border="0" id="serviceBox">'+
								'<tr class="service_title">'+
									'<td width="300" height="45" align="center" valign="middle">订单编号</td>'+
									'<td width="230" align="center" valign="middle">订单商品</td>'+
									'<td width="300" align="center" valign="middle">下单时间</td>'+
								'</tr>';
				if (data.object.length==0) {
					html += '<tr class="c1_noinfor"><td colspan="7" align="center" valign="middle">暂无此类商品，请去挑选商品：<a href="../../index.html">首页</a></td></tr>';
				} else{
				
					for (var i = 0; i< data.object.length;i ++) {
						html += '<tr><td align="center" valign="middle">'+
								data.object[i].orderId+
								'</td><td align="center" valign="middle" class="apply_for"><br>';
						for (var j=0 ; j<data.object[i].list.length;j++) {
							var image=JSON.parse(data.object[i].list[j].id.image)
							html += '<a href="../../goods_details.html">';
							for (var int = 0; int < image.length; int++) {
								
								html +='<img src="'+images_path80+''+image[0]+'"  style="width:30px; height:30px"></a>';
							}	
						}
						html += '</td><td align="center" valign="middle">'+
								getLocalTime(data.object[i].createTime)+
								'</td></tr>';
					}
				}
				
				html += '</table>';				
				$("#serviceBox").append(html);
			}else{
				new $.flavr({
				    content      : data.message,
				    closeOverlay : true,
				    closeEsc     : true
				});
			}
		},
		error:function(){
			new $.flavr({
			    content      : '服务器异常',
			    closeOverlay : true,
			    closeEsc     : true
			});
		}
	});
}
