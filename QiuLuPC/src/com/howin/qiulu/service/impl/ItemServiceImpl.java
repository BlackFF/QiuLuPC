package com.howin.qiulu.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.howin.qiulu.Util.Page;
import com.howin.qiulu.dao.CategoryDao;
import com.howin.qiulu.dao.ItemDao;
import com.howin.qiulu.pojo.Category;
import com.howin.qiulu.pojo.Sku;
import com.howin.qiulu.result.QiuluResult;
import com.howin.qiulu.service.ItemService;
@Service("ItemService")
public class ItemServiceImpl implements ItemService{
	@Autowired
	private ItemDao itemDao;
 

	@Resource
	private CategoryDao categoryDao;

	/*** 
	* Title: 根据分类查找商品
	* Description: 
	* @author 杨冰冰
	* @date 2017年2月21日 上午10:50:25
	* 
	*/
	@Override
	public QiuluResult findItemByCategoryId(Integer categoryId) {
		QiuluResult result=new QiuluResult();
			result.setStatus(true);
			result.setObject(itemDao.findItemByCategoryId(categoryId));
		return result;
	}

	/*** 
	* Title: 根据商品找sku
	* Description: 
	* @author 杨冰冰
	* @date 2017年2月21日 上午11:07:39
	* 
	*/
	@Override
	public QiuluResult findSkuByItemId(Integer itemId) {
		QiuluResult result=new QiuluResult();
		result.setStatus(true);
		result.setObject(itemDao.findSkuByItemId(itemId));
		return result;
	}
	/*** 
	* Title: 根据卖点查找Sku
	* Description: 
	* @author 杨冰冰
	* @date 2017年2月21日 下午4:30:35
	* 
	*/
	@Override
	public QiuluResult findSkuBySellPoint(String sellPoint,Integer pageSize,Integer pageNo) {
		Page page=new Page();
		//查询商品的总条数
		Integer allRow=itemDao.querySkuNumber(sellPoint);
		//当前内容
		Integer offSet=page.countOffset(pageNo, pageSize);
		//获取当前页内容
		List<Sku> list=itemDao.findSkuBySellPoint(sellPoint,pageSize,offSet);
		
		page.setList(list);
		page.setPageNo(pageNo);
		page.setPageSize(pageSize);
		page.setTotalRecords(allRow);
		QiuluResult result=new QiuluResult();
		result.setStatus(true);
		result.setObject(page);
		return result;
	}
	/*** 
	* Title: 根据SkuId找ItemId，然后去找属于这个ItemsId的所有Sku
	* Description: 
	* @author 杨冰冰
	* @date 2017年2月22日 下午2:33:45
	* 
	*/
	@Override
	public QiuluResult findSkuBySkuId(Integer id) {
		QiuluResult result=new QiuluResult();
		result.setStatus(true);
		result.setObject(itemDao.findSkuBySkuId(id));
		return result;
	}

	@Override
	public QiuluResult findSkuByTime(Integer pageSize,Integer pageNo) {
		QiuluResult result=new QiuluResult();
		Page page=new Page();
		Integer allRow=itemDao.findSkuByTimes();
		Integer offSet=page.countOffset(pageNo, pageSize);
		List<Sku> list=itemDao.findSkuByTime(pageSize, offSet);
		page.setList(list);
		page.setPageNo(pageNo);
		page.setPageSize(pageSize);
		page.setTotalRecords(allRow);
		result.setStatus(true);
		result.setObject(page);
		return result;
	}
	//根据SkuId找到一件商品
	@Override
	public QiuluResult querySkuById(Integer id) {
		QiuluResult result=new QiuluResult();
		result.setStatus(true);
		result.setObject(itemDao.querySkuById(id));
		return result;
	}

	@Override
	public QiuluResult findItemBySkuId(Integer id) {
		QiuluResult result=new QiuluResult();
		result.setStatus(true);
		result.setObject(itemDao.findItemBySkuId(id));
		return result;
	}
	/**
	 * 根据价格升序查找
	 * @param id
	 * @param pageNo
	 * @return
	 */
	@Override
	public QiuluResult findSkuByPrice(Integer id,Integer pageSize,Integer pageNo) {
		categoryDao.clearList();
		Category c=categoryDao.queryCategory(id);
		List list=categoryDao.queryAllCategory(c);
		Page page=new Page();
		List<Sku> li =new ArrayList<Sku>();
		QiuluResult result=new QiuluResult();
		//查询商品的总条数
		Integer allRow=itemDao.findSkuByPriceNum(list);
		//当前内容
		Integer offSet=page.countOffset(pageNo, pageSize);
		//获取当前页内容
		List<Sku> list1=itemDao.findSkuByPrice(list,pageSize, offSet);
				
		page.setList(list1);
		page.setPageNo(pageNo);
		page.setPageSize(pageSize);
		page.setTotalRecords(allRow);
		result.setStatus(true);
		result.setObject(page);
		return result;
	}
	/**
	 * 根据价格降序查找
	 * @param id
	 * @param pageNo
	 * @return
	 */
	@Override
	public QiuluResult findSkuByPrice2(Integer id,Integer pageSize,Integer pageNo) {
		QiuluResult result=new QiuluResult();
		categoryDao.clearList();
		Category c=categoryDao.queryCategory(id);
		List list=categoryDao.queryAllCategory(c);
		Page page=new Page();
		//查询商品的总条数
		Integer allRow=itemDao.findSkuByPrice2Num(list);
		//当前内容
		Integer offSet=page.countOffset(pageNo, pageSize);
		//获取当前页内容
		List<Sku> list1=itemDao.findSkuByPrice2(list,pageSize, offSet);
				
		page.setList(list1);
		page.setPageNo(pageNo);
		page.setPageSize(pageSize);
		page.setTotalRecords(allRow);
		result.setStatus(true);
		result.setObject(page);
		return result;
	}                                        

	@Override
	public QiuluResult queryItemByRepertory(Integer id, Integer pageSize, Integer pageNo) {
		categoryDao.clearList();
		Category c=categoryDao.queryCategory(id);
		List list=categoryDao.queryAllCategory(c);
		Page page=new Page();
		List<Sku> li =new ArrayList<Sku>();
		QiuluResult result=new QiuluResult();
		
			Integer in=categoryDao.findSkuByItemId(list);
			Integer offSet=page.countOffset(pageNo, pageSize);
			li=itemDao.queryItemByRepertory(list, pageSize, offSet);
			page.setList(li);
			page.setPageNo(pageNo);
			page.setPageSize(pageSize);
			page.setTotalRecords(in);
			result.setStatus(true);
			result.setObject(page);
		return result;
	}
	/**
	 * 根据人气查找商品
	 * @param id
	 * @param pageNo
	 * @return
	 */
	@Override
	public QiuluResult findSkuByFavrites(Integer id, Integer pageSize, Integer pageNo) {
		QiuluResult result=new QiuluResult();
		categoryDao.clearList();
		Category c=categoryDao.queryCategory(id);
		List list=categoryDao.queryAllCategory(c);
		Page page=new Page();
		List<Sku> li =new ArrayList<Sku>();
		//查询商品的总条数
		Integer allRow=itemDao.findSkuByFavrites2(list);
		//当前内容
		Integer offSet=page.countOffset(pageNo, pageSize);
		//获取当前页内容
		List<Sku> list1=itemDao.findSkuByFavrites(list, pageSize, offSet);
				
		page.setList(list1);
		page.setPageNo(pageNo);
		page.setPageSize(pageSize);
		page.setTotalRecords(allRow);
		result.setStatus(true);
		result.setObject(page);
		return result;
	}
	/**
	 * 根据区间价格和销量或者人气查找商品
	 * @param id
	 * @param type 1，销量；2，人气
	 * @param min价格最小值
	 * @param max价格最大值
	 * @param pageNo
	 * @return
	 */
	@Override
	public QiuluResult findSkuByTypeByPrice(Integer id,Integer type, Double min, Double max, Integer pageSize, Integer pageNo) {
		QiuluResult result=new QiuluResult();
		categoryDao.clearList();
		Category c=categoryDao.queryCategory(id);
		List list=categoryDao.queryAllCategory(c);
		Page page=new Page();
		//查询商品的总条数
		Integer allRow=itemDao.findSkuByTypeByPrice2(type, list, min, max);
		//当前内容
		Integer offSet=page.countOffset(pageNo, pageSize);
		//获取当前页内容
		List<Sku> list1=itemDao.findSkuByTypeByPrice(type, list, pageSize, offSet, min, max);
						
		page.setList(list1);
		page.setPageNo(pageNo);
		page.setPageSize(pageSize);
		page.setTotalRecords(allRow);
		result.setStatus(true);
		result.setObject(page);
		return result;
	}
	/**
	 * 查询相似的商品
	 */
	@Override
	public QiuluResult querySimilarSku(Integer categoryId,Integer id) {
		QiuluResult qiuluResult=new QiuluResult();
		
		//查看同级类目下 是否有其他商品
		List<Integer>  list= itemDao.queryItemBycategoryId(categoryId,id);
		if(list.size()==0){
			qiuluResult.setMessage("查询失败 同级没有相似的的商品");
			qiuluResult.setStatus(false);
			
		}
		else{
			List<Sku> list2=itemDao.querySimilarSku(list);
			qiuluResult.setMessage("成功");
			qiuluResult.setStatus(true);
			qiuluResult.setObject(list2);
		}
		
		return qiuluResult;
	}

	
	//销量排行榜（前六 ）
	@Override
	public QiuluResult findSkuBysalesVolume() {
		QiuluResult qiuluResult=new QiuluResult();
		List<Sku> list2=itemDao.findSkuBysalesVolume();
		qiuluResult.setMessage("成功");
		qiuluResult.setStatus(true);
		qiuluResult.setObject(list2);
		return qiuluResult;
	}

	
	//查找活动商品(首页展示)
	@Override
	public QiuluResult findOnSaleSkuOfIndex() {
		QiuluResult qiuluResult=new QiuluResult();
		List<Sku> list2=itemDao.findOnSaleSkuOfIndex();
		qiuluResult.setMessage("成功");
		qiuluResult.setStatus(true);
		qiuluResult.setObject(list2);
		return qiuluResult;
	
	}
   //查找所有活动商品（包含分页）
	@Override
	public QiuluResult findOnSaleSku(Integer pageSize, Integer pageNo) {
		Page page=new Page();
		//查询商品的总条数
		Integer allRow=itemDao.querySkuNumberOnSale();
		//当前内容
		Integer offSet=page.countOffset(pageNo, pageSize);
		//获取当前页内容
		List<Sku> list=itemDao.findOnSaleSku(pageSize,offSet);
		
		page.setList(list);
		page.setPageNo(pageNo);
		page.setPageSize(pageSize);
		page.setTotalRecords(allRow);
		QiuluResult result=new QiuluResult();
		result.setStatus(true);
		result.setObject(page);
		return result;
	}
}
