package dev.sbs.api;

import ch.qos.logback.classic.Logger;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import dev.sbs.api.client.RequestInterface;
import dev.sbs.api.client.adapter.InstantTypeAdapter;
import dev.sbs.api.client.adapter.NbtContentTypeAdapter;
import dev.sbs.api.client.adapter.SkyBlockRealTimeTypeAdapter;
import dev.sbs.api.client.adapter.SkyBlockTimeTypeAdapter;
import dev.sbs.api.client.adapter.UUIDTypeAdapter;
import dev.sbs.api.client.hypixel.HypixelApiBuilder;
import dev.sbs.api.client.hypixel.implementation.HypixelPlayerData;
import dev.sbs.api.client.hypixel.implementation.HypixelResourceData;
import dev.sbs.api.client.hypixel.implementation.HypixelSkyBlockData;
import dev.sbs.api.client.hypixel.response.skyblock.SkyBlockDate;
import dev.sbs.api.client.hypixel.response.skyblock.island.SkyBlockIsland;
import dev.sbs.api.client.sbs.SbsApiBuilder;
import dev.sbs.api.client.sbs.implementation.MojangData;
import dev.sbs.api.client.sbs.implementation.SkyBlockData;
import dev.sbs.api.client.sbs.response.SkyBlockEmojisResponse;
import dev.sbs.api.data.Repository;
import dev.sbs.api.data.model.Model;
import dev.sbs.api.data.model.SqlModel;
import dev.sbs.api.data.model.discord.application_requirements.ApplicationRequirementSqlRepository;
import dev.sbs.api.data.model.discord.command_categories.CommandCategorySqlRepository;
import dev.sbs.api.data.model.discord.command_configs.CommandConfigSqlRepository;
import dev.sbs.api.data.model.discord.command_groups.CommandGroupSqlRepository;
import dev.sbs.api.data.model.discord.emojis.EmojiSqlRepository;
import dev.sbs.api.data.model.discord.guild_application_entries.GuildApplicationEntrySqlRepository;
import dev.sbs.api.data.model.discord.guild_application_requirements.GuildApplicationRequirementSqlRepository;
import dev.sbs.api.data.model.discord.guild_application_types.GuildApplicationTypeSqlRepository;
import dev.sbs.api.data.model.discord.guild_applications.GuildApplicationSqlRepository;
import dev.sbs.api.data.model.discord.guild_command_configs.GuildCommandConfigSqlRepository;
import dev.sbs.api.data.model.discord.guild_embeds.GuildEmbedSqlRepository;
import dev.sbs.api.data.model.discord.guild_report_types.GuildReportTypeSqlRepository;
import dev.sbs.api.data.model.discord.guild_reports.GuildReportSqlRepository;
import dev.sbs.api.data.model.discord.guild_reputation.GuildReputationSqlRepository;
import dev.sbs.api.data.model.discord.guild_reputation_types.GuildReputationTypeSqlRepository;
import dev.sbs.api.data.model.discord.guild_skyblock_events.GuildSkyBlockEventSqlRepository;
import dev.sbs.api.data.model.discord.guilds.GuildSqlRepository;
import dev.sbs.api.data.model.discord.optimizer_mob_types.OptimizerMobTypeSqlRepository;
import dev.sbs.api.data.model.discord.optimizer_support_items.OptimizerSupportItemSqlRepository;
import dev.sbs.api.data.model.discord.sbs_beta_testers.SbsBetaTesterSqlRepository;
import dev.sbs.api.data.model.discord.sbs_developers.SbsDeveloperSqlRepository;
import dev.sbs.api.data.model.discord.sbs_legacy_donors.SbsLegacyDonorSqlRepository;
import dev.sbs.api.data.model.discord.setting_types.SettingTypeSqlRepository;
import dev.sbs.api.data.model.discord.settings.SettingSqlRepository;
import dev.sbs.api.data.model.discord.skyblock_event_timers.SkyBlockEventTimerSqlRepository;
import dev.sbs.api.data.model.discord.skyblock_events.SkyBlockEventSqlRepository;
import dev.sbs.api.data.model.discord.users.UserSqlRepository;
import dev.sbs.api.data.model.skyblock.accessories.AccessorySqlRepository;
import dev.sbs.api.data.model.skyblock.accessory_enrichments.AccessoryEnrichmentSqlRepository;
import dev.sbs.api.data.model.skyblock.accessory_families.AccessoryFamilySqlRepository;
import dev.sbs.api.data.model.skyblock.accessory_powers.AccessoryPowerSqlRepository;
import dev.sbs.api.data.model.skyblock.bag_sizes.BagSizeSqlRepository;
import dev.sbs.api.data.model.skyblock.bags.BagSqlRepository;
import dev.sbs.api.data.model.skyblock.bonus_armor_sets.BonusArmorSetSqlRepository;
import dev.sbs.api.data.model.skyblock.bonus_enchantment_stats.BonusEnchantmentStatSqlRepository;
import dev.sbs.api.data.model.skyblock.bonus_item_stats.BonusItemStatSqlRepository;
import dev.sbs.api.data.model.skyblock.bonus_pet_ability_stats.BonusPetAbilityStatSqlRepository;
import dev.sbs.api.data.model.skyblock.bonus_reforge_stats.BonusReforgeStatSqlRepository;
import dev.sbs.api.data.model.skyblock.collection_item_tiers.CollectionItemTierSqlRepository;
import dev.sbs.api.data.model.skyblock.collection_items.CollectionItemSqlRepository;
import dev.sbs.api.data.model.skyblock.collections.CollectionSqlRepository;
import dev.sbs.api.data.model.skyblock.craftingtable_recipe_slots.CraftingTableRecipeSlotSqlRepository;
import dev.sbs.api.data.model.skyblock.craftingtable_recipes.CraftingTableRecipeSqlRepository;
import dev.sbs.api.data.model.skyblock.craftingtable_slots.CraftingTableSlotSqlRepository;
import dev.sbs.api.data.model.skyblock.dungeon_bosses.DungeonBossSqlRepository;
import dev.sbs.api.data.model.skyblock.dungeon_classes.DungeonClassSqlRepository;
import dev.sbs.api.data.model.skyblock.dungeon_fairy_souls.DungeonFairySoulSqlRepository;
import dev.sbs.api.data.model.skyblock.dungeon_floor_sizes.DungeonFloorSizeSqlRepository;
import dev.sbs.api.data.model.skyblock.dungeon_floors.DungeonFloorSqlRepository;
import dev.sbs.api.data.model.skyblock.dungeon_levels.DungeonLevelSqlRepository;
import dev.sbs.api.data.model.skyblock.dungeons.DungeonSqlRepository;
import dev.sbs.api.data.model.skyblock.enchantment_families.EnchantmentFamilySqlRepository;
import dev.sbs.api.data.model.skyblock.enchantment_stats.EnchantmentStatSqlRepository;
import dev.sbs.api.data.model.skyblock.enchantment_types.EnchantmentTypeSqlRepository;
import dev.sbs.api.data.model.skyblock.enchantments.EnchantmentSqlRepository;
import dev.sbs.api.data.model.skyblock.essence_perks.EssencePerkSqlRepository;
import dev.sbs.api.data.model.skyblock.fairy_exchanges.FairyExchangeSqlRepository;
import dev.sbs.api.data.model.skyblock.fairy_souls.FairySoulSqlRepository;
import dev.sbs.api.data.model.skyblock.formats.FormatSqlRepository;
import dev.sbs.api.data.model.skyblock.gemstone_stats.GemstoneStatSqlRepository;
import dev.sbs.api.data.model.skyblock.gemstone_types.GemstoneTypeSqlRepository;
import dev.sbs.api.data.model.skyblock.gemstones.GemstoneSqlRepository;
import dev.sbs.api.data.model.skyblock.guild_levels.GuildLevelSqlRepository;
import dev.sbs.api.data.model.skyblock.hot_potato_stats.HotPotatoStatSqlRepository;
import dev.sbs.api.data.model.skyblock.hotm_perk_stats.HotmPerkStatSqlRepository;
import dev.sbs.api.data.model.skyblock.hotm_perks.HotmPerkSqlRepository;
import dev.sbs.api.data.model.skyblock.item_types.ItemTypeSqlRepository;
import dev.sbs.api.data.model.skyblock.items.ItemSqlRepository;
import dev.sbs.api.data.model.skyblock.location_areas.LocationAreaSqlRepository;
import dev.sbs.api.data.model.skyblock.location_remotes.LocationRemoteSqlRepository;
import dev.sbs.api.data.model.skyblock.locations.LocationSqlRepository;
import dev.sbs.api.data.model.skyblock.melodys_songs.MelodySongSqlRepository;
import dev.sbs.api.data.model.skyblock.menus.MenuSqlRepository;
import dev.sbs.api.data.model.skyblock.minion_items.MinionItemSqlRepository;
import dev.sbs.api.data.model.skyblock.minion_tier_upgrades.MinionTierUpgradeSqlRepository;
import dev.sbs.api.data.model.skyblock.minion_tiers.MinionTierSqlRepository;
import dev.sbs.api.data.model.skyblock.minion_uniques.MinionUniqueSqlRepository;
import dev.sbs.api.data.model.skyblock.minions.MinionSqlRepository;
import dev.sbs.api.data.model.skyblock.npcs.NpcSqlRepository;
import dev.sbs.api.data.model.skyblock.pet_abilities.PetAbilitySqlRepository;
import dev.sbs.api.data.model.skyblock.pet_ability_stats.PetAbilityStatSqlRepository;
import dev.sbs.api.data.model.skyblock.pet_items.PetItemSqlRepository;
import dev.sbs.api.data.model.skyblock.pet_levels.PetLevelSqlRepository;
import dev.sbs.api.data.model.skyblock.pet_scores.PetScoreSqlRepository;
import dev.sbs.api.data.model.skyblock.pet_stats.PetStatSqlRepository;
import dev.sbs.api.data.model.skyblock.pet_types.PetTypeSqlRepository;
import dev.sbs.api.data.model.skyblock.pets.PetSqlRepository;
import dev.sbs.api.data.model.skyblock.potion_brew_buffs.PotionBrewBuffSqlRepository;
import dev.sbs.api.data.model.skyblock.potion_brews.PotionBrewSqlRepository;
import dev.sbs.api.data.model.skyblock.potion_group_items.PotionGroupItemSqlRepository;
import dev.sbs.api.data.model.skyblock.potion_groups.PotionGroupSqlRepository;
import dev.sbs.api.data.model.skyblock.potion_mixins.PotionMixinSqlRepository;
import dev.sbs.api.data.model.skyblock.potion_tiers.PotionTierSqlRepository;
import dev.sbs.api.data.model.skyblock.potions.PotionSqlRepository;
import dev.sbs.api.data.model.skyblock.profiles.ProfileSqlRepository;
import dev.sbs.api.data.model.skyblock.rarities.RaritySqlRepository;
import dev.sbs.api.data.model.skyblock.reforge_conditions.ReforgeConditionSqlRepository;
import dev.sbs.api.data.model.skyblock.reforge_stats.ReforgeStatSqlRepository;
import dev.sbs.api.data.model.skyblock.reforge_types.ReforgeTypeSqlRepository;
import dev.sbs.api.data.model.skyblock.reforges.ReforgeSqlRepository;
import dev.sbs.api.data.model.skyblock.sack_items.SackItemSqlRepository;
import dev.sbs.api.data.model.skyblock.sacks.SackSqlRepository;
import dev.sbs.api.data.model.skyblock.seasons.SeasonSqlRepository;
import dev.sbs.api.data.model.skyblock.shop_bit_enchanted_books.ShopBitEnchantedBookSqlRepository;
import dev.sbs.api.data.model.skyblock.shop_bit_item_craftables.ShopBitItemCraftableSqlRepository;
import dev.sbs.api.data.model.skyblock.shop_bit_items.ShopBitItemSqlRepository;
import dev.sbs.api.data.model.skyblock.shop_bit_types.ShopBitTypeSqlRepository;
import dev.sbs.api.data.model.skyblock.shop_profile_upgrades.ShopProfileUpgradeSqlRepository;
import dev.sbs.api.data.model.skyblock.skill_levels.SkillLevelSqlRepository;
import dev.sbs.api.data.model.skyblock.skills.SkillSqlRepository;
import dev.sbs.api.data.model.skyblock.slayer_levels.SlayerLevelSqlRepository;
import dev.sbs.api.data.model.skyblock.slayers.SlayerSqlRepository;
import dev.sbs.api.data.model.skyblock.stats.StatSqlRepository;
import dev.sbs.api.data.sql.SqlConfig;
import dev.sbs.api.data.sql.SqlRepository;
import dev.sbs.api.data.sql.SqlSession;
import dev.sbs.api.data.sql.exception.SqlException;
import dev.sbs.api.manager.builder.BuilderManager;
import dev.sbs.api.manager.service.ServiceManager;
import dev.sbs.api.minecraft.nbt.NbtFactory;
import dev.sbs.api.minecraft.text.MinecraftTextBuilder;
import dev.sbs.api.minecraft.text.MinecraftTextObject;
import dev.sbs.api.scheduler.Scheduler;
import dev.sbs.api.util.HypixelConfig;
import dev.sbs.api.util.SimplifiedException;
import dev.sbs.api.util.builder.string.StringBuilder;
import dev.sbs.api.util.collection.concurrent.Concurrent;
import dev.sbs.api.util.collection.concurrent.ConcurrentList;
import feign.gson.DoubleToIntMapTypeAdapter;
import lombok.SneakyThrows;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;

public final class SimplifiedApi {

    private static final ServiceManager serviceManager = new ServiceManager();
    private static final BuilderManager builderManager = new BuilderManager();

    static {
        // Load Config
        HypixelConfig config;
        try {
            File currentDir = new File(SimplifiedApi.class.getProtectionDomain().getCodeSource().getLocation().toURI());
            config = new HypixelConfig(currentDir.getParentFile(), "simplified");
        } catch (Exception exception) {
            throw new IllegalArgumentException("Unable to retrieve current directory", exception); // Should never get here
        }

        // Load Gson
        Gson gson = new GsonBuilder()
            .registerTypeAdapter(new TypeToken<Map<String, Object>>() {}.getType(), new DoubleToIntMapTypeAdapter()) // Feign
            .registerTypeAdapter(Instant.class, new InstantTypeAdapter())
            .registerTypeAdapter(UUID.class, new UUIDTypeAdapter())
            .registerTypeAdapter(SkyBlockIsland.NbtContent.class, new NbtContentTypeAdapter())
            .registerTypeAdapter(SkyBlockDate.RealTime.class, new SkyBlockRealTimeTypeAdapter())
            .registerTypeAdapter(SkyBlockDate.SkyBlockTime.class, new SkyBlockTimeTypeAdapter())
            .registerTypeAdapter(SkyBlockIsland.class, new SkyBlockIsland.Deserializer())
            .registerTypeAdapter(SkyBlockEmojisResponse.class, new SkyBlockEmojisResponse.Deserializer())
            .setPrettyPrinting().create();

        // Provide Services
        serviceManager.add(HypixelConfig.class, config);
        serviceManager.add(Gson.class, gson);
        serviceManager.add(NbtFactory.class, new NbtFactory());
        serviceManager.add(Scheduler.class, new Scheduler());

        // Create Api Builders
        SbsApiBuilder sbsApiBuilder = new SbsApiBuilder();
        HypixelApiBuilder hypixelApiBuilder = new HypixelApiBuilder();

        // Provide Builders
        builderManager.add(MojangData.class, SbsApiBuilder.class);
        builderManager.add(SkyBlockData.class, SbsApiBuilder.class);
        builderManager.add(HypixelPlayerData.class, HypixelApiBuilder.class);
        builderManager.add(HypixelResourceData.class, HypixelApiBuilder.class);
        builderManager.add(HypixelSkyBlockData.class, HypixelApiBuilder.class);
        builderManager.add(String.class, StringBuilder.class);
        builderManager.add(MinecraftTextObject.class, MinecraftTextBuilder.class);

        // Provide Client Api Implementations
        serviceManager.add(HypixelPlayerData.class, hypixelApiBuilder.build(HypixelPlayerData.class));
        serviceManager.add(HypixelResourceData.class, hypixelApiBuilder.build(HypixelResourceData.class));
        serviceManager.add(HypixelSkyBlockData.class, hypixelApiBuilder.build(HypixelSkyBlockData.class));
        serviceManager.add(MojangData.class, sbsApiBuilder.build(MojangData.class));
        serviceManager.add(SkyBlockData.class, sbsApiBuilder.build(SkyBlockData.class));
    }

    public static void connectDatabase(SqlConfig sqlConfig) {
        if (!serviceManager.isRegistered(SqlSession.class)) {
            // Create SqlSession
            SqlSession sqlSession = new SqlSession(sqlConfig, getAllSqlRepositoryClasses());
            serviceManager.add(SqlSession.class, sqlSession);

            // Initialize Database
            sqlSession.initialize();

            // Cache Repositories
            sqlSession.cacheRepositories();
        } else
            serviceManager.get(SqlSession.class).initialize(); // Reinitialize Database
    }

    public static void disconnectDatabase() {
        if (serviceManager.isRegistered(SqlSession.class)) {
            SqlSession sqlSession = serviceManager.get(SqlSession.class);

            if (sqlSession.isActive())
                sqlSession.shutdown();
        } else
            throw SimplifiedException.of(SqlException.class)
                .withMessage("Database is not active!")
                .build();
    }

    public static BuilderManager getBuilderManager() {
        return builderManager;
    }

    public static HypixelConfig getConfig() {
        return serviceManager.get(HypixelConfig.class);
    }

    @SneakyThrows
    public static File getCurrentDirectory() {
        return new File(SimplifiedApi.class.getProtectionDomain().getCodeSource().getLocation().toURI());
    }

    public static Gson getGson() {
        return serviceManager.get(Gson.class);
    }

    public static Logger getLog(Class<?> tClass) {
        return (Logger) LoggerFactory.getLogger(tClass);
    }

    public static Logger getLog(String name) {
        return (Logger) LoggerFactory.getLogger(name);
    }

    public static NbtFactory getNbtFactory() {
        return serviceManager.get(NbtFactory.class);
    }

    public static Scheduler getScheduler() {
        return serviceManager.get(Scheduler.class);
    }

    /**
     * Gets the {@link Repository<T>} caching all items of type {@link T}.
     *
     * @param tClass The {@link Model} class to find.
     * @param <T> The type of model.
     * @return The repository of type {@link T}.
     */
    public static <T extends Model> Repository<T> getRepositoryOf(Class<T> tClass) {
        return getSqlSession().getRepositoryOf(tClass);
    }

    private static ConcurrentList<Class<? extends SqlRepository<? extends SqlModel>>> getAllSqlRepositoryClasses() {
        return Concurrent.newUnmodifiableList(
            // No Foreign Keys
            AccessoryFamilySqlRepository.class,
            ApplicationRequirementSqlRepository.class,
            CraftingTableRecipeSqlRepository.class,
            CraftingTableSlotSqlRepository.class,
            CommandConfigSqlRepository.class,
            CommandGroupSqlRepository.class,
            DungeonBossSqlRepository.class,
            DungeonFairySoulSqlRepository.class,
            DungeonFloorSizeSqlRepository.class,
            DungeonLevelSqlRepository.class,
            EnchantmentFamilySqlRepository.class,
            FairyExchangeSqlRepository.class,
            FormatSqlRepository.class,
            GemstoneTypeSqlRepository.class,
            GuildLevelSqlRepository.class,
            GuildSqlRepository.class,
            HotmPerkSqlRepository.class,
            ItemTypeSqlRepository.class,
            LocationSqlRepository.class,
            LocationRemoteSqlRepository.class,
            MelodySongSqlRepository.class,
            MenuSqlRepository.class,
            MinionUniqueSqlRepository.class,
            OptimizerMobTypeSqlRepository.class,
            PetLevelSqlRepository.class,
            PetScoreSqlRepository.class,
            PetTypeSqlRepository.class,
            PotionSqlRepository.class,
            PotionGroupSqlRepository.class,
            RaritySqlRepository.class,
            ReforgeTypeSqlRepository.class,
            SackSqlRepository.class,
            SbsBetaTesterSqlRepository.class,
            SbsDeveloperSqlRepository.class,
            SbsLegacyDonorSqlRepository.class,
            SeasonSqlRepository.class,
            SettingTypeSqlRepository.class,
            ShopBitTypeSqlRepository.class,
            ShopProfileUpgradeSqlRepository.class,
            UserSqlRepository.class,

            // Requires Above
            EmojiSqlRepository.class,
            CommandCategorySqlRepository.class,
            CraftingTableRecipeSlotSqlRepository.class,
            EnchantmentSqlRepository.class,
            GuildApplicationTypeSqlRepository.class,
            GuildCommandConfigSqlRepository.class,
            GuildEmbedSqlRepository.class,
            GuildReportTypeSqlRepository.class,
            GuildReputationTypeSqlRepository.class,
            ItemSqlRepository.class,
            LocationAreaSqlRepository.class,
            ReforgeSqlRepository.class,
            ReforgeStatSqlRepository.class,
            SettingSqlRepository.class,
            SkyBlockEventSqlRepository.class,
            StatSqlRepository.class,

            // Requires Above
            AccessorySqlRepository.class,
            AccessoryEnrichmentSqlRepository.class,
            AccessoryPowerSqlRepository.class,
            BonusArmorSetSqlRepository.class,
            BonusEnchantmentStatSqlRepository.class,
            BonusItemStatSqlRepository.class,
            BonusPetAbilityStatSqlRepository.class,
            BonusReforgeStatSqlRepository.class,
            DungeonSqlRepository.class,
            DungeonClassSqlRepository.class,
            EnchantmentStatSqlRepository.class,
            EnchantmentTypeSqlRepository.class,
            EssencePerkSqlRepository.class,
            FairySoulSqlRepository.class,
            GemstoneSqlRepository.class,
            GuildApplicationSqlRepository.class,
            GuildReportSqlRepository.class,
            GuildReputationSqlRepository.class,
            GuildSkyBlockEventSqlRepository.class,
            HotmPerkStatSqlRepository.class,
            HotPotatoStatSqlRepository.class,
            NpcSqlRepository.class,
            OptimizerSupportItemSqlRepository.class,
            PetSqlRepository.class,
            PetItemSqlRepository.class,
            PotionTierSqlRepository.class,
            ProfileSqlRepository.class,
            ReforgeConditionSqlRepository.class,
            SackItemSqlRepository.class,
            ShopBitEnchantedBookSqlRepository.class,
            ShopBitItemSqlRepository.class,
            SkillSqlRepository.class,
            SkyBlockEventTimerSqlRepository.class,
            SlayerSqlRepository.class,

            // Requires Above
            CollectionSqlRepository.class,
            DungeonFloorSqlRepository.class,
            GemstoneStatSqlRepository.class,
            GuildApplicationEntrySqlRepository.class,
            GuildApplicationRequirementSqlRepository.class,
            PetAbilitySqlRepository.class,
            PetStatSqlRepository.class,
            PotionBrewSqlRepository.class,
            PotionGroupItemSqlRepository.class,
            PotionMixinSqlRepository.class,
            ShopBitItemCraftableSqlRepository.class,
            SkillLevelSqlRepository.class,
            SlayerLevelSqlRepository.class,

            // Requires Above
            CollectionItemSqlRepository.class,
            MinionSqlRepository.class,
            PetAbilityStatSqlRepository.class,
            PotionBrewBuffSqlRepository.class,

            // Requires Above
            BagSqlRepository.class,
            CollectionItemTierSqlRepository.class,
            MinionItemSqlRepository.class,
            MinionTierSqlRepository.class,

            // Requires Above
            BagSizeSqlRepository.class,
            MinionTierUpgradeSqlRepository.class
        );
    }

    public static SqlSession getSqlSession() {
        if (serviceManager.isRegistered(SqlSession.class))
            return serviceManager.get(SqlSession.class);
        else
            throw SimplifiedException.of(SqlException.class)
                .withMessage("Database has not been initialized!")
                .build();
    }

    public static <T extends RequestInterface> T getWebApi(Class<T> tClass) {
        return serviceManager.get(tClass);
    }

    public static boolean isDatabaseConnected() {
        return serviceManager.isRegistered(SqlSession.class) && serviceManager.get(SqlSession.class).isActive();
    }

}
