package com.howin.qiulu.result;

import java.sql.Timestamp;
import java.util.List;

import com.howin.qiulu.pojo.OrderResult;

public class AbandonOrder {

	private Long orderId;
	private List<OrderResult> list;
	private Timestamp createTime;
	
	
	public AbandonOrder(){
		
	}
	
	public AbandonOrder(Long orderId, List<OrderResult> list, Timestamp createTime) {
		super();
		this.orderId = orderId;
		this.list = list;
		this.createTime = createTime;
	}
	public Long getOrderId() {
		return orderId;
	}
	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}
	public List<OrderResult> getList() {
		return list;
	}
	public void setList(List<OrderResult> list) {
		this.list = list;
	}
	public Timestamp getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	
	

	
}
