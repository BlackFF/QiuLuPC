package com.howin.qiulu.controller;


import java.io.IOException;
import java.util.Date;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.howin.qiulu.result.QiuluResult;
import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.AlibabaAliqinFcSmsNumSendRequest;
import com.taobao.api.response.AlibabaAliqinFcSmsNumSendResponse;

@Controller
@RequestMapping("/message")
public class MessageController {
    private final Integer NUM=6; 
    //private Logger log = Logger.getLogger(MessageController.class);

    @RequestMapping("/send")
    @ResponseBody
    public QiuluResult sendMessage2(String mobile,HttpServletRequest request) {
    	QiuluResult result=new QiuluResult();
    	System.out.println(mobile);
        //官网的URL---必须是这个
        String url="http://gw.api.taobao.com/router/rest"; 

        //成为开发者，创建应用后系统会自动生成--前面图片中有说明
        String appkey="23643069"; 

        //创建应用后系统会自动生成--前面图片中有说明
        String secret="cf4cfcfc0d541de13270a7a584eae25a";

        //随机生成 num 位验证码
        String code="";
        Random r = new Random(new Date().getTime());
        for(int i=0;i<NUM;i++){
            code = code+r.nextInt(10);
        }
        //需要log4j的Jar包--日志-可删
        //log.info("手机号为:"+mobile+",验证码为:"+code);

        //将验证码加入容器中---用户输入验证码之后验证
        request.getSession().setAttribute("messageCode", code);
        System.out.println(code);
        //短信模板的内容
        String json="{\"code\":\""+code+"\"}";

        TaobaoClient client = new DefaultTaobaoClient(url, appkey, secret);

        AlibabaAliqinFcSmsNumSendRequest req = new AlibabaAliqinFcSmsNumSendRequest();
        //公共回传参数，在“消息返回”中会透传回该参数；
        //举例：用户可以传入自己下级的会员ID，在消息返回时，该会员ID会包含在内，用户可以根据该会员ID识别是哪位会员使用了你的应用

        req.setExtend("");  
        //短信类型，传入值请填写normal
        req.setSmsType("normal");

        //签名名称
        req.setSmsFreeSignName("河南秋露");

        //短信模板变量，传参规则{"key":"value"}，key的名字须和申请模板中的变量名一致，多个变量之间以逗号隔开。
        req.setSmsParamString(json);

        //短信接收号码
        req.setRecNum(mobile);

        //短信模板ID
        req.setSmsTemplateCode("SMS_48010052");
        AlibabaAliqinFcSmsNumSendResponse rsp = null;
        try {
            rsp = client.execute(req);
        } catch (ApiException e) {
            e.printStackTrace();
        }
        result.setStatus(true);
        result.setObject(rsp.getBody());
        return result;  
    }
    
    
	@RequestMapping("/verifyMessageCode")
	@ResponseBody
    public QiuluResult verifyCode(HttpServletRequest req, HttpServletResponse res,String messageCode) throws ServletException, IOException {
		QiuluResult result=new QiuluResult();
		System.out.println(result);
		res.setContentType("text/html;charset=utf-8");
		String mesCode=(String)req.getSession().getAttribute("messageCode");
		//code=req.getParameter("veryCode");
		System.out.println(mesCode);
		System.out.println(messageCode);
		//PrintWriter pw=res.getWriter();
		if(mesCode==null){
			result.setStatus(false);
			result.setMessage("请先获取短信校验码");
		}
		else{
		if(messageCode==null||"".equals(messageCode)){
			//pw.println("验证码为空");
			result.setStatus(false);
			result.setMessage("短信校验码为空");
		}else{
			if(mesCode.equals(messageCode)){
				result.setStatus(true);
				result.setMessage("短信校验码正确");
				//pw.println("验证码正确");
			}else{
				result.setStatus(false);
				result.setMessage("短信校验码错误");
				//pw.println("验证码错误");
			}
		}
		}
		return result;
	}
}
