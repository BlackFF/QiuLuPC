package com.howin.qiulu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.howin.qiulu.result.QiuluResult;
import com.howin.qiulu.service.SearchService;

@Controller
@RequestMapping("/search")
public class SearchController {
	
	@Autowired
	private SearchService searchService;

	@RequestMapping("/searchItem")
	@ResponseBody
	public QiuluResult  searchItem(String sellPoint,Integer pageNo){
		QiuluResult qiuluResult=new QiuluResult();
		if(pageNo==null){
			pageNo=1;
		}
		//根据关键词搜索商品
		QiuluResult result = searchService.searchItem(sellPoint,20,pageNo);
		
		return result;
	}
	
	/**
	 * 
	 * @author 张磊
	 * @Description: 根据销量排序查找商品
	 * @return QiuluResult
	 * @date 2017年4月18日 下午5:20:46
	 * @param id
	 * @param pageNo
	 * @return
	 */
	@RequestMapping("/queryItemBySellPointAndRepertory")
	@ResponseBody
	public QiuluResult queryItemBySellPointAndRepertory(String sellPoint,Integer pageNo){
		QiuluResult qiuluResult=new QiuluResult();
		if(pageNo==null){
			pageNo=1;
		}
		qiuluResult=searchService.queryItemBySellPointAndRepertory(sellPoint,20,pageNo);
	    return qiuluResult;
	
	}
	

/**
 * 
 * @author 张磊
 * @Description: 根据人气排序查找商品
 * @return QiuluResult
 * @date 2017年4月18日 下午5:20:46
 * @param id
 * @param pageNo
 * @return
 */
@RequestMapping("/queryItemBySellPointAndFavrites")
@ResponseBody
public QiuluResult queryItemBySellPointAndFavrites(String sellPoint,Integer pageNo){
	QiuluResult qiuluResult=new QiuluResult();
	if(pageNo==null){
		pageNo=1;
	}
	qiuluResult=searchService.queryItemBySellPointAndFavrites(sellPoint,20,pageNo);
	return qiuluResult;
	
}

/**
 * 根据价格降序查找
 * @param id分类id
 * @param pageNo
 * @return
 */
@RequestMapping("/findSkuByPriceDesc")
@ResponseBody
public QiuluResult findSkuByPriceDesc(String sellPoint,Integer pageNo){
	if(pageNo==null){
		pageNo=1;
	}
	QiuluResult result=searchService.findSkuByPriceDesc(sellPoint,20, pageNo);
	return result;
	
}

/**
 * 根据价格升序查找
 * @param id分类id
 * @param pageNo
 * @return
 */
@RequestMapping("/findSkuByPriceAsc")
@ResponseBody
public QiuluResult findSkuByPriceAsc(String sellPoint,Integer pageNo){
if(pageNo==null){
		pageNo=1;
	}
	QiuluResult result=searchService.findSkuByPriceAsc(sellPoint,20, pageNo);
	return result;
	
}

/**
 * 根据区间价格和销量或者人气查找商品
 * @param id分类id
 * @param type 1，销量；2，人气
 * @param min价格最小值
 * @param max价格最大值
 * @param pageNo
 * @return
 */
@RequestMapping("/findItemByTypeByPrice")
@ResponseBody
public QiuluResult findItemByTypeByPrice(String sellPoint,Integer type,Double min,Double max,Integer pageNo){
	QiuluResult result=new QiuluResult();
	if(pageNo==null){
		pageNo=1;
	}
	result=searchService.findItemByTypeByPrice(sellPoint, type, min, max, 20, pageNo);
	return result;
}
}
