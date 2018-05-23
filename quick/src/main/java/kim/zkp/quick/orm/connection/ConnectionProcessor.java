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

package kim.zkp.quick.orm.connection;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.LogFactory;

import kim.zkp.quick.orm.annotation.Join;
import kim.zkp.quick.orm.cache.ClassCache;
import kim.zkp.quick.orm.exception.ConnectionException;
import kim.zkp.quick.orm.exception.ConnectionExceptionCount;
import kim.zkp.quick.orm.exception.ExecuteSqlException;
import kim.zkp.quick.orm.exception.TransactionException;
import kim.zkp.quick.orm.model.Schema;
import kim.zkp.quick.orm.sql.SqlInfo;
import kim.zkp.quick.orm.sql.convert.FieldConvertProcessor;

public class ConnectionProcessor {
	private static final Log log = LogFactory.get();
	private DataSource dataSource;
	private JDBCConfig jdbcConfig;
	
	public ConnectionProcessor(JDBCConfig jdbcConfig) {
		super();
		this.jdbcConfig = jdbcConfig;
		this.dataSource = new QuickDataSource(jdbcConfig);
	}
	
	public DataSource getDataSource(){
		return dataSource;
	}
	
	public Connection getConnection(){
		try {
//			Connection conn = SingleThreadConnectionHolder.getConnection(dataSource);
//			if (conn == null || conn.isClosed()) {
//				conn = dataSource.getConnection();
//				SingleThreadConnectionHolder.setConnection(dataSource, conn);
//			}
//			return conn;
			
			return dataSource.getConnection();
		} catch (SQLException e) {
			throw new ConnectionException("Get db connection error",e);
		}
	}
	
	public void setAutoCommit(Connection conn,boolean commit){
		try {
			conn.setAutoCommit(commit);
		} catch (SQLException e) {
			ConnectionExceptionCount.add(conn);
			throw new TransactionException("Open transaction error",e);
		}
	}
	
	public void rollback(Connection conn){
		try {
			conn.rollback();
		} catch (SQLException e) {
			throw new TransactionException("Rollback transaction error",e);
		}
	}
	
	public void commit(Connection conn){
		try {
			conn.commit();
		} catch (SQLException e) {
			throw new TransactionException("Commit transaction error",e);
		}
	}
	public void close(Connection conn){
		try {
			conn.setAutoCommit(true);
			release(conn);
		} catch (SQLException e) {
			ConnectionExceptionCount.destroy(conn);//有事务的连接关闭出现异常直接废弃
			log.error(e, "Close connection error");
			try {
				conn.close();
			} catch (SQLException e1) {
				throw new TransactionException("Close transaction error",e1);
			}
			throw new TransactionException("Close transaction error",e);
		}
		
		
//		try {
//			SingleThreadConnectionHolder.removeConnection(conn);
//			conn.setAutoCommit(true);
//			conn.close();
//		} catch (SQLException e) {
//			log.error(e, "Close connection error");
//			try {
//				conn.close();
//			} catch (SQLException e1) {
//				throw new TransactionException("Close2 transaction error",e1);
//			}
//			throw new TransactionException("Close transaction error",e);
//		}
	}
	
	public int update(Connection conn, SqlInfo sqlInfo) {
		PreparedStatementWrapper stmt = null;
		try {
			stmt = createPreparedStatement(conn, sqlInfo);
			return stmt.executeUpdate();
		} catch (SQLException e) {
//			log.error(e, "execute sql error");
			throw new ExecuteSqlException(e);
		} finally {
			close(stmt);
			release(conn);
		}
	}

	public Object get(Connection conn, SqlInfo sqlInfo,Class<?> clzz) {
		List<Object> list = list(conn, sqlInfo, clzz);
		if (list == null || list.size()==0) {
			if (clzz.isAssignableFrom(Schema.class)) {
				return Schema.open();
			}
			return null;
		}
		if (list.size()==1) {
			return list.get(0);
		}
		throw new ExecuteSqlException("Query out multiple results!");
	}

	public List<Object> list(Connection conn, SqlInfo sqlInfo,Class<?> clzz) {
		PreparedStatementWrapper stmt = null;
		ResultSet rs = null;
		try {
			stmt = createPreparedStatement(conn, sqlInfo);
			rs = stmt.executeQuery();
			return parseResultSetToObject(rs,clzz,sqlInfo);
		} catch (Exception e) {
			throw new ExecuteSqlException(e);
		} finally {
			close(stmt);
			close(rs);
			release(conn);
		}
	}
	
	private PreparedStatementWrapper createPreparedStatement(Connection conn, SqlInfo sqlInfo) throws SQLException {
		if (jdbcConfig.getPrintSql()) {
			log.info("execute sql:{}", sqlInfo.getSql());
			log.info("params:{}", sqlInfo.getParam());
		}
		PreparedStatement stmt;
		try {
			stmt = conn.prepareStatement(sqlInfo.getSql());
		} catch (SQLException e) {
			ConnectionExceptionCount.add(conn);
			throw new ExecuteSqlException(e);
		}
		List<Object> params = sqlInfo.getParam();
		for (int i = 0; i < params.size(); i++) {
			try {
				stmt.setObject(i + 1, params.get(i));
			} catch (Exception e) {
				log.error(e);
				throw new ExecuteSqlException("Setting sql param error",e);
			}
		}
		return new PreparedStatementWrapper(stmt, sqlInfo,jdbcConfig.getExecuteTimeMonitor(),jdbcConfig.getMaxExecuteTime());
	}
	
	private List<Object> parseResultSetToObject(ResultSet rs,Class<?> clzz, SqlInfo sqlInfo) {
		List<Object> list = new ArrayList<>();
		try {
			ResultSetMetaData rsmd = rs.getMetaData();
			int count = rsmd.getColumnCount();
			while (rs.next()) {
				Map<String,Object> result = new HashMap<>();
				for (int i = 0; i < count; i++) {
					String columnLabel = rsmd.getColumnLabel(i + 1);
					Object value = rs.getObject(i + 1);
					result.put(columnLabel, value);
				}
				 Object o = toJavaObject(result, clzz, sqlInfo);
				 list.add(o);
			}
			return list;
		} catch (SQLException e) {
			throw new ExecuteSqlException(e);
		}
	}
	
	private Object toJavaObject(Map<String,Object> result,Class<?> clzz, SqlInfo sqlInfo){
		try {
			if (clzz.isAssignableFrom(Map.class)) {
				return result;
			}
			if (clzz.isAssignableFrom(Schema.class)) {
				Schema s = Schema.open("");
				s.setResult(result);
				return s;
			}
			if (clzz.isAssignableFrom(Short.class)) {
				if(result.keySet().size() != 1){
					throw new ExecuteSqlException("Query out result is not the "+clzz.getSimpleName());
				}
				return Short.parseShort(result.get(result.keySet().toArray()[0]).toString());
			}
			if (clzz.isAssignableFrom(Float.class)) {
				if(result.keySet().size() != 1){
					throw new ExecuteSqlException("Query out result is not the "+clzz.getSimpleName());
				}
				return Float.parseFloat(result.get(result.keySet().toArray()[0]).toString());
			}
			if (clzz.isAssignableFrom(Integer.class)) {
				if(result.keySet().size() != 1){
					throw new ExecuteSqlException("Query out result is not the "+clzz.getSimpleName());
				}
				return Integer.parseInt(result.get(result.keySet().toArray()[0]).toString());
			}
			if (clzz.isAssignableFrom(Double.class)) {
				if(result.keySet().size() != 1){
					throw new ExecuteSqlException("Query out result is not the "+clzz.getSimpleName());
				}
				return Double.parseDouble(result.get(result.keySet().toArray()[0]).toString());
			}
			if (clzz.isAssignableFrom(Long.class)) {
				if(result.keySet().size() != 1){
					throw new ExecuteSqlException("Query out result is not the "+clzz.getSimpleName());
				}
				return Long.parseLong(result.get(result.keySet().toArray()[0]).toString());
			}
			if (clzz.isAssignableFrom(BigDecimal.class)) {
				if(result.keySet().size() != 1){
					throw new ExecuteSqlException("Query out result is not the "+clzz.getSimpleName());
				}
				return new BigDecimal(result.get(result.keySet().toArray()[0]).toString());
			}
			if (clzz.isAssignableFrom(String.class)) {
				if(result.keySet().size() != 1){
					throw new ExecuteSqlException("Query out result is not the "+clzz.getSimpleName());
				}
				return result.get(result.keySet().toArray()[0]).toString();
			}
//			log.debug("result:{}",result);
			return fillResultObject(clzz, result,true);
		} catch (Exception e) {
			log.error(e, "query result convert java object error");
		}
		return null;
		
	}
	
	private Object fillResultObject(Class<?> clzz, Map<String,Object> result,boolean nextFind){
		Object o;
		try {
			o = clzz.newInstance();
		} catch (InstantiationException | IllegalAccessException e1) {
			return null;
		}
		String aliasUnderline = ClassCache.getAliasUnderline(clzz);
		List<Field> joinAnnotationFields = ClassCache.getJoin(clzz);
		List<Field> findAnnotationFields = ClassCache.getFind(clzz);
		List<Field> cachelist = ClassCache.getAllFieldByCache(clzz);
		List<Field> attrFieldList = new ArrayList<>(cachelist);
		attrFieldList.addAll(joinAnnotationFields);
		attrFieldList.addAll(findAnnotationFields);
		
		for (Field field : attrFieldList) {
			Join join = field.getAnnotation(Join.class);
			Object v = null;
			if (join != null && nextFind) {
				v = fillResultObject(field.getType(), result,false);
			}else {
				String fieldName = field.getName();
				String k = aliasUnderline + fieldName;
				v = result.get(k);
				if (v == null) {
					v = result.get(fieldName);
					if (v == null) { //某些数据库返回的字段全为大写
						v = result.get(k.toUpperCase());
						if (v == null) {
							v = result.get(fieldName.toUpperCase());
							
						}
					}
				}
//				log.debug("key:{},value:{}", k,v);
			}
			if (v != null) {
				try {
					field.setAccessible(true);
					field.set(o, FieldConvertProcessor.toJava(field.getType(),v));
				} catch (Exception e) {
					log.error(e, "db type to java type error");
				}
			}
		}
		return o;
	}
	
	private final void release(Connection x) {
		if (x != null) {
			try {
				if (x.getAutoCommit()) {
					x.close();
					SingleThreadConnectionHolder.removeConnection(dataSource);
				}
			} catch (Exception e) {//此处若获取事务状态异常，直接移除连接，防止连接占用
				SingleThreadConnectionHolder.removeConnection(dataSource);
				log.error("close connection error", e);
				try {
					x.close();
				} catch (SQLException e1) {
					log.error("close connection error", e1);
				}
			}
		}
	}

	private final void close(PreparedStatementWrapper x) {
		if (x != null) {
			try {
				x.close();
			} catch (Exception e) {
				log.error("close statement error", e);
			}
		}
	}

	private final void close(ResultSet x) {
		if (x != null) {
			try {
				x.close();
			} catch (Exception e) {
				log.error("close resultset error", e);
			}
		}
	}

}