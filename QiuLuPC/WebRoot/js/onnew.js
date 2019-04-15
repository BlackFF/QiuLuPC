$(document).ready(function(){

      //页眉
		$.ajax({ url : "User/isLogin",//请求地址 
			dataType : "json",//数据格式  
			type : "post",//请求方式 
			async : false,//是否异步请求 
			success : function(data){
				var tital=$('.list').empty();
				var xs='';
				xs+='<li>欢迎光临秋露商城</li>';
				if(data.status){
					xs+='<li><a href="user/personal/personal_information.html" >'+data.object.username+'</a></li>'
					   +'<li><a href="javascript:void(0)" onclick="logout()" >退出登录</a></li>';
				}
				else{
					xs+='<li><a href="login.html" >请登录</a></li>';
				}
				xs+='<li><a href="register.html" >免费注册</a></li>'
				+'<li><a href="user/personal/personal_indent.html" >我的订单</a></li>'
				+'<li><a href="###" >服务中心</a></li>'
				+'<li><a href="javascript:void(0)" onclick="location.href =\'/QiuLuPC/user/cart.html?backUrl='+encodeURIComponent(location.href)+'\';">购物车</a></li>';
				$(tital).html(xs);
				xs='';
				
			},
		})
		
		//客服服务
		callCustomerService();
		//查找上新的商品
		findOnnewSku(1);
		
		
})

//查找上新的商品
function findOnnewSku(pageNo){
	$.ajax({
		url:"Item/findSkuByOnNew",
		data:{"pageNo":pageNo},
		dataType:"json",
		type:"post",
		async : false,//是否异步请求 
		success: function(data){
			if(data.status){
				console.log(data);
				var ul=$('.products').empty();
				var temp_html='';
				
				for (var int = 0; int < data.object.list.length; int++) {
					var image=JSON.parse(data.object.list[int].image)
					temp_html+='<li>'
					+'<figure>'
					+'<a href="goods_details.html?id='+data.object.list[int].id+'"><img src="'+images_path80+''+image[0]+'"></a>'
					+'<figcaption>'
					+'<a href="goods_details.html?id='+data.object.list[int].id+'">'
					+'<span class="description">'+data.object.list[int].sellPoint+'</span>'
					+'</a>'
					+'<span class="products_prices">￥'+data.object.list[int].price+'</span>'
				    +'<span class="products_sales">销量：'+data.object.list[int].salesVolume+'件</span>'
				    +'<!--清理浮动-->'
					+'<div class="clearfix"></div>	'
					+'<div class="products_box">'
					+'<div class="products_drop">'
					+'<a href="javascript:void(0)" onclick="checkNum('+data.object.list[int].id+','+data.object.list[int].price+','+data.object.list[int].repertory+')"><span class="products_cart">加入购物车</span></a>'
					+'<span>仅剩'+data.object.list[int].repertory+'件</span>'
					+'<a href="javascript:void(0)" onclick="addFavorites('+data.object.list[int].id+')"><span class="collection"><i></i>收藏</span></a>'
					+'</div></div></figcaption>'
					+'<p class="products_p">'
					+'已收藏商品'
					+'</p>'
					+'</figure>'
			        +'</li>';
				}
				 $(ul).html(temp_html);
				 temp_html = '';
				 var div=$('.paging_down').empty();
			  	temp_html +='<form>'
					+'<a href="javascript:void(0)" onclick="findOnnewSku('+data.object.previousPageNo+')">上一页</a>';
					
			        if(data.object.bottomPageNo>8){	  
			        	 temp_html +='<a href="javascript:void(0)" onclick="findOnnewSku('+data.object.topPageNo+')">'+data.object.topPageNo+'</a>';
			           if((pageNo-2)<=1){
			        for(var n=2;n<=(pageNo+2);n++){
			        	  temp_html +='<a href="javascript:void(0)" onclick="findOnnewSku('+n+')">'+n+'</a>';
			        }
			        temp_html +='<span>...</span>';
			         }
			         if((pageNo+2)>=data.object.bottomPageNo){
			        	 temp_html +='<span>...</span>';
			        	for(var n=(pageNo-2);n<data.object.bottomPageNo;n++){
				        	  temp_html +='<a href="javascript:void(0)" onclick="findOnnewSku('+n+')">'+n+'</a>';
				        }
			        }
			        if((pageNo-2)>1&&(pageNo+2)<data.object.bottomPageNo){
			        	if(pageNo-3>1){temp_html +='<span>...</span>';}
			        	for(var n=(pageNo-2);n<(pageNo+3);n++){
				        	  temp_html +='<a href="javascript:void(0)" onclick="findOnnewSku('+n+')">'+n+'</a>';
				        }
			        	if(pageNo+3<data.object.bottomPageNo)
			        	{temp_html +='<span>...</span>';}
			        }
			        temp_html +='<a href="javascript:void(0)" onclick="findOnnewSku('+data.object.bottomPageNo+')">'+data.object.bottomPageNo+'</a>';
			           }
			        if(data.object.bottomPageNo<=8){
			        	 for(var n=1;n<data.object.bottomPageNo+1;n++){
				        	  temp_html +='<a href="javascript:void(0)" onclick="findOnnewSku('+n+')">'+n+'</a>';
				        }
			        }
			        temp_html +='<a href="javascript:void(0)" onclick="findOnnewSku('+data.object.nextPageNo+')">下一页</a>'
					+'共'+data.object.totalPages+'页,'
				
					+'到第<input type="text" id="pageNoNum" "name="pageNo" class="paging_down_text" />页'
					+'<input type="button" name="Submit" value="确定" class="paging_down_submit" onclick="forminput('+data.object.bottomPageNo+')"/>'
					+'</form>';
				 	$(div).html(temp_html);
				 	temp_html = '';
				
			}
		},
		
	})
	
	
}



//类目 搜索时 页面跳转
function forminput(total){
    var domInput = document.getElementById('pageNoNum');
	var x=domInput.value;
	   var re = /^[1-9]\d*$/;
     if (!re.test(x))
   {
    	 new $.flavr({
			    content     : '请输入正整数',
			    
			});
 
   }
    else{ 
    	 if(x>total){
    		 new $.flavr({
 			    content     : '请输入正确的范围',
 			    
 			});
	                }
	    else{
	window.location.href='onnew.html';
	        }
       }
}


//检查库存量是否满足
function checkNum(id,price,repertory){
	  if(repertory<1){
		  new $.flavr({
			    content     : '库存不足,去看看其它商品吧',
			    
			});
	  }
	  else{
		  addShoppingCart(id,price);
	  }
}
//加入购物车
function addShoppingCart(id,price){
	  var total=price;
    $.ajax({
    	url:'addShoppingCart',
    	data:{"skuId":id,"number":1,"total":total},
    	async:false,
    	datatype:"json",
    	type:"post",
    	success:function(data){
    		if(data.status)
    			new $.flavr({
    			    content     : '成功加入购物车',
    			    buttons     : {
    			        resize  : { text: '去购物车', style: 'danger',
    			                        action: function(){
    			                        	//location.href = path + "/login.html?backUrl="+encodeURIComponent(location.href);
    			                        	//window.location.href='user/cart.html';
    			                            window.open("user/cart.html");
    			                            return false;
    			                        }
    			        },
    			        close   : {text: '继续购物'}
    			   	},
    			    closeOverlay : true,
    		    	closeEsc     : true
    			});
    		else{
    			new $.flavr({
    			    content     : '您当前没有登录,是否跳转登陆页面？',
    			    dialog      : 'confirm',
    			    onConfirm   : function(){
    			        location.href = path + "/login.html?backUrl="+encodeURIComponent(location.href);
    			    },
    			    onCancel    : function(){
    			    }
    			});
    		}
    	},
    	})
      }

//加入收藏夹
 function addFavorites(skuId){
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
    	    }
    	 else{
    		 $.ajax({
    				url:"addFavorites",
    				data:{"skuId":skuId},
    			    datatype:"json",
    		    	async:false,
    			    type:"post",
    			    success:function(data){
    			    	if(data.status)
    			    		new $.flavr({
    		    			    content     : '加入收藏夹成功',
    		    			    
    		    			});
    			    		else{
    			    			new $.flavr({
        		    			    content     : data.message,
        		    			    
        		    			});
    			    		}
    			    }
    			})
    	 }
	
}

 function checkCollect(skuId,t){
		$.ajax({ url : "queryFavoritesBySkuId",//请求地址 
			dataType : "json",//数据格式  
			data:{"skuId":skuId},
			type : "post",//请求方式 
			async : true,//是否异步请求 
			context:t,
			success : function(data){
			   if(data.status){
				  
				  $(this).next().next().show();
				  
			   }
			   //alert(data.message)
			},
		})
	}

	function checkCollectBack(){
		  $('.products_p').hide();
	}

