package gg.sbs.api.data.sql.integrated;

import gg.sbs.api.data.sql.integrated.factory.SQLWrapper;
import gg.sbs.api.util.FormatUtil;

import java.sql.SQLException;

public class SQLServer extends SQLWrapper {

	public static final int DEFAULT_PORT = 1433;

	public SQLServer(String host, String user, String pass, String schema) throws SQLException {
		this(host, DEFAULT_PORT, user, pass, schema);
	}

	public SQLServer(String host, int port, String user, String pass, String schema) throws SQLException {
		super("com.microsoft.jdbc.sqlserver.SQLServerDriver", FormatUtil.format("jdbc:microsoft:sqlserver://{0}:{1,number,#};DatabaseName={2}", host, port, schema), user, pass);
	}

	/**
	 * Checks if the Microsoft SQL JDBC driver is available.
	 *
	 * @return True if available, otherwise false.
	 */
	@Override
	public final boolean isDriverAvailable() {
		return super.isDriverAvailable();
	}

}