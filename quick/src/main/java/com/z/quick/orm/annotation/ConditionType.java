package com.z.quick.orm.annotation;

public enum ConditionType{
	/**小于*/
	LT(" < "),
	/**大于*/
	GT(" > "),
	/**小于等于*/
	LE(" <= "),
	/**大于等于*/
	GE(" >= "),
	/**等于*/
	EQ(" = "),
	/**不等于*/
	NEQ(" != "),
	/**like条件*/
	LIKE(" like ");
	
	private String operation;
	private ConditionType(String operation){
		this.operation = operation;
	}
	public String getOperation() {
		return operation;
	}
	
}