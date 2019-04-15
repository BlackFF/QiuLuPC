package com.howin.qiulu.result;

import java.util.List;

import com.howin.qiulu.pojo.ShoppingCartResult;

public class ShoppingCartObject {
private Integer postLine;
private Integer postage;
private List<ShoppingCartResult> list;


public ShoppingCartObject(){
	
}

public ShoppingCartObject(Integer postLine, Integer postage, List<ShoppingCartResult> list) {
	super();
	this.postLine = postLine;
	this.postage = postage;
	this.list = list;
}



public Integer getPostLine() {
	return postLine;
}



public void setPostLine(Integer postLine) {
	this.postLine = postLine;
}



public Integer getPostage() {
	return postage;
}



public void setPostage(Integer postage) {
	this.postage = postage;
}



public List<ShoppingCartResult> getList() {
	return list;
}



public void setList(List<ShoppingCartResult> list) {
	this.list = list;
}



}
