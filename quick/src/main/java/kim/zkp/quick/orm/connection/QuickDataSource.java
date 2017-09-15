package kim.zkp.quick.orm.connection;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

import javax.sql.DataSource;

/**
 * ******************  类说明  *********************
 * class       :  DefaultDataSource
 * @author     :  zhukaipeng
 * @version    :  1.0  
 * description :  默认DataSource
 * @see        :                        
 * ***********************************************
 */
public class QuickDataSource implements DataSource{
	
	private ConnectionPool connPool;

	public QuickDataSource(JDBCConfig jdbcConfig) {
		super();
		connPool = new ConnectionPool(jdbcConfig);
	}

	@Override
	public Connection getConnection() throws SQLException {
		return connPool.getConnection();
	}

	@Override
	public Connection getConnection(String username, String password) throws SQLException {
		return connPool.getConnection(username, password);
	}
	
	@Override
	public PrintWriter getLogWriter() throws SQLException {
		return DriverManager.getLogWriter();
	}

	@Override
	public void setLogWriter(PrintWriter out) throws SQLException {
		DriverManager.setLogWriter(out);
	}

	@Override
	public void setLoginTimeout(int seconds) throws SQLException {
		DriverManager.setLoginTimeout(seconds);
	}

	@Override
	public int getLoginTimeout() throws SQLException {
		return DriverManager.getLoginTimeout();
	}

	@Override
	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		throw new SQLFeatureNotSupportedException("DataSource can't support getParentLogger method!");
	}

	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		throw new SQLException("Can't support unwrap method!");
	}

	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		throw new SQLException("Can't support isWrapperFor method!");
	}


}
