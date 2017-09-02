package com.z.quick.orm.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import com.z.quick.orm.session.Session;
@SuppressWarnings("unchecked")
public abstract class Model<T> extends LogicOperate<T> {
	
	private String tableName;
	private String select;
	private String where;
	//TODO orderby group by
	private List<String> orderByAsc;
	private List<String> orderByDesc;
	private Map<String, Object> insert;
	private Map<String, Object> modif;
	
	private Session session;
	
	public Model(){
		session = Session.getSession();
	}

	public String tableName() {
		return tableName;
	}

	public T tableName(String tableName) {
		this.tableName = tableName;
		return (T) this;
	}

	public String where() {
		return where;
	}

	public T where(String where) {
		this.where = where;
		return (T) this;
	}
	
	public List<String> orderByAsc() {
		return orderByAsc;
	}
	
	public T orderByAsc(String...orderByAscs) {
		if (this.orderByAsc == null) {
			this.orderByAsc = new ArrayList<>();
		}
		this.orderByAsc.addAll(Arrays.asList(orderByAscs));
		return (T) this;
	}
	public List<String> orderByDesc() {
		return orderByDesc;
	}
	
	public T orderByDesc(String...orderByDescs) {
		if (this.orderByDesc == null) {
			this.orderByDesc = new ArrayList<>();
		}
		this.orderByDesc.addAll(Arrays.asList(orderByDescs));
		return (T) this;
	}

	public String select() {
		return select;
	}

	public T select(String select) {
		this.select = select;
		return (T) this;
	}

	public Map<String, Object> pk() {
		return eq();
	}

	public T pk(String column, Object value) {
		eq(column, value);
		return (T) this;
	}

	public T pk(Map<String, Object> pkAll) {
		eq(pkAll);
		return (T) this;
	}

	public Map<String, Object> insert() {
		return insert;
	}

	public T insert(String column, Object value) {
		if (insert == null)
			insert = createConditionMap();
		insert.put(column, value);
		return (T) this;
	}

	public T insert(Map<String, Object> insertAll) {
		if (insert == null)
			insert = createConditionMap();
		insert.putAll(insertAll);
		return (T) this;
	}
	public Map<String, Object> modif() {
		return modif;
	}
	
	public T modif(String column, Object value) {
		if (modif == null)
			modif = createConditionMap();
		modif.put(column, value);
		return (T) this;
	}
	
	public T modif(Map<String, Object> modifAll) {
		if (modif == null)
			modif = createConditionMap();
		modif.putAll(modifAll);
		return (T) this;
	}
	
	//数据库操作
	public int save(String column, Object value) {
		insert(column, value);
		return save();
	}
	public int save(Map<String,Object> insertAll) {
		insert(insertAll);
		return save();
	}
	
	public int delete(String column, Object value) {
		this.eq(column, value);
		return delete();
	}
	public int delete(Map<String,Object> condition) {
		this.eq(condition);
		return delete();
	}
	
	public int update(String column, Object value) {
		this.modif(column, value);
		return update();
	}
	public int update(Map<String,Object> modifAll) {
		this.modif(modifAll);
		return update();
	}
	public T get(String column, Object value) {
		this.eq(column, value);
		return get();
	}
	public T get(Map<String,Object> eqAll) {
		this.eq(eqAll);
		return get();
	}
	
	public List<T> list(String column, Object value) {
		this.eq(column, value);
		return list();
	}
	public List<T> list(Map<String,Object> eqAll) {
		this.eq(eqAll);
		return list();
	}
	
	public int save() {
		return session.save(this);
	}
	
	public int delete() {
		return session.delete(this);
	}

	public int update() {
		return session.update(this);
	}

	public T get() {
		return (T) session.get(this);
	}

	public List<T> list() {
		return (List<T>) session.list(this);
	}
	public Object get(String sql,Class<?> clzz, List<Object> params) {
		return session.get(sql, clzz, params);
	}
	
	public List<Object> list(String sql,Class<?> clzz, List<Object> params) {
		return session.list(sql, clzz, params);
	}
	public T get(String sql, List<Object> params) {
		return (T) session.get(sql, this.getClass(), params);
	}
	
	public List<T> list(String sql, List<Object> params) {
		return (List<T>) session.list(sql, this.getClass(), params);
	}
	public Page<T> page(){
		return (Page<T>) session.page(this);
	}
	public Page<T> page(Integer pageNum,Integer pageSize){
		Page.page(pageNum, pageSize);
		return (Page<T>) session.page(this);
	}
	public Page<T> page(Integer pageNum,Integer pageSize,Class<?> clzz){
		Page.page(pageNum, pageSize);
		return (Page<T>) session.page(this);
	}

	public Future<Integer> ftSave() {
		return session.ftSave(this);
	}

	public Future<Integer> ftUpdate() {
		return session.ftUpdate(this);
	}

	public Future<T> ftGet() {
		return (Future<T>) session.get(this);
	}

	public Future<List<Object>> ftList() {
		return session.ftList(this);
	}
	
	public Future<Object> ftGet(String sql,Class<?> clzz, List<Object> params) {
		return session.ftGet(sql, clzz, params);
	}

	public Future<List<Object>> ftList(String sql,Class<?> clzz, List<Object> params) {
		return session.ftList(sql, clzz, params);
	}
	public Future<T> ftGet(String sql,List<Object> params) {
		return (Future<T>) session.ftGet(sql, this.getClass(), params);
	}
	
}
