package com.howin.qiulu.service;

import com.howin.qiulu.pojo.User;
import com.howin.qiulu.pojo.UserDetail;
import com.howin.qiulu.result.QiuluResult;

public interface UserService {
	//用户注册
	public QiuluResult register(String password,String telephone);
	//去数据库查找这个手机号用户是否注册
	public QiuluResult findTelephone(String telephone);
	public QiuluResult findTele(String telephone);
	//用户注册
	public QiuluResult Login(String password,String telephone);
	//查看个人信息
	public QiuluResult LookUserDetail(Integer userId);
	//完善个人信息
	public QiuluResult saveUserDetail(UserDetail ud);
	//修改个人密码
	public QiuluResult updatePassword(Integer id,String password);
	public QiuluResult findTelephoneByUserId(Integer id);
	//个人信息页面展示头像，手机号，订单总数，收藏夹总数
	public QiuluResult findHeadPortTelOrderFavByUserId(Integer userId);
	//上传头像
	public boolean uploadHeadPort(String path,Integer userId);
	
}
