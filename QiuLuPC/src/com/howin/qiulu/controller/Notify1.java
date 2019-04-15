package com.howin.qiulu.controller;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.howin.qiulu.Util.HttpUtil;
import com.howin.qiulu.Util.PayCommonUtil;
import com.howin.qiulu.Util.PayConfigUtil;
import com.howin.qiulu.pojo.Order;
import com.howin.qiulu.service.OrderService;

/**
 * Servlet implementation class Notify1
 */
@WebServlet("/Notify1")
public class Notify1 extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(Notify1.class);
	@Autowired
	private OrderService orderService;
	

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Notify1() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public void init(ServletConfig servletConfig) throws ServletException {  
	    ServletContext servletContext = servletConfig.getServletContext();  
	    WebApplicationContext webApplicationContext = WebApplicationContextUtils  
	            .getWebApplicationContext(servletContext);  
	    AutowireCapableBeanFactory autowireCapableBeanFactory = webApplicationContext  
	            .getAutowireCapableBeanFactory();  
	    autowireCapableBeanFactory.configureBean(this, "OrderService");  
	}  
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub

		// 读取xml
		InputStream inputStream;
		StringBuffer sb = new StringBuffer();
		inputStream = request.getInputStream();
		String s;
		BufferedReader in = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
		while ((s = in.readLine()) != null) {
			sb.append(s);
		}
		in.close();
		inputStream.close();
		SortedMap<Object, Object> packageParams = PayCommonUtil.xmlConvertToMap(sb.toString());
		logger.info(packageParams); 
		
		// 账号信息
		String key = PayConfigUtil.API_KEY; // key
		
		String resXml="";//反馈给微信服务器
		// 验签
		if (PayCommonUtil.isTenpaySign("UTF-8", packageParams, key)) {
			//appid openid mch_id is_subscribe nonce_str product_id sign
			
			//统一下单
			String openid = (String)packageParams.get("openid");
            String product_id = (String)packageParams.get("product_id");
            Long product_id1=Long.parseLong(product_id);
            //解析product_id，计算价格等
            
    		String out_trade_no =product_id; // 订单号  
    		//String out_trade_no =String.valueOf(System.currentTimeMillis());
    	    System.out.println(product_id);
    		//获取订单的价格
    	    Order order=orderService.findOrderByOrderId(product_id1);
    	    System.out.println(order.getTotal());
    	    float price1=order.getTotal()*100;
    	    int i=(int)price1;
    	    System.out.println(i);
    	    String price=String.valueOf(i);
    		String order_price =price; // 价格   注意：价格的单位是分  
            //获取订单中的商品名称
    	     List<Integer> list=orderService.findOrderDetailByOrderId(product_id1);
    	     List<Integer> list1=orderService.findItemId(list);
    	     String name=orderService.findItemName(list1);
    		String body = name;   // 商品名称  这里设置为product_id
            String attach = "秋露商城"; //附加数据
            
    		String nonce_str0 = PayCommonUtil.getNonce_str();
    		
            // 获取发起电脑 ip  
            String spbill_create_ip = PayConfigUtil.CREATE_IP;    
            String trade_type = "NATIVE"; 
    		
            
            SortedMap<Object,Object> unifiedParams = new TreeMap<Object,Object>();  
            unifiedParams.put("appid", PayConfigUtil.APP_ID); // 必须
            unifiedParams.put("mch_id", PayConfigUtil.MCH_ID); // 必须
            unifiedParams.put("out_trade_no", out_trade_no); // 必须
            unifiedParams.put("product_id", product_id);
            unifiedParams.put("body", body); // 必须
            unifiedParams.put("attach", attach);
            unifiedParams.put("total_fee", order_price);  // 必须 
            unifiedParams.put("nonce_str", nonce_str0);  // 必须
            unifiedParams.put("spbill_create_ip", spbill_create_ip); // 必须 
            unifiedParams.put("trade_type", trade_type); // 必须  
            unifiedParams.put("openid", openid);  
            unifiedParams.put("notify_url", PayConfigUtil.NOTIFY_URL);//异步通知url
            
            String sign0 = PayCommonUtil.createSign("UTF-8", unifiedParams,key);  
            unifiedParams.put("sign", sign0); //签名
            String requestXML = PayCommonUtil.getRequestXml(unifiedParams);  
            logger.info(requestXML);
            //统一下单接口
            String rXml = HttpUtil.postData(PayConfigUtil.UFDODER_URL, requestXML);  
            
            //统一下单响应
            SortedMap<Object, Object> reParams = PayCommonUtil.xmlConvertToMap(rXml);
    		logger.info(reParams); 
    		
    		//验签
    		if (PayCommonUtil.isTenpaySign("UTF-8", reParams, key)) {
    			// 统一下单返回的参数
    			String prepay_id = (String)reParams.get("prepay_id");//交易会话标识  2小时内有效
    			
        		String nonce_str1 = PayCommonUtil.getNonce_str();
    			
    			SortedMap<Object,Object> resParams = new TreeMap<Object,Object>();  
    			resParams.put("return_code", "SUCCESS"); // 必须
    			resParams.put("return_msg", "OK");
    			resParams.put("appid", PayConfigUtil.APP_ID); // 必须
    			resParams.put("mch_id", PayConfigUtil.MCH_ID);
    			resParams.put("nonce_str", nonce_str1); // 必须
    			resParams.put("prepay_id", prepay_id); // 必须
    			resParams.put("result_code", "SUCCESS"); // 必须
    			resParams.put("err_code_des", "OK");
    			String sign1 = PayCommonUtil.createSign("UTF-8", resParams,key);  
    			resParams.put("sign", sign1); //签名
    			
    			resXml = PayCommonUtil.getRequestXml(resParams);
    			logger.info(resXml);
                
    		}else{
    			logger.info("签名验证错误");
    			resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"  
                        + "<return_msg><![CDATA[签名验证错误]]></return_msg>" + "</xml> "; 
    		}
            
		}else{
			logger.info("签名验证错误");
			resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"  
                    + "<return_msg><![CDATA[签名验证错误]]></return_msg>" + "</xml> "; 
		}

		//------------------------------  
        //处理业务完毕  
        //------------------------------  
        BufferedOutputStream out = new BufferedOutputStream(  
                response.getOutputStream());  
        out.write(resXml.getBytes());  
        out.flush();  
        out.close();  
        
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
