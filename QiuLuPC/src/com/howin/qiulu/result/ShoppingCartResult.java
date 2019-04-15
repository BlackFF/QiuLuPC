package com.howin.qiulu.result;

public class ShoppingCartResult {
private Integer id;
private Integer skuId;
private String image;
private String sellPoint;
private float price;
private float discount;
private String deliveryPlace;
private Integer number;
private float total;

public ShoppingCartResult(Integer id, Integer skuId, String image, String sellPoint, float price, float discount,
		String deliveryPlace, Integer number, float total) {
	super();
	this.id = id;
	this.skuId = skuId;
	this.image = image;
	this.sellPoint = sellPoint;
	this.price = price;
	this.discount = discount;
	this.deliveryPlace = deliveryPlace;
	this.number = number;
	this.total = total;
}
public Integer getId() {
	return id;
}
public void setId(Integer id) {
	this.id = id;
}
public Integer getSkuId() {
	return skuId;
}
public void setSkuId(Integer skuId) {
	this.skuId = skuId;
}
public String getImage() {
	return image;
}
public void setImage(String image) {
	this.image = image;
}
public String getSellPoint() {
	return sellPoint;
}
public void setSellPoint(String sellPoint) {
	this.sellPoint = sellPoint;
}
public float getPrice() {
	return price;
}
public void setPrice(float price) {
	this.price = price;
}
public float getDiscount() {
	return discount;
}
public void setDiscount(float discount) {
	this.discount = discount;
}
public String getDeliveryPlace() {
	return deliveryPlace;
}
public void setDeliveryPlace(String deliveryPlace) {
	this.deliveryPlace = deliveryPlace;
}
public Integer getNumber() {
	return number;
}
public void setNumber(Integer number) {
	this.number = number;
}
public float getTotal() {
	return total;
}
public void setTotal(float total) {
	this.total = total;
}
@Override
public String toString() {
	return "ShoppingCartResult [id=" + id + ", skuId=" + skuId + ", image=" + image + ", sellPoint=" + sellPoint
			+ ", price=" + price + ", discount=" + discount + ", deliveryPlace=" + deliveryPlace + ", number=" + number
			+ ", total=" + total + "]";
}

}
