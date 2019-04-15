package com.howin.qiulu.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.howin.qiulu.Util.DOM4JReader;
import com.howin.qiulu.dao.OrderDao;
import com.howin.qiulu.pojo.Evaluation;
import com.howin.qiulu.pojo.ReceivingAddress;
import com.howin.qiulu.pojo.ShoppingCart;
import com.howin.qiulu.pojo.User;
import com.howin.qiulu.result.QiuluResult;
import com.howin.qiulu.service.OrderService;
import com.howin.qiulu.service.ReceivingAddressService;
import com.howin.qiulu.service.ShoppingCartService;

@Controller
@RequestMapping("/order")
public class OrderController {
	@Autowired
	private OrderService orderService;
	@Resource
	private OrderDao orderDao;
	@Resource
	private ReceivingAddressService receivingAddressService;
	@Resource
	private ShoppingCartService shoppingCartService;
	/***
	 * Title: 取消订单 Description:
	 * 
	 * @author 杨冰冰
	 * @date 2017年2月22日 上午11:28:25
	 * 
	 */
	@RequestMapping("/cancelOrder")
	@ResponseBody

	public QiuluResult cancelOrder(Long id) {

		QiuluResult result = orderService.cancelOrder(id);

		return result;

	}

	/***
	 * Title: 查看订单 Description:
	 * 
	 * @author 杨冰冰
	 * @date 2017年2月22日 下午1:37:22
	 */
	@RequestMapping("/lookOrder")
	@ResponseBody


	public QiuluResult findOrderByUserId(Integer userId,HttpSession session){
		User user=(User) session.getAttribute("user");
		QiuluResult result=orderService.findOrderByUserId(user.getId());
		return result;

	}

	/***
	 * Title: 确认收货，订单完成 Description:
	 * 
	 * @author 杨冰冰
	 * @date 2017年2月22日 下午2:45:53
	 * 
	 */
	@RequestMapping("/finishOrder")
	@ResponseBody
	public QiuluResult finishOrder(Long id) {
		QiuluResult result = orderService.finishOrder(id);
		return result;

	}

	/***
	 * Title: 订单评价 Description:
	 * 
	 * @author 杨冰冰
	 * @date 2017年2月22日 下午3:01:00
	 * 
	 */
	@RequestMapping("/evaluation")
	@ResponseBody
	public QiuluResult evaluationByOrderId(Evaluation ev) {
		QiuluResult result = orderService.evaluationByOrderId(ev);
		return result;
	}

	/***
	 * Title: 删除订单
	 * Description:
	 * 
	 * @author 杨冰冰
	 * @date 2017年2月23日 上午11:21:01
	 * 
	 */
	@RequestMapping("/deleteOrder")
	@ResponseBody
	public QiuluResult deleteOrder(Long id) {

		QiuluResult result = orderService.deleteOrder(id);

		return result;
	}

	/**
	 * @author 张磊
	 * @Description: 保存订单
	 * @return QiuluResult
	 * @date 2017年2月23日 下午4:18:30
	 * @param receivingAddress
	 * @return
	 */
	@RequestMapping("/saveOrder")
	@ResponseBody
	public QiuluResult saveOrder(HttpSession session,Float total,Integer aid,Integer[] id,String express,Integer postage) {
		QiuluResult result = new QiuluResult();
		User user=(User)session.getAttribute("user");
		
		if(user==null){
			result.setStatus(false);
			result.setMessage("用户未登录，请先登录");
		}
		else{
		//Float total = (float) 0.01;
		//获取收货地址
		QiuluResult result1=receivingAddressService.LookAddressById(aid);
		ReceivingAddress receivingAddress=(ReceivingAddress)result1.getObject();
		//加入订单表中
		Long x = orderService.saveOrder(receivingAddress, total,express,postage);
		if (x == null) {
			result.setStatus(false);
			result.setMessage("保存订单失败");
		} else {
			List<Integer> list = new ArrayList<Integer>();
			for(int i=0;i<id.length;i++){
				list.add(id[i]);
			}
			//购物车商品
			List<ShoppingCart> shoppingCart=shoppingCartService.queryShoppingCartByIds(list);
			boolean b = orderService.saveOrderDetail(shoppingCart, x);
			if (b) {
				boolean flg=orderService.changeRepertory(shoppingCart);
				if(flg){
				result.setStatus(true);
				result.setMessage("保存订单成功");
				result.setObject(x);
				shoppingCartService.deleteShoppingCartById(list);
				}
				else{
					result.setStatus(false);
					result.setMessage("减少库存出错");
					orderDao.deleteOrder(x);
					orderDao.deleteOrderDetail(x);
				}
			} else {
				orderDao.deleteOrder(x);
				result.setStatus(false);
				result.setMessage("保存订单失败");
			}
		}
		}
		return result;
	}
	
	
	
	//再次购买商品
	@RequestMapping("/saveOrderAgain")
	@ResponseBody
	public QiuluResult saveOrderAgain(HttpSession session,Long orderId,Float total,Integer aid,String express,Integer postage){
		QiuluResult result = new QiuluResult();
		User user=(User)session.getAttribute("user");
		
		if(user==null){
			result.setStatus(false);
			result.setMessage("用户未登录，请先登录");
		}
		else{
			result=orderService.saveOrderAgain(user.getId(),orderId,total,aid,express,postage);
		}
		return result;
	}
	
	
	/**
	 * 退货
	 * @param id
	 * @return
	 */
	@RequestMapping("/rejectedOrder")
	@ResponseBody
	public QiuluResult rejectedByOrderId(Long id){
		QiuluResult result=new QiuluResult();
		result=orderService.rejectedByOrderId(id);
		return result;
	}
	
	/**
	 * @author 杨冰冰
	 * @Description: 订单详情，根据status查找订单
	 * @return QiuluResult
	 * @date 2017年4月7日 上午10:23:53
	 * @param status
	 * @param session
	 * @param pageNo
	 * @return
	 */
	@RequestMapping("/findOrderByStatus")
	@ResponseBody
	public QiuluResult findOrderByStatus(Integer status,HttpSession session,Integer pageNo){
		User user=(User) session.getAttribute("user");
		QiuluResult result=new QiuluResult();
		if(pageNo==null){
			pageNo=1;                                                                                     
		}
		result=orderService.findOrderByStatus(user.getId(), status, 5, pageNo);
		return result;
	}
	/**
	 * @author 杨冰冰
	 * @Description:根据状态查找总共有多少条订单
	 * @return QiuluResult
	 * @date 2017年4月6日 下午3:05:49
	 * @param session
	 * @return
	 */
	@RequestMapping("/findOrderByStatusTotal")
	@ResponseBody
	public QiuluResult findOrderByStatusTotal(HttpSession session){
		QiuluResult result=new QiuluResult();
		User user=(User) session.getAttribute("user");
		result=orderService.findOrderByStatusTotal(user.getId());
		return result;
	}
	
	/**
	 * @author 杨冰冰
	 * @Description: 查找总共有多少订单
	 * @return QiuluResult
	 * @date 2017年4月6日 下午2:38:11
	 * @param session
	 * @return
	 */
	@RequestMapping("/findOrderNumByUserId")
	@ResponseBody
	public QiuluResult findOrderNumByUserId(HttpSession session){
		QiuluResult result=new QiuluResult();
		User user=(User) session.getAttribute("user");
		result=orderService.findOrderNumByUserId(user.getId());
		return result;
		
	}
	
	
	/**
	 * @author 杨冰冰
	 * @Description: 根据id查看订单详情
	 * @return QiuluResult
	 * @date 2017年4月11日 上午10:44:12
	 * @param id
	 * @return
	 */
	@RequestMapping("/findOrderResultById")
	@ResponseBody
	public QiuluResult findOrderResultById(Long orderId){
		QiuluResult result=new QiuluResult();
		result=orderService.findOrderResultbyId(orderId);
		return result;
	}
	
	
/**
 * 
 * @author 张磊
 * @Description: 显示退货的订单（售后）
 * @return QiuluResult
 * @date 2017年4月12日 下午2:04:12
 * @param session
 * @return
 */
	@RequestMapping("/queryAbandonOrder")
	@ResponseBody
	public QiuluResult queryAbandonOrder(HttpSession session){
		User user=(User) session.getAttribute("user");
		Integer userId=user.getId();
		QiuluResult result=orderService.queryAbandonOrder(userId);
		return result;
	}
	
	
	
	
	@RequestMapping("/getPostMessage")
	@ResponseBody
	public QiuluResult getPostMessage(HttpSession session){
		QiuluResult result=new QiuluResult();
		DOM4JReader dom = new DOM4JReader();
		 String demo[] = new String[2];
			try {
				demo= dom.xmlRead();
			    } catch (Exception e) { 
				e.printStackTrace();
			    }
			Integer postLine=Integer.parseInt(demo[0]);
			Integer postage=Integer.parseInt(demo[1]);
		   Integer[] x={postLine,postage};
		   result.setObject(x);
		   result.setStatus(true);
		return result;
	}
}
