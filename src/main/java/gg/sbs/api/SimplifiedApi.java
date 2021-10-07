package gg.sbs.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import feign.gson.DoubleToIntMapTypeAdapter;
import gg.sbs.api.apiclients.RequestInterface;
import gg.sbs.api.apiclients.converter.InstantTypeConverter;
import gg.sbs.api.apiclients.converter.SkyBlockRealTimeTypeConverter;
import gg.sbs.api.apiclients.converter.SkyBlockTimeTypeConverter;
import gg.sbs.api.apiclients.hypixel.implementation.HypixelPlayerData;
import gg.sbs.api.apiclients.hypixel.implementation.HypixelResourceData;
import gg.sbs.api.apiclients.hypixel.implementation.HypixelSkyBlockData;
import gg.sbs.api.apiclients.hypixel.response.skyblock.SkyBlockDate;
import gg.sbs.api.apiclients.mojang.implementation.MojangData;
import gg.sbs.api.data.sql.model.SqlModel;
import gg.sbs.api.data.sql.SqlRefreshable;
import gg.sbs.api.data.sql.SqlRepository;
import gg.sbs.api.data.sql.model.accessories.AccessoryRefreshable;
import gg.sbs.api.data.sql.model.accessories.AccessoryRepository;
import gg.sbs.api.data.sql.model.accessoryfamilies.AccessoryFamilyRefreshable;
import gg.sbs.api.data.sql.model.accessoryfamilies.AccessoryFamilyRepository;
import gg.sbs.api.data.sql.model.collectionitems.CollectionItemRefreshable;
import gg.sbs.api.data.sql.model.collectionitems.CollectionItemRepository;
import gg.sbs.api.data.sql.model.collectionitemtiers.CollectionItemTierRefreshable;
import gg.sbs.api.data.sql.model.collectionitemtiers.CollectionItemTierRepository;
import gg.sbs.api.data.sql.model.collections.CollectionRefreshable;
import gg.sbs.api.data.sql.model.collections.CollectionRepository;
import gg.sbs.api.data.sql.model.enchantments.EnchantmentRefreshable;
import gg.sbs.api.data.sql.model.enchantments.EnchantmentRepository;
import gg.sbs.api.data.sql.model.fairysouls.FairySoulRefreshable;
import gg.sbs.api.data.sql.model.fairysouls.FairySoulRepository;
import gg.sbs.api.data.sql.model.formats.FormatRefreshable;
import gg.sbs.api.data.sql.model.formats.FormatRepository;
import gg.sbs.api.data.sql.model.items.ItemRefreshable;
import gg.sbs.api.data.sql.model.items.ItemRepository;
import gg.sbs.api.data.sql.model.itemtypes.ItemTypeRefreshable;
import gg.sbs.api.data.sql.model.itemtypes.ItemTypeRepository;
import gg.sbs.api.data.sql.model.locationareas.LocationAreaRefreshable;
import gg.sbs.api.data.sql.model.locationareas.LocationAreaRepository;
import gg.sbs.api.data.sql.model.locations.LocationRefreshable;
import gg.sbs.api.data.sql.model.locations.LocationRepository;
import gg.sbs.api.data.sql.model.minionitems.MinionItemRefreshable;
import gg.sbs.api.data.sql.model.minionitems.MinionItemRepository;
import gg.sbs.api.data.sql.model.minions.MinionRefreshable;
import gg.sbs.api.data.sql.model.minions.MinionRepository;
import gg.sbs.api.data.sql.model.miniontiers.MinionTierRefreshable;
import gg.sbs.api.data.sql.model.miniontiers.MinionTierRepository;
import gg.sbs.api.data.sql.model.miniontierupgrades.MinionTierUpgradeRefreshable;
import gg.sbs.api.data.sql.model.miniontierupgrades.MinionTierUpgradeRepository;
import gg.sbs.api.data.sql.model.pets.PetRefreshable;
import gg.sbs.api.data.sql.model.pets.PetRepository;
import gg.sbs.api.data.sql.model.potions.PotionRefreshable;
import gg.sbs.api.data.sql.model.potions.PotionRepository;
import gg.sbs.api.data.sql.model.rarities.RarityRefreshable;
import gg.sbs.api.data.sql.model.rarities.RarityRepository;
import gg.sbs.api.data.sql.model.reforges.ReforgeRefreshable;
import gg.sbs.api.data.sql.model.reforges.ReforgeRepository;
import gg.sbs.api.apiclients.hypixel.HypixelApiBuilder;
import gg.sbs.api.apiclients.mojang.MojangApiBuilder;
import gg.sbs.api.data.sql.model.skilllevels.SkillLevelRefreshable;
import gg.sbs.api.data.sql.model.skilllevels.SkillLevelRepository;
import gg.sbs.api.data.sql.model.skills.SkillRefreshable;
import gg.sbs.api.data.sql.model.skills.SkillRepository;
import gg.sbs.api.data.sql.model.stats.StatRefreshable;
import gg.sbs.api.data.sql.model.stats.StatRepository;
import gg.sbs.api.scheduler.Scheduler;
import gg.sbs.api.service.ServiceManager;

import java.io.File;
import java.time.Instant;
import java.util.Map;

public class SimplifiedApi {

    private static final ServiceManager serviceManager = new ServiceManager();
    private static boolean databaseEnabled = false;
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(new TypeToken<Map<String, Object>>() {}.getType(), new DoubleToIntMapTypeAdapter()) // Feign
            .registerTypeAdapter(Instant.class, new InstantTypeConverter())
            .registerTypeAdapter(SkyBlockDate.RealTime.class, new SkyBlockRealTimeTypeConverter())
            .registerTypeAdapter(SkyBlockDate.SkyBlockTime.class, new SkyBlockTimeTypeConverter())
            .setPrettyPrinting().create();
    private static final SimplifiedConfig config;

    static {
        try {
            File currentDir = new File(SimplifiedApi.class.getProtectionDomain().getCodeSource().getLocation().toURI());
            config = new SimplifiedConfig(currentDir.getParentFile(), "simplified");
        } catch (Exception exception) {
            throw new IllegalArgumentException("Unable to retrieve current directory", exception); // Will never get here
        }

        serviceManager.provide(Scheduler.class, Scheduler.getInstance());
        serviceManager.provide(Gson.class, gson);

        serviceManager.provide(HypixelPlayerData.class, new HypixelApiBuilder().build(HypixelPlayerData.class));
        serviceManager.provide(HypixelResourceData.class, new HypixelApiBuilder().build(HypixelResourceData.class));
        serviceManager.provide(HypixelSkyBlockData.class, new HypixelApiBuilder().build(HypixelSkyBlockData.class));
        serviceManager.provide(MojangData.class, new MojangApiBuilder().build(MojangData.class));
    }

    public static File getCurrentDirectory() {
        return getConfig().getParentDirectory();
    }

    public static SimplifiedConfig getConfig() {
        return config;
    }

    public static Scheduler getScheduler() {
        return getServiceManager().getProvider(Scheduler.class);
    }

    public static ServiceManager getServiceManager() {
        return serviceManager;
    }

    public static void enableDatabase() {
        if (!databaseEnabled) {
            // Repositories
            serviceManager.provide(AccessoryRepository.class, new AccessoryRepository());
            serviceManager.provide(AccessoryFamilyRepository.class, new AccessoryFamilyRepository());
            serviceManager.provide(CollectionRepository.class, new CollectionRepository());
            serviceManager.provide(CollectionItemRepository.class, new CollectionItemRepository());
            serviceManager.provide(CollectionItemTierRepository.class, new CollectionItemTierRepository());
            serviceManager.provide(EnchantmentRepository.class, new EnchantmentRepository());
            serviceManager.provide(FairySoulRepository.class, new FairySoulRepository());
            serviceManager.provide(FormatRepository.class, new FormatRepository());
            serviceManager.provide(ItemRepository.class, new ItemRepository());
            serviceManager.provide(ItemTypeRepository.class, new ItemTypeRepository());
            serviceManager.provide(LocationRepository.class, new LocationRepository());
            serviceManager.provide(LocationAreaRepository.class, new LocationAreaRepository());
            serviceManager.provide(MinionRepository.class, new MinionRepository());
            serviceManager.provide(MinionItemRepository.class, new MinionItemRepository());
            serviceManager.provide(MinionTierRepository.class, new MinionTierRepository());
            serviceManager.provide(MinionTierUpgradeRepository.class, new MinionTierUpgradeRepository());
            serviceManager.provide(PetRepository.class, new PetRepository());
            serviceManager.provide(PotionRepository.class, new PotionRepository());
            serviceManager.provide(RarityRepository.class, new RarityRepository());
            serviceManager.provide(ReforgeRepository.class, new ReforgeRepository());
            serviceManager.provide(SkillRepository.class, new SkillRepository());
            serviceManager.provide(SkillLevelRepository.class, new SkillLevelRepository());
            serviceManager.provide(StatRepository.class, new StatRepository());

            // Refreshables
            serviceManager.provide(AccessoryRefreshable.class, new AccessoryRefreshable());
            serviceManager.provide(AccessoryFamilyRefreshable.class, new AccessoryFamilyRefreshable());
            serviceManager.provide(CollectionRefreshable.class, new CollectionRefreshable());
            serviceManager.provide(CollectionItemRefreshable.class, new CollectionItemRefreshable());
            serviceManager.provide(CollectionItemTierRefreshable.class, new CollectionItemTierRefreshable());
            serviceManager.provide(EnchantmentRefreshable.class, new EnchantmentRefreshable());
            serviceManager.provide(FairySoulRefreshable.class, new FairySoulRefreshable());
            serviceManager.provide(FormatRefreshable.class, new FormatRefreshable());
            serviceManager.provide(ItemRefreshable.class, new ItemRefreshable());
            serviceManager.provide(ItemTypeRefreshable.class, new ItemTypeRefreshable());
            serviceManager.provide(LocationRefreshable.class, new LocationRefreshable());
            serviceManager.provide(LocationAreaRefreshable.class, new LocationAreaRefreshable());
            serviceManager.provide(MinionRefreshable.class, new MinionRefreshable());
            serviceManager.provide(MinionItemRefreshable.class, new MinionItemRefreshable());
            serviceManager.provide(MinionTierRefreshable.class, new MinionTierRefreshable());
            serviceManager.provide(MinionTierUpgradeRefreshable.class, new MinionTierUpgradeRefreshable());
            serviceManager.provide(PetRefreshable.class, new PetRefreshable());
            serviceManager.provide(PotionRefreshable.class, new PotionRefreshable());
            serviceManager.provide(RarityRefreshable.class, new RarityRefreshable());
            serviceManager.provide(ReforgeRefreshable.class, new ReforgeRefreshable());
            serviceManager.provide(SkillRefreshable.class, new SkillRefreshable());
            serviceManager.provide(SkillLevelRefreshable.class, new SkillLevelRefreshable());
            serviceManager.provide(StatRefreshable.class, new StatRefreshable());

            databaseEnabled = true;
        }
    }

    public static void disableDatabase() {
        if (databaseEnabled) {
            SqlRefreshable.shutdown();
            databaseEnabled = false;
        }
    }

    public static Gson getGson() {
        return getServiceManager().getProvider(Gson.class);
    }

    public static <T extends SqlModel, R extends SqlRepository<T>, E extends SqlRefreshable<T, R>> E getSqlRefreshable(Class<E> tClass) {
        return getServiceManager().getProvider(tClass);
    }

    public static <T extends SqlModel, R extends SqlRepository<T>> R getSqlRepository(Class<R> tClass) {
        return getServiceManager().getProvider(tClass);
    }

    public static <T extends RequestInterface> T getWebApi(Class<T> tClass) {
        return getServiceManager().getProvider(tClass);
    }

}