package dev.sbs.api.data.sql;

import dev.sbs.api.data.model.SqlModel;
import dev.sbs.api.data.sql.exception.SqlException;
import dev.sbs.api.data.sql.function.ReturnSessionFunction;
import dev.sbs.api.data.sql.function.VoidSessionFunction;
import dev.sbs.api.manager.service.ServiceManager;
import dev.sbs.api.reflection.Reflection;
import dev.sbs.api.util.concurrent.ConcurrentList;
import lombok.Getter;
import org.ehcache.jsr107.EhcacheCachingProvider;
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
import javax.cache.expiry.ExpiryPolicy;
import javax.cache.spi.CachingProvider;
import java.lang.reflect.ParameterizedType;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

@SuppressWarnings("unchecked")
public final class SqlSession {

    private final ServiceManager serviceManager = new ServiceManager();
    @Getter
    private final SqlConfig config;
    @Getter
    private final ConcurrentList<Class<? extends SqlRepository<? extends SqlModel>>> repositories;
    private StandardServiceRegistry serviceRegistry;
    @Getter
    private SessionFactory sessionFactory;
    @Getter
    private boolean active;

    @Getter
    private boolean cached = false;

    @Getter
    private long initializationTime;

    @Getter
    private long startupTime;

    public SqlSession(SqlConfig config, ConcurrentList<Class<? extends SqlRepository<? extends SqlModel>>> repositories) {
        this.config = config;
        this.repositories = repositories;
    }

    private void buildCacheConfiguration(String cacheName, Function<SqlConfig, SqlConfig.CacheExpiry> function) {
        MutableConfiguration<Long, String> defaultConfiguration = new MutableConfiguration<>();
        defaultConfiguration.setExpiryPolicyFactory(() -> new ExpiryPolicy() {

            @Override
            public Duration getExpiryForCreation() {
                return function.apply(getConfig()).getCreation();
            }

            @Override
            public Duration getExpiryForAccess() {
                return function.apply(getConfig()).getAccess();
            }

            @Override
            public Duration getExpiryForUpdate() {
                return function.apply(getConfig()).getUpdate();
            }

        });

        this.setCacheConfiguration(cacheName, defaultConfiguration);
    }

    private Class<?> buildCacheConfiguration(Class<? extends SqlModel> tClass) {
        MutableConfiguration<Long, String> defaultConfiguration = new MutableConfiguration<>();
        defaultConfiguration.setExpiryPolicyFactory(() -> new ExpiryPolicy() {

            @Override
            public Duration getExpiryForCreation() {
                return getConfig().getDatabaseModels().get(tClass).getCreation();
            }

            @Override
            public Duration getExpiryForAccess() {
                return getConfig().getDatabaseModels().get(tClass).getAccess();
            }

            @Override
            public Duration getExpiryForUpdate() {
                return getConfig().getDatabaseModels().get(tClass).getUpdate();
            }

        });

        this.setCacheConfiguration(tClass.getName(), defaultConfiguration);
        return tClass;
    }

    public void cacheRepositories() {
        if (!this.isCached()) {
            this.cached = true;
            long startTime = System.currentTimeMillis();

            // Provide SqlRepositories
            for (Class<? extends SqlRepository<? extends SqlModel>> repository : this.getRepositories())
                serviceManager.addRaw(repository, new Reflection(repository).newInstance(this));

            this.startupTime = System.currentTimeMillis() - startTime;
        } else
            throw new SqlException("Database has already cached repositories!");
    }

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

                // SQL
                put("hibernate.generate_statistics", config.isDatabaseDebugMode());
                put("hibernate.show_sql", false);
                put("hibernate.format_sql", false); // Log Spam
                put("hibernate.use_sql_comments", true);
                put("hibernate.order_inserts", true);
                put("hibernate.order_updates", true);

                // Prepared Statements
                put("hikari.cachePrepStmts", true);
                put("hikari.prepStmtCacheSize", 256);
                put("hikari.prepStmtCacheSqlLimit", 2048);
                put("hikari.useServerPrepStmts", true);

                // Caching
                put("hibernate.cache.use_second_level_cache", config.isDatabaseCaching());
                put("hibernate.cache.use_query_cache", config.isDatabaseCaching());
                put("hibernate.cache.region.factory_class", "org.hibernate.cache.jcache.JCacheRegionFactory");
                put("hibernate.cache.provider_class", "org.ehcache.jsr107.EhcacheCachingProvider");
                put("hibernate.cache.use_structured_entries", config.isDatabaseDebugMode());
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
                .map(tClass -> {
                    SqlConfig.CacheExpiry cacheExpiry;

                    // Load Custom Cache Expiry
                    if (tClass.isAnnotationPresent(SqlCacheExpiry.class)) {
                        SqlCacheExpiry sqlCacheExpiry = tClass.getAnnotation(SqlCacheExpiry.class);

                        cacheExpiry = new SqlConfig.CacheExpiry(
                            new Duration(TimeUnit.SECONDS, sqlCacheExpiry.creation()),
                            new Duration(TimeUnit.SECONDS, sqlCacheExpiry.access()),
                            new Duration(TimeUnit.SECONDS, sqlCacheExpiry.update())
                        );
                    } else
                        cacheExpiry = new SqlConfig.CacheExpiry(Duration.ONE_MINUTE);

                    return config.addDatabaseModel(tClass, cacheExpiry);
                })
                .map(this::buildCacheConfiguration)
                .forEach(sources::addAnnotatedClass);

            // Build Default Caches
            this.buildCacheConfiguration("default-update-timestamps-region", SqlConfig::getDatabaseUpdateTimestampsTTL);
            this.buildCacheConfiguration("default-query-results-region", SqlConfig::getDatabaseQueryResultsTTL);

            // Build Session Factory
            Metadata metadata = sources.getMetadataBuilder().build();
            this.sessionFactory = metadata.buildSessionFactory();
            this.active = true;
            this.initializationTime = System.currentTimeMillis() - startTime;
        } else
            throw new SqlException("Database is already initialized!");
    }

    public Session openSession() {
        return this.sessionFactory.openSession();
    }

    private void setCacheConfiguration(String cacheName, MutableConfiguration<Long, String> defaultConfiguration) {
        CachingProvider cachingProvider = Caching.getCachingProvider();
        EhcacheCachingProvider ehcacheProvider = (EhcacheCachingProvider) cachingProvider;
        CacheManager cacheManager = ehcacheProvider.getCacheManager();
        cacheManager.createCache(cacheName, defaultConfiguration);
    }

    public void transaction(VoidSessionFunction function) throws SqlException {
        try {
            Session session = this.openSession();
            Transaction transaction = session.beginTransaction();
            function.accept(session);
            session.flush();
            transaction.commit();
            session.close();
        } catch (Exception exception) {
            throw new SqlException(exception);
        }
    }

    public <S> S transaction(ReturnSessionFunction<S> function) throws SqlException {
        try {
            Session session = this.openSession();
            Transaction transaction = session.beginTransaction();
            S result = function.apply(session);
            session.flush();
            transaction.commit();
            session.close();
            return result;
        } catch (Exception exception) {
            throw new SqlException(exception);
        }
    }

    public void shutdown() {
        this.active = false;
        StandardServiceRegistryBuilder.destroy(this.serviceRegistry);

        if (this.sessionFactory != null)
            this.sessionFactory.close();
    }

    public void with(VoidSessionFunction function) throws SqlException {
        try {
            Session session = this.openSession();
            function.accept(session);
            session.close();
        } catch (Exception exception) {
            throw new SqlException(exception);
        }
    }

    public <R> R with(Function<Session, R> function) throws SqlException {
        try {
            Session session = this.openSession();
            R result = function.apply(session);
            session.close();
            return result;
        } catch (Exception exception) {
            throw new SqlException(exception);
        }
    }

    public <S> S with(ReturnSessionFunction<S> function) throws SqlException {
        try {
            Session session = this.openSession();
            S result = function.apply(session);
            session.close();
            return result;
        } catch (Exception exception) {
            throw new SqlException(exception);
        }
    }

}
