package dev.sbs.api.data.sql;

import dev.sbs.api.builder.ClassBuilder;
import dev.sbs.api.builder.annotation.BuildFlag;
import dev.sbs.api.collection.concurrent.ConcurrentList;
import dev.sbs.api.data.DataConfig;
import dev.sbs.api.data.DataSession;
import dev.sbs.api.data.DataType;
import dev.sbs.api.data.json.JsonConfig;
import dev.sbs.api.data.sql.driver.MariaDBDriver;
import dev.sbs.api.data.sql.driver.SqlDriver;
import dev.sbs.api.data.yaml.annotation.Flag;
import dev.sbs.api.reflection.Reflection;
import dev.sbs.api.util.NumberUtil;
import dev.sbs.api.util.SystemUtil;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.Configurator;
import org.ehcache.core.Ehcache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.cache.jcache.MissingCacheStrategy;
import org.jetbrains.annotations.NotNull;

import javax.cache.expiry.Duration;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class SqlConfig implements DataConfig<SqlModel> {

    private final @NotNull ConcurrentList<Class<SqlModel>> models;
    private final @NotNull String identifier;
    private final @NotNull SqlDriver driver;
    @Flag(secure = true)
    private final @NotNull String host;
    @Flag(secure = true)
    private final int port;
    @Flag(secure = true)
    private final @NotNull String schema;
    @Flag(secure = true)
    private final @NotNull String user;
    @Flag(secure = true)
    private final @NotNull String password;
    private final boolean usingQueryCache;
    private final boolean using2ndLevelCache;
    private final boolean usingStatistics;
    private final @NotNull CacheConcurrencyStrategy cacheConcurrencyStrategy;
    private final @NotNull MissingCacheStrategy missingCacheStrategy;
    private final @NotNull Duration queryResultsTTL;
    private @NotNull Level logLevel;

    public static @NotNull Builder builder() {
        return new Builder();
    }

    @Override
    public @NotNull DataSession<SqlModel> createSession() {
        return new SqlSession(this);
    }

    @Override
    public @NotNull DataType getType() {
        return DataType.SQL;
    }

    public static @NotNull SqlConfig defaultSql() {
        return builder()
            .withDriver(new MariaDBDriver())
            .withHost(SystemUtil.getEnv("DATABASE_HOST"))
            .withPort(SystemUtil.getEnv("DATABASE_PORT").map(NumberUtil::tryParseInt))
            .withSchema(SystemUtil.getEnv("DATABASE_SCHEMA"))
            .withUser(SystemUtil.getEnv("DATABASE_USER"))
            .withPassword(SystemUtil.getEnv("DATABASE_PASSWORD"))
            .withLogLevel(Level.WARN)
            .isUsingQueryCache()
            .isUsing2ndLevelCache()
            .withCacheConcurrencyStrategy(CacheConcurrencyStrategy.READ_WRITE)
            .withMissingCacheStrategy(MissingCacheStrategy.CREATE_WARN)
            .withQueryResultsTTL(30)
            .build();
    }

    @Override
    public void setLogLevel(@NotNull Level logLevel) {
        this.logLevel = logLevel;

        // SQL Level
        Configurator.setLevel("org.hibernate", this.getLogLevel());
        Configurator.setLevel("org.ehcache", this.getLogLevel());
        Configurator.setLevel("com.zaxxer.hikari", this.getLogLevel());
        Configurator.setLevel("org.jboss.logging", this.getLogLevel());
        Configurator.setLevel("ch.qos.logback", this.getLogLevel());
        Configurator.setLevel(this.getDriver().getClassPath(), this.getLogLevel());
        Configurator.setLevel(String.format("%s-%s", Ehcache.class, "default-update-timestamps-region"), this.getLogLevel());
        Configurator.setLevel(String.format("%s-%s", Ehcache.class, "default-query-results-region"), this.getLogLevel());

        // SQL Model Level
        this.getModels().forEach(model -> Configurator.setLevel(String.format("%s-%s", Ehcache.class, model.getName()), this.getLogLevel()));
    }

    public static class Builder implements ClassBuilder<SqlConfig> {

        // Required Settings
        @BuildFlag(nonNull = true)
        private SqlDriver driver = new MariaDBDriver();
        @BuildFlag(nonNull = true)
        private Optional<String> host = Optional.empty();
        @BuildFlag(nonNull = true, pattern = "[\\d]+")
        private Optional<Integer> port = Optional.empty();
        @BuildFlag(nonNull = true)
        private Optional<String> schema = Optional.empty();
        @BuildFlag(nonNull = true)
        private Optional<String> user = Optional.empty();
        @BuildFlag(nonNull = true)
        private Optional<String> password = Optional.empty();

        @BuildFlag(nonNull = true)
        private Level logLevel = Level.WARN;
        private boolean usingQueryCache = false;
        private boolean using2ndLevelCache = false;
        private boolean usingStatistics = false;
        @BuildFlag(nonNull = true)
        private CacheConcurrencyStrategy cacheConcurrencyStrategy = CacheConcurrencyStrategy.NONE;
        @BuildFlag(nonNull = true)
        private MissingCacheStrategy missingCacheStrategy = MissingCacheStrategy.FAIL;
        @BuildFlag(nonNull = true)
        private Optional<Long> queryResultsTTL = Optional.of(0L);

        public Builder isUsing2ndLevelCache() {
            return this.isUsing2ndLevelCache(true);
        }

        public Builder isUsing2ndLevelCache(boolean value) {
            this.using2ndLevelCache = value;
            return this;
        }

        public Builder isUsingQueryCache() {
            return this.isUsingQueryCache(true);
        }

        public Builder isUsingQueryCache(boolean value) {
            this.usingQueryCache = value;
            return this;
        }

        public Builder isUsingStatistics() {
            return this.isUsingStatistics(true);
        }

        public Builder isUsingStatistics(boolean value) {
            this.usingStatistics = value;
            return this;
        }

        public Builder withCacheConcurrencyStrategy(@NotNull CacheConcurrencyStrategy cacheConcurrencyStrategy) {
            this.cacheConcurrencyStrategy = cacheConcurrencyStrategy;
            return this;
        }

        public Builder withDriver(@NotNull SqlDriver driver) {
            this.driver = driver;
            return this;
        }

        public Builder withHost(@NotNull String host) {
            return this.withHost(Optional.of(host));
        }

        public Builder withHost(@NotNull Optional<String> host) {
            this.host = host;
            return this;
        }

        public Builder withLogLevel(@NotNull Level level) {
            this.logLevel = level;
            return this;
        }

        public Builder withMissingCacheStrategy(@NotNull MissingCacheStrategy missingCacheStrategy) {
            this.missingCacheStrategy = missingCacheStrategy;
            return this;
        }

        public Builder withPassword(@NotNull String password) {
            return this.withPassword(Optional.of(password));
        }

        public Builder withPassword(@NotNull Optional<String> password) {
            this.password = password;
            return this;
        }

        public Builder withPort(int port) {
            return this.withPort(Optional.of(port));
        }

        public Builder withPort(@NotNull Optional<Integer> port) {
            this.port = port;
            return this;
        }

        public Builder withQueryResultsTTL(long queryResultsTTL) {
            return this.withQueryResultsTTL(Optional.of(queryResultsTTL));
        }

        public Builder withQueryResultsTTL(@NotNull Optional<Long> queryResultsTTL) {
            this.queryResultsTTL = queryResultsTTL;
            return this;
        }

        public Builder withSchema(@NotNull String schema) {
            return this.withSchema(Optional.of(schema));
        }

        public Builder withSchema(@NotNull Optional<String> schema) {
            this.schema = schema;
            return this;
        }

        public Builder withUser(@NotNull String user) {
            return this.withUser(Optional.of(user));
        }

        public Builder withUser(@NotNull Optional<String> user) {
            this.user = user;
            return this;
        }

        @Override
        public @NotNull SqlConfig build() {
            Reflection.validateFlags(this);

            SqlConfig sqlConfig = new SqlConfig(
                DataConfig.detectModels(Reflection.getSuperClass(JsonConfig.class)),
                UUID.randomUUID().toString(),
                this.driver,
                this.host.orElseThrow(),
                this.port.orElseThrow(),
                this.schema.orElseThrow(),
                this.user.orElseThrow(),
                this.password.orElseThrow(),
                this.usingQueryCache,
                this.using2ndLevelCache,
                this.usingStatistics,
                this.cacheConcurrencyStrategy,
                this.missingCacheStrategy,
                new Duration(TimeUnit.SECONDS, this.queryResultsTTL.orElseThrow()),
                this.logLevel
            );

            sqlConfig.setLogLevel(this.logLevel);
            return sqlConfig;
        }

    }

}
