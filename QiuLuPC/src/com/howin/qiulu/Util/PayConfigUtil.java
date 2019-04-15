package com.howin.qiulu.Util;

public class PayConfigUtil {

	//以下相关参数需要根据自己实际情况进行配置
	public static String APP_ID = "wx7504778ec4f99579";// appid

	public static String APP_SECRET = "b77fba754559304c833a258f4a6f1853";// appsecret 
	public static String MCH_ID = "1381362902";// 你的商业号
	public static String API_KEY = "F0ADD9B0A643CD55AEE6964A5870DB62";// API key  后期用于加工签名使用
	public static String CREATE_IP = "127.0.0.1";// key  请求的端的iP 地址
	public static String UFDODER_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";//统一下单接口 
	public static String NOTIFY_URL = "http://qiulupc.ittun.com/QiuLuPC/Re_notify";//回调地址,需要在公众号平台设置，一般10分钟左右生效（要是外网可以访问的）
}
