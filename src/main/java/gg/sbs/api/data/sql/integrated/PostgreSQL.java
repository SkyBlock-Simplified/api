package gg.sbs.api.data.sql.integrated;

import gg.sbs.api.data.sql.integrated.factory.SQLWrapper;
import gg.sbs.api.util.helper.FormatUtil;

import java.sql.SQLException;

public class PostgreSQL extends SQLWrapper {

	public static final int DEFAULT_PORT = 5432;

	public PostgreSQL(String host, String user, String pass, String schema) throws SQLException {
		this(host, DEFAULT_PORT, user, pass, schema);
	}

	public PostgreSQL(String host, int port, String user, String pass, String schema) throws SQLException {
		super("org.postgresql.Driver", FormatUtil.format("jdbc:postgresql://{0}:{1,number,#}/{2}", host, port, schema), user, pass);
	}

	/**
	 * Checks if the PostgreSQL JDBC driver is available.
	 *
	 * @return True if available, otherwise false.
	 */
	@Override
	public final boolean isDriverAvailable() {
		return super.isDriverAvailable();
	}

}