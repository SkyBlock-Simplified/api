package dev.sbs.api.data.sql;

import ch.qos.logback.classic.Level;
import dev.sbs.api.data.DataSession;
import dev.sbs.api.data.model.SqlModel;
import dev.sbs.api.data.sql.exception.SqlException;
import dev.sbs.api.util.SimplifiedException;
import dev.sbs.api.util.collection.concurrent.ConcurrentList;
import lombok.Cleanup;
import lombok.Getter;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.jetbrains.annotations.NotNull;

import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.expiry.Duration;
import javax.cache.expiry.ModifiedExpiryPolicy;
import java.util.Properties;
import java.util.function.Consumer;
import java.util.function.Function;

public final class SqlSession extends DataSession<SqlModel> {

    @Getter private final @NotNull SqlConfig config;
    private StandardServiceRegistry serviceRegistry;
    @Getter private SessionFactory sessionFactory;

    public SqlSession(@NotNull SqlConfig config, @NotNull ConcurrentList<Class<SqlModel>> models) {
        super(models);
        this.config = config;
    }

    @Override
    protected void addRepository(@NotNull Class<? extends SqlModel> model) {
        this.serviceManager.add(model, new SqlRepository<>(this, model));
    }

    private Class<SqlModel> buildCacheConfiguration(Class<SqlModel> tClass) {
        this.buildCacheConfiguration(tClass.getName(), __ -> Duration.ETERNAL);
        return tClass;
    }

    private void buildCacheConfiguration(String cacheName, Function<SqlConfig, Duration> function) {
        // Build Configuration
        MutableConfiguration<Object, Object> cacheConfiguration = new MutableConfiguration<>()
            .setStoreByValue(false)
            .setExpiryPolicyFactory(ModifiedExpiryPolicy.factoryOf(function.apply(this.getConfig())));

        // Set Configuration
        CacheManager cacheManager = Caching.getCachingProvider().getCacheManager();
        cacheManager.createCache(cacheName, cacheConfiguration);
    }

    @Override
    protected void build() {
        // Build Service Registry
        StandardServiceRegistryBuilder registryBuilder = new StandardServiceRegistryBuilder();
        Properties properties = new Properties() {{
            // Connection
            put("hibernate.dialect", config.getDatabaseDriver().getDialectClass());
            put("hibernate.connection.driver_class", config.getDatabaseDriver().getDriverClass());
            put("hibernate.connection.url", config.getDatabaseDriver().getConnectionUrl(config.getDatabaseHost(), config.getDatabasePort(), config.getDatabaseSchema()));
            put("hibernate.connection.username", config.getDatabaseUser());
            put("hibernate.connection.password", config.getDatabasePassword());
            put("hibernate.connection.provider_class", "org.hibernate.hikaricp.internal.HikariCPConnectionProvider");

            // Settings
            put("hibernate.generate_statistics", true);
            put("hibernate.globally_quoted_identifiers", true);
            put("hibernate.show_sql", config.isLoggingLevel(Level.DEBUG));
            put("hibernate.format_sql", config.isLoggingLevel(Level.TRACE)); // Log Spam
            put("hibernate.use_sql_comments", true);

            // Batching
            put("hibernate.jdbc.batch_size", 100);
            put("hibernate.jdbc.fetch_size", 400);
            put("hibernate.order_inserts", true);
            put("hibernate.order_updates", true);

            // Caching
            put("hibernate.cache.region.factory_class", "jcache");
            put("hibernate.cache.provider_class", "org.ehcache.jsr107.EhcacheCachingProvider");
            put("hibernate.cache.use_structured_entries", config.isLoggingLevel(Level.DEBUG));
            put("hibernate.cache.use_reference_entries", true);
            put("hibernate.cache.use_query_cache", config.isDatabaseCachingQueries());
            put("hibernate.cache.default_cache_concurrency_strategy", config.getDatabaseCachingConcurrencyStrategy().toAccessType().getExternalName());
            put("hibernate.javax.cache.missing_cache_strategy", config.getDatabaseCachingMissingStrategy().getExternalRepresentation());

            // Hikari
            put("hikari.maximumPoolSize", 20);
        }};
        registryBuilder.applySettings(properties);
        this.serviceRegistry = registryBuilder.build();

        // Register SqlModel Classes
        MetadataSources sources = new MetadataSources(this.serviceRegistry);
        this.models.stream()
            .map(config::addDatabaseModel)
            .map(this::buildCacheConfiguration) // Build Entity Cache
            .forEach(sources::addAnnotatedClass);

        // Build Query Caches
        this.buildCacheConfiguration("default-update-timestamps-region", SqlConfig::getDatabaseUpdateTimestampsTTL);
        this.buildCacheConfiguration("default-query-results-region", SqlConfig::getDatabaseQueryResultsTTL);

        // Build Session Factory
        Metadata metadata = sources.getMetadataBuilder().build();
        this.sessionFactory = metadata.buildSessionFactory();
    }

    public @NotNull Session openSession() {
        return this.getSessionFactory().openSession();
    }

    public void transaction(@NotNull Consumer<Session> consumer) throws SqlException {
        this.with(session -> {
            Transaction transaction = session.beginTransaction();
            consumer.accept(session);
            transaction.commit();
        });
    }

    public <R> R transaction(@NotNull Function<Session, R> function) throws SqlException {
        return this.with(session -> {
            Transaction transaction = session.beginTransaction();
            R result = function.apply(session);
            transaction.commit();
            return result;
        });
    }

    @Override
    public void shutdown() {
        super.shutdown();
        StandardServiceRegistryBuilder.destroy(this.serviceRegistry);

        if (this.getSessionFactory() != null)
            this.getSessionFactory().close();
    }

    public void with(@NotNull Consumer<Session> consumer) throws SqlException {
        try {
            @Cleanup Session session = this.openSession();
            consumer.accept(session);
        } catch (Exception exception) {
            throw SimplifiedException.of(SqlException.class)
                .withCause(exception)
                .build();
        }
    }

    public <R> R with(@NotNull Function<Session, R> function) throws SqlException {
        try {
            @Cleanup Session session = this.openSession();
            return function.apply(session);
        } catch (Exception exception) {
            throw SimplifiedException.of(SqlException.class)
                .withCause(exception)
                .build();
        }
    }

}
