package com.howin.qiulu.service;

import java.util.List;

import com.howin.qiulu.pojo.Item;
import com.howin.qiulu.pojo.Sku;
import com.howin.qiulu.result.CategoryObject;
import com.howin.qiulu.result.CategoryResult;
import com.howin.qiulu.result.QiuluResult;

public interface CategoryService {
		//查看所有类目
		public List<CategoryResult> queryAllCategoryName();
		
		//public List queryCategoryById(Integer id);
		public QiuluResult queryCategoryById(Integer id,Integer pageSize,Integer pageNo);
		
		//查看同级类目
		public QiuluResult queryEqualCategory(Integer id);
		
		//查看下级类目
		public QiuluResult querySubordinateCategory(Integer id);
		
		//查看商品类目路径
		public List<CategoryObject> queryCategoryTree(Integer categoryId);
		public void clear();
	}

