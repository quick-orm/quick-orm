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

package kim.zkp.quick.orm.cache;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kim.zkp.quick.orm.annotation.Condition;
import kim.zkp.quick.orm.annotation.Exclude;
import kim.zkp.quick.orm.annotation.Find;
import kim.zkp.quick.orm.annotation.Join;
import kim.zkp.quick.orm.annotation.NoFind;
import kim.zkp.quick.orm.annotation.OrderBy;
import kim.zkp.quick.orm.annotation.PrimaryKey;
import kim.zkp.quick.orm.annotation.Table;
import kim.zkp.quick.orm.common.Constants;
public class ClassCache {
	
	private static final Map<Class<?>,Map<String,Field>> allFieldClassCache = new HashMap<>();
	private static final Map<Class<?>,List<Field>> declaredFieldsCache = new HashMap<>();
	private static final Map<Class<?>,String> annationTableNameCache = new HashMap<>();
	private static final Map<Class<?>,String> annationSelectCache = new HashMap<>();
	private static final Map<Class<?>,List<Field>> annationPKCache = new HashMap<>();
	private static final Map<Class<?>,List<Field>> annationOrderByCache = new HashMap<>();
	private static final Map<Class<?>,List<Field>> annationConditionCache = new HashMap<>();
	private static final Map<Class<?>,List<Field>> insertParamCache = new HashMap<>();
	private static final Map<Class<?>,List<Field>> joinParamCache = new HashMap<>();
	private static final Map<Class<?>,List<Field>> findParamCache = new HashMap<>();
	private static final Map<Class<?>,Method> strategyMethodCache = new HashMap<>();
	private static final Map<Class<?>,String> aliasCache = new HashMap<>();
	
	private static Map<String,Field> getAllFieldMap(Class<?> clzz){
		Map<String,Field> fieldMap = allFieldClassCache.get(clzz);
		if (fieldMap != null) {
			return fieldMap;
		}
		fieldMap = parseClass(clzz);
		allFieldClassCache.put(clzz,fieldMap);
		return fieldMap;
	}
	/**
	 * method name   : getNoJoinAllField 
	 * description   : 获取排除联表外的所有字段
	 * @return       : List<Field>
	 * @param        : @param clzz
	 * @param        : @return
	 * modified      : zhukaipeng ,  2018年2月1日
	 */
	private static List<Field> getNoJoinAllField(Class<?> clzz){
		List<Field> list = getAllField(clzz);
		list.removeIf(f->f.getAnnotation(Join.class)!=null);
		list.removeIf(f->f.getAnnotation(Find.class)!=null);
		return list;
		
	}
	private static List<Field> getAllField(Class<?> clzz){
		Field[] fields = clzz.getDeclaredFields();
		List<Field> list = new ArrayList<>(Arrays.asList(fields));
		list.removeIf(f->{
			int m = f.getModifiers();
			if ((Modifier.STATIC & m) == Modifier.STATIC) {
				return true;
			}else if((Modifier.FINAL & m) == Modifier.FINAL) {
				return true;
			}else if((Modifier.VOLATILE & m) == Modifier.VOLATILE) {
				return true;
			}
			return false;
		});
		return list;
		
	}
	/**
	 * method name   : getAllDeclaredFields 
	 * description   : 获取该类(不包含父类及被static、final、volatile修饰的属性)所有属性
	 * @return       : List<Field>
	 * @param        : @param clzz
	 * @param        : @return
	 * modified      : zhukaipeng ,  2017年8月29日
	 * @see          : *
	 */
	public static List<Field> getAllFieldByCache(Class<?> clzz){
		if (declaredFieldsCache.get(clzz) != null) {
			return declaredFieldsCache.get(clzz);
		}
		List<Field> list = getNoJoinAllField(clzz);
		declaredFieldsCache.put(clzz, list);
		return list;
	}
	public static Field getField(Class<?> clzz,String fieldName){
		return getAllFieldMap(clzz).get(fieldName);
	}
	public static String getTableName(Class<?> clzz){
		if (annationTableNameCache.get(clzz) != null) {
			return annationTableNameCache.get(clzz);
		}
		Table table = clzz.getAnnotation(Table.class);
		if (table != null && !"".equals(table.tableName())) {
			return table.tableName();
		}
		String tableName = clzz.getSimpleName();
		annationTableNameCache.put(clzz, tableName);
		return tableName;
		
	}
	public static String getSelect(Class<?> clzz,boolean nextFind){
		if (annationSelectCache.get(clzz) != null) {
			return annationSelectCache.get(clzz);
		}
		String alias = getAliasPoint(clzz);
		String aliasUnderline = getAliasUnderline(clzz);
		List<Field> fieldList = getNoJoinAllField(clzz);
		fieldList.removeIf(f -> f.getAnnotation(Exclude.class)!=null); 
		fieldList.removeIf(f -> f.getAnnotation(NoFind.class)!=null); 
		StringBuffer selectsb = new StringBuffer();
		fieldList.forEach((f) -> {
			selectsb.append(Constants.SPACE).append(alias).append(f.getName());
			selectsb.append(Constants.AS).append(aliasUnderline).append(f.getName()).append(Constants.COMMA);
		});
		if (selectsb.length() == 0) {
			annationSelectCache.put(clzz, "");
			return "";
		}
		if (nextFind) {
			List<Field> joinList = getJoin(clzz);
			for (Field f : joinList) {
				Class<?> joinClass = f.getType();
				String joinAlias = getAliasPoint(joinClass);
				String joinAliasUnderline = getAliasUnderline(joinClass);
				Join join = f.getAnnotation(Join.class);
				String[] findList = join.findList();
				if (findList.length != 0) {
					for (int i = 0; i < findList.length; i++) {
						selectsb.append(Constants.SPACE).append(joinAlias).append(findList[i]);
						selectsb.append(Constants.AS).append(joinAliasUnderline).append(findList[i]).append(Constants.COMMA);
					}
				}else {
					String joinFind = getSelect(joinClass,false);
					selectsb.append(joinFind).append(",");
				}
			}
			List<Field> findList = getFind(clzz);
			for (Field f : findList) {
				Find find = f.getAnnotation(Find.class);
				String findField = find.field();
				if ("".equals(findField)) {
					findField = f.getName();
				}
				String joinAlias = find.joinTable()+Constants.UNDERLINE+Constants.POINT;
				selectsb.append(Constants.SPACE).append(joinAlias).append(findField);
				selectsb.append(Constants.AS).append(aliasUnderline).append(f.getName()).append(Constants.COMMA);
			}
		}
		
		
		String select = selectsb.deleteCharAt(selectsb.lastIndexOf(",")).toString();
		annationSelectCache.put(clzz, select);
		return select;
		
	}
	
	public static List<Field> getPrimaryKey(Class<?> clzz){
		return getAnnotationField(annationPKCache, clzz,PrimaryKey.class);
	}
	public static List<Field> getOrderBy(Class<?> clzz){
		return getAnnotationField(annationOrderByCache, clzz,OrderBy.class);
	}
	
	public static List<Field> getCondition(Class<?> clzz){
		return getAnnotationField(annationConditionCache, clzz,Condition.class);
	}
	public static List<Field> getInsert(Class<?> clzz){
		if (insertParamCache.get(clzz) != null) {
			return insertParamCache.get(clzz);
		}
		List<Field> fieldList = getNoJoinAllField(clzz);
		fieldList.removeIf(f -> f.getAnnotation(Exclude.class)!=null); 
		insertParamCache.put(clzz, fieldList);
		return fieldList;
	}
	public static List<Field> getJoin(Class<?> clzz){
		if (joinParamCache.get(clzz) != null) {
			return joinParamCache.get(clzz);
		}
		List<Field> fieldList = getAllField(clzz);
		List<Field> joinList = new ArrayList<>();
		fieldList.forEach(f->{
			if (f.getAnnotation(Join.class) != null) {
				joinList.add(f);
			}
		}); 
		joinParamCache.put(clzz, joinList);
		return joinList;
	}
	public static List<Field> getFind(Class<?> clzz){
		if (findParamCache.get(clzz) != null) {
			return findParamCache.get(clzz);
		}
		List<Field> fieldList = getAllField(clzz);
		List<Field> findList = new ArrayList<>();
		fieldList.forEach(f->{
			if (f.getAnnotation(Find.class) != null) {
				findList.add(f);
			}
		}); 
		findParamCache.put(clzz, findList);
		return findList;
	}
	
	@SuppressWarnings({"unchecked","rawtypes"})
	public static List<Field> getAnnotationField(Map<Class<?>,List<Field>> fieldCache,Class<?> clzz, Class annotationClass){
		if (fieldCache.get(clzz) != null) {
			return (List<Field>) fieldCache.get(clzz);
		}
		List<Field> fieldList = getNoJoinAllField(clzz);
		fieldList.removeIf(f -> f.getAnnotation(annotationClass)==null); 
		fieldCache.put(clzz, fieldList);
		return fieldList;
	}	
	
	private static Map<String,Field> parseClass(Class<?> clzz){
		Map<String,Field> fieldMap = new HashMap<>();
		List<Field> list = getNoJoinAllField(clzz);
		list.forEach(f->{
			fieldMap.put(f.getName(), f);
		});
		if (clzz.getSuperclass() != null) {
			fieldMap.putAll(parseClass(clzz.getSuperclass()));
		}
		return fieldMap;
	}
	public static Method getStrategyMethod(Class<?> clzz){
		Method m = strategyMethodCache.get(clzz);
		if (m == null) {
			try {
				m = clzz.getMethod("strategy");
				strategyMethodCache.put(clzz, m);
			} catch (NoSuchMethodException | SecurityException e) {
				return null;
			}
		}
		return m;
	}
	public static String getAlias(Class<?> clzz){
		String alias = aliasCache.get(clzz);
		if (alias == null) {
			alias = clzz.getSimpleName().toLowerCase();
			if ("schema".equals(alias)) {
				alias = "temp_0";
			}
			aliasCache.put(clzz, alias);
		}
		return alias;
	}
	
	public static String getAliasPoint(Class<?> clzz){
		return getAlias(clzz)+Constants.POINT;
	}
	public static String getAliasUnderline(Class<?> clzz){
		return getAlias(clzz)+Constants.UNDERLINE;
	}
}






