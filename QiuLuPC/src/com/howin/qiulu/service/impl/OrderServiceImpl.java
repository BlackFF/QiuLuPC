package com.howin.qiulu.service.impl;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.howin.qiulu.Util.Page;
import com.howin.qiulu.Util.T;
import com.howin.qiulu.dao.ItemDao;
import com.howin.qiulu.dao.OrderDao;
import com.howin.qiulu.dao.ReceivingAddressDao;
import com.howin.qiulu.pojo.Evaluation;
import com.howin.qiulu.pojo.Order;
import com.howin.qiulu.pojo.OrderDetail;
import com.howin.qiulu.pojo.ReceivingAddress;
import com.howin.qiulu.pojo.ShoppingCart;
import com.howin.qiulu.pojo.Sku;
import com.howin.qiulu.result.AbandonOrder;
import com.howin.qiulu.result.OrderdeResult;
import com.howin.qiulu.result.QiuluResult;
import com.howin.qiulu.service.OrderService;
@Service("OrderService")
public class OrderServiceImpl implements OrderService{
	@Autowired
	private OrderDao orderDao;
	@Autowired
	private ItemDao itemDao;
	@Autowired
	private ReceivingAddressDao receivingAddressDao;
	
	/*** 
	* Title: 取消订单
	* Description: 
	* @author 杨冰冰
	* @date 2017年2月22日 上午11:28:25
	* 
	*/
	@Override
	public QiuluResult cancelOrder(Long id) {
		QiuluResult result=new QiuluResult();
		if((orderDao.cancelOrder(id))!=0){
			result.setStatus(true);
			result.setMessage("取消订单成功");
		}else{
			result.setStatus(false);
			result.setMessage("取消订单失败");
		}
		
		return result;
	}
	/*** 
	* Title: 查看订单
	* Description: 
	* @author 杨冰冰
	* @date 2017年2月22日 下午1:37:22
	* 
	*/
	@Override
	public QiuluResult findOrderByUserId(Integer userId) {
		QiuluResult result=new QiuluResult();
		result.setStatus(true);
		result.setObject(orderDao.findOrderByUserId(userId));
		return result;
	}
	/*** 
	* Title: 确认收货，订单完成
	* Description: 
	* @author 杨冰冰
	* @date 2017年2月22日 下午2:45:53
	* 
	*/
	@Override
	public QiuluResult finishOrder(Long id) {
		QiuluResult result=new QiuluResult();
		Order order=orderDao.findOrderById(id);
		order.setStatus(3);
		order.setEndTime(new Timestamp(System.currentTimeMillis()));
		result.setStatus(true);
		result.setObject(orderDao.finishOrder(order));
		
		return result;
	}
	/*** 
	* Title: 订单评价
	* Description: 
	* @author 杨冰冰
	* @date 2017年2月22日 下午3:01:00
	* 
	*/
	@Override
	public QiuluResult evaluationByOrderId(Evaluation ev) {
		QiuluResult result=new QiuluResult();
		if(ev.getExpressScore()==null){
			ev.setExpressScore(0);
		}
		
		ev.setTime(new Timestamp(System.currentTimeMillis()));
		Evaluation evs=orderDao.evaluationByOrderId(ev);
		
		//修改订单为已评价状态
		boolean b=orderDao.changeOrderType(evs.getOrderId(),evs.getSkuId());
		if(b){
			result.setStatus(true);
			evs.setType(1);
			result.setObject(evs);
			return result;
			}
		else{
			result.setStatus(false);
			result.setObject(null);
			return result;
		}
	}
	/*** 																														
	* Title: 删除订单
	* Description: 
	* @author 杨冰冰
	* @date 2017年2月23日 上午11:21:01
	* 
	*/
	@Override
	public QiuluResult deleteOrder(Long id) {
		QiuluResult result=new QiuluResult();
		if((orderDao.deleteOrder(id))!=null){
			result.setStatus(true);
			result.setMessage("删除订单成功");
		}else{
			result.setStatus(false);
			result.setMessage("删除订单失败");
		}
		
		return result;
	}

	
	/**
	 * 张磊
	 * 保存订单
	 */
	@Override
	public Long saveOrder(ReceivingAddress re,float total,String express,Integer postage) {
		Long id=Long.parseLong(T.getOrderIdByUUId());
		StringBuffer sb=new StringBuffer();
		sb.append(re.getProvince());
		sb.append(re.getCity());
		sb.append(re.getArea());
		sb.append(re.getAddress());
		String s=sb.toString();
		Order order=new Order();
		order.setId(id);
		order.setReceiver(re.getReceiver());
		order.setUserId(re.getUserId());
		order.setReceiverAddr(s);
	    order.setPhone(re.getTelephone());
	    order.setTotal(total);
	    order.setExpress(express);
	    if(postage==null){
	    order.setPostage(0);
	    }
	    else{
	    	order.setPostage(postage);
	    }
	    Long x=orderDao.saveOrder(order);
	    System.out.println(x);
		return x;
	
	}
	/**
	 * 张磊
	 * 保存订单详情
	 */
	@Override
	public boolean saveOrderDetail(List<ShoppingCart> list,Long x) {
		List<OrderDetail> oederList=orderDao.queryOrderDetailList(list,x);
		boolean m=orderDao.saveOrderDetail(oederList);
		return m;
	}
	//通过订单编号查看订单表
	@Override
	public Order findOrderByOrderId(Long orderId) {
		Order order=orderDao.findOrderByOrderId(orderId);
		return order;
	}
	//通过订单编号查看订单详情表
	@Override
	public List<Integer> findOrderDetailByOrderId(Long orderId) {
		List<Integer> list=orderDao.findOrderDetailByOrderId(orderId);
		return list;
	}
	
	//通过SKU_ID找到item_id
	@Override
	public List<Integer> findItemId(List list) {
		return orderDao.findItemId(list);
	}
   //通过item_id查找商品的名称
	@Override
	public String findItemName(List list) {
		String s="";
		List<String> nameList=orderDao.findItemName(list);
		for (int i = 0; i < nameList.size(); i++) {
			System.out.println(nameList.get(i));
			s=s.concat(nameList.get(i)).concat(" ");
			
		}
		System.out.println(s);
		return s;
	}
	//修改订单状态为已支付（1）
	@Override
	public void updateOrderByOrderId(Long orderId) {
		orderDao.updateOrderByOrderId(orderId);
		
	}
	
	
	@Override
	public boolean changeRepertory(List<ShoppingCart> list) {
		boolean b=orderDao.changeRepertory(list);
		return b;
	}
	/**
	 * 退货
	 * @param id
	 * @return
	 */
	@Override
	public QiuluResult rejectedByOrderId(Long id) {
		QiuluResult result=new QiuluResult();
		Integer i=orderDao.rejectedByOrderId(id);
		if(i!=null){
			result.setStatus(true);
		}else{
			result.setStatus(false);
		}
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
	@Override
	public QiuluResult findOrderByStatus(Integer userId, Integer status, Integer pageSize, Integer pageNo) {
		QiuluResult result=new QiuluResult();
		Page page=new Page();
		//查询商品的总条数
		Integer allRow=orderDao.findOrderByStatusNum(userId, status);
		//当前内容
		Integer offSet=page.countOffset(pageNo, pageSize);
		//获取当前页内容
		List<OrderdeResult> list=orderDao.findOrderByStatus(userId, status, pageSize, offSet);
		
		page.setList(list);
		page.setPageNo(pageNo);
		page.setPageSize(pageSize);
		page.setTotalRecords(allRow);
		result.setStatus(true);
		result.setObject(page);
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
	@Override
	public QiuluResult findOrderByStatusTotal(Integer userId) {
		QiuluResult result=new QiuluResult();
		result.setStatus(true);
		Map<Object, Object> map=new HashMap<Object, Object>();
		map.put("status0", orderDao.findOrderByStatus0Total(userId));
		map.put("status1", orderDao.findOrderByStatus1Total(userId));
		map.put("status2", orderDao.findOrderByStatus2Total(userId));
		map.put("status3", orderDao.findOrderByStatus3Total(userId));
		map.put("status4", orderDao.findOrderByStatus4Total(userId));
		map.put("statusAll", orderDao.findOrderByStatusAll(userId));
		result.setObject(map);
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
	@Override
	public QiuluResult findOrderNumByUserId(Integer userId) {
		QiuluResult result=new QiuluResult();
		result.setStatus(true);
		result.setObject(orderDao.findOrderNumByUserId(userId));
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
	@Override
	public QiuluResult findOrderResultbyId(Long orderId) {
		QiuluResult result=new QiuluResult();
		Order order=orderDao.findOrderById(orderId);
		Map<Object, Object> map=new HashMap<Object,Object>();
		map.put("status", order.getStatus());
		map.put("id", order.getId());
		map.put("CreateTime",order.getCreateTime());
		map.put("order", orderDao.findOrderById(orderId));
		map.put("orderResult", orderDao.findOrderResult(orderId));
		result.setStatus(true);
		result.setObject(map);
		return result;
	}
	@Override
	public QiuluResult queryAbandonOrder(Integer userId) {
		QiuluResult result=new QiuluResult();
		List<AbandonOrder> list=orderDao.queryAbandonOrder(userId);
		
		result.setStatus(true);
		result.setObject(list);
		return result;
	}
	@Override
	public QiuluResult saveOrderAgain(Integer id, Long orderId, Float total, Integer aid, String express, Integer postage) {
		QiuluResult result=new QiuluResult();
		boolean flag=true;
		List<OrderDetail> list=orderDao.queryOrderDetailByOrderId(orderId);
		for (int i = 0; i < list.size(); i++) {
			Sku sku=itemDao.querySkuById(list.get(i).getSkuId());
			if(sku.getStatus()==1){
				result.setStatus(false);
				result.setMessage("部分商品已下架无法再次购买");
				flag=false;
				break;
			}
			else if(list.get(i).getNumber()>sku.getRepertory()){
				result.setStatus(false);
				result.setMessage("部分商品库存不足无法再次购买");
				flag=false;
				break;
			}
		}
		
		if(flag){
			//获取收货地址
			ReceivingAddress receivingAddress=receivingAddressDao.LookAddressById(aid);
			Long newOrderId=saveOrder(receivingAddress,total,express,postage);
			if(newOrderId==0){
				result.setStatus(false);
				result.setMessage("订单错误");
			}
			else{
				for (int i = 0; i < list.size(); i++) {
					OrderDetail od=new OrderDetail();
				    od=list.get(i);
					od.setOrderId(newOrderId);
					od.setId(null);
					orderDao.saveOrderDetailAgain(od);
				}
				result.setStatus(true);
				result.setMessage("再次购买成功");
				result.setObject(newOrderId);
			}
			
		}
		
		return result;
	}
}
