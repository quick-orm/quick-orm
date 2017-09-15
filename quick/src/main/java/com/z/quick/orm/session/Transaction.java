package com.z.quick.orm.session;
/**
 * class       :  Transaction
 * @author     :  zhukaipeng
 * @version    :  1.0  
 * description : 事务管理 
 * @see        :  *
 */
public interface Transaction {
	/**
	 * method name   : start 
	 * description   : 开启事务
	 * @return       : void
	 * @param        : 
	 * modified      : zhukaipeng ,  2017年9月15日
	 * @see          : *
	 */
	void start();
	/**
	 * method name   : rollback 
	 * description   : 回滚事务
	 * @return       : void
	 * @param        : 
	 * modified      : zhukaipeng ,  2017年9月15日
	 * @see          : *
	 */
	void rollback();
	/**
	 * method name   : commit 
	 * description   : 提交事务
	 * @return       : void
	 * @param        : 
	 * modified      : zhukaipeng ,  2017年9月15日
	 * @see          : *
	 */
	void commit();
	/**
	 * method name   : close 
	 * description   : 关闭事务
	 * @return       : void
	 * @param        : 
	 * modified      : zhukaipeng ,  2017年9月15日
	 * @see          : *
	 */
	void close();
}
