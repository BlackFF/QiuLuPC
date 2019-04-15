package com.howin.qiulu.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.howin.qiulu.Util.HttpUtil;
import com.howin.qiulu.result.QiuluResult;

@Controller
@RequestMapping("/express") 
public class ExpressController {

	/** 
	* @Title: queryExpress 
	* @date 2017年4月25日 下午7:16:08 
	* @author 魏吉鹏
	* @Description: 查询快递
	* @param express	快递公司代码
	* @param courierNumber	快递单号
	* @return
	* @return QiuluResult
	* @throws 
	*/ 
	@RequestMapping("/queryExpress")
	@ResponseBody
	public QiuluResult queryExpress(String express, String courierNumber){ 
//		String s = HttpUtil.postData("https://m.kuaidi100.com/index_all.html?type="+express+"&postid="+courierNumber,"","GET");
		String s = HttpUtil.sendGet("https://m.kuaidi100.com/query","type="+express+"&postid="+courierNumber);
		QiuluResult result = new QiuluResult();
		result.setObject(s);
		return result;
	}

}
