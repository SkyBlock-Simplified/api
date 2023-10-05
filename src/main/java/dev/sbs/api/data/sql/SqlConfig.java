package dev.sbs.api.data.sql;

import dev.sbs.api.data.DataConfig;
import dev.sbs.api.data.model.SqlModel;
import dev.sbs.api.util.helper.NumberUtil;
import dev.sbs.api.util.helper.ResourceUtil;
import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.Configurator;
import org.ehcache.core.Ehcache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.cache.jcache.MissingCacheStrategy;
import org.jetbrains.annotations.NotNull;

import javax.cache.expiry.Duration;
import java.util.concurrent.TimeUnit;

public class SqlConfig extends DataConfig<SqlModel> {

    @Getter @Setter protected String databaseHost = ResourceUtil.getEnv("DATABASE_HOST").orElse("");
    @Getter @Setter protected Integer databasePort = ResourceUtil.getEnv("DATABASE_PORT").map(NumberUtil::tryParseInt).orElse(0);
    @Getter @Setter protected String databaseSchema = ResourceUtil.getEnv("DATABASE_SCHEMA").orElse("");
    @Getter @Setter protected String databaseUser = ResourceUtil.getEnv("DATABASE_USER").orElse("");
    @Getter @Setter protected String databasePassword = ResourceUtil.getEnv("DATABASE_PASSWORD").orElse("");
    @Getter @Setter protected boolean databaseCachingQueries = ResourceUtil.getEnv("DATABASE_CACHING_QUERIES").map(Boolean::parseBoolean).orElse(true);
    @Getter @Setter protected CacheConcurrencyStrategy databaseCachingConcurrencyStrategy = ResourceUtil.getEnv("DATABASE_CACHING_CONCURRENCY_STRATEGY").map(CacheConcurrencyStrategy::parse).orElse(CacheConcurrencyStrategy.READ_WRITE);
    @Getter @Setter protected MissingCacheStrategy databaseCachingMissingStrategy = ResourceUtil.getEnv("DATABASE_CACHING_MISSING_STRATEGY").map(String::toLowerCase).map(MissingCacheStrategy::interpretSetting).orElse(MissingCacheStrategy.CREATE_WARN);
    @Getter protected final Duration databaseUpdateTimestampsTTL = Duration.ETERNAL;
    @Getter @Setter protected Duration databaseQueryResultsTTL = new Duration(TimeUnit.SECONDS, ResourceUtil.getEnv("DATABASE_QUERIES_TTL").map(NumberUtil::tryParseInt).orElse(30));
    @Getter @Setter protected SqlDriver databaseDriver = SqlDriver.MariaDB;

    public SqlConfig() {
        this.setLoggingLevel(Level.toLevel(ResourceUtil.getEnv("DATABASE_LOGGING").orElse(""), Level.WARN));
    }

    @Override
    protected Class<SqlModel> addDatabaseModel(@NotNull Class<SqlModel> model) {
        Configurator.setLevel(String.format("%s-%s", Ehcache.class, model.getName()), this.getLoggingLevel());
        return super.addDatabaseModel(model);
    }

    @Override
    protected void onLoggingLevelChange(@NotNull Level level) {
        // Set Logging Level
        Configurator.setLevel("org.hibernate", this.getLoggingLevel());
        Configurator.setLevel("org.ehcache", this.getLoggingLevel());
        Configurator.setLevel("com.zaxxer.hikari", this.getLoggingLevel());
        Configurator.setLevel("org.jboss.logging", this.getLoggingLevel());
        Configurator.setLevel("ch.qos.logback", this.getLoggingLevel());
        Configurator.setLevel(this.getDatabaseDriver().getDriverPackage(), this.getLoggingLevel());
        Configurator.setLevel(String.format("%s-%s", Ehcache.class, "default-update-timestamps-region"), this.getLoggingLevel());
        Configurator.setLevel(String.format("%s-%s", Ehcache.class, "default-query-results-region"), this.getLoggingLevel());

        // SQL Model Loggers
        this.models.forEach(model -> Configurator.setLevel(String.format("%s-%s", Ehcache.class, model.getName()), this.getLoggingLevel()));
    }

}
