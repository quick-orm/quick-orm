package com.z.quick.orm.sql.convert;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.z.quick.orm.exception.SqlBuilderException;

public class FieldConvertProcessor {
	
	private static final Map<Class<?>,FieldConvert> converts = new HashMap<Class<?>,FieldConvert>();
	
	static{
		registerConvert(Boolean.class, new BooleanFieldConvert());
		registerConvert(String.class, new StringFieldConvert());
		registerConvert(Integer.class, new IntegerFieldConvert());
		registerConvert(Float.class, new FloatFieldConvert());
		registerConvert(Short.class, new ShortFieldConvert());
		registerConvert(Long.class, new LongFieldConvert());
		registerConvert(Double.class, new DoubleFieldConvert());
		registerConvert(BigDecimal.class, new BigDecimalFieldConvert());
		registerConvert(Timestamp.class, new TimestampFieldConvert());
		registerConvert(LinkedHashMap.class, new DefaultFieldConvert());
	}
	
	public static void registerConvert(Class<?> clzz,FieldConvert ac){
		converts.put(clzz, ac);
	}
	
	public static Object toJava(Class<?>clzz, Object o){
		FieldConvert ac = converts.get(clzz);
		if (ac == null) {
			return o;
		}
		return ac.toJava(o);
	}
	public static Object toDB(Object o){
		FieldConvert ac = converts.get(o.getClass());
		if (ac == null) {
			throw new SqlBuilderException(o.getClass()+"未注册转换器");
		}
		return ac.toDB(o);
	}
	public static Object toDB(Field f,Object o){
		Object v = null;
		try {
			f.setAccessible(true);
			v = f.get(o);
			if (v == null) {
				return null;
			}
		} catch (IllegalArgumentException e) {
			throw new SqlBuilderException("通过反射获取属性值出错!",e);
		} catch (IllegalAccessException e) {
			throw new SqlBuilderException("通过反射获取属性值出错!",e);
		}
		return FieldConvertProcessor.toDB(v);
	}
}
