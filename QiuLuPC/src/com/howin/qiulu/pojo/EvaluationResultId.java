package com.howin.qiulu.pojo;
// default package
// Generated 2017-4-11 16:17:51 by Hibernate Tools 3.2.1.GA

import java.sql.Timestamp;
import java.util.Date;

/**
 * EvaluationResultId generated by hbm2java
 */
public class EvaluationResultId implements java.io.Serializable {

	private String sellPoint;
	private String simage;
	private long orderId;
	private int skuId;
	private int status;
	public EvaluationResultId() {
	}

	public EvaluationResultId(long orderId, int skuId, int status) {
		this.orderId = orderId;
		this.skuId = skuId;
		this.status = status;
	}

	
	public EvaluationResultId(String sellPoint, String simage, long orderId, int skuId, int status
			) {
		super();
		this.sellPoint = sellPoint;
		this.simage = simage;
		this.orderId = orderId;
		this.skuId = skuId;
		this.status = status;
		
	}

	public String getSellPoint() {
		return sellPoint;
	}

	public void setSellPoint(String sellPoint) {
		this.sellPoint = sellPoint;
	}

	public String getSimage() {
		return simage;
	}

	public void setSimage(String simage) {
		this.simage = simage;
	}

	public long getOrderId() {
		return orderId;
	}

	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}

	public int getSkuId() {
		return skuId;
	}

	public void setSkuId(int skuId) {
		this.skuId = skuId;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (orderId ^ (orderId >>> 32));
		result = prime * result + ((sellPoint == null) ? 0 : sellPoint.hashCode());
		result = prime * result + ((simage == null) ? 0 : simage.hashCode());
		result = prime * result + skuId;
		result = prime * result + status;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EvaluationResultId other = (EvaluationResultId) obj;
		if (orderId != other.orderId)
			return false;
		if (sellPoint == null) {
			if (other.sellPoint != null)
				return false;
		} else if (!sellPoint.equals(other.sellPoint))
			return false;
		if (simage == null) {
			if (other.simage != null)
				return false;
		} else if (!simage.equals(other.simage))
			return false;
		if (skuId != other.skuId)
			return false;
		if (status != other.status)
			return false;
		return true;
	}

	
	
}