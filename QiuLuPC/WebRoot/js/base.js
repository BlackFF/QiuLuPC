/** 公用js类--------------------- */
var path = "/QiuLuPC";
var PATH = "/QiuLuPC";
var IMAGEPATH = 'http://192.168.0.200:8081/QiuLu/images';
// 返回上一页
function goBack(){
	window.history.back(-1);
	//location.reload(); // 刷新页面
};

/**
 * 添加到我的收藏
 * @param url
 * @returns
 */
function addFavorite() {
    var url = window.location.href;
    var title = document.title;
    try {
        window.external.addFavorite(url, title);
    }
    catch (e) {
        try {
            window.sidebar.addPanel(title, url, "");
        }
        catch (e) {
        	new $.flavr({
			    content      : "抱歉，您所使用的浏览器无法完成此操作，请使用Ctrl+D进行添加",
			    closeOverlay : true,
			    closeEsc     : true
			});
        }
    }
}

// 跳转到页面
function skipUrl(url){
	location.href=url;
};


// 一列或者多列显示的切换
function changeShow(thisOpt){
	var $this = $(thisOpt);
	if($this.hasClass("rows-ico")){
		$this.removeClass("rows-ico");
		$('.warps-block').closest('li').css({'width':'100%','border-right':'none'});
		$('.warps-block').removeClass("warps-block-cols");
	}else{
		$this.addClass("rows-ico");
		$('.warps-block').closest('li').css({'width':'50%','border-right':'1px solid #e5e5e5'});
		$('.warps-block').addClass("warps-block-cols");
	}
};

/**
 * 异步判断是否登录
 */
function isLogin(){
	var flag;
	$.ajax({
		type: "post",
		url: path+"/User/isLogin",
		async: false,
		dataType:'json',
		success: function(data){
			if(!data.status){
				/*location.href="user/login.html";*/
				flag = false;
			}else{
				flag = true;
			}
		}
	});
	return flag;
}

/**
 * 设置搜索条件参数*/
function saveSearchParam(){
	
	var searchParam = getSearchParam();
	if(arguments.length==2){
		searchParam[arguments[0]] = arguments[1];
	}else if(arguments.length==1){
		for(var temp in arguments[0]){
			searchParam[temp] = arguments[0][temp];
		}
	}
	sessionStorage.setItem("searchParam",JSON.stringify(searchParam));
}
/**
 * 得到搜索条件*/
function getSearchParam(){
	var searchParam = sessionStorage.getItem("searchParam");
	searchParam = JSON.parse(searchParam);
	if(!searchParam){
		searchParam = {sousuo:'',startIndex:0,pageSize:20,categoryId:0,industryId:0,areacode:410105,pageStyle:'',sorter:0,loPrice:'',hiPrice:''}
	}
	return searchParam;
}

/**
 * 将要传递的参数存入sessionStrong
 * @param name 参数名称
 * @param value1
 * @param value2
 * */
function saveSessionStrongParam(name,value1,value2){
	var param = getSessionStrongParam(name);
	if(!param){
		param = {};
	}
	if(value2){
		param[value1] = value2;
	}else if(typeof value1 === 'object'){
		for(var temp in value1){
			param[temp] = value1[temp];
		}
	}else{
		param = value1;
	}
	sessionStorage.setItem(name,JSON.stringify(param));
}

/**
 * 得到sessionStrong中的参数*/
function getSessionStrongParam(name){
	var param = sessionStorage.getItem(name);
	param = JSON.parse(param);
	return param;
}

/**
 * 删除sessionStrong中的参数*/
function removeSessionStrongParam(name){
	sessionStorage.removeItem(name);
}


/*--获取网页传递的参数--*/
function request(paras)
{ 
    var url = location.href; 
    var paraString = url.substring(url.indexOf("?")+1,url.length).split("&"); 
    var paraObj = {} 
    for (i=0; j=paraString[i]; i++){ 
    paraObj[j.substring(0,j.indexOf("=")).toLowerCase()] = j.substring(j.indexOf("=")+1,j.length); 
    } 
    var returnValue = paraObj[paras.toLowerCase()]; 
    if(typeof(returnValue)=="undefined"){ 
    return ""; 
    }else{ 
    return decodeURIComponent(returnValue); 
    } 
}

/**
 * 判断输入页码是否规范
 */
function checkpageNo(obj){
	var pageNo =  $(obj).val();
	if (pageNo > totalPages || !(/^(\+|-)?\d+$/.test( pageNo ))||pageNo<=0) {
		$(obj).val('1');
		return;
	}
}
/**
 * 跳转页面
 * @param {Object} obj
 */
function skipPage(obj){
	var pageNo = $(obj).siblings("input").val();
	getFavorites(pageNo);
}

/**
 * 时间戳转换为日期
 */
 function getLocalTime(nS) {     
       return new Date(parseInt(nS)).toLocaleString().replace(/年|月/g, "-").replace(/日/g, " ");      
    } 

/**
 * 支付
 */
function gotoPay(orderId,total){
	var order = new Object();
	order.orderId = orderId;
	order.total = total;
	removeSessionStrongParam("order");
	saveSessionStrongParam("order",order);
	location.href = path + "/user/generate_orders.html";
}
/**
 * 查看订单详情
 */
function orderDetail(orderId){
	var orderid = new Object;
	orderid.id = orderId;
	removeSessionStrongParam("orderid");
	saveSessionStrongParam("orderid",orderid);
	location.href =path + "/user/sam/order_details.html";
}
/**
 * 取消订单
 */
function concleOrder(orderId,from){
 	new $.flavr({
	    content     : '确认取消该订单吗？',
	    dialog      : 'confirm',
	    onConfirm   : function(){
	    	$.ajax({
				url:"../../order/cancelOrder",
				data:{"id":orderId},
				success: function(data){
					if (data.status) {
						if (from == 0) {
							getOrders();
						}
						if (from == 1) {
							getOrders();
							getOrdersCount();
						}
						if (from == 2) {
							location.href = path + "/user/personal/personal_indent.html";
						}
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
	    },
	    onCancel    : function(){
	    },
	    closeOverlay : true,
  		closeEsc     : true
	});
}


/**
 * 催单
 */
function reminder(orderId){
	
}

/**
 * 退货
 */
function salesReturn(orderId,from){
	new $.flavr({
	    content     : '确定向商家发起退货吗？',
	    dialog      : 'confirm',
	    onConfirm   : function(){
	    	$.ajax({
				url:"../../order/rejectedOrder",
				data:{"id":orderId},
				success: function(data){
					if (data.status) {
						getOrders();
						if (from == 1) {
							getOrdersCount();
						}
						new $.flavr({
						    content      : '退货已申请，请联系在线客服人员！<br/>查看退货信息请到“我的售后”'
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
	    },
	    onCancel    : function(){
	    },
	    closeOverlay : true,
  		closeEsc     : true
	});
}

/**
 * 删除
 */
function delOrder(orderId,from){
	new $.flavr({
	    content     : '确认永久删除该订单吗？(删除后将不能恢复)',
	    dialog      : 'confirm',
	    onConfirm   : function(){
	    	$.ajax({
				url:"../../order/deleteOrder",
				data:{"id":orderId},
				success: function(data){
					if (data.status) {
						getOrders();
						if (from == 1) {
							getOrdersCount();
						}
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
	    },
	    onCancel    : function(){
	    },
	    closeOverlay : true,
  		closeEsc     : true
	});
}

//获取输入框的搜索内容
function getSellPoint(){
	var input= document.getElementById("search").value;
	var sellValue=$.trim(input);
	if(sellValue==""){
		new $.flavr({
		    content      : '搜索内容为空',
		});
	}
	else{
		saveSessionStrongParam("sellPoint",sellValue);
		//window.location.href="search_goods.html?pageNo=1";
		location.assign("http://192.168.0.141:7070/QiuLuPC/search_goods.html?pageNo=1");
	}
	
}

//回车事件
$(document).keydown(function(e) {  
    if (e.keyCode == 13) {  
    	getSellPoint(); 
    }  
});  
 
//QQ联系客服
function callCustomerService(){
	var tital = $('.list li:nth-of-type(5)')
	.empty();
var xs = '';
xs+='<li><a href="http://wpa.qq.com/msgrd?v=3&uin=827150261&site=qq&menu=yes" target="blank" >联系客服</a></li>';

$(tital).html(xs);
xs='';
}
