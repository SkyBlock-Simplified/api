package gg.sbs.api.data.sql;

import gg.sbs.api.SimplifiedApi;
import gg.sbs.api.SimplifiedConfig;
import gg.sbs.api.data.sql.function.ReturnSessionFunction;
import gg.sbs.api.data.sql.function.VoidSessionFunction;
import gg.sbs.api.data.sql.model.SqlModel;
import gg.sbs.api.data.sql.model.accessories.AccessoryRepository;
import gg.sbs.api.data.sql.model.accessoryfamilies.AccessoryFamilyRepository;
import gg.sbs.api.data.sql.model.collectionitems.CollectionItemRepository;
import gg.sbs.api.data.sql.model.collectionitemtiers.CollectionItemTierRepository;
import gg.sbs.api.data.sql.model.collections.CollectionRepository;
import gg.sbs.api.data.sql.model.enchantments.EnchantmentRepository;
import gg.sbs.api.data.sql.model.fairysouls.FairySoulRepository;
import gg.sbs.api.data.sql.model.formats.FormatRepository;
import gg.sbs.api.data.sql.model.items.ItemRepository;
import gg.sbs.api.data.sql.model.itemtypes.ItemTypeRepository;
import gg.sbs.api.data.sql.model.locationareas.LocationAreaRepository;
import gg.sbs.api.data.sql.model.locations.LocationRepository;
import gg.sbs.api.data.sql.model.minionitems.MinionItemRepository;
import gg.sbs.api.data.sql.model.minions.MinionRepository;
import gg.sbs.api.data.sql.model.miniontiers.MinionTierRepository;
import gg.sbs.api.data.sql.model.miniontierupgrades.MinionTierUpgradeRepository;
import gg.sbs.api.data.sql.model.pets.PetRepository;
import gg.sbs.api.data.sql.model.potions.PotionRepository;
import gg.sbs.api.data.sql.model.rarities.RarityRepository;
import gg.sbs.api.data.sql.model.reforges.ReforgeRepository;
import gg.sbs.api.data.sql.model.skilllevels.SkillLevelRepository;
import gg.sbs.api.data.sql.model.skills.SkillRepository;
import gg.sbs.api.data.sql.model.stats.StatRepository;
import gg.sbs.api.util.concurrent.Concurrent;
import gg.sbs.api.util.concurrent.ConcurrentSet;
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
            put("hibernate.show_sql", config.isDatabaseDebugMode());
            put("hibernate.format_sql", config.isDatabaseDebugMode());
            put("hibernate.generate_statistics", false);
            put("hibernate.use_sql_comments", false);
            put("log4j.logger.net.sf.hibernate", "FATAL");
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

        // Add all inheritors of SqlModel from their Repositories
        Configuration configuration = new Configuration().setProperties(properties);
        for (Class<?> rClass : getSqlRepositoryClasses()) {
            ParameterizedType superClass = (ParameterizedType) rClass.getGenericSuperclass();
            Class<?> tClass = (Class<?>) superClass.getActualTypeArguments()[0];
            configuration.addAnnotatedClass(tClass);
        }

        return configuration.buildSessionFactory();
    }

    public static ConcurrentSet<Class<? extends SqlRepository<? extends SqlModel>>> getSqlRepositoryClasses() {
        return Concurrent.newSet(
                AccessoryRepository.class,
                AccessoryFamilyRepository.class,
                CollectionRepository.class,
                CollectionItemRepository.class,
                CollectionItemTierRepository.class,
                EnchantmentRepository.class,
                FairySoulRepository.class,
                FormatRepository.class,
                ItemRepository.class,
                ItemTypeRepository.class,
                LocationRepository.class,
                LocationAreaRepository.class,
                MinionRepository.class,
                MinionItemRepository.class,
                MinionTierRepository.class,
                MinionTierUpgradeRepository.class,
                PetRepository.class,
                PotionRepository.class,
                RarityRepository.class,
                ReforgeRepository.class,
                SkillRepository.class,
                SkillLevelRepository.class,
                StatRepository.class
        );
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