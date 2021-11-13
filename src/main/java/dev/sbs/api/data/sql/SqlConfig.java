package dev.sbs.api.data.sql;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import dev.sbs.api.data.model.SqlModel;
import dev.sbs.api.data.yaml.YamlConfig;
import dev.sbs.api.util.concurrent.Concurrent;
import dev.sbs.api.util.concurrent.ConcurrentMap;
import dev.sbs.api.util.helper.NumberUtil;
import dev.sbs.api.util.helper.ResourceUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

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
    protected boolean databaseStatistics = Boolean.parseBoolean(ResourceUtil.getEnvironmentVariable("DATABASE_STATISTICS"));

    @Getter
    @Setter
    protected boolean databaseCaching = Boolean.parseBoolean(ResourceUtil.getEnvironmentVariable("DATABASE_CACHING", "true"));

    private final ConcurrentMap<Class<? extends SqlModel>, CacheExpiry> databaseEntityTTL = Concurrent.newMap();

    @Getter
    @Setter
    protected CacheExpiry databaseUpdateTimestampsTTL = new CacheExpiry(new Duration(TimeUnit.SECONDS, NumberUtil.toInt(ResourceUtil.getEnvironmentVariable("DATABASE_TIMESTAMPS_TTL", "60"))));

    @Getter
    @Setter
    protected CacheExpiry databaseQueryResultsTTL = new CacheExpiry(new Duration(TimeUnit.SECONDS, NumberUtil.toInt(ResourceUtil.getEnvironmentVariable("DATABASE_QUERIES_TTL", "60"))));

    @Getter @Setter
    protected SqlDriver databaseDriver = SqlDriver.MariaDB;

    public SqlConfig(File configDir, String fileName, String... header) {
        super(configDir, fileName, header);
        this.setLoggingLevel(Level.WARN);
    }

    Class<? extends SqlModel> addEntityTTL(Class<? extends SqlModel> entity, CacheExpiry cacheExpiry) {
        this.databaseEntityTTL.put(entity, cacheExpiry);
        return entity;
    }

    public ConcurrentMap<Class<? extends SqlModel>, CacheExpiry> getDatabaseEntityTTL() {
        return Concurrent.newUnmodifiableMap(this.databaseEntityTTL);
    }

    public final void setLoggingLevel(Level level) {
        Logger hibernateLogger = (Logger)org.slf4j.LoggerFactory.getLogger("org.hibernate");
        Logger ehcacheLogger = (Logger)org.slf4j.LoggerFactory.getLogger("org.ehcache");
        Logger hikariLogger = (Logger)org.slf4j.LoggerFactory.getLogger("com.zaxxer.hikari");
        Logger jbossLogger = (Logger)org.slf4j.LoggerFactory.getLogger("org.jboss.logging");
        hibernateLogger.setLevel(level);
        ehcacheLogger.setLevel(level);
        hikariLogger.setLevel(level);
        jbossLogger.setLevel(level);
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
