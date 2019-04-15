$(document).ready(function(){
$.ajax({ url : "queryCategory",//请求地址 
	dataType : "json",//数据格式  
	type : "post",//请求方式 
	async : false,//是否异步请求 
	success : function(data) { //如何发送成功
		if(data.status)
	     console.log(data)
	     var ol=$('.classification').empty();
		 var temp_html = '';
		 for(var i=0;i<data.object.length;i++) {
			
			 temp_html += '<li>'
					+'<a href="classification.html?id='+data.object[i].parentName.id+'&pageNo=1"><dd>'+data.object[i].parentName.name+'</dd></a>';
			        
					for(var k=0;k<data.object[i].sonName.length;k++){
						temp_html +='<a href="classification.html?id='+data.object[i].sonName[k].id+'&pageNo=1"><dt>'+data.object[i].sonName[k].name+'</dt></a>';
					}
					temp_html += '<section class="detail">'
					+'<div>'
					+'<h6>分类</h6>';
					for(var j=0;j<data.object[i].childName.length;j++){
						temp_html += '<a href="classification.html?id='+data.object[i].childName[j].id+'&pageNo=1"><span>'+data.object[i].childName[j].name+'</span></a>';
					}
					temp_html += '</div>'
					+'</section>'
					+'</li>';
			 }
		 	$(ol).html(temp_html);
		 	temp_html = '';
	
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
		//活动模块的商品展示
		$.ajax({ url : "Item/findOnSaleSkuOfIndex",//请求地址 
			dataType : "json",//数据格式  
			type : "post",//请求方式 
			async : false,//是否异步请求 
			success : function(data){
				console.log(data);
				if(data.status){
				var sale=$('.discount').empty();
				var html_sale='';
				html_sale+='<div  class="title">'
				+'<h2>活动<div>优惠活动</div></h2>'
				+'<a href="privilege.html"><h4>更多商品&gt;</h4></a>'
				+'</div><!--清理浮动-->'
				+'<div class="clearfix"></div>'
				+'<a href="goods_details.html?id='+data.object[0].id+'">'
				+'<figure class="discount_left">';
				
				var image=JSON.parse(data.object[0].image);
				     for (var i = 0; i < image.length; i++) {
					      html_sale += '<img src="'+images_path240+''+image[0]+'">';
				                      }
				  html_sale+='<figcaption>'
				  +'<span>CHANEL<strong>'+data.object[0].price+'元</strong><p>'+data.object[0].discount+'元</p></span>'
				  +'</figcaption>'
				  +'</figure>'
				  +'</a>'
				 +'<a href="goods_details.html?id='+data.object[1].id+'">'
				 +'<figure class="discount_top">';
				 var image1=JSON.parse(data.object[1].image);
			     for (var i = 0; i < image.length; i++) {
				      html_sale += '<img src="'+images_path240+''+image1[0]+'">';
			                      }
			  html_sale+='<figcaption>'
			  +'<span>CHANEL<strong>'+data.object[1].price+'元</strong><p>'+data.object[1].discount+'元</p></span>'
			  +'</figcaption>'
			  +'</figure>'
			  +'</a>'
			  +'<a href="goods_details.html?id='+data.object[2].id+'">'
			  +'<figure class="discount_top discount_bottom">'
				 var image2=JSON.parse(data.object[2].image);
			     for (var i = 0; i < image.length; i++) {
				      html_sale += '<img src="'+images_path240+''+image2[0]+'">';
			                      }
			  html_sale+='<figcaption>'
			  +'<span>CHANEL<strong>'+data.object[2].price+'元</strong><p>'+data.object[2].discount+'元</p></span>'
			  +'</figcaption>'
			  +'</figure>'
			  +'</a>'
			  +'<div  class="discount_right">'
			  +'<a href="goods_details.html?id='+data.object[3].id+'"><figure class="discount_right_top">';
				var image3=JSON.parse(data.object[3].image);
			     for (var i = 0; i < image.length; i++) {
				      html_sale += '<img src="'+images_path240+''+image3[0]+'">';
			                      }
			  html_sale+='<figcaption>'
			  +'<span>CHANEL<strong>'+data.object[3].price+'元</strong><p>'+data.object[3].discount+'元</p></span>'
			  +'</figcaption>'
			  +'</figure>'
			  +'</a>'
			  +'<a href="goods_details.html?id='+data.object[4].id+'"><figure class="discount_right_top">';
				var image4=JSON.parse(data.object[4].image);
			     for (var i = 0; i < image.length; i++) {
				      html_sale += '<img src="'+images_path240+''+image4[0]+'">';
			                      }
			  html_sale+='<figcaption>'
			  +'<span>CHANEL<strong>'+data.object[4].price+'元</strong><p>'+data.object[4].discount+'元</p></span>'
			  +'</figcaption>'
			  +'</figure>'
			  +'</a>'
			  +'</div>';
			$(sale).html(html_sale);
			html_sale='';
				}
			},
			
		})
		
		
})
         //注销登录
		function logout(){
	$.ajax({
		url:"User/logout",
		type:"post",
		dataType:"json",
		async:false,
		success: function(data){
			if(data.status){
				window.location.href="index.html";
			}
		},
	})
	
}


$(document).ready(function(){
		$.ajax({
			url:"Item/findSkuByTimes",
			dateType:"json",
			type:"post",
			async : false,
			success : function(data){
				if(data.status)
				console.log(data)
				var s=$('.onnew').empty();
				var temp_html = '';
				  temp_html+='<div  class="title"><h2>上新'
				+'<div>今日上新</div>'
				+'</h2>'
				+'<a href="onnew.html"><h4>更多商品&gt;</h4></a>'
				+'</div>'
				+'<table width="1200" border="1" class="onnewtable">'
				+'<tr>'
				+' <td width="480" height="330" rowspan="2" scope="col">'
				+'<a href="goods_details.html?id='+data.object.list["0"].id+'">'
				+'<figure class="onnew_left">'
				
				+'<img src="'+images_path240+''+JSON.parse(data.object.list["0"].image)[0]+'">'
				+'<figcaption>'
				+'<span>CHANEL <p>'+data.object.list["0"].price+'元</p></span>'
				+'</figcaption>'
				+'</figure>'
				+'</a>'
				+'</td>'
				+'<td width="240" rowspan="2" scope="col">'
				+'<a href="goods_details.html?id='+data.object.list["1"].id+'">'
				+'<figure class="onnew_center">'
				+'<img src="'+images_path240+''+JSON.parse(data.object.list["1"].image)[0]+'">'
				+'<figcaption>'
				+'<span>CHANEL <p>'+data.object.list["1"].price+'元</p></span>'
				+'</figcaption>'
				+'</figure>'
				+'</a>'
				+'</td>'
				+'<td width="240" height="165" scope="col">'
				+'<a href="goods_details.html?id='+data.object.list["2"].id+'">'
				+'<figure class="onnew_right">'
				+'<img src="'+images_path240+''+JSON.parse(data.object.list["2"].image)[0]+'">'
				+'<figcaption>'
				+'<span>CHANEL <p>'+data.object.list["2"].price+'元</p></span>'
				+'</figcaption>'
				+'</figure>'
				+'</a>'
				+'</td>'
				+'<td width="240" scope="col">'
				+'<a href="goods_details.html?id='+data.object.list["3"].id+'">'
				+'<figure class="onnew_right">'
				+'<img src="'+images_path240+''+JSON.parse(data.object.list["3"].image)[0]+'">'
				+'<figcaption>'
				+'<span>CHANEL <p>'+data.object.list["3"].price+'元</p></span>'
				+'</figcaption>'
				+'</figure></a></td></tr><tr>'
				+' <td width="240" height="165">'
				+'<a href="goods_details.html?id='+data.object.list["4"].id+'">'
				+'<figure class="onnew_right">'
				+'<img src="'+images_path240+''+JSON.parse(data.object.list["4"].image)[0]+'">'
				+'<figcaption>'
				+'<span>CHANEL <p>'+data.object.list["4"].price+'元</p></span>'
				+'</figcaption>'
				+'</figure>'
				+'</a>'
				+'</td>'
				+'<td>'
				+'<a href="goods_details.html?id='+data.object.list["5"].id+'">'
				+'<figure class="onnew_right">'
				+'<img src="'+images_path240+''+JSON.parse(data.object.list["5"].image)[0]+'">'
				+'<figcaption>'
				+'<span>CHANEL <p>'+data.object.list["5"].price+'元</p></span>'
				+'</figcaption>'
				+'</figure></a></td></tr></table></div></section><!--清理浮动--><div class="clearfix"></div>';
		 $(s).html(temp_html);
		 temp_html = '';
			},
			
		})
})
