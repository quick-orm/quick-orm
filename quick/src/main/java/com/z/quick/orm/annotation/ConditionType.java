package com.z.quick.orm.annotation;

public enum ConditionType{
	LT("<"),
	GT(">"),
	LE("<="),
	GE(">="),
	EQ("="),
	NEQ("!=");
	
	private String operation;
	private ConditionType(String operation){
		this.operation = operation;
	}
	public String getOperation() {
		return operation;
	}
	
}