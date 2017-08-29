package com.z.quick.orm.oop;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.z.quick.orm.cache.ClassCache;
import com.z.quick.orm.sql.convert.FieldConvertProcessor;

public class Schema extends Model<Schema> {
	
	private Map<String,Object> result;
	
	public Schema() {
		super();
	}
	
	public Schema(String tableName) {
		super();
		super.tableName(tableName);
	}

	public Schema get() {
		this.setResult(super.get().getResult());
		return this;
	}
	
	public Set<String> columns(){
		if (!this.isResult()) {
			return null;
		}
		return result.keySet();
	}
	
	public Integer getInt(String column){
		if (!this.isResult()) 
			return null;
		return result.get(column)==null?null:Integer.parseInt(result.get(column).toString());
	}
	public Long getLong(String column){
		if (!this.isResult()) 
			return null;
		return result.get(column)==null?null:Long.parseLong(result.get(column).toString());
	}
	public Double getDouble(String column){
		if (!this.isResult()) 
			return null;
		return result.get(column)==null?null:Double.parseDouble(result.get(column).toString());
	}
	public Short getShort(String column){
		if (!this.isResult()) 
			return null;
		return result.get(column)==null?null:Short.parseShort(result.get(column).toString());
	}
	public Boolean getBoolean(String column){
		if(!this.isResult() || result.get(column)==null)
			return false;
		return "1".equals(result.get(column).toString());
	}
	public String getStr(String column){
		if (!this.isResult()) 
			return null;
		return result.get(column)==null?null:result.get(column).toString();
	}

	public Map<String, Object> result() {
		return result;
	}

	public boolean isResult(){
		return result!=null;
	}
	
	public void setResult(Map<String, Object> result) {
		this.result = result;
	}
	
	public Map<String, Object> getResult() {
		return result;
	}

	public Object toObject(Class<?> clzz){
		if (!this.isResult()) {
			return null;
		}
		try {
			Object o = clzz.newInstance();
			List<Field> fields = ClassCache.getAllDeclaredFields(clzz);
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



