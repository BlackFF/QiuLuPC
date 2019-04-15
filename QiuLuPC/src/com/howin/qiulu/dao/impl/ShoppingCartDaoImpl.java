package com.howin.qiulu.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;
import com.howin.qiulu.dao.ShoppingCartDao;
import com.howin.qiulu.pojo.OrderDetail;
import com.howin.qiulu.pojo.ShoppingCart;
import com.howin.qiulu.pojo.ShoppingCartResult;

import sun.rmi.server.UnicastRef;

@Repository
public class ShoppingCartDaoImpl extends HibernateDaoSupport implements ShoppingCartDao {

	@Resource
	private SessionFactory sessionFactory;

	@Autowired
	public void setMySessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	/**
	 * @author 张磊
	 * @Description: 将商品加入到购物车中
	 * @return QiuluResult
	 * @date 2017年2月20日 下午2:11:29
	 * @param shoppingCart
	 * @param httpSession
	 * @param request
	 * @param response
	 * @return
	 */
	@Override
	public Integer addShoppingCart(ShoppingCart shoppingCart) {
		Integer id = null;
		ShoppingCart sh = null;
		Session session = sessionFactory.openSession();
		Transaction tr = session.beginTransaction();
		Integer userId = shoppingCart.getUserId();
		Integer skuId = shoppingCart.getSkuId();
		try {
			// 查看购物车中是否有该商品
			sh = (ShoppingCart) session.createQuery("from ShoppingCart s where s.userId=:m and s.skuId=:n")
					.setParameter("m", userId).setParameter("n", skuId).uniqueResult();
			if (sh == null) {
				id = (int) session.save(shoppingCart);
				id = 1;
			} else {
				sh.setNumber(shoppingCart.getNumber() + sh.getNumber());
				sh.setTotal(shoppingCart.getTotal() + sh.getTotal());
				session.update(sh);
				id = 1;
			}
			tr.commit();
			session.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return id;
	}

	/**
	 * @author 张磊
	 * @Description: 查看购物中的商品
	 * @return QiuluResult
	 * @date 2017年2月20日 下午4:21:27
	 * @param httpSession
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<ShoppingCartResult> queryShoppingCart(Integer userId) {
		Session session=sessionFactory.openSession();
		Transaction tr=session.beginTransaction();
		List<ShoppingCart> list =(List<ShoppingCart>)session.createQuery("from ShoppingCart s where s.userId=:i").setParameter("i", userId).list();
		
	
		//查看购物车中的商品数量，是否一直满足库存。
		for (int i = 0; i <list.size(); i++) {
		ShoppingCart s=list.get(i);
		Integer x=queryRepertory(s.getSkuId());
		Integer y=s.getNumber();
		//库存已不足的情况下
		if(x<y){
		s.setStatus(2);
		session.update(s);
		}
		//库存足够的情况下
		else{
			//开始库存不满足，现在满足了，状态重新改为0
			if(list.get(i).getStatus()==2){
				s.setStatus(0);
			}
		}
	
		}
		tr.commit();
		session.close();
		
		//分页
		Query query= sessionFactory.getCurrentSession().createQuery("from ShoppingCartResult  s where s.id.userId=:i order by s.id.id asc")
				.setInteger("i", userId);
		List<ShoppingCartResult> list1=query.list();
		return list1;
	}

	/**
	 * @author 张磊
	 * @Description: 清空用户购物车
	 * @return QiuluResult
	 * @date 2017年2月21日 上午9:00:58
	 * @param httpSession
	 * @param request
	 * @param response
	 * @return
	 */
	@Override
	public Integer deleteShoppingCart(Integer userId) {
		Session session = sessionFactory.openSession();
		Transaction tr = session.beginTransaction();
		Integer x = session.createQuery("delete from ShoppingCart s where s.userId=:i").setParameter("i", userId)
				.executeUpdate();
		tr.commit();
		session.close();
		System.out.println(x);
		return x;
	}

	/**
	 * @author 张磊
	 * @Description: 删除用户所选中的商品（购物车中）
	 * @return QiuluResult
	 * @date 2017年2月21日 下午2:45:13
	 * @param httpSession
	 * @param request
	 * @param response
	 * @return
	 */
	@Override
	public Integer deleteShoppingCart(List list) {
		Integer m = 0;
		Session session = sessionFactory.openSession();
		Transaction tr = session.beginTransaction();
		for (int i = 0; i < list.size(); i++) {
			Integer id = (Integer) list.get(i);
			m = session.createQuery("delete from ShoppingCart s where s.id=:i").setParameter("i", id).executeUpdate();
		}
		tr.commit();
		session.close();
		return m;

	}

	/**
	 * @author 张磊
	 * @Description: 修改购物车中的商品数量
	 * @return QiuluResult
	 * @date 2017年2月21日 下午4:14:37
	 * @param shoppingCart
	 * @param httpSession
	 * @param request
	 * @param response
	 * @return
	 */
	@Override
	public void updateShoppingCartNumber(ShoppingCart shoppingCart) {
		Session session = sessionFactory.openSession();
		Transaction tr = session.beginTransaction();
		session.update(shoppingCart);
		tr.commit();
		session.close();
	}

	/**
	 *查看商品的库存量
	 */
	@Override
	public Integer queryRepertory(Integer skuId) {
		Integer x=null;
		Session session = sessionFactory.openSession();
		Transaction tr = session.beginTransaction();
		try {
			 x = (Integer) session.createQuery("select s.repertory from Sku s where s.id=:i")
					.setParameter("i", skuId).uniqueResult();
		} catch (Exception e) {
		
		}
	
		tr.commit();
		session.close();
		return x;
	}
  /**
   * 查看用户的购物车中是否已添加过某件商品
   */
	@Override
	public ShoppingCart checkShoppingCart(ShoppingCart shoppingCart) {
		ShoppingCart sh=null;
		Integer userId = shoppingCart.getUserId();
		Integer skuId = shoppingCart.getSkuId();
		Session session = sessionFactory.openSession();
		Transaction tr = session.beginTransaction();
		try {
		 sh = (ShoppingCart) session.createQuery("from ShoppingCart s where s.userId=:m and s.skuId=:n")
					.setParameter("m", userId).setParameter("n", skuId).uniqueResult();
		} catch (Exception e) {
			// TODO: handle exception
		} 
		tr.commit();
		session.close();
		return sh;
	}

	@Override
	public Integer queryShoppingCartNumber(Integer userId) {
		Session session = sessionFactory.openSession();
		Transaction tr = session.beginTransaction();
		Long allRow=(Long)session.createQuery("select count(*) from ShoppingCart s where s.userId=:i").setParameter("i", userId).uniqueResult();
		tr.commit();
		session.close();
		return allRow.intValue();
	}
	
	//通过购物车ID 查找购物车中的一条记录 
	@Override
	public ShoppingCart queryShoppingCartById(Integer id) {
           Session session=sessionFactory.openSession();
           ShoppingCart shoppingCart=(ShoppingCart)session.createQuery("from ShoppingCart s where s.id=:i").setParameter("i", id).uniqueResult();
		   session.close();
           return shoppingCart;
	}

	@Override
	public List<ShoppingCart> queryShoppingCartByIds(List ids) {
	
		List<ShoppingCart> list=new ArrayList<ShoppingCart>();
		  Session session=sessionFactory.openSession();
	      for (int i = 0; i < ids.size(); i++) {
		  Integer id=(Integer) ids.get(i);
		  ShoppingCart shoppingCart=(ShoppingCart)session.createQuery("from ShoppingCart s where s.id=:i").setParameter("i", id).uniqueResult();
		  list.add(shoppingCart);
		 
		  
	}
		
		return list;
	}

	@Override
	public void saveShoppingCart(ShoppingCart shoppingCart) {
		  Session session=sessionFactory.openSession();
	      session.save(shoppingCart);
	      session.close();
		
	}

}
