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
    getFavorites();
    
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
});

//查看用户收藏夹中的商品列表
var totalPages;
function getFavorites(pageNo){
	$.ajax({
		async:false,
		url:"../../findFavorites",
		data:{"pageNo":pageNo},
		success: function(data){
			if (data.status) {
				console.log(data);
				if(data.object!=null){
				totalPages = data.object.totalPages;
				$("#cltbox").empty();
				var html = '';
				html += '<tr><td width="60" height="50" align="center" valign="middle"><input type="checkbox" class="check-all check"><span>全选</span></td><td width="100" align="center" valign="middle">商品图片</td><td width="310" align="center" valign="middle">商品名称</td><td width="100" align="center" valign="middle">类型</td><td width="100" align="center" valign="middle">价格</td><td width="100" align="center" valign="middle">操作</td></tr>';
					if (data.object.list.length != 0) {
						for (var i=0;i<data.object.list.length;i++) {
							var image=JSON.parse(data.object.list[i].id.image)
				
							html += '<tr class="c3_padding">'+
										'<input type="hidden" value="'+data.object.list[i].id.id+'" class="id"/>'+
										'<td align="center" valign="middle">';
							         if(data.object.list[i].id.repertory!=0){
							        	 html += '<input type="checkbox" class="check-one check">';
							         }
							         html += '</td><td align="center" valign="middle"><a href="../../goods_details.html?id='+data.object.list[i].id.skuId+'">';

							            for (var int = 0; int < 1; int++) {
								         html += '<img src="'+images_path80+''+image[0]+'"'
												}
																
								 
										
											html +=' alt="'+data.object.list[i].id.skuId+'"></a></td>'+
										'<td align="left" valign="top" class="c3_description">'+
											'<a href="../../goods_details.html?id='+data.object.list[i].id.skuId+'"><span>'+data.object.list[i].id.sellPoint+'</span></a></td>'+
										'<td align="center" valign="middle">已收藏</td>'+
										'<td align="center" valign="middle" class="c3_price">￥'+data.object.list[i].id.price+'</td>'+
										'<td align="center" valign="middle" class="c3_add_cart">';
											 if(data.object.list[i].id.repertory!=0){
									        	 html += '<a href="javascript:void(0)" onclick="addCar('+data.object.list[i].id.skuId +')" target="_blank"><span>加入购物车</span></a>';
									         }
											
											 html += '<a href="javascript:void(0)" onclick="consoleClt('+data.object.list[i].id.id+')"><p>取消收藏</p></a>'+
										'</td>'+
									'</tr>'	;
						}
					}
					html += '<tr>'+
								'<td height="40" colspan="6" align="left" valign="middle" class="c3_choice">'+
									'<span><input type="checkbox" class="check-all check">全选</span>'+
									'<a href="javascript:void(0);" onclick="addCars()">加入购物车</a>'+
									'<a href="javascript:void(0);" onclick="consoleClts()">取消收藏</a>'+
								'</td>'+
							'</tr>';
					html += '<tr>'+
								'<td height="40" colspan="6" align="left" valign="middle">'+
								'<div class="paging_down">';
								
					html += '<a href="javascript:void(0);" onclick="getFavorites('+data.object.previousPageNo+')">上一页</a>';
					
					if(data.object.bottomPageNo > 8) {
						html += '<a href="javascript:void(0);" onclick="getFavorites('+data.object.topPageNo+')">'+data.object.topPageNo+'</a>';
						if((pageNo - 2) <= 1) {
							for(var n = 2; n <= (pageNo + 2); n++) {
								html += '<a href="javascript:void(0);" onclick="getFavorites('+n+')">'+n+'</a>';
							}
							html += '<span>...</span>';
						}
						if((pageNo + 2) >= data.object.bottomPageNo) {
							html += '<span>...</span>';
							for(var n = (pageNo - 2); n < data.object.bottomPageNo; n++) {
								temp_html += '<a href="javascript:void(0);" onclick="getFavorites('+n+')">'+n+'</a>';
							}
						}
						if((pageNo - 2) > 1 && (pageNo + 2) < data.object.bottomPageNo) {
							if(pageNo - 3 > 1) {
								html += '<span>...</span>';
							}
							for(var n = (pageNo - 2); n < (pageNo + 3); n++) {
								html += '<a href="javascript:void(0);" onclick="getFavorites('+n+')">'+n+'</a>';
							}
							if(pageNo + 3 < data.object.bottomPageNo) {
								html += '<span>...</span>';
							}
						}
						html += '<a href="javascript:void(0);" onclick="getFavorites('+data.object.bottomPageNo+')">'+data.object.bottomPageNo+'</a>';
					}else {
						for(var n = 1; n < data.object.bottomPageNo + 1; n++) {
							html += '<a href="javascript:void(0);" onclick="getFavorites('+n+')">'+n+'</a>';
						}
					}
					html += '<a href="javascript:void(0);" onclick="getFavorites('+data.object.nextPageNo+')">下一页</a>';
					html +=	'共'+data.object.totalPages+'页,当前第'+data.object.pageNo+'页,到第<input type="text" class="paging_down_text" onblur="checkpageNo(this)" value="'+
							data.object.pageNo+'"/>页'+
							'<input type="button" name="Submit" value="确定" class="paging_down_submit" onclick="skipPage(this)"/>';
				$("#cltbox").append(html);	
			} else{
				var div=$('.c3_main').empty();
			    var html='';
			    html+='<div class="evaluate_no">'
				+'<span>暂无评价商品</span>'
				+'<a href="../../index.html"><p>立即前往购买</p></a>'
			    +'</div>'
			    $(div).html(html);
			    html='';}
			}
			 else{
				var div=$('.c3_main').empty();
				 var html='';
				 html+='<div class="evaluate_no">'
					+'<span>暂无评价商品</span>'
					+'<a href="###"><p>立即前往购买</p></a>'
				    +'</div>'
				    $(div).html(html);
				    html='';			    }
			},
		error:function(){
			new $.flavr({
			    content      : '服务器异常',
			    closeOverlay : true,
			    closeEsc     : true
			});
		}
	});

	/**
	 * 选择框事件
	 */
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
        }
    }
}

var skuId;
//批量加入购物车
function addCars(){
	skuId  = new Array();
	var checkeds = $(":checkbox:checked.check-one");
	for (var i = 0;i<checkeds.length;i++) {
		var id = $(checkeds[i]).parent().parent().find("img").attr("alt");
		skuId.push(id);
	}
	addCarAjax(skuId);
}
//加入购物车
function addCar(id){
	skuId  = new Array();
	skuId.push(id);
	addCarAjax(skuId);
}

function addCarAjax(skuId){
	if(skuId.length==0){
		new $.flavr({
		    content      : '请先选中要添加的商品',
		});
		
	   }
	else{
	$.ajax({
		type:"post",
		url:"../../addShoppingCarts",
		data:{"skuId":skuId},
		traditional:true,
		success:function(data){
			if (data.status) {
				
				new $.flavr({
				    content     : '成功加入购物车',
				    buttons     : {
				        resize  : { text: '去购物车', style: 'danger',
				                        action: function(){
				                            window.open("../cart.html");
				                            return false;
				                        }
				        },
				        close   : {text: '继续购物'}
				   	},
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
}

var ids;
//批量取消收藏
function consoleClts(){
	ids = new Array();
	var checkeds = $(":checkbox:checked.check-one");
	if (checkeds.length==0) {
		new $.flavr({
		    content      : '请选择所要取消收藏的商品',
		    closeOverlay : true,
		    closeEsc     : true
		});
		return;
	}
 	new $.flavr({
	    content     : '确认取消收藏这些商品吗？',
	    dialog      : 'confirm',
	    onConfirm   : function(){
			for (var i = 0;i<checkeds.length;i++) {
				var id = $(checkeds[i]).parent().siblings("input").val();
				ids.push(id);
			}
			consoleCltAjax(ids);
	    },
	    onCancel    : function(){
	    },
	    closeOverlay : true,
  		closeEsc     : true
	});
}
//取消收藏
function consoleClt(id){
 	new $.flavr({
	    content     : '确认取消收藏该件商品吗？',
	    dialog      : 'confirm',
	    onConfirm   : function(){
		    ids = new Array();
			ids.push(id);
			consoleCltAjax(ids);
	    },
	    onCancel    : function(){
	    },
	    closeOverlay : true,
  		closeEsc     : true
	});
}
function consoleCltAjax(id){
	$.ajax({
		type:"post",
		url:"../../deleteFavorites",
		data:{"id":id},
		traditional:true,
		success:function(data){
			if (data.status) {
				getFavorites();
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