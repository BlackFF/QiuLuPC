package com.howin.qiulu.dao;

import java.util.List;

import com.howin.qiulu.pojo.ShoppingCart;
import com.howin.qiulu.pojo.ShoppingCartResult;

public interface ShoppingCartDao {
	// 将商品加入到购物车中
	Integer addShoppingCart(ShoppingCart shoppingCart);

	// 查看购物中的商品
	List<ShoppingCartResult> queryShoppingCart(Integer userId);

	// 清空用户购物车
	Integer deleteShoppingCart(Integer userId);

	// 删除用户所选中的商品（购物车中）
	Integer deleteShoppingCart(List list);

	// 修改购物车中的商品数量
	public void updateShoppingCartNumber(ShoppingCart shoppingCart);

	//查询购物车中是否已有该商品
	ShoppingCart checkShoppingCart(ShoppingCart shoppingCart);
	
	
	// 查询SKU的库存量
	Integer queryRepertory(Integer skuId);
	
    //查询购物车商品总条数
	Integer queryShoppingCartNumber(Integer userId);
	
	//通过购物车ID 查找购物车中的一条记录 
	ShoppingCart queryShoppingCartById(Integer id);
	//通过购物车ID集合 查找购物车中的多条记录
	List<ShoppingCart> queryShoppingCartByIds(List id);

	void saveShoppingCart(ShoppingCart shoppingCart);

}
