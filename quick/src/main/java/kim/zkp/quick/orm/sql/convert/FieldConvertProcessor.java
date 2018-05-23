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

package kim.zkp.quick.orm.sql.convert;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.LogFactory;

import kim.zkp.quick.orm.exception.SqlBuilderException;

public class FieldConvertProcessor {
	
	private static final Map<Class<?>,FieldConvert> converts = new HashMap<Class<?>,FieldConvert>();
	private static final Log log = LogFactory.get();
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
		registerConvert(StringBuilder.class, new StringBuilderConvert());
		registerConvert(LinkedHashMap.class, new DefaultFieldConvert());
		registerConvert(ArrayList.class, new DefaultFieldConvert());
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
			log.info("{}未注册java至数据库的类型转换器，无法获取到值",o.getClass());
			return null;
//			throw new SqlBuilderException(o.getClass()+"未注册转换器");
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