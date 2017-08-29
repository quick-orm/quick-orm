package com.z.quick.orm.connection;

import java.lang.reflect.Field;
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
import com.z.quick.orm.cache.ClassCache;
import com.z.quick.orm.exception.ConnectionException;
import com.z.quick.orm.exception.ExecuteSqlException;
import com.z.quick.orm.exception.TransactionException;
import com.z.quick.orm.model.Schema;
import com.z.quick.orm.sql.SqlInfo;
import com.z.quick.orm.sql.convert.FieldConvertProcessor;

public class ConnectionProcessor {
	private static final Log log = LogFactory.get();

	public static Connection getConnection(DataSource dataSource){
		try {
			return SingleThreadConnectionHolder.getConnection(dataSource);
		} catch (SQLException e) {
			throw new ConnectionException("Get db connection error",e);
		}
	}
	
	public static void setAutoCommit(Connection conn,boolean commit){
		try {
			conn.setAutoCommit(commit);
		} catch (SQLException e) {
			throw new TransactionException("Open transaction error",e);
		}
	}
	
	public static void rollback(Connection conn){
		try {
			conn.rollback();
		} catch (SQLException e) {
			throw new TransactionException("Rollback transaction error",e);
		}
	}
	
	public static void commit(Connection conn){
		try {
			conn.commit();
		} catch (SQLException e) {
			throw new TransactionException("Commit transaction error",e);
		}
	}
	public static void close(Connection conn){
		try {
			conn.setAutoCommit(true);
			conn.close();
			SingleThreadConnectionHolder.removeConnection(conn);
		} catch (SQLException e) {
			throw new TransactionException("Close transaction error",e);
		}
	}
	
	public static int update(Connection conn, SqlInfo sqlInfo) {
		PreparedStatementWrapper stmt = null;
		try {
			stmt = createPreparedStatement(conn, sqlInfo);
			return stmt.executeUpdate();
		} catch (SQLException e) {
			log.error(e, "execute sql error");
			throw new ExecuteSqlException(e);
		} finally {
			close(stmt);
			release(conn);
		}
	}

	public static Object get(Connection conn, SqlInfo sqlInfo,Class<?> clzz) {
		PreparedStatementWrapper stmt = null;
		ResultSet rs = null;
		try {
			stmt = createPreparedStatement(conn, sqlInfo);
			rs = stmt.executeQuery();
			List<Object> list = parseResultSetToMap(rs,clzz);
			if (list == null || list.size()==0) {
				return null;
			}
			if (list.size()==1) {
				return list.get(0);
			}
			throw new ExecuteSqlException("Query out multiple results!");
		} catch (SQLException e) {
			log.error(e, "execute sql error");
			throw new ExecuteSqlException(e);
		} finally {
			close(stmt);
			close(rs);
			release(conn);
		}
	}

	public static List<Object> list(Connection conn, SqlInfo sqlInfo,Class<?> clzz) {
		PreparedStatementWrapper stmt = null;
		ResultSet rs = null;
		try {
			stmt = createPreparedStatement(conn, sqlInfo);
			rs = stmt.executeQuery();
			return parseResultSetToMap(rs,clzz);
		} catch (Exception e) {
			log.error(e, "execute sql error");
			throw new ExecuteSqlException(e);
		} finally {
			close(stmt);
			close(rs);
			release(conn);
		}
	}
	
	
	private static List<Object> parseResultSetToMap(ResultSet rs,Class<?> clzz) {
		List<Object> list = new ArrayList<>();
		try {
			ResultSetMetaData rsmd = rs.getMetaData();
			int count = rsmd.getColumnCount();
			while (rs.next()) {
				Map<String,Object> result = new HashMap<>();
				for (int i = 0; i < count; i++) {
					String cloumn = rsmd.getColumnName(i + 1);
					Object value = rs.getObject(i + 1);
					result.put(cloumn.toUpperCase(), value);
				}
				 Object o = toJavaObject(result, clzz);
				 list.add(o);
			}
			return list;
		} catch (SQLException e) {
			log.error(e, "parse query result error");
			throw new ExecuteSqlException(e);
		}
	}
	
	private static PreparedStatementWrapper createPreparedStatement(Connection conn, SqlInfo sqlInfo) throws SQLException {
		log.info("execute sql:{}", sqlInfo.getSql());
		log.info("params:{}", sqlInfo.getParam());
		PreparedStatement stmt;
		stmt = conn.prepareStatement(sqlInfo.getSql());
		List<Object> params = sqlInfo.getParam();
		for (int i = 0; i < params.size(); i++) {
			try {
				stmt.setObject(i + 1, params.get(i));
			} catch (Exception e) {
				log.error(e);
				throw new ExecuteSqlException("Setting sql param error",e);
			}
		}
		return new PreparedStatementWrapper(stmt, sqlInfo);
	}
	
	private static Object toJavaObject(Map<String,Object> result,Class<?> clzz){
		try {
			if (clzz.isAssignableFrom(Map.class)) {
				return result;
			}
			if (clzz.isAssignableFrom(Schema.class)) {
				Schema s = new Schema();
				s.setResult(result);
				return s;
			}
			
			Object o = clzz.newInstance();
			List<Field> list = ClassCache.getAllDeclaredFields(clzz);
			list.forEach((f)->{
				String k = f.getName().toUpperCase();
				Object v = result.get(k);
				if (v != null) {
					try {
						f.setAccessible(true);
						f.set(o, FieldConvertProcessor.toJava(f.getType(),v));
					} catch (Exception e) {
						log.error(e, "db type to java type error");
					}
				}
				
			});
			return o;
		} catch (InstantiationException | IllegalAccessException e) {
			log.error(e, "query result convert java object error");
		}
		return null;
		
	}
	
	private final static void release(Connection x) {
		if (x != null) {
			try {
				if (x.getAutoCommit()) {
					x.close();
					SingleThreadConnectionHolder.removeConnection(x);
				}
			} catch (Exception e) {
				log.error("close connection error", e);
			}
		}
	}

	private final static void close(PreparedStatementWrapper x) {
		if (x != null) {
			try {
				x.close();
			} catch (Exception e) {
				log.error("close statement error", e);
			}
		}
	}

	private final static void close(ResultSet x) {
		if (x != null) {
			try {
				x.close();
			} catch (Exception e) {
				log.error("close resultset error", e);
			}
		}
	}

}
