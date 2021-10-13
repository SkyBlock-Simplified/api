package gg.sbs.api.data.sql;

import gg.sbs.api.SimplifiedApi;
import gg.sbs.api.SimplifiedConfig;
import gg.sbs.api.data.sql.function.ReturnSessionFunction;
import gg.sbs.api.data.sql.function.VoidSessionFunction;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.lang.reflect.ParameterizedType;
import java.util.Properties;

public class SqlSessionUtil {

    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        SimplifiedConfig config = SimplifiedApi.getConfig();

        Properties properties = new Properties() {{
            put("connection.driver_class", SqlDriver.MariaDB.getDriverClass());
            put("hibernate.show_sql", String.valueOf(config.isDatabaseDebugMode()));
            put("hibernate.connection.url", SqlDriver.MariaDB.getConnectionUrl(config.getDatabaseHost(), config.getDatabasePort(), config.getDatabaseSchema()));
            put("hibernate.connection.username", config.getDatabaseUser());
            put("hibernate.connection.password", config.getDatabasePassword());
            put("hibernate.connection.provider_class", org.hibernate.hikaricp.internal.HikariCPConnectionProvider.class.getCanonicalName());
            put("hibernate.dialect", org.hibernate.dialect.MariaDB103Dialect.class.getCanonicalName());
            put("hikari.prepStmtCacheSize", "250");
            put("hikari.prepStmtCacheSqlLimit", "2048");
            put("hikari.cachePrepStmts", "true");
            put("hikari.useServerPrepStmts", "true");
        }};

        // Add all inheritors of SqlModel
        Configuration configuration = new Configuration().setProperties(properties);
        for (Class<?> rClass : SimplifiedApi.getServiceManager().getServices(SqlRepository.class)) {
            ParameterizedType superClass = (ParameterizedType) rClass.getGenericSuperclass();
            Class<?> tClass = (Class<?>) superClass.getActualTypeArguments()[0];
            configuration = configuration.addAnnotatedClass(tClass);
        }

        return configuration.buildSessionFactory();
    }

    public static Session openSession() {
        return sessionFactory.openSession();
    }

    public static void withSession(VoidSessionFunction function) {
        Session session = openSession();
        function.handle(session);
        session.close();
    }

    public static <S> S withSession(ReturnSessionFunction<S> function) {
        Session session = openSession();
        S result = function.handle(session);
        session.close();
        return result;
    }

    public static void withTransaction(VoidSessionFunction function) {
        Session session = openSession();
        Transaction tx = session.beginTransaction();
        function.handle(session);
        session.flush();
        tx.commit();
        session.close();
    }

    public static <S> S withTransaction(ReturnSessionFunction<S> function) {
        Session session = openSession();
        Transaction tx = session.beginTransaction();
        S result = function.handle(session);
        session.flush();
        tx.commit();
        session.close();
        return result;
    }

}