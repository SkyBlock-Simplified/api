package dev.sbs.api;

import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import dev.sbs.api.client.RequestInterface;
import dev.sbs.api.client.adapter.*;
import dev.sbs.api.client.hypixel.HypixelApiBuilder;
import dev.sbs.api.client.hypixel.implementation.HypixelPlayerData;
import dev.sbs.api.client.hypixel.implementation.HypixelResourceData;
import dev.sbs.api.client.hypixel.implementation.HypixelSkyBlockData;
import dev.sbs.api.client.hypixel.response.skyblock.SkyBlockDate;
import dev.sbs.api.client.hypixel.response.skyblock.SkyBlockIsland;
import dev.sbs.api.client.mojang.MojangApiBuilder;
import dev.sbs.api.client.mojang.implementation.MojangData;
import dev.sbs.api.data.model.SqlModel;
import dev.sbs.api.data.model.accessories.AccessorySqlRepository;
import dev.sbs.api.data.model.accessory_families.AccessoryFamilySqlRepository;
import dev.sbs.api.data.model.collection_item_tiers.CollectionItemTierSqlRepository;
import dev.sbs.api.data.model.collection_items.CollectionItemSqlRepository;
import dev.sbs.api.data.model.collections.CollectionSqlRepository;
import dev.sbs.api.data.model.dungeon_bosses.DungeonBossSqlRepository;
import dev.sbs.api.data.model.dungeon_classes.DungeonClassSqlRepository;
import dev.sbs.api.data.model.dungeon_fairy_souls.DungeonFairySoulSqlRepository;
import dev.sbs.api.data.model.dungeon_floor_sizes.DungeonFloorSizeSqlRepository;
import dev.sbs.api.data.model.dungeon_floors.DungeonFloorSqlRepository;
import dev.sbs.api.data.model.dungeon_levels.DungeonLevelSqlRepository;
import dev.sbs.api.data.model.dungeons.DungeonSqlRepository;
import dev.sbs.api.data.model.fairy_souls.FairySoulSqlRepository;
import dev.sbs.api.data.model.formats.FormatSqlRepository;
import dev.sbs.api.data.model.items.ItemSqlRepository;
import dev.sbs.api.data.model.location_areas.LocationAreaSqlRepository;
import dev.sbs.api.data.model.location_remotes.LocationRemoteSqlRepository;
import dev.sbs.api.data.model.locations.LocationSqlRepository;
import dev.sbs.api.data.model.minion_items.MinionItemSqlRepository;
import dev.sbs.api.data.model.minion_tier_upgrades.MinionTierUpgradeSqlRepository;
import dev.sbs.api.data.model.minion_tiers.MinionTierSqlRepository;
import dev.sbs.api.data.model.minion_uniques.MinionUniqueSqlRepository;
import dev.sbs.api.data.model.minions.MinionSqlRepository;
import dev.sbs.api.data.model.npcs.NpcSqlRepository;
import dev.sbs.api.data.model.pet_abilities.PetAbilitySqlRepository;
import dev.sbs.api.data.model.pet_ability_stats.PetAbilityStatSqlRepository;
import dev.sbs.api.data.model.pet_exp_scales.PetExpScaleSqlRepository;
import dev.sbs.api.data.model.pet_items.PetItemSqlRepository;
import dev.sbs.api.data.model.pet_scores.PetScoreSqlRepository;
import dev.sbs.api.data.model.pet_stats.PetStatSqlRepository;
import dev.sbs.api.data.model.pet_types.PetTypeSqlRepository;
import dev.sbs.api.data.model.pets.PetSqlRepository;
import dev.sbs.api.data.model.potion_brew_buffs.PotionBrewBuffSqlRepository;
import dev.sbs.api.data.model.potion_brews.PotionBrewSqlRepository;
import dev.sbs.api.data.model.potion_group_items.PotionGroupItemSqlRepository;
import dev.sbs.api.data.model.potion_groups.PotionGroupSqlRepository;
import dev.sbs.api.data.model.potion_mixins.PotionMixinSqlRepository;
import dev.sbs.api.data.model.potion_tiers.PotionTierSqlRepository;
import dev.sbs.api.data.model.potions.PotionSqlRepository;
import dev.sbs.api.data.model.rarities.RaritySqlRepository;
import dev.sbs.api.data.model.reforge_stats.ReforgeStatSqlRepository;
import dev.sbs.api.data.model.reforge_types.ReforgeTypeSqlRepository;
import dev.sbs.api.data.model.reforges.ReforgeSqlRepository;
import dev.sbs.api.data.model.skill_levels.SkillLevelSqlRepository;
import dev.sbs.api.data.model.skills.SkillSqlRepository;
import dev.sbs.api.data.model.skyblock_bag_sizes.SkyBlockBagSizeSqlRepository;
import dev.sbs.api.data.model.skyblock_bags.SkyBlockBagSqlRepository;
import dev.sbs.api.data.model.skyblock_sack_items.SkyBlockSackItemSqlRepository;
import dev.sbs.api.data.model.skyblock_sacks.SkyBlockSackSqlRepository;
import dev.sbs.api.data.model.slayer_levels.SlayerLevelSqlRepository;
import dev.sbs.api.data.model.slayers.SlayerSqlRepository;
import dev.sbs.api.data.model.stats.StatSqlRepository;
import dev.sbs.api.data.sql.SqlRefreshTime;
import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import dev.sbs.api.manager.builder.BuilderManager;
import dev.sbs.api.manager.service.ServiceManager;
import dev.sbs.api.minecraft.nbt_old.NbtFactory_old;
import dev.sbs.api.minecraft.text.MinecraftTextBuilder;
import dev.sbs.api.minecraft.text.MinecraftTextObject;
import dev.sbs.api.reflection.Reflection;
import dev.sbs.api.scheduler.Scheduler;
import dev.sbs.api.util.builder.string.StringBuilder;
import dev.sbs.api.util.concurrent.Concurrent;
import dev.sbs.api.util.concurrent.ConcurrentList;
import dev.sbs.api.util.helper.TimeUtil;
import feign.gson.DoubleToIntMapTypeAdapter;

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
            .registerTypeAdapter(Instant.class, new InstantTypeAdapter())
            .registerTypeAdapter(SkyBlockIsland.NbtContent.class, new NbtContentTypeAdapter())
            .registerTypeAdapter(SkyBlockDate.RealTime.class, new SkyBlockRealTimeTypeAdapter())
            .registerTypeAdapter(SkyBlockDate.SkyBlockTime.class, new SkyBlockTimeTypeAdapter())
            .registerTypeAdapter(UUIDAdapter.class, new UUIDAdapter())
            .registerTypeAdapter(SkyBlockIsland.class, new SkyBlockIsland.Deserializer())
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
        builderManager.add(MinecraftTextObject.class, MinecraftTextBuilder.class);

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

    // TODO: Replace with inherited repository that's implemented by SQL, Web
    public static <T extends SqlModel, R extends SqlRepository<T>> R getSqlRepository(Class<R> tClass) {
        return getServiceManager().get(tClass);
    }

    private static ConcurrentList<Class<? extends SqlRepository<? extends SqlModel>>> getAllSqlRepositoryClasses() {
        return Concurrent.newUnmodifiableList( // This must follow the foreign key and model ordering
                // No Foreign Keys
                RaritySqlRepository.class,
                FormatSqlRepository.class,
                LocationSqlRepository.class,
                DungeonFairySoulSqlRepository.class,
                SlayerSqlRepository.class,
                PetTypeSqlRepository.class,
                PotionSqlRepository.class,
                ReforgeTypeSqlRepository.class,
                AccessoryFamilySqlRepository.class,
                PetExpScaleSqlRepository.class,
                PotionGroupSqlRepository.class,
                DungeonSqlRepository.class,
                DungeonBossSqlRepository.class,
                DungeonClassSqlRepository.class,
                DungeonFloorSizeSqlRepository.class,
                PetScoreSqlRepository.class,
                MinionUniqueSqlRepository.class,
                LocationRemoteSqlRepository.class,
                SkyBlockSackSqlRepository.class,

                // Requires Above
                ItemSqlRepository.class,
                StatSqlRepository.class,
                LocationAreaSqlRepository.class,
                SlayerLevelSqlRepository.class,
                SkillSqlRepository.class,
                SkillLevelSqlRepository.class,
                ReforgeSqlRepository.class,
                ReforgeStatSqlRepository.class,
                AccessorySqlRepository.class,
                PotionMixinSqlRepository.class,
                DungeonFloorSqlRepository.class,
                DungeonLevelSqlRepository.class,

                // Requires Above
                CollectionSqlRepository.class,
                PetItemSqlRepository.class,
                NpcSqlRepository.class,
                FairySoulSqlRepository.class,
                PetSqlRepository.class,
                PotionTierSqlRepository.class,
                SkyBlockSackItemSqlRepository.class,

                // Requires Above
                MinionSqlRepository.class,
                CollectionItemSqlRepository.class,
                PetStatSqlRepository.class,
                PotionBrewSqlRepository.class,
                PotionGroupItemSqlRepository.class,

                // Requires Above
                MinionTierSqlRepository.class,
                CollectionItemTierSqlRepository.class,
                MinionItemSqlRepository.class,
                PotionBrewBuffSqlRepository.class,
                SkyBlockBagSqlRepository.class,

                // Requires Above
                MinionTierUpgradeSqlRepository.class,
                PetAbilitySqlRepository.class,
                SkyBlockBagSizeSqlRepository.class,

                // Requires Above
                PetAbilityStatSqlRepository.class
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
