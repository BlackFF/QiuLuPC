package com.howin.qiulu.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.howin.qiulu.Util.DOM4JReader;
import com.howin.qiulu.dao.ItemDao;
import com.howin.qiulu.dao.ShoppingCartDao;
import com.howin.qiulu.pojo.ShoppingCart;
import com.howin.qiulu.pojo.ShoppingCartResult;
import com.howin.qiulu.pojo.Sku;
import com.howin.qiulu.result.QiuluResult;
import com.howin.qiulu.result.ShoppingCartObject;
import com.howin.qiulu.service.ShoppingCartService;
@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

	@Resource
	private ShoppingCartDao shoppingCartDao;
	@Autowired
	private ItemDao itemDao;
 
	/**
	  * @author 张磊
	  * @Description: 将商品加入到购物车中
	  * @return QiuluResult
	  * @date 2017年2月20日 下午2:11:29
	  * @param shoppingCart
	  * @param httpSession
	  * @param request
	  * @param response
	  * @return
	  */
	@SuppressWarnings("unused")
	@Override
	public QiuluResult addShoppingCart(ShoppingCart shoppingCart) {
	
		QiuluResult result=new QiuluResult();
		//查询库存量
		Integer m=shoppingCartDao.queryRepertory(shoppingCart.getSkuId());
		//判断商品中是否存在该商品
		if(m==null){
			result.setStatus(false);
			result.setMessage("添加失败,商品中没有该商品信息");
		}else
		{
		//添加到购物车中的量
		Integer n=shoppingCart.getNumber();
		//查看购物车中是否已有该商品的信息
		ShoppingCart sh=shoppingCartDao.checkShoppingCart(shoppingCart);
		if(sh==null){
			//没有该商品 （第一次加入购物车）可以直接拿n值比较
		}
		else{
			//购物车中已有该商品 需要取得两次 数量的和  来比较库存量是否满足
			n=n+sh.getNumber();
		}
		if(n>m){
			result.setStatus(false);
			result.setMessage("库存不足");
		}
		else{
			Integer x=shoppingCartDao.addShoppingCart(shoppingCart);
			if(x==null){
				result.setStatus(false);
				result.setMessage("添加失败");
			  }
			else{ 
				result.setStatus(true);
				}
		}
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
	  * @return
	  */
	@SuppressWarnings("unchecked")
	@Override
	public QiuluResult queryShoppingCart(Integer userId) {
	List<ShoppingCartResult> list=shoppingCartDao.queryShoppingCart(userId);
        ShoppingCartObject cartObject=new ShoppingCartObject();
        DOM4JReader dom = new DOM4JReader();
        String demo[] = new String[2];
	    try {
		demo= dom.xmlRead();
	    } catch (Exception e) { 
		e.printStackTrace();
	    }
		Integer postLine=Integer.parseInt(demo[0]);
		Integer postage=Integer.parseInt(demo[1]);
		cartObject.setList(list);
		cartObject.setPostLine(postLine);
		cartObject.setPostage(postage);
		QiuluResult result=new QiuluResult();
		if(list.size()==0){
			result.setStatus(true);
			result.setMessage("购物车中无商品");
		}else{
			result.setStatus(true);
			result.setObject(cartObject);
		    }
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
	@Override
	public QiuluResult deleteShoppingCart(Integer userId) {
		Integer x=shoppingCartDao.deleteShoppingCart(userId);
		QiuluResult result=new QiuluResult();
		if(x==null){
			result.setStatus(false);
			result.setMessage("删除失败");
		}
		else{
			result.setStatus(true);
		}
		return result;
	}
	/**
	 * @author 张磊
	 * @Description: 删除用户所选中的商品（购物车中）
	 * @return QiuluResult
	 * @date 2017年2月21日 下午2:45:13
	 * @return
	 */
	@Override
	public QiuluResult deleteShoppingCartById(List list) {
		Integer m=shoppingCartDao.deleteShoppingCart(list);
		QiuluResult result=new QiuluResult();
		if(m==0){
			result.setStatus(false);
			result.setMessage("刪除失敗");
		}
		else{
			result.setStatus(true);
		}
		return result;
	}
	/**
	  * @author 张磊
	  * @Description: 修改购物车中的商品数量
	  * @return QiuluResult
	  * @date 2017年2月21日 下午4:14:37
	  * @param shoppingCart
	  * @return
	  */
	@Override
	public QiuluResult updateShoppingCartNumber(ShoppingCart shoppingCart) {
		//商品的库存量
		Integer m=shoppingCartDao.queryRepertory(shoppingCart.getSkuId());
		//要更新的商品数量
		Integer n=shoppingCart.getNumber();
		
		QiuluResult result=new QiuluResult();
		if(n>m){
			result.setStatus(false);
			result.setObject(m);
			result.setMessage("库存不足");
		}else{
	     shoppingCartDao.updateShoppingCartNumber(shoppingCart);
		result.setStatus(true);
		}
		return result;
	}
	
	
	//通过购物车ID 查找购物车中的一条记录 
	@Override
	public ShoppingCart queryShoppingCartById(Integer id) {

		return shoppingCartDao.queryShoppingCartById(id);
	}
	@Override
	public List<ShoppingCart> queryShoppingCartByIds(List id) {
		return shoppingCartDao.queryShoppingCartByIds(id);
	
	}
	@Override
	public QiuluResult addShoppingCarts(Integer[] skuId,Integer userId) {
		QiuluResult qiuluResult=new QiuluResult();
		boolean flg=true;
		for (int i = 0; i < skuId.length; i++) {
			Sku s=itemDao.querySkuById(skuId[i]);
			ShoppingCart shoppingCart=new ShoppingCart();
			shoppingCart.setSkuId(skuId[i]);
			shoppingCart.setUserId(userId);
			shoppingCart.setNumber(1);
			double p=s.getPrice();
			shoppingCart.setTotal((float)p);
			ShoppingCart sh=shoppingCartDao.checkShoppingCart(shoppingCart);
			if(sh==null){
				try {
					shoppingCartDao.saveShoppingCart(shoppingCart);
					
				} catch (Exception e) {
				 flg=false;
				}
			}
			else{
				
			  }
			
		    }
		if(flg){
			qiuluResult.setStatus(true);
			qiuluResult.setMessage("添加成功");
		  }
		else{
			qiuluResult.setStatus(false);
			qiuluResult.setMessage("添加失败");
		}
		
		return qiuluResult;
	}

}
