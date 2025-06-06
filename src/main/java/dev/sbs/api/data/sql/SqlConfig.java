package dev.sbs.api.data.sql;

import dev.sbs.api.data.DataConfig;
import dev.sbs.api.data.DataSession;
import dev.sbs.api.data.model.SqlModel;
import dev.sbs.api.data.sql.driver.MariaDBDriver;
import dev.sbs.api.data.sql.driver.SqlDriver;
import dev.sbs.api.data.yaml.annotation.Flag;
import dev.sbs.api.reflection.Reflection;
import dev.sbs.api.util.NumberUtil;
import dev.sbs.api.util.SystemUtil;
import dev.sbs.api.builder.ClassBuilder;
import dev.sbs.api.builder.annotation.BuildFlag;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.spi.StandardLevel;
import org.ehcache.core.Ehcache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.cache.jcache.MissingCacheStrategy;
import org.jetbrains.annotations.NotNull;

import javax.cache.expiry.Duration;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class SqlConfig extends DataConfig<SqlModel> {

    private @NotNull SqlDriver driver;
    @Flag(secure = true)
    private @NotNull String host;
    @Flag(secure = true)
    private int port;
    @Flag(secure = true)
    private @NotNull String schema;
    @Flag(secure = true)
    private @NotNull String user;
    @Flag(secure = true)
    private @NotNull String password;
    private boolean usingQueryCache;
    private boolean using2ndLevelCache;
    private boolean usingStatistics;
    private @NotNull CacheConcurrencyStrategy cacheConcurrencyStrategy;
    private @NotNull MissingCacheStrategy missingCacheStrategy;
    private @NotNull Duration queryResultsTTL;

    public static @NotNull Builder builder() {
        return new Builder();
    }

    @Override
    protected @NotNull DataSession<SqlModel> createSession() {
        return new SqlSession(this);
    }

    public static @NotNull SqlConfig defaultSql() {
        return builder()
            .withDriver(new MariaDBDriver())
            .withHost(SystemUtil.getEnv("DATABASE_HOST"))
            .withPort(SystemUtil.getEnv("DATABASE_PORT").map(NumberUtil::tryParseInt))
            .withSchema(SystemUtil.getEnv("DATABASE_SCHEMA"))
            .withUser(SystemUtil.getEnv("DATABASE_USER"))
            .withPassword(SystemUtil.getEnv("DATABASE_PASSWORD"))
            .withLogLevel(StandardLevel.WARN)
            .isUsingQueryCache()
            .isUsing2ndLevelCache()
            .withCacheConcurrencyStrategy(CacheConcurrencyStrategy.READ_WRITE)
            .withMissingCacheStrategy(MissingCacheStrategy.CREATE_WARN)
            .withQueryResultsTTL(30)
            .build();
    }

    @Override
    protected void onLogLevelChange(@NotNull Level level) {
        // Set Logging Level
        Configurator.setLevel("org.hibernate", this.getLogLevel());
        Configurator.setLevel("org.ehcache", this.getLogLevel());
        Configurator.setLevel("com.zaxxer.hikari", this.getLogLevel());
        Configurator.setLevel("org.jboss.logging", this.getLogLevel());
        Configurator.setLevel("ch.qos.logback", this.getLogLevel());
        Configurator.setLevel(this.getDriver().getClassPath(), this.getLogLevel());
        Configurator.setLevel(String.format("%s-%s", Ehcache.class, "default-update-timestamps-region"), this.getLogLevel());
        Configurator.setLevel(String.format("%s-%s", Ehcache.class, "default-query-results-region"), this.getLogLevel());

        // SQL Model Loggers
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
        private StandardLevel logLevel = StandardLevel.WARN;
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

        public Builder withLogLevel(@NotNull StandardLevel level) {
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
                new Duration(TimeUnit.SECONDS, this.queryResultsTTL.orElseThrow())
            );

            sqlConfig.setLogLevel(Level.toLevel(this.logLevel.name()));
            return sqlConfig;
        }

    }

}
