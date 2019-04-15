var flag = 0;
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
	getAddress();
	
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
//初始化验证插件
$(function () {
	Validator.language = 'zh';
});
//获取地址信息
function getAddress(){
	$.ajax({
		url:"../../Address/findAddress",
		success: function(data){
			if (data.object == null) {
				return;
			} else{
				$("#addBox").empty();
				var html = '';
				html += '<tr><td width="90" height="45" align="center" valign="middle">收货人</td><td width="350" align="center" valign="middle">详细地址</td><td width="110" align="center" valign="middle">手机</td><td width="110" align="center" valign="middle">&nbsp;</td><td width="110" align="center" valign="middle">操作</td></tr>';
				for (var i=0;i<data.object.length;i++) {
					html += '<tr><td align="center" valign="middle">'+
							data.object[i].receiver+
							'</td><td align="center" valign="middle" class="address_width">'+
							data.object[i].province+
							data.object[i].city+
							data.object[i].area+
							data.object[i].address+
							'</td><td align="center" valign="middle">'+
							data.object[i].telephone+
							'</td>';
					if (data.object[i].status==0) {
						html += '<td align="center" valign="middle" class="set"><a href="javascript:void(0)" onclick="defaultAdd('+
							data.object[i].id+
							')">设备默认</a></td>';
					}else{
						html += '<td align="center" valign="middle" class="set">&nbsp;</td>';
					}
					html += '<td align="center" valign="middle" class="edit"><a href="javascript:void(0)" onclick="editAdd('+
						data.object[i].id+
						')"><span>编辑</span></a><a href="javascript:void(0)" onclick="delAdd('+
						data.object[i].id+
						',this)"><span>删除</span></a></td></tr>';
				}
				$("#addBox").append(html);
			}
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

//设置为默认地址
function defaultAdd(id){
	$.ajax({
		url:"../../Address/defaultAddress",
		data:{"id":id},
		success: function(data){
			if (data.status) {
				getAddress();
			} else{
				new $.flavr({
				    content      : data.message,
				    closeOverlay : true,
				    closeEsc     : true
				});
			}
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

//删除地址
function delAdd(id,obj){
	$.ajax({
		url:"../../Address/deleteAddress",
		data:{"id":id},
		success: function(data){
			if (data.status) {
				$(obj).parent().parent().remove();
			} else{
				new $.flavr({
				    content      : data.message,
				    closeOverlay : true,
				    closeEsc     : true
				});
			}
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

//编辑地址
function editAdd(id){
	$.ajax({
		url:"../../Address/lookAddress",
		data:{"id":id},
		success: function(data){
			if (data.status) {
				flag = 1;
				$("#name").val(data.object.receiver);
				var li=$('.validator ul li:nth-of-type(2)').empty();
				var html='';
				html+='<span>*</span>所有区域：'
				+'<select id="s_province" name="province"></select> ' 
				+'<select id="s_city" name="city"></select> ' 
				+'<select id="s_county" name="area"></select>'
				+'<script class="resources library" src="../../js/area.js" type="text/javascript"></script>'
				+'<script type="text/javascript">var opt0 = ["'+data.object.province+'","'+data.object.city+'","'+data.object.area+'"];_init_area();</script>';
				$(li).html(html);
				html='';
				$("#status").val(data.object.status);
				$("#upId").val(data.object.id);
				$("#userId").val(data.object.userId);
				$("#address").val(data.object.address);
				$("#telephone").val(data.object.telephone);
			} else{
				new $.flavr({
				    content      : data.message,
				    closeOverlay : true,
				    closeEsc     : true
				});
			}
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

function confirmAdd(){
	var addId,receiver,address,telephone,html='';
	switch (flag){
		case 1:
			//编辑地址
			if (!Validator.validate($("#addForm"))) {
				return;
			}
			if ($("#s_province").val()=="省份" || $("#s_city").val()=="地级市" || $("#s_county").val()=="市、县级市") {
				new $.flavr({
				    content      : '请您选择地址',
				    closeOverlay : true,
				    closeEsc     : true
				});
				return;
			}
			$("#addForm").attr("action","../../Address/updateAddress");
			$("#addForm").ajaxSubmit(function(data){
				if (data.status) {
					new $.flavr({
					    content      : '保存成功',
					});
					
					window.location.href="personal_address.html";
				}
			});
			break;
		case 0:
			//新增地址
			if (!Validator.validate($("#addForm"))) {
				return;
			}
			if ($("#s_province").val()=="省份" || $("#s_city").val()=="地级市" || $("#s_county").val()=="市、县级市") {
				new $.flavr({
				    content      : '请您选择地址',
				    closeOverlay : true,
				    closeEsc     : true
				});
				return;
			}
			$("#addForm").attr("action","../../Address/addAddress");
			$("#addForm").ajaxSubmit(function(data){
				new $.flavr({
				    content      : '保存成功',
				    closeOverlay : window.location.href="personal_address.html",
				
				   });
				/*
				addId = data.object.id;
				receiver = data.object.receiver;
				address = data.object.province+data.object.city+data.object.area+data.object.address;
				telephone = data.object.telephone;
				html += '<tr><td align="center" valign="middle">'+
						receiver+
						'</td><td align="center" valign="middle" class="address_width">'+
						address+
						'</td><td align="center" valign="middle">'+
						telephone+
						'</td>';
				if (data.object.status==0) {
				html += '<td align="center" valign="middle" class="set"><a href="javascript:void(0)" onclick="defaultAdd('+
					addId+
					')">设备默认</a></td>';
				}	
				html += '<td align="center" valign="middle" class="edit"><a href="javascript:void(0)" onclick="editAdd('+
					addId+
					')"><span>编辑</span></a><a href="javascript:void(0)" onclick="delAdd('+
					addId+
					',this)"><span>删除</span></a></td></tr>';
				$("#addBox").append(html);	
			*/});
			break;
	}
}