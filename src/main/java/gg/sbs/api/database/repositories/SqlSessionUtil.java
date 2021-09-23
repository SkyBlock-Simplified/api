package gg.sbs.api.database.repositories;

import gg.sbs.api.util.EnvVars;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.util.HashMap;
import java.util.Map;

public class SqlSessionUtil {
    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        Map<String, String> env = EnvVars.get();
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder().applySettings(
                new HashMap<String, String>() {{
                    put("connection.driver_class", org.mariadb.jdbc.Driver.class.getCanonicalName());
                    put("hibernate.connection.url", "jdbc:mariadb://" + env.get("DB_HOST")
                            + ":" + env.get("DB_PORT")
                            + "/" + env.get("DB_DATABASE"));
                    put("hibernate.connection.username", env.get("DB_USER"));
                    put("hibernate.connection.password", env.get("DB_PASSWORD"));
                    put("hibernate.dialect", org.hibernate.dialect.MariaDB103Dialect.class.getCanonicalName());
                }}
        ).build();
        return new MetadataSources(registry).buildMetadata().buildSessionFactory();
    }

    public static Session openSession() {
        return sessionFactory.openSession();
    }
}
