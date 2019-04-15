package com.howin.qiulu.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.howin.qiulu.Util.HanyuPinyinHelper;
import com.howin.qiulu.Util.Page;
import com.howin.qiulu.dao.SearchDao;
import com.howin.qiulu.pojo.Sku;
import com.howin.qiulu.pojo.SkuFavorites;
import com.howin.qiulu.result.QiuluResult;
import com.howin.qiulu.service.SearchService;

@Service
public class SearchServiceImpl implements SearchService{

	@Autowired
	private SearchDao searchDao;
	
	/** 
	* @Title: searchItem 
	* @date 2017年4月28日 上午9:45:55 
	* @author 魏吉鹏
	* @Description: 根据关键词搜索商品
	* @return
	* @return QiuluResult
	* @throws 
	*/ 
	@Override
	public QiuluResult searchItem(String sellPoint,Integer pageSize,Integer pageNo) {
		Page page=new Page();
		List<Sku> li =new ArrayList<Sku>();
		QiuluResult result=new QiuluResult();
		    //查询商品的总条数
			Integer allRow=searchDao.findSkuNumberBy(HanyuPinyinHelper.toHanyuPinyin(sellPoint));
			
			Integer offSet=page.countOffset(pageNo, pageSize);
			List<Sku> list = searchDao.searchItem(HanyuPinyinHelper.toHanyuPinyin(sellPoint),pageSize,offSet);
			page.setList(list);
			page.setPageNo(pageNo);
			page.setPageSize(pageSize);
			page.setTotalRecords(allRow);
			result.setStatus(true);
			result.setObject(page);
		return result;
		
		
	}

	
	//根据关键词和销量搜索商品
	@Override
	public QiuluResult queryItemBySellPointAndRepertory(String sellPoint, Integer pageSize, Integer pageNo) {
		Page page=new Page();
		List<Sku> li =new ArrayList<Sku>();
		QiuluResult result=new QiuluResult();
		    //查询商品的总条数
			Integer allRow=searchDao.findSkuNumberBy(HanyuPinyinHelper.toHanyuPinyin(sellPoint));
			
			Integer offSet=page.countOffset(pageNo, pageSize);
			li=searchDao.queryItemBySellPointAndRepertory(HanyuPinyinHelper.toHanyuPinyin(sellPoint), pageSize, offSet);
			page.setList(li);
			page.setPageNo(pageNo);
			page.setPageSize(pageSize);
			page.setTotalRecords(allRow);
			result.setStatus(true);
			result.setObject(page);
		return result;
		
	}


	@Override
	public QiuluResult queryItemBySellPointAndFavrites(String sellPoint, int pageSize, Integer pageNo) {
		QiuluResult result=new QiuluResult();
		Page page=new Page();
		List<Sku> li =new ArrayList<Sku>();
		//查询商品的总条数
		Integer allRow=searchDao.findSkuNumberBy(HanyuPinyinHelper.toHanyuPinyin(sellPoint));
		//当前内容
		Integer offSet=page.countOffset(pageNo, pageSize);
		//获取当前页内容
		List<SkuFavorites> list1=searchDao.queryItemBySellPointAndFavrites(HanyuPinyinHelper.toHanyuPinyin(sellPoint), pageSize, offSet);
				
		page.setList(list1);
		page.setPageNo(pageNo);
		page.setPageSize(pageSize);
		page.setTotalRecords(allRow);
		result.setStatus(true);
		result.setObject(page);
		return result;
	}


	@Override
	public QiuluResult findSkuByPriceDesc(String sellPoint, Integer pageSize, Integer pageNo) {
		QiuluResult result=new QiuluResult();
		Page page=new Page();
		List<Sku> li =new ArrayList<Sku>();
		//查询商品的总条数
		Integer allRow=searchDao.findSkuNumberBy(HanyuPinyinHelper.toHanyuPinyin(sellPoint));
		//当前内容
		Integer offSet=page.countOffset(pageNo, pageSize);
		//获取当前页内容
		List<Sku> list1=searchDao.findSkuByPriceDesc(HanyuPinyinHelper.toHanyuPinyin(sellPoint), pageSize, offSet);
				
		page.setList(list1);
		page.setPageNo(pageNo);
		page.setPageSize(pageSize);
		page.setTotalRecords(allRow);
		result.setStatus(true);
		result.setObject(page);
		return result;
	}


	@Override
	public QiuluResult findSkuByPriceAsc(String sellPoint, Integer pageSize, Integer pageNo) {
		QiuluResult result=new QiuluResult();
		Page page=new Page();
		List<Sku> li =new ArrayList<Sku>();
		//查询商品的总条数
		Integer allRow=searchDao.findSkuNumberBy(HanyuPinyinHelper.toHanyuPinyin(sellPoint));
		//当前内容
		Integer offSet=page.countOffset(pageNo, pageSize);
		//获取当前页内容
		List<Sku> list1=searchDao.findSkuByPriceAsc(HanyuPinyinHelper.toHanyuPinyin(sellPoint), pageSize, offSet);
				
		page.setList(list1);
		page.setPageNo(pageNo);
		page.setPageSize(pageSize);
		page.setTotalRecords(allRow);
		result.setStatus(true);
		result.setObject(page);
		return result;
	}


	@Override
	public QiuluResult findItemByTypeByPrice(String sellPoint, Integer type, Double min, Double max, int pageSize,
			Integer pageNo) {
		QiuluResult result=new QiuluResult();
		Page page=new Page();
		//查询商品的总条数
		Integer allRow=searchDao.findSkuNumByTypeByPrice(type, HanyuPinyinHelper.toHanyuPinyin(sellPoint), min, max);
		//当前内容
		Integer offSet=page.countOffset(pageNo, pageSize);
		//获取当前页内容
		List<Object> list1=searchDao.findItemByTypeByPrice(type, HanyuPinyinHelper.toHanyuPinyin(sellPoint), pageSize, offSet, min, max);
						
		page.setList(list1);
		page.setPageNo(pageNo);
		page.setPageSize(pageSize);
		page.setTotalRecords(allRow);
		result.setStatus(true);
		result.setObject(page);
		return result;
	}

}
