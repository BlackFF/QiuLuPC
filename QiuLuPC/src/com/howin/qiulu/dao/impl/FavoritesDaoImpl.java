package com.howin.qiulu.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import com.howin.qiulu.dao.FavoritesDao;
import com.howin.qiulu.pojo.Favorites;
import com.howin.qiulu.pojo.FavoritesResult;
@Repository
public class FavoritesDaoImpl extends HibernateDaoSupport implements FavoritesDao{
   @Resource
   private SessionFactory sessionFactory;
  
	@Autowired
	public void setMySessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	/**
	 * @author 张磊
	 * @Description: 将商品加入到收藏夹中
	 * @return QiuluResult
	 * @date 2017年2月22日 上午9:30:25
	 * @param skuId
	 * @param httpSession
	 * @param request
	 * @param response
	 * @return
	 */
	@Override
	public Favorites addFavorites(Favorites favorites) {
		Favorites fa=null;
		Session session=sessionFactory.openSession();
		Transaction tr= session.beginTransaction();
		Integer userId=favorites.getUserId();
		Integer skuId=favorites.getSkuId();
		 try {
			 //查询是否已添加过
			 fa=(Favorites)session.createQuery("from Favorites f where f.userId=:m and f.skuId=:n").setParameter("m", userId).setParameter("n", skuId).uniqueResult();
		     if(fa==null)  {
		    	 session.saveOrUpdate(favorites);
		    	 fa = favorites;
			  }
		        tr.commit();
		        session.close();
		} catch (Exception e) {
			// TODO: handle exception
		} 
			return fa;
		
	}
	
	/**
	 * @author 张磊
	 * @Description: 查看收藏夹中的商品
	 * @return List<FavoritesResult>
	 * @date 2017年2月22日 上午10:35:47
	 * @param httpSession
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<FavoritesResult> findFavorites(Integer pageSize, Integer offSet, Integer userId) {
		 List<FavoritesResult> list=null;
		try {
			
			
			Query query=sessionFactory.getCurrentSession().createQuery("from FavoritesResult f where f.id.userId=:i").setInteger("i", userId);
			query.setFirstResult(offSet);
			query.setMaxResults(pageSize);
			list=query.list();
		} catch (Exception e) {
			// TODO: handle exception
		} 
		return list;
			
	}
	/**
	 * @author 张磊
	 * @Description: 删除收藏夹中的商品
	 * @return Integer
	 * @date 2017年2月22日 上午11:07:39
	 * @param id
	 * @param httpSession
	 * @param request
	 * @param response
	 * @return
	 */
	@Override
	public boolean deleteFavorites(Integer[] id) {
		boolean n=true;
		Session session=sessionFactory.openSession();
		Transaction tr= session.beginTransaction();
		 try {
		        for (int i = 0; i < id.length; i++) {
		        	session.createQuery("delete from Favorites f where f.id=:i").setParameter("i", id[i]).executeUpdate();
				}
		       
		} catch (Exception e) {
			n=false;
		} 
		 if(n){
			 tr.commit();
		 }
		 session.close();
			
		 return n;
	}
	//获取收藏夹总条数
	@Override
	public Integer queryFavoritesNumber(Integer userId) {
     Long allRow=(Long)sessionFactory.getCurrentSession().createQuery("select count(*) from Favorites f where f.userId=:i").setParameter("i",userId).uniqueResult();
	 return allRow.intValue();
	}
	
	
	/**
	 * @author 张磊
	 * @Description:查看用户是否收藏某件商品
	 * @return QiuluResult
	 * @date 2017年3月28日 上午10:39:55
	 * @param session
	 * @param skuId
	 * @return
	 */
	@Override
	public Favorites queryFavoritesBySkuId(Integer userId,Integer skuId) {
		Favorites fa=null;
		 Session session=sessionFactory.openSession();
		 try {
			 fa=(Favorites)session.createQuery("from Favorites f where f.userId=:m and f.skuId=:i").setParameter("m", userId).setParameter("i", skuId).uniqueResult();
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		 session.close();
		return fa;
	}
	
}
