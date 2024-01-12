package dev.sbs.api.data.sql.driver;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;

@Getter
public class MariaDBDriver implements SqlDriver {

    private final int defaultPort = 3306;
    private final @NotNull String dialectClass = "org.hibernate.dialect.MySQLDialect";
    private final @NotNull String classPath = "com.mysql.jdbc.Driver";

    @Override
    public @NotNull String getConnectionUrl(@NotNull String databaseHost, int databasePort, @NotNull String databaseSchema) {
        return String.format("jdbc:mysql://%s:%s/%s", databaseHost, databasePort, databaseSchema);
    }

}
