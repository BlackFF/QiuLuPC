package com.howin.qiulu.result;

import java.util.List;
public class CategoryResult {
private CategoryObject parentName;
private List<CategoryObject> sonName;
private List<CategoryObject> childName;
public CategoryObject getParentName() {
	return parentName;
}
public void setParentName(CategoryObject parentName) {
	this.parentName = parentName;
}
public List<CategoryObject> getSonName() {
	return sonName;
}
public void setSonName(List<CategoryObject> sonName) {
	this.sonName = sonName;
}
public List<CategoryObject> getChildName() {
	return childName;
}
public void setChildName(List<CategoryObject> childName) {
	this.childName = childName;
}
}
