var type="0";
var storage;
var userID;
$(document).ready(function(){
	
	$.ajax({ url : "User/isLogin",//请求地址 
		dataType : "json",//数据格式  
		type : "post",//请求方式 
		async : false,//是否异步请求 
		success : function(data){
			if(data.status){
				userID=data.object.id;
			}
			else{
				userID="tourist";
			}
		},
	})
	
	function getLocalStorage() {
		if(typeof localStorage == "object") {
			return localStorage;
		} else if(typeof globalStorage == "object") {
			return globalStorage[location.host];
		} else {
			throw new Error("Local storage not availbale.");
		}
	}

	storage = getLocalStorage();
var sellPoint=getSessionStrongParam("sellPoint");
var pageNo1=request("pageNo");
if(pageNo1==""){
	pageNo1=1;
}

else{
	
	   var re = /^[1-9]\d*$/;
	   if (!re.test(pageNo1))
	   {
	    	 new $.flavr({
				    content     : '链接参数异常',
				    
				});
	 
	    	 window.location.href='index.html';
	   }
}
if(pageNo1!=null){
var pageNo=parseInt(pageNo1);
}

//显示当前类目的路径
tree(sellPoint);
	


//根据卖点搜索商品
$.ajax({ url : "search/searchItem",//请求地址 
	dataType : "json",//数据格式  
	data:{"sellPoint":sellPoint,"pageNo":pageNo},
	type : "post",//请求方式 
	async : false,//是否异步请求 
	success : function(data){
		select(data,sellPoint,pageNo);
		all(data,sellPoint,pageNo);
	},
		})
		
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
					xs+='<li><a href="user/personal/personal_information.html" >'+data.object.username+'</a></li>';
				}
				else{
					xs+='<li><a href="login.html" >请登录</a></li>';
				}
				xs+='<li><a href="register.html" >免费注册</a></li>'
				+'<li><a href="user/personal/personal_indent.html" >我的订单</a></li>'
				+'<li><a href="###" >服务中心</a></li>'
				+'<li><a href="user/cart.html" >购物车</a></li>';
				$(tital).html(xs);
				xs='';
				
			},
		})
		//客服服务
		callCustomerService();
       //销量排行榜		
        $.ajax({ url : "Item/findSkuBysalesVolume",//请求地址 
					dataType : "json",//数据格式  
					type : "post",//请求方式 
					async : false,//是否异步请求 
					success : function(data){
					findSkuBysalesVolume(data);
					},
				})
				
				
				
		//浏览历史
	    getRecentlyViewedSKU(userID);
	
		
		
})


//销量排行榜		
function findSkuBysalesVolume(data){
	if(data.status){
		console.log(data)
		//var div=$('.browse').empty();
		var div=$('.browse ul:nth-of-type(2)').empty();
		html_top='';
		for (var int = 0; int < data.object.length; int++) {
			var image=JSON.parse(data.object[int].image)
			html_top+='<li>'
				+'<figure>'
				+'<a href="goods_details.html?id='+data.object[int].id+'"><img src="'+images_path80+''+image[0]+'"></a>'
				+'<figcaption>'
				+'<a href="goods_details.html?id='+data.object[int].id+'" class="underline">'
				+'<span class="character">'+data.object[int].sellPoint+'</span>'
						+'</a>'
						+'<span class="products_prices">'+data.object[int].price+'</span>'
					
							+'</figcaption>'
							+'</figure>'
							+'</li>';
		}
		$(div).html(html_top);
		html_top='';
		
	}
}
//显示当前类目的路径
function tree(sellPoint){

	var div=$('.list_title').empty();
	var temp='';
	
	temp+='<a href="###"><span>全部<i class="ico_right"></i></span></a>'

		
	    +'<a href="search_goods.html?&pageNo=1">'
		+'<span  class="list_title_drop">'+sellPoint+'<i class="ico_bottom ico_top"></i></span>'
		+'</a>'
		+ '<ul class="list_title_open">'
	    +'</ul>';
	
	$(div).html(temp);
 	temp = '';
	
}
//搜索条件
function select(data,sellPoint,pageNo){
	if(data.status){
		var sp=sellPoint;
		var select=$('.class_good').empty();
		s_html='';
		s_html+='<h1 id="goodNum">商品('+data.object.totalRecords+')</h1>'
		+'<div class="good_choose">'
		+'<div class="good_choose_a">'
		+'<a href="javascript:void(0)" onclick="sort(\''+sp+'\','+1+');changeStyle(this)">人气<i class="arrows_black"></i></a>'
		/*+'<a href="javascript:void(0)">新品<i class="arrows_black"></i></a>'*/
		+'<a href="javascript:void(0)" onclick="sort1(\''+sp+'\','+1+');changeStyle(this)">销量<i class="arrows_black"></i></a>'
		/*+'<a href="javascript:void(0)">折扣<i class="arrows_black"></i></a>'*/
		+'<a href="javascript:void(0)" id="change" onclick="priceType(\''+sp+'\','+1+');changeStyle(this);" value="0">价格<i class="arrows_black"></i></a>'
		+'</div>'
		+'<form  id="forms" ">'
		+'<div class="price">'
		+'<input type="text" id="min" name="min" placeholder="￥" />--<input type="text" id="max" name="max" placeholder="￥" />'
		+'<input type="hidden" name="id" value="'+sellPoint+'">'
		+'<input type="hidden" id="type" name="type" value="'+type+'">'
		+'<input type="hidden" id="pageNo" name="pageNo" value="'+pageNo+'">'
		+'</div>'
		+'<div class="choice">'
		+'<input type="checkbox"><span>	新到商品</span>'
		+'<input type="checkbox"><span>	促销商品</span>'
		+'</div>'
		+'<div class="good_search">'
		+'<input type="button" name="Submit" value="确定" onclick="submitForm(\''+sp+'\','+1+')" class="search_submit"/>'
		+'</div>'
		+'</form>'
		+'</div>';
		$(select).html(s_html);
		
		
	}
}


//提交表单 ----联合条件搜索商品
function submitForm(sellPoint,pageNo){
	var type= document.getElementById("type").value;
	var min= document.getElementById("min").value;
	var max= document.getElementById("max").value;
	
	var re = /^[1-9]\d{0,6}|0$/;
	   if (!((min==""||re.test(min))&&(max==""||re.test(max))))
	   {
	    	 new $.flavr({
				    content     : '请输入小于8位的正整数',
				    
				});
	 
	   }
	    else{ 
	    	if((min==""&&max=="")){
	    	 if(min>max){
	    		 new $.flavr({
	 			    content     : '请输入正确的范围',
	 			    
	 			});
		                }
	    	    }
		    else{
		    	
		    	 $.ajax({ url : "search/findItemByTypeByPrice",//请求地址 
		 			dataType : "json",//数据格式  
		 			data:{"sellPoint":sellPoint,"type":type,"max":max,"min":min,"pageNo":pageNo},
		 			type : "post",//请求方式 
		 			async : false,//是否异步请求 
		 			context :type,
		 			success : function(data,t){
		                  
		 				 //如何发送成功
		 				if(data.status){
		 					//改变商品的数量
		 				changGoodsNum(data.object.totalRecords);
		 			     var ol=$('.products').empty();
		 				// var div=('.paging_down').enpty();
		 				 var temp_html = '';
		 				 for(var i=0;i<data.object.list.length;i++) {
		 					 
		 					 if(type==2){
		 	                	  var u=data.object.list[i].id;
		 	                	  
		 	                  }
		 					 else{
		 						  var u=data.object.list[i];
		 					 }
		 					 
		 					 var image=JSON.parse(u.image)
		 					 temp_html += '<li>'
		 						 +'<figure>'
		 						 +'<a href="goods_details.html?id='+u.id+'" onmouseover=checkCollect('+u.id+')  onmouseout="checkCollectBack()">';
		 					 for (var int = 0; int <1; int++) {
		 						 temp_html += '<img src="'+images_path240+''+image[0]+'"></a>';
		 											}
		 						 temp_html +='<figcaption>'
		 						 +'	<a href="goods_details.html?id='+u.id+'">'
		 						 +'<span class="description">'+u.sellPoint+'</span>'
		 						 +'</a>'
		 						 +'	<span class="products_prices">￥'+u.price+'</span>'
		 						 +'	<span class="products_sales">'
		 						 +'	销量：'+u.salesVolume+'件'
		 									+'</span>'
		 									+'<!--清理浮动-->'
		 									+'<div class="clearfix"></div>	'
		 									+'<div class="products_box">'
		 									+'<div class="products_drop">'
		 									+'<a href="javascript:void(0)" onclick="checkNum('+u.id+','+u.price+','+u.repertory+')"><span class="products_cart">加入购物车</span></a>'
		 									+'<span>仅剩'+data.object.list[i].repertory+'件</span>'
		 									+'<a href="javascript:void(0)" onclick="addFavorites('+u.id+')"><span class="collection"><i></i>收藏</span></a>'
		 									+'</div></div></figcaption>'
		 									+'<p class="products_p">'
		 									+'已收藏商品'
		 									+'</p>'
		 									+'</figure>'
		 							+'</li>';
		 					}
		 			        temp_html += '<div class="paging_down">'
		 			        +'<form>'
		 					+'<a href="javascript:void(0)" onclick="submitForm(\''+sellPoint+'\','+data.object.previousPageNo+');goTop();">上一页</a>';
		 					
		 			        if(data.object.bottomPageNo>8){	  
		 			        	 temp_html +='<a href="javascript:void(0)" onclick="submitForm(\''+sellPoint+'\','+data.object.topPageNo+');goTop();">'+data.object.topPageNo+'</a>';
		 			           if((pageNo-2)<=1){
		 			        for(var n=2;n<=(pageNo+2);n++){
		 			        	  temp_html +='<a href="javascript:void(0)" onclick="submitForm(\''+sellPoint+'\','+n+');goTop();">'+n+'</a>';
		 			        }
		 			        temp_html +='<span>...</span>';
		 			         }
		 			         if((pageNo+2)>=data.object.bottomPageNo){
		 			        	 temp_html +='<span>...</span>';
		 			        	for(var n=(pageNo-2);n<data.object.bottomPageNo;n++){
		 				        	  temp_html +='<a href="javascript:void(0)" onclick="submitForm(\''+sellPoint+'\','+n+');goTop();">'+n+'</a>';
		 				        }
		 			        }
		 			        if((pageNo-2)>1&&(pageNo+2)<data.object.bottomPageNo){
		 			        	if(pageNo-3>1){temp_html +='<span>...</span>';}
		 			        	for(var n=(pageNo-2);n<(pageNo+3);n++){
		 				        	  temp_html +='<a href="javascript:void(0)" onclick="submitForm(\''+sellPoint+'\','+n+');goTop();">'+n+'</a>';
		 				        }
		 			        	if(pageNo+3<data.object.bottomPageNo)
		 			        	{temp_html +='<span>...</span>';}
		 			        }
		 			        temp_html +='<a href="javascript:void(0)" onclick="submitForm(\''+sellPoint+'\','+data.object.bottomPageNo+');goTop();">'+data.object.bottomPageNo+'</a>';
		 			           }
		 			        if(data.object.bottomPageNo<=8){
		 			        	 for(var n=1;n<data.object.bottomPageNo+1;n++){
		 				        	  temp_html +='<a href="javascript:void(0)" onclick="submitForm(\''+sellPoint+'\','+n+');goTop();">'+n+'</a>';
		 				        }
		 			        }
		 			        temp_html +='<a href="javascript:void(0)" onclick="submitForm(\''+sellPoint+'\','+data.object.nextPageNo+');goTop();">下一页</a>'
		 					+'共'+data.object.totalPages+'页,'
		 				
		 					+'到第<input type="text" id="pageNoSub" "name="pageNoNum" value="'+pageNo+'" class="paging_down_text" />页'
		 					+'<input type="button" name="Submit" value="确定" class="paging_down_submit" onclick="submitFormJump(\''+sellPoint+'\','+data.object.bottomPageNo+');goTop();"/>'
		 					+'</form></div>';
		 				 	$(ol).html(temp_html);
		 				 	temp_html = '';
		 				}
		 				 if(data.status){
		 					 if(data.object.list.length==0){
		 						 
		 						   $('.products').empty();
		 						   $('.without_goods').show();
		 					 }
		 					
		 				  }
		 				 else{
		 					   $('.products').empty();
	 						   $('.without_goods').show();
		 				 }
		 			  
		 			},
		 				})
	    	}
    }
	
	  
} 
  //联合条件搜索商品---页面刷新
  function submitFormJump(sellPoint,total){
	  var domInput = document.getElementById('pageNoSub');
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
			    	
			    	submitForm(sellPoint,x);
		        }
	       }
  }

//排序方式（人气）
   function sort(sellPoint,pageNo){
	   $('.without_goods').hide();
	   document.getElementById('min').value=""; 
	   document.getElementById('max').value=""; 
	   document.getElementById('type').value="2"; 
	   $.ajax({ url : "search/queryItemBySellPointAndFavrites",//请求地址 
			dataType : "json",//数据格式  
			data:{"sellPoint":sellPoint,"pageNo":pageNo},
			type : "post",//请求方式 
			async : false,//是否异步请求 
			success : function(data){
			//select(data,sellPoint,pageNo);
			
				 //如何发送成功
				if(data.status){
			     var ol=$('.products').empty();
				// var div=('.paging_down').enpty();
				 var temp_html = '';
				 for(var i=0;i<data.object.list.length;i++) {
					 var image=JSON.parse(data.object.list[i].id.image)
					 temp_html += '<a name="jump"></a>'
						 +'<li>'
						 +'<figure>'
						 +'<a href="goods_details.html?id='+data.object.list[i].id.id+'" onmouseover=checkCollect('+data.object.list[i].id.id+')  onmouseout="checkCollectBack()">';
					 for (var int = 0; int <1; int++) {
						 temp_html += '<img src="'+images_path240+''+image[0]+'"></a>';
											}
						 temp_html +='<figcaption>'
						 +'	<a href="goods_details.html?id='+data.object.list[i].id.id+'">'
						 +'<span class="description">'+data.object.list[i].id.sellPoint+'</span>'
						 +'</a>'
						 +'	<span class="products_prices">￥'+data.object.list[i].id.price+'</span>'
						 +'	<span class="products_sales">'
						 +'	销量：'+data.object.list[i].id.salesVolume+'件'
									+'</span>'
									+'<!--清理浮动-->'
									+'<div class="clearfix"></div>	'
									+'<div class="products_box">'
									+'<div class="products_drop">'
									+'<a href="javascript:void(0)" onclick="checkNum('+data.object.list[i].id.id+','+data.object.list[i].id.price+','+data.object.list[i].id.repertory+')"><span class="products_cart">加入购物车</span></a>'
									+'<span>仅剩'+data.object.list[i].id.repertory+'件</span>'
									+'<a href="javascript:void(0)" onclick="addFavorites('+data.object.list[i].id.id+')"><span class="collection"><i></i>收藏</span></a>'
									+'</div></div></figcaption>'
									+'<p class="products_p">'
									+'已收藏商品'
									+'</p>'
									+'</figure>'
							        +'</li>';
					}
			        temp_html += '<div class="paging_down">'
			        +'<form>'
					+'<a href="javascript:void(0)" onclick="sort(\''+sellPoint+'\','+data.object.previousPageNo+');goTop();">上一页</a>';
					
			        if(data.object.bottomPageNo>8){	  
			        	 temp_html +='<a href="javascript:void(0)" onclick="sort(\''+sellPoint+'\','+data.object.topPageNo+');goTop();">'+data.object.topPageNo+'</a>';
			           if((pageNo-2)<=1){
			        for(var n=2;n<=(pageNo+2);n++){
			        	  temp_html +='<a href="javascript:void(0)" onclick="sort(\''+sellPoint+'\','+n+');goTop();">'+n+'</a>';
			        }
			        temp_html +='<span>...</span>';
			         }
			         if((pageNo+2)>=data.object.bottomPageNo){
			        	 temp_html +='<span>...</span>';
			        	for(var n=(pageNo-2);n<data.object.bottomPageNo;n++){
				        	  temp_html +='<a href="javascript:void(0)" onclick="sort(\''+sellPoint+'\','+n+');goTop();">'+n+'</a>';
				        }
			        }
			        if((pageNo-2)>1&&(pageNo+2)<data.object.bottomPageNo){
			        	if(pageNo-3>1){temp_html +='<span>...</span>';}
			        	for(var n=(pageNo-2);n<(pageNo+3);n++){
				        	  temp_html +='<a href="javascript:void(0)" onclick="sort(\''+sellPoint+'\','+n+');goTop();">'+n+'</a>';
				        }
			        	if(pageNo+3<data.object.bottomPageNo)
			        	{temp_html +='<span>...</span>';}
			        }
			        temp_html +='<a href="javascript:void(0)" onclick="sort(\''+sellPoint+'\','+data.object.bottomPageNo+');goTop();">'+data.object.bottomPageNo+'</a>';
			           }
			        if(data.object.bottomPageNo<=8){
			        	 for(var n=1;n<data.object.bottomPageNo+1;n++){
				        	  temp_html +='<a href="javascript:void(0)" onclick="sort(\''+sellPoint+'\','+n+');goTop();">'+n+'</a>';
				        }
			        }
			        temp_html +='<a href="#jump" onclick="sort(\''+sellPoint+'\','+data.object.nextPageNo+');goTop();">下一页</a>'
					+'共'+data.object.totalPages+'页,'
				
					+'到第<input type="text" id="pageNoNum" "name="pageNo" value="'+pageNo+'" class="paging_down_text" />页'
					+'<input type="button" name="Submit" value="确定" class="paging_down_submit" onclick="sortJump(\''+sellPoint+'\','+data.object.bottomPageNo+');goTop();"/>'
					+'</form></div>';
				 	$(ol).html(temp_html);
				 	temp_html = '';
				}
				if(data.status){
					 if(data.object.list.length==0){
						 
						   $('.products').empty();
						   $('.without_goods').show();
					 }
					
				  }
				 else{
					   $('.products').empty();
					   $('.without_goods').show();
				 }
			},
				})
   }
   //人气---输入页面跳转
   function sortJump(sellPoint,total){
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
			    	 
		                  sort(sellPoint,x);
		             }
	       }
   }
 //排序方式（销量）
   function sort1(sellPoint,pageNo){
	   $('.without_goods').hide();
	   document.getElementById('min').value=""; 
	   document.getElementById('max').value=""; 
	   document.getElementById('type').value="1"; 
	   $.ajax({ url : "search/queryItemBySellPointAndRepertory",//请求地址 
			dataType : "json",//数据格式  
			data:{"sellPoint":sellPoint,"pageNo":pageNo},
			type : "post",//请求方式 
			async : false,//是否异步请求 
			success : function(data){

				//select(data,sellPoint,pageNo);
				
				
				 //如何发送成功
				if(data.status){
					
			     var ol=$('.products').empty();
				// var div=('.paging_down').enpty();
				 var temp_html = '';
				 for(var i=0;i<data.object.list.length;i++) {
					 var image=JSON.parse(data.object.list[i].image)
					 temp_html += '<li>'
						 +'<figure>'
						 +'<a href="goods_details.html?id='+data.object.list[i].id+'" onmouseover=checkCollect('+data.object.list[i].id+')  onmouseout="checkCollectBack()">';
					 for (var int = 0; int <1; int++) {
						 temp_html += '<img src="'+images_path240+''+image[0]+'"></a>';
											}
						 temp_html +='<figcaption>'
						 +'	<a href="goods_details.html?id='+data.object.list[i].id+'">'
						 +'<span class="description">'+data.object.list[i].sellPoint+'</span>'
						 +'</a>'
						 +'	<span class="products_prices">￥'+data.object.list[i].price+'</span>'
						 +'	<span class="products_sales">'
						 +'	销量：'+data.object.list[i].salesVolume+'件'
									+'</span>'
									+'<!--清理浮动-->'
									+'<div class="clearfix"></div>	'
									+'<div class="products_box">'
									+'<div class="products_drop">'
									+'<a href="javascript:void(0)" onclick="checkNum('+data.object.list[i].id+','+data.object.list[i].price+','+data.object.list[i].repertory+')"><span class="products_cart">加入购物车</span></a>'
									+'<span>仅剩'+data.object.list[i].repertory+'件</span>'
									+'<a href="javascript:void(0)" onclick="addFavorites('+data.object.list[i].id+')"><span class="collection"><i></i>收藏</span></a>'
									+'</div></div></figcaption>'
									+'<p class="products_p">'
									+'已收藏商品'
									+'</p>'
									+'</figure>'
							+'</li>';
					}
			        temp_html += '<div class="paging_down">'
			        +'<form>'
					+'<a href="javascript:void(0)" onclick="sort1(\''+sellPoint+'\','+data.object.previousPageNo+');goTop();" >上一页</a>';
					
			        if(data.object.bottomPageNo>8){	  
			        	 temp_html +='<a href="javascript:void(0)" onclick="sort1(\''+sellPoint+'\','+data.object.topPageNo+');goTop();">'+data.object.topPageNo+'</a>';
			           if((pageNo-2)<=1){
			        for(var n=2;n<=(pageNo+2);n++){
			        	  temp_html +='<a href="javascript:void(0)" onclick="sort1(\''+sellPoint+'\','+n+');goTop();">'+n+'</a>';
			        }
			        temp_html +='<span>...</span>';
			         }
			         if((pageNo+2)>=data.object.bottomPageNo){
			        	 temp_html +='<span>...</span>';
			        	for(var n=(pageNo-2);n<data.object.bottomPageNo;n++){
				        	  temp_html +='<a href="javascript:void(0)" onclick="sort1(\''+sellPoint+'\','+n+');goTop();">'+n+'</a>';
				        }
			        }
			        if((pageNo-2)>1&&(pageNo+2)<data.object.bottomPageNo){
			        	if(pageNo-3>1){temp_html +='<span>...</span>';}
			        	for(var n=(pageNo-2);n<(pageNo+3);n++){
				        	  temp_html +='<a href="javascript:void(0)" onclick="sort1(\''+sellPoint+'\','+n+');goTop();">'+n+'</a>';
				        }
			        	if(pageNo+3<data.object.bottomPageNo)
			        	{temp_html +='<span>...</span>';}
			        }
			        temp_html +='<a href="javascript:void(0)" onclick="sort1(\''+sellPoint+'\','+data.object.bottomPageNo+');goTop();">'+data.object.bottomPageNo+'</a>';
			           }
			        if(data.object.bottomPageNo<=8){
			        	 for(var n=1;n<data.object.bottomPageNo+1;n++){
				        	  temp_html +='<a href="javascript:void(0)" onclick="sort1(\''+sellPoint+'\','+n+');goTop();">'+n+'</a>';
				        }
			        }
			        temp_html +='<a href="javascript:void(0)" onclick="sort1(\''+sellPoint+'\','+data.object.nextPageNo+');goTop();">下一页</a>'
					+'共'+data.object.totalPages+'页,'
				
					+'到第<input type="text" id="pageNoSort1" "name="pageNo" value="'+pageNo+'" class="paging_down_text" />页'
					+'<input type="button" name="Submit" value="确定" class="paging_down_submit" onclick="sort1Jump(\''+sellPoint+'\','+data.object.bottomPageNo+');goTop();"/>'
					+'</form></div>';
				 	$(ol).html(temp_html);
				 	temp_html = '';
				}
				if(data.status){
					 if(data.object.list.length==0){
						 
						   $('.products').empty();
						   $('.without_goods').show();
					 }
					
				  }
				 else{
					   $('.products').empty();
					   $('.without_goods').show();
				 }
			  
			},
				})
   }
   
   //销量---页面跳转
   function sort1Jump(sellPoint,total){
	   var domInput = document.getElementById('pageNoSort1');
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
		             sort1(sellPoint,x);
		        }
	       }
   }
 //确定排序方式（）
   function priceType(sellPoint,pageNo){
	   document.getElementById('min').value=""; 
	   document.getElementById('max').value=""; 
	   var va=$("#change").attr("value");
	   if(va==0){
		   document.getElementById('type').value="3"; 
		   var url="search/findSkuByPriceAsc";
		  document.getElementById('change').value="1";
		  document.getElementById('change').innerHTML="价格由低到高";
	   }
	   else{
		   document.getElementById('type').value="4";
		   var url="search/findSkuByPriceDesc";
	       document.getElementById('change').value="0";
	       document.getElementById('change').innerHTML="价格由高到低";
	     }
	     sort2(sellPoint,pageNo,url);
   }
   
 //排序方式（价格）
   function sort2(sellPoint,pageNo,url){
	   $('.without_goods').hide();
	   $.ajax({ 
		   url : url,//请求地址 
		   dataType : "json",//数据格式  
		   data:{"sellPoint":sellPoint,"pageNo":pageNo},
		   type : "post",//请求方式 
		   async : false,//是否异步请求 
		   success : function(data){
			   
			   //如何发送成功
			   if(data.status){
				   var ol=$('.products').empty();
				   // var div=('.paging_down').enpty();
				   var temp_html = '';
				   for(var i=0;i<data.object.list.length;i++) {
					   var image=JSON.parse(data.object.list[i].image)
					   temp_html += '<li>'
						   +'<figure>'
						   +'<a href="goods_details.html?id='+data.object.list[i].id+'" onmouseover=checkCollect('+data.object.list[i].id+')  onmouseout="checkCollectBack()">';
					   for (var int = 0; int <1; int++) {
						   temp_html += '<img src="'+images_path240+''+image[0]+'"></a>';
					   }
					   temp_html +='<figcaption>'
						   +'	<a href="goods_details.html?id='+data.object.list[i].id.id+'">'
						   +'<span class="description">'+data.object.list[i].sellPoint+'</span>'
						   +'</a>'
						   +'	<span class="products_prices">￥'+data.object.list[i].price+'</span>'
						   +'	<span class="products_sales">'
						   +'	销量：'+data.object.list[i].salesVolume+'件'
						   +'</span>'
						   +'<!--清理浮动-->'
						   +'<div class="clearfix"></div>	'
						   +'<div class="products_box">'
						   +'<div class="products_drop">'
						   +'<a href="javascript:void(0)" onclick="checkNum('+data.object.list[i].id+','+data.object.list[i].price+','+data.object.list[i].repertory+')"><span class="products_cart">加入购物车</span></a>'
						   +'<span>仅剩'+data.object.list[i].repertory+'件</span>'
						   +'<a href="javascript:void(0)" onclick="addFavorites('+data.object.list[i].id+')"><span class="collection"><i></i>收藏</span></a>'
						   +'</div></div></figcaption>'
						   +'<p class="products_p">'
						   +'已收藏商品'
						   +'</p>'
						   +'</figure>'
						   +'</li>';
				   }
				   temp_html += '<div class="paging_down">'
					   +'<form>'
					   +'<a href="javascript:void(0)" onclick="sort2(\''+sellPoint+'\','+data.object.previousPageNo+',\''+url+'\')">上一页</a>';
				   
				   if(data.object.bottomPageNo>8){	  
					   temp_html +='<a href="javascript:void(0)" onclick="sort2(\''+sellPoint+'\','+data.object.topPageNo+',\''+url+'\')">'+data.object.topPageNo+'</a>';
					   if((pageNo-2)<=1){
						   for(var n=2;n<=(pageNo+2);n++){
							   temp_html +='<a href="javascript:void(0)" onclick="sort2(\''+sellPoint+'\','+n+',\''+url+'\')">'+n+'</a>';
						   }
						   temp_html +='<span>...</span>';
					   }
					   if((pageNo+2)>=data.object.bottomPageNo){
						   temp_html +='<span>...</span>';
						   for(var n=(pageNo-2);n<data.object.bottomPageNo;n++){
							   temp_html +='<a href="javascript:void(0)" onclick="sort2(\''+sellPoint+'\','+n+',\''+url+'\')">'+n+'</a>';
						   }
					   }
					   if((pageNo-2)>1&&(pageNo+2)<data.object.bottomPageNo){
						   if(pageNo-3>1){temp_html +='<span>...</span>';}
						   for(var n=(pageNo-2);n<(pageNo+3);n++){
							   temp_html +='<a href="javascript:void(0)" onclick="sort2(\''+sellPoint+'\','+n+',\''+url+'\')">'+n+'</a>';
						   }
						   if(pageNo+3<data.object.bottomPageNo)
						   {temp_html +='<span>...</span>';}
					   }
					   temp_html +='<a href="javascript:void(0)" onclick="sort2(\''+sellPoint+'\','+data.object.bottomPageNo+',\''+url+'\')">'+data.object.bottomPageNo+'</a>';
				   }
				   if(data.object.bottomPageNo<=8){
					   for(var n=1;n<data.object.bottomPageNo+1;n++){
						   temp_html +='<a href="javascript:void(0)" onclick="sort2(\''+sellPoint+'\','+n+',\''+url+'\')">'+n+'</a>';
					   }
				   }
				   temp_html +='<a href="javascript:void(0)" onclick="sort2(\''+sellPoint+'\','+data.object.nextPageNo+',\''+url+'\')">下一页</a>'
				   +'共'+data.object.totalPages+'页,'
				   
				   +'到第<input type="text" id="pageNoSort2" "name="pageNo" value="'+pageNo+'" class="paging_down_text" />页'
				   +'<input type="button" name="Submit" value="确定" class="paging_down_submit" onclick="sort2Jump(\''+sellPoint+'\','+data.object.bottomPageNo+',\''+url+'\')"/>'
				   +'</form></div>';
				   $(ol).html(temp_html);
				   temp_html = '';
			   }
			   if(data.status){
					 if(data.object.list.length==0){
						 
						   $('.products').empty();
						   $('.without_goods').show();
					 }
					
				  }
				 else{
					   $('.products').empty();
					   $('.without_goods').show();
				 }
			   
		   },
	   })
   }
//价格---页面跳转
   function sort2Jump(sellPoint,total,url){
	   var domInput = document.getElementById('pageNoSort2');
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
		    sort2(sellPoint,x,url);
		        }
	       }
   }
   
   
//类目 搜索时 页面拼接  及  排序
  function all(data,sellPoint,pageNo){
	  $('.without_goods').hide();
	 //如何发送成功
	if(data.status){
    var ol=$('.products').empty();
	// var div=('.paging_down').enpty();
	 var temp_html = '';
	 for(var i=0;i<data.object.list.length;i++) {
		 var image=JSON.parse(data.object.list[i].image)
		 temp_html += '<li>'
			 +'<figure>'
			 +'<a href="goods_details.html?id='+data.object.list[i].id+'" onmouseover="checkCollect('+data.object.list[i].id+',this)"  onmouseout="checkCollectBack()">';
		 for (var int = 0; int <1; int++) {
			 temp_html += '<img src="'+images_path240+''+image[0]+'"></a>';
								}
			 temp_html +='<figcaption>'
			 +'	<a href="goods_details.html?id='+data.object.list[i].id+'">'
			 +'<span class="description">'+data.object.list[i].sellPoint+'</span>'
			 +'</a>'
			 +'	<span class="products_prices">￥'+data.object.list[i].price+'</span>'
			 +'	<span class="products_sales">'
			 +'	销量：'+data.object.list[i].salesVolume+'件'
						+'</span>'
						+'<!--清理浮动-->'
						+'<div class="clearfix"></div>	'
						+'<div class="products_box">'
						+'<div class="products_drop">'
						+'<a href="javascript:void(0)" onclick="checkNum('+data.object.list[i].id+','+data.object.list[i].price+','+data.object.list[i].repertory+')"><span class="products_cart">加入购物车</span></a>'
						+'<span>仅剩'+data.object.list[i].repertory+'件</span>'
						+'<a href="javascript:void(0)" onclick="addFavorites('+data.object.list[i].id+')"><span class="collection"><i></i>收藏</span></a>'
						+'</div></div></figcaption>'
						+'<p class="products_p">'
						+'已收藏商品'
						+'</p>'
						+'</figure>'
				+'</li>';
		}
        temp_html += '<div class="paging_down">'
        +'<form>'
		+'<a href="search_goods.html?pageNo='+data.object.previousPageNo+'">上一页</a>';
		
        if(data.object.bottomPageNo>8){	  
        	 temp_html +='<a href="search_goods.html?pageNo='+data.object.topPageNo+'">'+data.object.topPageNo+'</a>';
           if((pageNo-2)<=1){
        for(var n=2;n<=(pageNo+2);n++){
        	  temp_html +='<a href="search_goods.html?pageNo='+n+'">'+n+'</a>';
        }
        temp_html +='<span>...</span>';
         }
         if((pageNo+2)>=data.object.bottomPageNo){
        	 temp_html +='<span>...</span>';
        	for(var n=(pageNo-2);n<data.object.bottomPageNo;n++){
	        	  temp_html +='<a href="search_goods.html?pageNo='+n+'">'+n+'</a>';
	        }
        }
        if((pageNo-2)>1&&(pageNo+2)<data.object.bottomPageNo){
        	if(pageNo-3>1){temp_html +='<span>...</span>';}
        	for(var n=(pageNo-2);n<(pageNo+3);n++){
	        	  temp_html +='<a href="search_goods.html?pageNo='+n+'">'+n+'</a>';
	        }
        	if(pageNo+3<data.object.bottomPageNo)
        	{temp_html +='<span>...</span>';}
        }
        temp_html +='<a href="search_goods.html?pageNo='+data.object.bottomPageNo+'">'+data.object.bottomPageNo+'</a>';
           }
        if(data.object.bottomPageNo<=8){
        	 for(var n=1;n<data.object.bottomPageNo+1;n++){
	        	  temp_html +='<a href="search_goods.html?pageNo='+n+'">'+n+'</a>';
	        }
        }
        temp_html +='<a href="search_goods.html?pageNo='+data.object.nextPageNo+'">下一页</a>'
		+'共'+data.object.totalPages+'页,'
	
		+'到第<input type="text" id="pageNoNum" "name="pageNo" value="'+pageNo+'" class="paging_down_text" />页'
		+'<input type="button" name="Submit" value="确定" class="paging_down_submit" onclick="forminput(\''+sellPoint+'\','+data.object.bottomPageNo+')"/>'
		+'</form></div>';
	 	$(ol).html(temp_html);
	 	temp_html = '';
	}
	if(data.status){
		 if(data.object.list.length==0){
			 
			   $('.products').empty();
			   $('.without_goods').show();
		 }
		
	  }
	 else{
		   $('.products').empty();
		   $('.without_goods').show();
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

     
//类目 搜索时 页面跳转
function forminput(sellPoint,total){
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
	window.location.href='search_goods.html&pageNo='+x+'';
	        }
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

function show(skuId){
	$.ajax({ url : "queryEqualCategory",//请求地址 
		dataType : "json",//数据格式  
		data:{"id":skuId},
		type : "post",//请求方式 
		async : true,//是否异步请求 
		success : function(data){
		   if(data.status){
			   var li=$('.list_title_open').empty();
			   var ht=' ';
			   for (var int = 0; int < data.object.length; int++) {
				  ht +='<a href="classification.html?id='+data.object[int].id+'&pageNo=1"><li>'+data.object[int].name+'</li></a>';
			}
			   $(li).html(ht);
		
			  
		   }
		   //alert(data.message)
		},
	})
}

/**
 * 读取存储
 */
function getRecentlyViewedSKU(userID) {
	var mySkuItem = storage.getItem(userID);
	if(mySkuItem == null) {
		$("#recentlyViewed").empty();
		//alert("暂无最近浏览的商品记录");
	} else {
		var mySkuArray = JSON.parse(mySkuItem),
			i,
			len = mySkuArray.length,
			mySku,
			code="",
			img;
		console.log(mySkuArray);
		for(i = 0; i < len; i++) {
			mySku = mySkuArray[i];
			//拼接
			img = JSON.parse(mySku.object.image);
			code += "<li><figure><a href='goods_details.html?id="+mySku.object.id+"'><img src='"+images_path80
					+ img[0] +"'></a><figcaption>"
					+"<a href='goods_details.html?id="+mySku.object.id+"' class='underline'><span class='character'>"
					+ mySku.object.sellPoint + "</span></a><span class='products_prices'>"
					+ mySku.object.price + "</span></figcaption></figure></li>";
		}
		$("#recentlyViewed").empty().append(code);

	}
}
function changeStyle(t){
	$('.good_choose_a a').removeClass('a_on');
	$(t).addClass('a_on');
}

function goTop(){
	$("html,body,.main-container").animate({
        scrollTop: 0
    })

}

function changGoodsNum(num){
	$('#goodNum').empty().append("商品("+num+")");
}