package kim.zkp.quick.orm.session;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

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
public class Session implements DataBaseManipulation,FutureDataBaseManipulation,Transaction {
	private static final Map<String,Session> sessionContainer = new HashMap<String, Session>();
	private ConnectionProcessor connectionProcessor;
	private ExecutorService futurePool;
	private SqlBuilderProcessor sqlBuilderProcessor;

	private Session(String jdbcName) {
		super();
		JDBCConfig jdbcConfig = JDBCConfig.newInstance(jdbcName);
		connectionProcessor = new ConnectionProcessor(jdbcConfig);
		sqlBuilderProcessor = new SqlBuilderProcessor(jdbcConfig.getDbType());
		futurePool = Executors.newFixedThreadPool(jdbcConfig.getAsyncPoolSize());
		if (jdbcConfig.getPackagePath()!=null) {
			CreateTable createTable = new CreateTable(this,sqlBuilderProcessor,jdbcConfig.getPackagePath());
			createTable.start();
		}
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
		return getSession("jdbc.setting");
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
	public Page<Object> page(String countSql,String listSql, List<Object> params, Class<?> clzz) {
		Map<String,Integer> pageInfo = Page.getPageInfo();
		if (pageInfo == null || pageInfo.get("pageNum")==null || pageInfo.get("pageSize")==null) {
			throw new SqlBuilderException("PageNum or pageSize is null");
		}
		
		SqlInfo countSqlInfo = new SqlInfo(countSql, params);
		Integer total = (Integer) connectionProcessor.get(getConnection(), countSqlInfo,Integer.class);
		
		if (total == 0) {
			Page<Object> page = new Page<Object>(pageInfo.get("pageNum"),pageInfo.get("pageSize"), total, new ArrayList<Object>());
			return page;
		}
		
		SqlInfo listSqlInfo = new SqlInfo(listSql, params);
		List<Object> list = connectionProcessor.list(getConnection(), listSqlInfo,clzz);
		return new Page<Object>(pageInfo.get("pageNum"),pageInfo.get("pageSize"), total, list);
	}
	
	@Override
	public List<Object> list(Object o,Class<?> clzz) {
		SqlInfo sqlInfo = sqlBuilderProcessor.getSql(SqlBuilder.SBType.LIST, o);
		return connectionProcessor.list(getConnection(), sqlInfo,clzz);
	}
	
	@Override
	public int save(String sql,List<Object> params) {
		SqlInfo sqlInfo = new SqlInfo(sql, params);
		return connectionProcessor.update(getConnection(), sqlInfo);
	}
	
	@Override
	public int delete(String sql, List<Object> params) {
		SqlInfo sqlInfo = new SqlInfo(sql, params);
		return connectionProcessor.update(getConnection(), sqlInfo);
	}
	
	@Override
	public int update(String sql,List<Object> params) {
		SqlInfo sqlInfo = new SqlInfo(sql, params);
		return connectionProcessor.update(getConnection(), sqlInfo);
	}
	
	@Override
	public Object get(String sql,List<Object> params,Class<?> clzz) {
		SqlInfo sqlInfo = new SqlInfo(sql, params);
		return connectionProcessor.get(getConnection(), sqlInfo, clzz);
	}
	
	@Override
	public List<Object> list(String sql,List<Object> params,Class<?> clzz) {
		SqlInfo sqlInfo = new SqlInfo(sql, params);
		return connectionProcessor.list(getConnection(), sqlInfo, clzz);
	}
	
	private Connection getConnection(){
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
	public Future<Integer> ftSave(String sql, List<Object> params) {
		return futurePool.submit(new Callable<Integer>() {
			public Integer call() throws Exception {
				return save(sql, params);
			}
		});
	}

	@Override
	public Future<Integer> ftDelete(String sql, List<Object> params) {
		return futurePool.submit(new Callable<Integer>() {
			public Integer call() throws Exception {
				return update(sql, params);
			}
		});
	}

	@Override
	public Future<Integer> ftUpdate(String sql, List<Object> params) {
		return futurePool.submit(new Callable<Integer>() {
			public Integer call() throws Exception {
				return update(sql, params);
			}
		});
	}

	@Override
	public Future<Object> ftGet(String sql, List<Object> params, Class<?> clzz) {
		return futurePool.submit(new Callable<Object>() {
			public Object call() throws Exception {
				return get(sql, params, clzz);
			}
		});
	}

	@Override
	public Future<List<Object>> ftList(String sql, List<Object> params, Class<?> clzz) {
		return futurePool.submit(new Callable<List<Object>>() {
			public List<Object> call() throws Exception {
				return list(sql, params, clzz);
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
	public Future<Page<Object>> ftPage(String countSql, String listSql, List<Object> params, Class<?> clzz) {
		return futurePool.submit(new Callable<Page<Object>>() {
			public Page<Object> call() throws Exception {
				return page(countSql, listSql, params, clzz);
			}
		});
	}

	
	
}
