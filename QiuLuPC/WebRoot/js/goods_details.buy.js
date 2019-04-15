var buyFun = (function(){
	
	/**
	 * @description  立即购买
	 */
	function buyNow(){
		
		//得到购买skuId
		var skuId = targetSku.id;
		//得到购买数量
		var number = $("#qulity").val();
		//计算总价
		var total = targetSku.price*100*number/100;
		
		//ajax  addShoppingCart
		$.ajax({
			url : '/QiuLuPC/addShoppingCart',
			type : 'POST',
			data : {"skuId" : skuId,"total" : total,"number" : number},
			dataType : 'json',
			async : true,
			success : function(result){
				
				if(result.status){
					skip2Cart();
				}
			}
		});
	}
		
		
	
	/**
	 * @description  加入到购物车
	 */
	function add2Cart(){
		//得到购买skuId
		var skuId = targetSku.id;
		//得到购买数量
		var number = $("#qulity").val();
		//计算总价
		var total = targetSku.price*100*number/100;
		$.ajax({
    	url:'addShoppingCart',
    	data:{"skuId":skuId,"number":number,"total":total},
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
	
	//跳转页面  到购物车
	function skip2Cart(){
		location.href = '/QiuLuPC/user/cart.html';
	}
	
	$('.add_shops a:first-of-type').click(buyNow);
	$('.add_shops a:last-of-type,.goods_center_title a:last-of-type li').click(add2Cart);
	
}())
