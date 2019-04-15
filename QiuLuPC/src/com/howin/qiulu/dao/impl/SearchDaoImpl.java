package com.howin.qiulu.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.howin.qiulu.dao.SearchDao;
import com.howin.qiulu.pojo.Sku;
import com.howin.qiulu.pojo.SkuFavorites;

@Repository
public class SearchDaoImpl implements SearchDao{

	@Autowired
	private SessionFactory sessionFactory;
	
	/** 
	* @Title: searchItem 
	* @date 2017年4月28日 上午9:45:55 
	* @author 魏吉鹏
	* @Description: 根据关键词进行模糊查询商品  使用 like
	* @param 关键字
	* @return
	* @return QiuluResult
	* @throws 
	*/ 
	@Override
	public List<Sku> searchItem(String key,Integer pageSize,Integer offSet) {
		Session session = sessionFactory.getCurrentSession();
		String hql = "From Sku where sellPointPinyin like '%"+key+"%' ";
		Query q=session.createQuery(hql);
		q.setFirstResult(offSet);
		q.setMaxResults(pageSize);
		List<Sku> list = q.list();
		return list;
	}

	
	/**
	 * 通过卖点来查找
	 */
	@Override
	public Integer findSkuNumberBy(String sellPoint) {
	   Session session=sessionFactory.openSession();
	   String hql = "select count(*) from Sku where sellPointPinyin like '%"+sellPoint+"%' ";
	   Long allRow=(Long) session.createQuery(hql).uniqueResult();
	   session.close();
	   return allRow.intValue();
	}


	/**
	 * 通过销量和卖点来搜索商品
	 */
	@Override
	public List<Sku> queryItemBySellPointAndRepertory(String sellPoint, Integer pageSize, Integer offSet) {
		 Session session=sessionFactory.openSession();
		 String hql = "From Sku where sellPointPinyin like '%"+sellPoint+"%' order by salesVolume desc";
		 Query q=session.createQuery(hql);
		 q.setFirstResult(offSet);
		 q.setMaxResults(pageSize);
		 List<Sku> list= q.list();
		 session.close();
		 return list;
	}

	/**
	 * 通过人气和卖点来搜索商品
	 */
	@Override
	public List<SkuFavorites> queryItemBySellPointAndFavrites(String sellPoint, int pageSize, Integer offSet) {
		 Session session=sessionFactory.openSession();
		 String hql = "From SkuFavorites s where s.id.status=0 and s.id.sellPointPinyin like '%"+sellPoint+"%' ";
		 Query q=session.createQuery(hql);
		 q.setFirstResult(offSet);
		 q.setMaxResults(pageSize);
		 List<SkuFavorites> list= q.list();
		 session.close();
		 return list;
	}

	/**
	 * 通过价格降序
	 */
	@Override
	public List<Sku> findSkuByPriceDesc(String sellPoint, Integer pageSize, Integer offSet) {
		 Session session=sessionFactory.openSession();
		 String hql = "From Sku s where s.status=0 and s.sellPointPinyin like '%"+sellPoint+"%' order by s.price desc ";
		 Query q=session.createQuery(hql);
		 q.setFirstResult(offSet);
		 q.setMaxResults(pageSize);
		 List<Sku> list= q.list();
		 session.close();
		 return list;
	}

	/**
	 * 通过价格升序
	 */
	@Override
	public List<Sku> findSkuByPriceAsc(String sellPoint, Integer pageSize, Integer offSet) {
		 Session session=sessionFactory.openSession();
		 String hql = "From Sku s where s.status=0 and s.sellPointPinyin like '%"+sellPoint+"%' order by s.price asc ";
		 Query q=session.createQuery(hql);
		 q.setFirstResult(offSet);
		 q.setMaxResults(pageSize);
		 List<Sku> list= q.list();
		 session.close();
		 return list;
	}


	@Override
	public Integer findSkuNumByTypeByPrice(Integer type, String sellPoint, Double min, Double max) {
	 Session session=sessionFactory.openSession();
		Long l=0l;
		if(min==null){
			min=(double)0;
		}
		if(max==null){
			max=(double)20000;
		}
		if(type==0){
			String sql2="select count(*) from Sku s where s.sellPointPinyin like '%"+sellPoint+"%' and (s.price between "+min+" and "+max+") and s.status=0";
			Query q=session.createQuery(sql2);
			l=  (long) q.uniqueResult();
		}
		if(type==3){
			String sql2="select count(*) from Sku s where s.sellPointPinyin like '%"+sellPoint+"%' and (s.price between "+min+" and "+max+") and s.status=0 ORDER BY s.price DESC";
			Query q=session.createQuery(sql2);
			l=  (long) q.uniqueResult();
		}
		if(type==4){
			String sql2="select count(*) from Sku s where s.sellPointPinyin like '%"+sellPoint+"%' and (s.price between "+min+" and "+max+") and s.status=0 ORDER BY s.price";
			Query q=session.createQuery(sql2);
			l=  (long) q.uniqueResult();
			
		}
		if(type==1){
			String sql2="select count(*) from Sku s where s.sellPointPinyin like '%"+sellPoint+"%' and (s.price between "+min+" and "+max+") and s.status=0 ORDER BY s.salesVolume DESC";
			Query q=session.createQuery(sql2);
			l=  (long) q.uniqueResult();
			
		}
		if(type==2){
			String sql2="select count(*) from SkuFavorites sfa where sfa.id.sellPointPinyin like '%"+sellPoint+"%' and (sfa.id.price between "+min+" and "+max+") and sfa.id.status=0 order by sfa.id.count desc";
			Query q=session.createQuery(sql2);
			l=  (long) q.uniqueResult();
			
		}
		session.close();
		return l.intValue();
	}


	@Override
	public List<Object> findItemByTypeByPrice(Integer type, String sellPoint, int pageSize, Integer offSet, Double min,
			Double max) {
		
		List<Object> li=new ArrayList<Object>();
		Session session=sessionFactory.openSession();
		Long l=0l;
		if(min==null){
			min=(double)0;
		}
		if(max==null){
			max=(double)20000;
		}
		if(type==0){
			String sql2="from Sku s where s.sellPointPinyin like '%"+sellPoint+"%' and (s.price between "+min+" and "+max+") and s.status=0";
			Query q=session.createQuery(sql2);
			q.setFirstResult(offSet);
			q.setMaxResults(pageSize);
			li=q.list();
		}
		if(type==3){
			String sql2="from Sku s where s.sellPointPinyin like '%"+sellPoint+"%' and (s.price between "+min+" and "+max+") and s.status=0 ORDER BY s.price DESC";
			Query q=session.createQuery(sql2);
			q.setFirstResult(offSet);
			q.setMaxResults(pageSize);
			li=q.list();
		}
		if(type==4){
			String sql2="from Sku s where s.sellPointPinyin like '%"+sellPoint+"%' and (s.price between "+min+" and "+max+") and s.status=0 ORDER BY s.price";
			Query q=session.createQuery(sql2);
			q.setFirstResult(offSet);
			q.setMaxResults(pageSize);
			li=q.list();
			
		}
		if(type==1){
			String sql2="from Sku s where s.sellPointPinyin like '%"+sellPoint+"%' and (s.price between "+min+" and "+max+") and s.status=0 ORDER BY s.salesVolume DESC";
			Query q=session.createQuery(sql2);
			q.setFirstResult(offSet);
			q.setMaxResults(pageSize);
			li=q.list();
			
		}
		if(type==2){
			String sql2="from SkuFavorites sfa where sfa.id.sellPointPinyin like '%"+sellPoint+"%' and (sfa.id.price between "+min+" and "+max+") and sfa.id.status=0 order by sfa.id.count desc";
			Query q=session.createQuery(sql2);
			q.setFirstResult(offSet);
			q.setMaxResults(pageSize);
			li=q.list();
			
		}
		session.close();
		return li;
	}
	
}
