var userID = "";// 获取用户ID
//将sku设为全局变量
var targetSku;
var storage;
//$(document).ready(
$(function(){
				

					function getLocalStorage() {
						if (typeof localStorage == "object") {
							return localStorage;
						} else if (typeof globalStorage == "object") {
							return globalStorage[location.host];
						} else {
							throw new Error("Local storage not availbale.");
						}
					}

					storage = getLocalStorage();

					// 页眉
					$.ajax({
						url : "User/isLogin",// 请求地址
						dataType : "json",// 数据格式
						type : "post",// 请求方式
						async : false,// 是否异步请求
						success : function(data) {
							var tital = $('.list li:nth-of-type(2)')
									.empty();
							var xs = '';
							if (data.status) {
								console.log(data)
								userID = data.object.id;
								xs += '<li><a href="user/personal/personal_information.html" >'
										+ data.object.username
										+ '</a></li>';
										
								
							} else {
								userID = "tourist";
								xs += '<li><a href="login.html" >请登录</a></li>';
								
							}
							$(tital).html(xs);
							xs = '';
						},
					})
                     
					//客服服务
					callCustomerService();
					
					// 相关商品
					$.ajax({
						url : "Item/querySimilarSku",// 请求地址
						dataType : "json",// 数据格式
						data : {skuId:parseInt(request("id"),10)},
						type : "post",// 请求方式
						async : true,// 是否异步请求
						success : function(data) {
							var titals = $('#similar_goods').empty();
							var xss = '<h5>相关商品</h5>';
							if (data.status) {
								var similar = data.object;
									for (var i = 0, length = similar.length; i < length; i++) {
										xss += '<li>'
											+'<dd><img src="'+IMAGEPATH+'/80x80/'+JSON.parse(similar[i].image)[0]+'"></dd>'
											+'<a href="'+PATH+'/goods_details.html?id='+similar[i].id+'">'
											+'<dt>'
											+'<h6>'+similar[i].sellPoint+'</h6>'
											+'<p>￥'+similar[i].price+'</p>'
											+'</dt>'
											+'</a>'
											+'</li>';
									}
							} 
							$(titals).html(xss);
							xss = '';
						},
					})
					 //销量排行榜		
			        $.ajax({ url : "Item/findSkuBysalesVolume",//请求地址 
						dataType : "json",//数据格式  
						type : "post",//请求方式 
						async : false,//是否异步请求 
						success : function(data){
//						findSkuBysalesVolume(data);
						//显示销量排行 
						var titals = $('#saleVolume_goods').empty();
							var xss = '<h5>畅销排行</h5>';
							if (data.status) {
								var saleVolume = data.object;
									for (var i = 0, length = saleVolume.length; i < length; i++) {
										xss += '<li>'
											+'<dd><img src="'+IMAGEPATH+'/80x80/'+JSON.parse(saleVolume[i].image)[0]+'"></dd>'
											+'<a href="'+PATH+'/goods_details.html?id='+saleVolume[i].id+'">'
											+'<dt>'
											+'<h6>'+saleVolume[i].sellPoint+'</h6>'
											+'<p>￥'+saleVolume[i].price+'</p>'
											+'</dt>'
											+'</a>'
											+'</li>';
									}
							} 
							$(titals).html(xss);
							xss = '';
						},
					})
})
$(function() {
	var id = parseInt(request("id"),10);
	getSkuById(id);
	getItem(id);
	getEvaluation(id);
	getEva(id);
})
function getSkuById(id) {
	// var di=$('goods_price').empty();
	var url = "Item/findSkuById";
	var data = {
		id : id
	};
	$.post(
					url,
					data,
					function(result) {
						if (result.status == true) {

							setRecentlyViewedSKU(result, userID);
							console.log(result);
							//将sku设为全局变量
							targetSku = result['object'];
							var image = JSON.parse(result.object.image);
							//图片列表
							var temp_html = '';
							for(var i = 0, length = (image.length < 5? image.length: 5); i < length; i++){
								temp_html += '<li class="'+(i===0?'active':'')+'"><img src="'+IMAGEPATH+'/80x80/'+image[i]+'" data-bigimg="'+image[i]+'"><i></i></li>';
							}
							$('.con-FangDa-ImgList').html(temp_html);
							//正常显示图片
							$('.con-fangDaIMg > img').attr('src',IMAGEPATH+'/500x500/'+image[0]);
							//放大镜显示的图片
							$('.con-fangDaIMg .magnifyingShow img').attr('src',IMAGEPATH+'/'+image[0]);;
							$('#h2').html(result.object.sellPoint);
							$('.typeface2').html(result.object.price);
							$('.goods_address').html(result.object.deliveryPlace);
							
							
							if(result.object.repertory >0 && result.object.status == 0){
								//设置库存
								if(result.object.repertory <100 ){
									$('.goods_number dt').append('<span>仅剩<strong>'+ result.object.repertory +'</strong>件，抓紧时间购买哦！</span>');
								}
							}else{
								$('.goods-buy-count').hide();
								$('.add_shops').html('<a href="javaScript:void(0)"><div class="no_shops">已售罄</div></a>');
							}
						}
						getSku(id);
						getSkuLine(id,result.object.sellPoint);
						initFavorite();
					});
					
}

//设置收藏
function initFavorite(){
	var $coll = $('.goods_left_bottom .collect');
	if(userID !== 'tourist'){
		$.ajax({
			type:"post",
			url:"queryFavoritesBySkuId",
			data:{skuId:targetSku.id},
			dataType:'json',
			async:true,
			success:function(result){
				//已收藏
				if(result.status){
					//取消收藏图标
					$coll.css('background-position','-196px 0px');
					$coll.next().html('取消收藏').click(function(){deleteFavorites(result.object.id)});
				//没有收藏
				}else{
					$coll.next().click(function(){addFavorites(targetSku.id)});
				}
			}
		});
	}else{
		$coll.next().attr('href','login.html');
	}
}

//添加收藏
function addFavorites(skuId){
	$.ajax({
		type:"post",
		url : 'addFavorites',
		data : {"skuId":skuId},
		dataType : 'json',
		async : true,
		success : function(result){
			if(result.status){
				var $coll = $('.goods_left_bottom .collect');
				//取消收藏图标
				$coll.css('background-position','-196px 0px');
				$coll.next().html('取消收藏').click(function(){deleteFavorites(result.object.id)});
			}
		}
	});
}

//删除收藏
function deleteFavorites(id){
	$.ajax({
		type : 'post',
		url : 'deleteFavorites',
		data : {id:id},
		dataType : 'json',
		async : true,
		success : function(result){
			if(result.status){
				var $coll = $('.goods_left_bottom .collect');
				//添加收藏图标
				$coll.css('background-position','-196px 0px');
				$coll.next().html('收藏商品').click(function(){addFavorites(targetSku.id)});
			}
		}
	});
}

//获取Sku的类目路径
 function getSkuLine(skuId,line){
	 $.ajax({
		 url:"queryCategoryTree",
		 data:{"skuId":skuId},
	     dataType:"json",
	     type:"post",
	     success : function(data){
	    	 if(data.status){
	    		 console.log(data);
	    		 var div=$('.list_title').empty();
	    		 var html='';
	    		html+= '<a href="'+PATH+'"><span>首页</span></a><i class="ico_right"></i>';
	    			
	    			for (var int = 0; int < data.object.length; int++) {
	    				html+= '<a href="classification.html?id='+data.object[int].id+'"><span>'+data.object[int].name+'</span></a><i class="ico_right"></i>';
					}
					html+='<a href="'+location.href+'"><span>'+line+'</span></a>';
					
					$(div).html(html);
					html='';
	    	 }
	     }
	 })
 }



var index;
function getSku(id) {
	var url = "Item/findSkuBySkuId";
	var data = {
		id : id
	};
	$.post(
					url,
					data,
					function(result) {
						if (result.status == true) {
							skuFun.init('.goods_color',result.object);
						}
					})
}

function method(e) {
	var $num = $("#qulity");
	if(checkCount() ){
		
		//购买数量已经是1  不可再减
		if($num.val() == 1){
			$('.goods_number a:first-of-type').css('cursor','not-allowed');
			
		}else{
			$('.goods_number a:first-of-type').css('cursor','inherit');
			$num.val(parseInt($num.val()) - 1);
		}
		//购买熟练已经是 库存 不可再加
		if($num.val() == targetSku.repertory){
			$('.goods_number a:last-of-type').css('cursor','not-allowed');
			
		}else{
			$('.goods_number a:last-of-type').css('cursor','inherit');
		}
	}
	
}
function add(e){
	var $num = $("#qulity");
	if(checkCount() ){
		//购买数量已经是1  不可再加
		if($num.val() == 1){
			$('.goods_number a:first-of-type').css('cursor','not-allowed');
			
		}else{
			$('.goods_number a:first-of-type').css('cursor','inherit');
		}
		//购买熟练已经是 库存 不可再加
		if($num.val() == targetSku.repertory){
			$('.goods_number a:last-of-type').css('cursor','not-allowed');
		}else{
			$('.goods_number a:last-of-type').css('cursor','inherit');
			$num.val(parseInt($num.val())+1);
		}
	}
	
}

/**
 * @description  检查用户输入的购买数量是否有效
 * @return true 购买数量有效
 */
function checkCount() {
	var num = parseInt($("#qulity").val());
	var kvcun = targetSku.repertory;
	var re = /^[1-9]\d*$/;
	if (!re.test(num) ) {
		new $.flavr({
			content : '请输入正整数',
		});
		$("#qulity").val(1);
		return false;
	} 
	else if (num > kvcun) {
		new $.flavr({
			content : '库存不足',

		});
		$("#qulity").val(kvcun);
		return false;
	}
	return true;
}
$("#qulity").blur(function(){
	checkCount();
});
function getItem(id) {

	var url = 'Item/findItemBySkuId';
	var data = {
		id : id
	};
	$.post(url, data, function(result) {
		if (result.status == true) {
			var par = JSON.parse(result.object.paramData);
			var div = $('.goods_details_text').empty();
			var ul = '<h3>商品参数</h3>';
			for (var v = 0; v < par.length; v++) {
				for (var a = 0; a < par[v].param.length; a++) {
					ul += '<ul>' + '<li>' + par[v].param[a].k + ':'
							+ par[v].param[a].v + '</li>' + '</ul>';

				}
			}

			$(div).html(ul);
			var div1 = $('.goods_details_image').empty();
			var h = $('<h3>商品详情</h3><p>'
					+ decodeURIComponent(result.object.itemDetail).replace('../../../apache-tomcat-8.5.11/webapps/','') + '</p>');
			div1.append(h);
		}
	})
}

function getLocalTime(nS) {
	return new Date(parseInt(nS)).toLocaleString().replace(/\//g, "-");
}

function getEvaluation(id) {

	var divEva = $('.comments_main').empty();
	var url = 'Eva/findEvaNum';
	var data = {
		id : id
	};
	var li = '';
	$.post(url, data, function(result) {
		if (result.status == true) {
			$('.comments_title').html('商品评价(' + result.object.EvaNum + ')');

			li += '<div>' + '<a href="javaScript:void(0)"><span>全部（' + result.object.EvaNum
					+ '）</span></a><a href ="javaScript:void(0)"><span>有图（'
					+ result.object.imgNum + '）</span></a>' + '</div>';
			$(divEva).html(li);

		}
	})
}
var totalPages;
/**
 * @description 得到sku的评价
 * @param {Object} id  skuId
 * @param {Object} pageNo  查询页码
 */
function getEva(id, pageNo) {

	var url = 'Eva/findEvaBySkuId';
	var data = {
		id : id,
		pageNo : pageNo
	};
	$
			.post(
					url,
					data,
					function(result) {
						if (result.status == true) {
							totalPages = result.object.totalPages;
							
							//显示评价
							var ul = $('.comments_review');
							var li = '';
							var list = result.object.list;
							for(var i = 0,length = list.length; i < length; i++){
								li += '<li><dd>';
									if(list[i].id.headPort==""||list[i].id.headPort==null){
										li +='<img src="images/add_picture.png">';
									}
									else{
										li +='<img src="'+ headPort_images + list[i].id.headPort+ '">';
									}
										
									li +='<p>'+(list[i].etype?(list[i].id.username.substr(0, 3) + "****"+ list[i].id.username.substr(8)):'匿名')+'</p>'
												+'</dd>'
												+'<dt>'
												+'<p class="comment_time">'
												+'<img src="images/comment.png">'
												+'<span class="color_red">'+list[i].commodityScore+'星</span>'
												+'<span class="f_right">'+getLocalTime(list[i].time)+'</span>'
												+'</p>'
												+'<p class="comment_text">'+list[i].content+'</p>'
												+'<p class="comment_img">';
												var img = JSON.parse(result.object.list[i].image);
												for(var j = 0, length2 = img?img.length:0; j < length2; j++ ){
													li += '<img style="width:80px;heigth:80px" src="'+ evaluation_images + img[i] + '">';
												}
									
								li += '</p>'
									+'<p>'
									+'<span class="f_left">' + list[i].id.sellPoint + '</span>'
									+'<span class="f_right like_move"><a href="javaScript:void(0)"><i class="like_button"></i></a>(1)</span>'
									+'</p>'
									+'</dt></li>';
							}
							li += '<div class="clearfix"></div>';
							$(ul).html(li);
							
							
							//显示页码导航
							var div = $('.paging_down');
							var li = '';
							li += '<form>'
									+ '<a href="javascript:void(0);" onclick="getEva('
									+ id + ',' + result.object.previousPageNo
									+ ')">上一页</a>';
							if (result.object.bottomPageNo > 8) {
								li += '<a href="javascript:void(0);" onclick="getEva('
										+ id
										+ ','
										+ result.object.topPageNo
										+ result.object.topPageNo
										+ '</a>';
								if ((pageNo - 2) <= 1) {
									for (var n = 2; n <= (pageNo + 2); n++) {
										li += '<a href="javascript:void(0);" onclick="getEva('
												+ id
												+ ','
												+ n
												+ ')">'
												+ n
												+ '</a>';
									}
									li += '<span>...</span>';
								}
								if ((pageNo + 2) >= result.object.bottomPageNo) {
									li += '<span>...</span>';
									for (var n = (pageNo - 2); n < result.object.bottomPageNo; n++) {
										li += '<a href="javascript:void(0);" onclick="getEva('
												+ id
												+ ','
												+ n
												+ ')">'
												+ n
												+ '</a>';
									}
								}
								if ((pageNo - 2) > 1
										&& (pageNo + 2) < result.object.bottomPageNo) {
									if (pageNo - 3 > 1) {
										li += '<span>...</span>';
									}
									for (var n = (pageNo - 2); n < (pageNo + 3); n++) {
										li += '<a href="javascript:void(0);" onclick="getEva('
												+ id
												+ ','
												+ n
												+ ')">'
												+ n
												+ '</a>';
									}
									if (pageNo + 3 < result.object.bottomPageNo) {
										li += '<span>...</span>';
									}
								}
								li += '<a href="javascript:void(0);" onclick="getEva('
										+ id
										+ ','
										+ result.object.bottomPageNo
										+ ')">'
										+ result.object.bottomPageNo
										+ '</a>';
							} else {
								for (var n = 1; n < result.object.bottomPageNo + 1; n++) {
									li += '<a href="javascript:void(0);" onclick="getEva('
											+ id + ',' + n + ')">' + n + '</a>';
								}
							}
							li += '<a href="javascript:void(0);" onclick="getEva('
									+ id
									+ ','
									+ result.object.nextPageNo
									+ ')">下一页</a>';
							li += '共'
									+ result.object.totalPages
									+ '页,当前第'
									+ result.object.pageNo
									+ '页,到第<input type="text" class="paging_down_text" />页'
									+ '<input type="button" name="Submit" value="确定" class="paging_down_submit" onclick="forminput('+id+','+result.object.totalPages+')"/>';
							

							li += '</form>';
							$(div).html(li);
						}

					})

}

function forminput(id, total) {
	var domInput = $('.paging_down_text').val();
	//console.log(domInput);
	var x = domInput;
	var re = /^[1-9]\d*$/;
	if (!re.test(x)) {
		new $.flavr({
			content : '请输入正整数',
		});

	} else {
		if (x > total) {
			new $.flavr({
				content : '请输入正确的范围',
			});
		} else {
			getEva(id, x);
		}
	}
}

/**
 * 向localStorage中存入最近浏览的SKU记录（即sku的json对象）
 */
function setRecentlyViewedSKU(result, userID) {
	var sku = result, skuItem = storage.getItem(userID), skuArray;

	if (sku.object != null) {
		if (skuItem == null) { //最近浏览物品的sku不存在
			skuArray = new Array();
			skuArray.push(sku); //添加最近浏览的sku
			setLocalStorage(userID, skuArray); //存储storage
		} else {
			skuArray = JSON.parse(skuItem);
			if (isExist(sku.object.id, userID)) {
				if (skuArray.length < 6) { //只保存最近浏览的10个sku
					skuArray.push(sku);
				} else {
					skuArray.shift(); //移除队列第一项
					skuArray.push(sku);
				}
				setLocalStorage(userID, skuArray); //存储storage
			}
		}

	}

}

function setLocalStorage(userID, skuArray) {
	skuArray2String = JSON.stringify(skuArray);
	storage.setItem(userID, skuArray2String); //最近浏览记录存入localStorage
}

/**
 * 验证是否已浏览
 */
function isExist(sku_key, userID) {
	var flag = true, //第一次浏览为true
	mySkuItem = storage.getItem(userID);
	if (mySkuItem == null) {
		//alert("暂无最近浏览的商品记录");
	} else {
		var mySkuArray = JSON.parse(mySkuItem), i, len = mySkuArray.length;
		for (i = 0; i < len; i++) {
			if (mySkuArray[i].object.id == sku_key) {
				flag = false;
				break;
			}
		}
		return flag;
	}
}