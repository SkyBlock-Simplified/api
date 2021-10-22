package dev.sbs.api.data.sql;

import dev.sbs.api.util.concurrent.ConcurrentSet;
import dev.sbs.api.data.sql.function.ReturnSessionFunction;
import dev.sbs.api.data.sql.function.VoidSessionFunction;
import dev.sbs.api.data.sql.model.SqlModel;
import lombok.Getter;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.lang.reflect.ParameterizedType;
import java.util.Properties;

public final class SqlSession {

    @Getter private final SessionFactory sessionFactory;
    @Getter private final SqlConfig config;
    @Getter private final ConcurrentSet<Class<? extends SqlRepository<? extends SqlModel>>> repositories;

    public SqlSession(SqlConfig config, ConcurrentSet<Class<? extends SqlRepository<? extends SqlModel>>> repositories) {
        this.config = config;
        this.repositories = repositories;

        Properties properties = new Properties() {{
            put("connection.driver_class", config.getDatabaseDriver().getDriverClass());
            put("hibernate.show_sql", config.isDatabaseDebugMode());
            put("hibernate.format_sql", config.isDatabaseDebugMode());
            put("hibernate.generate_statistics", false);
            put("hibernate.use_sql_comments", false);
            put("hibernate.connection.url", config.getDatabaseDriver().getConnectionUrl(config.getDatabaseHost(), config.getDatabasePort(), config.getDatabaseSchema()));
            put("hibernate.connection.username", config.getDatabaseUser());
            put("hibernate.connection.password", config.getDatabasePassword());
            put("hibernate.connection.provider_class", "org.hibernate.hikaricp.internal.HikariCPConnectionProvider");
            put("hibernate.dialect", config.getDatabaseDriver().getDialectClass());
            put("hikari.prepStmtCacheSize", "250");
            put("hikari.prepStmtCacheSqlLimit", "2048");
            put("hikari.cachePrepStmts", "true");
            put("hikari.useServerPrepStmts", "true");
        }};

        // Add all inheritors of SqlModel from their Repositories
        Configuration configuration = new Configuration().setProperties(properties);
        for (Class<?> rClass : this.repositories) {
            ParameterizedType superClass = (ParameterizedType) rClass.getGenericSuperclass();
            Class<?> tClass = (Class<?>) superClass.getActualTypeArguments()[0];
            configuration.addAnnotatedClass(tClass);
        }

        this.sessionFactory = configuration.buildSessionFactory();
    }

    public Session openSession() {
        return this.sessionFactory.openSession();
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

}
