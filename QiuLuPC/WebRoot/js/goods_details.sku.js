/*sku组合选择*/

//li 选中.on
//li 不可选 .disable
//
//ul 没选中 .good_code_unSelect


var skuFun = (function(){
	
	//数组  不重复
	function Set() {
		this.value = [];
		if (typeof this.add != 'funciton') {
			Set.prototype.add = function(data) {
				if (this.value.indexOf(data) == -1) {
					this.value.push(data);
				}
			}
	
		}
	}
	
	function Map(){
		this.value = {};
		if(typeof this.push != 'function'){
			Map.prototype.push = function(key,value){
				this.value[key] = value;
				return this.value;
			};
			
			Map.prototype.get = function(key){
				if(key){
					return this.value[key];
				}else{
					return this.value;
				}
			};
			
			Map.prototype.hasKey = function(key){
				if(typeof key === 'string'){
					return this.value.hasOwnProperty(key);
				}
				return false;
			}
			
			Map.prototype.remove = function(key){
				if(this.hasKey(key)){
					delete this.value[key];
				}
			}
		}
	}
	
	function init(divu,skuArray){
		var div 
		,skuParamArray
		,skuIdMap;
		this.div = $(divu)[0];
		initskuParamArray(skuArray);
		initSkuIdArray(skuArray);
		printSku();

		//绑定sku属性选择事件
		$(this.div).on('click','dt a',function(event){
			var target = event.currentTarget;
			//当选择的sku属性不是已选中状态时才执行
			if(!target.classList.contains('goods_on')){
				selectSkuParam(event.currentTarget);
				tryToSkipUrl();
			}
		});
		initSkuSelect();
	}
	//
	//初始化sku属性数组  {'k':{'v':{可能存在的其他sku所具有的属性值/或者skuId}}} //	[{'k':'','v':'',other:{可能存在的其他sku所具有的属性值/或者skuId}}{}{}]
	function initskuParamArray(skuArray){
		skuFun.skuParamArray = {};
		var key,value,paramData;
		for(var i = skuArray.length-1; i >= 0; i --){
			paramData = JSON.parse(skuArray[i].paramData);
			for(var j = paramData.length-1; j >= 0; j --){
				key = paramData[j].k;
				value = paramData[j].v;
				if(!skuFun.skuParamArray[key]){
					skuFun.skuParamArray[key] = {};
					
				}
				if(!skuFun.skuParamArray[key][value]){
					skuFun.skuParamArray[key][value] = new Set();
				}
				skuFun.skuParamArray[key][value].add( skuArray[i].id );
			}
		}
	}
	
	//初始化skuId数组  通过已选择的属性组合检索出一个sku的id或者返回null
	function initSkuIdArray(skuArray){
		skuFun.skuIdMap = new Map();
		var key,value,paramData,tempArray = [];
		for(var i = skuArray.length-1; i >= 0; i --){
			paramData = JSON.parse(skuArray[i].paramData);
			//将一个sku的paramData属性值排序作为key值，sku的id作为value值存入Map skuFun.skuIdMap
			for(var j = paramData.length-1; j >= 0; j --){
				tempArray.push(paramData[j].v);
			}
			skuFun.skuIdMap.push(tempArray.sort().join(''),skuArray[i].id);
			tempArray = [];
		}
		
	}
	//根据skuFun.skuParamArray 打印
	function printSku(){
		var temp_html = '';
		for(var key in skuFun.skuParamArray){
			temp_html += 
				'<li><dd  class="good_size">'+ key +'</dd><dt class="good_code">';
			for(var value in skuFun.skuParamArray[key]){
				temp_html += '<a href="javaScript:void(0)" data-skuIds="'+JSON.stringify(skuFun.skuParamArray[key][value].value)+'" data-skuParamKey="'+key+'" data-skuValue="'+value+'" ><span>'+ value +'</span></a>';
			}
			temp_html +='</dt></li>';
		}
		$(skuFun.div).prepend(temp_html);
	}
	
	
	//当每个sku属性组为已选择状态，根据选择的属性检索出一个skuid
	function getskuIdBySelectSkuParam(){
		var temp_array = [];
		//得到所有已经选中的sku属性
		$('.goods_on').each(function(){
			temp_array.push(this.dataset.skuvalue);
		});
		var id = skuFun.skuIdMap.get(temp_array.sort().join(''));
		return id;
	}
	
	//选择一个sku属性
	function selectSkuParam(a){
		var $a = $(a);
		if(a.classList.contains('disable')){
		//选择一个虚化的属性
			//移除此sku属性的disable类名
			$a.removeClass('disable');
		}else{
		//选择了一个普通元素
		}
		//移除此sku属性组的背景提示
		$a.parent().removeClass('good_code_unSelected');
		//移除同级的已选属性的 goods_on 类名
		$a.siblings('.goods_on').removeClass('goods_on');
		//添加goods_on类名
		$a.addClass('goods_on');
		
		//依次检测其他的每组sku属性，将不可匹配的选项“虚化”，
		var $li = $(a).parent().parent();
		var $lis = $li.siblings();
		$.each($lis,function(index){
			//当此sku属性组内没有选中的属性，有标记颜色，则跳过检测
			if(!this.querySelector('dt').classList.contains('good_code_unSelected')){
				$('dt a',this).each(function(){
					//a 是选择的sku属性  this是要检测的sku属相
					if(!isMixed(this.dataset.skuids,a.dataset.skuids)){
					//没有相同同时存在的sku
						$(this).addClass('disable')
						//已选项不匹配，去除选择，将此sku属性组标记颜色
						if(this.classList.contains('goods_on')){
							$(this).removeClass('goods_on');
							$(this).parent().addClass('good_code_unSelected');
						}
					}else{
					//有相同同时存在的sku
						$(this).removeClass('disable');
					}
				});
			}
			
		});
	}
	
	//当每个sku属性组为已选择状态，根据选择的属性检索出一个skuid
	function tryToSkipUrl(){
		if($('.good_code_unSelected').length === 0){
			var id = getskuIdBySelectSkuParam();
			if(id !== window.targetSku.id){
				//跳转页面
				window.location.href="goods_details.html?id="+id+"";
			}
		}
	}
	
	
	/**
	 * 初始选择本sku选择
	 */
	function initSkuSelect(){
		var param = JSON.parse(window.targetSku.paramData);
		for(var i = param.length-1; i >= 0; i --){
//			$("dt a[data-skuParamKey='"+param[i].k+"'][data-skuValue='"+param[i].v+"']",skuFun.div).click();
			
			selectSkuParam($("dt a[data-skuParamKey='"+param[i].k+"'][data-skuValue='"+param[i].v+"']",skuFun.div)[0]);
		}
	}
	
	
	
	//判断两个数组是否有交集	数组值是基本数据类型
	function isMixed(array1,array2){
		var arr1 = JSON.parse(array1);
		var arr2 = JSON.parse(array2);
		if(arr1 instanceof Array && arr2 instanceof Array){
			for(var i = arr1.length-1; i >= 0; i --){
				for(var j = arr2.length-1; j >= 0; j --){
					if(arr1[i] === arr2[j]){
						return true;
					}
				}
			}
		}
		return false;
	}
	
	//得到多个数组的交集
	function getMixed(){
		
	}
	
	return {init:init}
}())
