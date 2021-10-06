package gg.sbs.api.data.sql.driver;

import gg.sbs.api.util.FormatUtil;

public class PostgreSQLDriver extends SQLDriver {

    @Override
    public String getConnectionUrl(String databaseHost, int databasePort, String databaseSchema) {
        return FormatUtil.format("jdbc:postgresql://{0}:{1,number,#}/{2}", databaseHost, databasePort, databaseSchema);
    }

    @Override
    public final int getDefaultPort() {
        return 5432;
    }

    @Override
    public String getDriverClass() {
        return "org.postgresql.Driver";
    }

}