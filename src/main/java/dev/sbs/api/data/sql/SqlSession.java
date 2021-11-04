package dev.sbs.api.data.sql;

import dev.sbs.api.data.model.SqlModel;
import dev.sbs.api.data.sql.function.ReturnSessionFunction;
import dev.sbs.api.data.sql.function.VoidSessionFunction;
import dev.sbs.api.util.concurrent.ConcurrentList;
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
    @Getter private final ConcurrentList<Class<? extends SqlRepository<? extends SqlModel>>> repositories;

    public SqlSession(SqlConfig config, ConcurrentList<Class<? extends SqlRepository<? extends SqlModel>>> repositories) {
        this.config = config;
        this.repositories = repositories;

        Properties properties = new Properties() {{
            put("connection.driver_class", config.getDatabaseDriver().getDriverClass());
            put("hibernate.show_sql", false);
            put("hibernate.generate_statistics", config.isDatabaseStatistics());
            put("hibernate.connection.url", config.getDatabaseDriver().getConnectionUrl(config.getDatabaseHost(), config.getDatabasePort(), config.getDatabaseSchema()));
            put("hibernate.connection.username", config.getDatabaseUser());
            put("hibernate.connection.password", config.getDatabasePassword());
            put("hibernate.connection.provider_class", "org.hibernate.hikaricp.internal.HikariCPConnectionProvider");
            put("hibernate.format_sql", false); // Log Spam
            put("hibernate.use_sql_comments", true);
            put("hibernate.order_inserts", true);
            put("hibernate.dialect", config.getDatabaseDriver().getDialectClass());
            put("hikari.cachePrepStmts", true);
            put("hikari.prepStmtCacheSize", 256);
            put("hikari.prepStmtCacheSqlLimit", 2048);
            put("hikari.useServerPrepStmts", true);
        }};

        // Add all inheritors of SqlModel from their Repositories
        Configuration configuration = new Configuration().setProperties(properties);
        this.repositories.stream()
                .map(Class::getGenericSuperclass)
                .map(ParameterizedType.class::cast)
                .map(ParameterizedType::getActualTypeArguments)
                .map(index -> index[0])
                .map(Class.class::cast)
                .forEach(configuration::addAnnotatedClass);

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
