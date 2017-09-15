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

import kim.zkp.quick.orm.cache.ClassCache;
import kim.zkp.quick.orm.exception.ConnectionException;
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

	public Connection getConnection(){
		try {
			return SingleThreadConnectionHolder.getConnection(dataSource);
		} catch (SQLException e) {
			throw new ConnectionException("Get db connection error",e);
		}
	}
	
	public void setAutoCommit(Connection conn,boolean commit){
		try {
			conn.setAutoCommit(commit);
		} catch (SQLException e) {
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
			conn.close();
			SingleThreadConnectionHolder.removeConnection(conn);
		} catch (SQLException e) {
			throw new TransactionException("Close transaction error",e);
		}
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
			return parseResultSetToObject(rs,clzz);
		} catch (Exception e) {
//			log.error(e, "execute sql error");
			throw new ExecuteSqlException(e);
		} finally {
			close(stmt);
			close(rs);
			release(conn);
		}
	}
	
	private List<Object> parseResultSetToObject(ResultSet rs,Class<?> clzz) {
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
//			log.error(e, "parse query result error");
			throw new ExecuteSqlException(e);
		}
	}
	
	private PreparedStatementWrapper createPreparedStatement(Connection conn, SqlInfo sqlInfo) throws SQLException {
		if (jdbcConfig.getPrintSql()) {
			log.info("execute sql:{}", sqlInfo.getSql());
			log.info("params:{}", sqlInfo.getParam());
		}
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
		return new PreparedStatementWrapper(stmt, sqlInfo,jdbcConfig.getExecuteTimeMonitor(),jdbcConfig.getMaxExecuteTime());
	}
	
	private Object toJavaObject(Map<String,Object> result,Class<?> clzz){
		try {
			if (clzz.isAssignableFrom(Map.class)) {
				return result;
			}
			if (clzz.isAssignableFrom(Schema.class)) {
				Schema s = new Schema("");
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
	
	private final void release(Connection x) {
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
