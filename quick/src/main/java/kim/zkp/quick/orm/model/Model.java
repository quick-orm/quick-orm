package kim.zkp.quick.orm.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import kim.zkp.quick.orm.session.Session;
/**
 * class       :  Model
 * @author     :  zhukaipeng
 * @version    :  1.0  
 * description :  提供对象操作数据库的API，po继承此类，即可在po上通过简便的方式操作数据库
 * @see        :  @param <T>*
 */
@SuppressWarnings("unchecked")
public abstract class Model<T> extends ConditionSetting<T> {
	
	private String tableName;
	private String select;
	private String where;
	private List<String> orderByAsc;
	private List<String> orderByDesc;
	private Map<String, Object> pk;
	private Map<String, Object> insert;
	private Map<String, Object> modif;
	
	protected Session session;
	
	public Model(){
		session = Session.getDefaultSession();
	}
	/**
	 * method name   : setSession 
	 * description   : 设置session，Model通过此数据源操作数据库，若不设置，则默认使用jdbc.setting配置的数据源
	 * @return       : void
	 * @param        : @param session
	 * modified      : zhukaipeng ,  2017年9月15日
	 * @see          : *
	 */
	public void setSession(Session session){
		this.session = session;
	}
	/**
	 * method name   : setSession 
	 * description   : 设置session，Model通过此数据源操作数据库，若不设置，则默认使用jdbc.setting配置的数据源
	 * @return       : void
	 * @param        : @param jdbcName
	 * modified      : zhukaipeng ,  2017年9月15日
	 * @see          : *
	 */
	public void setSession(String jdbcName){
		this.session = Session.getSession(jdbcName);
	}
	/**
	 * method name   : tableName 
	 * description   : 获取表名
	 * @return       : String
	 * @param        : @return
	 * modified      : zhukaipeng ,  2017年9月15日
	 * @see          : *
	 */
	public String tableName() {
		return tableName;
	}
	/**
	 * method name   : tableName 
	 * description   : 设置表名
	 * @return       : T
	 * @param        : @param tableName
	 * @param        : @return
	 * modified      : zhukaipeng ,  2017年9月15日
	 * @see          : *
	 */
	public T tableName(String tableName) {
		this.tableName = tableName;
		return (T) this;
	}
	/**
	 * method name   : where 
	 * description   : 获取查询条件
	 * @return       : String
	 * @param        : @return
	 * modified      : zhukaipeng ,  2017年9月15日
	 * @see          : *
	 */
	public String where() {
		return where;
	}
	/**
	 * method name   : where 
	 * description   : 设置查询条件，格式为 "(c1=v1 and c2=v2) or (c3 like v3)"
	 * @return       : T
	 * @param        : @param where
	 * @param        : @return
	 * modified      : zhukaipeng ,  2017年9月15日
	 * @see          : *
	 */
	public T where(String where) {
		this.where = where;
		return (T) this;
	}
	/**
	 * method name   : orderByAsc 
	 * description   : 设置升序排序字段
	 * @return       : T
	 * @param        : @param orderByAscs
	 * @param        : @return
	 * modified      : zhukaipeng ,  2017年9月15日
	 * @see          : *
	 */
	public T orderByAsc(String...orderByAscs) {
		if (this.orderByAsc == null) {
			this.orderByAsc = new ArrayList<>();
		}
		this.orderByAsc.addAll(Arrays.asList(orderByAscs));
		return (T) this;
	}
	public List<String> getOrderByAsc(){
		return orderByAsc;
	}
	/**
	 * method name   : orderByDesc 
	 * description   : 设置降序排序字段
	 * @return       : T
	 * @param        : @param orderByDescs
	 * @param        : @return
	 * modified      : zhukaipeng ,  2017年9月15日
	 * @see          : *
	 */
	public T orderByDesc(String...orderByDescs) {
		if (this.orderByDesc == null) {
			this.orderByDesc = new ArrayList<>();
		}
		this.orderByDesc.addAll(Arrays.asList(orderByDescs));
		return (T) this;
	}
	
	public List<String> getOrderByDesc(){
		return orderByDesc;
	}
	
	/**
	 * method name   : select 
	 * description   : 设置select参数，格式为 "id,userName,age"
	 * @return       : T
	 * @param        : @param select
	 * @param        : @return
	 * modified      : zhukaipeng ,  2017年9月15日
	 * @see          : *
	 */
	public T select(String select) {
		this.select = select;
		return (T) this;
	}
	
	public String getSelect(){
		return select;
	}
	
	public Map<String, Object> getPk() {
		return pk;
	}
	/**
	 * method name   : pk 
	 * description   : 设置更新条件-此方法设置的值只在更新方法生效
	 * @return       : T
	 * @param        : @param column
	 * @param        : @param value
	 * @param        : @return
	 * modified      : zhukaipeng ,  2017年9月15日
	 * @see          : *
	 */
	public T pk(String column, Object value) {
		if (pk == null)
			pk = createConditionMap();
		pk.put(column, value);
		return (T) this;
	}
	/**
	 * method name   : pk 
	 * description   : 设置一组更新条件-此方法设置的值只在更新方法生效
	 * @return       : T
	 * @param        : @param pkAll
	 * @param        : @return
	 * modified      : zhukaipeng ,  2017年9月15日
	 * @see          : *
	 */
	public T pk(Map<String, Object> pkAll) {
		if (pk == null)
			pk = createConditionMap();
		pk.putAll(pkAll);
		return (T) this;
	}

	public Map<String, Object> getInsert() {
		return insert;
	}
	/**
	 * method name   : insert 
	 * description   : 设置插入的值
	 * @return       : T
	 * @param        : @param column
	 * @param        : @param value
	 * @param        : @return
	 * modified      : zhukaipeng ,  2017年9月15日
	 * @see          : *
	 */
	public T insert(String column, Object value) {
		if (insert == null)
			insert = createConditionMap();
		insert.put(column, value);
		return (T) this;
	}
	/**
	 * method name   : insert 
	 * description   : 设置一组插入的值
	 * @return       : T
	 * @param        : @param insertAll
	 * @param        : @return
	 * modified      : zhukaipeng ,  2017年9月15日
	 * @see          : *
	 */
	public T insert(Map<String, Object> insertAll) {
		if (insert == null)
			insert = createConditionMap();
		insert.putAll(insertAll);
		return (T) this;
	}
	public Map<String, Object> getModif() {
		return modif;
	}
	/**
	 * method name   : modif 
	 * description   : 设置更新的值
	 * @return       : T
	 * @param        : @param column
	 * @param        : @param value
	 * @param        : @return
	 * modified      : zhukaipeng ,  2017年9月15日
	 * @see          : *
	 */
	public T modif(String column, Object value) {
		if (modif == null)
			modif = createConditionMap();
		modif.put(column, value);
		return (T) this;
	}
	/**
	 * method name   : modif 
	 * description   : 设置一组更新的值
	 * @return       : T
	 * @param        : @param modifAll
	 * @param        : @return
	 * modified      : zhukaipeng ,  2017年9月15日
	 * @see          : *
	 */
	public T modif(Map<String, Object> modifAll) {
		if (modif == null)
			modif = createConditionMap();
		modif.putAll(modifAll);
		return (T) this;
	}
	
	/**
	 * method name   : save 
	 * description   : 保存单列至数据库
	 * @return       : int
	 * @param        : @param column
	 * @param        : @param value
	 * @param        : @return
	 * modified      : zhukaipeng ,  2017年9月15日
	 * @see          : *
	 */
	public int save(String column, Object value) {
		insert(column, value);
		return save();
	}
	/**
	 * method name   : save 
	 * description   : 保存多列至数据库
	 * @return       : int
	 * @param        : @param insertAll
	 * @param        : @return
	 * modified      : zhukaipeng ,  2017年9月15日
	 * @see          : *
	 */
	public int save(Map<String,Object> insertAll) {
		insert(insertAll);
		return save();
	}
	/**
	 * method name   : delete 
	 * description   : 设置删除条件且删除数据
	 * @return       : int
	 * @param        : @param column
	 * @param        : @param value
	 * @param        : @return
	 * modified      : zhukaipeng ,  2017年9月15日
	 * @see          : *
	 */
	public int delete(String column, Object value) {
		this.eq(column, value);
		return delete();
	}
	/**
	 * method name   : delete 
	 * description   : 设置删除条件且删除数据
	 * @return       : int
	 * @param        : @param condition
	 * @param        : @return
	 * modified      : zhukaipeng ,  2017年9月15日
	 * @see          : *
	 */
	public int delete(Map<String,Object> condition) {
		this.eq(condition);
		return delete();
	}
	/**
	 * method name   : update 
	 * description   : 更新单列
	 * @return       : int
	 * @param        : @param column
	 * @param        : @param value
	 * @param        : @return
	 * modified      : zhukaipeng ,  2017年9月15日
	 * @see          : *
	 */
	public int update(String column, Object value) {
		this.modif(column, value);
		return update();
	}
	/**
	 * method name   : update 
	 * description   : 更新多列
	 * @return       : int
	 * @param        : @param modifAll
	 * @param        : @return
	 * modified      : zhukaipeng ,  2017年9月15日
	 * @see          : *
	 */
	public int update(Map<String,Object> modifAll) {
		this.modif(modifAll);
		return update();
	}
	/**
	 * method name   : get 
	 * description   : 设置查询条件查询数据
	 * @return       : T
	 * @param        : @param column
	 * @param        : @param value
	 * @param        : @return
	 * modified      : zhukaipeng ,  2017年9月15日
	 * @see          : *
	 */
	public T get(String column, Object value) {
		this.eq(column, value);
		return get();
	}
	/**
	 * method name   : get 
	 * description   : 设置查询条件查询数据
	 * @return       : T
	 * @param        : @param eqAll
	 * @param        : @return
	 * modified      : zhukaipeng ,  2017年9月15日
	 * @see          : *
	 */
	public T get(Map<String,Object> eqAll) {
		this.eq(eqAll);
		return get();
	}
	/**
	 * method name   : list 
	 * description   : 设置查询条件查询列表
	 * @return       : List<T>
	 * @param        : @param column
	 * @param        : @param value
	 * @param        : @return
	 * modified      : zhukaipeng ,  2017年9月15日
	 * @see          : *
	 */
	public List<T> list(String column, Object value) {
		this.eq(column, value);
		return list();
	}
	/**
	 * method name   : list 
	 * description   : 设置查询条件查询列表
	 * @return       : List<T>
	 * @param        : @param eqAll
	 * @param        : @return
	 * modified      : zhukaipeng ,  2017年9月15日
	 * @see          : *
	 */
	public List<T> list(Map<String,Object> eqAll) {
		this.eq(eqAll);
		return list();
	}
	/**
	 * method name   : save 
	 * description   : 保存
	 * @return       : int
	 * @param        : @return
	 * modified      : zhukaipeng ,  2017年9月15日
	 * @see          : *
	 */
	public int save() {
		return session.save(this);
	}
	/**
	 * method name   : delete 
	 * description   : 删除
	 * @return       : int
	 * @param        : @return
	 * modified      : zhukaipeng ,  2017年9月15日
	 * @see          : *
	 */
	public int delete() {
		return session.delete(this);
	}
	/**
	 * method name   : update 
	 * description   : 更新
	 * @return       : int
	 * @param        : @return
	 * modified      : zhukaipeng ,  2017年9月15日
	 * @see          : *
	 */
	public int update() {
		return session.update(this);
	}
	/**
	 * method name   : get 
	 * description   : 查询
	 * @return       : T
	 * @param        : @return
	 * modified      : zhukaipeng ,  2017年9月15日
	 * @see          : *
	 */
	public T get() {
		return (T) session.get(this);
	}
	/**
	 * method name   : list 
	 * description   : 列表查询
	 * @return       : List<T>
	 * @param        : @return
	 * modified      : zhukaipeng ,  2017年9月15日
	 * @see          : *
	 */
	public List<T> list() {
		return (List<T>) session.list(this);
	}
	/**
	 * method name   : get 
	 * description   : 执行sql查询
	 * @return       : Object
	 * @param        : @param sql sql
	 * @param        : @param params 参数
	 * @param        : @param clzz 返回值类型
	 * @param        : @return
	 * modified      : zhukaipeng ,  2017年9月15日
	 * @see          : *
	 */
	public Object get(String sql, List<Object> params,Class<?> clzz) {
		return session.get(sql, params, clzz);
	}
	/**
	 * method name   : list 
	 * description   : 执行sql查询列表
	 * @return       : List<Object>
	 * @param        : @param sql sql
	 * @param        : @param params 参数
	 * @param        : @param clzz 返回值类型
	 * @param        : @return
	 * modified      : zhukaipeng ,  2017年9月15日
	 * @see          : *
	 */
	public List<Object> list(String sql, List<Object> params,Class<?> clzz) {
		return session.list(sql, params, clzz);
	}
	/**
	 * method name   : get 
	 * description   : 执行sql查询
	 * @return       : T
	 * @param        : @param sql sql
	 * @param        : @param params 参数
	 * @param        : @return
	 * modified      : zhukaipeng ,  2017年9月15日
	 * @see          : *
	 */
	public T get(String sql, List<Object> params) {
		return (T) session.get(sql, params, this.getClass());
	}
	/**
	 * method name   : list 
	 * description   : 执行sql查询列表
	 * @return       : List<T>
	 * @param        : @param sql sql
	 * @param        : @param params 返回值类型
	 * @param        : @return
	 * modified      : zhukaipeng ,  2017年9月15日
	 * @see          : *
	 */
	public List<T> list(String sql, List<Object> params) {
		return (List<T>) session.list(sql, params, this.getClass());
	}
	/**
	 * method name   : page 
	 * description   : 分页查询
	 * @return       : Page<T>
	 * @param        : @param pageNum 当前页
	 * @param        : @param pageSize 每页条数
	 * @param        : @return
	 * modified      : zhukaipeng ,  2017年9月15日
	 * @see          : *
	 */
	public Page<T> page(Integer pageNum,Integer pageSize){
		return (Page<T>) this.page(pageNum, pageSize, this.getClass());
	}
	/**
	 * method name   : page 
	 * description   : 分页查询
	 * @return       : Page<Object>
	 * @param        : @param pageNum 当前页
	 * @param        : @param pageSize 每页条数
	 * @param        : @param clzz 返回值类型
	 * @param        : @return
	 * modified      : zhukaipeng ,  2017年9月15日
	 * @see          : *
	 */
	public Page<Object> page(Integer pageNum,Integer pageSize,Class<?> clzz){
		Page.page(pageNum, pageSize);
		return session.page(this,clzz);
	}
	/**
	 * method name   : page 
	 * description   : 执行SQL分页查询
	 * @return       : Page<Object>
	 * @param        : @param pageNum 当前页
	 * @param        : @param pageSize 每页条数
	 * @param        : @param countSql 统计的SQL
	 * @param        : @param listSql 查询的SQL
	 * @param        : @param params 参数
	 * @param        : @param clzz 返回值类型
	 * @param        : @return
	 * modified      : zhukaipeng ,  2017年9月15日
	 * @see          : *
	 */
	public Page<Object> page(Integer pageNum,Integer pageSize,String countSql,String listSql, List<Object> params, Class<?> clzz){
		Page.page(pageNum, pageSize);
		return session.page(countSql, listSql, params, clzz);
	}
	/**
	 * method name   : ftPage 
	 * description   : 异步分页查询
	 * @return       : Future<Page<Object>>
	 * @param        : @param pageNum 当前页
	 * @param        : @param pageSize 每页条数
	 * @param        : @return
	 * modified      : zhukaipeng ,  2017年9月15日
	 * @see          : *
	 */
	public Future<Page<Object>> ftPage(Integer pageNum,Integer pageSize){
		return this.ftPage(pageNum, pageSize, this.getClass());
	}
	/**
	 * method name   : ftPage 
	 * description   : 异步分页查询
	 * @return       : Future<Page<Object>>
	 * @param        : @param pageNum 当前页
	 * @param        : @param pageSize 每页条数
	 * @param        : @param clzz 返回值类型
	 * @param        : @return
	 * modified      : zhukaipeng ,  2017年9月15日
	 * @see          : *
	 */
	public Future<Page<Object>> ftPage(Integer pageNum,Integer pageSize,Class<?> clzz){
		Page.page(pageNum, pageSize);
		return session.ftPage(this, clzz);
	}
	/**
	 * method name   : ftPage 
	 * description   : 异步分页查询
	 * @return       : Future<Page<Object>>
	 * @param        : @param pageNum 当前页
	 * @param        : @param pageSize 每页条数
	 * @param        : @param countSql 统计的SQL
	 * @param        : @param listSql 查询的SQL
	 * @param        : @param params 参数
	 * @param        : @param clzz 返回值类型
	 * @param        : @return
	 * modified      : zhukaipeng ,  2017年9月15日
	 * @see          : *
	 */
	public Future<Page<Object>> ftPage(Integer pageNum,Integer pageSize,String countSql,String listSql, List<Object> params, Class<?> clzz){
		Page.page(pageNum, pageSize);
		return session.ftPage(countSql, listSql, params, clzz);
	}
	/**
	 * method name   : ftSave 
	 * description   : 异步保存
	 * @return       : Future<Integer>
	 * @param        : @return
	 * modified      : zhukaipeng ,  2017年9月15日
	 * @see          : *
	 */
	public Future<Integer> ftSave() {
		return session.ftSave(this);
	}
	/**
	 * method name   : ftSave 
	 * description   : 异步保存单列至数据库
	 * @return       : Future<Integer>
	 * @param        : @param column
	 * @param        : @param value
	 * @param        : @return
	 * modified      : zhukaipeng ,  2017年9月15日
	 * @see          : *
	 */
	public Future<Integer> ftSave(String column,Object value) {
		insert(column, value);
		return ftSave();
	}
	/**
	 * method name   : ftSave 
	 * description   : 保存多列至数据库
	 * @return       : Future<Integer>
	 * @param        : @param insertAll
	 * @param        : @return
	 * modified      : zhukaipeng ,  2017年9月15日
	 * @see          : *
	 */
	public Future<Integer> ftSave(Map<String,Object> insertAll) {
		insert(insertAll);
		return ftSave();
	}
	/**
	 * method name   : ftUpdate 
	 * description   : 异步更新
	 * @return       : Future<Integer>
	 * @param        : @return
	 * modified      : zhukaipeng ,  2017年9月15日
	 * @see          : *
	 */
	public Future<Integer> ftUpdate() {
		return session.ftUpdate(this);
	}
	/**
	 * method name   : ftUpdate 
	 * description   : 异步更新单列 
	 * @return       : Future<Integer>
	 * @param        : @param column
	 * @param        : @param value
	 * @param        : @return
	 * modified      : zhukaipeng ,  2017年9月15日
	 * @see          : *
	 */
	public Future<Integer> ftUpdate(String column,Object value) {
		modif(column, value);
		return ftUpdate();
	}
	/**
	 * method name   : ftUpdate 
	 * description   : 异步更新多列
	 * @return       : Future<Integer>
	 * @param        : @param insertAll
	 * @param        : @return
	 * modified      : zhukaipeng ,  2017年9月15日
	 * @see          : *
	 */
	public Future<Integer> ftUpdate(Map<String,Object> insertAll) {
		modif(insertAll);
		return ftUpdate();
	}
	/**
	 * method name   : ftGet 
	 * description   : 异步查询
	 * @return       : Future<T>
	 * @param        : @return
	 * modified      : zhukaipeng ,  2017年9月15日
	 * @see          : *
	 */
	public Future<T> ftGet() {
		return (Future<T>) session.get(this);
	}
	/**
	 * method name   : ftGet 
	 * description   : 根据条件异步查询
	 * @return       : Future<T>
	 * @param        : @param column
	 * @param        : @param value
	 * @param        : @return
	 * modified      : zhukaipeng ,  2017年9月15日
	 * @see          : *
	 */
	public Future<T> ftGet(String column,Object value) {
		eq(column, value);
		return ftGet();
	}
	/**
	 * method name   : ftGet 
	 * description   : 根据多个条件异步查询
	 * @return       : Future<T>
	 * @param        : @param eqAll
	 * @param        : @return
	 * modified      : zhukaipeng ,  2017年9月15日
	 * @see          : *
	 */
	public Future<T> ftGet(Map<String,Object> eqAll) {
		eq(eqAll);
		return ftGet();
	}
	/**
	 * method name   : ftList 
	 * description   : 异步查询列表
	 * @return       : Future<List<Object>>
	 * @param        : @return
	 * modified      : zhukaipeng ,  2017年9月15日
	 * @see          : *
	 */
	public Future<List<Object>> ftList() {
		return session.ftList(this);
	}
	/**
	 * method name   : ftList 
	 * description   : 根据条件异步查询列表
	 * @return       : Future<List<Object>>
	 * @param        : @param column
	 * @param        : @param value
	 * @param        : @return
	 * modified      : zhukaipeng ,  2017年9月15日
	 * @see          : *
	 */
	public Future<List<Object>> ftList(String column,Object value) {
		eq(column, value);
		return ftList();
	}
	/**
	 * method name   : ftList 
	 * description   : 根据多个条件异步查询列表
	 * @return       : Future<List<Object>>
	 * @param        : @param eqAll
	 * @param        : @return
	 * modified      : zhukaipeng ,  2017年9月15日
	 * @see          : *
	 */
	public Future<List<Object>> ftList(Map<String,Object> eqAll) {
		eq(eqAll);
		return ftList();
	}
	/**
	 * method name   : ftGet 
	 * description   : 异步SQL查询
	 * @return       : Future<Object>
	 * @param        : @param sql
	 * @param        : @param params
	 * @param        : @param clzz 返回值类型
	 * @param        : @return
	 * modified      : zhukaipeng ,  2017年9月15日
	 * @see          : *
	 */
	public Future<Object> ftGet(String sql, List<Object> params,Class<?> clzz) {
		return session.ftGet(sql, params, clzz);
	}
	
	/**
	 * method name   : ftList 
	 * description   : 异步执行SQL查询列表
	 * @return       : Future<List<Object>>
	 * @param        : @param sql
	 * @param        : @param params
	 * @param        : @param clzz 返回值类型
	 * @param        : @return
	 * modified      : zhukaipeng ,  2017年9月15日
	 * @see          : *
	 */
	public Future<List<Object>> ftList(String sql, List<Object> params,Class<?> clzz) {
		return session.ftList(sql, params, clzz);
	}
	/**
	 * method name   : ftGet 
	 * description   : 异步执行SQL查询
	 * @return       : Future<T>
	 * @param        : @param sql
	 * @param        : @param params
	 * @param        : @return
	 * modified      : zhukaipeng ,  2017年9月15日
	 * @see          : *
	 */
	public Future<T> ftGet(String sql,List<Object> params) {
		return (Future<T>) session.ftGet(sql, params, this.getClass());
	}
	
}
