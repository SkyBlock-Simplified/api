package dev.sbs.api.data.sql;

import dev.sbs.api.util.helper.FormatUtil;

public abstract class SqlDriver {

    public static final SqlDriver MariaDB = new SqlDriver() {

        @Override
        public String getConnectionUrl(String databaseHost, int databasePort, String databaseSchema) {
            return FormatUtil.format("jdbc:mariadb://{0}:{1,number,#}/{2}", databaseHost, databasePort, databaseSchema);
        }

        @Override
        public int getDefaultPort() {
            return 3306;
        }

        @Override
        public String getDialectClass() {
            return "org.hibernate.dialect.MariaDBDialect";
        }

        @Override
        public String getDriverClass() {
            return "org.mariadb.jdbc.Driver";
        }

    };

    public static final SqlDriver MySQL = new SqlDriver() {

        @Override
        public String getConnectionUrl(String databaseHost, int databasePort, String databaseSchema) {
            return FormatUtil.format("jdbc:mysql://{0}:{1,number,#}/{2}", databaseHost, databasePort, databaseSchema);
        }

        @Override
        public int getDefaultPort() {
            return 3306;
        }

        @Override
        public String getDialectClass() {
            return "org.hibernate.dialect.MySQLDialect";
        }

        @Override
        public String getDriverClass() {
            return "com.mysql.jdbc.Driver";
        }

    };

    public static final SqlDriver OracleSQL = new SqlDriver() {

        @Override
        public String getConnectionUrl(String databaseHost, int databasePort, String databaseSchema) {
            return FormatUtil.format("jdbc:oracle:thin:@{0}:{1,number,#}:{2}", databaseHost, databasePort, databaseSchema);
        }

        @Override
        public int getDefaultPort() {
            return 1571;
        }

        @Override
        public String getDialectClass() {
            return "org.hibernate.dialect.OracleDialect";
        }

        @Override
        public String getDriverClass() {
            return "oracle.jdbc.driver.OracleDriver";
        }

    };

    public static final SqlDriver PostgreSQL = new SqlDriver() {

        @Override
        public String getConnectionUrl(String databaseHost, int databasePort, String databaseSchema) {
            return FormatUtil.format("jdbc:postgresql://{0}:{1,number,#}/{2}", databaseHost, databasePort, databaseSchema);
        }

        @Override
        public int getDefaultPort() {
            return 5432;
        }

        @Override
        public String getDialectClass() {
            return "org.hibernate.dialect.PostgreSQLDialect";
        }

        @Override
        public String getDriverClass() {
            return "org.postgresql.Driver";
        }

    };

    public static final SqlDriver SQLServer = new SqlDriver() {

        @Override
        public String getConnectionUrl(String databaseHost, int databasePort, String databaseSchema) {
            return FormatUtil.format("jdbc:microsoft:sqlserver://{0}:{1,number,#};DatabaseName={2}", databaseHost, databasePort, databaseSchema);
        }

        @Override
        public int getDefaultPort() {
            return 1433;
        }

        @Override
        public String getDialectClass() {
            return "org.hibernate.dialect.SQLServerDialect";
        }

        @Override
        public String getDriverClass() {
            return "com.microsoft.jdbc.sqlserver.SQLServerDriver";
        }

    };

    public final String getConnectionUrl(String databaseHost, String databaseSchema) {
        return this.getConnectionUrl(databaseHost, this.getDefaultPort(), databaseSchema);
    }

    public abstract String getConnectionUrl(String databaseHost, int databasePort, String databaseSchema);

    public abstract int getDefaultPort();

    public abstract String getDialectClass();

    public abstract String getDriverClass();

    public final boolean isDriverAvailable() {
        try {
            Class.forName(this.getDriverClass());
            return true;
        } catch (ClassNotFoundException cnfException) {
            return false;
        }
    }

}
