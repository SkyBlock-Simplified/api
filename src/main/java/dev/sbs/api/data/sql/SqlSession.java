package dev.sbs.api.data.sql;

import ch.qos.logback.classic.Level;
import dev.sbs.api.SimplifiedException;
import dev.sbs.api.data.Repository;
import dev.sbs.api.data.model.Model;
import dev.sbs.api.data.model.SqlModel;
import dev.sbs.api.data.sql.exception.SqlException;
import dev.sbs.api.manager.service.ServiceManager;
import dev.sbs.api.manager.service.exception.UnknownServiceException;
import dev.sbs.api.reflection.Reflection;
import dev.sbs.api.util.concurrent.ConcurrentList;
import lombok.Cleanup;
import lombok.Getter;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.expiry.Duration;
import javax.cache.expiry.ModifiedExpiryPolicy;
import java.lang.reflect.ParameterizedType;
import java.util.Properties;
import java.util.function.Consumer;
import java.util.function.Function;

public final class SqlSession {

    private final ServiceManager serviceManager = new ServiceManager();
    @Getter private final SqlConfig config;
    @Getter private final ConcurrentList<Class<? extends SqlRepository<? extends SqlModel>>> repositories;
    private StandardServiceRegistry serviceRegistry;
    @Getter private SessionFactory sessionFactory;
    @Getter private boolean active;
    @Getter private boolean cached = false;
    @Getter private long initializationTime;
    @Getter private long startupTime;

    public SqlSession(SqlConfig config, ConcurrentList<Class<? extends SqlRepository<? extends SqlModel>>> repositories) {
        this.config = config;
        this.repositories = repositories;
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

    public void cacheRepositories() {
        if (!this.isCached()) {
            this.cached = true;
            long startTime = System.currentTimeMillis();

            // Provide SqlRepositories
            for (Class<? extends SqlRepository<? extends SqlModel>> repository : this.getRepositories())
                this.serviceManager.addRaw(repository, Reflection.of(repository).newInstance(this));

            this.startupTime = System.currentTimeMillis() - startTime;
        } else
            throw SimplifiedException.of(SqlException.class)
                .withMessage("Database has already cached repositories!")
                .build();
    }

    /**
     * Gets the {@link Repository <T>} caching all items of type {@link T}.
     *
     * @param tClass The {@link Model} class to find.
     * @param <T> The type of model.
     * @return The repository of type {@link T}.
     */
    @SuppressWarnings("unchecked")
    public <T extends Model> Repository<T> getRepositoryOf(Class<T> tClass) {
        if (this.isActive()) {
            return this.serviceManager.getAll(SqlRepository.class)
                .stream()
                .filter(sqlRepository -> tClass.isAssignableFrom(sqlRepository.getTClass()))
                .findFirst()
                .orElseThrow(() -> SimplifiedException.of(UnknownServiceException.class)
                    .withMessage(UnknownServiceException.getMessage(tClass))
                    .build()
                );
        } else
            throw SimplifiedException.of(SqlException.class)
                .withMessage("Database connection is not active!")
                .build();
    }

    @SuppressWarnings("unchecked")
    public void initialize() {
        if (!this.isActive()) {
            long startTime = System.currentTimeMillis();

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
            this.repositories.stream()
                .map(Class::getGenericSuperclass)
                .map(ParameterizedType.class::cast)
                .map(ParameterizedType::getActualTypeArguments)
                .map(index -> index[0])
                .map(tClass -> (Class<SqlModel>) tClass)
                .map(config::addDatabaseModel)
                .map(this::buildCacheConfiguration) // Build Entity Cache
                .forEach(sources::addAnnotatedClass);

            // Build Query Caches
            this.buildCacheConfiguration("default-update-timestamps-region", SqlConfig::getDatabaseUpdateTimestampsTTL);
            this.buildCacheConfiguration("default-query-results-region", SqlConfig::getDatabaseQueryResultsTTL);

            // Build Session Factory
            Metadata metadata = sources.getMetadataBuilder().build();
            this.sessionFactory = metadata.buildSessionFactory();
            this.active = true;
            this.initializationTime = System.currentTimeMillis() - startTime;
        } else
            throw SimplifiedException.of(SqlException.class)
                .withMessage("Database is already initialized!")
                .build();
    }

    public Session openSession() {
        return this.getSessionFactory().openSession();
    }

    public void transaction(Consumer<Session> consumer) throws SqlException {
        this.with(session -> {
            Transaction transaction = session.beginTransaction();
            consumer.accept(session);
            transaction.commit();
        });
    }

    public <R> R transaction(Function<Session, R> function) throws SqlException {
        return this.with(session -> {
            Transaction transaction = session.beginTransaction();
            R result = function.apply(session);
            transaction.commit();
            return result;
        });
    }

    public void shutdown() {
        this.active = false;
        StandardServiceRegistryBuilder.destroy(this.serviceRegistry);

        if (this.getSessionFactory() != null)
            this.getSessionFactory().close();
    }

    public void with(Consumer<Session> consumer) throws SqlException {
        try {
            @Cleanup Session session = this.openSession();
            consumer.accept(session);
        } catch (Exception exception) {
            throw SimplifiedException.of(SqlException.class)
                .withCause(exception)
                .build();
        }
    }

    public <R> R with(Function<Session, R> function) throws SqlException {
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
