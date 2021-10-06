package gg.sbs.api.data.sql;

import gg.sbs.api.SimplifiedApi;
import gg.sbs.api.SimplifiedConfig;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.lang.reflect.ParameterizedType;
import java.util.Properties;

public class SqlSessionUtil {

    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        SimplifiedApi.enableDatabase();
        SimplifiedConfig config = SimplifiedApi.getConfig();

        Properties properties = new Properties() {{
            put("connection.driver_class", SqlDrivers.MariaDB.getDriverClass());
            put("hibernate.show_sql", String.valueOf(config.isDatabaseDebugMode()));
            put("hibernate.connection.url", SqlDrivers.MariaDB.getConnectionUrl(config.getDatabaseHost(), config.getDatabasePort(), config.getDatabaseSchema()));
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
        for (Class<?> rClass : SimplifiedApi.getServiceManager().getServices(SqlRefreshable.class)) {
            ParameterizedType superClass = (ParameterizedType) rClass.getGenericSuperclass();
            Class<?> tClass = (Class<?>) superClass.getActualTypeArguments()[0];
            configuration = configuration.addAnnotatedClass(tClass);
        }

        return configuration.buildSessionFactory();
    }

    public static Session openSession() {
        return sessionFactory.openSession();
    }

    public static void createTransaction(SessionFunction f) {
        Session session = openSession();
        f.run(session);
        session.close();
    }

    public interface SessionFunction {

        void run(Session session);

    }

}