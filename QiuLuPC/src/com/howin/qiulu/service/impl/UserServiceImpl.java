package com.howin.qiulu.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.howin.qiulu.dao.FavoritesDao;
import com.howin.qiulu.dao.OrderDao;
import com.howin.qiulu.dao.UserDao;
import com.howin.qiulu.pojo.User;
import com.howin.qiulu.pojo.UserDetail;
import com.howin.qiulu.result.QiuluResult;
import com.howin.qiulu.service.UserService;




@Service("UserService")
public class UserServiceImpl implements UserService{
	@Autowired
	private UserDao userDao;
	@Autowired
	private OrderDao orderDao;
	@Autowired
	private FavoritesDao favoritesDao;
	/*** 
	* Title:用户注册
	* Description: 
	* @author 杨冰冰
	* @date 2017年2月20日 上午11:04:04
	* 
	*/
	@SuppressWarnings("null")
	@Override
	public QiuluResult register(String password, String telephone) {
		QiuluResult result=new QiuluResult();
		User u=userDao.findTelephone(telephone);
		User user =new User();
		UserDetail ud=new UserDetail();
			System.out.println(user);
			user.setPassword(password);
			user.setTelephone(telephone);
			user.setUsername(telephone);
			User us=userDao.register(user);
			if(user!=null){
			ud.setUserId(us.getId());
			ud.setVip((short)0);
			userDao.addUserDetail(ud);
			}
			result.setStatus(true);
			result.setObject(us);
		return result;
	}
	/*** 
	* Title:去数据库查找这个手机号用户是否注册
	* Description: 
	* @author 杨冰冰
	* @date 2017年2月20日 上午11:04:04
	* 
	*/
	@Override
	public QiuluResult findTelephone(String telephone) {
		User user=userDao.findTelephone(telephone);
		QiuluResult result =new QiuluResult();
		if(user!=null){
			result.setMessage("已经注册");
			result.setStatus(false);
		}else{
			result.setMessage("未注册");
			result.setStatus(true);
		}
		
		return result;
	}
	@Override
	public QiuluResult findTele(String telephone) {
		User user=userDao.findTelephone(telephone);
		QiuluResult result =new QiuluResult();
		if(user!=null){
			result.setMessage("账号正确");
			result.setObject(user.getId());
			result.setStatus(true);
		}else{
			result.setMessage("输入账号有误");
			result.setStatus(false);
		}
		
		return result;
	}
	/*** 
	* Title:用户登录
	* Description: 
	* @author 杨冰冰
	* @date 2017年2月20日 上午11:04:04
	* 
	*/
	@Override
	public QiuluResult Login(String password, String telephone) {
		System.out.println(password+telephone);
		User user=userDao.findTelephone(telephone);
		QiuluResult result=new QiuluResult();
		User u=	userDao.Login(telephone, password);
		System.out.println(u);
		if(user!=null  ){
			if(u!=null){
				result.setStatus(true);
				result.setObject(u);
			}else{
				result.setStatus(false);
				result.setMessage("密码错误");
			}
			
		}else{
			result.setStatus(false);
			result.setMessage("用户未注册");
		}
		return result;
	}
	/*** 
	* Title:查看个人信息
	* Description: 
	* @author 杨冰冰
	* @date 2017年2月20日 上午11:04:04
	* 
	*/
	@Override
	public QiuluResult LookUserDetail(Integer userId) {
		QiuluResult result =new QiuluResult();
		result.setStatus(true);
		User user=new User();
		User us=userDao.findTelephoneUserName(userId);
		user.setId(user.getId());
		user.setTelephone(us.getTelephone());
		user.setUsername(us.getUsername());
		Map<Object, Object> map=new HashMap<Object,Object>();
		map.put("user", user);
		map.put("userDetatil", userDao.LookUserDetail(userId));
		result.setObject(map);
		return result;
	}
	/*** 
	* Title:完善个人信息
	* Description: 
	* @author 杨冰冰
	* @date 2017年2月20日 上午11:04:04
	* 
	*/
	@Override
	public QiuluResult saveUserDetail(UserDetail ud) {
		
		Integer x=userDao.saveUserDetail(ud);
		QiuluResult result=new QiuluResult();
		if(x==null){
			result.setStatus(false);
			result.setMessage("失败");
		}else{
			result.setStatus(true);
			result.setMessage("完善信息成功");
		}
		
		return result;
	}
	/*** 
	* Title: 修改密码
	* Description: id 用户id，Oldpassword 老密码，password，新密码
	* @author 杨冰冰
	* @date 2017年2月21日 上午9:56:10
	* 
	*/
	@Override
	public QiuluResult updatePassword(Integer id, String password) {
		QiuluResult result=new QiuluResult();
		Integer in=userDao.updatePassword(id,password);
		if(in!=null){
			result.setStatus(true);
		}else{
			result.setStatus(false);
		}
		return result;
	}
	@Override
	public boolean uploadHeadPort(String path,Integer userId) {
		
		return userDao.uploadHeadPort(path,userId);
	}
	@Override
	public QiuluResult findTelephoneByUserId(Integer id) {
		QiuluResult result=new QiuluResult();
		result.setStatus(true);
		result.setObject(userDao.findTelephoneByUserId(id));
		return result;
	}
	/**
	 * @author 杨冰冰
	 * @Description: 个人信息页面展示头像，手机号，订单总数，收藏夹总数
	 * @return QiuluResult
	 * @date 2017年4月7日 下午2:10:01
	 * @param session
	 * @return
	 */
	@Override
	public QiuluResult findHeadPortTelOrderFavByUserId(Integer userId) {
		QiuluResult result=new QiuluResult();
		Map<Object, Object> map=new HashMap<Object, Object>();
		map.put("telephone", userDao.findTelephoneByUserId(userId));
		map.put("headPort", userDao.findHeadPortByUserId(userId));
		map.put("orderNum", orderDao.findOrderNumByUserId(userId));
		map.put("favoritesNum", favoritesDao.queryFavoritesNumber(userId));
		result.setStatus(true);
		result.setObject(map);
		return result;
	}
	
}
