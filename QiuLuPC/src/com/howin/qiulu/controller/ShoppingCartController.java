package com.howin.qiulu.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.howin.qiulu.Util.JsonUtils;
import com.howin.qiulu.pojo.ShoppingCart;
import com.howin.qiulu.pojo.User;
import com.howin.qiulu.result.QiuluResult;
import com.howin.qiulu.service.ShoppingCartService;

@Controller
public class ShoppingCartController {
	@Resource
	private ShoppingCartService shoppingCartService;
	
	

	/**
	 * @author 张磊
	 * @Description: 将商品加入到购物车中
	 * @return QiuluResult
	 * @date 2017年2月20日 下午2:11:29
	 * @param shoppingCart
	 */
	@RequestMapping("/addShoppingCart")
	@ResponseBody
	public QiuluResult addShoppingCart(HttpSession session,ShoppingCart shoppingCart,Integer skuId,Float total,Integer number) {
		User user=(User)session.getAttribute("user");
		QiuluResult result=new QiuluResult();
		if(user==null){
			result.setStatus(false);
			result.setMessage("用户未登录，请先登录！");
		}
		else{
		shoppingCart.setSkuId(skuId);
		shoppingCart.setUserId(user.getId());
		shoppingCart.setNumber(number);
		shoppingCart.setTotal(total);
	    result = shoppingCartService.addShoppingCart(shoppingCart);
		System.out.println(result);
		}
		return result;
	}
/**
 * 
 * @author 张磊
 * @Description: 从收藏夹中 添加多个商品到购物车中
 * @return QiuluResult
 * @date 2017年4月5日 下午4:25:22
 * @param session
 * @param shoppingCart
 * @param skuId
 * @return
 */
	@RequestMapping("/addShoppingCarts")
	@ResponseBody
	public QiuluResult addShoppingCarts(HttpSession session,Integer[] skuId) {
		User user=(User)session.getAttribute("user");
		QiuluResult result=new QiuluResult();
		if(user==null){
			result.setStatus(false);
			result.setMessage("用户未登录，请先登录！");
		}
		else{
				Integer userId=user.getId();
				result = shoppingCartService.addShoppingCarts(skuId,userId);
			   System.out.println(result);
		}
		return result;
	}
	/**
	 * @author 张磊
	 * @Description: 查看购物中的商品
	 * @return QiuluResult
	 * @date 2017年2月20日 下午4:21:27
	 * @param httpSession
	 * @param request
	 * @param response
	 */
	@RequestMapping("/queryShoppingCart")
	@ResponseBody
	public QiuluResult queryShoppingCart(HttpSession httpSession) {
		User user =(User)httpSession.getAttribute("user");
		Integer userId=user.getId();
		QiuluResult result = shoppingCartService.queryShoppingCart(userId);
		System.out.println(JsonUtils.objectToJson(result));
		return result;
	}

	/**
	 * @author 张磊
	 * @Description: 清空用户购物车
	 * @return QiuluResult
	 * @date 2017年2月21日 上午9:00:58
	 * @param httpSession
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/deleteShoppingCart")
	@ResponseBody
	public QiuluResult deleteShoppingCart(HttpSession httpSession) {
		User user =(User)httpSession.getAttribute("user");
		Integer userId=user.getId();
		QiuluResult result = shoppingCartService.deleteShoppingCart(userId);

		return result;
	}

	/**
	 * @author 张磊
	 * @Description: 删除用户所选中的商品（购物车中）
	 * @return QiuluResult
	 * @date 2017年2月21日 下午2:45:13
	 * @param httpSession
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/deleteShoppingCartById")
	@ResponseBody
	public QiuluResult deleteShoppingCartById(HttpSession httpSession,Integer[] id) {
		@SuppressWarnings("rawtypes")
		List list = new ArrayList();
		for(int i=0;i<id.length;i++){
			list.add(id[i]);
		}
		QiuluResult result = shoppingCartService.deleteShoppingCartById(list);
		return result;
	}
	/**
	 * @author 张磊
	 * @Description: 修改购物车中的商品数量
	 * @return QiuluResult
	 * @date 2017年2月21日 下午4:14:37
	 * @param shoppingCart
	 * @param httpSession
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/updateShoppingCartNumber")
	@ResponseBody 
	public QiuluResult updateShoppingCartNumber(Integer id,Integer number,Float total) {
		ShoppingCart cart=shoppingCartService.queryShoppingCartById(id);
		cart.setNumber(number);
		cart.setTotal(total);
		QiuluResult result = shoppingCartService.updateShoppingCartNumber(cart);
		System.out.println(result);																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																									
		return result;
	}
}
