package gg.sbs.api.data.sql.integrated;

import gg.sbs.api.data.sql.integrated.factory.SQLWrapper;
import gg.sbs.api.util.helper.FormatUtil;

import java.sql.SQLException;

public class OracleSQL extends SQLWrapper {

	public static final int DEFAULT_PORT = 1571;

	public OracleSQL(String host, String user, String pass, String schema) throws SQLException {
		this(host, DEFAULT_PORT, user, pass, schema);
	}

	public OracleSQL(String host, int port, String user, String pass, String schema) throws SQLException {
		super("oracle.jdbc.driver.OracleDriver", FormatUtil.format("jdbc:oracle:thin:@{0}:{1,number,#}:{2}", host, port, schema), user, pass);
	}

	/**
	 * Checks if the Oracle JDBC driver is available.
	 *
	 * @return True if available, otherwise false.
	 */
	@Override
	public final boolean isDriverAvailable() {
		return super.isDriverAvailable();
	}

}