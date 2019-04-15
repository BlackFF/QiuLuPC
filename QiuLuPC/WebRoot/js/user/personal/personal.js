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
	getUserInfo();
	getOrders();
	
	
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
/*
 * 获取用户信息
 */
function getUserInfo(){
	$.ajax({
		url:"../../User/findHeadPortTelOrderFav",
		success: function(data){
			if (data.status) {
				console.log(data);
				$("#userInfo").empty();
				var html = '';
				html += '<li class="c1_top_left">';
				if(data.object.headPort==null||data.object.headPort==""){
					html += '<a href="personal_information.html"><img src="../../images/personal_c1.png" class="c1_img"></a>';
				}
				else{
					html += '<a href="personal_information.html"><img style="width:90px;height:110px"  src="'+headPort_images+''+data.object.headPort+'" class="c1_img"></a>';
				}		
				html += '<p class="c1_user">'+
							data.object.telephone+
							'</p></li>'+
						'<li class="c1_top_center">'+
							'<h3>其他信息：</h3>'+
							'<a href="personal_indent.html"><p>订单提醒：<span>（'+
							data.object.orderNum+'）</span></p></a>'+
							'<a href="personal_collect.html"><p>我的收藏：<span>（'+
							data.object.favoritesNum+'）</span></p></a></li>';
				$("#userInfo").append(html);
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


/*
 * 获取订单信息
 */
function getOrders(pageNo){
	$.ajax({
		url:"../../order/findOrderByStatus",
		data:{"status":status,"pageNo":pageNo},
		success: function(data){
			$("#orderBox").empty();
			var html = '';
			html += '<table width="780" border="0" cellspacing="0" cellpadding="0">'+
						'<tr class="c1_title">'+
							'<td width="110" height="40" align="center" valign="middle">订单编码</td>'+
							'<td width="220" align="center" valign="middle">订单商品</td>'+
							'<td width="98" align="center" valign="middle">收货人</td>'+
							'<td width="94" align="center" valign="middle">订单金额</td>'+
							'<td width="88" align="center" valign="middle">下单日期</td>'+
							'<td width="88" align="center" valign="middle">订单状态</td>'+
							'<td width="81" align="center" valign="middle">操作</td>'+
						'</tr>'	;
			if (data.status) {
				if (data.object.list.length==0) {
					html += '<tr class="c1_noinfor">'+
								'<td colspan="7" align="center" valign="middle">'+
									'暂无订单，请去挑选商品：<a href="../../index.html">首页</a>'+
								'</td>'+
							'</tr>';
				} else {
					for (var i = 0; i < data.object.list.length; i++) {
						html += '<tr class="c1_center"><td align="center" valign="middle">'+
								data.object.list[i].orderId+
								'</td><td align="left" valign="middle">';
								for (var j = 0 ; j < data.object.list[i].listOr.length ; j++) {
									var image=JSON.parse(data.object.list[i].listOr[j].id.image);
									html += '<a href="../../goods_details.html?id='+data.object.list[i].listOr[j].id.id+'">'+
												'<img src="'+images_path80+''+image[0]+'"></a>';
								}
						html += '</td>';
						html += '<td align="center" valign="middle">'+
								data.object.list[i].receive+
								'</td><td align="center" valign="middle"><span>'+
								data.object.list[i].listOr[0].id.total+
								'元</span></td><td align="center" valign="middle"><span>'+
								getLocalTime(data.object.list[i].crTime)+
								'</span></td>'+
								'<td align="center" valign="middle"><span>';
						switch (data.object.list[i].listOr[0].id.status){
							case 0:
								html += '待付款';
								break;
							case 1:
								html += '待发货';
								break;
							case 2:
								html += '待收货';	
								break;
							case 3:
								html += '已收货';
								break;
							case 4:
								html += '已取消';
								break;
						}		
						html += '</span></td><td align="center" valign="middle">';
						switch (data.object.list[i].listOr[0].id.status){
							case 0:
								html += '<a href="javascript:void(0)" onclick="gotoPay('+
										data.object.list[i].orderId+
										','+data.object.list[i].listOr[0].id.total+
										')"><span>去付款</span></a><br/>'+
										'<a href="javascript:void(0)" onclick="concleOrder('+
										data.object.list[i].orderId+
										',0)"><span>取消订单</span></a><br/>';
								html += '<a href="javascript:void(0)" onclick="orderDetail('+
										data.object.list[i].orderId+
										',0)"><span>查看订单</span></a></td>'+
								'</tr>';		
								break;
							case 1:
								html += '<a href="javascript:void(0)" onclick="concleOrder('+
										data.object.list[i].orderId+
										',0)"><span>取消订单</span></a><br/>';
								html += '<a href="javascript:void(0)" onclick="orderDetail('+
										data.object.list[i].orderId+
										',0)"><span>查看订单</span></a></td>'+
								'</tr>';
								break;
							case 2:
								html += '<a href="javascript:void(0)" onclick="salesReturn('+
										data.object.list[i].orderId+
										')"><span>退货</span></a><br/>';	
								html += '<a href="javascript:void(0)" onclick="orderDetail('+
										data.object.list[i].orderId+
										',0)"><span>查看订单</span></a></td>'+
								'</tr>';		
								break;
							case 3:
								html += '<a href="javascript:void(0)" onclick="salesReturn('+
										data.object.list[i].orderId+
										')"><span>退货</span></a><br/>'+
										'<a href="javascript:void(0)" onclick="delOrder('+
										data.object.list[i].orderId+
										')"><span>删除订单</span></a><br/>';
								html += '<a href="javascript:void(0)" onclick="orderDetail('+
										data.object.list[i].orderId+
										',0)"><span>查看订单</span></a></td>'+
								'</tr>';		
								break;
							case 4:
								html += '<a href="javascript:void(0)" onclick="delOrder('+
										data.object.list[i].orderId+
										')"><span>删除订单</span></a><br/>';
								break;
						}
					}
					html += '<tr>'+
								'<td height="40" colspan="6" align="left" valign="middle">'+
								'<div class="paging_down">';
								
					html += '<a href="javascript:void(0);" onclick="getOrders('+data.object.previousPageNo+')">上一页</a>';
					
					if(data.object.bottomPageNo > 8) {
						html += '<a href="javascript:void(0);" onclick="getOrders('+data.object.topPageNo+')">'+data.object.topPageNo+'</a>';
						if((pageNo - 2) <= 1) {
							for(var n = 2; n <= (pageNo + 2); n++) {
								html += '<a href="javascript:void(0);" onclick="getOrders('+n+')">'+n+'</a>';
							}
							html += '<span>...</span>';
						}
						if((pageNo + 2) >= data.object.bottomPageNo) {
							html += '<span>...</span>';
							for(var n = (pageNo - 2); n < data.object.bottomPageNo; n++) {
								temp_html += '<a href="javascript:void(0);" onclick="getOrders('+n+')">'+n+'</a>';
							}
						}
						if((pageNo - 2) > 1 && (pageNo + 2) < data.object.bottomPageNo) {
							if(pageNo - 3 > 1) {
								html += '<span>...</span>';
							}
							for(var n = (pageNo - 2); n < (pageNo + 3); n++) {
								html += '<a href="javascript:void(0);" onclick="getOrders('+n+')">'+n+'</a>';
							}
							if(pageNo + 3 < data.object.bottomPageNo) {
								html += '<span>...</span>';
							}
						}
						html += '<a href="javascript:void(0);" onclick="getOrders('+data.object.bottomPageNo+')">'+data.object.bottomPageNo+'</a>';
					}else {
						for(var n = 1; n < data.object.bottomPageNo + 1; n++) {
							html += '<a href="javascript:void(0);" onclick="getOrders('+n+')">'+n+'</a>';
						}
					}
					html += '<a href="javascript:void(0);" onclick="getOrders('+data.object.nextPageNo+')">下一页</a>';
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
