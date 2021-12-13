package dev.sbs.api.data.sql;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import dev.sbs.api.data.model.SqlModel;
import dev.sbs.api.data.yaml.YamlConfig;
import dev.sbs.api.util.concurrent.Concurrent;
import dev.sbs.api.util.concurrent.ConcurrentMap;
import dev.sbs.api.util.helper.FormatUtil;
import dev.sbs.api.util.helper.NumberUtil;
import dev.sbs.api.util.helper.ResourceUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.ehcache.core.Ehcache;

import javax.cache.expiry.Duration;
import java.io.File;
import java.util.concurrent.TimeUnit;

public abstract class SqlConfig extends YamlConfig {

    @Getter
    @Setter
    protected String databaseHost = ResourceUtil.getEnvironmentVariable("DATABASE_HOST");

    @Getter
    @Setter
    protected Integer databasePort = NumberUtil.toInt(ResourceUtil.getEnvironmentVariable("DATABASE_PORT"));

    @Getter
    @Setter
    protected String databaseSchema = ResourceUtil.getEnvironmentVariable("DATABASE_SCHEMA");

    @Getter
    @Setter
    protected String databaseUser = ResourceUtil.getEnvironmentVariable("DATABASE_USER");

    @Getter
    @Setter
    protected String databasePassword = ResourceUtil.getEnvironmentVariable("DATABASE_PASSWORD");

    @Getter
    @Setter
    protected boolean databaseDebugMode = Boolean.parseBoolean(ResourceUtil.getEnvironmentVariable("DATABASE_DEBUG"));

    @Getter
    @Setter
    protected boolean databaseCaching = Boolean.parseBoolean(ResourceUtil.getEnvironmentVariable("DATABASE_CACHING", "true"));

    private final ConcurrentMap<Class<? extends SqlModel>, CacheExpiry> databaseModels = Concurrent.newMap();

    @Getter
    @Setter
    protected CacheExpiry databaseUpdateTimestampsTTL = new CacheExpiry(new Duration(TimeUnit.SECONDS, NumberUtil.toInt(ResourceUtil.getEnvironmentVariable("DATABASE_TIMESTAMPS_TTL", "60"))));

    @Getter
    @Setter
    protected CacheExpiry databaseQueryResultsTTL = new CacheExpiry(new Duration(TimeUnit.SECONDS, NumberUtil.toInt(ResourceUtil.getEnvironmentVariable("DATABASE_QUERIES_TTL", "60"))));

    @Getter
    @Setter
    protected SqlDriver databaseDriver = SqlDriver.MariaDB;

    @Getter
    protected Level loggingLevel;

    public SqlConfig(File configDir, String fileName, String... header) {
        super(configDir, fileName, header);
        this.setLoggingLevel(this.isDatabaseDebugMode() ? Level.DEBUG : Level.WARN);
    }

    Class<? extends SqlModel> addDatabaseModel(Class<? extends SqlModel> model, CacheExpiry cacheExpiry) {
        this.databaseModels.put(model, cacheExpiry);
        this.getLogger(FormatUtil.format("{0}-{1}", Ehcache.class, model.getName())).setLevel(this.getLoggingLevel());
        return model;
    }

    public final ConcurrentMap<Class<? extends SqlModel>, CacheExpiry> getDatabaseModels() {
        return Concurrent.newUnmodifiableMap(this.databaseModels);
    }

    private Logger getLogger(String loggerName) {
        return (Logger) org.slf4j.LoggerFactory.getLogger(loggerName);
    }

    public final void setLoggingLevel(Level level) {
        this.loggingLevel = level;

        // Set Logging Level
        this.getLogger(FormatUtil.format("{0}-{1}", Ehcache.class, "default-update-timestamps-region")).setLevel(this.getLoggingLevel());
        this.getLogger(FormatUtil.format("{0}-{1}", Ehcache.class, "default-query-results-region")).setLevel(this.getLoggingLevel());
        this.getLogger("org.hibernate").setLevel(this.getLoggingLevel());
        this.getLogger("org.ehcache").setLevel(this.getLoggingLevel());
        this.getLogger("com.zaxxer.hikari").setLevel(this.getLoggingLevel());
        this.getLogger("org.jboss.logging").setLevel(this.getLoggingLevel());

        // SQL Model Loggers
        this.databaseModels.forEach((model, cacheExpiry) -> this.getLogger(FormatUtil.format("{0}-{1}", Ehcache.class, model.getName())).setLevel(this.getLoggingLevel()));
    }

    @AllArgsConstructor
    public static final class CacheExpiry {

        @Getter
        @Setter
        private Duration creation;

        @Getter
        @Setter
        private Duration access = null;

        @Getter
        @Setter
        private Duration update = Duration.ZERO;

        public CacheExpiry(Duration creation) {
            this.creation = creation;
        }

    }

}
