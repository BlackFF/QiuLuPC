package com.howin.qiulu.service;

import java.util.List;

import com.howin.qiulu.pojo.ReceivingAddress;
import com.howin.qiulu.result.QiuluResult;

public interface ReceivingAddressService {
	//查找收货地址
	public QiuluResult findAddress(Integer userId);
	// 增加收货地址
	public QiuluResult addAddress(ReceivingAddress ra);
	//点击编辑按钮回显收货地址
	public QiuluResult LookAddressById(Integer id);
	// 修改收货地址
	public QiuluResult updateAddress(ReceivingAddress ra);
	// 删除收货地址
	public QiuluResult deleteAddress(Integer id);
	//设置为默认地址
	public QiuluResult defaultAddress(Integer id,Integer userId);
	//显示默认地址
	public QiuluResult lookDefaultAddressByStatus(Integer userId);
}
