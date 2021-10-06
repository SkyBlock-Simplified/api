package gg.sbs.api.data.sql.driver;

import gg.sbs.api.util.FormatUtil;

public class MySQLDriver extends SQLDriver {

    @Override
    public String getConnectionUrl(String databaseHost, int databasePort, String databaseSchema) {
        return FormatUtil.format("jdbc:mysql://{0}:{1,number,#}/{2}", databaseHost, databasePort, databaseSchema);
    }

    @Override
    public final int getDefaultPort() {
        return 3306;
    }

    @Override
    public String getDriverClass() {
        return "com.mysql.jdbc.Driver";
    }

}