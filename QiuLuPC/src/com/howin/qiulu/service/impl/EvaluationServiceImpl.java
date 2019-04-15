package com.howin.qiulu.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.howin.qiulu.Util.Page;
import com.howin.qiulu.dao.EvaluationDao;
import com.howin.qiulu.pojo.EvaluationResult;
import com.howin.qiulu.result.QiuluResult;
import com.howin.qiulu.service.EvaluationService;
@Service("EvaluationService")
public class EvaluationServiceImpl implements EvaluationService{
	@Autowired
	private EvaluationDao evalutionDao; 
	/**
	 * 查找评价
	 * @param id
	 * @return
	 */
	@Override
	public QiuluResult findEvaBySkuId(Integer id,Integer pageSize,Integer pageNo) {
		QiuluResult result=new QiuluResult();
		Page page=new Page();
		//查询商品的总条数
		Integer allRow=evalutionDao.findEvaBySkuIdNum(id);
		//当前内容
		Integer offSet=page.countOffset(pageNo, pageSize);
		//获取当前页内容
		List<EvaluationResult> list=evalutionDao.findEvaBySkuId(id, pageSize, offSet);
		
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
	 * @Description:评价页面查看所有已经完成订单
	 * @return QiuluResult
	 * @date 2017年4月11日 上午11:00:58
	 * @param session
	 * @return
	 */
	@Override
	public QiuluResult findOrderResultByStatus3(Integer userId,Integer pageSize,Integer pageNo) {
		QiuluResult result=new QiuluResult();
		Page page=new Page();
		//查询商品的总条数
		Integer allRow=evalutionDao.findOrderResultByStatus3Num(userId);
		//当前内容
		Integer offSet=page.countOffset(pageNo, pageSize);
		//获取当前页内容
		List<EvaluationResult> list=evalutionDao.findOrderByStatus3(userId,  pageSize,offSet);
		
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
	 * @Description: 未评价的订单
	 * @return QiuluResult
	 * @date 2017年4月11日 下午2:31:54
	 * @param session
	 * @return
	 */
	@Override
	public QiuluResult findEvaNoFinish(Integer userId,Integer pageSize,Integer pageNo) {
		QiuluResult result=new QiuluResult();
		Page page=new Page();
		//查询商品的总条数
		Integer allRow=evalutionDao.findEvaNoFinishNum(userId);
		//当前内容
		Integer offSet=page.countOffset(pageNo, pageSize);
		//获取当前页内容
		List<Object> list=evalutionDao.findEvaNoFinish(userId, pageSize, offSet);
		
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
	 * @Description: 已经评价的订单
	 * @return QiuluResult
	 * @date 2017年4月11日 下午2:32:07
	 * @param userId
	 * @return
	 */
	@Override
	public QiuluResult findEvaFinish(Integer userId,Integer pageSize,Integer pageNo) {
		QiuluResult result=new QiuluResult();
		Page page=new Page();
		//查询商品的总条数
		Integer allRow=evalutionDao.findEvaFinishNum(userId);
		//当前内容
		Integer offSet=page.countOffset(pageNo, pageSize);
		//获取当前页内容
		List<Object> list=evalutionDao.findEvaFinish(userId, pageSize, offSet);
		
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
	 * @Description: 查看所有评价
	 * @return QiuluResult
	 * @date 2017年4月12日 上午11:46:58
	 * @param session
	 * @param pageNo
	 * @return
	 */
	@Override
	public QiuluResult findEvaResultByUserId(Integer userId, Integer pageSize, Integer pageNo) {
		QiuluResult result=new QiuluResult();
		Page page=new Page();
		//查询商品的总条数
		Integer allRow=evalutionDao.findEvaResultByUserIdNum(userId);
		//当前内容
		Integer offSet=page.countOffset(pageNo, pageSize);
		//获取当前页内容
		List<EvaluationResult> list=evalutionDao.findEvaResultByUserId(userId, pageSize, offSet);
		
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
	 * @Description: 查看某一条订单是否已经评价
	 * @return QiuluResult
	 * @date 2017年4月12日 下午2:01:13
	 * @param orderId
	 * @param skuId
	 * @return
	 */
	@Override
	public QiuluResult findEvaResultBySkuId(Integer skuId,Integer pageSize,Integer pageNo) {
		QiuluResult result=new QiuluResult();
		Page page=new Page();
		//查询商品的总条数
		Integer allRow=evalutionDao.findEvaResultBySkuNum(skuId);
		//当前内容
		Integer offSet=page.countOffset(pageNo, pageSize);
		//获取当前页内容
		List<EvaluationResult> list=evalutionDao.findEvaResultBySkuId(skuId, pageSize, offSet);
		
		page.setList(list);
		page.setPageNo(pageNo);
		page.setPageSize(pageSize);
		page.setTotalRecords(allRow);
		result.setStatus(true);
		result.setObject(page);
		return result;
	}
	@Override
	public QiuluResult findEvaByImage(Integer id, Integer pageSize, Integer pageNo) {
		QiuluResult result=new QiuluResult();
		Page page=new Page();
		//查询商品的总条数
		Integer allRow=evalutionDao.findEvaBySkuIdNum(id);
		//当前内容
		Integer offSet=page.countOffset(pageNo, pageSize);
		//获取当前页内容
		List<EvaluationResult> list=evalutionDao.findEvaByImage(id, pageSize, offSet);
		
		page.setList(list);
		page.setPageNo(pageNo);
		page.setPageSize(pageSize);
		page.setTotalRecords(allRow);
		result.setStatus(true);
		result.setObject(page);
		return result;
	}
	@Override
	public QiuluResult findEvaNum(Integer id) {
		QiuluResult result=new QiuluResult();
		Map<Object,Object> map=new HashMap<Object,Object>();
		map.put("EvaNum", evalutionDao.findEvaBySkuIdNum(id));
		map.put("imgNum", evalutionDao.findEvaByImages(id));
		result.setStatus(true);
		result.setObject(map);
		return result;
	}

}
