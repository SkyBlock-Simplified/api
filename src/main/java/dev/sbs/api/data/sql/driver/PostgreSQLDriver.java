package dev.sbs.api.data.sql.driver;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;

@Getter
public class PostgreSQLDriver implements SqlDriver {

    private final int defaultPort = 5432;
    private final @NotNull String dialectClass = "org.hibernate.dialect.PostgreSQLDialect";
    private final @NotNull String classPath = "org.postgresql.Driver";

    @Override
    public @NotNull String getConnectionUrl(@NotNull String databaseHost, int databasePort, @NotNull String databaseSchema) {
        return String.format("jdbc:postgresql://%s:%s/%s", databaseHost, databasePort, databaseSchema);
    }

}
