package gg.sbs.api;

import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import feign.gson.DoubleToIntMapTypeAdapter;
import gg.sbs.api.apiclients.RequestInterface;
import gg.sbs.api.apiclients.converter.InstantTypeConverter;
import gg.sbs.api.apiclients.converter.NbtContentTypeConverter;
import gg.sbs.api.apiclients.converter.SkyBlockRealTimeTypeConverter;
import gg.sbs.api.apiclients.converter.SkyBlockTimeTypeConverter;
import gg.sbs.api.apiclients.hypixel.HypixelApiBuilder;
import gg.sbs.api.apiclients.hypixel.implementation.HypixelPlayerData;
import gg.sbs.api.apiclients.hypixel.implementation.HypixelResourceData;
import gg.sbs.api.apiclients.hypixel.implementation.HypixelSkyBlockData;
import gg.sbs.api.apiclients.hypixel.response.skyblock.SkyBlockDate;
import gg.sbs.api.apiclients.hypixel.response.skyblock.SkyBlockIsland;
import gg.sbs.api.apiclients.mojang.MojangApiBuilder;
import gg.sbs.api.apiclients.mojang.implementation.MojangData;
import gg.sbs.api.data.sql.SqlRepository;
import gg.sbs.api.data.sql.SqlSession;
import gg.sbs.api.data.sql.model.SqlModel;
import gg.sbs.api.data.sql.model.SqlRefreshTime;
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
import gg.sbs.api.manager.builder.BuilderManager;
import gg.sbs.api.manager.service.ServiceManager;
import gg.sbs.api.reflection.Reflection;
import gg.sbs.api.scheduler.Scheduler;
import gg.sbs.api.util.helper.StringUtil;
import gg.sbs.api.util.helper.TimeUtil;
import gg.sbs.api.util.builder.string.StringBuilder;
import gg.sbs.api.util.concurrent.Concurrent;
import gg.sbs.api.util.concurrent.ConcurrentSet;

import java.io.File;
import java.time.Instant;
import java.util.Map;

public class SimplifiedApi {

    private static final ServiceManager serviceManager = new ServiceManager();
    private static final BuilderManager builderManager = new BuilderManager();
    private static boolean databaseEnabled = false;
    private static boolean databaseRegistered = false;
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(new TypeToken<Map<String, Object>>() {}.getType(), new DoubleToIntMapTypeAdapter()) // Feign
            .registerTypeAdapter(Instant.class, new InstantTypeConverter())
            .registerTypeAdapter(SkyBlockIsland.NbtContent.class, new NbtContentTypeConverter())
            .registerTypeAdapter(SkyBlockDate.RealTime.class, new SkyBlockRealTimeTypeConverter())
            .registerTypeAdapter(SkyBlockDate.SkyBlockTime.class, new SkyBlockTimeTypeConverter())
            .setPrettyPrinting().create();
    private static final SimplifiedConfig config;

    static {
        // Load Config
        try {
            File currentDir = new File(SimplifiedApi.class.getProtectionDomain().getCodeSource().getLocation().toURI());
            config = new SimplifiedConfig(currentDir.getParentFile(), "simplified");
        } catch (Exception exception) {
            throw new IllegalArgumentException("Unable to retrieve current directory", exception); // Should never get here
        }

        // Provide Services
        serviceManager.add(Scheduler.class, Scheduler.getInstance());
        serviceManager.add(Gson.class, gson);

        // Create Api Builders
        MojangApiBuilder mojangApiBuilder = new MojangApiBuilder();
        HypixelApiBuilder hypixelApiBuilder = new HypixelApiBuilder();

        // Provide Builders
        builderManager.add(MojangData.class, MojangApiBuilder.class);
        builderManager.add(HypixelPlayerData.class, HypixelApiBuilder.class);
        builderManager.add(HypixelResourceData.class, HypixelApiBuilder.class);
        builderManager.add(HypixelSkyBlockData.class, HypixelApiBuilder.class);
        builderManager.add(String.class, StringBuilder.class);

        if (StringUtil.isNotEmpty(config.getHypixelApiKey())) {
            hypixelApiBuilder.setApiKey(config.getHypixelApiKey());

            // Provide Client Api Implementations
            serviceManager.add(HypixelPlayerData.class, hypixelApiBuilder.build(HypixelPlayerData.class));
            serviceManager.add(HypixelResourceData.class, hypixelApiBuilder.build(HypixelResourceData.class));
            serviceManager.add(HypixelSkyBlockData.class, hypixelApiBuilder.build(HypixelSkyBlockData.class));
            serviceManager.add(MojangData.class, mojangApiBuilder.build(MojangData.class));
        }
    }

    public static void enableDatabase() {
        if (!databaseRegistered) {
            // Load SqlSession
            SqlSession sqlSession = new SqlSession(getConfig(), getSqlRepositoryClasses());
            serviceManager.add(SqlSession.class, sqlSession);

            // Provide SqlRepositories
            for (Class<? extends SqlRepository<? extends SqlModel>> repository : getSqlRepositoryClasses()) {
                long refreshTime = TimeUtil.ONE_MINUTE_MS;

                // Get Custom Refresh Time
                if (repository.isAnnotationPresent(SqlRefreshTime.class)) {
                    SqlRefreshTime annoRefreshTime = repository.getAnnotation(SqlRefreshTime.class);
                    refreshTime = annoRefreshTime.value();
                }

                // Provide Repository
                serviceManager.addRaw(repository, new Reflection(repository).newInstance(sqlSession, refreshTime));
            }
            // TODO: This works but generates an error, see bottom of class

            databaseRegistered = true;
        }

        databaseEnabled = true;
    }

    public static void disableDatabase() {
        if (databaseEnabled) {
            SqlRepository.shutdownRefreshers();
            databaseEnabled = false;
        }
    }

    public static BuilderManager getBuilderManager() {
        return builderManager;
    }

    public static SimplifiedConfig getConfig() {
        return config;
    }

    public static File getCurrentDirectory() {
        return getConfig().getParentDirectory();
    }

    public static Gson getGson() {
        return getServiceManager().get(Gson.class);
    }

    public static Scheduler getScheduler() {
        return getServiceManager().get(Scheduler.class);
    }

    private static ServiceManager getServiceManager() {
        return serviceManager;
    }

    public static <T extends SqlModel, R extends SqlRepository<T>> R getSqlRepository(Class<R> tClass) {
        return getServiceManager().get(tClass);
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

    public static SqlSession getSqlSession() {
        Preconditions.checkArgument(databaseRegistered, "Database has not been registered");
        Preconditions.checkArgument(databaseEnabled, "Database has not been enabled");
        return getServiceManager().get(SqlSession.class);
    }

    public static <T extends RequestInterface> T getWebApi(Class<T> tClass) {
        return getServiceManager().get(tClass);
    }

}