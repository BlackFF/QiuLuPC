var orders,flag=0;
$(document).ready(function(){
    if (!isLogin()) {
    	location.href = "/QiuLuPC/login.html";
    	return false;
    }
	//订单操作
	orders = getSessionStrongParam("orders");
	$("tbody").empty();
	var html = '';
	html += '<tr><td height="46" colspan="6" align="left"  valign="middle" class="cart_title">商品清单</td></tr>'+
		'<tr class="table_title"><th width="445" height="50" align="left" valign="middle" scope="col" class="cart_shop">商品名称</th><th width="90" align="center" valign="middle" scope="col">发货站</th><th width="130" align="center" valign="middle" scope="col">单价</th><th width="90" align="center" valign="middle" scope="col">数量</th><th width="155" align="center" valign="middle" scope="col">小计</th></tr>';
	for (var i=0;i<orders.len;i++) {
		html += '<tr><td align="left" valign="middle" height="50" class="item_details">'+
			orders[i].name+
			'</td><td align="center" valign="middle">'+
			orders[i].deliveryPlace+
			'</td><td align="center" valign="middle">￥'+
			orders[i].price+
			'</td><td align="center" valign="middle">'+
			orders[i].count+
			'</td><td align="center" valign="middle" class="table_color">￥'+
			orders[i].rowtol+
			'</td>'+
			'<input type="hidden" class="orderId" value="'+
			orders[i].id+'"/> </tr>';
	}
	html += '<tr>'+
				'<td height="10" colspan="6" align="center" valign="middle" style="background: #ECECEC;"></td>'+
			'</tr>'+
			'<tr>'+
				'<td align="left" valign="top" height="207" colspan="7" class="table_total">'+
				'<div class="total_left">'+
					'<a href="cart.html" class="return_cart">返回修改购物车</a>'+
				'</div>'	+
				'<div class="total_right"><p>商品数量总计：<span id="selectedTotal">'+
				orders.tolcount+
				'</span>件</p>'+
				'<p style="color:red;">邮费：￥'+orders.postage+'(满￥<span id="postLine">'+
				orders.postLine+'</span>包邮)</p>'+
				'<p  class="aggregate">商品金额总计：￥<span id="priceTotal">'+
				orders.tolprice+
				'</span></p>';
				if(orders.type==1){
					html += '<a href="javascript:(0)" class="settlement" onclick="gotoOrderAgain('+orders.orderId+')">立即结算</a></div></td></tr>';
				}
				else{
					html += '<a href="javascript:(0)" class="settlement" onclick="gotoOrder()">立即结算</a></div></td></tr>';
					}
	$("tbody").append(html);
	//地址操作
	getDefAddress();
	
	/**
	 * 配送方式操作
	 */
	$("#mchoose").hide();
	var method =$("#mchoose").find("input:checked").siblings("span").text();
	checkIsPost(method);
});
//初始化验证插件
$(function () {
	Validator.language = 'zh';
});

//获取默认地址信息
function getDefAddress(){
	$.ajax({
		url:"../Address/lookDefault",
		async:false,
		success: function(data){
			if (data.object == null) {
				flag = 2;
				$("#achange").hide();
				$("#userMes").hide();
				$("#editAdd").hide();
			} else{
				$("#addForm").hide();
				$("#editAdd").hide();
				$("#userMes").empty();
				var html='';
				html += '<div class="address_style" id="userMes"><dd>'+
						data.object.receiver+
						'<span>'+
						data.object.telephone+
						'</span></dd><dt>'+
						data.object.province+
						data.object.city+
						data.object.area+
						data.object.address+
						'</dt><input  type="hidden" value="'+
						data.object.id+
						'" id="addId"/>';
				$("#userMes").append(html);
			}
		}
	});
}
//修改地址
function changeAdd(){
	$.ajax({
		url:"../Address/findAddress",
		success: function(data){
			if (data.object == null) {
				$("#editAdd").fadeIn("slow");
			} else{
				$("#achange").fadeOut("slow");
				$("#userMes").hide();
				$("#addForm").fadeOut("slow");
				$("#editAdd").empty();
				var html = '';
				for (var i=0;i<data.object.length;i++) {
					html += '<li class="set_default"><input type="radio" name="radiobutton" value="'+
							data.object[i].id+
							'" ';
							if (i==0) {
								html += 'checked="checked"';
							}
					html += '><span class="receiver">'+
							data.object[i].receiver+
							'</span><span class="address">'+
							data.object[i].province+
							data.object[i].city+
							data.object[i].area+
							data.object[i].address+
							'</span><span class="telephone">'+
							data.object[i].telephone+
							'</span>';
						if (data.object[i].status==0) {
							html += '<a href="javascript:(0)" onclick="defaultAdd('+
									data.object[i].id+
									')">设为默认地址</a>';
						} else{
							html += '默认地址&nbsp';
						}	
					html += '<a href="javascript:(0)" onclick="editAdd('+
						data.object[i].id+
						',this)">编辑</a><a href="javascript:(0)" onclick="delAdd('+
						data.object[i].id+
						')">删除</a></li>';
				}
				html += '<li><input type="radio" name="radiobutton" value="usernew"> <span>使用新地址</span> </li>';
				$("#editAdd").append(html);
				$("#editAdd").fadeIn("slow");
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
	
	//使用新地址
	$("#editAdd").on("click","input",function(){
		flag = 0;
		if ($(this).siblings("span").text()=="使用新地址") {
			flag = 2;
			if ($("#name").val()!="") {
				$("#name").val("");
				$("#address").val("");
				$("#telephone").val("");
			}
			$("#addForm").fadeIn("slow");
		} else{
			$("#addForm").fadeOut("fast");
		}
	});
	
}
//设置为默认地址
function defaultAdd(id){
	$.ajax({
		url:"../Address/defaultAddress",
		data:{"id":id},
		success: function(data){
			if (data.status) {
				changeAdd();
			} else{
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
//编辑地址
function editAdd(id,obj){
	$.ajax({
		url:"../Address/lookAddress",
		data:{"id":id},
		success: function(data){
			if (data.status) {
				flag = 1;
				$(obj).siblings("input").attr("checked","checked");
				$("#name").val(data.object.receiver);
			/*	//地址回显有问题
				$("#s_province").val();
				$("#s_city").val();
				$("#s_county").val();*/
				$("#status").val(data.object.status);
				$("#upId").val(data.object.id);
				$("#userId").val(data.object.userId);
				$("#address").val(data.object.address);
				$("#telephone").val(data.object.telephone);
				$("#addForm").fadeIn("slow");
			} else{
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
//删除地址
function delAdd(id){
	$.ajax({
		url:"../Address/deleteAddress",
		data:{"id":id},
		success: function(data){
			if (data.status) {
				changeAdd();
			} else{
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

//确认收货地址
function confirmAdd(){
	if ($(userMes).css("display") != "none") {
		return ;
	}
	var addId,receiver,address,telephone;
	switch (flag){
		case 0:
			//默认
			var checked = $("#editAdd input:checked");
			addId = $(checked[0]).val();
			receiver = $(checked[0]).siblings("span.receiver").text();
			address = $(checked[0]).siblings("span.address").text();
			telephone = $(checked[0]).siblings("span.telephone").text();
			$("#userMes").empty();
			var html = '<dd>'+receiver+
						'<span>'+telephone+'</span></dd><dt>'+
						address+'</dt><input  type="hidden" value="'+
						addId+'" id="addId"/>';
			$("#userMes").append(html);	
			$("#editAdd").hide();
			$("#addForm").hide();
			$("#achange").show();
			$("#userMes").fadeIn("slow");
			break;
		case 1:
			//编辑地址
			if (!Validator.validate($("#addForm"))) {
				return;
			}
			if ($("#s_province").val()=="省份" || $("#s_city").val()=="地级市" || $("#s_county").val()=="市、县级市") {
				new $.flavr({
				    content      : '请您选择地址',
				    closeOverlay : true,
				    closeEsc     : true
				});
				return;
			}
			$("#addForm").attr("action","../Address/updateAddress");
			$("#addForm").ajaxSubmit(function(data){
				addId = data.object.id;
				receiver = data.object.receiver;
				address = data.object.province+data.object.city+data.object.area+data.object.address;
				telephone = data.object.telephone;
				$("#userMes").empty();
				var html = '<dd>'+receiver+
							'<span>'+telephone+'</span></dd><dt>'+
							address+'</dt><input  type="hidden" value="'+
							addId+'" id="addId"/>';
				$("#userMes").append(html);
				//回显数据
				$("#editAdd").hide();
				$("#addForm").hide();
				$("#achange").show();
				$("#userMes").fadeIn("slow");
			});
			break;
		case 2:
			//新增地址
			if (!Validator.validate($("#addForm"))) {
				return;
			}
			if ($("#s_province").val()=="省份" || $("#s_city").val()=="地级市" || $("#s_county").val()=="市、县级市") {
				new $.flavr({
				    content      : '请您选择地址',
				    closeOverlay : true,
				    closeEsc     : true
				});
				return;
			}
			$("#addForm").attr("action","../Address/addAddress");
			$("#addForm").ajaxSubmit(function(data){
				addId = data.object.id;
				receiver = data.object.receiver;
				address = data.object.province+data.object.city+data.object.area+data.object.address;
				telephone = data.object.telephone;
				$("#userMes").empty();
				var html = '<dd>'+receiver+
							'<span>'+telephone+'</span></dd><dt>'+
							address+'</dt><input  type="hidden" value="'+
							addId+'" id="addId"/>';
				$("#userMes").append(html);
				//回显数据
				$("#editAdd").hide();
				$("#addForm").hide();
				$("#achange").show();
				$("#userMes").fadeIn("slow");
			});
			break;
	}
}
//修改配送方式
function changeMed(){
	$("#mshow").hide();
	$("#mchange").hide();
	$("#mchoose").fadeIn("slow");
	$("#confirmDiv").attr("onclick","confirmMed()");
}

//确认配送方式
function confirmMed(){
	$("#mchange").show();
	var method =$("#mchoose").find("input:checked").siblings("span").text();
	checkIsPost(method);
	$("#mshow").empty();
	var html = '<dd>配送方式：'+method+'</dd><dt>支付方式：在线支付</dt>';
	$("#mshow").append(html);
	$("#mshow").fadeIn("slow");
	$("#mchoose").hide();
	$("#confirmDiv").removeAttr("onclick");
}
//判断是否包邮
var tip = 0;
function checkIsPost(method){
	if (method == "快递送货"&&tip==0) {
		var priceTotal = $("#priceTotal").text();
		var postLine = $("#postLine").text();
		if (parseFloat(priceTotal) < parseFloat(postLine)) {
			$("#priceTotal").text(parseFloat(priceTotal)+parseFloat(orders.postage));
			tip = 1;
		}
	}else{
		$("#priceTotal").text(orders.tolprice);
		tip = 0;
	}
}

//提交订单
function gotoOrder(){
	var postage =0;
	if (tip == 1) {
		postage = orders.postage;
	}
	if ($("#achange").css("display") == "none") {
		new $.flavr({
		    content      : '请您完善收货人信息',
		    closeOverlay : true,
		    closeEsc     : true
		});
		return;
	}else if ($("#mchange").css("display") == "none") {
		new $.flavr({
		    content      : '请您选择配送方式',
		    closeOverlay : true,
		    closeEsc     : true
		});
		return;
	}
	var total = $("#priceTotal").text();
	var aid = $("#addId").val();
//	var express = $("#mchoose").find("input:checked").siblings("span").text();
	var express = ($("#mchoose").find("input:checked").parent().index() == 0 ? {k:'快递送货',v:true} : {k:'自提',v:false});
	var id = new Array();
    var orderId = $(".orderId");
    for (var i = 0; i < orderId.length ;i ++) {
    	id.push($(orderId[i]).val());
    }
    if (total == "undefined" || aid == "undefined" || express == "undefined") {
    	new $.flavr({
		    content      : '请确认商品信息',
		    closeOverlay : true,
		    closeEsc     : true
		});
		return;
    }
    $.ajax({
    	type:"post",
    	data:{"total":total,"aid":aid,"express":JSON.stringify(express),"id":id,"postage":postage},
    	url:"../order/saveOrder",
    	async:true,
    	traditional:true,
    	success:function(data){
    		if (data.status) {
    			var order = new Object();
    			order.orderId = data.object;
    			order.total = total;
    			removeSessionStrongParam("order");
				saveSessionStrongParam("order",order);
    			location.href = "generate_orders.html";
    		}
    		return;
    	},
    	error:function(){
			location.href="cart.html";
			return;
    	}
    });
}

//再次购买结算时
function gotoOrderAgain(oldOrderId){
	var postage =0;
	if (tip == 1) {
		postage = orders.postage;
	}
	if ($("#achange").css("display") == "none") {
		new $.flavr({
			content      : '请您完善收货人信息',
			closeOverlay : true,
			closeEsc     : true
		});
		return;
	}else if ($("#mchange").css("display") == "none") {
		new $.flavr({
			content      : '请您选择配送方式',
			closeOverlay : true,
			closeEsc     : true
		});
		return;
	}
	var total = $("#priceTotal").text();
	var aid = $("#addId").val();
//	var express = $("#mchoose").find("input:checked").siblings("span").text();
	var express = ($("#mchoose").find("input:checked").parent().index() == 0 ? {k:'快递送货',v:true} : {k:'自提',v:false});
	var id = new Array();
	var orderId = $(".orderId");
	for (var i = 0; i < orderId.length ;i ++) {
		id.push($(orderId[i]).val());
	}
	if (total == "undefined" || aid == "undefined" || express == "undefined") {
		new $.flavr({
			content      : '请确认商品信息',
			closeOverlay : true,
			closeEsc     : true
		});
		return;
	}
	$.ajax({
		type:"post",
		data:{"orderId":oldOrderId,"total":total,"aid":aid,"express":JSON.stringify(express),"id":id,"postage":postage},
		url:"../order/saveOrderAgain",
		async:true,
		traditional:true,
		success:function(data){
			if (data.status) {
				var order = new Object();
				order.orderId = data.object;
				order.total = total;
				removeSessionStrongParam("order");
				saveSessionStrongParam("order",order);
				location.href = "generate_orders.html";
			}
			return;
		},
		error:function(){
			location.href="cart.html";
			return;
		}
	});
}
