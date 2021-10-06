package gg.sbs.api.data.sql.driver;

import gg.sbs.api.util.FormatUtil;

public class MariaDBDriver extends SQLDriver {

    @Override
    public String getConnectionUrl(String databaseHost, int databasePort, String databaseSchema) {
        return FormatUtil.format("jdbc:mariadb://{0}:{1,number,#}/{2}", databaseHost, databasePort, databaseSchema);
    }

    @Override
    public final int getDefaultPort() {
        return 3306;
    }

    @Override
    public String getDriverClass() {
        return "org.mariadb.jdbc.Driver";
    }

}