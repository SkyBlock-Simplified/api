package gg.sbs.api.database.drivers;

import gg.sbs.api.database.drivers.factory.SQLWrapper;
import gg.sbs.api.util.StringUtil;

import java.sql.SQLException;

public class MariaDB extends SQLWrapper {

	public static final int DEFAULT_PORT = 3306;

	public MariaDB(String host, String user, String pass, String schema) throws SQLException {
		this(host, DEFAULT_PORT, user, pass, schema);
	}

	public MariaDB(String host, int port, String user, String pass, String schema) throws SQLException {
		super("org.mariadb.jdbc.Driver", StringUtil.format("jdbc:mysql://{0}:{1,number,#}/{2}", host, port, schema), user, pass);
	}

	/**
	 * Checks if the MySQL jdbc driver is available.
	 *
	 * @return True if available, otherwise false.
	 */
	@Override
	public final boolean isDriverAvailable() {
		return super.isDriverAvailable();
	}

}