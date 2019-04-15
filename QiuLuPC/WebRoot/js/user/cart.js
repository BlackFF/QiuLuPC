window.onload = function () {
    if (!isLogin()) {
    	new $.flavr({
		    content     : '您当前没有登录,是否跳转登陆页面？',
		    dialog      : 'confirm',
		    onConfirm   : function(){
		        location.href = path + "/login.html?backUrl="+encodeURIComponent(location.href);
		    },
		    onCancel    : function(){
		    	$("#isLogin").show();
		    }
		});
    	return false;
    }
    
    if (!document.getElementsByClassName) {
        document.getElementsByClassName = function (cls) {
            var ret = [];
            var els = document.getElementsByTagName('*');
            for (var i = 0, len = els.length; i < len; i++) {
                if (els[i].className.indexOf(cls + ' ') >=0 || els[i].className.indexOf(' ' + cls + ' ') >=0 || els[i].className.indexOf(' ' + cls) >=0) {
                    ret.push(els[i]);
                }
            }
            return ret;
        }
    }
    getCartJson();
    var selectInputs = document.getElementsByClassName('check'); // 所有勾选框
    var checkAllInputs = document.getElementsByClassName('check-all') // 全选框
    // 点击选择框
    for(var i = 0; i < selectInputs.length; i++ ){
        selectInputs[i].onclick = function () {
            if (this.className.indexOf('check-all') >= 0) { //如果是全选，则吧所有的选择框选中
                for (var j = 0; j < selectInputs.length; j++) {
                    selectInputs[j].checked = this.checked;
                }
            }
            if (!this.checked) { //只要有一个未勾选，则取消全选框的选中状态
                for (var i = 0; i < checkAllInputs.length; i++) {
                    checkAllInputs[i].checked = false;
                }
            }
            getTotal();//选完更新总计
        }
    }
    // 默认全选
    /*checkAllInputs[0].checked = true;
    checkAllInputs[0].onclick();*/
}

function getCartJson(){
	$.ajax({
		url:"../queryShoppingCart",
		async:false,
		success:function(data){
			if (data.status) {
				if (data.object.list.length==0) {
					$("#form").hide();
					$("#no_goods").show();
					return;
				}
				$("#no_goods").hide();
				$("tbody").empty();
				var html = '';
				carCount = data.object.list.length;
				for(var i=0;i<data.object.list.length;i++){
					var image=JSON.parse(data.object.list[i].id.image);
					html+='<tr class="table_shops"><td height="118" align="center" valign="middle" class="checkbox">';
					if (data.object.list[i].id.status == 0) {
						html+='<input type="checkbox" class="check-one check">';
					} else if (data.object.list[i].id.status == 1) {
						html+='<input type="checkbox" class="check-one check" checked="checked">';
					} 
					html+='</td><td valign="middle" class="cart_img">'+
					'<a href="../goods_details.html?id='+data.object.list[i].id.skuId+'">'
						+'<img src="'+images_path80+''+image[0]+'" alt="'+data.object.list[i].id.id+'"></a>'
					    +'<a href="../goods_details.html?id='+data.object.list[i].id.skuId+'"><span class="name">'+data.object.list[i].id.sellPoint+'</span>'
					    +'</a></td><td align="center" valign="middle" class="deliveryPlace">'+
						data.object.list[i].id.deliveryPlace+
						'</td><td align="center" valign="middle" class="table_color">￥<span class="price">'+
						data.object.list[i].id.price+
						'</span></td><td align="center" valign="middle"  class="goods_number"><i class="price_add" onclick="reduce(this)"></i><input type="text" value="'+
						data.object.list[i].id.number+
						'" class="count" onblur="getTotal();getRowPrice(this);getCount(this,'+
						data.object.list[i].id.number+
						');"/><i class="price_subtract" onclick="add(this)"></i></td><td align="center" valign="middle" class="table_color">￥<span class="subtotal">'+
						data.object.list[i].id.total+
						'</span></span></td><td align="center" valign="middle" class="table_dele"><span onclick="delRow(this)">删除</span></td></tr>';
				}
				html+='<tr><td height="10" colspan="7" align="center" valign="middle" style="background: #ECECEC;"></td></tr>'+
					'<tr>'+
					    '<td align="left" valign="top" height="207" colspan="7" class="table_total">'+
						    '<div class="total_left">'+
							    '<input type="checkbox" class="check-all check" ><span class="color_black">全选</span><a href="javascript:void(0);" onclick="deleteSelect()"><span class="bottom_dele"><i class="delete_button"></i>删除选中商品</span></a>'+
							    '<a href="javascript:void(0)" onclick="returnBack()" class="to_continue">继续购物</a>'+
							'</div>'+
							'<div class="total_right">'+
								'<p>商品数量总计：<span id="selectedTotal">0</span>件</p>'+
								'<p style="color:red;">邮费：￥'+data.object.postage+'(满￥<span id="postLine">'+data.object.postLine+'</span>包邮)</p>'+
								'<input type="hidden" id="postage" value="'+data.object.postage+'"/>'+
								'<p  class="aggregate">商品金额总计：￥<span id="priceTotal">0</span></p>'+
								'<a class="settlement" onclick="gotoOrder();">立即结算</a>'+
							'</div>'+
						'</td>'+
					'</tr>'	;
				$("tbody").append(html);
				$("#form").show();
			}
		}
	});
}

function returnBack(){
	var backUrl = request("backUrl");
	if (backUrl=='') {
		window.location.href='../index.html';
	} else{
		location.href=backUrl;
	}
}
// 更新总数和总价格
function getTotal() {
	var checkeds = $(":checkbox:checked.check-one");
	var tolPrice = 0;
	var tolCount = 0;
	for (var i = 0;i<checkeds.length;i++) {
		var subtotal = $(checkeds[i]).parent().parent().find("span.price").text();
		//获取总件数
		var count = $(checkeds[i]).parent().parent().find("input.count").val();
		//获取总价
		   var tota = parseFloat(subtotal)*parseInt(count);
		   var tot= parseInt(tota*100);
		   var total=parseFloat(tot/100);
		tolPrice += total;
		tolCount += parseInt(count);
	}
	$("#priceTotal").text(tolPrice);
	$("#selectedTotal").text(tolCount);
}
//增加数量,修改单行价格
function add(obj) {
   var count = $(obj).siblings("input").val();
   var id = $(obj).parent().parent().find("img").attr("alt");
   var price = $(obj).parent().parent().find(".price").text();
   var tota = parseFloat(price)*(parseInt(count)+1);
   var tot= parseInt(tota*100);
   var total=parseFloat(tot/100);
   $.ajax({
   	type:"post",
   	url:"../updateShoppingCartNumber",
   	data:{"id":id,"number":parseInt(count)+1,"total":total},
   	async:true,
   	success:function(data){
   		if (data.status) {
			$(obj).siblings("input").val(parseInt(count)+1);
		    getRowPrice(obj);
		    getTotal();
   		} else{
   			$(obj).siblings("input").val(data.object);
   			getCount(obj);
   			var tr=$(obj).parent().parent()[0];
   			var td=tr.firstChild;
   			td.innerHTML='<input type="checkbox" class="check-one check">';
   			new $.flavr({
			    content      : data.message
			});
   		}
   	}
   });
}
//输入值后，判断取值
function getCount(obj,number){
	var count = $(obj).parent().find("input").val();
	var id = $(obj).parent().parent().find("img").attr("alt");
	var price = $(obj).parent().parent().find(".price").text();
    var tota = parseFloat(price)*(parseInt(count));
	var tot= parseInt(tota*100);
	var total=parseFloat(tot/100);
    $.ajax({
	   	type:"post",
	   	url:"../updateShoppingCartNumber",
	   	data:{"id":id,"number":count,"total":total},
	   	async:true,
	   	success:function(data){
	   		if (!data.status) {
	   			getCount(obj);
	   			$(obj).val(data.object);
	   			var tr=$(obj).parent().parent()[0];
	   			var td=tr.firstChild;
	   			td.innerHTML='<input type="checkbox" class="check-one check" onclick="getTotal();getRowPrice(obj)">';
	   			new $.flavr({
				    content : data.message
				});
	   		} 
	   		getRowPrice(obj);
		    getTotal();
	   	}
	   });
};
//减少数量,修改单行价格
function reduce(obj) {
   var count = $(obj).siblings("input").val();
   var id = $(obj).parent().parent().find("img").attr("alt");
   var price = $(obj).parent().parent().find(".price").text();
   var tota = parseFloat(price)*(parseInt(count)-1);
   var tot= parseInt(tota*100);
   var total=parseFloat(tot/100);
   if (count>1) {
	   	$.ajax({
		   	type:"post",
		   	url:"../updateShoppingCartNumber",
		   	data:{"id":id,"number":parseInt(count)-1,"total":total},
		   	async:true,
		   	success:function(data){
		   		if (data.status) {
		   		   $(obj).siblings("input").val(parseInt(count)-1);	
			       getRowPrice(obj);
			       getTotal();
		   		} else{
		   			$(obj).siblings("input").val(data.object);
		   			getCount(obj);
		   			var tr=$(obj).parent().parent()[0];
		   			var td=tr.firstChild;
		   			td.innerHTML='<input type="checkbox" class="check-one check">';
		   			new $.flavr({
					    content      : data.message
					});
		   		}
		   	}
	   });
	}
}
//计算单行价格
function getRowPrice(obj) {
   var count = $(obj).parent().find("input").val();
   if (!(/^(\+|-)?\d+$/.test( count ))||count<=0) {
   		new $.flavr({
		    content      : '请输入规范的数量',
		    closeOverlay : true,
		    closeEsc     : true
		});
		$(obj).parent().find("input").val("1");
		count = 1;
   }
   var price = $(obj).parent().parent().find(".price").text();
   var sub = parseInt(count)*parseFloat(price);
   var tot= parseInt(sub*100);
   var subtotal=parseFloat(tot/100);
   $(obj).parent().parent().find(".subtotal").text(subtotal);
}

//判断购物车是否为空
function checkIsNull(){
	if (carCount == 0) {
		$("#form").hide();
		$("#no_goods").show();
		return false;
	}
}

//删除单行
function delRow(row) {
	var id = new Array();
	id.push($(row).parent().parent().find("img").attr("alt"));
 	new $.flavr({
	    content     : '确认删除该件商品吗？',
	    dialog      : 'confirm',
	    onConfirm   : function(){
	    	$.ajax({
	    		type:"post",
	    		url:"../deleteShoppingCartById",
	    		traditional:true,
	    		async:true,
	    		data:{"id":id},
	    		success:function(data){
	    			if (data.status) {
	    				--carCount;
	    				checkIsNull();
	    				$(row).parent().parent().remove();
	    			} else{
	    				new $.flavr({
						    content      : '服务器异常',
						    closeOverlay : true,
						    closeEsc     : true
						});
	    			}
	    		}
	    	});
	    	getTotal();
	    },
	    onCancel    : function(){
	    },
	    closeOverlay : true,
  	closeEsc     : true
	});
}
//删除选中商品
function deleteSelect(){
	var checkeds = $(":checkbox:checked.check-one");
	var id = new Array();
	for (var i = 0;i<checkeds.length;i++) {
		//将选中的id拼接为一个数组
		id.push($(checkeds[i]).parent().parent().find("img").attr("alt"));
	}
	if (checkeds.length==0) {
		new $.flavr({
		    content      : '请选择所要删除的商品',
		    closeOverlay : true,
		    closeEsc     : true
		});
	} else{
		new $.flavr({
		    content     : '确认删除所选商品吗？',
		    dialog      : 'confirm',
		    onConfirm   : function(){
		    	$.ajax({
		    		type:"post",
		    		url:"../deleteShoppingCartById",
		    		traditional:true,
		    		async:true,
		    		data:{"id":id},
		    		success:function(data){
		    			if (data.status) {
		    				carCount -= checkeds.length;
		    				checkIsNull();
		    				checkeds.parent().parent().remove();
		    			} else{
		    				new $.flavr({
							    content      : '服务器异常',
							    closeOverlay : true,
							    closeEsc     : true
							});
		    			}
		    		}
		    	});
		    	getTotal();
		    },
		    onCancel    : function(){
		    },
		    closeOverlay : true,
	    	closeEsc     : true
		});
	}
};

//进入订单页面
function gotoOrder(){
	var checkeds = $(":checkbox:checked.check-one");
	if(checkeds.length==0){
		new $.flavr({
		    content      : '请选择所要购买的商品',
		    closeOverlay : true,
	    	closeEsc     : true
		});
		return false;
	}
	var order = new Object();
	var orders = new Array();
	for (var i = 0;i<checkeds.length;i++) {
		order.id = $(checkeds[i]).parent().parent().find("img").attr("alt");
		order.name = $(checkeds[i]).parent().parent().find("span.name").text();
		order.deliveryPlace = $(checkeds[i]).parent().parent().find("td.deliveryPlace").text();
		order.price = $(checkeds[i]).parent().parent().find("span.price").text();
		order.count = $(checkeds[i]).parent().parent().find("input.count").val();
		order.rowtol = $(checkeds[i]).parent().parent().find("span.subtotal").text();
		orders = orders.concat(order);
		order = new Object();
	}
	orders.postLine = $("#postLine").text();
	orders.postage = $("#postage").val();
	orders.tolprice = $("#priceTotal").text();
	orders.tolcount = $("#selectedTotal").text();
	orders.len = checkeds.length;
	removeSessionStrongParam("orders");
	saveSessionStrongParam("orders",orders);
	location.href="indent.html";
}
