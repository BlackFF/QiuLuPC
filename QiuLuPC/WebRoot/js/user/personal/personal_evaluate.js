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
	getAllEva();
	
	
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
					xs+='<li><a href="user/personal/personal_information.html" >'+data.object.username+'</a></li>'
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

//初始化验证插件
$(function () {
	Validator.language = 'zh';
});

function getScore(obj){
	var score = $(obj).val();
	$(obj).parents("form").children("#score").val(score);
};

function changeEva(obj){
	var flag = $(obj).val();
	switch (flag){
		case "全部评价":
			getAllEva(1);
			break;
		case "待评价":
			getNoEva(1);
			break;
		case "已评价":
			getIsEva(1);
			break;
	}
}

//全部评价
function getAllEva(pageNo){
	$.ajax({
		url:"../../Eva/findOrderResult",
		data:{"pageNo":pageNo},
		success:function(data){
			if (data.status) {
				$("#content").empty();
				var html = '';
				html += '<div class="shop_evaluate"><div class="evaluate_top"><h4>商品评价</h4><select name="select" onchange="changeEva(this)"><option value="全部评价" selected="selected">全部评价</option><option value="待评价">待评价</option><option value="已评价">已评价</option></select></div>';
				if (data.object.list.length == 0) {
					html += '<div class="evaluate_no"><span>暂无需评价商品</span><a href="../../index.html"><p>立即前往购买</p></a></div>';
				} else{
					for (var i = 0; i< data.object.list.length;i++) {
						var image=JSON.parse(data.object.list[i].id.simage)
					    var eimage=JSON.parse(data.object.list[i].image)
					    var type = data.object.list[i].type;
						switch (true){
							case type == null || type == 0://未评价
								html += '<div class="to_evaluate"><table width="970" border="0" ><tr class="evaluate_title"><td width="540" height="40" valign="middle" class="shop_infor">商品信息</td><td width="240" align="center" valign="middle">下单时间</td><td width="190" align="center" valign="middle">操作</td></tr><tr><td valign="middle"  align="left" class="payment_img">'+
										'<a href="../../goods_details.html?id='+data.object.list[i].id.skuId+'">';
											
											for (var int = 0; int < 1; int++) {
												html += '<img src="'+images_path80+''+image[0]+'"></a>'
																}
											
												html +='<a href="../../goods_details.html?id='+data.object.list[i].id.skuId+'"><span>'+
											data.object.list[i].id.sellPoint+
										'</span></a>'+
										'</td><td align="center" valign="middle">'+
										getLocalTime(data.object.list[i].createTime)+
										'</td><td align="center" valign="middle" class="evaluate_button"><a href="javascript:void(0);"  onclick="showform(this)"><p>发表评价</p></a></td></tr></table>';
								//表单		
								html += '<ul class="evaluate_bottom" style="display: none;"><form class="validator" method="post" name="form1"><input type="hidden" name="orderId" value="'+
										data.object.list[i].id.orderId+'"/><input type="hidden" name="skuId" value="'+
										data.object.list[i].id.skuId+'"/><input type="hidden" name="commodityScore" value="5" id="score"/><li><dd><span>*</span>评分：</dd><dt>';
										switch (i){
											case 0:
												html += '<fieldset class="starability-grow"> <input type="radio" id="rate5-3" name="rating" value="5" onclick="getScore(this)"/><label for="rate5-3" title="Amazing"></label><input type="radio" id="rate4-3" name="rating" value="4" onclick="getScore(this)"/><label for="rate4-3" title="Very good"></label><input type="radio" id="rate3-3" name="rating" value="3" onclick="getScore(this)"/><label for="rate3-3" title="Average"></label><input type="radio" id="rate2-3" name="rating" value="2" onclick="getScore(this)"/><label for="rate2-3" title="Not good"></label><input type="radio" id="rate1-3" name="rating" value="1" onclick="getScore(this)"/><label for="rate1-3" title="Terrible"></label></fieldset>';
												break;
											case 1:
												html += '<fieldset class="starability-slot"><input type="radio" id="rate5-2" name="rating" value="5" onclick="getScore(this)"/><label for="rate5-2" title="Amazing"></label><input type="radio" id="rate4-2" name="rating" value="4" onclick="getScore(this)"/><label for="rate4-2" title="Very good"></label><input type="radio" id="rate3-2" name="rating" value="3" onclick="getScore(this)"/><label for="rate3-2" title="Average"></label><input type="radio" id="rate2-2" name="rating" value="2" onclick="getScore(this)"/><label for="rate2-2" title="Not good"></label><input type="radio" id="rate1-2" name="rating" value="1" onclick="getScore(this)"/><label for="rate1-2" title="Terrible"></label></fieldset>';
												break;
											case 2:
												html += '<fieldset class="starability-growRotate"> <input type="radio" id="rate5-4" name="rating" value="5" onclick="getScore(this)"/><label for="rate5-4" title="Amazing"></label><input type="radio" id="rate4-4" name="rating" value="4" onclick="getScore(this)"/><label for="rate4-4" title="Very good"></label><input type="radio" id="rate3-4" name="rating" value="3" onclick="getScore(this)"/><label for="rate3-4" title="Average"></label><input type="radio" id="rate2-4" name="rating" value="2" onclick="getScore(this)"/><label for="rate2-4" title="Not good"></label><input type="radio" id="rate1-4" name="rating" value="1" onclick="getScore(this)"/><label for="rate1-4" title="Terrible"></label></fieldset>';
												break;
											case 3:
												html += '<fieldset class="starability-fade"> <input type="radio" id="rate5-5" name="rating" value="5" onclick="getScore(this)"/><label for="rate5-5" title="Amazing"></label><input type="radio" id="rate4-5" name="rating" value="4" onclick="getScore(this)"/><label for="rate4-5" title="Very good"></label><input type="radio" id="rate3-5" name="rating" value="3" onclick="getScore(this)"/><label for="rate3-5" title="Average"></label><input type="radio" id="rate2-5" name="rating" value="2" onclick="getScore(this)"/><label for="rate2-5" title="Not good"></label><input type="radio" id="rate1-5" name="rating" value="1" onclick="getScore(this)"/><label for="rate1-5" title="Terrible"></label></fieldset>';
												break;
											case 4:
												html += '	<fieldset class="starability-basic"><input type="radio" id="rate5-1" name="rating" value="5" onclick="getScore(this)"/><label for="rate5-1" title="Amazing"></label><input type="radio" id="rate4-1" name="rating" value="4" onclick="getScore(this)"/><label for="rate4-1" title="Very good"></label><input type="radio" id="rate3-1" name="rating" value="3" onclick="getScore(this)"/><label for="rate3-1" title="Average"></label><input type="radio" id="rate2-1" name="rating" value="2" onclick="getScore(this)"/><label for="rate2-1" title="Not good"></label><input type="radio" id="rate1-1" name="rating" value="1" onclick="getScore(this)"/><label for="rate1-1" title="Terrible"></label></fieldset>'
												break;	
										}
								html+='</dt></li>'+
										'<li><dd><span>*</span>评价：</dd><dt class="evaluate_text"><textarea name="content" placeholder="您的评价是什么？"></textarea></dt></li><li><dd>晒单：</dd><div class="img-box full"><section class=" img-section"><p><span>最多可以上传5张图片</span></p><div class="z_photo upimg-div clear" ><section class="z_file fl"><img src="../../images/a11.png" class="add-img"><input type="file" name="files" id="file" class="file" value="" accept="image/jpg,image/jpeg,image/png,image/bmp" onchange="change(this)"/></section></div></section></div></li><li class="eva_bobut"><input type="checkbox" name="type" value="0" onchange="changeNoName(this)"><span>匿名评价</span><a href="javascript:void(0);" onclick="subEva(this)"><p>发表评价</p></a></li></form></ul>';
								html+='</div>';
								break;
							case  type == 1://已评价
								html += '<div class="evaluate_over"><table width="970" border="0" ><tr class="evaluate_title"><td width="540" height="2" valign="middle" class="shop_infor"></td><td width="240" align="center" valign="middle"></td><td width="190" align="center" valign="middle"></td></tr>'+
										'<tr><td valign="middle"  align="left" class="payment_img">'+
											'<a href="../../goods_details.html?id='+data.object.list[i].id.skuId+'">';

								        for (var int = 0; int <1; int++) {
									     html += '<img src="'+images_path80+''+image[0]+'"></a>';
													}
									 
									    html +='<a href="../../goods_details.html?id='+data.object.list[i].id.skuId+'"><span>'+
											data.object.list[i].id.sellPoint+
											'</span></a></td><td align="center" valign="middle">'+
											getLocalTime(data.object.list[i].createTime)+
											'</td><td align="center" valign="middle" class="evaluate_button_over"><a href="javascript:void(0);" onclick="showEva(this)"><p>查看评价</p></a></td></tr></table>'+
											'<div class="evaluate_over_text" style="display: none;"><ul class="evaluate_over_ul"><li class="evaluate_time"><dd>评分：'+
											data.object.list[i].commodityScore+
											'</dd><dt>评论时间:'+
											getLocalTime(data.object.list[i].time)+
											'</dt></li><li class="evaluate_content">'+
											data.object.list[i].content+
											'</li><li class="evaluate-img">';
								if (eimage != null) {
									for (var int = 0; int < eimage.length; int++) {
										html += '<img src="'+evaluation_images+''+eimage[int]+'" style="width=80px;height=80px"></a>'
														}
																					
									/*var imgs = data.object.list[i].image.split(",");
									for (var j =0 ; j<imgs.length;j++) {
										html += '<a onclick="focusImg(this)"><img src="'+
												imgs[j]+
												'"></a>';
									}*/
								}
								html += '</ul></div></div>';
								break;
						}
					}
					//分页
					html += '<tr>'+
							'<td height="40" colspan="6" align="left" valign="middle">'+
							'<div class="paging_down">';
							
					html += '<a href="javascript:void(0);" onclick="getAllEva('+data.object.previousPageNo+')">上一页</a>';
					
					if(data.object.bottomPageNo > 8) {
						html += '<a href="javascript:void(0);" onclick="getAllEva('+data.object.topPageNo+')">'+data.object.topPageNo+'</a>';
						if((pageNo - 2) <= 1) {
							for(var n = 2; n <= (pageNo + 2); n++) {
								html += '<a href="javascript:void(0);" onclick="getAllEva('+n+')">'+n+'</a>';
							}
							html += '<span>...</span>';
						}
						if((pageNo + 2) >= data.object.bottomPageNo) {
							html += '<span>...</span>';
							for(var n = (pageNo - 2); n < data.object.bottomPageNo; n++) {
								temp_html += '<a href="javascript:void(0);" onclick="getAllEva('+n+')">'+n+'</a>';
							}
						}
						if((pageNo - 2) > 1 && (pageNo + 2) < data.object.bottomPageNo) {
							if(pageNo - 3 > 1) {
								html += '<span>...</span>';
							}
							for(var n = (pageNo - 2); n < (pageNo + 3); n++) {
								html += '<a href="javascript:void(0);" onclick="getAllEva('+n+')">'+n+'</a>';
							}
							if(pageNo + 3 < data.object.bottomPageNo) {
								html += '<span>...</span>';
							}
						}
						html += '<a href="javascript:void(0);" onclick="getAllEva('+data.object.bottomPageNo+')">'+data.object.bottomPageNo+'</a>';
					}else {
						for(var n = 1; n < data.object.bottomPageNo + 1; n++) {
							html += '<a href="javascript:void(0);" onclick="getAllEva('+n+')">'+n+'</a>';
						}
					}
					html += '<a href="javascript:void(0);" onclick="getAllEva('+data.object.nextPageNo+')">下一页</a>';
					html +=	'共'+data.object.totalPages+'页,当前第'+data.object.pageNo+'页,到第<input type="text" class="paging_down_text" onblur="checkpageNo(this)" value="'+
							data.object.pageNo+'"/>页'+
							'<input type="button" name="Submit" value="确定" class="paging_down_submit" onclick="skipPage(this)"/>';
				}
				html += '</div>';
				$("#content").append(html);
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

//已评价
function getIsEva(pageNo){
	$.ajax({
		url:"../../Eva/findEvaFinish",
		data:{"pageNo":pageNo},
		success:function(data){
			if (data.status) {
				console.log(data);
				$("#content").empty();
				var html = '';
				html += '<div class="shop_evaluate"><div class="evaluate_top"><h4>商品评价</h4><select name="select" onchange="changeEva(this)"><option value="全部评价">全部评价</option><option value="待评价">待评价</option><option value="已评价" selected="selected">已评价</option></select></div>';
				if (data.object.list.length == 0) {
					html += '<div class="evaluate_no"><span>暂无需评价商品</span><a href="../../index.html"><p>立即前往购买</p></a></div>';
				}else{
					for (var i = 0; i< data.object.list.length;i++) {
						var eimage=JSON.parse(data.object.list[i].image)
						var image=JSON.parse(data.object.list[i].id.simage)
						html += '<div class="evaluate_over"><table width="970" border="0" ><tr class="evaluate_title"><td width="540" height="2" valign="middle" class="shop_infor"></td><td width="240" align="center" valign="middle"></td><td width="190" align="center" valign="middle"></td></tr>'+
								'<tr><td valign="middle"  align="left" class="payment_img">'+
									'<a href="../../goods_details.html?id='+data.object.list[i].id.skuId+'">'
									 + '<img style="width:80px;height:80px;" src="'+images_path80+''+image[0]+'"></a>';
									html +='<a href="../../goods_details.html?id='+data.object.list[i].id.skuId+'"><span>'+
									data.object.list[i].id.sellPoint+
									'</span></a></td><td align="center" valign="middle">'+
									getLocalTime(data.object.list[i].createTime)+
									'</td><td align="center" valign="middle" class="evaluate_button_over"><a href="javascript:void(0);" onclick="showEva(this)"><p>查看评价</p></a></td></tr></table>'+
									'<div class="evaluate_over_text" style="display: none;"><ul class="evaluate_over_ul"><li class="evaluate_time"><dd>评分：'+
									data.object.list[i].commodityScore+
									'</dd><dt>评论时间:'+
									getLocalTime(data.object.list[i].time)+
									'</dt></li><li class="evaluate_content">'+
									data.object.list[i].content+
									'</li><li class="evaluate-img">';
									if (eimage != null) {
										for (var int = 0; int < eimage.length; int++) {
											html += '<img src="'+evaluation_images+''+eimage[int]+'" style="width=80px;height=80px"></a>'
															}
																						
										/*var imgs = data.object.list[i].image.split(",");
										for (var j =0 ; j<imgs.length;j++) {
											html += '<a onclick="focusImg(this)"><img src="'+
													imgs[j]+
													'"></a>';
										}*/
									}
						html += '</ul></div></div>';
					}
					//分页
					html += '<tr>'+
							'<td height="40" colspan="6" align="left" valign="middle">'+
							'<div class="paging_down">';
							
					html += '<a href="javascript:void(0);" onclick="getIsEva('+data.object.previousPageNo+')">上一页</a>';
					
					if(data.object.bottomPageNo > 8) {
						html += '<a href="javascript:void(0);" onclick="getIsEva('+data.object.topPageNo+')">'+data.object.topPageNo+'</a>';
						if((pageNo - 2) <= 1) {
							for(var n = 2; n <= (pageNo + 2); n++) {
								html += '<a href="javascript:void(0);" onclick="getIsEva('+n+')">'+n+'</a>';
							}
							html += '<span>...</span>';
						}
						if((pageNo + 2) >= data.object.bottomPageNo) {
							html += '<span>...</span>';
							for(var n = (pageNo - 2); n < data.object.bottomPageNo; n++) {
								temp_html += '<a href="javascript:void(0);" onclick="getIsEva('+n+')">'+n+'</a>';
							}
						}
						if((pageNo - 2) > 1 && (pageNo + 2) < data.object.bottomPageNo) {
							if(pageNo - 3 > 1) {
								html += '<span>...</span>';
							}
							for(var n = (pageNo - 2); n < (pageNo + 3); n++) {
								html += '<a href="javascript:void(0);" onclick="getIsEva('+n+')">'+n+'</a>';
							}
							if(pageNo + 3 < data.object.bottomPageNo) {
								html += '<span>...</span>';
							}
						}
						html += '<a href="javascript:void(0);" onclick="getIsEva('+data.object.bottomPageNo+')">'+data.object.bottomPageNo+'</a>';
					}else {
						for(var n = 1; n < data.object.bottomPageNo + 1; n++) {
							html += '<a href="javascript:void(0);" onclick="getIsEva('+n+')">'+n+'</a>';
						}
					}
					html += '<a href="javascript:void(0);" onclick="getIsEva('+data.object.nextPageNo+')">下一页</a>';
					html +=	'共'+data.object.totalPages+'页,当前第'+data.object.pageNo+'页,到第<input type="text" class="paging_down_text" onblur="checkpageNo(this)" value="'+
							data.object.pageNo+'"/>页'+
							'<input type="button" name="Submit" value="确定" class="paging_down_submit" onclick="skipPage(this)"/>';
				}
				html += '</div>';
				$("#content").append(html);
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

//未评价
function getNoEva(pageNo){
	$.ajax({
		url:"../../Eva/findEvaNoFinish",
		data:{"pageNo":pageNo},
		success:function(data){
			if (data.status) {
				$("#content").empty();
				var html = '';
				html += '<div class="shop_evaluate"><div class="evaluate_top"><h4>商品评价</h4><select name="select" onchange="changeEva(this)"><option value="全部评价">全部评价</option><option value="待评价" selected="selected">待评价</option><option value="已评价">已评价</option></select></div>';
				if (data.object.list.length == 0) {
					html += '<div class="evaluate_no"><span>暂无需评价商品</span><a href="../../index.html"><p>立即前往购买</p></a></div>';
				} else{
					for (var i = 0; i< data.object.list.length;i++) {
						var eimage=JSON.parse(data.object.list[i].image)
						var image=JSON.parse(data.object.list[i].id.simage)
						html += '<div class="to_evaluate"><table width="970" border="0" ><tr class="evaluate_title"><td width="540" height="40" valign="middle" class="shop_infor">商品信息</td><td width="240" align="center" valign="middle">下单时间</td><td width="190" align="center" valign="middle">操作</td></tr><tr><td valign="middle"  align="left" class="payment_img">'+
								'<a href="../../goods_details.html?id='+data.object.list[i].id.skuId+'">'+
									'<img src="'+images_path80+''+image[0]+'"></a>'+
								'<a href="../../goods_details.html?id='+data.object.list[i].id.skuId+'"><span>'+
									data.object.list[i].id.sellPoint+
								'</span></a>'+
								'</td><td align="center" valign="middle">'+
								getLocalTime(data.object.list[i].createTime)+
								'</td><td align="center" valign="middle" class="evaluate_button"><a href="javascript:void(0);"  onclick="showform(this)"><p>发表评价</p></a></td></tr></table>';
						//表单		
						html += '<ul class="evaluate_bottom" style="display: none;"><form class="validator" method="post" name="form1"><input type="hidden" name="orderId" value="'+
								data.object.list[i].id.orderId+'"/><input type="hidden" name="skuId" value="'+
								data.object.list[i].id.skuId+'"/><input type="hidden" name="commodityScore" value="5" id="score"/><li><dd><span>*</span>评分：</dd><dt>';
								switch (i){
									case 0:
										html += '<fieldset class="starability-grow"> <input type="radio" id="rate5-3" name="rating" value="5" onclick="getScore(this)"/><label for="rate5-3" title="Amazing"></label><input type="radio" id="rate4-3" name="rating" value="4" onclick="getScore(this)"/><label for="rate4-3" title="Very good"></label><input type="radio" id="rate3-3" name="rating" value="3" onclick="getScore(this)"/><label for="rate3-3" title="Average"></label><input type="radio" id="rate2-3" name="rating" value="2" onclick="getScore(this)"/><label for="rate2-3" title="Not good"></label><input type="radio" id="rate1-3" name="rating" value="1" onclick="getScore(this)"/><label for="rate1-3" title="Terrible"></label></fieldset>';
										break;
									case 1:
										html += '<fieldset class="starability-slot"><input type="radio" id="rate5-2" name="rating" value="5" onclick="getScore(this)"/><label for="rate5-2" title="Amazing"></label><input type="radio" id="rate4-2" name="rating" value="4" onclick="getScore(this)"/><label for="rate4-2" title="Very good"></label><input type="radio" id="rate3-2" name="rating" value="3" onclick="getScore(this)"/><label for="rate3-2" title="Average"></label><input type="radio" id="rate2-2" name="rating" value="2" onclick="getScore(this)"/><label for="rate2-2" title="Not good"></label><input type="radio" id="rate1-2" name="rating" value="1" onclick="getScore(this)"/><label for="rate1-2" title="Terrible"></label></fieldset>';
										break;
									case 2:
										html += '<fieldset class="starability-growRotate"> <input type="radio" id="rate5-4" name="rating" value="5" onclick="getScore(this)"/><label for="rate5-4" title="Amazing"></label><input type="radio" id="rate4-4" name="rating" value="4" onclick="getScore(this)"/><label for="rate4-4" title="Very good"></label><input type="radio" id="rate3-4" name="rating" value="3" onclick="getScore(this)"/><label for="rate3-4" title="Average"></label><input type="radio" id="rate2-4" name="rating" value="2" onclick="getScore(this)"/><label for="rate2-4" title="Not good"></label><input type="radio" id="rate1-4" name="rating" value="1" onclick="getScore(this)"/><label for="rate1-4" title="Terrible"></label></fieldset>';
										break;
									case 3:
										html += '<fieldset class="starability-fade"> <input type="radio" id="rate5-5" name="rating" value="5" onclick="getScore(this)"/><label for="rate5-5" title="Amazing"></label><input type="radio" id="rate4-5" name="rating" value="4" onclick="getScore(this)"/><label for="rate4-5" title="Very good"></label><input type="radio" id="rate3-5" name="rating" value="3" onclick="getScore(this)"/><label for="rate3-5" title="Average"></label><input type="radio" id="rate2-5" name="rating" value="2" onclick="getScore(this)"/><label for="rate2-5" title="Not good"></label><input type="radio" id="rate1-5" name="rating" value="1" onclick="getScore(this)"/><label for="rate1-5" title="Terrible"></label></fieldset>';
										break;
									case 4:
										html += '	<fieldset class="starability-basic"><input type="radio" id="rate5-1" name="rating" value="5" onclick="getScore(this)"/><label for="rate5-1" title="Amazing"></label><input type="radio" id="rate4-1" name="rating" value="4" onclick="getScore(this)"/><label for="rate4-1" title="Very good"></label><input type="radio" id="rate3-1" name="rating" value="3" onclick="getScore(this)"/><label for="rate3-1" title="Average"></label><input type="radio" id="rate2-1" name="rating" value="2" onclick="getScore(this)"/><label for="rate2-1" title="Not good"></label><input type="radio" id="rate1-1" name="rating" value="1" onclick="getScore(this)"/><label for="rate1-1" title="Terrible"></label></fieldset>'
										break;	
								}
						html+='</dt></li>'+
								'<li><dd><span>*</span>评价：</dd><dt class="evaluate_text"><textarea name="content" placeholder="您的评价是什么？"></textarea></dt></li><li><dd>晒单：</dd><div class="img-box full"><section class=" img-section"><p><span>最多可以上传5张图片</span></p><div class="z_photo upimg-div clear" ><section class="z_file fl"><img src="../../images/a11.png" class="add-img"><input type="file" name="files" id="file" class="file" value="0" accept="image/jpg,image/jpeg,image/png,image/bmp" onchange="change(this)"/></section></div></section></div></li><li class="eva_bobut"><input type="checkbox" name="type" value="0" onchange="changeNoName(this)"><span>匿名评价</span><a href="javascript:void(0);" id="sub" onclick="subEva(this)"><p>发表评价</p></a></li></form></ul>';
						html+='</div>';
					}
					//分页
					html += '<tr>'+
							'<td height="40" colspan="6" align="left" valign="middle">'+
							'<div class="paging_down">';
							
					html += '<a href="javascript:void(0);" onclick="getNoEva('+data.object.previousPageNo+')">上一页</a>';
					
					if(data.object.bottomPageNo > 8) {
						html += '<a href="javascript:void(0);" onclick="getNoEva('+data.object.topPageNo+')">'+data.object.topPageNo+'</a>';
						if((pageNo - 2) <= 1) {
							for(var n = 2; n <= (pageNo + 2); n++) {
								html += '<a href="javascript:void(0);" onclick="getNoEva('+n+')">'+n+'</a>';
							}
							html += '<span>...</span>';
						}
						if((pageNo + 2) >= data.object.bottomPageNo) {
							html += '<span>...</span>';
							for(var n = (pageNo - 2); n < data.object.bottomPageNo; n++) {
								temp_html += '<a href="javascript:void(0);" onclick="getNoEva('+n+')">'+n+'</a>';
							}
						}
						if((pageNo - 2) > 1 && (pageNo + 2) < data.object.bottomPageNo) {
							if(pageNo - 3 > 1) {
								html += '<span>...</span>';
							}
							for(var n = (pageNo - 2); n < (pageNo + 3); n++) {
								html += '<a href="javascript:void(0);" onclick="getNoEva('+n+')">'+n+'</a>';
							}
							if(pageNo + 3 < data.object.bottomPageNo) {
								html += '<span>...</span>';
							}
						}
						html += '<a href="javascript:void(0);" onclick="getNoEva('+data.object.bottomPageNo+')">'+data.object.bottomPageNo+'</a>';
					}else {
						for(var n = 1; n < data.object.bottomPageNo + 1; n++) {
							html += '<a href="javascript:void(0);" onclick="getNoEva('+n+')">'+n+'</a>';
						}
					}
					html += '<a href="javascript:void(0);" onclick="getNoEva('+data.object.nextPageNo+')">下一页</a>';
					html +=	'共'+data.object.totalPages+'页,当前第'+data.object.pageNo+'页,到第<input type="text" class="paging_down_text" onblur="checkpageNo(this)" value="'+
							data.object.pageNo+'"/>页'+
							'<input type="button" name="Submit" value="确定" class="paging_down_submit" onclick="skipPage(this)"/>';
				}
				html += '</div>';
				$("#content").append(html);
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

function showform(obj){
	$(obj).parents("table").siblings("ul").fadeToggle("slow");
};

function showEva(obj){
	$(obj).parents("table").siblings("div .evaluate_over_text").fadeToggle("slow");
};

function changeNoName(obj){
	var flag = $(obj).attr("checked");
	if (flag == "checked") {
		$(obj).val(1);
	} else{
		$(obj).val(0);
	}
};
//提交评价
function subEva(obj){
	if (!Validator.validate($("#addForm"))) {
				return;
	}
	$(obj).parents("form").attr("action","../../Eva/addEvaluation");
	$(obj).parents("form").ajaxSubmit(function(data){
		getAllEva();
	})
}


//聚焦图片
function focusImg(obj){
	var html = $(obj).html();
	new $.flavr({
	    content      : html,
	    buttons     : {
	        close   : { text: '关闭'}
	    },
	    closeOverlay : true,
	    closeEsc     : true
	});
}
