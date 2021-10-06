package gg.sbs.api.data.sql.driver;

public abstract class SQLDriver {

    public final String getConnectionUrl(String databaseHost, String databaseSchema) {
        return this.getConnectionUrl(databaseHost, this.getDefaultPort(), databaseSchema);
    }

    public abstract String getConnectionUrl(String databaseHost, int databasePort, String databaseSchema);

    public abstract int getDefaultPort();

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