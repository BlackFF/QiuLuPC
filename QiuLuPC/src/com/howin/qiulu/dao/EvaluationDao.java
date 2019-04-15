package com.howin.qiulu.dao;

import java.util.List;

import com.howin.qiulu.pojo.EvaluationResult;

public interface EvaluationDao {
	public List<EvaluationResult> findEvaBySkuId(Integer id,Integer pageSize,Integer offSet);
	public Integer findEvaBySkuIdNum(Integer id);
	//评价页面查看所有已经完成订单
	public List<EvaluationResult> findOrderByStatus3(Integer userId,Integer pageSize,Integer offSet);
	//评价页面查看所有已经完成订单
	public Integer findOrderResultByStatus3Num(Integer userId);
	//未评价的订单
	public List findEvaNoFinish(Integer userId,Integer pageSize,Integer offSet);
	//未评价的订单
	public Integer findEvaNoFinishNum(Integer userId);
	// 已经评价的订单
	public List findEvaFinish(Integer userId,Integer pageSize,Integer offSet);
	// 已经评价的订单
	public Integer findEvaFinishNum(Integer userId);
	//查看所有评价
	public List<EvaluationResult> findEvaResultByUserId(Integer userId,Integer pageSize,Integer offSet);
	//查看所有评价
	public Integer findEvaResultByUserIdNum(Integer userId);
	//查看某一个商品所有的评价
	public List<EvaluationResult> findEvaResultBySkuId(Integer skuId,Integer pageSize,Integer offSet);
	public Integer findEvaResultBySkuNum(Integer skuId);
	public List<EvaluationResult> findEvaByImage(Integer id,Integer pageSize,Integer offSet);
	public Integer findEvaByImages(Integer id);
}
