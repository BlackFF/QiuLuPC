$(document).ready(function(){
	privilege(1);
	
	//销量排行榜		
    $.ajax({ url : "Item/findSkuBysalesVolume",//请求地址 
				dataType : "json",//数据格式  
				type : "post",//请求方式 
				async : false,//是否异步请求 
				success : function(data){
				findSkuBysalesVolume(data);
				},
			})
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
    //活动商品（带分页）
	function privilege(pageNo){
	$.ajax({
		url:"Item/findOnSaleSku",
		data:{"pageNo":pageNo},
		dataType:"json",
		type:"post",
		async:"false",
		success : function(data){
			if(data.status){
			var Left=$('.privilege_left').empty();
			console.log(data);
			var html='';
			for (var int = 0; int < data.object.list.length; int++) {
				//console.log(data.object.list[int].etime);
				show_time(data.object.list[int].etime,int,pageNo);
				var image=JSON.parse(data.object.list[int].saleImage)
				html+='<a href="goods_details.html?id='+data.object.list[int].id+'">'
				+'<li>'
				+'<figure>';
				for (var int2 = 0; int2 < image.length; int2++) {
					html += '<img src="'+onSale_images+''+image[0]+'">';
				}
				html+='<figcaption>'
				/*+'<p class="privilege_left_img"><img src="images/privilege2.png"></p>'*/
				+'<p class="privilege_left_p1">'+data.object.list[int].sellPoint+'</p>'
				+'<p class="privilege_left_p2"><span>'+data.object.list[int].price+'</span></p>'
				+'<p class="privilege_left_p3"><i class="privilege_clock"></i>';
				if(data.object.list[int].etime==null){
					html+='<span></span>无期限！等你来买';
				}
				else{
					html+='<span id="'+pageNo+'time_d'+int+'"></span> 天'
				    +'<span id="'+pageNo+'time_h'+int+'"></span> 时'
				    +'<span id="'+pageNo+'time_m'+int+'"></span> 分'
				    +'<span id="'+pageNo+'time_s'+int+'"></span> 秒';
				}
				html+='</p>'
				+'</figcaption>'
				+'</figure>'
				+'</li>'
				+'</a>';
			}
			$(Left).html(html);
			html='';
			var left=$('.paging_down').empty();
			var html2='';
			html2+='<form>'+
			'<a href="javascript:void(0);" onclick="privilege('+data.object.previousPageNo+')">上一页</a>';
			if(data.object.bottomPageNo > 8) {
				html+='<a href="javascript:void(0);" onclick="privilege('+data.object.topPageNo+')">'+data.object.topPageNo+'</a>';
				if((pageNo - 2) <= 1) {
					for(var n = 2; n <= (pageNo + 2); n++) {
						html2+='<a href="javascript:void(0);" onclick="privilege('+n+')">'+n+'</a>';
						}
					html2+='<span>...</span>';
					}
				if((pageNo + 2) >= data.object.bottomPageNo) {
					html2+='<span>...</span>';
					for(var n = (pageNo - 2); n < data.object.bottomPageNo; n++) {
						html2+='<a href="javascript:void(0);" onclick="privilege('+n+')">'+n+'</a>';
					}
				}
				if((pageNo - 2) > 1 && (pageNo + 2) < data.object.bottomPageNo) {
					if(pageNo - 3 > 1) {
						html2+='<span>...</span>';
					}
					for(var n = (pageNo - 2); n < (pageNo + 3); n++) {
						html2+='<a href="javascript:void(0);" onclick="privilege('+n+')">'+n+'</a>';
					}
					if(pageNo + 3 < data.object.bottomPageNo) {
						html2+= '<span>...</span>';
					}
				}
				lhtml2+= '<a href="javascript:void(0);" onclick="privilege('+data.object.bottomPageNo+')">'+result.object.bottomPageNo+'</a>';
				}else {
					for(var n = 1; n < data.object.bottomPageNo + 1; n++) {
						html2+= '<a href="javascript:void(0);" onclick="privilege('+n+')">'+n+'</a>';
					}
				}
			html2+='<a href="javascript:void(0);" onclick="privilege('+data.object.nextPageNo+')">下一页</a>';
			html2+='共'+data.object.totalPages+'页,当前第'+data.object.pageNo+'页,到第<input type="text" class="paging_down_text" />页'+
					'<input type="button" name="Submit" value="确定" class="paging_down_submit" onclick="forminput('+data.object.bottomPageNo+')"/>';
			html2+='</form>';
			$(left).html(html2);
			html2='';
			}
		},
	})
	
}


function forminput(total){
    var domInput = $('.paging_down_text').val();
	var x=domInput;
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
		    	
		    	privilege(x);
	        }
       }
}

//销量排行榜		
function findSkuBysalesVolume(data){
	if(data.status){
		var div=$('.privilege_right').empty();
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

function show_time(time,int,pageNo){ 
	if(time==null){
		return;
	}
	else{
    var time_start = new Date().getTime(); //设定当前时间
    var time_end =Number(time);//设定目标时间
    // 计算时间差 
    var time_distance = time_end - time_start; 
    // 天
    var int_day = Math.floor(time_distance/86400000) 
    time_distance -= int_day * 86400000; 
    // 时
    var int_hour = Math.floor(time_distance/3600000) 
    time_distance -= int_hour * 3600000; 
    // 分
    var int_minute = Math.floor(time_distance/60000) 
    time_distance -= int_minute * 60000; 
    // 秒 
    var int_second = Math.floor(time_distance/1000) 
    // 时分秒为单数时、前面加零 
    if(int_day < 10){ 
        int_day = "0" + int_day; 
    } 
    if(int_hour < 10){ 
        int_hour = "0" + int_hour; 
    } 
    if(int_minute < 10){ 
        int_minute = "0" + int_minute; 
    } 
    if(int_second < 10){
        int_second = "0" + int_second; 
    } 
  
    // 显示时间 
    $("#"+pageNo+"time_d"+int+"").text(int_day); 
    $("#"+pageNo+"time_h"+int+"").text(int_hour); 
    $("#"+pageNo+"time_m"+int+"").text(int_minute); 
    $("#"+pageNo+"time_s"+int+"").text(int_second); 
    // 设置定时器
    if(int_day=="00"&int_hour=="00"&int_minute=="00"&int_second=="00"){
    	
     }
    else{
    setTimeout("show_time("+time+","+int+","+pageNo+")",1000); 
    }
	}
}