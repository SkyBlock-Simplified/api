package dev.sbs.api.data.sql.driver;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;

@Getter
public class SQLServerDriver implements SqlDriver {

    private final int defaultPort = 1433;
    private final @NotNull String dialectClass = "org.hibernate.dialect.SQLServerDialect";
    private final @NotNull String classPath = "com.microsoft.jdbc.sqlserver.SQLServerDriver";

    @Override
    public @NotNull String getConnectionUrl(@NotNull String databaseHost, int databasePort, @NotNull String databaseSchema) {
        return String.format("jdbc:microsoft:sqlserver://%s:%s;DatabaseName=%s", databaseHost, databasePort, databaseSchema);
    }

}
