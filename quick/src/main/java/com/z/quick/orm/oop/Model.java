package com.z.quick.orm.oop;

import java.util.List;
import java.util.concurrent.Future;

import com.z.quick.orm.Session;
@SuppressWarnings("unchecked")
public abstract class Model<T> extends ObjectOperate<T> {
	
	public int save() {
		return Session.getSession().save(this);
	}

	public int update() {
		return Session.getSession().update(this);
	}

	public T get() {
		return (T) Session.getSession().get(this);
	}

	public List<T> list() {
		return (List<T>) Session.getSession().list(this);
	}

	public Future<Integer> asyncSave() {
		return Session.getSession().getSqlAsyncExecute().save(this);
	}

	public Future<Integer> asyncUpdate() {
		return Session.getSession().getSqlAsyncExecute().update(this);
	}

	public Future<T> asyncGet() {
		return (Future<T>) Session.getSession().getSqlAsyncExecute().get(this);
	}

	public Future<List<Object>> asyncList() {
		return Session.getSession().getSqlAsyncExecute().list(this);
	}
	
}
