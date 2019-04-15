package com.howin.qiulu.service;

import com.howin.qiulu.result.QiuluResult;

public interface SearchService {

	//根据关键词搜索商品
	public QiuluResult searchItem(String sellPoint,Integer pageSize, Integer pageNo);

	//根据关键词和销量搜索商品
	public QiuluResult queryItemBySellPointAndRepertory(String sellPoint, Integer i, Integer pageNo);

	//根据关键词和人气搜索商品
	public QiuluResult queryItemBySellPointAndFavrites(String sellPoint, int i, Integer pageNo);

	public QiuluResult findSkuByPriceDesc(String sellPoint, Integer pageSize, Integer pageNo);

	public QiuluResult findSkuByPriceAsc(String sellPoint, Integer pageSize, Integer pageNo);

	public QiuluResult findItemByTypeByPrice(String sellPoint, Integer type, Double min, Double max, int i,
			Integer pageNo);

	
}
