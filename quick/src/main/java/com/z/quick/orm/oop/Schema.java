package com.z.quick.orm.oop;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.z.quick.orm.Session;
import com.z.quick.orm.sql.convert.FieldConvertProcessor;
@SuppressWarnings("unchecked")
public class Schema extends ObjectOperate<Schema> {
	
	private Map<String,Object> result;
	
	public Schema() {
		super();
	}
	
	public Schema(String tableName) {
		super();
		super.tableName(tableName);
	}

	public int save() {
		return Session.getSession().save(this);
	}
	public int save(String column, String value) {
		insert(column, value);
		return save();
	}
	public int save(Map<String,Object> insertAll) {
		insert(insertAll);
		return save();
	}
	
	public int delete() {
		return Session.getSession().delete(this);
	}
	public int delete(String column, String value) {
		this.eq(column, value);
		return delete();
	}
	public int delete(Map<String,Object> condition) {
		this.eq(condition);
		return delete();
	}
	
	public int update() {
		return Session.getSession().update(this);
	}
	public int update(String column, String value) {
		this.modif(column, value);
		return update();
	}
	public int update(Map<String,Object> modifAll) {
		this.modif(modifAll);
		return update();
	}
	
	public Schema get() {
		result = (Map<String, Object>) Session.getSession().get(this,Map.class);
		return this;
	}
	public Schema get(String column, String value) {
		this.eq(column, value);
		return get();
	}
	public Schema get(Map<String,Object> eqAll) {
		this.eq(eqAll);
		return get();
	}
	
	public List<Schema> list() {
		List<Schema> list = new ArrayList<>();
		Session.getSession().list(this,Map.class).forEach(o->{
			Schema s = new Schema();
			s.result = (Map<String, Object>) o;
			list.add(s);
		});
		return list;
	}
	public List<Schema> list(String column, String value) {
		this.eq(column, value);
		return list();
	}
	public List<Schema> list(Map<String,Object> eqAll) {
		this.eq(eqAll);
		return list();
	}
	
	public Set<String> columns(){
		return result.keySet();
	}
	
	public Integer getInt(String column){
		if (result == null) 
			return null;
		return result.get(column)==null?null:Integer.parseInt(result.get(column).toString());
	}
	public Long getLong(String column){
		if (result == null) 
			return null;
		return result.get(column)==null?null:Long.parseLong(result.get(column).toString());
	}
	public Double getDouble(String column){
		if (result == null) 
			return null;
		return result.get(column)==null?null:Double.parseDouble(result.get(column).toString());
	}
	public Short getShort(String column){
		if (result == null) 
			return null;
		return result.get(column)==null?null:Short.parseShort(result.get(column).toString());
	}
	public Boolean getBoolean(String column){
		if(result == null || result.get(column)==null)
			return false;
		return "1".equals(result.get(column).toString());
	}
	public String getStr(String column){
		if (result == null) 
			return null;
		return result.get(column)==null?null:result.get(column).toString();
	}

	public Map<String, Object> result() {
		return result;
	}

//	public void setResult(Map<String, Object> result) {
//		this.result = result;
//	}
	
	public boolean isResult(){
		return result!=null;
	}
	
	public Object toObject(Class<?> clzz){
		if (result == null) {
			return null;
		}
		try {
			Object o = clzz.newInstance();
			@SuppressWarnings("rawtypes")
			List<Field> fields = new ArrayList(Arrays.asList(clzz.getDeclaredFields()));
			fields.forEach(f->{
				String k = f.getName().toUpperCase();
				Object v = result.get(k);
				if (v != null) {
					try {
						f.setAccessible(true);
						f.set(o, FieldConvertProcessor.toJava(f.getType(),v));
					} catch (Exception e) {
					}
				}
			});
			return o;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public String toString() {
		return "Schema [result=" + result + "]";
	}
	
}



