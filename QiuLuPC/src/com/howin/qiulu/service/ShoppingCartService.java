package com.howin.qiulu.service;

import java.util.List;

import com.howin.qiulu.pojo.ShoppingCart;
import com.howin.qiulu.result.QiuluResult;

public interface ShoppingCartService {
	// 将商品加入到购物车中
	QiuluResult addShoppingCart(ShoppingCart shoppingCart);

	// 查看购物中的商品
	QiuluResult queryShoppingCart(Integer userId);

	// 清空用户购物车
	QiuluResult deleteShoppingCart(Integer userId);

	// 删除用户所选中的商品（购物车中）
	QiuluResult deleteShoppingCartById(List list);

	// 修改购物车中的商品数量
	QiuluResult updateShoppingCartNumber(ShoppingCart shoppingCart);
	
	//通过购物车ID 查找购物车中的一条记录 
	public ShoppingCart queryShoppingCartById(Integer id);
	
	//通过购物车ID集合 查看购物车中的多条记录
	public List<ShoppingCart> queryShoppingCartByIds(List id);

	QiuluResult addShoppingCarts(Integer[] skuId,Integer userId);

}
