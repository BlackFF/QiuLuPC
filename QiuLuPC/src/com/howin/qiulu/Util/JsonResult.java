package com.howin.qiulu.Util;
/**
 * 用于封装服务器到客户端的返回值（是Json字符串）
 * JsonResult在jQuery里使用
 */
import java.io.Serializable;

public class JsonResult<T> implements Serializable {// 泛型List<T>
	private static final int SUCCESS=0;
	private static final int ERROR=1;
	private int status;
	private String message="";
	private T data;//T是一个对象信息
	public JsonResult(){
		status = SUCCESS;
	}
	//为了方便，我们要重载多个构造器
//	{"state":0,"message":"","data":[{"name":"wsf","id":"0037215c-09fe-]
	public JsonResult(int status, String message, T data) {
		super();
		this.status = status;
		this.message = message;
		this.data = data;
	}
	//错误信息
	public JsonResult(String error){
		this(ERROR,error,null);
	}
	//this重载：调用匹配参数的构造器，即上面带3个参数的构造器,将this(3个参数)分别赋值给构造器
	//集合
	
	
	//匹配成功时调用
	//自动转为json字符串，由于是将对象（JsonResult）转成json，所以浏览器响应的是{"属性":"属性值"，...}
	//所以浏览器端肯定有对象的所有属性即这里有3个：state,message,data;对应值分别是this()中的，其中，data是由表示层页面传进来
	//的参数（由于user也是一个对象，转成json字符串时仍然是{}格式）
	public JsonResult(T data){
		this(SUCCESS,"",data);
	}
	public JsonResult(int status){
		this(status,"",null);
	}
	//this重载：调用匹配参数的构造器，即上面带3个参数的构造器
	//业务层实现类只要抛出异常，在表示层就会调用；并且把抛出异常时存储的message信息获取，向页面展示；抛出异常时调用
	//业务层只有抛出异常，就会回到表示层去执行catch异常中的代码（调用这里），同样由于也是讲对象转成json，所以页面显示也是{"属性","属性值"...}
	//同样页面显示的属性必须有对象（JsonResult）的三个属性：state,message,data；对应值是this()参数中的三个值，其中e是由调用者传进来的参数（也就是抛出异常时所储存的信息）
	public JsonResult(Throwable e){
		this(ERROR,e.getMessage(),null);
	}
	
	
	
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public T getData() {
		return data;
	}
	public void setData(T data) {
		this.data = data;
	}
	public static int getSuccess() {
		return SUCCESS;
	}
	public static int getError() {
		return ERROR;
	}
	@Override
	public String toString() {
		return "JsonResult [state=" + status + ", message=" + message + ", data=" + data + "]";
	}
	
}






















