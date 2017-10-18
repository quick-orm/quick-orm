/**
 * Copyright (c) 2017, ZhuKaipeng 朱开鹏 (2076528290@qq.com).

 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package kim.zkp.quick.orm.model;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;

import kim.zkp.quick.orm.cache.ClassCache;
import kim.zkp.quick.orm.session.Session;
import kim.zkp.quick.orm.sql.convert.FieldConvertProcessor;

public class Schema extends Model<Schema> {
	
	private Map<String,Object> result;
	
	/**
	 * method name   : open 
	 * description   : 打开表
	 * @return       : Schema
	 * @param        : @param tableName 表名
	 * @param        : @return
	 * modified      : zhukaipeng ,  2017年9月19日
	 * @see          : *
	 */
	public static Schema open(String tableName){
		return new Schema(tableName);
	}
	public static Schema open(){
		return new Schema();
	}
	public static Schema open(Session session){
		Schema s = open();
		s.setSession(session);
		return s;
	}
	/**
	 * method name   : open 
	 * description   : 打开表
	 * @return       : Schema
	 * @param        : @param tableName 表名
	 * @param        : @param session 数据源对象
	 * @param        : @return
	 * modified      : zhukaipeng ,  2017年9月19日
	 * @see          : *
	 */
	public static Schema open(String tableName,Session session){
		Schema s = open(tableName);
		s.setSession(session);
		return s;
	}
	/**
	 * method name   : open 
	 * description   : 打开表
	 * @return       : Schema
	 * @param        : @param tableName 表名
	 * @param        : @param jdbcConfigName 数据源配置文件名
	 * @param        : @return
	 * modified      : zhukaipeng ,  2017年9月19日
	 * @see          : *
	 */
	public static Schema open(String tableName,String jdbcConfigName){
		Schema s = open(tableName);
		s.setSession(jdbcConfigName);
		return s;
	}
	
	/**
	 * 创建Schema对象，需指定数据库表名
	 * @param tableName
	 */
	protected Schema(String tableName) {
		super();
		super.tableName(tableName);
	}
	/**
	 * 创建Schema对象，无需指定数据库表名
	 * @param tableName
	 */
	protected Schema() {
		super();
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
	 * method name   : getBigDecimal 
	 * description   : 根据列名获取值，返回值为BigDecimal
	 * @return       : String
	 * @param        : @param column
	 * @param        : @return
	 * modified      : zhukaipeng ,  2017年9月22日
	 */
	public BigDecimal getBigDecimal(String column){
		if (!this.isResult()) 
			return null;
		return result.get(column)==null?null:new BigDecimal(result.get(column).toString());
	}
	/**
	 * method name   : isResult 
	 * description   : 是否查询到结果
	 * @return       : boolean
	 * @param        : @return
	 * modified      : zhukaipeng ,  2017年9月15日
	 * @see          : *
	 */
	private boolean isResult(){
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