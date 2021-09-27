package gg.sbs.api.data.sql;

import gg.sbs.api.data.sql.models.accessories.AccessoryModel;
import gg.sbs.api.data.sql.models.accessoryfamilies.AccessoryFamilyModel;
import gg.sbs.api.data.sql.models.enchantments.EnchantmentModel;
import gg.sbs.api.data.sql.models.itemtypes.ItemTypeModel;
import gg.sbs.api.data.sql.models.pets.PetModel;
import gg.sbs.api.data.sql.models.potions.PotionModel;
import gg.sbs.api.data.sql.models.rarities.RarityModel;
import gg.sbs.api.data.sql.models.reforges.ReforgeModel;
import gg.sbs.api.data.sql.models.skills.SkillModel;
import gg.sbs.api.util.ResourceUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.*;

public class SqlSessionUtil {
    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        Map<String, String> env = ResourceUtil.getEnvironmentVariables();
        Properties properties = new Properties();
        properties.putAll(
                new HashMap<String, String>() {{
                    put("connection.driver_class", org.mariadb.jdbc.Driver.class.getCanonicalName());
                    put("hibernate.connection.url", "jdbc:mariadb://" + env.get("DB_HOST")
                            + ":" + env.get("DB_PORT")
                            + "/" + env.get("DB_DATABASE"));
                    put("hibernate.connection.username", env.get("DB_USER"));
                    put("hibernate.connection.password", env.get("DB_PASSWORD"));
                    put("hibernate.connection.provider_class",
                            org.hibernate.hikaricp.internal.HikariCPConnectionProvider.class.getCanonicalName());
                    put("hibernate.dialect", org.hibernate.dialect.MariaDB103Dialect.class.getCanonicalName());
                    put("hikari.prepStmtCacheSize", "250");
                    put("hikari.prepStmtCacheSqlLimit", "2048");
                    put("hikari.cachePrepStmts", "true");
                    put("hikari.useServerPrepStmts", "true");
                }}
        );
        Configuration configuration = new Configuration().setProperties(properties);
        for (Class<? extends SqlModel> model : Arrays.asList(
                AccessoryModel.class,
                AccessoryFamilyModel.class,
                EnchantmentModel.class,
                ItemTypeModel.class,
                PetModel.class,
                PotionModel.class,
                RarityModel.class,
                ReforgeModel.class,
                SkillModel.class
        )) configuration = configuration.addAnnotatedClass(model);
        return configuration.buildSessionFactory();
    }

    public static Session openSession() {
        return sessionFactory.openSession();
    }
}
