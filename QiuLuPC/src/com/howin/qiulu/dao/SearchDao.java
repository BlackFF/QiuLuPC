package com.howin.qiulu.dao;

import java.util.List;

import com.howin.qiulu.pojo.Item;
import com.howin.qiulu.pojo.Sku;
import com.howin.qiulu.pojo.SkuFavorites;

public interface SearchDao {

	//根据关键词进行模糊查询商品  使用%like%
	public List<Sku> searchItem (String key,Integer pageSize,Integer offSet);
    
	//查找sku的总条数（分页用）
	public Integer findSkuNumberBy(String sellPoint);

	public List<Sku> queryItemBySellPointAndRepertory(String sellPoint, Integer pageSize, Integer offSet);

	public List<SkuFavorites> queryItemBySellPointAndFavrites(String sellPoint, int pageSize, Integer offSet);

	public List<Sku> findSkuByPriceDesc(String sellPoint, Integer pageSize, Integer offSet);

	public List<Sku> findSkuByPriceAsc(String sellPoint, Integer pageSize, Integer offSet);

	public Integer findSkuNumByTypeByPrice(Integer type, String sellPoint, Double min, Double max);

	public List<Object> findItemByTypeByPrice(Integer type, String sellPoint, int pageSize, Integer offSet, Double min,
			Double max);
}
