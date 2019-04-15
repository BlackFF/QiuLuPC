$(document).ready(function(){
	var orderSku;
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
	getOrdersCount();
	getOrders();
	
	
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
/*
 * 获取订单信息
 */
function getOrders(pageNo,status){
	$.ajax({
		type:"post",
		url:"../../order/findOrderByStatus",
		data:{"status":status,"pageNo":pageNo},
		success:function(data){
			$("#orderBox").empty();
			var html = '';
			html += '<table width="970" border="0">'+
						'<tr class="c2_bottom_title">'+
							'<td width="380" height="40" align="center" valign="middle">商品</td>'+
							'<td width="120" align="center" valign="middle">单价</td>'+
							'<td width="100" align="center" valign="middle">数量</td>'+
							'<td width="130" align="center" valign="middle">实付款</td>'+
							'<td width="120" align="center" valign="middle">订单状态</td>'+
							'<td width="120" align="center" valign="middle">操作</td>'+
						'</tr>'
			if (data.status) {
				if (data.object.list.length==0) {
					html += '<tr class="c1_noinfor">'+
								'<td colspan="7" align="center" valign="middle">'+
								'暂无订单，请去挑选商品：<a href="../../index.html">首页</a>'+
								'</td>'+
							'</tr>';	
				}else {
					orderSku = data.object.list;
					for (var i = 0; i < data.object.list.length; i++) {
						switch (data.object.list[i].listOr[0].id.status){
							case 0:
								html += '<tbody id="c2_immediate"><tr class="c2_bottom_time"><td height="40" colspan="6" align="left" valign="middle"><span>主订单号:'+
										data.object.list[i].orderId+
										'</span> <span>下单时间:'+
										getLocalTime(data.object.list[i].crTime)+
										'</span></td></tr><tr class="c2_bottom_obligation"><td colspan="3" align="left" valign="middle" class="c2_list">';
								for (var j = 0 ; j < data.object.list[i].listOr.length ; j++) {
								var image=JSON.parse(data.object.list[i].listOr[j].id.image)
				  		             	html += '<ol><li class="c2_bottom_img">'
												+'<a href="../../goods_details.html?id='+data.object.list[i].listOr[j].id.id+'">'
												for (var int = 0; int < 1; int++) {
													
													html += '<img src="'+images_path80+''+image[0]+'"></a>'
												
												}
												
												
									html += '<a href="../../goods_details.html?id='+data.object.list[i].listOr[j].id.id+'">'+
												'<span>'+
												data.object.list[i].listOr[j].id.sellPoint+
												'</span></a></li><li class="c2_bottom_money"><span>￥'+
												data.object.list[i].listOr[j].id.price+
												'</span></li><li class="c2_bottom_number"><span>'+
												data.object.list[i].listOr[j].id.number+
												'</span></li></ol>';
								}
								html += '</td>'+'<td align="center" valign="top" class="annual_payment"><span>￥'+
												data.object.list[i].listOr[0].id.total+'</span>';
								html += '<td align="center" valign="top" class="obligation"><span>待付款</span> '+
										'<a href="javascript:void(0)" onclick="orderDetail('+
										data.object.list[i].orderId+
										')"><p>订单详情</p></a></td>'+
										'<td align="center" valign="top" class="immediate">'+
										'<a herf="javascript:void(0)" onclick="gotoPay('+
										data.object.list[i].orderId+
										','+
										data.object.list[i].listOr[0].id.total+
										')"><span>立即付款</span></a><a href="javascript:void(0)" onclick="concleOrder('+
										data.object.list[i].orderId+
										',1)"><div>取消订单</div></a></td></tr><tr><td height="10" colspan="6" align="center" valign="middle" style="background: #ECECEC;"></td></tr></tbody>';
								break;
							case 1:
								html += '<tbody id="c2_immediate"><tr class="c2_bottom_time"><td height="40" colspan="6" align="left" valign="middle"><span>主订单号:'+
										data.object.list[i].orderId+
										'</span> <span>下单时间:'+
										getLocalTime(data.object.list[i].crTime)+
										'</span></td></tr><tr class="c2_bottom_obligation"><td colspan="3" align="left" valign="middle" class="c2_list">';
								for (var j = 0 ; j < data.object.list[i].listOr.length ; j++) {
									var image=JSON.parse(data.object.list[i].listOr[j].id.image)
									html += '<ol><li class="c2_bottom_img">'+
												'<a href="../../goods_details.html?id='+data.object.list[i].listOr[j].id.id+'">';
												for (var int = 0; int < 1; int++) {
													html += '<img src="'+images_path80+''+image[0]+'"></a>'
												}
												
											
											
												html +='<a href="../../goods_details.html?id='+data.object.list[i].listOr[j].id.id+'">'+
												'<span>'+
												data.object.list[i].listOr[j].id.sellPoint+
												'</span></a></li><li class="c2_bottom_money"><span>￥'+
												data.object.list[i].listOr[j].id.price+
												'</span></li><li class="c2_bottom_number"><span>'+
												data.object.list[i].listOr[j].id.number+
												'</span></li></ol>';
								}
								html += '</td>'+'<td align="center" valign="top" class="annual_payment"><span>￥'+
												data.object.list[i].listOr[0].id.total+'</span>';
								html += '<td align="center" valign="top" class="obligation"><span>待发货</span> '+
										'<a href="javascript:void(0)" onclick="orderDetail('+
										data.object.list[i].orderId+
										')"><p>订单详情</p></a></td>'+
										'<td align="center" valign="top" class="immediate"></a><a href="javascript:void(0)" onclick="concleOrder('+
										data.object.list[i].orderId+
										',1)"><div>取消订单</div></a></td></tr><tr><td height="10" colspan="6" align="center" valign="middle" style="background: #ECECEC;"></td></tr></tbody>';
								break;
							case 2:
								html += '<tbody id="c2_immediate"><tr class="c2_bottom_time"><td height="40" colspan="6" align="left" valign="middle"><span>主订单号:'+
										data.object.list[i].orderId+
										'</span> <span>下单时间:'+
										getLocalTime(data.object.list[i].crTime)+
										'</span></td></tr><tr class="c2_bottom_obligation"><td colspan="3" align="left" valign="middle" class="c2_list">';
						for (var j = 0 ; j < data.object.list[i].listOr.length ; j++) {
							var image=JSON.parse(data.object.list[i].listOr[j].id.image)
									html += '<ol><li class="c2_bottom_img">'+
											'<a href="../../goods_details.html?id='+data.object.list[i].listOr[j].id.id+'">';
											for (var int = 0; int < 1; int++) {
												html += '<img src="'+images_path80+''+image[0]+'"></a>'
																}
																							
																				
												html +='<a href="../../goods_details.html?id='+data.object.list[i].listOr[j].id.id+'">'+
												'<span>'+
												data.object.list[i].listOr[j].id.sellPoint+
												'</span></a></li><li class="c2_bottom_money"><span>￥'+
												data.object.list[i].listOr[j].id.price+
												'</span></li><li class="c2_bottom_number"><span>'+
												data.object.list[i].listOr[j].id.number+
												'</span></li></ol>';
								}
								html += '</td>'+'<td align="center" valign="top" class="annual_payment"><span>￥'+
												data.object.list[i].listOr[0].id.total+'</span>';
								html += '<td align="center" valign="top" class="obligation"><span>已发货</span> '+
										'<a href="javascript:void(0)" onclick="orderDetail('+
										data.object.list[i].orderId+
										')"><p>订单详情</p></a></td>'+
										'<td align="center" valign="top" class="immediate"></a><a href="javascript:void(0)" onclick="confirm('+
										data.object.list[i].orderId+
										')"><span>确认收货</span></a><a href="javascript:void(0)" onclick="salesReturn('+
										data.object.list[i].orderId+
										',1)"><p>退货</p></a></td></tr><tr><td height="10" colspan="6" align="center" valign="middle" style="background: #ECECEC;"></td></tr></tbody>';
								break;
							case 3:
								html += '<tbody id="c2_immediate"><tr class="c2_bottom_time"><td height="40" colspan="6" align="left" valign="middle"><span>主订单号:'+
										data.object.list[i].orderId+
										'</span> <span>下单时间:'+
										getLocalTime(data.object.list[i].crTime)+
										'</span></td></tr><tr class="c2_bottom_obligation"><td colspan="3" align="left" valign="middle" class="c2_list">';
								for (var j = 0 ; j < data.object.list[i].listOr.length ; j++) {
									var image=JSON.parse(data.object.list[i].listOr[j].id.image)
									html += '<ol><li class="c2_bottom_img">'+
												'<a href="../../goods_details.html?id='+data.object.list[i].listOr[j].id.id+'">';
												for (var int = 0; int < 1; int++) {
													html += '<img src="'+images_path80+''+image[0]+'"></a>'
																	}
																								
																					
													html +='<a href="../../goods_details.html?id='+data.object.list[i].listOr[j].id.id+'">'+
												'<span>'+
												data.object.list[i].listOr[j].id.sellPoint+
												'</span></a></li><li class="c2_bottom_money"><span>￥'+
												data.object.list[i].listOr[j].id.price+
												'</span></li><li class="c2_bottom_number"><span>'+
												data.object.list[i].listOr[j].id.number+
												'</span></li></ol>';
								}
								html += '</td>'+'<td align="center" valign="top" class="annual_payment"><span>￥'+
												data.object.list[i].listOr[0].id.total+'</span>';
								html += '<td align="center" valign="top" class="obligation"><span>已签收</span> '+
										'<a href="javascript:void(0)" onclick="orderDetail('+
										data.object.list[i].orderId+
										')"><p>订单详情</p></a></td>'+
										'<td align="center" valign="top" class="immediate">'+
										'<a href="javascript:void(0)" onclick="salesReturn('+
										data.object.list[i].orderId+
										',1)"><p>退货</p></a>'+
										'<a href="javascript:void(0)" onclick="delOrder('+
										data.object.list[i].orderId+
										',1)"><p>删除订单</p></a>'+
										'</td></tr><tr><td height="10" colspan="6" align="center" valign="middle" style="background: #ECECEC;"></td></tr></tbody>';
								break;
							case 4:
								html += '<tbody id="c2_canceled"><tr class="c2_bottom_time"><td height="40" colspan="6" align="left" valign="middle"><span>主订单号:'+
										data.object.list[i].orderId+
										'</span> <span>下单时间:'+
										getLocalTime(data.object.list[i].crTime)+
										'</span></td></tr><tr class="c2_bottom_obligation"><td colspan="3" align="left" valign="middle" class="c2_list">';
								for (var j = 0 ; j < data.object.list[i].listOr.length ; j++) {
									var image=JSON.parse(data.object.list[i].listOr[j].id.image)
									html += '<ol><li class="c2_bottom_img">'+
												'<a href="../../goods_details.html?id='+data.object.list[i].listOr[j].id.id+'">';
									for (var int = 0; int < 1; int++) {
										console.log(data);
										html += '<img src="'+images_path80+''+image[0]+'"></a>'
														}
																		
									     html +='<a href="../../goods_details.html?id='+data.object.list[i].listOr[j].id.id+'">'+
												'<span>'+
												data.object.list[i].listOr[j].id.sellPoint+
												'</span></a></li><li class="c2_bottom_money"><span>￥'+
												data.object.list[i].listOr[j].id.price+
												'</span></li><li class="c2_bottom_number"><span>'+
												data.object.list[i].listOr[j].id.number+
												'</span></li></ol>';
								}
								html += '</td>'+'<td align="center" valign="top" class="annual_payment"><span>￥'+
												data.object.list[i].listOr[0].id.total+'</span>';
								html += '<td align="center" valign="top" class="obligation"><span>已取消</span> '+
										'</td>'+
										'<td align="center" valign="top" class="immediate">'+
											'<a href="javascript:void(0)" onclick="buyAgain('+i+')"><p>再次购买</p></a>'+
											'<a href="javascript:void(0)" onclick="delOrder('+
											data.object.list[i].orderId+
											',1)"><p>删除订单</p></a>'+
										'</td></tr><tr><td height="10" colspan="6" align="center" valign="middle" style="background: #ECECEC;"></td></tr></tbody>'	;
								break;
						}		
					}
					html += '<tr>'+
								'<td height="40" colspan="6" align="left" valign="middle">'+
								'<div class="paging_down">';
								
					html += '<a href="javascript:void(0);" onclick="getOrders('+data.object.previousPageNo+','+status+')">上一页</a>';
					
					if(data.object.bottomPageNo > 8) {
						html += '<a href="javascript:void(0);" onclick="getOrders('+data.object.topPageNo+','+status+')">'+data.object.topPageNo+'</a>';
						if((pageNo - 2) <= 1) {
							for(var n = 2; n <= (pageNo + 2); n++) {
								html += '<a href="javascript:void(0);" onclick="getOrders('+n+','+status+')">'+n+'</a>';
							}
							html += '<span>...</span>';
						}
						if((pageNo + 2) >= data.object.bottomPageNo) {
							html += '<span>...</span>';
							for(var n = (pageNo - 2); n < data.object.bottomPageNo; n++) {
								temp_html += '<a href="javascript:void(0);" onclick="getOrders('+n+','+status+')">'+n+'</a>';
							}
						}
						if((pageNo - 2) > 1 && (pageNo + 2) < data.object.bottomPageNo) {
							if(pageNo - 3 > 1) {
								html += '<span>...</span>';
							}
							for(var n = (pageNo - 2); n < (pageNo + 3); n++) {
								html += '<a href="javascript:void(0);" onclick="getOrders('+n+','+status+')">'+n+'</a>';
							}
							if(pageNo + 3 < data.object.bottomPageNo) {
								html += '<span>...</span>';
							}
						}
						html += '<a href="javascript:void(0);" onclick="getOrders('+data.object.bottomPageNo+','+status+')">'+data.object.bottomPageNo+'</a>';
					}else {
						for(var n = 1; n < data.object.bottomPageNo + 1; n++) {
							html += '<a href="javascript:void(0);" onclick="getOrders('+n+','+status+')">'+n+'</a>';
						}
					}
					html += '<a href="javascript:void(0);" onclick="getOrders('+data.object.nextPageNo+','+status+')">下一页</a>';
					html +=	'共'+data.object.totalPages+'页,当前第'+data.object.pageNo+'页,到第<input type="text" class="paging_down_text" onblur="checkpageNo(this)" value="'+
							data.object.pageNo+'"/>页'+
							'<input type="button" name="Submit" value="确定" class="paging_down_submit" onclick="skipPage(this)"/>';
				}
			}
			html += '</table>';
			$("#orderBox").append(html);
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
/*
 * 获取订单类型数量orderSku
 */
function getOrdersCount(){
	$.ajax({
		url:"../../order/findOrderByStatusTotal",
		success:function(data){
			if (data.status) {
				$("#status").empty();
				var html = ''
				html += '<a href="javascript:void(0)" onclick="getOrders()">全部订单<span>（'+
							data.object.statusAll+
							'）</span></a>'+
						'<a href="javascript:void(0)" onclick="getOrders(1,0)">待付款<span>（'+
							data.object.status0+
							'）</span></a>'+
						'<a href="javascript:void(0)" onclick="getOrders(1,1)">待发货<span>（'+
							data.object.status1+
							'）</span></a>'+
						'<a href="javascript:void(0)" onclick="getOrders(1,2)">待收货<span>（'+
							data.object.status2+
							'）</span></a>'+	
						'<a href="javascript:void(0)" onclick="getOrders(1,3)">已收货<span>（'+
							data.object.status3+
							'）</span></a>'+
						'<a href="javascript:void(0)" onclick="getOrders(1,4)">已取消<span>（'+
							data.object.status4+
							'）</span></a>'	;
				$("#status").append(html);			
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

function buyAgain(i){
	var p;
	
	$.ajax({
		url:"../../order/getPostMessage",
	   type:"post",
	   dataType:"json",
	   async:false,
	   success : function(data){
		  p=data;
		
		   
	   },
	})
	
	
	var number=0;
	var reallyTotal=0;
	var or=orderSku[i];
	console.log(orderSku[i]);
	var order = new Object();
	var orders = new Array();
	for (var i = 0;i<or.listOr.length;i++) {
	/*	order.id = or.listOr[i].id.skuId;*/
		order.name = or.listOr[i].id.sellPoint;
		order.deliveryPlace = or.listOr[i].id.deliveryPlace;
		order.price = or.listOr[i].id.sprice;
		order.count = or.listOr[i].id.number;
		order.rowtol = or.listOr[i].id.paid;
		reallyTotal=reallyTotal+or.listOr[i].id.paid;
		orders = orders.concat(order);
		number=number+parseInt(or.listOr[i].id.number);
		order = new Object();
	}
	 console.log(p);
	orders.postLine = p.object["0"];
	orders.postage = p.object["1"];
	orders.tolprice = reallyTotal;
	orders.tolcount = number;
	orders.len = or.listOr.length;
	orders.type=1;
	orders.orderId=or.listOr["0"].id.orderId;
	removeSessionStrongParam("orders");
	saveSessionStrongParam("orders",orders);
	location.href="../../user/indent.html";
}


function confirm(orderId){
	new $.flavr({
	    content     : '是否确定收货',
	    dialog      : 'confirm',
	    onConfirm   : function(){
	    	$.ajax({
				url:"../../order/finishOrder",
				data:{"id":orderId},
				success: function(data){
					if (data.status) {
						
						new $.flavr({
						    content      : '确认完成！感谢您的光临',
						    buttons     : {
			    			       resize  : { text: '确定', style: 'danger',
			    			                        action: function(){
			    			                        	window.location.href="personal_indent.html";
			    			                        	return false;
			    			                        }
			    			        },
						
						}
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
	    },
	    onCancel    : function(){
	    },
	    closeOverlay : true,
  		closeEsc     : true
	});
}
