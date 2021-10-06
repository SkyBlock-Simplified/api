package gg.sbs.api.data.sql;

import gg.sbs.api.data.sql.driver.*;

public final class SqlDrivers {

    public static final MariaDBDriver MariaDB = new MariaDBDriver();
    public static final MySQLDriver MySQL = new MySQLDriver();
    public static final OracleSQLDriver OracleSQL = new OracleSQLDriver();
    public static final PostgreSQLDriver PostgreSQL = new PostgreSQLDriver();
    public static final SQLServerDriver SQLServer = new SQLServerDriver();

}