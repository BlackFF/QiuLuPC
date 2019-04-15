package com.howin.qiulu.result;

import java.sql.Timestamp;
import java.util.List;

import com.howin.qiulu.pojo.OrderResult;

public class OrderdeResult {
	private Timestamp crTime;
	private Long orderId;
	private List<OrderResult> listOr;
	private String receive;
	
	public Timestamp getCrTime() {
		return crTime;
	}
	public void setCrTime(Timestamp crTime) {
		this.crTime = crTime;
	}
	public Long getOrderId() {
		return orderId;
	}
	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}
	public List<OrderResult> getListOr() {
		return listOr;
	}
	public void setListOr(List<OrderResult> listOr) {
		this.listOr = listOr;
	}
	public String getReceive() {
		return receive;
	}
	public void setReceive(String receive) {
		this.receive = receive;
	}
	@Override
	public String toString() {
		return "OrderdeResult [crTime=" + crTime + ", orderId=" + orderId + ", listOr=" + listOr + ", receive="
				+ receive + "]";
	}
	
	
	
}
