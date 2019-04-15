package com.howin.qiulu.dao;

import java.util.List;

import com.howin.qiulu.pojo.Favorites;
import com.howin.qiulu.pojo.FavoritesResult;

public interface FavoritesDao {
	//将商品加入到收藏夹中
	Favorites addFavorites(Favorites favorites);
    //查看收藏夹中的商品
	List<FavoritesResult> findFavorites(Integer pageSize, Integer offSet,Integer userId );
	//删除收藏夹中的商品
	boolean deleteFavorites(Integer[] id);
	//获取该用户的收藏夹商品的数量
	Integer queryFavoritesNumber(Integer userId);
	//查看用户是否收藏某件商品
	Favorites queryFavoritesBySkuId(Integer userId,Integer skuId);

}
