package dev.sbs.api.data.sql;

import ch.qos.logback.classic.Level;
import dev.sbs.api.SimplifiedApi;
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

@SuppressWarnings("all")
public abstract class SqlConfig extends YamlConfig {

    @Getter
    @Setter
    protected String databaseHost = ResourceUtil.getEnv("DATABASE_HOST").orElse("");

    @Getter
    @Setter
    protected Integer databasePort = ResourceUtil.getEnv("DATABASE_PORT").map(NumberUtil::tryParseInt).orElse(0);

    @Getter
    @Setter
    protected String databaseSchema = ResourceUtil.getEnv("DATABASE_SCHEMA").orElse("");

    @Getter
    @Setter
    protected String databaseUser = ResourceUtil.getEnv("DATABASE_USER").orElse("");

    @Getter
    @Setter
    protected String databasePassword = ResourceUtil.getEnv("DATABASE_PASSWORD").orElse("");

    @Getter
    @Setter
    protected boolean databaseDebugMode = ResourceUtil.getEnv("DATABASE_DEBUG").map(Boolean::parseBoolean).orElse(false);

    @Getter
    @Setter
    protected boolean databaseCaching = ResourceUtil.getEnv("DATABASE_CACHING").map(Boolean::parseBoolean).orElse(true);

    private final ConcurrentMap<Class<? extends SqlModel>, CacheExpiry> databaseModels = Concurrent.newMap();

    @Getter
    @Setter
    protected CacheExpiry databaseUpdateTimestampsTTL = new CacheExpiry(new Duration(TimeUnit.SECONDS, ResourceUtil.getEnv("DATABASE_TIMESTAMPS_TTL").map(NumberUtil::tryParseInt).orElse(60)));

    @Getter
    @Setter
    protected CacheExpiry databaseQueryResultsTTL = new CacheExpiry(new Duration(TimeUnit.SECONDS, ResourceUtil.getEnv("DATABASE_QUERIES_TTL").map(NumberUtil::tryParseInt).orElse(60)));

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
        SimplifiedApi.getLog(FormatUtil.format("{0}-{1}", Ehcache.class, model.getName())).setLevel(this.getLoggingLevel());
        return model;
    }

    public final ConcurrentMap<Class<? extends SqlModel>, CacheExpiry> getDatabaseModels() {
        return Concurrent.newUnmodifiableMap(this.databaseModels);
    }

    public final void setLoggingLevel(Level level) {
        this.loggingLevel = level;

        // Set Logging Level
        SimplifiedApi.getLog(FormatUtil.format("{0}-{1}", Ehcache.class, "default-update-timestamps-region")).setLevel(this.getLoggingLevel());
        SimplifiedApi.getLog(FormatUtil.format("{0}-{1}", Ehcache.class, "default-query-results-region")).setLevel(this.getLoggingLevel());
        SimplifiedApi.getLog("org.hibernate").setLevel(this.getLoggingLevel());
        SimplifiedApi.getLog("org.ehcache").setLevel(this.getLoggingLevel());
        SimplifiedApi.getLog("com.zaxxer.hikari").setLevel(this.getLoggingLevel());
        SimplifiedApi.getLog("org.jboss.logging").setLevel(this.getLoggingLevel());

        // SQL Model Loggers
        this.databaseModels.forEach((model, cacheExpiry) -> SimplifiedApi.getLog(FormatUtil.format("{0}-{1}", Ehcache.class, model.getName())).setLevel(this.getLoggingLevel()));
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
