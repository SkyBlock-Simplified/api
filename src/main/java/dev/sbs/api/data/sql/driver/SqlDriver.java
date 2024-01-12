package dev.sbs.api.data.sql.driver;

import org.jetbrains.annotations.NotNull;

public interface SqlDriver {

    default @NotNull String getConnectionUrl(@NotNull String databaseHost, @NotNull String databaseSchema) {
        return this.getConnectionUrl(databaseHost, this.getDefaultPort(), databaseSchema);
    }

    @NotNull String getConnectionUrl(@NotNull String databaseHost, int databasePort, @NotNull String databaseSchema);

    int getDefaultPort();

    @NotNull String getDialectClass();

    @NotNull String getClassPath();

    default boolean isAvailable() {
        try {
            Class.forName(this.getClassPath());
            return true;
        } catch (ClassNotFoundException cnfException) {
            return false;
        }
    }

}
