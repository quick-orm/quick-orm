package com.z.quick.orm.connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.xiaoleilu.hutool.date.DateUtil;
import com.xiaoleilu.hutool.date.TimeInterval;
import com.z.quick.monitor.MonitorSql;
import com.z.quick.orm.session.Session;
import com.z.quick.orm.sql.SqlInfo;

/**
 * ****************** 类说明 ********************* class : PreparedStatementWrapper
 * 
 * @author : zhukaipeng
 * @version : 1.0 description : 目前无法从代理类内部获取到执行的sql，无法用代理实现 后期增加sql预编译缓存再更改
 * @see : ***********************************************
 */
public class PreparedStatementWrapper {

	private PreparedStatement stmt;
	private SqlInfo sqlInfo;

	public PreparedStatementWrapper(PreparedStatement stmt, SqlInfo sqlInfo) {
		super();
		this.stmt = stmt;
		this.sqlInfo = sqlInfo;
	}

	public int executeUpdate() throws SQLException {
		if (!Session.getSession().getJdbcConfig().getExecuteTimeMonitor())
			return stmt.executeUpdate();

		TimeInterval timer = DateUtil.timer();
		int result = stmt.executeUpdate();
		if (timer.interval() > Session.getSession().getJdbcConfig().getMaxExecuteTime()) {
			MonitorSql.record(sqlInfo.getSql(),timer.interval());
		}
		return result;
	}

	public ResultSet executeQuery() throws SQLException {
		if (!Session.getSession().getJdbcConfig().getExecuteTimeMonitor())
			return stmt.executeQuery();

		TimeInterval timer = DateUtil.timer();
		ResultSet result = stmt.executeQuery();
		if (timer.interval() > Session.getSession().getJdbcConfig().getMaxExecuteTime()) {
			MonitorSql.record(sqlInfo.getSql(),timer.interval());
		}
		return result;
	}

	public void close() throws SQLException {
		stmt.close();
	}

}
