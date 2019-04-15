

(function(){
	
	//鼠标放在分类上时显示同级分类
	
	function showSibCategory(event){
		var currentTarget = event.currentTarget;
		var sibling = currentTarget.parentElement.nextElementSibling;
		$(sibling).css('display','block');
		var i = currentTarget.querySelector('i');//nextElementSibling;
		i.style.backgroundPositionY = '-17px';
	}
	
	//鼠标移出同级分类时隐藏同级分类
	
	function hidSibCategory(event){
		var currentTarget = event.currentTarget;
		var sibling = currentTarget.previousElementSibling;
		$(currentTarget).css('display','none');
		var i = sibling.querySelector('i');//nextElementSibling;
		i.style.backgroundPositionY = '-11px';
	}
	
	$('.list_title').on('mouseover','.list_title_drop',function(event){
		showSibCategory(event);
	});
	
	$('.list_title').on('mouseleave','.list_title_open',function(event){
		hidSibCategory(event);
	});
	$('.list_title').on('mouseleave','.list_title_drop',function(event){
		var related = event.relatedTarget;
		var sibling = event.currentTarget.parentElement.nextElementSibling;
		if(related !== sibling){
			$(sibling).mouseleave();
		}
	});
	
	//显示和隐藏 “最近浏览”“畅销排行榜”
	
	function top_twoFocus(event){
		var currentTarget = event.currentTarget;
		var sibA = $(currentTarget).siblings('a');
		var sibUl = $(currentTarget).siblings('ul');
		$(sibA).removeClass('top_two');
		$(currentTarget).addClass('top_two');
		
		//显示“最近浏览”
		if(currentTarget.parentElement.children[0] === currentTarget){
			$(sibUl[0]).removeClass('none');
			$(sibUl[1]).addClass('none');
		//显示“畅销排行榜”
		}else{
			$(sibUl[0]).addClass('none');
			$(sibUl[1]).removeClass('none');
		}
	}
	
	$('.browse').on('mouseover','a',function(event){
		top_twoFocus(event);
	});
	
	
	
})()
