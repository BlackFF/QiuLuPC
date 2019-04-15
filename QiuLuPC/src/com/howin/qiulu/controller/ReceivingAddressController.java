package com.howin.qiulu.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.howin.qiulu.pojo.ReceivingAddress;
import com.howin.qiulu.pojo.User;
import com.howin.qiulu.result.QiuluResult;
import com.howin.qiulu.service.ReceivingAddressService;

@Controller
@RequestMapping("/Address")
public class ReceivingAddressController {
	@Autowired
	private ReceivingAddressService receivingAddressService;
	
	/*** 
	* Title: 查找收货地址
	* Description: 
	* @author 杨冰冰
	* @date 2017年2月21日 上午11:31:00
	* 
	*/
	@RequestMapping("/findAddress")
	@ResponseBody
	public QiuluResult  findAddress(HttpSession session){
		User user=(User) session.getAttribute("user");
		QiuluResult result=receivingAddressService.findAddress(user.getId());
		return result;
	}
	
	/*** 
	* Title: 增加收货地址
	* Description: 
	* @author 杨冰冰
	* @date 2017年2月21日 下午1:45:47
	* 
	*/
	@RequestMapping("/addAddress")
	@ResponseBody
	public QiuluResult  addAddress(ReceivingAddress ra,HttpSession session){
		User user=(User) session.getAttribute("user");
		ra.setUserId(user.getId());
		
		QiuluResult result=receivingAddressService.addAddress(ra);
		return result;
	}
	/*** 
	* Title: 点击编辑按钮回显收货地址
	* Description: 
	* @author 杨冰冰
	* @date 2017年2月21日 下午2:35:41
	* 
	*/
	@RequestMapping("/lookAddress")
	@ResponseBody
	public QiuluResult lookAddressById(Integer id){
		QiuluResult result=receivingAddressService.LookAddressById(id);
		return result;
	}
	/*** 
	* Title: 修改收货地址
	* Description: 
	* @author 杨冰冰
	* @date 2017年2月21日 下午1:46:01
	* 
	*/
	@RequestMapping("/updateAddress")
	@ResponseBody
	public QiuluResult updateAddress(ReceivingAddress ra){
		QiuluResult result=receivingAddressService.updateAddress(ra);
		return result;
	}
	
	/*** 
	* Title: 删除收货地址
	* Description: 
	* @author 杨冰冰
	* @date 2017年2月21日 下午2:26:01
	* 
	*/
	@RequestMapping("/deleteAddress")
	@ResponseBody
	public QiuluResult deleteAddress(Integer id){
		
		QiuluResult result=receivingAddressService.deleteAddress(id);
		return result;
		
	}
	
	
	/*** 
	* Title: 设置为默认地址
	* Description: 
	* @author 杨冰冰
	* @date 2017年2月21日 下午3:21:06
	* 
	*/
	@RequestMapping("/defaultAddress")
	@ResponseBody
	public QiuluResult defaultAddress(Integer id,HttpSession session){
		User user=(User) session.getAttribute("user");
		QiuluResult result=receivingAddressService.defaultAddress(id,user.getId());
		
		return result;
		
	}
	
	/*** 
	* Title: 显示默认地址
	* Description: 
	* @author 杨冰冰
	* @date 2017年2月22日 上午11:02:43
	* 
	*/
	@RequestMapping("/lookDefault")
	@ResponseBody
	public QiuluResult lookDefaultAddressByStatus(HttpSession session){
		User user=(User) session.getAttribute("user");
		QiuluResult result=receivingAddressService.lookDefaultAddressByStatus(user.getId());
		return result;
	}
}
