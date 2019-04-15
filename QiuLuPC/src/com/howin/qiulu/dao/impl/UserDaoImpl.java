package com.howin.qiulu.dao.impl;

import javax.annotation.Resource;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import com.howin.qiulu.dao.UserDao;
import com.howin.qiulu.pojo.User;
import com.howin.qiulu.pojo.UserDetail;
@Repository("UserDao")
public class UserDaoImpl extends HibernateDaoSupport implements UserDao{
	
	@Resource
	private SessionFactory sessionFactory;
	@Autowired
	public void setMySessionFactory(SessionFactory sessionFactory){  
		super.setSessionFactory(sessionFactory);
	}
	/*** 
	* Title:用户注册
	* Description: 
	* @author 杨冰冰
	* @date 2017年2月20日 上午11:04:04
	* 
	*/
	@Override
	public User register(User user) {
		Session session = null;
		try{
			session=sessionFactory.openSession();
			session.save(user);
			return user;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}finally{
			session.close();
		}
	}
	
	/*** 
	* Title:去数据库查找用这个手机号注册的用户是否存在
	* Description: 
	* @author 杨冰冰
	* @date 2017年2月20日 上午11:04:04
	* 
	*/
	@Override
	public User findTelephone(String telephone) {
		Session session=sessionFactory.openSession();
		Transaction tr=session.beginTransaction();
		User user=(User) session.createQuery("from User where telephone=:i").setParameter("i", telephone).uniqueResult();
		tr.commit();
		session.close();
		return user;
		
	}
	/*** 
	* Title:用户登录
	* Description: 
	* @author 杨冰冰
	* @date 2017年2月20日 上午11:04:04
	* 
	*/
	@Override
	public User Login(String telephone, String password) {
		Session session=sessionFactory.openSession();
		Transaction tr=session.beginTransaction();
		User user=(User) session.createQuery("from User where password=:p and telephone=:t").setParameter("p", password).setParameter("t", telephone).uniqueResult();
		System.out.println(user);
		tr.commit();
		session.close();
		return user;
	}
	@Override
	public User loginByTel(String telephone, String password) {
		Session session=sessionFactory.openSession();
		Transaction tr=session.beginTransaction();
		User i=(User) session.createQuery("from User where password=:p and telephone=:t").setParameter("p", password).setParameter("t", telephone).uniqueResult();
		tr.commit();
		session.close();
		return i;
	}
	/*** 
	* Title:查看个人信息
	* Description: 
	* @author 杨冰冰
	* @date 2017年2月20日 上午11:04:04
	* 
	*/
	@Override
	public UserDetail LookUserDetail(Integer userId) {
		Session session=sessionFactory.openSession();
		Transaction tr=session.beginTransaction();
		UserDetail ud=(UserDetail) session.createQuery("from UserDetail where userId=:u").setInteger("u", userId).uniqueResult();
		tr.commit();
		session.close();
		return ud;
	}
	/*** 
	* Title:完善个人信息
	* Description: 
	* @author 杨冰冰
	* @date 2017年2月20日 上午11:04:04
	* 
	*/
	@Override
	public Integer saveUserDetail(UserDetail ud) {
		UserDetail uds=LookUserDetail(ud.getUserId());
		Integer x=null;
		Session session=sessionFactory.openSession();
		Transaction tr=session.beginTransaction();
		ud.setHeadPort(uds.getHeadPort());
		ud.setVip(uds.getVip());
			session.saveOrUpdate(ud);
			x=1;
		tr.commit();
		session.close();
		return x;
	}
	/*** 
	* Title:修改密码
	* Description: 
	* @author 杨冰冰
	* @date 2017年2月20日 上午11:04:04
	* 
	*/
	@Override
	public Integer updatePassword(Integer id,String password) {
		Session session = sessionFactory.openSession();
		Integer in=session.createQuery("update User set password=:p where id=:i").setParameter("p", password).setParameter("i", id).executeUpdate();
		session.close();
		return in;
	}
	@Override
	public User findPasswordByUserId(Integer id,String Oldpassword) {
		Session session=sessionFactory.openSession();
		Transaction tr=session.beginTransaction();
		User user=(User) session.createQuery("from User where id=:i and password=:p").setInteger("i", id).setParameter("p", Oldpassword).uniqueResult();
		tr.commit();
		session.close();
		return user;
	}
	@SuppressWarnings("null")
	@Override
	public boolean uploadHeadPort(String path,Integer userId) {
		boolean flg=true;
		UserDetail detail=LookUserDetail(userId);
		if(detail==null){
			detail.setUserId(userId);
			detail.setVip((short)0);
			detail.setHeadPort(path);
		}
		else{
			detail.setHeadPort(path);
		}
	 

		Session session=sessionFactory.openSession();
		Transaction tr= session.beginTransaction();
		try {
			session.saveOrUpdate(detail);
		} catch (Exception e) {
			flg=false;
		}
		tr.commit();
		session.close();
		
		return flg;
	}
	@Override
	public String findTelephoneByUserId(Integer id) {
		Session session=sessionFactory.openSession();
		String s=(String) session.createQuery("select telephone from User where id=:i").setParameter("i", id).uniqueResult();
		session.close();
		return s;
	}
	@Override
	public String findHeadPortByUserId(Integer userId) {
		Session session=sessionFactory.openSession();
		String s=(String) session.createQuery("select headPort from UserDetail where userId=:u").setParameter("u", userId).uniqueResult();
		session.close();
		return s;
	}
	@Override
	public User findTelephoneUserName(Integer id) {
		Session session=sessionFactory.openSession();
		User user=(User) session.createQuery(" from User where id=:i").setParameter("i", id).uniqueResult();
		session.close();
		return user;
	}
	@Override
	public UserDetail saveUserId(UserDetail ud) {
		Session session=sessionFactory.openSession();
		try {
			session.save(ud);
		} catch (Exception e) {
			e.printStackTrace();
		}
		session.close();
		return ud;
	}
	@Override
	public Integer addUserDetail(UserDetail ud) {
		Integer x=null;
		Session session=sessionFactory.openSession();
		Transaction tr=session.beginTransaction();
			session.saveOrUpdate(ud);
			x=1;
		tr.commit();
		session.close();
		return x;
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
