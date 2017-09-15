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


