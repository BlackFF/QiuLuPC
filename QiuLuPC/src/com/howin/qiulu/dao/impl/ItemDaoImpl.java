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

import com.howin.qiulu.dao.ItemDao;
import com.howin.qiulu.pojo.Item;
import com.howin.qiulu.pojo.Sku;
@Repository("ItemDao")
public class ItemDaoImpl extends HibernateDaoSupport implements ItemDao{
	@Resource
	private SessionFactory sessionFactory;
	
	@Autowired
	public void setMySessionFactory(SessionFactory sessionFactory){  
		super.setSessionFactory(sessionFactory);
	}
	/*** 
	* Title: 根据分类查找商品
	* Description: 
	* @author 杨冰冰
	*
	* @date 2017年2月21日 上午10:50:25
	*/
	@Override
	public List<Item> findItemByCategoryId(Integer categoryId) {
		Session session=sessionFactory.openSession();
		Transaction tr=session.beginTransaction();
		List<Item> list=session.createQuery("from Item where categoryId=:i").setInteger("i", categoryId).list();
		tr.commit();
		session.close();
		return list;
	}
	/*** 
	* Title: 根据商品找sku
	* Description: 
	* @author 杨冰冰
	* @date 2017年2月21日 上午11:07:39
	*/
	@Override
	public List<Sku> findSkuByItemId(Integer itemId) {
		Session session=sessionFactory.openSession();
		Transaction tr=session.beginTransaction();
		List<Sku> list=session.createQuery("from Sku where status=0 and itemId=:i").setInteger("i", itemId).list();
		tr.commit();
		session.close();
		return list;
	}
	/*** 
	* Title: 根据卖点查找Sku
	* Description: 
	* @author 杨冰冰
	* @date 2017年2月21日 下午4:30:35
	* 
	*/
	@SuppressWarnings("unchecked")
	@Override
	public List<Sku> findSkuBySellPoint(String sellPoint,Integer pageSize,Integer offSet) {
		Session session=sessionFactory.openSession();
		 String hql="from Sku where status=0 and sellPoint like '%"+sellPoint+"%'";
		 Query q = session.createQuery(hql);
		 q.setFirstResult(offSet);
		 q.setMaxResults(pageSize);
		 List<Sku> list=q.list();
		 session.close();
		 return list;
	}
	/*** 
	* Title: 根据SkuId找ItemId，然后去找属于这个ItemsId的所有Sku
	* Description: 
	* @author 杨冰冰
	* @date 2017年2月22日 下午2:33:45
	*/
	@Override
	public List<Sku> findSkuBySkuId(Integer id) {
		Session session=sessionFactory.openSession();
		Transaction tr=session.beginTransaction();
		List<Sku> list=session.createQuery("from Sku where status=0 and itemId=(select itemId from Sku where id=:i)").setInteger("i", id).list();
		tr.commit();
		session.close();
		return list;
	}

	@Override
	public Short findItemByCategoryId2(Integer categoryId) {
		Session session=sessionFactory.openSession();
		Short s= (Short) session.createQuery("select isParent from Category where id=:i").setParameter("i", categoryId).uniqueResult();
		System.out.println(s);
		session.close();
		return s;
	}

	
	/**
	 * 查询Sku总条数
	 */
	@Override
	public Integer querySkuNumber(String sellPoint) {
		Session session=sessionFactory.openSession();
		String hql="select count(*) from Sku s where s.status=0 and s.sellPoint like '%"+sellPoint+"%'";
		Query query=session.createQuery(hql);
		Long allRow=(Long) query.uniqueResult();
		session.close();
		return allRow.intValue();
	}
	/**
	 * 根据分类id找查父节点找商品    
	 * 根据分类id找查父节点找商品(先不用)
	 */
	@Override
	public List<Item> findItemByCategory(Integer categoryId) {
		Session session=sessionFactory.openSession();
		List<Item> li=new ArrayList<Item>();
			 List list=session.createQuery("select id from Category  where parentId=:i").setParameter("i", categoryId).list();
			 for(int i=0;i<list.size();i++){
				 short s=(Short) session.createQuery("select isParent from Category where id=:i").setParameter("i", list.get(i)).uniqueResult();
				 if(s!=0){
					 list=session.createQuery("select id from Category where parentId=:i").setParameter("i", categoryId).list();
					 short sh=(short) session.createQuery("select isParent from Category where id=:i").setParameter("i", list.get(i)).uniqueResult();
					 if(s!=0){
						 list=session.createQuery("select id from Category where parentId=:i").setParameter("i", categoryId).list();
					 }else{
						 li=session.createQuery("from Item where categoryId=:c ").setParameter("c", categoryId).list();
						 System.out.println(li);
					 }
				 }else{
					 li=session.createQuery("from Item where categoryId=:c ").setParameter("c", categoryId).list();
					 System.out.println(li);
				 }
			 }
			 session.close();
	
		return li;
	}
	@Override
	public List<Sku> findSkuByTime(Integer pageSize,Integer offSet) {
		Session session=sessionFactory.openSession();
		Transaction tr=session.beginTransaction();
		Query q=session.createQuery("from Sku where status=0 order by id desc");
		q.setFirstResult(offSet);
		q.setMaxResults(pageSize);
		List<Sku> list=q.list();
		tr.commit();
		session.close();
		return list;
	}
	/**
	 * 根据skuId找item                                      
	 * @param id
	 * @return
	 */
	@Override
	public Integer findSkuByTimes() {
		Session session=sessionFactory.openSession();
		String hql="select count(*) from Sku where status=0 order by id desc";
		Query query=session.createQuery(hql);
		Long allRow=(Long) query.uniqueResult();
		session.close();
		return allRow.intValue();
	}
	
	
	//根据SkuId找到一件商品
	@Override
	public Sku querySkuById(Integer id) {
		Session session=sessionFactory.openSession();
	    Sku sku=(Sku)session.createQuery("from Sku s where s.id=:i").setParameter("i", id).uniqueResult();
	    session.close();
		return sku;
	}
	@Override
	public Item findItemBySkuId(Integer id) {
		Session session=sessionFactory.openSession();
		Item item=(Item) session.createQuery("from Item where id=(select itemId from Sku where id=:i)").setParameter("i", id).uniqueResult();
		session.close();
		return item;
	}

	@Override
	public List<Sku> queryItemByRepertory(List<Integer> list, Integer pageSize, Integer offSet) {
		Session session=sessionFactory.openSession();
		List<Sku> list1=new ArrayList<Sku>();
		List<Integer> list3=new ArrayList<Integer>();
		for (int i = 0; i < list.size(); i++) {
			List<Integer> list2=(List<Integer>) session.createQuery("select id from Item where categoryId=:c").setInteger("c", list.get(i)).list();
               list3.addAll(list2);
		}
		
		   StringBuffer hql3=new StringBuffer();
		   hql3.append("(");
	    	for(int i=0;i<list3.size();i++){
	    		hql3.append(list3.get(i).toString());
	    		hql3.append(",");
	    	}
	    	String hql2=hql3.toString().substring(0,hql3.toString().length()-1);
	    	hql2=hql2+")";
	    	System.out.println(hql2);
	    	String hql="from Sku where itemId in "+hql2+" order by salesVolume desc";
	    	System.out.println(hql);
			Query q=session.createQuery(hql);
			q.setFirstResult(offSet);
			q.setMaxResults(pageSize);
			list1=q.list();
		    session.close();
		    return list1;
	}

	
	/**
	 * 根据价格升序查找
	 * @param id
	 * @param pageNo
	 * @return
	 */
	@Override
	public List<Sku> findSkuByPrice(List<Integer> id,Integer pageSize,Integer offSet) {
		Session session=sessionFactory.openSession();
		List<Integer> list2=new ArrayList<Integer>();
		List<Sku> list=new ArrayList<Sku>();
		for(int n=0;n<id.size();n++){
			List list1=(List) session.createQuery("select id from Item where categoryId=:c").setInteger("c", id.get(n)).list();
			list2.addAll(list1);
		}
		StringBuffer hql3=new StringBuffer();
		   hql3.append("(");
	    	for(int i=0;i<list2.size();i++){
	    		hql3.append(list2.get(i).toString());
	    		hql3.append(",");
	    	}
	    	String hql2=hql3.toString().substring(0,hql3.toString().length()-1);
	    	hql2=hql2+")";
	    	System.out.println(hql2);
		String Hql="from Sku where itemId in "+hql2+" order by price asc";
		Query query=session.createQuery(Hql);
		query.setFirstResult(offSet);
		query.setMaxResults(pageSize);
		list=query.list();
		session.close();
		return list;
	}
	/**
	 * 根据价格降序查找
	 * @param id
	 * @param pageNo
	 * @return
	 */
	@Override
	public List<Sku> findSkuByPrice2(List<Integer> id,Integer pageSize,Integer offSet) {
		Session session=sessionFactory.openSession();
		List<Integer> list2=new ArrayList<Integer>();
		List<Sku> list=new ArrayList<Sku>();
		for(int n=0;n<id.size();n++){
			List list1=(List) session.createQuery("select id from Item where categoryId=:c").setInteger("c", id.get(n)).list();
			list2.addAll(list1);
		}
		
		StringBuffer hql3=new StringBuffer();
		   hql3.append("(");
	    	for(int i=0;i<list2.size();i++){
	    		hql3.append(list2.get(i).toString());
	    		hql3.append(",");
	    	}
	    	String hql2=hql3.toString().substring(0,hql3.toString().length()-1);
	    	hql2=hql2+")";
	    	System.out.println(hql2);
		String Hql="from Sku where itemId in "+hql2+" order by price desc";
		Query query=session.createQuery(Hql);
		query.setFirstResult(offSet);
		query.setMaxResults(pageSize);
		list=query.list();
		session.close();
		return list;
	}
	/**
	 * 根据价格升序查找
	 * @param id
	 * @param pageNo
	 * @return
	 */
	@Override
	public Integer findSkuByPriceNum(List<Integer> id) {
		Session session=sessionFactory.openSession();
		List<Integer> list2=new ArrayList<Integer>();
		Long l=0l;
		for (int i = 0; i <id.size(); i++) {
			List<Integer> list1=(List<Integer>)session.createQuery("select id from Item where categoryId=:c").setInteger("c", id.get(i)).list();
			list2.addAll(list1);
		}
		for(int i=0;i<list2.size();i++){
			Long ll= (Long) session.createQuery("select count(*) from Sku where itemId=:c order by price desc").setParameter("c", list2.get(i)).uniqueResult(); 
			l=l+ll;
		}
		session.close();
		return l.intValue();
	}
	/**
	 * 根据价格降序查找
	 * @param id
	 * @param pageNo
	 * @return
	 */
	@Override
	public Integer findSkuByPrice2Num(List<Integer> id) {
		Session session=sessionFactory.openSession();
		List<Integer> list2=new ArrayList<Integer>();
		Long l=0l;
		for (int i = 0; i <id.size(); i++) {
			List<Integer> list1=(List<Integer>)session.createQuery("select id from Item where categoryId=:c").setInteger("c", id.get(i)).list();
			list2.addAll(list1);
		}
		for(int i=0;i<list2.size();i++){
			Long li= (Long) session.createQuery("select count(*) from Sku where itemId=:c order by price asc").setParameter("c", list2.get(i)).uniqueResult(); 
			l=l+li;
		}
		session.close();
		return l.intValue();
	}
	/**
	 * 根据人气查找商品
	 * @param id
	 * @param pageNo
	 * @return
	 */
	@Override
	public List<Sku> findSkuByFavrites(List<Integer> id, Integer pageSize, Integer offSet) {
		Session session=sessionFactory.openSession();
		List<Integer> list=new ArrayList<Integer>();
		List<Integer> list2=new ArrayList<Integer>();
		List<Sku> li=new ArrayList<Sku>();
		for(int n=0;n<id.size();n++){
			List list1=(List) session.createQuery("select id from Item where categoryId=:c").setInteger("c", id.get(n)).list();
			list2.addAll(list1);
		}
		
		StringBuffer hql3=new StringBuffer();
		   hql3.append("(");
	    	for(int i=0;i<list2.size();i++){
	    		hql3.append(list2.get(i).toString());
	    		hql3.append(",");
	    	}
	    	String hql2=hql3.toString().substring(0,hql3.toString().length()-1);
	    	hql2=hql2+")";
	    	System.out.println(hql2);
		String Hql="select id from Sku where itemId in "+hql2+"";
		Query query=session.createQuery(Hql);
		list=query.list();
		StringBuffer hql4=new StringBuffer();
		   hql4.append("(");
	    	for(int i=0;i<list.size();i++){
	    		hql4.append(list.get(i).toString());
	    		hql4.append(",");
	    	}
	    	String hql5=hql4.toString().substring(0,hql4.toString().length()-1);
	    	hql5=hql5+")";
	    	System.out.println(hql5);
		String Hql1=" from SkuFavorites s where s.id.status=0 and s.id.id in "+hql5+"  order by s.id.count desc";
		Query q=session.createQuery(Hql1);
		q.setFirstResult(offSet);
		q.setMaxResults(pageSize);
		li= q.list();
		session.close();
		return li;
	}
	/**
	 * 根据人气查找商品
	 * @param id
	 * @param pageNo
	 * @return
	 */
	@Override
	public Integer findSkuByFavrites2(List<Integer> id) {
		Session session=sessionFactory.openSession();
		List<Integer> list=new ArrayList<Integer>();
		List<Integer> list2=new ArrayList<Integer>();
		for(int n=0;n<id.size();n++){
			List list1=(List) session.createQuery("select id from Item where categoryId=:c").setInteger("c", id.get(n)).list();
			list2.addAll(list1);
		}
		
		StringBuffer hql3=new StringBuffer();
		   hql3.append("(");
	    	for(int i=0;i<list2.size();i++){
	    		hql3.append(list2.get(i).toString());
	    		hql3.append(",");
	    	}
	    	String hql2=hql3.toString().substring(0,hql3.toString().length()-1);
	    	hql2=hql2+")";
	    	System.out.println(hql2);
		Long l=0l;
		String Hql2=" SELECT COUNT(*) from SkuFavorites s where s.id.status=0 and s.id.itemId in "+hql2+" ";
		l= (Long) session.createQuery(Hql2).uniqueResult();
		session.close();
		return l.intValue();
	}
	/**
	 * 根据区间价格和销量或者人气查找商品
	 * @param id
	 * @param type 1，销量；2，人气
	 * @param min价格最小值
	 * @param max价格最大值
	 * @param pageNo
	 * @return
	 */
	@Override
	public List<Sku> findSkuByTypeByPrice(Integer type, List<Integer> id, Integer pageSize, Integer offSet,Double min,Double max) {
		Session session=sessionFactory.openSession();
		List<Integer> list=new ArrayList<Integer>();
		List<Integer> list2=new ArrayList<Integer>();
		List<Sku> li=new ArrayList<Sku>();
		for(int n=0;n<id.size();n++){
			List list1=(List) session.createQuery("select id from Item where categoryId=:c").setInteger("c", id.get(n)).list();
			list2.addAll(list1);
		}
		
		StringBuffer hql3=new StringBuffer();
		   hql3.append("(");
	    	for(int i=0;i<list2.size();i++){
	    		hql3.append(list2.get(i).toString());
	    		hql3.append(",");
	    	}
	    	String hql2=hql3.toString().substring(0,hql3.toString().length()-1);
	    	hql2=hql2+")";
	    	System.out.println(hql2);
		String Hql="select id from Sku where itemId in "+hql2+"";
		Query query=session.createQuery(Hql);
		list=query.list();
		if(min==null){
			min=(double)0;
		}
		if(max==null){
			max=(double)20000;
		}
		if(type==0){
			Query q=session.createQuery("from Sku s where s.id in (:sql1)  and ( s.price between :min and :max) and s.status=0 ").setParameterList("sql1", list).setParameter("min", min).setParameter("max", max);
			q.setFirstResult(offSet);
			q.setMaxResults(pageSize);
			li= q.list();
		}
		if(type==3){
			Query q=session.createQuery("from Sku s where s.id in (:sql1)  and ( s.price between :min and :max) and s.status=0 ORDER BY s.price asc").setParameterList("sql1", list).setParameter("min", min).setParameter("max", max);
			q.setFirstResult(offSet);
			q.setMaxResults(pageSize);
			li= q.list();
		}
		if(type==4){
			Query q=session.createQuery("from Sku s where s.id in (:sql1)  and ( s.price between :min and :max) and s.status=0 ORDER BY s.price desc").setParameterList("sql1", list).setParameter("min", min).setParameter("max", max);
			q.setFirstResult(offSet);
			q.setMaxResults(pageSize);
			li= q.list();
		}
		if(type==1){
			Query q=session.createQuery("from Sku s where s.id in (:sql1)  and ( s.price between :min and :max) and s.status=0 ORDER BY s.salesVolume DESC").setParameterList("sql1", list).setParameter("min", min).setParameter("max", max);
			q.setFirstResult(offSet);
			q.setMaxResults(pageSize);
			li= q.list();
		}
		if(type==2){
			Query qu=session.createQuery("from SkuFavorites sfa where  sfa.id.id in (:i) and ( sfa.id.price between :min and :max) and sfa.id.status=0 order by sfa.id.count desc").setParameterList("i", list).setParameter("min", min).setParameter("max", max);
			qu.setFirstResult(offSet);
			qu.setMaxResults(pageSize);
			li= qu.list();
		}
		session.close();
		return li;
	}
	/**
	 * 根据区间价格和销量或者人气查找商品
	 * @param id
	 * @param type 1，销量；2，人气
	 * @param min价格最小值
	 * @param max价格最大值
	 * @param pageNo
	 * @return
	 */
	@Override
	public Integer findSkuByTypeByPrice2(Integer type, List<Integer> id, Double min, Double max) {
		Session session=sessionFactory.openSession();
		List<Integer> list=new ArrayList<Integer>();
		List<Integer> list2=new ArrayList<Integer>();
		for(int n=0;n<id.size();n++){
			List list1=(List) session.createQuery("select id from Item where categoryId=:c").setInteger("c", id.get(n)).list();
			list2.addAll(list1);
		}
		
		StringBuffer hql3=new StringBuffer();
		   hql3.append("(");
	    	for(int i=0;i<list2.size();i++){
	    		hql3.append(list2.get(i).toString());
	    		hql3.append(",");
	    	}
	    	String hql2=hql3.toString().substring(0,hql3.toString().length()-1);
	    	hql2=hql2+")";
	    	System.out.println(hql2);
		String Hql="select id from Sku where itemId in "+hql2+"";
		Query query=session.createQuery(Hql);
		list=query.list();
		String sql1="(";
		for (int i = 0; i < list.size(); i++) {
			sql1=sql1+list.get(i).toString();
			sql1=sql1+",";
		}
		sql1=sql1.substring(0,sql1.length()-1);
		sql1=sql1+")";
		Long l=0l;
		
		if(min==null){
			min=(double)0;
		}
		if(max==null){
			max=(double)20000;
		}
		if(type==0){
			String sql2="select count(*) from Sku s where s.id in "+sql1+" and (s.price between "+min+" and "+max+") and s.status=0";
			Query q=session.createQuery(sql2);
			l=  (long) q.uniqueResult();
		}
		if(type==3){
			String sql2="select count(*) from Sku s where s.id in "+sql1+" and (s.price between "+min+" and "+max+") and s.status=0 ORDER BY s.price DESC";
			Query q=session.createQuery(sql2);
			l=  (long) q.uniqueResult();
		}
		if(type==4){
			String sql2="select count(*) from Sku s where s.id in "+sql1+" and (s.price between "+min+" and "+max+") and s.status=0 ORDER BY s.price";
			Query q=session.createQuery(sql2);
			l=  (long) q.uniqueResult();
			
		}
		if(type==1){
			String sql2="select count(*) from Sku s where s.id in "+sql1+" and (s.price between "+min+" and "+max+") and s.status=0 ORDER BY s.salesVolume DESC";
			Query q=session.createQuery(sql2);
			l=  (long) q.uniqueResult();
			
		}
		if(type==2){
			String sql2="select count(*) from SkuFavorites sfa where sfa.id.id in "+sql1+" and (sfa.id.price between "+min+" and "+max+") and sfa.id.status=0 order by sfa.id.count desc";
			Query q=session.createQuery(sql2);
			l=  (long) q.uniqueResult();
			
		}
		
		session.close();
		return l.intValue();
	}
	
	/**
	 * 查看同级类目下 是否有其他商品
	 */
	@Override
	public List<Integer> queryItemBycategoryId(Integer categoryId, Integer id) {
	    Session session=sessionFactory.openSession();
	    @SuppressWarnings("unchecked")
		List<Integer> list =session.createQuery("select i.id from Item i where i.id!=:m and i.categoryId=:n ").setParameter("m",id ).setParameter("n", categoryId).list();
		session.close();
	    return list;
	}
	
	/**
	 * 查询相似的商品
	 */
	@Override
	public List<Sku> querySimilarSku(List<Integer> list) {
		Session session=sessionFactory.openSession();
		Query q=session.createQuery("from Sku s where s.itemId in (:list) and s.status=0");
		q.setParameterList("list", list);
	    q.setFirstResult(0);
	    q.setMaxResults(4);
	    @SuppressWarnings("unchecked")
	    List<Sku> list1= q.list();
	    session.close();
		return list1;
	}
	
	
	//销量排行榜（前六 ）
	@Override
	public List<Sku> findSkuBysalesVolume() {
		Session session=sessionFactory.openSession();
		Query q=session.createQuery("from Sku s where s.status=0 order by s.salesVolume desc");
	    q.setFirstResult(0);
	    q.setMaxResults(5);
	    @SuppressWarnings("unchecked")
	    List<Sku> list1= q.list();
	    session.close();
	    return list1;
		
	}
	
	
	//查找活动商品(首页展示)
	@Override
	public List<Sku> findOnSaleSkuOfIndex() {
		Session session=sessionFactory.openSession();
		Query q=session.createQuery("from Sku s where s.saleType in (1,2) and s.isShow=1");
	    q.setFirstResult(0);
	    q.setMaxResults(5);
	    @SuppressWarnings("unchecked")
	    List<Sku> list1= q.list();
	    session.close();
	    return list1;
		
	}
	
	//查找活动商品的总条数
	@Override
	public Integer querySkuNumberOnSale() {
		Session session=sessionFactory.openSession();
		String hql="select count(*) from Sku s where s.saleType in (1,2)";
		Query query=session.createQuery(hql);
		Long allRow=(Long) query.uniqueResult();
		Integer x=allRow.intValue();
		session.close();
		return x;
	}
	
	
	
	//查找所有活动商品（包含分页）
	@SuppressWarnings("unchecked")
	@Override
	public List<Sku> findOnSaleSku(Integer pageSize, Integer offSet) {
		Session session=sessionFactory.openSession();
		String hql="from Sku s where s.saleType in (1,2)";
		Query query=session.createQuery(hql);
		query.setFirstResult(offSet);
		query.setMaxResults(pageSize);
		List<Sku> list=query.list();
		session.close();
		return list;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	


}
