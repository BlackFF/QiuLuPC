package com.howin.qiulu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.howin.qiulu.pojo.Item;
import com.howin.qiulu.result.QiuluResult;
import com.howin.qiulu.service.ItemService;

@Controller
@RequestMapping("/Item")
public class ItemsController {
	@Autowired
	private ItemService itemService;
	
	/*** 
	* Title: 根据分类查找商品
	* Description: 
	* @author 杨冰冰
	* @date 2017年2月21日 上午10:50:25
	* 
	*/
	@RequestMapping("/findItem")
	@ResponseBody
	public QiuluResult findItemByCategoryId(Integer categoryId){
		QiuluResult result=itemService.findItemByCategoryId(categoryId);
		return result;
	}
	
	/*** 
	* Title: 根据商品找sku
	* Description: 
	* @author 杨冰冰
	* @date 2017年2月21日 上午11:07:39
	* 
	*/
	@RequestMapping("/findSku")
	@ResponseBody
	public QiuluResult findSkuByItemId(Integer itemId){
		QiuluResult result=itemService.findSkuByItemId(itemId);
		return result;
	}
	
	/*** 
	* Title: 根据卖点查找Sku
	* Description: 
	* @author 杨冰冰
	* @date 2017年2月21日 下午4:30:35
	* 
	*/
	@RequestMapping("/findSkuBySellPoint")
	@ResponseBody
	public QiuluResult findSkuBySellPoint(String sellPoint,Integer pageNo){
		if(pageNo==null){
			pageNo=1;
		}
		
		QiuluResult result=itemService.findSkuBySellPoint(sellPoint,5,pageNo);
		return result;
	}
	
	/*** 
	* Title: 根据SkuId找ItemId，然后去找属于这个ItemsId的所有Sku
	* Description: 
	* @author 杨冰冰
	* @date 2017年2月22日 下午2:33:45
	* 
	*/
	@RequestMapping("/findSkuBySkuId")
	@ResponseBody
	public QiuluResult findSkuBySkuId(Integer id){
		QiuluResult result=itemService.findSkuBySkuId(id);
		return result;
	}
	/**
	 * 根据skuId找item
	 * @param id
	 * @return
	 */
	@RequestMapping("/findItemBySkuId")
	@ResponseBody
	public QiuluResult findItemBySkuId(Integer id){
		QiuluResult result=itemService.findItemBySkuId(id);
		return result;
	}
	/**
	 * 上新(前6个)
	 * @param pageNo
	 * @return
	 */
	@RequestMapping("/findSkuByTimes") 
	@ResponseBody
	public QiuluResult findSkuByTime(Integer pageNo){
		if(pageNo==null){
			pageNo=1;
		}
		QiuluResult result=itemService.findSkuByTime(6,pageNo);
	
		return result;
	}
	
	/**
	 * 上新商品查找
	 * @param pageNo
	 * @return
	 */
	@RequestMapping("/findSkuByOnNew") 
	@ResponseBody
	public QiuluResult findSkuByOnNew(Integer pageNo){
		if(pageNo==null){
			pageNo=1;
		}
		QiuluResult result=itemService.findSkuByTime(20,pageNo);
		
		return result;
	}
	/**
	 * 根据价格降序查找
	 * @param id分类id
	 * @param pageNo
	 * @return
	 */
	@RequestMapping("/findSkuByPriceDesc")
	@ResponseBody
	public QiuluResult findSkuByPrice(Integer id,Integer pageNo){
		if(pageNo==null){
			pageNo=1;
		}
		QiuluResult result=itemService.findSkuByPrice(id,20, pageNo);
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
	public QiuluResult findSkuByPrice2(Integer id,Integer pageNo){
	if(pageNo==null){
			pageNo=1;
		}
		QiuluResult result=itemService.findSkuByPrice2(id,20, pageNo);
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
	@RequestMapping("/queryItemByRepertory")
	@ResponseBody
	public QiuluResult queryItemByRepertory(Integer id,Integer pageNo){
		QiuluResult qiuluResult=new QiuluResult();
		if(pageNo==null){
			pageNo=1;
		}
		qiuluResult=itemService.queryItemByRepertory(id,20,pageNo);
	    return qiuluResult;
	
	}
	
	/**
	 * 根据人气查找商品
	 * @param id分类id
	 * @param pageNo
	 * @return
	 */
	@RequestMapping("/findSkuByFavrites")
	@ResponseBody
	public QiuluResult findSkuByFavrites(Integer id,Integer pageNo){
		QiuluResult result=new QiuluResult();
		if(pageNo==null){
			pageNo=1;
		}
		result=itemService.findSkuByFavrites(id, 20, pageNo);
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
	@RequestMapping("/findSkuByTypeByPrice")
	@ResponseBody
	public QiuluResult findSkuByTypeByPrice(Integer id,Integer type,Double min,Double max,Integer pageNo){
		QiuluResult result=new QiuluResult();
		if(pageNo==null){
			pageNo=1;
		}
		result=itemService.findSkuByTypeByPrice(id, type, min, max, 20, pageNo);
		return result;
	}
	/**
	 * 
	 * @author 张磊
	 * @Description: 查询相似的商品
	 * @return QiuluResult
	 * @date 2017年4月5日 上午11:29:02
	 * @param skuId
	 * @return
	 */
	@RequestMapping("/querySimilarSku")
	@ResponseBody
	public QiuluResult querySimilarSku(Integer skuId){
		QiuluResult q=itemService.findItemBySkuId(skuId);
		Item item=(Item)q.getObject();
		Integer categoryId=item.getCategoryId();
		Integer id=item.getId();
		QiuluResult result=itemService.querySimilarSku(categoryId,id);
		return result;
   }
	
	@RequestMapping("/findSkuById")
	@ResponseBody
	public QiuluResult findSkuById(Integer id){
		QiuluResult result=new QiuluResult();
		result=itemService.querySkuById(id);
		return result;
	}
	
	/**
	 * 
	 * @author 张磊
	 * @Description: 销量排行榜（前六 ）
	 * @return QiuluResult
	 * @date 2017年4月21日 下午3:52:38
	 * @return
	 */
	@RequestMapping("/findSkuBysalesVolume")
	@ResponseBody
	public QiuluResult findSkuBysalesVolume(){
		QiuluResult result=new QiuluResult();
		result=itemService.findSkuBysalesVolume();
		return result;
	}
	
	/**
	 * 
	 * @author 张磊
	 * @Description: 查找活动商品(首页展示)
	 * @return QiuluResult
	 * @date 2017年4月24日 上午9:47:15
	 * @return
	 */
	@RequestMapping("/findOnSaleSkuOfIndex")
	@ResponseBody
	public QiuluResult findOnSaleSkuOfIndex(){
		QiuluResult result=new QiuluResult();
		result=itemService.findOnSaleSkuOfIndex();
		return result;
	}
	
	
	/**
	 * 
	 * @author 张磊
	 * @Description: 查找所有活动商品（包含分页）
	 * @return QiuluResult
	 * @date 2017年4月24日 上午9:47:15
	 * @return
	 */
	@RequestMapping("/findOnSaleSku")
	@ResponseBody
	public QiuluResult findOnSaleSku(Integer pageNo){
		QiuluResult result=new QiuluResult();
		if(pageNo==null){
			pageNo=1;
		}
		result=itemService.findOnSaleSku(5, pageNo);
		return result;
	}
	
	
	
	
}