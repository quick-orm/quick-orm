package com.z.quick.orm.connection;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.LogFactory;
import com.z.quick.orm.sql.SqlInfo;
import com.z.quick.orm.sql.convert.FieldConvertProcessor;

public class ConnectionUtils {
	private static final Log log = LogFactory.get();

	public static int update(Connection conn, SqlInfo sqlInfo) {
		printLog(sqlInfo);

		PreparedStatement stmt = null;
		try {
			stmt = createPreparedStatement(conn, sqlInfo);
			return stmt.executeUpdate();
		} catch (SQLException e) {
			log.error(e, "执行sql出现异常");
			throw new RuntimeException(e);
		} finally {
			close(stmt);
			close(conn);
		}
	}

	public static Object get(Connection conn, SqlInfo sqlInfo,Class<?> clzz) {
		printLog(sqlInfo);
		
		PreparedStatement stmt = null;
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
			throw new RuntimeException("查询出多条结果!");
			
		} catch (SQLException e) {
			log.error(e, "执行sql出现异常");
			throw new RuntimeException(e);
		} finally {
			close(stmt);
			close(conn);
			close(rs);
		}
	}

	private static PreparedStatement createPreparedStatement(Connection conn, SqlInfo sqlInfo) throws SQLException {
		PreparedStatement stmt;
		stmt = conn.prepareStatement(sqlInfo.getSql());
		List<Object> params = sqlInfo.getParam();
		for (int i = 0; i < params.size(); i++) {
			try {
				stmt.setObject(i + 1, params.get(i));
			} catch (Exception e) {
				e.printStackTrace();
				log.error(e);
			}
		}
		return stmt;
	}
	public static List<Object> find(Connection conn, SqlInfo sqlInfo,Class<?> clzz) {
		printLog(sqlInfo);
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = createPreparedStatement(conn, sqlInfo);
			rs = stmt.executeQuery();
			return parseResultSetToMap(rs,clzz);
		} catch (SQLException e) {
			log.error(e, "执行sql出现异常");
			throw new RuntimeException(e);
		} finally {
			close(stmt);
			close(conn);
			close(rs);
		}
	}
	
	private static void printLog(SqlInfo sqlInfo){
		log.info("execute sql:{}", sqlInfo.getSql());
		log.info("params:{}", sqlInfo.getParam());
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
					result.put(cloumn, value);
				}
				 Object o = toJavaObject(result, clzz);
				 list.add(o);
			}
			return list;
		} catch (SQLException e) {
			log.error(e, "解析查询结果出现异常");
			throw new RuntimeException(e);
		}
	}
	private static Object toJavaObject(Map<String,Object> result,Class<?> clzz){
		try {
			Object o = clzz.newInstance();
			Field[] fields = clzz.getDeclaredFields();
			Arrays.asList(fields).forEach((f)->{
				String k = f.getName();
				Object v = result.get(k);
				if (v != null) {
					try {
						f.setAccessible(true);
						f.set(o, FieldConvertProcessor.toJava(f.getType(),v));
					} catch (Exception e) {
						log.error(e, "数据库类型转换为java类型出现异常");
					}
				}
				
			});
			return o;
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
		
	}

	private final static void close(Connection x) {
		if (x != null) {
			try {
				x.close();
			} catch (Exception e) {
				log.error("close connection error", e);
			}
		}
	}

	private final static void close(Statement x) {
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
