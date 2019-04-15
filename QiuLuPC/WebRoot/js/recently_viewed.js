var skuArray; //声明存放最近浏览的sku数组
//var userID  = ${user.id == null ? "null" : user.id};//获取用户ID

/**
 * 向localStorage中存入最近浏览的SKU记录（即sku的json对象）
 */
function setRecentlyViewedSKU(skuJSON,userID){
	var skuItem = localStorage.getItem(userID);
	if(skuItem == null){//最近浏览物品的sku不存在
		skuArray = new Array();
		skuArray.push(skuJson);//添加最近浏览的sku
	}else{
		skuArray = skuItem.split(",");
		if(skuArray.length<10){//只保存最近浏览的10个sku
			skuArray.push(skuJSON);
		}else{
			skuArray.shift();//移除队列第一项
			skuArray.push(skuJSON);
		}
	}
	var skuArray2String = skuArray.toString();
	localStorage.setItem(userID,skuArray2String);//最近浏览记录存入localStorage
}


/**
 * 获取localStorage存储的最近浏览商品记录
 */
function getRecentlyViewedSKU(){
	var allSkuIterm = localStorage.getItem(userID);
	if(allSkuIterm == null){
		alert("暂无最近浏览的商品记录");
	}else{
		skuArray = allSkuIterm.split(",");
		for( var i = 0 ; i<skuArray.length ; i++){
			recentlyViewedJSON = skuArray[i];
			//页面拼接
		}
	}
}
