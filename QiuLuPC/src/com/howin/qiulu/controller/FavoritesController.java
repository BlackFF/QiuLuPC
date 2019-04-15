package com.howin.qiulu.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.howin.qiulu.pojo.Favorites;
import com.howin.qiulu.pojo.User;
import com.howin.qiulu.result.QiuluResult;
import com.howin.qiulu.service.FavoritesService;

@Controller
public class FavoritesController {
	@Resource
	private FavoritesService favoritesService;

	/**
	 * @author 张磊
	 * @Description: 将商品加入到收藏夹中
	 * @return QiuluResult
	 * @date 2017年2月22日 上午9:30:25
	 * @param skuId
	 * @param httpSession
	 * @param request
	 * @param response
	 */
	@RequestMapping("/addFavorites")
	@ResponseBody
	public QiuluResult addFavorites(HttpSession session,Integer skuId) {
		QiuluResult result=new QiuluResult();
		System.out.println(result);
		User user=(User)session.getAttribute("user");
		if(user==null){
			result.setStatus(false);
			result.setMessage("用户未登录，请先登录！");
		}
		else{
		Favorites favorites = new Favorites();
		favorites.setSkuId(skuId);
		favorites.setUserId(user.getId());
		result = favoritesService.addFavorites(favorites);
	    }
		return result;
	    }
	/**
	 * @author 张磊
	 * @Description: 查看收藏夹中的商品
	 * @return QiuluResult
	 * @date 2017年2月22日 上午10:35:47
	 * @param httpSession
	 * @param request
	 * @param response
	 */
	@RequestMapping("/findFavorites")
	@ResponseBody
	public QiuluResult findFavorites(Integer pageNo,HttpSession session) {
      User user=(User) session.getAttribute("user");
		
		if(user==null){
			QiuluResult result = new QiuluResult();
			result.setStatus(false);
			result.setMessage("没登录请不要发送这个请求");
			return result;
		}else{
		// 定义page类
		if (pageNo == null) {
			pageNo = 1;
		}

		QiuluResult result = favoritesService.findFavorites(Integer.valueOf(pageNo), 5, user.getId());
		return result;
		}
	}

	/**
	 * @author 张磊
	 * @Description: 删除收藏夹中的商品
	 * @return QiuluResult
	 * @date 2017年2月22日 上午11:07:39
	 * @param id
	 */
	@RequestMapping("/deleteFavorites")
	@ResponseBody
	public QiuluResult deleteFavorites(Integer[] id) {
		QiuluResult result = favoritesService.deleteFavorites(id);
		return result;
	}

	/**
	 * @author 张磊
	 * @Description:查看用户是否收藏某件商品
	 * @return QiuluResult
	 * @date 2017年3月28日 上午10:39:55
	 * @param session
	 * @param skuId
	 * @return
	 */
	@RequestMapping("/queryFavoritesBySkuId")
	@ResponseBody
	public QiuluResult queryFavoritesBySkuId(HttpSession session,Integer skuId) {
		User user=(User) session.getAttribute("user");
		
		if(user==null){
			QiuluResult result = new QiuluResult();
			result.setStatus(false);
			result.setMessage("没登录请不要发送这个请求");
			return result;
		}
		else{
		Integer userId=user.getId();
		QiuluResult result = favoritesService.queryFavoritesBySkuId(userId,skuId);
		return result;
		}
	}
	
	/**
	 * @author 杨冰冰
	 * @Description: 查看收藏夹里面一共有多少条
	 * @return QiuluResult
	 * @date 2017年4月6日 下午2:25:23
	 * @param session
	 * @return
	 */
	@RequestMapping("/findFavoritesNum")
	@ResponseBody
	public QiuluResult findFavoritesNum(HttpSession session){
		QiuluResult result=new QiuluResult();
		User user=(User) session.getAttribute("user");
		result=favoritesService.findFavoritesNum(user.getId());
		return result;
				
	}
	
	
	
	
	
	
	
	
	
	
}
