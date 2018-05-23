/**
 * Copyright (c) 2017, ZhuKaipeng 朱开鹏 (2076528290@qq.com).

 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package kim.zkp.quick.orm.session;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.sql.DataSource;

import com.xiaoleilu.hutool.io.IORuntimeException;
import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.LogFactory;

import kim.zkp.quick.orm.connection.ConnectionProcessor;
import kim.zkp.quick.orm.connection.JDBCConfig;
import kim.zkp.quick.orm.exception.SqlBuilderException;
import kim.zkp.quick.orm.model.Page;
import kim.zkp.quick.orm.sql.SqlInfo;
import kim.zkp.quick.orm.sql.builder.SqlBuilder;
import kim.zkp.quick.orm.sql.builder.SqlBuilderProcessor;
import kim.zkp.quick.orm.table.CreateTable;
/**
 * class       :  Session
 * @author     :  zhukaipeng
 * @version    :  1.0  
 * description :  Session类似数据源，且提供了一套操作数据库的API
 * @see        :  *
 */
public class Session implements DataBaseManipulation,SqlDataBaseManipulation,FutureDataBaseManipulation,SqlFutureDataBaseManipulation,Transaction {
	private static final Log log = LogFactory.get();
	private static final Map<String,Session> sessionContainer = new HashMap<String, Session>();
	private ConnectionProcessor connectionProcessor;
	private ExecutorService futurePool;
	private SqlBuilderProcessor sqlBuilderProcessor;

	private Session(String jdbcConfigName) {
		super();
		JDBCConfig jdbcConfig = JDBCConfig.newInstance(jdbcConfigName);
		connectionProcessor = new ConnectionProcessor(jdbcConfig);
		sqlBuilderProcessor = new SqlBuilderProcessor(jdbcConfig.getDbType());
		futurePool = Executors.newFixedThreadPool(jdbcConfig.getAsyncPoolSize());
		if (jdbcConfig.getPackagePath()!=null) {
			CreateTable createTable = new CreateTable(this,sqlBuilderProcessor,jdbcConfig.getPackagePath());
			createTable.start();
		}
	}
	
	private DataSource getDataSource(){
		return connectionProcessor.getDataSource();
	}
	
	public static DataSource getDataSource(String jdbcConfigName){
		return getSession(jdbcConfigName).getDataSource();
	}
	/**
	 * method name   : getDefaultSession 
	 * description   : 获取jdbc.setting文件配置的Session
	 * @return       : Session
	 * @param        : @return
	 * modified      : zhukaipeng ,  2017年9月15日
	 * @see          : *
	 */
	public static Session getDefaultSession(){
		try {
			return getSession("jdbc.setting");
		} catch (IORuntimeException e) {
			log.warn("未配置默认数据源[jdbc.setting]");
			return null;
		}
	}
	/**
	 * method name   : getSession 
	 * description   : 获取Session
	 * @return       : Session
	 * @param        : @param jdbcName setting配置文件名
	 * @param        : @return
	 * modified      : zhukaipeng ,  2017年9月15日
	 * @see          : *
	 */
	public static Session getSession(String jdbcConfigName){
		Session session = sessionContainer.get(jdbcConfigName);
		if (session == null) {
			session = new Session(jdbcConfigName);
			sessionContainer.put(jdbcConfigName, session);
		}
		return session;
	}
	@Override
	public int save(Object o) {
		SqlInfo sqlInfo = sqlBuilderProcessor.getSql(SqlBuilder.SBType.SAVE, o);
		return connectionProcessor.update(getConnection(), sqlInfo);
	}
	@Override
	public int delete(Object o) {
		SqlInfo sqlInfo = sqlBuilderProcessor.getSql(SqlBuilder.SBType.DELETE, o);
		return connectionProcessor.update(getConnection(), sqlInfo);
	}
	
	@Override
	public int update(Object o) {
		SqlInfo sqlInfo = sqlBuilderProcessor.getSql(SqlBuilder.SBType.UPDATE, o);
		return connectionProcessor.update(getConnection(), sqlInfo);
	}
	
	@Override
	public Object get(Object o) {
		return this.get(o, o.getClass());
	}
	
	@Override
	public Object get(Object o,Class<?> clzz) {
		SqlInfo sqlInfo = sqlBuilderProcessor.getSql(SqlBuilder.SBType.GET, o);
		return connectionProcessor.get(getConnection(), sqlInfo,clzz);
	}

	@Override
	public List<Object> list(Object o) {
		return this.list(o, o.getClass());
	}
	
	@Override
	public Page<Object> page(Object o) {
		return page(o,o.getClass());
	}
	
	@Override
	public Page<Object> page(Object o,Class<?> clzz) {
		Map<String,Integer> pageInfo = Page.getPageInfo();
		if (pageInfo == null || pageInfo.get("pageNum")==null || pageInfo.get("pageSize")==null) {
			throw new SqlBuilderException("PageNum or pageSize is null");
		}
		
		SqlInfo countSqlInfo = sqlBuilderProcessor.getSql(SqlBuilder.SBType.PAGE_COUNT, o);
		Integer total = (Integer) connectionProcessor.get(getConnection(), countSqlInfo,Integer.class);
		
		if (total == 0) {
			Page<Object> page = new Page<Object>(pageInfo.get("pageNum"),pageInfo.get("pageSize"), total, new ArrayList<>());
			return page;
		}
		
		SqlInfo listSqlInfo = sqlBuilderProcessor.getSql(SqlBuilder.SBType.PAGE_LIST, o);
		List<Object> list = connectionProcessor.list(getConnection(), listSqlInfo,clzz);
		return new Page<Object>(pageInfo.get("pageNum"),pageInfo.get("pageSize"), total, list);
	}
	
	@Override
	public Page<Object> sqlPage(String countSql,String listSql, Class<?> clzz, Object ... params) {
		Map<String,Integer> pageInfo = Page.getPageInfo();
		if (pageInfo == null || pageInfo.get("pageNum")==null || pageInfo.get("pageSize")==null) {
			throw new SqlBuilderException("PageNum or pageSize is null");
		}
		List<Object> paramList = Arrays.asList(params);
		SqlInfo countSqlInfo = new SqlInfo(countSql, paramList);
		Integer total = (Integer) connectionProcessor.get(getConnection(), countSqlInfo,Integer.class);
		
		if (total == 0) {
			Page<Object> page = new Page<Object>(pageInfo.get("pageNum"),pageInfo.get("pageSize"), total, new ArrayList<Object>());
			return page;
		}
		
		SqlInfo listSqlInfo = new SqlInfo(listSql, paramList);
		List<Object> list = connectionProcessor.list(getConnection(), listSqlInfo,clzz);
		return new Page<Object>(pageInfo.get("pageNum"),pageInfo.get("pageSize"), total, list);
	}
	
	@Override
	public List<Object> list(Object o,Class<?> clzz) {
		SqlInfo sqlInfo = sqlBuilderProcessor.getSql(SqlBuilder.SBType.LIST, o);
		return connectionProcessor.list(getConnection(), sqlInfo,clzz);
	}
	
	@Override
	public int sqlSave(String sql, Object ... params) {
		List<Object> paramList = Arrays.asList(params);
		SqlInfo sqlInfo = new SqlInfo(sql, paramList);
		return connectionProcessor.update(getConnection(), sqlInfo);
	}
	
	@Override
	public int sqlDelete(String sql, Object ... params) {
		List<Object> paramList = Arrays.asList(params);
		SqlInfo sqlInfo = new SqlInfo(sql, paramList);
		return connectionProcessor.update(getConnection(), sqlInfo);
	}
	
	@Override
	public int sqlUpdate(String sql, Object ... params) {
		List<Object> paramList = Arrays.asList(params);
		SqlInfo sqlInfo = new SqlInfo(sql, paramList);
		return connectionProcessor.update(getConnection(), sqlInfo);
	}
	
	@Override
	public Object sqlGet(String sql,Class<?> clzz, Object ... params) {
		List<Object> paramList = Arrays.asList(params);
		SqlInfo sqlInfo = new SqlInfo(sql, paramList);
		return connectionProcessor.get(getConnection(), sqlInfo, clzz);
	}
	
	@Override
	public List<Object> sqlList(String sql,Class<?> clzz, Object ... params) {
		List<Object> paramList = Arrays.asList(params);
		SqlInfo sqlInfo = new SqlInfo(sql, paramList);
		return connectionProcessor.list(getConnection(), sqlInfo, clzz);
	}
	
	public Connection getConnection(){
		return connectionProcessor.getConnection();
	}
	@Override
	public void start(){
		connectionProcessor.setAutoCommit(getConnection(), false);
	}
	@Override
	public void rollback(){
		connectionProcessor.rollback(getConnection());
	}
	@Override
	public void commit(){
		connectionProcessor.commit(getConnection());
	}
	@Override
	public void close(){
		connectionProcessor.close(getConnection());
	}
	

	@Override
	public Future<Integer> ftSave(Object o) {
		return futurePool.submit(new Callable<Integer>() {
			public Integer call() throws Exception {
				return save(o);
			}
		});
	}

	@Override
	public Future<Integer> ftDelete(Object o) {
		return futurePool.submit(new Callable<Integer>() {
			public Integer call() throws Exception {
				return delete(o);
			}
		});
	}

	@Override
	public Future<Integer> ftUpdate(Object o) {
		return futurePool.submit(new Callable<Integer>() {
			public Integer call() throws Exception {
				return update(o);
			}
		});
	}

	@Override
	public Future<Object> ftGet(Object o) {
		return futurePool.submit(new Callable<Object>() {
			public Object call() throws Exception {
				return get(o);
			}
		});
	}

	@Override
	public Future<Object> ftGet(Object o, Class<?> clzz) {
		return futurePool.submit(new Callable<Object>() {
			public Object call() throws Exception {
				return get(o, clzz);
			}
		});
	}

	@Override
	public Future<List<Object>> ftList(Object o) {
		return futurePool.submit(new Callable<List<Object>>() {
			public List<Object> call() throws Exception {
				return list(o);
			}
		});
	}

	@Override
	public Future<List<Object>> ftList(Object o, Class<?> clzz) {
		return futurePool.submit(new Callable<List<Object>>() {
			public List<Object> call() throws Exception {
				return list(o, clzz);
			}
		});
	}

	@Override
	public Future<Integer> ftSqlSave(String sql, Object ... params) {
		return futurePool.submit(new Callable<Integer>() {
			public Integer call() throws Exception {
				return sqlSave(sql, params);
			}
		});
	}

	@Override
	public Future<Integer> ftSqlDelete(String sql, Object ... params) {
		return futurePool.submit(new Callable<Integer>() {
			public Integer call() throws Exception {
				return sqlUpdate(sql, params);
			}
		});
	}

	@Override
	public Future<Integer> ftSqlUpdate(String sql, Object ... params) {
		return futurePool.submit(new Callable<Integer>() {
			public Integer call() throws Exception {
				return sqlUpdate(sql, params);
			}
		});
	}

	@Override
	public Future<Object> ftSqlGet(String sql, Class<?> clzz, Object ... params) {
		return futurePool.submit(new Callable<Object>() {
			public Object call() throws Exception {
				return sqlGet(sql, clzz, params);
			}
		});
	}

	@Override
	public Future<List<Object>> ftSqlList(String sql, Class<?> clzz, Object ... params) {
		return futurePool.submit(new Callable<List<Object>>() {
			public List<Object> call() throws Exception {
				return sqlList(sql, clzz, params);
			}
		});
	}

	@Override
	public Future<Page<Object>> ftPage(Object o) {
		return futurePool.submit(new Callable<Page<Object>>() {
			public Page<Object> call() throws Exception {
				return page(o);
			}
		});
	}

	@Override
	public Future<Page<Object>> ftPage(Object o, Class<?> clzz) {
		return futurePool.submit(new Callable<Page<Object>>() {
			public Page<Object> call() throws Exception {
				return page(o,clzz);
			}
		});
	}

	@Override
	public Future<Page<Object>> ftSqlPage(String countSql, String listSql, Class<?> clzz, Object ... params) {
		return futurePool.submit(new Callable<Page<Object>>() {
			public Page<Object> call() throws Exception {
				return sqlPage(countSql, listSql, clzz, params);
			}
		});
	}
//	@Override
//	public int batchSave(List<?> list) {
//		SqlInfo sqlInfo = sqlBuilderProcessor.getSql(SqlBuilder.SBType.BATCH_SAVE, list);
//		return connectionProcessor.update(getConnection(), sqlInfo);
//	}

	
	
}