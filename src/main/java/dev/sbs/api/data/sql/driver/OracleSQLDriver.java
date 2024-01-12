package dev.sbs.api.data.sql.driver;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;

@Getter
public class OracleSQLDriver implements SqlDriver {

    private final int defaultPort = 1571;
    private final @NotNull String dialectClass = "org.hibernate.dialect.OracleDialect";
    private final @NotNull String classPath = "oracle.jdbc.driver.OracleDriver";

    @Override
    public @NotNull String getConnectionUrl(@NotNull String databaseHost, int databasePort, @NotNull String databaseSchema) {
        return String.format("jdbc:oracle:thin:@%s:%s:%s", databaseHost, databasePort, databaseSchema);
    }

}
