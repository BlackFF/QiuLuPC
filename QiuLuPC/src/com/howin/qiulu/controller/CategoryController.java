package com.howin.qiulu.controller;

import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.howin.qiulu.pojo.Item;
import com.howin.qiulu.result.CategoryObject;
import com.howin.qiulu.result.CategoryResult;
import com.howin.qiulu.result.QiuluResult;
import com.howin.qiulu.service.CategoryService;
import com.howin.qiulu.service.ItemService;

@Controller
public class CategoryController {
@Resource
private CategoryService categoryService;
@Resource
private ItemService itemService;


/**
 * 
 * @author 张磊
 * @Description: 查找首页类目列表 
 * @return QiuluResult
 * @date 2017年3月28日 上午9:31:33
 * @return
 */
@RequestMapping("/queryCategory")
@ResponseBody
public QiuluResult queryCategory(){
	List<CategoryResult> list=categoryService.queryAllCategoryName();
	QiuluResult qiuluResult=new QiuluResult();
	if(list==null){
		qiuluResult.setMessage("查询失败");
		qiuluResult.setStatus(false);
	}else{
		qiuluResult.setObject(list);
		qiuluResult.setStatus(true);
	}
	
	return qiuluResult;

}
/**
 * 
 * @author 张磊
 * @Description: 由类目的ID来 查找所有的Sku
 * @return QiuluResult
 * @date 2017年3月27日 下午4:47:57
 * @param id
 * @param pageNo
 * @return
 */
@RequestMapping("/queryCategoryById")
@ResponseBody
public QiuluResult queryCategoryById(Integer id,Integer pageNo){
	QiuluResult qiuluResult=new QiuluResult();
	if(pageNo==null){
		pageNo=1;
	}
	qiuluResult=categoryService.queryCategoryById(id,20,pageNo);
    return qiuluResult;
}
/**
 * 
 * @author 张磊
 * @Description: 查找同级类目
 * @return QiuluResult
 * @date 2017年3月28日 上午9:25:52
 * @param id
 * @return
 */
@RequestMapping("/queryEqualCategory")
@ResponseBody
public QiuluResult queryEqualCategory(Integer id){
	QiuluResult qiuluResult=categoryService.queryEqualCategory(id);
	return qiuluResult;
}
/**
 * 
 * @author 张磊
 * @Description: 查找下级类目
 * @return QiuluResult
 * @date 2017年3月28日 上午9:26:28
 * @param id
 * @return
 */
@RequestMapping("/querySubordinateCategory")
@ResponseBody
public QiuluResult querySubordinateCategory(Integer skuId){
	QiuluResult qiuluResult=categoryService.querySubordinateCategory(skuId);
	return qiuluResult;
}

/**
 * 
 * @author 张磊
 * @Description:查看商品类目路径
 * @return QiuluResult
 * @date 2017年3月31日 下午3:29:38
 * @param skuId
 * @return
 */
@RequestMapping("/queryCategoryTree")
@ResponseBody
public QiuluResult queryCategoryTree(Integer skuId){
	QiuluResult result=new QiuluResult();
	QiuluResult qiuluResult=itemService.findItemBySkuId(skuId);
	Item item=(Item)qiuluResult.getObject();
	Integer categoryId=item.getCategoryId();
	categoryService.clear();
	List<CategoryObject> list=categoryService.queryCategoryTree(categoryId);
	Collections.reverse(list);
	if(list.size()==0){
		result.setStatus(false);
		result.setMessage("查询出错");
	}
	else{
		result.setStatus(true);
		result.setObject(list);
	}
	return result;
}
/**
 * @author 张磊
 * @Description: 由类目Id来查询 当前类目的路径
 * @return QiuluResult
 * @date 2017年4月6日 上午9:02:53
 * @param categoryId
 * @return
 */
@RequestMapping("/queryCategoryTreeByCategoryId")
@ResponseBody
public QiuluResult queryCategoryTreeByCategoryId(Integer categoryId){
	QiuluResult result=new QiuluResult();
	categoryService.clear();
	List<CategoryObject> list=categoryService.queryCategoryTree(categoryId);
	Collections.reverse(list);
	if(list.size()==0){
		result.setStatus(false);
		result.setMessage("查询出错");
	}else{
		result.setStatus(true);
		result.setObject(list);
	}
	return result;
}
}