package com.z.quick.orm.oop;

import java.util.HashMap;
import java.util.Map;

public class LogicConstants {
	public static final Map<String,String> LOGIC_OPERATION = new HashMap<String, String>();
	static{
		LOGIC_OPERATION.put("lt", " < ");
		LOGIC_OPERATION.put("gt", " > ");
		LOGIC_OPERATION.put("le", " <= ");
		LOGIC_OPERATION.put("ge", " >= ");
		LOGIC_OPERATION.put("eq", " = ");
		LOGIC_OPERATION.put("neq", " != ");
	}
}
