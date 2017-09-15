package com.z.quick.orm.model;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.z.quick.orm.cache.ClassCache;
import com.z.quick.orm.sql.convert.FieldConvertProcessor;

public class Schema extends Model<Schema> {
	
	private Map<String,Object> result;
	
	/**
	 * 创建Schema对象，需指定数据库表名
	 * @param tableName
	 */
	public Schema(String tableName) {
		super();
		super.tableName(tableName);
	}
	/**
	 * method name   : get 查询
	 * modified      : zhukaipeng ,  2017年9月15日
	 * @see          : @see com.z.quick.orm.model.Model#get()*
	 */
	public Schema get() {
		Schema schema = super.get();
		if (schema!=null) {
			this.setResult(schema.getResult());
		}else {
			this.setResult(null);
		}
		return this;
	}
	/**
	 * method name   : columns 
	 * description   : 获取所有列
	 * @return       : Set<String>
	 * @param        : @return
	 * modified      : zhukaipeng ,  2017年9月15日
	 * @see          : *
	 */
	public Set<String> columns(){
		if (!this.isResult()) {
			return null;
		}
		return result.keySet();
	}
	/**
	 * method name   : getInt 
	 * description   : 根据列名获取值，返回值为Integer
	 * @return       : Integer
	 * @param        : @param column
	 * @param        : @return
	 * modified      : zhukaipeng ,  2017年9月15日
	 * @see          : *
	 */
	public Integer getInt(String column){
		if (!this.isResult()) 
			return null;
		return result.get(column)==null?null:Integer.parseInt(result.get(column).toString());
	}
	/**
	 * method name   : getLong 
	 * description   : 根据列名获取值，返回值为Long
	 * @return       : Long
	 * @param        : @param column
	 * @param        : @return
	 * modified      : zhukaipeng ,  2017年9月15日
	 * @see          : *
	 */
	public Long getLong(String column){
		if (!this.isResult()) 
			return null;
		return result.get(column)==null?null:Long.parseLong(result.get(column).toString());
	}
	/**
	 * method name   : getDouble 
	 * description   : 根据列名获取值，返回值为Double
	 * @return       : Double
	 * @param        : @param column
	 * @param        : @return
	 * modified      : zhukaipeng ,  2017年9月15日
	 * @see          : *
	 */
	public Double getDouble(String column){
		if (!this.isResult()) 
			return null;
		return result.get(column)==null?null:Double.parseDouble(result.get(column).toString());
	}
	/**
	 * method name   : getShort 
	 * description   : 根据列名获取值，返回值为Short
	 * @return       : Short
	 * @param        : @param column
	 * @param        : @return
	 * modified      : zhukaipeng ,  2017年9月15日
	 * @see          : *
	 */
	public Short getShort(String column){
		if (!this.isResult()) 
			return null;
		return result.get(column)==null?null:Short.parseShort(result.get(column).toString());
	}
	/**
	 * method name   : getBoolean 
	 * description   : 根据列名获取值，返回值为Boolean
	 * @return       : Boolean
	 * @param        : @param column
	 * @param        : @return
	 * modified      : zhukaipeng ,  2017年9月15日
	 * @see          : *
	 */
	public Boolean getBoolean(String column){
		if(!this.isResult() || result.get(column)==null)
			return false;
		return "1".equals(result.get(column).toString());
	}
	/**
	 * method name   : getStr 
	 * description   : 根据列名获取值，返回值为String
	 * @return       : String
	 * @param        : @param column
	 * @param        : @return
	 * modified      : zhukaipeng ,  2017年9月15日
	 * @see          : *
	 */
	public String getStr(String column){
		if (!this.isResult()) 
			return null;
		return result.get(column)==null?null:result.get(column).toString();
	}
	/**
	 * method name   : isResult 
	 * description   : 是否查询到结果
	 * @return       : boolean
	 * @param        : @return
	 * modified      : zhukaipeng ,  2017年9月15日
	 * @see          : *
	 */
	public boolean isResult(){
		return result!=null;
	}
	
	public void setResult(Map<String, Object> result) {
		this.result = result;
	}
	/**
	 * method name   : getResult 
	 * description   : 获取所有查询结果
	 * @return       : Map<String,Object>
	 * @param        : @return
	 * modified      : zhukaipeng ,  2017年9月15日
	 * @see          : *
	 */
	public Map<String, Object> getResult() {
		return result;
	}
	/**
	 * method name   : toObject 
	 * description   : 将查询结果转换对对象
	 * @return       : Object
	 * @param        : @param clzz 转换对象的类型
	 * @param        : @return
	 * modified      : zhukaipeng ,  2017年9月15日
	 * @see          : *
	 */
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



