package com.z.quick.orm.oop;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import com.z.quick.orm.Session;

@SuppressWarnings("unchecked")
public abstract class ObjectOperate<T> extends LogicOperate<T> {

	private String tableName;
	private String select;
	private String where;
	private Map<String, Object> insert;
	private Map<String, Object> modif;

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

//	public Future<Integer> asyncSave() {
//		return Session.getSession().getSqlAsyncExecute().save(this);
//	}
//
//	public Future<Integer> asyncUpdate() {
//		return Session.getSession().getSqlAsyncExecute().update(this);
//	}
//
//	public Future<T> asyncGet() {
//		return (Future<T>) Session.getSession().getSqlAsyncExecute().get(this);
//	}
//
//	public Future<List<Object>> asyncList() {
//		return Session.getSession().getSqlAsyncExecute().list(this);
//	}

}
