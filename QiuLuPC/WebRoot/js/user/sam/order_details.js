$(document).ready(function(){
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
	
	var order ;
	var express;
	
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
	
	var orderid = getSessionStrongParam("orderid");
	getOrderDetail(orderid.id);

function getOrderDetail(orderId){
	$.ajax({
		url:"../../order/findOrderResultById",
		data:{"orderId":orderId},
		success:function(data){
			if (data.status) {
				order = data.object.order;
				express = JSON.parse(order.express);
				queryExperss();
				$("#status0").empty();
				var html = ''
				switch (data.object.status){
					case 0:
						html += '<h4>订单详情</h4>'+
									'<div class="submit_order_top">'+
										'<ul class="submit_title">'+
											'<li class="f_left">状态：<span class="color_red">订单提交</span></li>'+
											'<li class="f_left">订单编号：'+data.object.id+'</li>'+
											'<li class="f_left">下单时间：'+getLocalTime(data.object.CreateTime)+'</li>'+
											'<a href="javascript:void(0);" onclick="concleOrder('+
											data.object.id+
											',2)"><li class="f_right cannel">取消订单</li></a>'+
											'<a href="javascript:void(0);" onclick="gotoPay('+
											data.object.id+','+data.object.order.total+
											')"><li class="f_right pay_now">立即支付</li></a>'+
										'</ul>'+
										'<ul class="submit_plan">'+
											'<li>'+
												'<i class="submit_plan_s"></i>'+
												'<p>提交订单</p>'+
												'<span>'+getLocalTime(data.object.CreateTime)+'</span>'+
											'</li>'+
											'<li><i class="submit_plan_n"></i><p>配货中</p><span></span></li>'+
											'<li><i class="submit_plan_n"></i><p>已发货</p><span></span></li>'+
											'<li><i class="submit_plan_n"></i><p>确认收货</p><span></span></li>'+
											'<li><i class="submit_plan_n"></i><p>完成</p><span></span></li>'+
										'</ul>'+
										'<ul class="order_tracking"><li>尊敬的用户：该订单会为您保存一个小时（从下单之时算起），1小时之后如果还未付款，系统将自动取消订单，释放库存，请您尽快付款，如果您已经付款，请务必操作付款确认。如有疑问，请联系在线客服。</li><li class="order_padding">订单跟踪：<span class="order_color_br">'+
											getLocalTime(data.object.CreateTime)+
											'您的订单已确认</span></li></ul></div>';
						break;
					case 1:
						html += '<h4>订单详情</h4>'+
								'<div class="submit_order_top"><ul class="submit_title"><li class="f_left">状态：<span class="color_red">配货中</span></li><li class="f_left">订单编号：'+
									data.object.id+
									'</li><li class="f_left">下单时间：'+
									getLocalTime(data.object.CreateTime)+
									'</li></ul>'+
									'<ul class="submit_plan">'+
										'<li>'+
											'<i class="submit_plan_s"></i>'+
											'<p>提交订单</p>'+
											'<span>'+getLocalTime(data.object.CreateTime)+'</span>'+
										'</li>'+
										'<li><i class="submit_plan_s"></i><p>配货中</p><span>2017.12.11 13.11.11（假）</span></li>'+
										'<li><i class="submit_plan_n"></i><p>已发货</p><span></span></li>'+
										'<li><i class="submit_plan_n"></i><p>确认收货</p><span></span></li>'+
										'<li><i class="submit_plan_n"></i><p>完成</p><span></span></li>'+
									'</ul>'+
									'<ul class="order_tracking">'+
										'<li class="order_padding">'+
										'<p>订单跟踪：'+getLocalTime(data.object.CreateTime)+'您的订单已经提交，等待系统确认</p>'+
										'<p class="order_color">2017-03-11 09:11:12 您的订单已确认</p>'+
									'</li></ul></div>';	
						break;
					case 2:
						html += '<h4>订单详情</h4>'+
								'<div class="submit_order_top"><ul class="submit_title"><li class="f_left">状态：<span class="color_red">已发货</span></li><li class="f_left">订单编号：'+
								data.object.id+
								'</li><li class="f_left">下单时间：'+
								getLocalTime(data.object.CreateTime)+
									'</li></ul>'+
									'<ul class="submit_plan">'+
										'<li>'+
											'<i class="submit_plan_s"></i>'+
											'<p>提交订单</p>'+
											'<span>'+getLocalTime(data.object.CreateTime)+'</span>'+
										'</li>'+
										'<li><i class="submit_plan_s"></i><p>配货中</p><span>2017.12.11 13.11.11（假）</span></li>'+
										'<li><i class="submit_plan_s"></i><p>已发货</p><span>2017.12.11 13.11.11（假</span></li>'+
										'<li><i class="submit_plan_n"></i><p>确认收货</p><span></span></li>'+
										'<li><i class="submit_plan_n"></i><p>完成</p><span></span></li>'+
									'</ul>'+
									'<ul class="order_tracking">'+
										'<li>尊敬的用户：该订单已发货，请耐心等待并查看页面下方物流信息了解商品物流情况。</li>'+
										'<li class="order_padding">'+
										'<p>订单跟踪：'+getLocalTime(data.object.CreateTime)+'您的订单已经提交，等待系统确认</p>'+
										'<p class="order_padding_left">2017-03-11 09:11:12 您的订单已确认 ； 快递公司：'+express.k+' 快递单号：'+order.courierNumber+'</p>'
										;
									'</li></ul></div>';
						break;
					case 3:
						html += '<h4>订单详情</h4>'+
								'<div class="submit_order_top"><ul class="submit_title"><li class="f_left">状态：<span class="color_red">已签收</span></li><li class="f_left">订单编号：'+
								data.object.id+
								'</li><li class="f_left">下单时间：'+
								getLocalTime(data.object.CreateTime)+
									'</li></ul>'+
									'<ul class="submit_plan">'+
										'<li>'+
											'<i class="submit_plan_s"></i>'+
											'<p>提交订单</p>'+
											'<span>'+getLocalTime(data.object.CreateTime)+'</span>'+
										'</li>'+
										'<li><i class="submit_plan_s"></i><p>配货中</p><span>2017.12.11 13.11.11（假）</span></li>'+
										'<li><i class="submit_plan_s"></i><p>已发货</p><span>2017.12.11 13.11.11（假</span></li>'+
										'<li><i class="submit_plan_s"></i><p>确认收货</p><span></span></li>'+
										'<li><i class="submit_plan_n"></i><p>完成</p><span></span></li>'+
									'</ul>'+
									'<ul class="order_tracking">'+
										'<li>尊敬的用户：该订单已发货，请耐心等待并查看页面下方物流信息了解商品物流情况。</li>'+
										'<li class="order_padding">'+
										'<p>订单跟踪：'+getLocalTime(data.object.CreateTime)+'您的订单已经提交，等待系统确认</p>'+
										'<p class="order_padding_left">2017-03-11 09:11:12 您的订单已确认； 快递公司：'+express.k+' 快递单号：'+order.courierNumber+'</p>';
									'</li></ul></div>';
						break;	
				}
				$("#status0").append(html);	
				
				$(".payment").empty();
				html = '';
				html += '<h4>支付信息</h4><div class="payment_menu"><ul class="payment_menu_top"><li><dd>收货人信息</dd><dt><span>'+
						data.object.order.receiver+
						'，</span><span>'+
						data.object.order.phone+
						'，</span><span>'+
						data.object.order.receiverAddr+
						'</span></dt></li><li><dd>支付及配送信息</dd><dt><p>支付方式：在线支付</p><p>配送方式：'+
						JSON.parse(data.object.order.express).k+
						'</p><p>运费：￥'+
						data.object.order.postage+
						'</p></dt></li></ul>';
				html += '<div class="payment_table"><table width="930" border="0" cellspacing="0" cellpadding="0"><tr><td width="327" height="40" align="center">商品</td><td width="120" align="center">状态</td><td width="114" align="center">单价</td><td width="73" align="center">数量</td><td width="186" align="center">小计</td></tr>';
				var tolPrice = 0;
				console.log(data);
				for (var i = 0; i < data.object.orderResult.length ; i ++ ) {
					var image=JSON.parse(data.object.orderResult[i].id.image);
					html += '<tr class="payment_tr">'+
								'<td align="left" class="payment_img">';
									
									for (var int = 0; int < 1; int++) {
										html += '<a href="../../goods_details.html?id='+data.object.orderResult[i].id.skuId+'"><img src="'+images_path80+''+image[0]+'"></a>';
									}
									
									html += '<a href="../../goods_details.html?id='+data.object.orderResult[i].id.skuId+'">'+
									'<span>'+
									data.object.orderResult[i].id.sellPoint+
									'</span></a></td><td align="center">';
					switch (data.object.orderResult[i].id.status){
						case 0:
							html += '待付款';
							break;
						case 1:
							html += '待发货';
							break;
						case 2:
							html += '送货中';
							break;
						case 3:
							html += '已收货';
							break;	
					}
					tolPrice += parseFloat(data.object.orderResult[i].id.price)*parseInt(data.object.orderResult[i].id.number);
					html += '</td><td align="center">￥'+
							data.object.orderResult[i].id.price+
							'</td><td align="center">'+
							data.object.orderResult[i].id.number+
							'</td><td align="center">￥'+
							parseFloat(data.object.orderResult[i].id.price)*parseInt(data.object.orderResult[i].id.number)+
							'</td></tr>';
				}
				html += '</table></div>';
				html += '<div class="payment_bottom"><p>商品金额：<span>￥'+
						tolPrice+
						'</span></p><div>应付金额：<span>￥'+
						data.object.orderResult[0].id.total+
						'</span></div></div></div></div>';
				$(".payment").append(html);
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
};

function queryExperss(){
	$.ajax({
		type : 'get'
		,url : '/QiuLuPC/express/queryExpress?express='+express.v+'&courierNumber='+order.courierNumber
		,async : true
		,success : function(data){
			var ex = JSON.parse(data.object).data;
			
			var $li = $('.order_padding');
			var temp_html = '';
			for(var i = 0, len = ex.length; i < len ; i ++){
				temp_html += '<p class="order_padding_left"><span>'+ex[len-i-1].time+'</span>&nbsp;'+ex[len-i-1].context+'</p>';
			}
			$li.append(temp_html);
		}
	});
}



});


 


