package com.howin.qiulu.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.howin.qiulu.Util.Page;
import com.howin.qiulu.dao.CategoryDao;
import com.howin.qiulu.dao.ItemDao;
import com.howin.qiulu.pojo.Category;
import com.howin.qiulu.pojo.Sku;
import com.howin.qiulu.result.CategoryObject;
import com.howin.qiulu.result.CategoryResult;
import com.howin.qiulu.result.QiuluResult;
import com.howin.qiulu.service.CategoryService;

@Service("CategoryService")
public class CategoryServiceImpl implements CategoryService {
	@Resource
	private CategoryDao categoryDao;
	@Resource
	private ItemDao itemDao;

	@Override
	public List<CategoryResult> queryAllCategoryName() {
		List<Category> categorie = categoryDao.queryParentCategory();
		List<CategoryResult> list = categoryDao.queryAllCategoryName(categorie);
		return list;
	}

	/**
	 * @author 张磊
	 * @Description: 由类目的ID来 查找所有的Sku
	 * @return QiuluResult
	 * @date 2017年3月27日 下午4:47:57
	 * @param id
	 * @param pageNo
	 * @return
	 */
	@Override
	public QiuluResult queryCategoryById(Integer id, Integer pageSize, Integer pageNo) {
		categoryDao.clearList();
		Category c = categoryDao.queryCategory(id);
		List list = categoryDao.queryAllCategory(c);
		Page page = new Page();
		List<Sku> li = new ArrayList<Sku>();
		QiuluResult result = new QiuluResult();
		Integer in = categoryDao.findSkuByItemId(list);
		Integer offSet = page.countOffset(pageNo, pageSize);
		li = categoryDao.findItemByCategoryId(list, pageSize, offSet);
		page.setList(li);
		page.setPageNo(pageNo);
		page.setPageSize(pageSize);
		page.setTotalRecords(in);
		result.setStatus(true);
		result.setObject(page);
		return result;
	}

	/**
	 * @author 张磊
	 * @Description: 查找同级类目
	 * @return QiuluResult
	 * @date 2017年3月28日 上午9:25:52
	 * @param id
	 * @return
	 */
	@Override
	public QiuluResult queryEqualCategory(Integer id) {
		QiuluResult result = new QiuluResult();
		List<CategoryObject> list = categoryDao.queryEqualCategory(id);
		if (list == null) {
			result.setStatus(false);
			result.setMessage("查询出错");
		} else {
			result.setStatus(true);
			result.setObject(list);
		}
		return result;
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
	@Override
	public QiuluResult querySubordinateCategory(Integer id) {
		QiuluResult result = new QiuluResult();
		List<CategoryObject> list = categoryDao.querySubordinateCategory(id);
		if (list.size() == 0) {
			result.setStatus(false);
			result.setMessage("查询出错");
		} else {
			result.setStatus(true);
			result.setObject(list);
		}
		return result;
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
	List<CategoryObject> list = new ArrayList<CategoryObject>();

	@Override
	public List<CategoryObject> queryCategoryTree(Integer categoryId) {
		Category category = categoryDao.queryCategoryTree(categoryId);
		Integer x = category.getParentId();
		CategoryObject cate = new CategoryObject();
		cate.setId(category.getId());
		cate.setName(category.getName());
		list.add(cate);
		if (x == null) {

		} else {
			queryCategoryTree(x);
		}
		return list;
	}

	@Override
	public void clear() {
		list.clear();

	}

}
