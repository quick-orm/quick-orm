package kim.zkp.quick.orm.cache;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kim.zkp.quick.orm.annotation.Condition;
import kim.zkp.quick.orm.annotation.Exclude;
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
	
	private static Map<String,Field> getAllFieldMap(Class<?> clzz){
		Map<String,Field> fieldMap = allFieldClassCache.get(clzz);
		if (fieldMap != null) {
			return fieldMap;
		}
		fieldMap = parseClass(clzz);
		allFieldClassCache.put(clzz,fieldMap);
		return fieldMap;
	}
	
	private static List<Field> getDeclaredFields(Class<?> clzz){
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
	public static List<Field> getAllDeclaredFields(Class<?> clzz){
		if (declaredFieldsCache.get(clzz) != null) {
			return declaredFieldsCache.get(clzz);
		}
		List<Field> list = getDeclaredFields(clzz);
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
	public static String getSelect(Class<?> clzz){
		if (annationSelectCache.get(clzz) != null) {
			return annationSelectCache.get(clzz);
		}
		List<Field> fieldList = getDeclaredFields(clzz);
		fieldList.removeIf(f -> f.getAnnotation(Exclude.class)!=null); 
		fieldList.removeIf(f -> f.getAnnotation(NoFind.class)!=null); 
		StringBuffer selectsb = new StringBuffer();
		fieldList.forEach((f) -> {
			selectsb.append(Constants.SPACE).append(f.getName()).append(",");
		});
		if (selectsb.length() == 0) {
			annationSelectCache.put(clzz, "");
			return "";
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
		List<Field> fieldList = getDeclaredFields(clzz);
		fieldList.removeIf(f -> f.getAnnotation(Exclude.class)!=null); 
		insertParamCache.put(clzz, fieldList);
		return fieldList;
	}
	
	@SuppressWarnings({"unchecked","rawtypes"})
	public static List<Field> getAnnotationField(Map<Class<?>,List<Field>> fieldCache,Class<?> clzz, Class annotationClass){
		if (fieldCache.get(clzz) != null) {
			return (List<Field>) fieldCache.get(clzz);
		}
		List<Field> fieldList = getDeclaredFields(clzz);
		fieldList.removeIf(f -> f.getAnnotation(annotationClass)==null); 
		fieldCache.put(clzz, fieldList);
		return fieldList;
	}	
	
	private static Map<String,Field> parseClass(Class<?> clzz){
		Map<String,Field> fieldMap = new HashMap<>();
		List<Field> list = getDeclaredFields(clzz);
		list.forEach(f->{
			fieldMap.put(f.getName(), f);
		});
		if (clzz.getSuperclass() != null) {
			fieldMap.putAll(parseClass(clzz.getSuperclass()));
		}
		return fieldMap;
	}
	
	
}
