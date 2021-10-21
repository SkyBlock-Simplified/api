package dev.sbs.api.data.sql;

import dev.sbs.api.data.yaml.YamlConfig;
import dev.sbs.api.util.helper.NumberUtil;
import dev.sbs.api.util.helper.ResourceUtil;
import lombok.Getter;
import lombok.Setter;

import java.io.File;

public abstract class SqlConfig extends YamlConfig {

    public SqlConfig(File configDir, String fileName, String... header) {
        super(configDir, fileName, header);
    }

    @Getter @Setter
    protected String databaseHost = ResourceUtil.getEnvironmentVariable("DATABASE_HOST");

    @Getter @Setter
    protected Integer databasePort = NumberUtil.toInt(ResourceUtil.getEnvironmentVariable("DATABASE_PORT"));

    @Getter @Setter
    protected String databaseSchema = ResourceUtil.getEnvironmentVariable("DATABASE_SCHEMA");

    @Getter @Setter
    protected String databaseUser = ResourceUtil.getEnvironmentVariable("DATABASE_USER");

    @Getter @Setter
    protected String databasePassword = ResourceUtil.getEnvironmentVariable("DATABASE_PASSWORD");

    @Getter @Setter
    protected boolean databaseDebugMode = Boolean.parseBoolean(ResourceUtil.getEnvironmentVariable("DATABASE_DEBUG"));

    @Getter @Setter
    protected SqlDriver databaseDriver = SqlDriver.MariaDB;

}
