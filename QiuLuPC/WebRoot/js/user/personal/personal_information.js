$(document).ready(function(){
	if (!isLogin()) {
    	new $.flavr({
		    content     : '您当前没有登录,是否跳转登陆页面？',
		    dialog      : 'confirm',
		    onConfirm   : function(){
		        location.href = path + "/login.html?backUrl="+encodeURIComponent(location.href);
		    },
		    onCancel    : function(){
		    }
		});
    	return false;
    }
	
	getUserDetail();
	//初始化验证插件
	$(function () {
		Validator.language = 'zh';
	});
	
	//页眉
	$.ajax({ url : "../../User/isLogin",//请求地址 
		dataType : "json",//数据格式  
		type : "post",//请求方式 
		async : false,//是否异步请求 
		success : function(data){
			console.log(data)
			if(data.status){
				var tital=$('.list li:nth-of-type(2)').empty();
				var xs='';
				if(data.status){
					xs+='<li><a href="../../user/personal/personal_information.html" >'+data.object.username+'</a></li>'
					   +'<li><a href="javascript:void(0)" onclick="logout()" >退出登录</a></li>';
				}
				else{
					xs+='<li><a href="../../login.html" >请登录</a></li>';
				}
				$(tital).html(xs);
				xs='';
			   }
			},
	})

	//客服服务
	callCustomerService();
});
/*
 * 获取个人信息
 */
function getUserDetail(){
	
	console.log(11111);
	$.ajax({
		url:"../../User/lookUserDetail",
		dataType : "json",//数据格式  
		type : "post",//请求方式 
		async : false,//是否异步请求 
		success: function(data){
			    console.log(data)
			    var headPort=data.object.userDetatil.headPort
				 var div=$('.c6_main').empty();
				 var temp_html = '';
				 temp_html +='<div class="c6_table">'
					       +'<form id="addForm" class="validator" method="post" name="uform" onsubmit="return false">'
					       +'<input type="hidden" id="userId" name="userId" value="'+data.object.userDetatil.userId+'"/>'
					       +'<table width="830" border="0" cellspacing="0" cellpadding="0">'
					       +' <tr>'
					       +'<td width="150" height="40" align="right" valign="middle">用户名：</td>'
					       +'  <td align="left" valign="middle" class="c6_username">'+data.object.user.username+'</td>'
					       +'</tr>'
					       +'<tr>'
					       +'<td height="40" align="right" valign="middle">会员等级：</td>'
					       +'<td align="left" valign="middle" class="c6_member">';
					
					       if(data.object.userDetatil.vip==0){
					    	   temp_html +='普通会员';
					       }else{
					    	   temp_html +='高级会员';
					       }
					       temp_html +='</td>'
					       +' </tr>'
					       +' <tr>'
					       +' <td height="40" align="right" valign="middle">真实姓名：</td>'
					       +' <td align="left" valign="middle" class="c6_name"><input type="text" id="name" name="name" class=""';
					       if(data.object.userDetatil.name==null||data.object.userDetatil.name==""){
					    	   temp_html +='';
					       }else{
					    	   temp_html += 'value="'+data.object.userDetatil.name+'';
					       }
					       temp_html += '" data-required/><td>'
					       +'</tr>'
					       +'<tr>'
					       +' <td height="40" align="right" valign="middle">性别：</td>'
					       +' <td align="left" valign="middle" class="c6_gender">';
					       if(data.object.userDetatil.gender==null||data.object.userDetatil.gender==""){
					    	   temp_html +='<span><input type="radio" value="2" id="gender" name="gender">保密 </span>'
							       +'<span><input type="radio" value="1" name="gender">男 </span>'
							       +'<span><input type="radio" value="0" name="gender">女 </span>';
					       }
					       else if(data.object.userDetatil.gender==0){
					    	   temp_html +='<span><input type="radio" value="2" id="gender" name="gender">保密 </span>'
							       +'<span><input type="radio" value="1" name="gender">男 </span>'
							       +'<span><input type="radio" value="0" name="gender" checked>女 </span>';
					       }
					       else if(data.object.userDetatil.gender==1){
					    	   temp_html +='<span><input type="radio" value="2" id="gender" name="gender">保密 </span>'
							       +'<span><input type="radio" value="1" name="gender" checked>男 </span>'
							       +'<span><input type="radio" value="0" name="gender">女 </span>';
					       }
					       else if(data.object.userDetatil.gender==2){
					    	   temp_html +='<span><input type="radio" name="gender" value="2" id="gender" checked>保密 </span>'
						       +'<span><input type="radio" value="1" name="gender">男 </span>'
						       +'<span><input type="radio" value="0" name="gender">女 </span>';
					       }
					 
					       temp_html +='</td>'
					       +' </tr>'
					       +'<tr>'
					       +'  <td height="40" align="right" valign="middle">生日：</td>'
					       +' <td align="left" valign="middle" class="c6_name">';
					       if(data.object.userDetatil.birthday==""||data.object.userDetatil.birthday==null){
					    	   temp_html +=' <input placeholder=" 请选择生日" readonly="readonly" name="birthday" id="appDate">';
					       }
					       else{
					    	   temp_html +=' <input value="'+data.object.userDetatil.birthday+'" readonly="readonly" name="birthday" id="appDate" type="text">';
					       }
					       temp_html +='</td>'
					       +'</tr>'
					       +' <tr>'
					       +' <td height="40" align="right" valign="middle">手机号码：</td>'
					       +' <td align="left" valign="middle"class="c6_telephone">'+data.object.user.telephone+'</td>'
					       +'</tr>'
					       +'<tr>'
					       +'<td height="40" align="right" valign="middle">所在地：</td>'
					       +'<td align="left" valign="middle">'
					       +'<select id="s_province"  name="province">'
					       +'<option>'+data.object.userDetatil.province+'</option>'
					       +'</select>' 
					       +'<select id="s_city" name="city" ></select>' 
					       +' <select id="s_county" name="area"></select>'
					       +' <script class="resources library" src="../../js/area.js" type="text/javascript"></script>'
					       +' <script type="text/javascript">var opt0 = ["'+data.object.userDetatil.province+'","'+data.object.userDetatil.city+'","'+data.object.userDetatil.area+'"];_init_area();</script>'
					       +'</td>'
					       +'</tr>'
					       +' <tr>'
					       +' <td height="40" align="right" valign="middle">详细地址：</td>'
					       +'<td align="left" valign="middle" class="c6_detailed_address"><input type="text" id="adress" name="adress"';
					      if(data.object.userDetatil.adress==null){
					    	  temp_html +='';
					      } 
					      else{
					    	  temp_html +='value="'+data.object.userDetatil.adress+'';
					      }
					       temp_html +='" data-required/></td></tr>'
					       +'<tr height="60">'
					       +'<td class="c6_submit">'
					       +'<input type="button" id="sub" value="提交" onclick="addUserDetail();return false"/>'
					       +'</td>'
					       +'</tr>'
					       +'</table>'
					       +'</form>'	
				
					       +'<figure class="add_picture">';
					       if(data.object.userDetatil.headPort==null){
					    	   temp_html +='<img src="../../images/add_picture.png"  id="imgHead">';
					       }
					       else{
					    	
					    	   temp_html +='<img style="width:120px;height:140px" src="'+headPort_images+''+headPort+'" id="imgHead">';
					       }
					       temp_html +='<figcaption>'
					       +'<input type="file" onchange="uploadHead()" id="basicInfoHead"  name="file"/>'
					       +'<span>修改头像</span>'
					       +'</figcaption>'
					       +'</figure></div>';
					 
					       $(div).html(temp_html);
						 	temp_html = '';
			
		},
		error:function(){
			new $.flavr({
			    content      : '服务器异常',
			    closeOverlay : true,
			    closeEsc     : true
			});
		}
	});
}

/**
 * 
 */
function addUserDetail(){
	
	   //document.forms[0].submit();
	    document.getElementById('sub').disable='disable';
	if (!Validator.validate($("#addForm"))) {
		return false;
	}
	if ($("#s_province").val()=="省份" || $("#s_city").val()=="地级市" || $("#s_county").val()=="市、县级市") {
		new $.flavr({
		    content      : '请您选择地址',
		    closeOverlay : true,
		    closeEsc     : true
		});
		return false;
	}
	$("#addForm").attr("action","../../User/saveUserDetail");
	$("#addForm").ajaxSubmit(function(data){
		if (data.status) {
			new $.flavr({
			    content      : '修改个人信息成功',
			    closeOverlay : true,
			    closeEsc     : true
			});
			getUserDetail();
		}
	});
}

/**
 * 上传头像
 */
function uploadHead(){  
 	$.ajaxFileUpload({
 		url: "../../User/uploadHeadPort",
 		secureuri: false,
 		fileElementId: "basicInfoHead", //文件选择框的id属性
 		enctype:'multipart/form-data',
 		dataType: 'json', //json  
 		success: function(data) {

 			if (data.status) {
 				location.reload();
 			  }
 			
 		},
 		error: function(data) {
 			new $.flavr({
			    content      : '服务器异常',
			    closeOverlay : true,
			    closeEsc     : true
			});
 		}
 	});
}; 

