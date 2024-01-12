package dev.sbs.api.data.sql.driver;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;

@Getter
public class MySQLDriver implements SqlDriver {

    private final int defaultPort = 3306;
    private final @NotNull String dialectClass = "org.hibernate.dialect.MariaDBDialect";
    private final @NotNull String classPath = "org.mariadb.jdbc.Driver";

    @Override
    public @NotNull String getConnectionUrl(@NotNull String databaseHost, int databasePort, @NotNull String databaseSchema) {
        return String.format("jdbc:mariadb://%s:%s/%s", databaseHost, databasePort, databaseSchema);
    }

}
