package com.howin.qiulu.service;

import java.util.List;

import com.howin.qiulu.pojo.Evaluation;
import com.howin.qiulu.pojo.Order;
import com.howin.qiulu.result.OrderdeResult;
import com.howin.qiulu.result.QiuluResult;
import com.howin.qiulu.pojo.ReceivingAddress;
import com.howin.qiulu.pojo.ShoppingCart;
import com.howin.qiulu.pojo.ShoppingCartResult;
import com.howin.qiulu.pojo.ShoppingCartResultId;


public interface OrderService {
	//取消订单
	public QiuluResult cancelOrder(Long id);
	//查看订单
	public QiuluResult findOrderByUserId(Integer userId);
	//确认收货，订单完成
	public QiuluResult finishOrder(Long id);
	//订单评价
	public QiuluResult evaluationByOrderId(Evaluation ev);
	//删除订单
	public QiuluResult deleteOrder(Long id);
	//生成订单（订单表）
	public Long saveOrder(ReceivingAddress receivingAddress, float total,String express,Integer postage);
	//生成订单（订单详情表）
	public boolean saveOrderDetail(List<ShoppingCart> list, Long x);
	//通过订单编号查看订单表
	public Order findOrderByOrderId(Long orderId);
	//通过订单编号查看订单详情表
	public List<Integer> findOrderDetailByOrderId(Long orderId);
	//通过SKU_ID找到item_id
	public List<Integer> findItemId(List list);
	//通过item_id查找商品的名称
	public String findItemName(List list);
	//修改订单状态为已支付（1）	
	public void updateOrderByOrderId(Long orderId);
	public boolean changeRepertory(List<ShoppingCart> list);
	//退货
	public QiuluResult rejectedByOrderId(Long id); 
	public QiuluResult findOrderByStatus(Integer userId,Integer status,Integer pageSize,Integer pageNo);
	//根据状态查找总共有多少条订单
	public QiuluResult findOrderByStatusTotal(Integer userId);
	//查找总共有多少订单
	public QiuluResult findOrderNumByUserId(Integer userId);
	//根据id查看订单详情
	public QiuluResult findOrderResultbyId(Long orderId);
	
	//显示退货的订单（售后）
	public QiuluResult queryAbandonOrder(Integer userId);
	
	public QiuluResult saveOrderAgain(Integer id, Long orderId, Float total, Integer aid, String express, Integer postage);
	
}
