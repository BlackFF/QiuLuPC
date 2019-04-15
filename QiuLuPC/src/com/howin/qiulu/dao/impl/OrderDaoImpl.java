package com.howin.qiulu.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import com.howin.qiulu.dao.OrderDao;
import com.howin.qiulu.pojo.Evaluation;
import com.howin.qiulu.pojo.Order;
import com.howin.qiulu.pojo.OrderDetail;
import com.howin.qiulu.pojo.OrderResult;
import com.howin.qiulu.pojo.ShoppingCart;
import com.howin.qiulu.pojo.Sku;
import com.howin.qiulu.result.AbandonOrder;
import com.howin.qiulu.result.OrderdeResult;

@Repository("OrderDao")
public class OrderDaoImpl extends HibernateDaoSupport implements OrderDao {
	@Resource
	private SessionFactory sessionFactory;

	@Autowired
	public void setMySessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	/***
	 * Title: 取消订单 Description:
	 * 
	 * @author 杨冰冰
	 * @date 2017年2月22日 上午11:28:25
	 * 
	 */
	@Override
	public int cancelOrder(Long id) {

		Session session=sessionFactory.openSession();
		Transaction tr=session.beginTransaction();
		Order od=(Order)session.createQuery("from Order o where o.id=:i").setParameter("i",id ).uniqueResult();
		od.setStatus(4);
		session.update(od);
		//取消订单后，归换Sku的库存量
        List<OrderDetail> list=(List<OrderDetail>)session.createQuery("from OrderDetail o  where o.orderId=:i order by o.skuId").setParameter("i",id).list();
		for (int j = 0; j < list.size(); j++) {
			Integer x=list.get(j).getskuId();
			Integer y=list.get(j).getNumber();
			Sku s=(Sku)session.createQuery("from Sku s where s.id=:i").setParameter("i",x).uniqueResult();
		    s.setRepertory(s.getRepertory()+y);
		    session.update(s);
		}
        tr.commit();
		session.close();
		return 1;
	}

	/***
	 * Title: 查看订单 Description:
	 * 
	 * @author 杨冰冰
	 * @date 2017年2月22日 下午1:37:22
	 * 
	 */
	@Override
	public List<Order> findOrderByUserId(Integer userId) {

		Session session=sessionFactory.openSession();
		Transaction tr=session.beginTransaction();
		List<Order> list=session.createQuery("from Order where status in (0,1,2,3,4) and userId=:u ").setInteger("u", userId).list();
		tr.commit();
		session.close();
		return list;
	}


	/*** 
	* Title: 确认收货，订单完成
	* Description: 
	* @author 杨冰冰
	* @date 2017年2月22日 下午2:45:53
	* 
	*/
	@Override
	public Order finishOrder(Order order) {
		Session session=sessionFactory.openSession();
		Transaction tr=session.beginTransaction();
		session.update(order);
		tr.commit();
		session.close();
		return order;
	}
	/*** 
	* Title: 订单评价
	* Description: 
	* @author 杨冰冰
	* @date 2017年2月22日 下午3:01:00
	* 
	*/
	@Override
	public Evaluation evaluationByOrderId(Evaluation ev) {
		Session session=sessionFactory.openSession();
		Transaction tr=session.beginTransaction();
		session.save(ev);
		tr.commit();
		session.close();
		return ev;
	}
	/*** 
	* Title: 删除订单
	* Description: 
	* @author 杨冰冰
	* @date 2017年2月23日 上午11:21:01
	* 
	*/
	@Override
	public Integer deleteOrder(Long id) {
		Session session=sessionFactory.openSession();
		Integer i= session.createQuery("delete from Order where id=:i").setParameter("i", id).executeUpdate();
		List list=(List)session.createQuery("select id from OrderDetail o  where o.orderId=:i ").setParameter("i",id).list();
		for(int j = 0; j < list.size(); j++){
			Integer in=session.createQuery("delete from OrderDetail where id=:d").setParameter("d", list.get(j)).executeUpdate();
		}
		session.close();
		return i;
	}
	/*** 
	* Title: 根据id找订单
	* Description: 
	* @author 杨冰冰
	* @date 2017年2月23日 上午11:21:01
	* 
	*/
	@Override
	public Order findOrderById(Long id) {
		Session session=sessionFactory.openSession();
		Transaction tr=session.beginTransaction();
		Order order=(Order) session.createQuery("from Order where id=:i").setParameter("i", id).uniqueResult();
		tr.commit();
		session.close();
		return order;
	}

	/**
	 * @author 张磊
	 * @Description: 保存订单
	 * @return Long
	 * @date 2017年2月23日 下午4:18:30
	 * @param receivingAddress
	 * @return
	 */
	@Override
	public Long saveOrder(Order order) {
		Long x = null;
		Session session = sessionFactory.openSession();
		Transaction tr = session.beginTransaction();
		try {
			session.save(order);
			x = order.getId();

		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			tr.commit();
			session.close();
		}

		return x;
	}

	/**
	 * @author 张磊
	 * @Description: 保存订单详情
	 * @return Boolean
	 * @date 2017年2月23日 下午4:18:30
	 * @param receivingAddress
	 * @return
	 */
	@Override
	public boolean saveOrderDetail(List<OrderDetail> oederList) {
		boolean flg = true;
		Session session = sessionFactory.openSession();
		Transaction tr = session.beginTransaction();
		  if(oederList.size()==0){
			flg=false;
	      	  }
		try {
			 for (int i = 0; i < oederList.size(); i++) {
				OrderDetail od = oederList.get(i);
				System.out.println(od);
				session.save(od);
			  }
		     } catch (Exception e) {
			 flg = false;
		      }
		if(flg){
			tr.commit();
		  }
		session.close();
		return flg;

	}
	
	//通过订单编号查看订单表
	@Override
	public Order findOrderByOrderId(Long orderId) {
		Session session = sessionFactory.openSession();
		Order order=(Order)session.createQuery("from Order o where o.id=:i").setParameter("i", orderId).uniqueResult();
		session.close();
		return order;
	}
	//通过订单编号查看订单详情表
	@Override
	public List<Integer> findOrderDetailByOrderId(Long orderId) {
		// 
		Session session = sessionFactory.openSession();
		List<Integer> list=session.createQuery("select od.skuId from OrderDetail od where od.orderId=:i").setParameter("i", orderId).list();
		session.close();
		return list;
	}
	//通过SKU_ID找到item_id
	@Override
	public List<Integer> findItemId(List list) {
		Session session = sessionFactory.openSession();
		List<Integer> list1=new ArrayList<Integer>();
		for (int i = 0; i <list.size(); i++) {
		Integer itemId=(Integer)session.createQuery("select s.itemId from Sku s where s.id=:i").setParameter("i", list.get(i)).uniqueResult();
		list1.add(itemId);
		}
		session.close();
		return list1;
	}
	//通过item_id查找商品的名称
	@Override
	public List<String> findItemName(List list) {
		Session session = sessionFactory.openSession();
		List<String> list1=new ArrayList<String>();
		for (int i = 0; i <list.size(); i++) {
		String itemId=(String)session.createQuery("select it.name from Item it where it.id=:i").setParameter("i", list.get(i)).uniqueResult();
		list1.add(itemId);
		}
		session.close();
		return list1;
	}
	//修改订单状态为已支付（1）
	@Override
	public void updateOrderByOrderId(Long orderId) {
		Session session=sessionFactory.openSession();
		Transaction tr=session.beginTransaction();
		Order order=(Order) session.createQuery("from Order  where id=:i").setParameter("i", orderId).uniqueResult();
		order.setStatus(1);
		session.update(session.merge(order));
		tr.commit();
		session.close();
		
	}

   /**
    * 行成订单时，减少相应商品的库存
    */
	@Override
	public boolean changeRepertory(List<ShoppingCart> list) {
		Session session=sessionFactory.openSession();
		Transaction tr=session.beginTransaction();
		boolean flg=true;
		try {
			for (int i = 0; i < list.size(); i++) {
				Integer x=list.get(i).getSkuId();
				Integer y=list.get(i).getNumber();
				Sku s=(Sku)session.createQuery("from Sku s where s.id=:i").setParameter("i", x).uniqueResult();
				if(s.getRepertory()-y<0){
					flg=false;
				}
				else{
			    s.setRepertory(s.getRepertory()-y);
				session.update(s);
				}
			}
		} catch (Exception e) {
			flg=false;
		}
		if(flg){
			tr.commit();
		}
		
		session.close();
		
		return flg;
	}
/**
 * 删除订单详情
 */
	@Override
	public Integer deleteOrderDetail(Long x) {
		Session session=sessionFactory.openSession();
		Transaction tr=session.beginTransaction();
		Integer i=session.createQuery("delete from OrderDetail o where o.orderId=:i").setParameter("i", x).executeUpdate();
		tr.commit();
		session.close();
		return i;
	}


	@Override
	public List<OrderDetail> queryOrderDetailList(List<ShoppingCart> list, Long x) {
		List<OrderDetail> orderDetailList=new ArrayList<OrderDetail>();
		Session session=sessionFactory.openSession();
		for (int i = 0; i < list.size(); i++) {
			  OrderDetail od=new OrderDetail();
	          od.setOrderId(x);
	          od.setskuId(list.get(i).getSkuId());
	          od.setNumber(list.get(i).getNumber());
	          od.setPaid(list.get(i).getTotal());
		      Integer skuid=list.get(i).getSkuId();
		      double price=(double)session.createQuery("select price from Sku s where s.id=:i").setParameter("i", skuid).uniqueResult();
		      od.setPrice((float)price);
		      orderDetailList.add(od);
		}
		return orderDetailList;
	}
	/**
	 * 退货
	 * @param id
	 * @return
	 */
	@Override
	public Integer rejectedByOrderId(Long id) {
		Session session=sessionFactory.openSession();
		Integer i=session.createQuery("update Order set status=5 where id=:i").setParameter("i", id).executeUpdate();
		session.close();
		return i;
	}
	/**
	 * @author 杨冰冰
	 * @Description: 订单详情，根据status查找订单
	 * @return QiuluResult
	 * @date 2017年4月7日 上午10:23:53
	 * @param status
	 * @param session
	 * @param pageNo
	 * @return
	 */
	@Override
	public List<OrderdeResult> findOrderByStatus(Integer userId, Integer status,Integer pageSize,Integer offSet) {
		Session session=sessionFactory.openSession();
		List lis=new ArrayList<>();
		List<OrderResult> list=new ArrayList<OrderResult>();
		List<OrderdeResult> list1=new ArrayList<OrderdeResult>();
		if(status!=null){
			Query q=session.createQuery("select id from Order where userId=:us and status=:st").setParameter("us", userId).setParameter("st", status);
			q.setFirstResult(offSet);
			q.setMaxResults(pageSize);
			lis=q.list();
			
		}else{
			Query q=session.createQuery("select id from Order where status in (0,1,2,3,4) and userId=:us order by status asc").setParameter("us", userId);
			q.setFirstResult(offSet);
			q.setMaxResults(pageSize);
			lis=q.list();
			
		}

		for (int i = 0; i < lis.size(); i++) {
			System.out.println(lis.get(i));
			try {
				OrderdeResult result=new OrderdeResult();
				list=session.createQuery("from OrderResult o  where o.id.orderId =:o ").setParameter("o", lis.get(i)).list();
				result.setCrTime(list.get(0).getId().getCreateTime());
				result.setOrderId(list.get(0).getId().getOrderId());
				result.setReceive(list.get(0).getId().getReceiver());
				result.setListOr(list);
				list1.add(result);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			 
		}	
		
		session.close();
		return list1;
	}

	@Override
	public Integer findOrderByStatusNum(Integer userId, Integer status) {
		Session session=sessionFactory.openSession();
		Long l=0l;
		if(status!=null){
			 l=(Long) session.createQuery("select count(*) from Order o where o.userId=:u and o.id.status=:s").setParameter("u", userId).setParameter("s", status).uniqueResult();

		}else{
			l=(Long) session.createQuery("select count(*) from Order o where status in (0,1,2,3,4) and o.userId=:u").setParameter("u", userId).uniqueResult();

		}
		session.close();
		return l.intValue();
	}
	/**
	 * @author 杨冰冰
	 * @Description:根据状态为0查找总共有多少条订单，订单生成 未支付
	 * @return QiuluResult
	 * @date 2017年4月6日 下午3:05:49
	 * @param session
	 * @return
	 */
	@Override
	public Integer findOrderByStatus0Total(Integer userId) {
		Session session=sessionFactory.openSession();
		Long l=(Long) session.createQuery("select count(*) from Order o  where o.userId=:u and o.status=0").setParameter("u", userId).uniqueResult();
		session.close();
		return l.intValue();
	}

	/**
	 * @author 杨冰冰
	 * @Description:根据状态为1查找总共有多少条订单，订单支付 未处理
	 * @return QiuluResult
	 * @date 2017年4月6日 下午3:05:49
	 * @param session
	 * @return
	 */
	@Override
	public Integer findOrderByStatus1Total(Integer userId) {
		Session session=sessionFactory.openSession();
		Long l=(Long)session.createQuery("select count(*) from Order o  where o.userId=:u and o.status=1").setParameter("u", userId).uniqueResult();
		session.close();
		return l.intValue();
	}
	/**
	 * @author 杨冰冰
	 * @Description:根据状态为2查找总共有多少条订单，订单支付 送货中
	 * @return QiuluResult
	 * @date 2017年4月6日 下午3:05:49
	 * @param session
	 * @return
	 */
	@Override
	public Integer findOrderByStatus2Total(Integer userId) {
		Session session=sessionFactory.openSession();
		Long l=(Long)session.createQuery("select count(*) from Order o  where o.userId=:u and o.status=2").setParameter("u", userId).uniqueResult();
		session.close();
		return l.intValue();
	}
	/**
	 * @author 杨冰冰
	 * @Description:根据状态为3查找总共有多少条订单，订单完成
	 * @return QiuluResult
	 * @date 2017年4月6日 下午3:05:49
	 * @param session
	 * @return
	 */
	@Override
	public Integer findOrderByStatus3Total(Integer userId) {
		Session session=sessionFactory.openSession();
		Long l=(Long)session.createQuery("select count(*) from Order o  where o.userId=:u and o.status=3").setParameter("u", userId).uniqueResult();
		session.close();
		return l.intValue();
	}
	/**
	 * @author 杨冰冰
	 * @Description:根据状态为4查找总共有多少条订单，订单取消
	 * @return QiuluResult
	 * @date 2017年4月6日 下午3:05:49
	 * @param session
	 * @return
	 */
	@Override
	public Integer findOrderByStatus4Total(Integer userId) {
		Session session=sessionFactory.openSession();
		Long l=(Long)session.createQuery("select count(*)from Order o  where o.userId=:u and o.status=4").setParameter("u", userId).uniqueResult();
		session.close();
		return l.intValue();
	}

	@Override
	public List<OrderDetail> findOrderDetailByStatus(Integer userId, Integer status) {
		Session session=sessionFactory.openSession();
		List<OrderDetail> list=session.createQuery("from OrderDetail where orderId=(select id from Order where userId=:u and status=:s)").setParameter("u", userId).setParameter("s", status).list();
		session.close();
		return list;
	}
	
	/**
	 * @author 杨冰冰
	 * @Description: 查找总共有多少订单
	 * @return QiuluResult
	 * @date 2017年4月6日 下午2:38:11
	 * @param session
	 * @return
	 */
	@Override
	public Integer findOrderNumByUserId(Integer userId) {
		Session session=sessionFactory.openSession();
		Long l=(Long) session.createQuery("select count(*) from Order where userId=:u").setParameter("u", userId).uniqueResult();
		session.close();
		return l.intValue();
		
	}
	/**
	 * @author 杨冰冰
	 * @Description: 查找总共有多少订单
	 * @return QiuluResult
	 * @date 2017年4月6日 下午2:38:11
	 * @param session
	 * @return
	 */
	@Override
	public Integer findOrderByStatusAll(Integer userId) {
		Session session=sessionFactory.openSession();
		Long l=(Long) session.createQuery("select count(*) from Order where status in (0,1,2,3,4) and userId=:u").setParameter("u", userId).uniqueResult();
		session.close();
		return l.intValue();
	}

	/**
	 * @author 杨冰冰
	 * @Description: 根据id查看订单详情
	 * @return QiuluResult
	 * @date 2017年4月11日 上午10:44:12
	 * @param id
	 * @return
	 */
	@Override
	public List<OrderResult> findOrderResult(Long orderId) {
		Session session=sessionFactory.openSession();
		List<OrderResult> order=(List<OrderResult>) session.createQuery("from OrderResult o where o.id.orderId=:o").setParameter("o", orderId).list();
		session.close();
		return order;
	}

	@Override
	public List<AbandonOrder> queryAbandonOrder(Integer userId) {
		List<AbandonOrder> abandonOrders=new ArrayList<AbandonOrder>();
		Session session=sessionFactory.openSession();
		@SuppressWarnings("unchecked")
	List<Long> odList=(List<Long>)session.createQuery("select od.id from Order od where od.userId=:m and od.status=5").setParameter("m", userId).list();
		for (int i = 0; i < odList.size(); i++) {
			AbandonOrder abandonOrder=new AbandonOrder();
			abandonOrder.setOrderId(odList.get(i));
			List<OrderResult> order=(List<OrderResult>) session.createQuery("from OrderResult o where o.id.orderId=:n").setParameter("n",odList.get(i)).list();
			abandonOrder.setList(order);
			abandonOrder.setCreateTime(order.get(0).getId().getCreateTime());
			abandonOrders.add(abandonOrder);
		}
		session.close();
		return abandonOrders;
	}

	
	//修改订单为已评价状态
	@Override
	public boolean changeOrderType(Long orderId,Integer skuId) {
		boolean flg=true;
		Session session=sessionFactory.openSession();
		Transaction tr=session.beginTransaction();
			session.createQuery("update OrderDetail o set o.type=1 where o.orderId=:i and o.skuId=:m").setParameter("i", orderId).setParameter("m", skuId).executeUpdate();
		tr.commit();
		session.close();
		return  flg;
	}

	@Override
	public List<OrderDetail> queryOrderDetailByOrderId(Long orderId) {
		Session session=sessionFactory.openSession();
		List<OrderDetail> order=(List<OrderDetail>) session.createQuery("from OrderDetail o where o.orderId=:o").setParameter("o", orderId).list();
		session.close();
		return order;
	}

	@Override
	public void saveOrderDetailAgain(OrderDetail od) {
		Session session=sessionFactory.openSession();
		Transaction tr=session.beginTransaction();
		session.save(od);
		tr.commit();
		session.close();
	}

	
	


	
	
	
	
}



