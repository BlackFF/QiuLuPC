package com.howin.qiulu.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import com.howin.qiulu.dao.CategoryDao;
import com.howin.qiulu.pojo.Category;
import com.howin.qiulu.pojo.Sku;
import com.howin.qiulu.result.CategoryObject;
import com.howin.qiulu.result.CategoryResult;
@Repository("CategoryDao")
public class CategoryDaoImpl extends HibernateDaoSupport implements CategoryDao {
    List<Integer> li=new ArrayList<Integer>();
	@Resource
	private SessionFactory sessionFactory;

	@Autowired
	public void SetMySessionFactory() {
		super.setSessionFactory(sessionFactory);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Category> queryParentCategory() {
		Session session = sessionFactory.openSession();
		List<Category> list = session.createQuery("from Category c where c.parentId is null").list();
		session.close();
		return list;
	}
  
	
/**
 * 查看该类目下的 所有最下级类目
 */
	@SuppressWarnings("unchecked")
	@Override
	public List<CategoryResult> queryAllCategoryName(List<Category> list1) {
		List<CategoryResult> list = new ArrayList<CategoryResult>();
		Session session = sessionFactory.openSession();
		for (int i = 0; i < list1.size(); i++) {
			 CategoryObject category2=new CategoryObject();
			 CategoryResult cr = new CategoryResult();
			// 二级类目
			List<CategoryObject> list3 =new ArrayList<CategoryObject>();
			// 三级类目
			List<CategoryObject> list5 =new ArrayList<CategoryObject>();
			Short x=list1.get(i).getIsParent();
			if ( x==1) {
				Integer y=list1.get(i).getId();
				List<Category> list2 = session.createQuery("from Category c where c.parentId=:i")
						.setParameter("i", y).list();
				
				for (int j = 0; j < list2.size(); j++) {
					CategoryObject category=new CategoryObject();
					if (list2.get(j).getIsParent() == 1) {
						List<Category> list4 = session.createQuery("from Category c where c.parentId=:i")
								.setParameter("i", list2.get(j).getId()).list();
						for (int k = 0; k < list4.size(); k++) {
							   CategoryObject category1=new CategoryObject();
				               category1.setId(list4.get(k).getId());
				               category1.setName(list4.get(k).getName());
							   list5.add(category1);
						}
					}
		               category.setId(list2.get(j).getId());
		               category.setName(list2.get(j).getName());
					   list3.add(category);
				}
			}
               category2.setId(list1.get(i).getId());
               category2.setName(list1.get(i).getName());
			   cr.setParentName(category2);
			cr.setSonName(list3);
			cr.setChildName(list5);
			list.add(cr);

		}
		session.close();
		return list;

	}
	@Override
	public Category queryCategory(Integer id){
		Session session = sessionFactory.openSession();
		Category c =(Category)session.createQuery("from Category c where c.id=:i").setParameter("i", id).uniqueResult();
		session.close();
		return c;
	}
	
	@Override
	public List<Integer> queryAllCategory(Category c){
		if(c.getIsParent()==1){
			Session session = sessionFactory.openSession();
			List<Category> list = session.createQuery("from Category c where c.parentId=:i").setParameter("i", c.getId()).list();
			session.close();
				for (int i = 0; i < list.size(); i++) {
				Category x=queryCategory(list.get(i).getId());
				queryAllCategory(x);
			}
		}
		else{
			li.add(c.getId());
		}
	
		return li;
	}
	  @Override
	  public void clearList(){
		     li.clear();
	    }

	@Override
	public List<Sku> findItemByCategoryId(List<Integer> list,Integer pageSize,Integer offSet) {
		Session session=sessionFactory.openSession();

		List<Sku> list1=new ArrayList<Sku>();
		List<Integer> list3=new ArrayList<Integer>();
		for (int i = 0; i < list.size(); i++) {
			List list2=(List) session.createQuery("select id from Item where categoryId=:c").setInteger("c", list.get(i)).list();
               list3.addAll(list2);
		}
		
		StringBuffer hql3=new StringBuffer();
		   hql3.append("(");
	    	for(int i=0;i<list3.size();i++){
	    		hql3.append(list3.get(i).toString());
	    		hql3.append(",");
	    	}
	    	String hql2=hql3.toString().substring(0,hql3.toString().length()-1);
	    	hql2=hql2+")";
	    	System.out.println(hql2);
	    	String hql="from Sku where itemId in "+hql2+"";
	    	System.out.println(hql);
	    	
			Query q=session.createQuery(hql);
			q.setFirstResult(offSet);
			q.setMaxResults(pageSize);
			list1=q.list();
		    session.close();
		    return list1;
	}

	@Override
	public Integer findSkuByItemId(List<Integer> categoryId) {
		List<Integer> list2=new ArrayList<Integer>();
		Session session=sessionFactory.openSession();
		Long l=0l;
		for (int i = 0; i <categoryId.size(); i++) {
		List<Integer> list1=(List<Integer>)session.createQuery("select id from Item where categoryId=:c").setInteger("c", categoryId.get(i)).list();
			list2.addAll(list1);
		}
		
		for(int i=0;i<list2.size();i++){
			Long li=(Long) session.createQuery("select count(*) from Sku where itemId=:t").setParameter("t", list2.get(i)).uniqueResult();
		    l=l+li;
		}
		
		
		session.close();
		return l.intValue();
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
	public List<CategoryObject> queryEqualCategory(Integer id) {
		List<CategoryObject> list1=new ArrayList<CategoryObject>();
		Session session=sessionFactory.openSession();
		try {
	    Category c =(Category)session.createQuery("from Category c where c.id=:i").setParameter("i",id).uniqueResult();
		if(c.getParentId()==null){
		List<Category> list = session.createQuery("from Category c where c.parentId is null and c.id!=:m").setParameter("m",id).list();
		for (int i = 0; i <list.size(); i++) {
			CategoryObject categoryObject=new CategoryObject();
			categoryObject.setId(list.get(i).getId());
			categoryObject.setName(list.get(i).getName());
			list1.add(categoryObject);
			
		}
		}
		else{
			List<Category> list =session.createQuery("from Category c where c.parentId=:i and c.id!=:m").setParameter("i", c.getParentId()).setParameter("m", id).list();
			for (int i = 0; i <list.size(); i++) {
				CategoryObject categoryObject=new CategoryObject();
				categoryObject.setId(list.get(i).getId());
				categoryObject.setName(list.get(i).getName());
				list1.add(categoryObject);
			}
		}
		} catch (Exception e) {
			// TODO: handle exception
		}
		session.close();
		return list1;
	}

	/**
	 * @author 张磊
	 * @Description: 查找下级类目
	 * @return QiuluResult
	 * @date 2017年3月28日 上午9:26:28
	 * @param id
	 * @return
	 */
	@Override
	public List<CategoryObject> querySubordinateCategory(Integer id) {
		List<CategoryObject> list1=new ArrayList<CategoryObject>();
		Session session=sessionFactory.openSession();
		List<Category> list =session.createQuery("from Category c where c.parentId=:i and c.id!=:m").setParameter("m",id).setParameter("i",id).list();
		for (int i = 0; i <list.size(); i++) {
			CategoryObject categoryObject=new CategoryObject();
			categoryObject.setId(list.get(i).getId());
			categoryObject.setName(list.get(i).getName());
			list1.add(categoryObject);
		}
		session.close();
		return list1;
	}

	/**
	 * @author 张磊
	 * @Description:查看商品类目路径
	 * @return QiuluResult
	 * @date 2017年3月31日 下午3:29:38
	 * @param skuId
	 * @return
	 */
	@Override
	public Category queryCategoryTree(Integer id) {
		Session session=sessionFactory.openSession();
		Category category=(Category)session.createQuery("from Category c where c.id=:i").setParameter("i", id).uniqueResult();
		session.close();
		return category;
	}
	
	
	
}
