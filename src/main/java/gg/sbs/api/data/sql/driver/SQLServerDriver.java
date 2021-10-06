package gg.sbs.api.data.sql.driver;

import gg.sbs.api.util.FormatUtil;

public class SQLServerDriver extends SQLDriver {

    @Override
    public String getConnectionUrl(String databaseHost, int databasePort, String databaseSchema) {
        return FormatUtil.format("jdbc:microsoft:sqlserver://{0}:{1,number,#};DatabaseName={2}", databaseHost, databasePort, databaseSchema);
    }

    @Override
    public final int getDefaultPort() {
        return 1433;
    }

    @Override
    public String getDriverClass() {
        return "com.microsoft.jdbc.sqlserver.SQLServerDriver";
    }

}