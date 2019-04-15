package com.howin.qiulu.dao;

import java.util.List;
import com.howin.qiulu.pojo.Item;
import com.howin.qiulu.pojo.Sku;
public interface ItemDao {
	//根据分类查找商品
	public List<Item> findItemByCategoryId(Integer categoryId);
	//根据商品找sku
	public List<Sku> findSkuByItemId(Integer itemId);
	//根据卖点查找Sku
	public List<Sku> findSkuBySellPoint(String sellPoint,Integer pageSize,Integer offSet);
	//根据SkuId找ItemId，然后去找属于这个ItemsId的所有Sku
	public List<Sku> findSkuBySkuId(Integer id);
	
	public Short findItemByCategoryId2(Integer categoryId);
	//根据分类id找找父节点找商品
	public List<Item> findItemByCategory(Integer categoryId);
	//查询SKU的总条数
	public Integer querySkuNumber(String sellPoint);
	//按上架的时间来查找SKU
	public List<Sku> findSkuByTime(Integer pageSize,Integer offSet);
	
	public Integer findSkuByTimes();
	
	//根据SkuId找到一件商品
	public Sku querySkuById(Integer id);
	
	//根据skuId找Item
	public Item findItemBySkuId(Integer id);
	//根据价格降序查找
	public List<Sku> findSkuByPrice(List<Integer> id,Integer pageSize,Integer offSet);
	//根据价格升序查找
	public List<Sku> findSkuByPrice2(List<Integer> id,Integer pageSize,Integer offSet);
	//根据价格降序查找总共
	public Integer findSkuByPriceNum(List<Integer> id);
	//根据价格升序查找总共
	public Integer findSkuByPrice2Num(List<Integer> id);
	
	//根据销量排名
	public List<Sku> queryItemByRepertory(List<Integer> list, Integer pageSize, Integer offSet);
	
	//根据人气查找商品
	public List<Sku> findSkuByFavrites(List<Integer> id,Integer pageSize,Integer offSet);
	
	public Integer findSkuByFavrites2(List<Integer> id);
	
	//根据区间价格和销量或者人气查找商品
	public List<Sku> findSkuByTypeByPrice(Integer type,List<Integer> id,Integer pageSize,Integer offSet,Double min,Double max);
	
	//根据区间价格和销量或者人气查找总共有多少商品
	public Integer findSkuByTypeByPrice2(Integer type,List<Integer> id,Double min,Double max);
	
	//查看同级类目下 是否有其他商品
	public List<Integer> queryItemBycategoryId(Integer categoryId, Integer id);
	
	//查询相似的商品
	public List<Sku> querySimilarSku(List<Integer> list);
	
	//销量排行榜（前六 ）
	public List<Sku> findSkuBysalesVolume();
	
	//查找活动商品(首页展示)
	public List<Sku> findOnSaleSkuOfIndex();
	
	//查找活动商品的总条数
	public Integer querySkuNumberOnSale();
	
	//获取当前页内容
	public List<Sku> findOnSaleSku(Integer pageSize, Integer offSet);

}
