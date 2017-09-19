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

import java.math.BigDecimal;

public interface FieldConvert {

	Object toJava(Object o);
	
	Object toDB(Object o);

}

class BooleanFieldConvert implements FieldConvert {

	@Override
	public Object toJava(Object o) {
		return "1".equals(o.toString());
	}

	@Override
	public Object toDB(Object o) {
		return (boolean) o?1:0;
	}

}

class StringFieldConvert implements FieldConvert {
	
	@Override
	public Object toJava(Object o) {
		return o.toString();
	}
	
	@Override
	public Object toDB(Object o) {
		return o;
	}
	
}

class IntegerFieldConvert implements FieldConvert {
	
	@Override
	public Object toJava(Object o) {
		return Integer.valueOf(o.toString());
	}
	
	@Override
	public Object toDB(Object o) {
		return o;
	}
	
}

class FloatFieldConvert implements FieldConvert {
	
	@Override
	public Object toJava(Object o) {
		return Float.valueOf(o.toString());
	}
	
	@Override
	public Object toDB(Object o) {
		return o;
	}
	
}

class ShortFieldConvert implements FieldConvert {
	
	@Override
	public Object toJava(Object o) {
		return Short.valueOf(o.toString());
	}
	
	@Override
	public Object toDB(Object o) {
		return o;
	}
	
}

class LongFieldConvert implements FieldConvert {
	
	@Override
	public Object toJava(Object o) {
		return Long.valueOf(o.toString());
	}
	
	@Override
	public Object toDB(Object o) {
		return o;
	}
	
}

class DoubleFieldConvert implements FieldConvert {
	
	@Override
	public Object toJava(Object o) {
		return Double.valueOf(o.toString());
	}
	
	@Override
	public Object toDB(Object o) {
		return o;
	}
	
}

class BigDecimalFieldConvert implements FieldConvert {
	
	@Override
	public Object toJava(Object o) {
		return new BigDecimal(o.toString());
	}
	
	@Override
	public Object toDB(Object o) {
		return o;
	}
	
}
//TODO 测试
class TimestampFieldConvert implements FieldConvert {
	
	@Override
	public Object toJava(Object o) {
		return o;
	}
	
	@Override
	public Object toDB(Object o) {
		return o;
	}
	
}
class DefaultFieldConvert implements FieldConvert {
	
	@Override
	public Object toJava(Object o) {
		return o;
	}
	
	@Override
	public Object toDB(Object o) {
		return o;
	}
	
}