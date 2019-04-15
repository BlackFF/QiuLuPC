package com.howin.qiulu.dao;

import java.util.List;

import com.howin.qiulu.pojo.ReceivingAddress;

public interface ReceivingAddressDao {
	//查找收货地址
	public List<ReceivingAddress> findAddress(Integer userId);
	// 增加收货地址
	public ReceivingAddress addAddress(ReceivingAddress ra);
	//点击编辑按钮回显收货地址
	public ReceivingAddress LookAddressById(Integer id);
	// 修改收货地址
	public ReceivingAddress updateAddress(ReceivingAddress ra);
	// 删除收货地址
	public int deleteAddress(Integer id);
	//设置为默认地址
	public int defaultAddress(Integer id,Integer userId);
	//显示默认地址
	public ReceivingAddress lookDefaultAddressByStatus(Integer userId);
}
