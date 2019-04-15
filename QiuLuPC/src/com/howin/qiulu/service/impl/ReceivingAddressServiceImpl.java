package com.howin.qiulu.service.impl;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.howin.qiulu.dao.ReceivingAddressDao;
import com.howin.qiulu.pojo.ReceivingAddress;
import com.howin.qiulu.result.QiuluResult;
import com.howin.qiulu.service.ReceivingAddressService;
@Service("ReceivingAddressService")
public class ReceivingAddressServiceImpl implements ReceivingAddressService{
	@Autowired
	private ReceivingAddressDao receivingAddressDao;
	/*** 
	* Title: 查找收货地址
	* Description: 
	* @author 杨冰冰
	* @date 2017年2月21日 上午11:31:00
	* 
	*/
	@Override
	public QiuluResult findAddress(Integer userId) {
		QiuluResult result=new QiuluResult();
		result.setStatus(true);
		result.setObject(receivingAddressDao.findAddress(userId));
		return result;
	}
	
	/*** 
	* Title: 增加收货地址
	* Description: 
	* @author 杨冰冰
	* @date 2017年2月21日 下午1:45:47
	* 
	*/
	@Override
	public QiuluResult addAddress(ReceivingAddress ra) {
		QiuluResult result=new QiuluResult();
		List<ReceivingAddress> list=receivingAddressDao.findAddress(ra.getUserId());
		if(list.size()==0){
			ra.setStatus((short) 1);
		}else{
			ra.setStatus((short) 0);
		}
		result.setStatus(true);
		result.setObject(receivingAddressDao.addAddress(ra));
		return result;
	}
	/*** 
	* Title: 点击编辑按钮回显收货地址
	* Description: 
	* @author 杨冰冰
	* @date 2017年2月21日 下午2:35:41
	* 
	*/
	@Override
	public QiuluResult LookAddressById(Integer id) {
		QiuluResult result=new QiuluResult();
		result.setStatus(true);
		result.setObject(receivingAddressDao.LookAddressById(id));
		return result;
		
	}
	/*** 
	* Title: 修改收货地址
	* Description: 
	* @author 杨冰冰
	* @date 2017年2月21日 下午1:46:01
	* 
	*/
	@Override
	public QiuluResult updateAddress(ReceivingAddress ra) {
		ra.setId(ra.getId());
		ra.setReceiver(ra.getReceiver());
		ra.setTelephone(ra.getTelephone());
		ra.setProvince(ra.getProvince());
		ra.setCity(ra.getCity());
		ra.setArea(ra.getArea());
		ra.setAddress(ra.getAddress());
		QiuluResult result=new QiuluResult();
		result.setStatus(true);
		result.setObject(receivingAddressDao.updateAddress(ra));
		return result;
		
	}
	/*** 
	* Title: 删除收货地址
	* Description: 
	* @author 杨冰冰
	* @date 2017年2月21日 下午2:26:01
	* 
	*/
	@Override
	public QiuluResult deleteAddress(Integer id) {
		QiuluResult result=new QiuluResult();
		if((receivingAddressDao.deleteAddress(id))!=0){
			result.setStatus(true);
			result.setMessage("删除成功");
		}else{
			result.setStatus(false);
			result.setMessage("删除失败");
			
		}
		
		return result;
	}
	/*** 
	* Title: 设置为默认地址
	* Description: 
	* @author 杨冰冰
	* @date 2017年2月21日 下午3:21:06
	* 
	*/
	@Override
	public QiuluResult defaultAddress(Integer id,Integer userId) {
		QiuluResult result=new QiuluResult();
		if((receivingAddressDao.defaultAddress(id,userId))!=0){
			result.setStatus(true);
			result.setMessage("设置默认地址成功");
		}else{
			result.setStatus(false);
			result.setMessage("设置默认地址失败");
		}
		
		return result;
	}
	/*** 
	* Title: 显示默认地址
	* Description: 
	* @author 杨冰冰
	* @date 2017年2月22日 上午11:02:43
	* 
	*/
	@Override
	public QiuluResult lookDefaultAddressByStatus(Integer userId) {
		QiuluResult result=new QiuluResult();
		result.setStatus(true);
		result.setObject(receivingAddressDao.lookDefaultAddressByStatus(userId));
		return result;
	}
}
