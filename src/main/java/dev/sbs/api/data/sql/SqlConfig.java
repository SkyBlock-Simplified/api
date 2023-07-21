package dev.sbs.api.data.sql;

import ch.qos.logback.classic.Level;
import dev.sbs.api.SimplifiedApi;
import dev.sbs.api.data.model.SqlModel;
import dev.sbs.api.data.yaml.YamlConfig;
import dev.sbs.api.util.collection.concurrent.Concurrent;
import dev.sbs.api.util.collection.concurrent.ConcurrentList;
import dev.sbs.api.util.helper.FormatUtil;
import dev.sbs.api.util.helper.NumberUtil;
import dev.sbs.api.util.helper.ResourceUtil;
import lombok.Getter;
import lombok.Setter;
import org.ehcache.core.Ehcache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.cache.jcache.MissingCacheStrategy;
import org.jetbrains.annotations.NotNull;

import javax.cache.expiry.Duration;
import java.io.File;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("all")
public abstract class SqlConfig extends YamlConfig {

    private final ConcurrentList<Class<? extends SqlModel>> databaseModels = Concurrent.newList();

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
    protected boolean databaseCachingQueries = ResourceUtil.getEnv("DATABASE_CACHING_QUERIES").map(Boolean::parseBoolean).orElse(true);

    @Getter
    @Setter
    protected CacheConcurrencyStrategy databaseCachingConcurrencyStrategy = ResourceUtil.getEnv("DATABASE_CACHING_CONCURRENCY_STRATEGY").map(CacheConcurrencyStrategy::parse).orElse(CacheConcurrencyStrategy.READ_WRITE);

    @Getter
    @Setter
    protected MissingCacheStrategy databaseCachingMissingStrategy = ResourceUtil.getEnv("DATABASE_CACHING_MISSING_STRATEGY").map(String::toLowerCase).map(MissingCacheStrategy::interpretSetting).orElse(MissingCacheStrategy.CREATE_WARN);

    @Getter
    protected final Duration databaseUpdateTimestampsTTL = Duration.ETERNAL;

    @Getter
    @Setter
    protected Duration databaseQueryResultsTTL = new Duration(TimeUnit.SECONDS, ResourceUtil.getEnv("DATABASE_QUERIES_TTL").map(NumberUtil::tryParseInt).orElse(30));

    @Getter
    @Setter
    protected SqlDriver databaseDriver = SqlDriver.MariaDB;

    @Getter
    protected Level loggingLevel;

    public SqlConfig(@NotNull String fileName, @NotNull String... header) {
        this(SimplifiedApi.getCurrentDirectory(), fileName, header);
    }

    public SqlConfig(@NotNull File configDir, @NotNull String fileName, String... header) {
        super(configDir, fileName, header);
        this.setLoggingLevel(Level.toLevel(ResourceUtil.getEnv("DATABASE_LOGGING").orElse(""), Level.WARN));
    }

    Class<SqlModel> addDatabaseModel(@NotNull Class<SqlModel> model) {
        this.databaseModels.add(model);
        SimplifiedApi.getLog(FormatUtil.format("{0}-{1}", Ehcache.class, model.getName())).setLevel(this.getLoggingLevel());
        return model;
    }

    public final ConcurrentList<Class<? extends SqlModel>> getDatabaseModels() {
        return Concurrent.newUnmodifiableList(this.databaseModels);
    }

    public final boolean isLoggingLevel(@NotNull Level level) {
        return level.toInt() >= this.loggingLevel.toInt();
    }

    public final void setLoggingLevel(@NotNull Level level) {
        this.loggingLevel = level;

        // Set Logging Level
        SimplifiedApi.getLog("org.hibernate").setLevel(this.getLoggingLevel());
        SimplifiedApi.getLog("org.ehcache").setLevel(this.getLoggingLevel());
        SimplifiedApi.getLog(this.getDatabaseDriver().getDriverPackage()).setLevel(this.getLoggingLevel());
        SimplifiedApi.getLog("com.zaxxer.hikari").setLevel(this.getLoggingLevel());
        SimplifiedApi.getLog("org.jboss.logging").setLevel(this.getLoggingLevel());
        SimplifiedApi.getLog("ch.qos.logback").setLevel(this.getLoggingLevel());
        SimplifiedApi.getLog(FormatUtil.format("{0}-{1}", Ehcache.class, "default-update-timestamps-region")).setLevel(this.getLoggingLevel());
        SimplifiedApi.getLog(FormatUtil.format("{0}-{1}", Ehcache.class, "default-query-results-region")).setLevel(this.getLoggingLevel());

        // SQL Model Loggers
        this.databaseModels.forEach(model -> SimplifiedApi.getLog(FormatUtil.format("{0}-{1}", Ehcache.class, model.getName())).setLevel(this.getLoggingLevel()));
    }

}
