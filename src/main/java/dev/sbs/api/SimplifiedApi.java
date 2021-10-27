package dev.sbs.api;

import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import dev.sbs.api.client.converter.InstantTypeConverter;
import dev.sbs.api.client.converter.NbtContentTypeConverter;
import dev.sbs.api.client.converter.SkyBlockRealTimeTypeConverter;
import dev.sbs.api.client.converter.SkyBlockTimeTypeConverter;
import dev.sbs.api.client.hypixel.implementation.HypixelResourceData;
import dev.sbs.api.client.hypixel.implementation.HypixelSkyBlockData;
import dev.sbs.api.client.hypixel.response.skyblock.SkyBlockDate;
import dev.sbs.api.client.mojang.implementation.MojangData;
import dev.sbs.api.data.sql.model.SqlRefreshTime;
import dev.sbs.api.model.sql.fairysouls.dungeonfairysouls.DungeonFairySoulRepository;
import dev.sbs.api.model.sql.formats.FormatRepository;
import dev.sbs.api.model.sql.items.ItemRepository;
import dev.sbs.api.model.sql.minions.MinionRepository;
import dev.sbs.api.model.sql.npcs.NpcRepository;
import dev.sbs.api.model.sql.pets.PetRepository;
import dev.sbs.api.model.sql.pets.petabilities.PetAbilityRepository;
import dev.sbs.api.model.sql.pets.petabilitystats.PetAbilityStatRepository;
import dev.sbs.api.model.sql.pets.petitemstats.PetItemStatRepository;
import dev.sbs.api.model.sql.pets.petstats.PetStatRepository;
import dev.sbs.api.model.sql.pets.pettypes.PetTypeRepository;
import dev.sbs.api.model.sql.potions.PotionRepository;
import dev.sbs.api.model.sql.reforges.reforgestats.ReforgeStatRepository;
import dev.sbs.api.model.sql.skills.SkillRepository;
import dev.sbs.api.model.sql.stats.StatRepository;
import dev.sbs.api.minecraft.nbt_old.NbtFactory_old;
import dev.sbs.api.reflection.Reflection;
import dev.sbs.api.scheduler.Scheduler;
import dev.sbs.api.util.concurrent.Concurrent;
import dev.sbs.api.util.concurrent.ConcurrentSet;
import feign.gson.DoubleToIntMapTypeAdapter;
import dev.sbs.api.client.RequestInterface;
import dev.sbs.api.client.hypixel.HypixelApiBuilder;
import dev.sbs.api.client.hypixel.implementation.HypixelPlayerData;
import dev.sbs.api.client.hypixel.response.skyblock.SkyBlockIsland;
import dev.sbs.api.client.mojang.MojangApiBuilder;
import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import dev.sbs.api.data.sql.model.SqlModel;
import dev.sbs.api.model.sql.accessories.AccessoryRepository;
import dev.sbs.api.model.sql.accessories.accessoryfamilies.AccessoryFamilyRepository;
import dev.sbs.api.model.sql.collections.collectionitems.CollectionItemRepository;
import dev.sbs.api.model.sql.collections.collectionitemtiers.CollectionItemTierRepository;
import dev.sbs.api.model.sql.collections.CollectionRepository;
import dev.sbs.api.model.sql.enchantments.EnchantmentRepository;
import dev.sbs.api.model.sql.fairysouls.FairySoulRepository;
import dev.sbs.api.model.sql.reforges.reforgetypes.ReforgeTypeRepository;
import dev.sbs.api.model.sql.locations.locationareas.LocationAreaRepository;
import dev.sbs.api.model.sql.locations.LocationRepository;
import dev.sbs.api.model.sql.minions.minionitems.MinionItemRepository;
import dev.sbs.api.model.sql.minions.miniontiers.MinionTierRepository;
import dev.sbs.api.model.sql.minions.miniontierupgrades.MinionTierUpgradeRepository;
import dev.sbs.api.model.sql.rarities.RarityRepository;
import dev.sbs.api.model.sql.reforges.ReforgeRepository;
import dev.sbs.api.model.sql.skills.skilllevels.SkillLevelRepository;
import dev.sbs.api.manager.builder.BuilderManager;
import dev.sbs.api.manager.service.ServiceManager;
import dev.sbs.api.util.helper.TimeUtil;
import dev.sbs.api.util.builder.string.StringBuilder;

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

        // Provide Client Api Implementations
        serviceManager.add(HypixelPlayerData.class, hypixelApiBuilder.build(HypixelPlayerData.class));
        serviceManager.add(HypixelResourceData.class, hypixelApiBuilder.build(HypixelResourceData.class));
        serviceManager.add(HypixelSkyBlockData.class, hypixelApiBuilder.build(HypixelSkyBlockData.class));
        serviceManager.add(MojangData.class, mojangApiBuilder.build(MojangData.class));
    }

    public static void enableDatabase() {
        if (!databaseRegistered) {
            // Load SqlSession
            SqlSession sqlSession = new SqlSession(getConfig(), getAllSqlRepositoryClasses());
            serviceManager.add(SqlSession.class, sqlSession);

            // Provide SqlRepositories
            for (Class<? extends SqlRepository<? extends SqlModel>> repository : getAllSqlRepositoryClasses()) {
                long refreshTime = TimeUtil.ONE_MINUTE_MS;

                // Get Custom Refresh Time
                if (repository.isAnnotationPresent(SqlRefreshTime.class)) {
                    SqlRefreshTime annoRefreshTime = repository.getAnnotation(SqlRefreshTime.class);
                    refreshTime = annoRefreshTime.value();
                }

                // Provide Repository
                serviceManager.addRaw(repository, new Reflection(repository).newInstance(sqlSession, refreshTime));
            }

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

    public static NbtFactory_old getNbtFactory() {
        return NbtFactory_old.getInstance(); // DO NOT USE THIS
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

    private static ConcurrentSet<Class<? extends SqlRepository<? extends SqlModel>>> getAllSqlRepositoryClasses() {
        return Concurrent.newSet(
                AccessoryRepository.class,
                AccessoryFamilyRepository.class,
                CollectionRepository.class,
                CollectionItemRepository.class,
                CollectionItemTierRepository.class,
                DungeonFairySoulRepository.class,
                EnchantmentRepository.class,
                FairySoulRepository.class,
                FormatRepository.class,
                ItemRepository.class,
                LocationRepository.class,
                LocationAreaRepository.class,
                MinionRepository.class,
                MinionItemRepository.class,
                MinionTierRepository.class,
                MinionTierUpgradeRepository.class,
                NpcRepository.class,
                PetRepository.class,
                PetAbilityRepository.class,
                PetAbilityStatRepository.class,
                PetItemStatRepository.class,
                PetStatRepository.class,
                PetTypeRepository.class,
                PotionRepository.class,
                RarityRepository.class,
                ReforgeRepository.class,
                ReforgeStatRepository.class,
                ReforgeTypeRepository.class,
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
