package com.z.quick.orm.model;

import java.util.LinkedHashMap;
import java.util.Map;
@SuppressWarnings("unchecked")
public abstract class LogicOperate<T>{
	private Map<String,Object> lt;
	private Map<String,Object> gt;
	private Map<String,Object> le;//<=
	private Map<String,Object> ge;//>=
	private Map<String,Object> eq;
	private Map<String,Object> neq;
	
	
	//private Map<String,Object> or; //or and中如何支持 > <
	//private Map<String,Object> and;
	
	//lt
	public Map<String,Object> lt() {
		return lt;
	}
	public T lt(String column, Object value) {
		if (lt == null) 
			lt = createConditionMap();
		lt.put(column, value);
		return (T) this;
	}
	public T lt(Map<String,Object> ltAll) {
		if (lt == null) 
			lt = createConditionMap();
		lt.putAll(ltAll);
		return (T) this;
	}
	
	//gt
	public Map<String,Object> gt() {
		return gt;
	}
	public T gt(String column, Object value) {
		if (gt == null) 
			gt = createConditionMap();
		gt.put(column, value);
		return (T) this;
	}
	public T gt(Map<String,Object> ltAll) {
		if (gt == null) 
			gt = createConditionMap();
		gt.putAll(ltAll);
		return (T) this;
	}
	
	//le
	public Map<String,Object> le() {
		return le;
	}
	public T le(String column, Object value) {
		if (le == null) 
			le = createConditionMap();
		le.put(column, value);
		return (T) this;
	}
	public T le(Map<String,Object> ltAll) {
		if (le == null) 
			le = createConditionMap();
		le.putAll(ltAll);
		return (T) this;
	}
	
	//ge
	public Map<String,Object> ge() {
		return ge;
	}
	public T ge(String column, Object value) {
		if (ge == null) 
			ge = createConditionMap();
		ge.put(column, value);
		return (T) this;
	}
	public T ge(Map<String,Object> ltAll) {
		if (ge == null) 
			ge = createConditionMap();
		ge.putAll(ltAll);
		return (T) this;
	}
	
	//eq
	public Map<String,Object> eq() {
		return eq;
	}
	public T eq(String column, Object value) {
		if (eq == null) 
			eq = createConditionMap();
		eq.put(column, value);
		return (T) this;
	}
	public T eq(Map<String,Object> eqAll) {
		if (eq == null) 
			eq = createConditionMap();
		eq.putAll(eqAll);
		return (T) this;
	}
	//neq
	public Map<String,Object> neq() {
		return neq;
	}
	public T neq(String column, Object value) {
		if (neq == null) 
			neq = createConditionMap();
		neq.put(column, value);
		return (T) this;
	}
	public T neq(Map<String,Object> ltAll) {
		if (neq == null) 
			neq = createConditionMap();
		neq.putAll(ltAll);
		return (T) this;
	}
	
	protected Map<String,Object> createConditionMap(){
		return new LinkedHashMap<>();
	}
	
}
