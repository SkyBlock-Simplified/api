package dev.sbs.api.data.sql;

public abstract class SqlDriver {

    public static final SqlDriver MariaDB = new SqlDriver() {

        @Override
        public String getConnectionUrl(String databaseHost, int databasePort, String databaseSchema) {
            return String.format("jdbc:mariadb://%s:%s/%s", databaseHost, databasePort, databaseSchema);
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
        public String getDriverClassName() {
            return "Driver";
        }

        @Override
        public String getDriverPackage() {
            return "org.mariadb.jdbc";
        }

    };

    public static final SqlDriver MySQL = new SqlDriver() {

        @Override
        public String getConnectionUrl(String databaseHost, int databasePort, String databaseSchema) {
            return String.format("jdbc:mysql://%s:%s/%s", databaseHost, databasePort, databaseSchema);
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
        public String getDriverClassName() {
            return "Driver";
        }

        @Override
        public String getDriverPackage() {
            return "com.mysql.jdbc";
        }

    };

    public static final SqlDriver OracleSQL = new SqlDriver() {

        @Override
        public String getConnectionUrl(String databaseHost, int databasePort, String databaseSchema) {
            return String.format("jdbc:oracle:thin:@%s:%s:%s", databaseHost, databasePort, databaseSchema);
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
        public String getDriverClassName() {
            return "OracleDriver";
        }

        @Override
        public String getDriverPackage() {
            return "oracle.jdbc.driver";
        }

    };

    public static final SqlDriver PostgreSQL = new SqlDriver() {

        @Override
        public String getConnectionUrl(String databaseHost, int databasePort, String databaseSchema) {
            return String.format("jdbc:postgresql://%s:%s/%s", databaseHost, databasePort, databaseSchema);
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
        public String getDriverClassName() {
            return "Driver";
        }

        @Override
        public String getDriverPackage() {
            return "org.postgresql";
        }

    };

    public static final SqlDriver SQLServer = new SqlDriver() {

        @Override
        public String getConnectionUrl(String databaseHost, int databasePort, String databaseSchema) {
            return String.format("jdbc:microsoft:sqlserver://%s:%s;DatabaseName=%s", databaseHost, databasePort, databaseSchema);
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
        public String getDriverClassName() {
            return "SQLServerDriver";
        }

        @Override
        public String getDriverPackage() {
            return "com.microsoft.jdbc.sqlserver";
        }

    };

    public final String getConnectionUrl(String databaseHost, String databaseSchema) {
        return this.getConnectionUrl(databaseHost, this.getDefaultPort(), databaseSchema);
    }

    public abstract String getConnectionUrl(String databaseHost, int databasePort, String databaseSchema);

    public abstract int getDefaultPort();

    public abstract String getDialectClass();

    public final String getDriverClass() {
        return String.format("%s.%s", this.getDriverPackage(), this.getDriverClassName());
    }

    public abstract String getDriverClassName();

    public abstract String getDriverPackage();

    public final boolean isDriverAvailable() {
        try {
            Class.forName(this.getDriverClass());
            return true;
        } catch (ClassNotFoundException cnfException) {
            return false;
        }
    }

}
