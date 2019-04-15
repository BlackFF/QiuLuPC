var order;
$(document).ready(function(){
	order = getSessionStrongParam("order");
	$("#orderId").text(order.orderId);
	$("#total").text(order.total);
	$("#wxinpay").attr("src","../pay1?orderId="+order.orderId);
});

function gotoOrder(){
	orderDetail(order.orderId);
}