package dev.sbs.api.data.sql;

import dev.sbs.api.data.model.SqlModel;
import dev.sbs.api.data.sql.function.ReturnSessionFunction;
import dev.sbs.api.data.sql.function.VoidSessionFunction;
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

public final class SqlSession {

    private final StandardServiceRegistry serviceRegistry;
    @Getter private final SessionFactory sessionFactory;
    @Getter private final SqlConfig config;
    @Getter private final ConcurrentList<Class<? extends SqlRepository<? extends SqlModel>>> repositories;

    public SqlSession(SqlConfig config, ConcurrentList<Class<? extends SqlRepository<? extends SqlModel>>> repositories) {
        this.config = config;
        this.repositories = repositories;

        // Build Service Registry
        StandardServiceRegistryBuilder registryBuilder = new StandardServiceRegistryBuilder();
        Properties properties = new Properties() {{
            // Connection
            put("connection.driver_class", config.getDatabaseDriver().getDriverClass());
            put("hibernate.generate_statistics", config.isDatabaseStatistics());
            put("hibernate.connection.url", config.getDatabaseDriver().getConnectionUrl(config.getDatabaseHost(), config.getDatabasePort(), config.getDatabaseSchema()));
            put("hibernate.connection.username", config.getDatabaseUser());
            put("hibernate.connection.password", config.getDatabasePassword());
            put("hibernate.dialect", config.getDatabaseDriver().getDialectClass());
            put("hibernate.connection.provider_class", "org.hibernate.hikaricp.internal.HikariCPConnectionProvider");

            // SQL
            put("hibernate.show_sql", false);
            put("hibernate.format_sql", false); // Log Spam
            put("hibernate.use_sql_comments", true);
            put("hibernate.order_inserts", true);

            // Caching
            put("hibernate.cache.use_second_level_cache", config.isDatabaseCaching());
            put("hibernate.cache.use_query_cache", config.isDatabaseCaching());
            put("hibernate.cache.region.factory_class", "org.hibernate.cache.jcache.JCacheRegionFactory");
            put("hibernate.javax.cache.provider", "org.ehcache.jsr107.EhcacheCachingProvider");

            // Prepared Statements
            put("hikari.cachePrepStmts", true);
            put("hikari.prepStmtCacheSize", 256);
            put("hikari.prepStmtCacheSqlLimit", 2048);
            put("hikari.useServerPrepStmts", true);
        }};
        registryBuilder.applySettings(properties);
        this.serviceRegistry = registryBuilder.build();

        // Add SqlModel classes to Registry
        MetadataSources sources = new MetadataSources(this.serviceRegistry);
        this.repositories.stream()
                .map(Class::getGenericSuperclass)
                .map(ParameterizedType.class::cast)
                .map(ParameterizedType::getActualTypeArguments)
                .map(index -> index[0])
                .map(Class.class::cast)
                .map(this::buildCacheConfiguration)
                .forEach(sources::addAnnotatedClass);

        // Initialize Default Caches
        this.buildCacheConfiguration("default-update-timestamps-region");
        this.buildCacheConfiguration("default-query-results-region");

        // Build Session Factory
        Metadata metadata = sources.getMetadataBuilder().build();
        this.sessionFactory = metadata.buildSessionFactory();
    }

    private void buildCacheConfiguration(String className) {
        MutableConfiguration<Long, String> defaultConfiguration = new MutableConfiguration<>();
        defaultConfiguration.setExpiryPolicyFactory(() -> new ExpiryPolicy() {

            @Override
            public Duration getExpiryForCreation() {
                return Duration.ONE_MINUTE; // TODO: Custom Cache Expiration
            }

            @Override
            public Duration getExpiryForAccess() {
                return null;
            }

            @Override
            public Duration getExpiryForUpdate() {
                return null;
            }

        });

        CachingProvider cachingProvider = Caching.getCachingProvider();
        EhcacheCachingProvider ehcacheProvider = (EhcacheCachingProvider) cachingProvider;
        CacheManager cacheManager = ehcacheProvider.getCacheManager();
        cacheManager.createCache(className, defaultConfiguration);
    }

    private Class<?> buildCacheConfiguration(Class<?> tClass) {
        this.buildCacheConfiguration(tClass.getName());
        return tClass;
    }

    public Session openSession() {
        return this.sessionFactory.openSession();
    }

    public void transaction(VoidSessionFunction function) {
        Session session = this.openSession();
        Transaction transaction = session.beginTransaction();
        function.accept(session);
        session.flush();
        transaction.commit();
        session.close();
    }

    public <S> S transaction(ReturnSessionFunction<S> function) {
        Session session = this.openSession();
        Transaction transaction = session.beginTransaction();
        S result = function.apply(session);
        session.flush();
        transaction.commit();
        session.close();
        return result;
    }

    public void shutdown() {
        StandardServiceRegistryBuilder.destroy(this.serviceRegistry);

        if (this.sessionFactory != null)
            this.sessionFactory.close();
    }

    public void with(VoidSessionFunction function) {
        Session session = this.openSession();
        function.accept(session);
        session.close();
    }

    public <S> S with(ReturnSessionFunction<S> function) {
        Session session = this.openSession();
        S result = function.apply(session);
        session.close();
        return result;
    }

}
