package com.howin.qiulu.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import com.howin.qiulu.dao.EvaluationDao;
import com.howin.qiulu.pojo.EvaluationResult;
@Repository("EvalutionDao")
public class EvaluationDaoImpl extends HibernateDaoSupport implements EvaluationDao{
	@Resource
	private SessionFactory sessionFactory;
	
	@Autowired
	public void setMySessionFactory(SessionFactory sessionFactory){  
		super.setSessionFactory(sessionFactory);
	}
	/**
	 * 查找评价
	 * @param id
	 * @return
	 */
	@Override
	public List<EvaluationResult> findEvaBySkuId(Integer id,Integer pageSize,Integer offSet) {
		Session session=sessionFactory.openSession();
		List<EvaluationResult> list=new ArrayList<EvaluationResult>();
		Query q=session.createQuery("from EvaluationResult e where e.id.skuId=:i and e.type=1").setParameter("i", id);
		q.setFirstResult(offSet);
		q.setMaxResults(pageSize);
		list=q.list();
		session.close();
		return list;
	}
	@Override
	public Integer findEvaBySkuIdNum(Integer id) {
		Session session=sessionFactory.openSession();
		Long l=(Long) session.createQuery("select count(*) from EvaluationResult e where e.id.skuId=:e and e.type=1").setParameter("e", id).uniqueResult();
		session.close(); 
		return l.intValue();
	}
	/**
	 * @author 杨冰冰
	 * @Description:评价页面查看所有已经完成订单
	 * @return QiuluResult
	 * @date 2017年4月11日 上午11:00:58
	 * @param session
	 * @return
	 */
	@Override
	public List<EvaluationResult> findOrderByStatus3(Integer userId,Integer pageSize,Integer offSet) {
		Session session=sessionFactory.openSession();
		Map<Object,Object> map=new HashMap<Object,Object>();
		List<EvaluationResult> list=new ArrayList<EvaluationResult>();
		
		List llist=session.createQuery("select o.id.orderId from OrderResult o where o.id.status=3 and o.id.userId=:u").setParameter("u", userId).list();
		List llist1=session.createQuery("select o.id.skuId from OrderResult o where o.id.status=3 and o.id.userId=:u").setParameter("u", userId).list();
		if(llist.size()!=0&&llist1.size()!=0){
		Query q=session.createQuery("from EvaluationResult e where e.id.orderId in (:or) and e.id.skuId in (:sk) order by e.type ").setParameterList("or", llist).setParameterList("sk", llist1);
		q.setFirstResult(offSet);
		q.setMaxResults(pageSize);
		list=q.list();
		}
		session.close();
		return list;
	}
	/**
	 * @author 杨冰冰
	 * @Description:评价页面查看所有已经完成订单
	 * @return QiuluResult
	 * @date 2017年4月11日 上午11:00:58
	 * @param session
	 * @return
	 */
	@Override
	public Integer findOrderResultByStatus3Num(Integer userId) {
		Session session=sessionFactory.openSession();
		Long l=0l;
		List llist=session.createQuery("select o.id.orderId from OrderResult o where o.id.status=3 and o.id.userId=:u").setParameter("u", userId).list();
		List llist1=session.createQuery("select o.id.skuId from OrderResult o where o.id.status=3 and o.id.userId=:u").setParameter("u", userId).list();
		
		if(llist.size()!=0&&llist1.size()!=0){
		l=(Long) session.createQuery("select count(*) from EvaluationResult e where e.id.orderId in (:or) and e.id.skuId in (:sk)").setParameterList("or", llist).setParameterList("sk", llist1).uniqueResult();
		}
		session.close();
		return l.intValue();
	}
	/**
	 * @author 杨冰冰
	 * @Description: 未评价的订单
	 * @return QiuluResult
	 * @date 2017年4月11日 下午2:31:54
	 * @param session
	 * @return
	 */
	@Override
	public List findEvaNoFinish(Integer userId,Integer pageSize,Integer offSet) {
		Session session=sessionFactory.openSession();
		List<EvaluationResult> li=new ArrayList<EvaluationResult>();
		Map<Object, Object> map=new HashMap<Object, Object>();
		List<Object> list1=new ArrayList<Object>();
		List list=session.createQuery("select o.id.skuId from OrderResult o where o.id.status=3 and o.id.userId=:o ").setParameter("o", userId).list();
		List list2=session.createQuery("select o.id.orderId from OrderResult o where o.id.status=3 and o.id.userId=:o ").setParameter("o", userId).list();
		if(list.size()!=0&&list2.size()!=0){
		Query q= (Query) session.createQuery("from EvaluationResult e where  e.id.skuId in (:in) and e.id.orderId in (:i) and (e.type is null or e.type=0)").setParameterList("i", list2).setParameterList("in", list);
		q.setFirstResult(offSet);
		q.setMaxResults(pageSize);
		li=q.list();
		}
		session.close();
		return li;
	}
	/**
	 * @author 杨冰冰
	 * @Description: 已经评价的订单
	 * @return QiuluResult
	 * @date 2017年4月11日 下午2:32:07
	 * @param userId
	 * @return
	 */
	@Override
	public List findEvaFinish(Integer userId,Integer pageSize,Integer offSet) {
		Session session=sessionFactory.openSession();
		List<EvaluationResult> li=new ArrayList<EvaluationResult>();
		Map<Object, Object> map=new HashMap<Object, Object>();
		List list1=new ArrayList<>();
		List list=session.createQuery("select o.id.skuId from OrderResult o where o.id.status=3 and o.id.userId=:o ").setParameter("o", userId).list();
		List list2=session.createQuery("select o.id.orderId from OrderResult o where o.id.status=3 and o.id.userId=:o ").setParameter("o", userId).list();
		if(list.size()!=0&&list2.size()!=0){
		Query q= (Query) session.createQuery("from EvaluationResult e where  e.id.skuId in (:in) and e.id.orderId in (:i) and e.type =1").setParameterList("i", list2).setParameterList("in", list);
		q.setFirstResult(offSet);
		q.setMaxResults(pageSize);
		li=q.list();
		}
		session.close();
		return li;
	}
	/**
	 * @author 杨冰冰
	 * @Description: 未评价的订单
	 * @return QiuluResult
	 * @date 2017年4月11日 下午2:31:54
	 * @param session
	 * @return
	 */
	@Override
	public Integer findEvaNoFinishNum(Integer userId) {
		Session session=sessionFactory.openSession();
		List list=session.createQuery("select o.id.skuId from OrderResult o where o.id.status=3 and o.id.userId=:o ").setParameter("o", userId).list();
		List list2=session.createQuery("select o.id.orderId from OrderResult o where o.id.status=3 and o.id.userId=:o ").setParameter("o", userId).list();
		Long l=0l;
		if(list.size()!=0&&list2.size()!=0){
			l=(Long) session.createQuery("select count(*) from EvaluationResult e where  e.id.skuId in (:in) and e.id.orderId in (:i) and (e.type is null or e.type=0)").setParameterList("i", list2).setParameterList("in", list).uniqueResult();
		}
		session.close();
		return l.intValue();
	}
	/**
	 * @author 杨冰冰
	 * @Description: 已经评价的订单
	 * @return QiuluResult
	 * @date 2017年4月11日 下午2:32:07
	 * @param userId
	 * @return
	 */
	@Override
	public Integer findEvaFinishNum(Integer userId) {
		Session session=sessionFactory.openSession();
		List list=session.createQuery("select o.id.skuId from OrderResult o where o.id.status=3 and o.id.userId=:o ").setParameter("o", userId).list();
		List list2=session.createQuery("select o.id.orderId from OrderResult o where o.id.status=3 and o.id.userId=:o ").setParameter("o", userId).list();
		Long l=0l;
		if(list.size()!=0&&list2.size()!=0){                 
			l=(Long) session.createQuery("select count(*) from EvaluationResult e where  e.id.skuId in (:in) and e.id.orderId in (:i) and e.type=1").setParameterList("i", list2).setParameterList("in", list).uniqueResult();
		}
		session.close();
		return l.intValue();
	}
	/**
	 * @author 杨冰冰
	 * @Description: 查看所有评价
	 * @return QiuluResult
	 * @date 2017年4月12日 上午11:46:58
	 * @param session
	 * @param pageNo
	 * @return
	 */
	@Override
	public List<EvaluationResult> findEvaResultByUserId(Integer userId, Integer pageSize, Integer offSet) {
		Session session=sessionFactory.openSession();
		List<EvaluationResult> li=new ArrayList<EvaluationResult>();
		Map<Object,Object> map=new HashMap<>();
		List list=session.createQuery("select o.id.skuId from OrderResult o where o.id.status=3 and o.id.userId=:o ").setParameter("o", userId).list();
		List list2=session.createQuery("select o.id.orderId from OrderResult o where o.id.status=3 and o.id.userId=:o ").setParameter("o", userId).list();
		if(list.size()!=0&&list2.size()!=0){
		Query q=session.createQuery("select e.time from EvaluationResult e where  e.id.skuId in (:in) and e.id.orderId in (:i) ").setParameterList("i", list2).setParameterList("in", list);
		q.setFirstResult(offSet);
		q.setMaxResults(pageSize);
		li=q.list();
		}
		session.close();
		return li;
	}
	/**
	 * @author 杨冰冰
	 * @Description: 查看所有评价
	 * @return QiuluResult
	 * @date 2017年4月12日 上午11:46:58
	 * @param session
	 * @param pageNo
	 * @return
	 */
	@Override
	public Integer findEvaResultByUserIdNum(Integer userId) {
		Long l=0l;
		Session session=sessionFactory.openSession();
		List list=session.createQuery("select o.id.orderId from OrderResult o where o.id.status=3 and o.id.userId=:u").setParameter("u", userId).list();
		if(list.size()!=0){
	    l=(Long)session.createQuery("select count(*) from EvaluationResult e where e.id.orderId in (:o)").setParameterList("o", list).uniqueResult();
		}
		return l.intValue();
	}
	/**
	 * @author 杨冰冰
	 * @Description: 查看某一件商品所有评价
	 * @return QiuluResult
	 * @date 2017年4月12日 下午2:01:13
	 * @param orderId
	 * @param skuId
	 * @return
	 */
	@Override
	public List<EvaluationResult>  findEvaResultBySkuId(Integer skuId,Integer pageSize,Integer offSet){
		Session session=sessionFactory.openSession();
		List<EvaluationResult> list=new ArrayList<EvaluationResult>();
		Query q=session.createQuery("from EvaluationResult e where e.id.skuId=:s and e.type=1").setParameter("s", skuId);
		q.setFirstResult(offSet);
		q.setMaxResults(pageSize);
		list=q.list();
		session.close();
		return list;
	}
	/**
	 * @author 杨冰冰
	 * @Description: 查看某一件商品的所有评价
	 * @return QiuluResult
	 * @date 2017年4月12日 下午2:01:13
	 * @param orderId
	 * @param skuId
	 * @return
	 */
	@Override
	public Integer findEvaResultBySkuNum(Integer skuId) {
		Session session=sessionFactory.openSession();
		Long l=(Long) session.createQuery("select count(*) from EvaluationResult e where e.id.skuId=:s and e.type=1").setParameter("s", skuId).uniqueResult();
		session.close();
		return l.intValue();
	}
	@Override
	public List<EvaluationResult> findEvaByImage(Integer id, Integer pageSize, Integer offSet) {
		Session session=sessionFactory.openSession();
		List<EvaluationResult> list=new ArrayList<EvaluationResult>();
		Query q=session.createQuery("from EvaluationResult e where e.id.skuId=:i and e.image!=null and e.type=1").setParameter("i", id);
		q.setFirstResult(offSet);
		q.setMaxResults(pageSize);
		list=q.list();
		return list;
	}
	@Override
	public Integer findEvaByImages(Integer id) {
		Session session=sessionFactory.openSession();
		Long l=(Long) session.createQuery("select count(*) from EvaluationResult e where e.id.skuId=:e and e.image!=null and e.type=1").setParameter("e", id).uniqueResult();
		session.close();
		return l.intValue();
	}
}
