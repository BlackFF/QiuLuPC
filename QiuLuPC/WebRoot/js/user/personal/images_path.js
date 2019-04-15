//图片路径
	var images_path80='http://192.168.0.200:8081/QiuLu/images/80x80/';
	var images_path240='http://192.168.0.200:8081/QiuLu/images/240x240/';
	var images_path500='http://192.168.0.200:8081/QiuLu/images/500x500/';
	var evaluation_images='http://192.168.0.141:8080/QiuLu/images/evaluation/';
	var headPort_images='http://192.168.0.141:8080/QiuLu/images/headPort/';
	var onSale_images='http://192.168.0.141:8080/QiuLu/images/onSale/'
    
		 
		
		
//注销登录
function logout(){
	$.ajax({
		url:"../../User/logout",
		type:"post",
		dataType:"json",
		async:false,
		success: function(data){
			if(data.status){
				window.location.href="../../index.html";
			}
		},
	})
	
}