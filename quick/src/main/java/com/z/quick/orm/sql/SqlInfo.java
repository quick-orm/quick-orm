package com.z.quick.orm.sql;

import java.util.List;

public class SqlInfo {
	
	private String sql;
	private List<Object> param;
	
	public SqlInfo(String sql, List<Object> param) {
		super();
		this.sql = sql;
		this.param = param;
	}
	public String getSql() {
		return sql;
	}
	public void setSql(String sql) {
		this.sql = sql;
	}
	public List<Object> getParam() {
		return param;
	}
	public void setParam(List<Object> param) {
		this.param = param;
	}
	@Override
	public String toString() {
		return "Sql [sql=" + sql + " param=" + param + "]";
	}
	
	
	
}
