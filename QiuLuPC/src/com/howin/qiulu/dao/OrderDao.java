package com.howin.qiulu.dao;

import java.util.List;

import com.howin.qiulu.pojo.Evaluation;
import com.howin.qiulu.pojo.Order;
import com.howin.qiulu.pojo.OrderDetail;
import com.howin.qiulu.pojo.OrderResult;
import com.howin.qiulu.pojo.ShoppingCart;
import com.howin.qiulu.result.AbandonOrder;
import com.howin.qiulu.result.OrderdeResult;

public interface OrderDao {
	// 取消订单
	public int cancelOrder(Long id);
	//查看订单
	public List<Order> findOrderByUserId(Integer userId);

	//确认收货，订单完成
	public Order finishOrder(Order order);
	
	//订单评价
	public Evaluation evaluationByOrderId(Evaluation ev);
	//删除订单
	public Integer deleteOrder(Long id);
	//根据id找订单
	public Order findOrderById(Long id);

	//生成订单（订单表）
	public Long saveOrder(Order order);
	//生成订单（订单表详情表）
	public boolean saveOrderDetail(List<OrderDetail> oederList);
	//通过订单编号查看订单表
	public Order findOrderByOrderId(Long orderId);
	//通过订单编号查看订单详情表
	public List<Integer> findOrderDetailByOrderId(Long orderId);
	
	//通过订单编号查看订单详情内容
	public List<OrderDetail> queryOrderDetailByOrderId(Long orderId);
	
	//通过SKU_ID找到item_id
	public List<Integer> findItemId(List list);
	//通过item_id查找商品的名称
	public List<String> findItemName(List list);
	//修改订单状态为已支付（1）
	public void updateOrderByOrderId(Long orderId);
	
	//行成订单时，减少相应商品的库存
	public boolean changeRepertory(List<ShoppingCart> list);
	
	//删除订单详情
	public Integer deleteOrderDetail(Long x);
	
	//包装订单详细的列表
	public List<OrderDetail> queryOrderDetailList(List<ShoppingCart> list, Long x);
	//退货
	public Integer rejectedByOrderId(Long id);
	public List<OrderdeResult> findOrderByStatus(Integer userId,Integer status,Integer pageSize,Integer offSet);
	public Integer findOrderByStatusNum(Integer userId,Integer status);
	////根据状态为1查找总共有多少条订单，订单生成 未支付
	public Integer findOrderByStatus0Total(Integer userId);
	//根据状态为1查找总共有多少条订单，订单支付 未处理
	public Integer findOrderByStatus1Total(Integer userId);
	//根据状态为2查找总共有多少条订单，订单支付 送货中
	public Integer findOrderByStatus2Total(Integer userId);
	//根据状态为3查找总共有多少条订单，订单完成
	public Integer findOrderByStatus3Total(Integer userId);
	//根据状态为4查找总共有多少条订单，订单取消
	//计算全部订单除去退货5	
	public Integer findOrderByStatusAll(Integer userId);
	public Integer findOrderByStatus4Total(Integer userId);
	public List<OrderDetail> findOrderDetailByStatus(Integer userId,Integer status);
	//查找总共有多少订单
	public Integer findOrderNumByUserId(Integer userId);
	//根据id查看订单详情
	public List<OrderResult> findOrderResult(Long orderId);
	
	//
	public List<AbandonOrder> queryAbandonOrder(Integer userId);
	
	//修改订单为已评价状态
	public boolean changeOrderType(Long orderId,Integer skuId);
	
	public void saveOrderDetailAgain(OrderDetail od);
}
