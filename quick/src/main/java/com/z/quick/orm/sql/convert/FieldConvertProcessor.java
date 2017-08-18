package com.z.quick.orm.sql.convert;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

public class FieldConvertProcessor {
	
	private static final Map<Class<?>,IFieldConvert> converts = new HashMap<Class<?>,IFieldConvert>();
	
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
	}
	
	public static void registerConvert(Class<?> clzz,IFieldConvert ac){
		converts.put(clzz, ac);
	}
	
	public static Object toJava(Class<?>clzz, Object o){
		IFieldConvert ac = converts.get(clzz);
		if (ac == null) {
			return o;
		}
		return ac.toJava(o);
	}
	public static Object toDB(Object o){
		IFieldConvert ac = converts.get(o.getClass());
		if (ac == null) {
			throw new RuntimeException(o.getClass()+"未注册转换器");
		}
		return ac.toDB(o);
	}
}
