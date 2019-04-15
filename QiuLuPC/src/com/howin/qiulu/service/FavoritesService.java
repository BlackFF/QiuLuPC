package com.howin.qiulu.service;

import com.howin.qiulu.pojo.Favorites;
import com.howin.qiulu.result.QiuluResult;

public interface FavoritesService {
	// 将商品加入到收藏夹中
	QiuluResult addFavorites(Favorites favorites);
    //查看收藏夹中的商品
	QiuluResult findFavorites(Integer pageNo,Integer pageSize, Integer userId);
	//删除收藏夹中的商品
	QiuluResult deleteFavorites(Integer[] id);
	//查看用户是否收藏某件商品
	QiuluResult queryFavoritesBySkuId(Integer userId,Integer skuId);
	public QiuluResult findFavoritesNum(Integer userId);
}
