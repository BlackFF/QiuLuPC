package com.howin.qiulu.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Session;
import org.springframework.stereotype.Service;

import com.howin.qiulu.Util.Page;
import com.howin.qiulu.dao.FavoritesDao;
import com.howin.qiulu.pojo.Favorites;
import com.howin.qiulu.pojo.FavoritesResult;
import com.howin.qiulu.result.QiuluResult;
import com.howin.qiulu.service.FavoritesService;

import sun.print.resources.serviceui;

@Service
public class FavoritesServiceImpl implements FavoritesService {
	@Resource
	private FavoritesDao favoritesDao;
  
	/**
	 * @author 张磊
	 * @Description: 将商品加入到收藏夹中
	 * @return QiuluResult
	 * @date 2017年2月22日 上午9:30:25
	 * @param skuId
	 * @param httpSession
	 * @param response
	 * @return
	 */
	@Override
	public QiuluResult addFavorites(Favorites favorites) {
		Favorites n = favoritesDao.addFavorites(favorites);
		QiuluResult result = new QiuluResult();
		if (n == null) {
			result.setStatus(false);
			result.setMessage("添加失败");
		} else {
			result.setStatus(true);
			result.setObject(n);
			result.setMessage("添加成功");
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
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public QiuluResult findFavorites(Integer pageNo,Integer pageSize, Integer userId) {
		Page page=new Page();
		//查询总条数
		Integer allRow=favoritesDao.queryFavoritesNumber(userId);
		//当前记录页
		  Integer offSet=page.countOffset(pageNo, pageSize);
		//当前页的结果集 
	   List<FavoritesResult> list = favoritesDao.findFavorites(pageSize,offSet,userId);
	   page.setList(list);
	   page.setPageNo(pageNo);
	   page.setPageSize(5);
	   page.setTotalRecords(allRow);
		QiuluResult result = new QiuluResult();
		if (list == null) {
			result.setStatus(false);
			result.setMessage("查看失败");
		} else {
			if (list.size() == 0) {
				result.setStatus(true);
				result.setMessage("收藏夹中未添加商品");
			} else {
				result.setObject(page);
				result.setStatus(true);
			}
		}

		return result;
	}

	/**
	 * @author 张磊
	 * @Description: 删除收藏夹中的商品
	 * @return QiuluResult
	 * @date 2017年2月22日 上午11:07:39
	 * @param id
	 * @param httpSession
	 * @param request
	 * @param response
	 * @return
	 */
	@Override
	public QiuluResult deleteFavorites(Integer[] id) {
		QiuluResult result = new QiuluResult();
		boolean b = favoritesDao.deleteFavorites(id);
		if (!b) {
			result.setStatus(false);
			result.setMessage("删除失败");
		} else {
			result.setStatus(true);
			result.setMessage("删除成功");
		}
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
	@Override
	public QiuluResult queryFavoritesBySkuId(Integer userId,Integer skuId) {
		QiuluResult result = new QiuluResult();
		Favorites x=favoritesDao.queryFavoritesBySkuId(userId,skuId);
		if(x==null){
			result.setStatus(false);
			result.setMessage("未收藏该商品");
		}
		else{
			result.setStatus(true);
			result.setObject(x);
			result.setMessage("已收藏该商品");
		}
		return result;
	}

	@Override
	public QiuluResult findFavoritesNum(Integer userId) {
		QiuluResult result=new QiuluResult();
		result.setStatus(true);
		result.setObject(favoritesDao.queryFavoritesNumber(userId));
		return result;
	}
}
