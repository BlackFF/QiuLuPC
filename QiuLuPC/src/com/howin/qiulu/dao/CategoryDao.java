package com.howin.qiulu.dao;

import java.util.List;
import com.howin.qiulu.pojo.Category;
import com.howin.qiulu.pojo.Item;
import com.howin.qiulu.pojo.Sku;
import com.howin.qiulu.result.CategoryObject;
import com.howin.qiulu.result.CategoryResult;

public interface CategoryDao {

	//查看所有的最上级类目
	public List<Category> queryParentCategory();
	//查看所有二级类目
	public List<CategoryResult> queryAllCategoryName(List<Category> list1);
	
    //通过类目ID 查询类目
	public	Category queryCategory(Integer id);
	
	public List<Integer> queryAllCategory(Category c);
	void clearList();
	//根据分类查找商品
	public Integer findSkuByItemId(List<Integer> categoryId);
	
	
	public List<Sku> findItemByCategoryId(List<Integer> list,Integer pageSize,Integer offSet);
	
	//查看同级类目
	public List<CategoryObject> queryEqualCategory(Integer id);
	
	//查看下级类目
	public List<CategoryObject> querySubordinateCategory(Integer id);

	
	//查看商品类目路径
	public Category queryCategoryTree(Integer id);

	

}
