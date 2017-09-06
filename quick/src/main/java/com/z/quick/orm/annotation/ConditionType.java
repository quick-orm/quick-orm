package com.z.quick.orm.annotation;

public enum ConditionType{
	LT(" < "),
	GT(" > "),
	LE(" <= "),
	GE(" >= "),
	EQ(" = "),
	NEQ(" != "),
	LIKE(" like ");
	
	private String operation;
	private ConditionType(String operation){
		this.operation = operation;
	}
	public String getOperation() {
		return operation;
	}
	
}