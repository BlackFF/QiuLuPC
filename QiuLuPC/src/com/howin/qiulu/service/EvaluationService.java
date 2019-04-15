package com.howin.qiulu.service;

import com.howin.qiulu.result.QiuluResult;

public interface EvaluationService {
	public QiuluResult findEvaBySkuId(Integer id,Integer pageSize,Integer pageNo);
	//评价页面查看所有已经完成订单
	public QiuluResult findOrderResultByStatus3(Integer userId,Integer pageSize,Integer pageNo);
	//未评价的订单
	public QiuluResult findEvaNoFinish(Integer userId,Integer pageSize,Integer pageNo);
	//已经评价的订单
	public QiuluResult findEvaFinish(Integer userId,Integer pageSize,Integer pageNo);
	//查看所有评价
	public QiuluResult findEvaResultByUserId(Integer userId,Integer pageSize,Integer pageNo);
	//查看某一条订单是否已经评价
	public QiuluResult findEvaResultBySkuId(Integer pageSize,Integer pageNo,Integer skuId);
	public QiuluResult findEvaByImage(Integer id, Integer pageSize, Integer pageNo);
	public QiuluResult findEvaNum(Integer id);
}
