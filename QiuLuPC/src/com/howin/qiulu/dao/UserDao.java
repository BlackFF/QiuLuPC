package com.howin.qiulu.dao;

import com.howin.qiulu.pojo.User;
import com.howin.qiulu.pojo.UserDetail;

public interface UserDao {
	//用户注册
	public User register(User user);
	public UserDetail saveUserId(UserDetail ud);
	//去数据库查找
	public User findTelephone(String telephone);
	//用户登录
	public User Login(String telephone,String password);
	public User loginByTel(String telephone,String password);
	//查看个人信息
	public UserDetail LookUserDetail(Integer userId);
	//完善个人信息
	public Integer saveUserDetail(UserDetail ud);
	//修改个人密码
	public Integer updatePassword(Integer id,String password);
	//查找老密码是否存在
	public User findPasswordByUserId(Integer id,String Oldpassword);
	public String findTelephoneByUserId(Integer id);
	public String findHeadPortByUserId(Integer userId); 
	//上传个人头像
	public boolean uploadHeadPort(String path,Integer userId);
	public User findTelephoneUserName(Integer id);
	public Integer addUserDetail(UserDetail ud);
	
}
