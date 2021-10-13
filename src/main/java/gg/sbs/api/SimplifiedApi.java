package gg.sbs.api;

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
import gg.sbs.api.reflection.Reflection;
import gg.sbs.api.scheduler.Scheduler;
import gg.sbs.api.manager.service.ServiceManager;
import gg.sbs.api.util.concurrent.Concurrent;
import gg.sbs.api.util.concurrent.ConcurrentSet;

import java.io.File;
import java.time.Instant;
import java.util.Map;

public class SimplifiedApi {

    private static final ServiceManager serviceManager = new ServiceManager();
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
            throw new IllegalArgumentException("Unable to retrieve current directory", exception); // Will never get here
        }

        serviceManager.provide(Scheduler.class, Scheduler.getInstance());
        serviceManager.provide(Gson.class, gson);

        // Provide Api Builders
        MojangApiBuilder mojangApiBuilder = new MojangApiBuilder();
        HypixelApiBuilder hypixelApiBuilder = new HypixelApiBuilder();
        hypixelApiBuilder.setApiKey(config.getHypixelApiKey());
        serviceManager.provide(MojangApiBuilder.class, mojangApiBuilder);
        serviceManager.provide(HypixelApiBuilder.class, hypixelApiBuilder);

        // Provide Api Implementations
        serviceManager.provide(HypixelPlayerData.class, hypixelApiBuilder.build(HypixelPlayerData.class));
        serviceManager.provide(HypixelResourceData.class, hypixelApiBuilder.build(HypixelResourceData.class));
        serviceManager.provide(HypixelSkyBlockData.class, hypixelApiBuilder.build(HypixelSkyBlockData.class));
        serviceManager.provide(MojangData.class, mojangApiBuilder.build(MojangData.class));
    }

    public static void enableDatabase() {
        if (!databaseRegistered) {
            // Provide SqlRepositories
            for (Class<? extends SqlRepository<?>> repository : getSqlRepositoryClasses())
                serviceManager.provideRaw(repository, new Reflection(repository).newInstance());

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

    public static SimplifiedConfig getConfig() {
        return config;
    }

    public static File getCurrentDirectory() {
        return getConfig().getParentDirectory();
    }

    public static Gson getGson() {
        return getServiceManager().getProvider(Gson.class);
    }

    public static Scheduler getScheduler() {
        return getServiceManager().getProvider(Scheduler.class);
    }

    public static ServiceManager getServiceManager() {
        return serviceManager;
    }

    public static ConcurrentSet<Class<? extends SqlRepository<?>>> getSqlRepositoryClasses() {
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

    public static <T extends SqlModel, R extends SqlRepository<T>> R getSqlRepository(Class<R> tClass) {
        return getServiceManager().getProvider(tClass);
    }

    public static <T extends RequestInterface> T getWebApi(Class<T> tClass) {
        return getServiceManager().getProvider(tClass);
    }

}