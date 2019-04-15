package com.howin.qiulu.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import com.howin.qiulu.dao.ReceivingAddressDao;
import com.howin.qiulu.pojo.ReceivingAddress;
@Repository("ReceivingAddressDao")
public class ReceivingAddressDaoImpl extends HibernateDaoSupport implements ReceivingAddressDao{
	@Resource
	private SessionFactory sessionFactory;
	
	@Autowired
	public void setMySessionFactory(SessionFactory sessionFactory){  
		super.setSessionFactory(sessionFactory);
	}
	/*** 
	* Title: 查找收货地址
	* Description: 
	* @author 杨冰冰
	* @date 2017年2月21日 上午11:31:00
	* 
	*/
	@Override
	public List<ReceivingAddress> findAddress(Integer userId) {
		Session session =sessionFactory.openSession();
		Transaction tr=session.beginTransaction();
		List<ReceivingAddress> list=session.createQuery("from ReceivingAddress where userId=:i").setInteger("i", userId).list();
		tr.commit();
		session.close();
		return list;
	}
	/*** 
	* Title: 增加收货地址
	* Description: 
	* @author 杨冰冰
	* @date 2017年2月21日 下午1:45:47
	* 
	*/
	@Override
	public ReceivingAddress addAddress(ReceivingAddress ra) {
		Session session=sessionFactory.openSession();
		Transaction tr=session.beginTransaction();
		session.saveOrUpdate(ra);
		tr.commit();
		session.close();
		return ra;
	}
	/*** 
	* Title: 点击编辑按钮回显收货地址
	* Description: 
	* @author 杨冰冰
	* @date 2017年2月21日 下午2:35:41
	* 
	*/
	@Override
	public ReceivingAddress LookAddressById(Integer id) {
		Session session=sessionFactory.openSession();
		Transaction tr=session.beginTransaction();
		ReceivingAddress ra=(ReceivingAddress) session.createQuery("from ReceivingAddress where id=:i").setInteger("i", id).uniqueResult();
		tr.commit();
		session.close();
		return ra;
	}
	/*** 
	* Title: 修改收货地址
	* Description: 
	* @author 杨冰冰
	* @date 2017年2月21日 下午1:46:01
	* 
	*/
	@Override
	public ReceivingAddress updateAddress(ReceivingAddress ra) {
		Session session=sessionFactory.openSession();
		Transaction tr=session.beginTransaction();
		session.update(ra);
		tr.commit();
		session.close();
		return ra;
	}
	/*** 
	* Title: 删除收货地址
	* Description: 
	* @author 杨冰冰
	* @date 2017年2月21日 下午2:26:01
	* 
	*/
	@Override
	public int deleteAddress(Integer id) {
	
		Session session=sessionFactory.openSession();
		Transaction tr=session.beginTransaction();
		int i=session.createQuery("delete from ReceivingAddress r where r.id=:i").setParameter("i", id).executeUpdate();
		tr.commit();
		session.close();
		return i;
	}
	/*** 
	* Title: 设置为默认地址
	* Description: 
	* @author 杨冰冰
	* @date 2017年2月21日 下午3:21:06
	* 
	*/
	@Override
	public int defaultAddress(Integer id,Integer userId) {
		Session session=sessionFactory.openSession();
		Transaction tr=session.beginTransaction();
		session.createQuery("update ReceivingAddress set status=0 where userId=:u").setParameter("u", userId).executeUpdate();
		int i= session.createQuery("update ReceivingAddress r set r.status=1 where r.id=:i").setInteger("i", id).executeUpdate();
		tr.commit();
		session.close();
		return i;                                                                                     
	}
	/*** 
	* Title: 显示默认地址
	* Description: 
	* @author 杨冰冰
	* @date 2017年2月22日 上午11:02:43
	* 
	*/
	@Override
	public ReceivingAddress lookDefaultAddressByStatus(Integer userId) {
		Session session=sessionFactory.openSession();
		Transaction tr=session.beginTransaction();
		ReceivingAddress ra=(ReceivingAddress) session.createQuery("from ReceivingAddress where status=1 and userId=:u")
				.setParameter("u", userId).uniqueResult();
		tr.commit();
		session.close();
		return ra;
	}
}
