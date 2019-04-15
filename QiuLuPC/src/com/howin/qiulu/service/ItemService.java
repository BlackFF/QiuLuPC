package com.howin.qiulu.service;

import com.howin.qiulu.result.QiuluResult;

public interface ItemService {
	//根据分类查找商品
	public QiuluResult findItemByCategoryId(Integer categoryId);
	//根据商品找sku
	public QiuluResult findSkuByItemId(Integer itemId);
	//根据卖点查找Sku
	public QiuluResult findSkuBySellPoint(String sellPoint,Integer pageSize,Integer pageNo);
	//根据SkuId找ItemId，然后去找属于这个ItemsId的所有Sku
	public QiuluResult findSkuBySkuId(Integer id);
	public QiuluResult  findSkuByTime(Integer pageSize,Integer pageNo);
	
	//根据SkuId找到一件商品
	public QiuluResult querySkuById(Integer id);
	
	public QiuluResult findItemBySkuId(Integer id);
	//根据价格降序查找
	public QiuluResult findSkuByPrice(Integer id,Integer pageSize,Integer pageNo);
	//根据价格升序查找
	public QiuluResult findSkuByPrice2(Integer id,Integer pageSize,Integer pageNo);
	
	public QiuluResult queryItemByRepertory(Integer id,Integer pageSize,Integer pageNo);
	//根据人气查找商品
	public QiuluResult findSkuByFavrites(Integer id, Integer pageSize, Integer pageNo);
	//根据区间价格和销量或者人气查找商品
	public QiuluResult findSkuByTypeByPrice(Integer id,Integer type,Double min,Double max,Integer pageSize,Integer pageNo);
	
	//相似商品查找
	public QiuluResult querySimilarSku(Integer categoryId,Integer id);
	
	//销量排行榜（前六 ）
	public QiuluResult findSkuBysalesVolume();
	
	//查找活动商品(首页展示)
	public QiuluResult findOnSaleSkuOfIndex();
	
	//查找所有活动商品（包含分页）
	public QiuluResult findOnSaleSku(Integer pageSize, Integer pageNo);
}
