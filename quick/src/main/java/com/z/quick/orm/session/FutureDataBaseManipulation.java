package com.z.quick.orm.session;

import java.util.List;
import java.util.concurrent.Future;

import com.z.quick.orm.model.Page;
/**
 * class       :  FutureDataBaseManipulation
 * @author     :  zhukaipeng
 * @version    :  1.0  
 * description :  异步数据库操作接口
 * @see        :  *
 */
public interface FutureDataBaseManipulation {
	/**
	 * method name   : ftSave 
	 * description   : 异步保存
	 * @return       : Future<Integer>
	 * @param        : @param o
	 * @param        : @return
	 * modified      : zhukaipeng ,  2017年9月15日
	 * @see          : *
	 */
	Future<Integer> ftSave(Object o);
	/**
	 * method name   : ftDelete 
	 * description   : 异步删除
	 * @return       : Future<Integer>
	 * @param        : @param o
	 * @param        : @return
	 * modified      : zhukaipeng ,  2017年9月15日
	 * @see          : *
	 */
	Future<Integer> ftDelete(Object o);
	/**
	 * method name   : ftUpdate 
	 * description   : 异步更新
	 * @return       : Future<Integer>
	 * @param        : @param o
	 * @param        : @return
	 * modified      : zhukaipeng ,  2017年9月15日
	 * @see          : *
	 */
	Future<Integer> ftUpdate(Object o);
	/**
	 * method name   : ftGet 
	 * description   : 异步查询
	 * @return       : Future<Object>
	 * @param        : @param o
	 * @param        : @return
	 * modified      : zhukaipeng ,  2017年9月15日
	 * @see          : *
	 */
	Future<Object> ftGet(Object o);
	/**
	 * method name   : ftGet 
	 * description   : 异步查询
	 * @return       : Future<Object>
	 * @param        : @param o
	 * @param        : @param clzz 返回值类型
	 * @param        : @return
	 * modified      : zhukaipeng ,  2017年9月15日
	 * @see          : *
	 */
	Future<Object> ftGet(Object o, Class<?> clzz);
	/**
	 * method name   : ftList 
	 * description   : 异步列表查询
	 * @return       : Future<List<Object>>
	 * @param        : @param o
	 * @param        : @return
	 * modified      : zhukaipeng ,  2017年9月15日
	 * @see          : *
	 */
	Future<List<Object>> ftList(Object o);
	/**
	 * method name   : ftList 
	 * description   : 异步列表查询
	 * @return       : Future<List<Object>>
	 * @param        : @param o
	 * @param        : @param clzz 返回值类型
	 * @param        : @return
	 * modified      : zhukaipeng ,  2017年9月15日
	 * @see          : *
	 */
	Future<List<Object>> ftList(Object o, Class<?> clzz);
	/**
	 * method name   : ftSave 
	 * description   : 异步保存
	 * @return       : Future<Integer>
	 * @param        : @param sql 保存SQL
	 * @param        : @param params 参数
	 * @param        : @return
	 * modified      : zhukaipeng ,  2017年9月15日
	 * @see          : *
	 */
	Future<Integer> ftSave(String sql, List<Object> params);
	/**
	 * method name   : ftDelete 
	 * description   : 异步删除
	 * @return       : Future<Integer>
	 * @param        : @param sql 删除SQL
	 * @param        : @param params 参数
	 * @param        : @return
	 * modified      : zhukaipeng ,  2017年9月15日
	 * @see          : *
	 */
	Future<Integer> ftDelete(String sql, List<Object> params);
	/**
	 * method name   : ftUpdate 
	 * description   : 异步更新
	 * @return       : Future<Integer>
	 * @param        : @param sql 更新SQL
	 * @param        : @param params 参数
	 * @param        : @return
	 * modified      : zhukaipeng ,  2017年9月15日
	 * @see          : *
	 */
	Future<Integer> ftUpdate(String sql, List<Object> params);
	/**
	 * method name   : ftGet 
	 * description   : 异步查询
	 * @return       : Future<Object>
	 * @param        : @param sql 查询SQL
	 * @param        : @param params 参数
	 * @param        : @param clzz 返回值类型
	 * @param        : @return
	 * modified      : zhukaipeng ,  2017年9月15日
	 * @see          : *
	 */
	Future<Object> ftGet(String sql, List<Object> params, Class<?> clzz);
	/**
	 * method name   : ftList 
	 * description   : 异步列表查询
	 * @return       : Future<List<Object>>
	 * @param        : @param sql 查询SQL
	 * @param        : @param params 参数
	 * @param        : @param clzz 返回值类型
	 * @param        : @return
	 * modified      : zhukaipeng ,  2017年9月15日
	 * @see          : *
	 */
	Future<List<Object>> ftList(String sql, List<Object> params, Class<?> clzz);
	/**
	 * method name   : ftPage 
	 * description   : 异步分页查询
	 * @return       : Future<Page<Object>>
	 * @param        : @param o
	 * @param        : @return
	 * modified      : zhukaipeng ,  2017年9月15日
	 * @see          : *
	 */
	Future<Page<Object>> ftPage(Object o);
	/**
	 * method name   : ftPage 
	 * description   : 异步分页查询
	 * @return       : Future<Page<Object>>
	 * @param        : @param o
	 * @param        : @param clzz 返回值类型
	 * @param        : @return
	 * modified      : zhukaipeng ,  2017年9月15日
	 * @see          : *
	 */
	Future<Page<Object>> ftPage(Object o, Class<?> clzz);
	/**
	 * method name   : ftPage 
	 * description   : 异步分页查询
	 * @return       : Future<Page<Object>>
	 * @param        : @param countSql 统计SQL
	 * @param        : @param listSql 列表查询SQL
	 * @param        : @param params 参数
	 * @param        : @param clzz 返回值类型
	 * @param        : @return
	 * modified      : zhukaipeng ,  2017年9月15日
	 * @see          : *
	 */
	Future<Page<Object>> ftPage(String countSql,String listSql, List<Object> params, Class<?> clzz);


}