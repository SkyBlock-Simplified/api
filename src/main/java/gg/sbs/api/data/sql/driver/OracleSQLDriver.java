package gg.sbs.api.data.sql.driver;

import gg.sbs.api.util.FormatUtil;

public class OracleSQLDriver extends SQLDriver {

    @Override
    public String getConnectionUrl(String databaseHost, int databasePort, String databaseSchema) {
        return FormatUtil.format("jdbc:oracle:thin:@{0}:{1,number,#}:{2}", databaseHost, databasePort, databaseSchema);
    }

    @Override
    public final int getDefaultPort() {
        return 1571;
    }

    @Override
    public String getDriverClass() {
        return "oracle.jdbc.driver.OracleDriver";
    }

}