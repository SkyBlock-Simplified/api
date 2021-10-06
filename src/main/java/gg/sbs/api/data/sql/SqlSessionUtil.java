package gg.sbs.api.data.sql;

import gg.sbs.api.SimplifiedApi;
import gg.sbs.api.SimplifiedConfig;
import gg.sbs.api.data.sql.driver.MariaDBDriver;
import gg.sbs.api.data.sql.models.accessories.AccessoryModel;
import gg.sbs.api.data.sql.models.accessoryfamilies.AccessoryFamilyModel;
import gg.sbs.api.data.sql.models.collectionitems.CollectionItemModel;
import gg.sbs.api.data.sql.models.collectionitemtiers.CollectionItemTierModel;
import gg.sbs.api.data.sql.models.collections.CollectionModel;
import gg.sbs.api.data.sql.models.enchantments.EnchantmentModel;
import gg.sbs.api.data.sql.models.items.ItemModel;
import gg.sbs.api.data.sql.models.itemtypes.ItemTypeModel;
import gg.sbs.api.data.sql.models.pets.PetModel;
import gg.sbs.api.data.sql.models.potions.PotionModel;
import gg.sbs.api.data.sql.models.rarities.RarityModel;
import gg.sbs.api.data.sql.models.reforges.ReforgeModel;
import gg.sbs.api.data.sql.models.skilllevels.SkillLevelModel;
import gg.sbs.api.data.sql.models.skills.SkillModel;
import gg.sbs.api.util.FormatUtil;
import gg.sbs.api.util.ResourceUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class SqlSessionUtil {

    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        SimplifiedConfig config = SimplifiedApi.getConfig();
        //Map<String, String> env = ResourceUtil.getEnvironmentVariables();
        Properties properties = new Properties();
        properties.putAll(
                new HashMap<String, String>() {{
                    put("connection.driver_class", SqlDrivers.MariaDB.getDriverClass() /*org.mariadb.jdbc.Driver.class.getCanonicalName()*/);
                    put("hibernate.show_sql", config.getDatabaseDebug().toString());
                    put("hibernate.connection.url", SqlDrivers.MariaDB.getConnectionUrl(config.getDatabaseHost(), config.getDatabasePort(), config.getDatabaseSchema())
                            /*"jdbc:mariadb://" + env.get("DB_HOST")
                            + ":" + env.get("DB_PORT")
                            + "/" + env.get("DB_DATABASE")*/);
                    put("hibernate.connection.username", config.getDatabaseUser());
                    put("hibernate.connection.password", config.getDatabasePassword());
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
        for (Class<? extends SqlModel> tClass : SimplifiedApi.getServiceManager().getServices(SqlModel.class))
            configuration = configuration.addAnnotatedClass(tClass);

        /*for (Class<? extends SqlModel> model : Arrays.asList(
                AccessoryModel.class,
                AccessoryFamilyModel.class,
                CollectionModel.class,
                CollectionItemModel.class,
                CollectionItemTierModel.class,
                EnchantmentModel.class,
                ItemModel.class,
                ItemTypeModel.class,
                PetModel.class,
                PotionModel.class,
                RarityModel.class,
                ReforgeModel.class,
                SkillModel.class,
                SkillLevelModel.class
        )) configuration = configuration.addAnnotatedClass(model);*/
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