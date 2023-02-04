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
import dev.sbs.api.client.antisniper.AntiSniperApiBuilder;
import dev.sbs.api.client.antisniper.request.NickRequest;
import dev.sbs.api.client.hypixel.HypixelApiBuilder;
import dev.sbs.api.client.hypixel.request.HypixelPlayerRequest;
import dev.sbs.api.client.hypixel.request.HypixelResourceRequest;
import dev.sbs.api.client.hypixel.request.HypixelSkyBlockData;
import dev.sbs.api.client.hypixel.response.skyblock.implementation.SkyBlockDate;
import dev.sbs.api.client.hypixel.response.skyblock.implementation.island.util.NbtContent;
import dev.sbs.api.client.sbs.SbsApiBuilder;
import dev.sbs.api.client.sbs.request.MojangRequest;
import dev.sbs.api.client.sbs.request.SkyBlockRequest;
import dev.sbs.api.client.sbs.response.SkyBlockEmojisResponse;
import dev.sbs.api.data.Repository;
import dev.sbs.api.data.model.Model;
import dev.sbs.api.data.model.SqlModel;
import dev.sbs.api.data.model.discord.application_requirements.ApplicationRequirementSqlModel;
import dev.sbs.api.data.model.discord.command_categories.CommandCategorySqlModel;
import dev.sbs.api.data.model.discord.command_configs.CommandConfigSqlModel;
import dev.sbs.api.data.model.discord.command_groups.CommandGroupSqlModel;
import dev.sbs.api.data.model.discord.emojis.EmojiSqlModel;
import dev.sbs.api.data.model.discord.guild_data.guild_application_entries.GuildApplicationEntrySqlModel;
import dev.sbs.api.data.model.discord.guild_data.guild_application_requirements.GuildApplicationRequirementSqlModel;
import dev.sbs.api.data.model.discord.guild_data.guild_application_types.GuildApplicationTypeSqlModel;
import dev.sbs.api.data.model.discord.guild_data.guild_applications.GuildApplicationSqlModel;
import dev.sbs.api.data.model.discord.guild_data.guild_command_configs.GuildCommandConfigSqlModel;
import dev.sbs.api.data.model.discord.guild_data.guild_embeds.GuildEmbedSqlModel;
import dev.sbs.api.data.model.discord.guild_data.guild_report_types.GuildReportTypeSqlModel;
import dev.sbs.api.data.model.discord.guild_data.guild_reports.GuildReportSqlModel;
import dev.sbs.api.data.model.discord.guild_data.guild_reputation.GuildReputationSqlModel;
import dev.sbs.api.data.model.discord.guild_data.guild_reputation_types.GuildReputationTypeSqlModel;
import dev.sbs.api.data.model.discord.guild_data.guild_skyblock_events.GuildSkyBlockEventSqlModel;
import dev.sbs.api.data.model.discord.guild_data.guilds.GuildSqlModel;
import dev.sbs.api.data.model.discord.optimizer_mob_types.OptimizerMobTypeSqlModel;
import dev.sbs.api.data.model.discord.optimizer_support_items.OptimizerSupportItemSqlModel;
import dev.sbs.api.data.model.discord.sbs_beta_testers.SbsBetaTesterSqlModel;
import dev.sbs.api.data.model.discord.sbs_developers.SbsDeveloperSqlModel;
import dev.sbs.api.data.model.discord.sbs_legacy_donors.SbsLegacyDonorSqlModel;
import dev.sbs.api.data.model.discord.setting_types.SettingTypeSqlModel;
import dev.sbs.api.data.model.discord.settings.SettingSqlModel;
import dev.sbs.api.data.model.discord.skyblock_event_timers.SkyBlockEventTimerSqlModel;
import dev.sbs.api.data.model.discord.skyblock_events.SkyBlockEventSqlModel;
import dev.sbs.api.data.model.discord.users.UserSqlModel;
import dev.sbs.api.data.model.skyblock.accessory_data.accessories.AccessorySqlModel;
import dev.sbs.api.data.model.skyblock.accessory_data.accessory_enrichments.AccessoryEnrichmentSqlModel;
import dev.sbs.api.data.model.skyblock.accessory_data.accessory_families.AccessoryFamilySqlModel;
import dev.sbs.api.data.model.skyblock.accessory_data.accessory_powers.AccessoryPowerSqlModel;
import dev.sbs.api.data.model.skyblock.bag_sizes.BagSizeSqlModel;
import dev.sbs.api.data.model.skyblock.bags.BagSqlModel;
import dev.sbs.api.data.model.skyblock.bonus_data.bonus_armor_sets.BonusArmorSetSqlModel;
import dev.sbs.api.data.model.skyblock.bonus_data.bonus_enchantment_stats.BonusEnchantmentStatSqlModel;
import dev.sbs.api.data.model.skyblock.bonus_data.bonus_item_stats.BonusItemStatSqlModel;
import dev.sbs.api.data.model.skyblock.bonus_data.bonus_pet_ability_stats.BonusPetAbilityStatSqlModel;
import dev.sbs.api.data.model.skyblock.bonus_data.bonus_reforge_stats.BonusReforgeStatSqlModel;
import dev.sbs.api.data.model.skyblock.collection_data.collection_item_tiers.CollectionItemTierSqlModel;
import dev.sbs.api.data.model.skyblock.collection_data.collection_items.CollectionItemSqlModel;
import dev.sbs.api.data.model.skyblock.collection_data.collections.CollectionSqlModel;
import dev.sbs.api.data.model.skyblock.craftingtable_data.craftingtable_recipe_slots.CraftingTableRecipeSlotSqlModel;
import dev.sbs.api.data.model.skyblock.craftingtable_data.craftingtable_recipes.CraftingTableRecipeSqlModel;
import dev.sbs.api.data.model.skyblock.craftingtable_data.craftingtable_slots.CraftingTableSlotSqlModel;
import dev.sbs.api.data.model.skyblock.dungeon_data.dungeon_bosses.DungeonBossSqlModel;
import dev.sbs.api.data.model.skyblock.dungeon_data.dungeon_classes.DungeonClassSqlModel;
import dev.sbs.api.data.model.skyblock.dungeon_data.dungeon_fairy_souls.DungeonFairySoulSqlModel;
import dev.sbs.api.data.model.skyblock.dungeon_data.dungeon_floor_sizes.DungeonFloorSizeSqlModel;
import dev.sbs.api.data.model.skyblock.dungeon_data.dungeon_floors.DungeonFloorSqlModel;
import dev.sbs.api.data.model.skyblock.dungeon_data.dungeon_levels.DungeonLevelSqlModel;
import dev.sbs.api.data.model.skyblock.dungeon_data.dungeons.DungeonSqlModel;
import dev.sbs.api.data.model.skyblock.enchantment_data.enchantment_families.EnchantmentFamilySqlModel;
import dev.sbs.api.data.model.skyblock.enchantment_data.enchantment_stats.EnchantmentStatSqlModel;
import dev.sbs.api.data.model.skyblock.enchantment_data.enchantment_types.EnchantmentTypeSqlModel;
import dev.sbs.api.data.model.skyblock.enchantment_data.enchantments.EnchantmentSqlModel;
import dev.sbs.api.data.model.skyblock.essence_perks.EssencePerkSqlModel;
import dev.sbs.api.data.model.skyblock.fairy_souls.FairySoulSqlModel;
import dev.sbs.api.data.model.skyblock.formats.FormatSqlModel;
import dev.sbs.api.data.model.skyblock.gemstone_data.gemstone_stats.GemstoneStatSqlModel;
import dev.sbs.api.data.model.skyblock.gemstone_data.gemstone_types.GemstoneTypeSqlModel;
import dev.sbs.api.data.model.skyblock.gemstone_data.gemstones.GemstoneSqlModel;
import dev.sbs.api.data.model.skyblock.guild_levels.GuildLevelSqlModel;
import dev.sbs.api.data.model.skyblock.hot_potato_stats.HotPotatoStatSqlModel;
import dev.sbs.api.data.model.skyblock.hotm_perk_stats.HotmPerkStatSqlModel;
import dev.sbs.api.data.model.skyblock.hotm_perks.HotmPerkSqlModel;
import dev.sbs.api.data.model.skyblock.item_types.ItemTypeSqlModel;
import dev.sbs.api.data.model.skyblock.items.ItemSqlModel;
import dev.sbs.api.data.model.skyblock.location_data.location_areas.LocationAreaSqlModel;
import dev.sbs.api.data.model.skyblock.location_data.location_remotes.LocationRemoteSqlModel;
import dev.sbs.api.data.model.skyblock.location_data.locations.LocationSqlModel;
import dev.sbs.api.data.model.skyblock.melodys_songs.MelodySongSqlModel;
import dev.sbs.api.data.model.skyblock.menus.MenuSqlModel;
import dev.sbs.api.data.model.skyblock.minion_data.minion_items.MinionItemSqlModel;
import dev.sbs.api.data.model.skyblock.minion_data.minion_tier_upgrades.MinionTierUpgradeSqlModel;
import dev.sbs.api.data.model.skyblock.minion_data.minion_tiers.MinionTierSqlModel;
import dev.sbs.api.data.model.skyblock.minion_data.minion_uniques.MinionUniqueSqlModel;
import dev.sbs.api.data.model.skyblock.minion_data.minions.MinionSqlModel;
import dev.sbs.api.data.model.skyblock.npcs.NpcSqlModel;
import dev.sbs.api.data.model.skyblock.pet_data.pet_abilities.PetAbilitySqlModel;
import dev.sbs.api.data.model.skyblock.pet_data.pet_ability_stats.PetAbilityStatSqlModel;
import dev.sbs.api.data.model.skyblock.pet_data.pet_items.PetItemSqlModel;
import dev.sbs.api.data.model.skyblock.pet_data.pet_levels.PetLevelSqlModel;
import dev.sbs.api.data.model.skyblock.pet_data.pet_scores.PetScoreSqlModel;
import dev.sbs.api.data.model.skyblock.pet_data.pet_stats.PetStatSqlModel;
import dev.sbs.api.data.model.skyblock.pet_data.pet_types.PetTypeSqlModel;
import dev.sbs.api.data.model.skyblock.pet_data.pets.PetSqlModel;
import dev.sbs.api.data.model.skyblock.potion_data.potion_brew_buffs.PotionBrewBuffSqlModel;
import dev.sbs.api.data.model.skyblock.potion_data.potion_brews.PotionBrewSqlModel;
import dev.sbs.api.data.model.skyblock.potion_data.potion_group_items.PotionGroupItemSqlModel;
import dev.sbs.api.data.model.skyblock.potion_data.potion_groups.PotionGroupSqlModel;
import dev.sbs.api.data.model.skyblock.potion_data.potion_mixins.PotionMixinSqlModel;
import dev.sbs.api.data.model.skyblock.potion_data.potion_tiers.PotionTierSqlModel;
import dev.sbs.api.data.model.skyblock.potion_data.potions.PotionSqlModel;
import dev.sbs.api.data.model.skyblock.profiles.ProfileSqlModel;
import dev.sbs.api.data.model.skyblock.rarities.RaritySqlModel;
import dev.sbs.api.data.model.skyblock.reforge_data.reforge_conditions.ReforgeConditionSqlModel;
import dev.sbs.api.data.model.skyblock.reforge_data.reforge_stats.ReforgeStatSqlModel;
import dev.sbs.api.data.model.skyblock.reforge_data.reforges.ReforgeSqlModel;
import dev.sbs.api.data.model.skyblock.sack_items.SackItemSqlModel;
import dev.sbs.api.data.model.skyblock.sacks.SackSqlModel;
import dev.sbs.api.data.model.skyblock.seasons.SeasonSqlModel;
import dev.sbs.api.data.model.skyblock.shop_data.shop_bit_enchanted_books.ShopBitEnchantedBookSqlModel;
import dev.sbs.api.data.model.skyblock.shop_data.shop_bit_item_craftables.ShopBitItemCraftableSqlModel;
import dev.sbs.api.data.model.skyblock.shop_data.shop_bit_items.ShopBitItemSqlModel;
import dev.sbs.api.data.model.skyblock.shop_data.shop_bit_types.ShopBitTypeSqlModel;
import dev.sbs.api.data.model.skyblock.shop_data.shop_profile_upgrades.ShopProfileUpgradeSqlModel;
import dev.sbs.api.data.model.skyblock.skill_levels.SkillLevelSqlModel;
import dev.sbs.api.data.model.skyblock.skills.SkillSqlModel;
import dev.sbs.api.data.model.skyblock.slayer_levels.SlayerLevelSqlModel;
import dev.sbs.api.data.model.skyblock.slayers.SlayerSqlModel;
import dev.sbs.api.data.model.skyblock.stats.StatSqlModel;
import dev.sbs.api.data.sql.SqlConfig;
import dev.sbs.api.data.sql.SqlSession;
import dev.sbs.api.data.sql.exception.SqlException;
import dev.sbs.api.manager.builder.BuilderManager;
import dev.sbs.api.manager.service.ServiceManager;
import dev.sbs.api.minecraft.nbt.NbtFactory;
import dev.sbs.api.minecraft.text.MinecraftTextBuilder;
import dev.sbs.api.minecraft.text.MinecraftTextObject;
import dev.sbs.api.scheduler.Scheduler;
import dev.sbs.api.util.HypixelConfig;
import dev.sbs.api.util.SerializedPathTypeAdaptorFactory;
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
            .registerTypeAdapter(NbtContent.class, new NbtContentTypeAdapter())
            .registerTypeAdapter(SkyBlockDate.RealTime.class, new SkyBlockRealTimeTypeAdapter())
            .registerTypeAdapter(SkyBlockDate.SkyBlockTime.class, new SkyBlockTimeTypeAdapter())
            .registerTypeAdapter(SkyBlockEmojisResponse.class, new SkyBlockEmojisResponse.Deserializer())
            .registerTypeAdapterFactory(new SerializedPathTypeAdaptorFactory())
            .setPrettyPrinting()
            .create();

        // Provide Services
        serviceManager.add(HypixelConfig.class, config);
        serviceManager.add(Gson.class, gson);
        serviceManager.add(NbtFactory.class, new NbtFactory());
        serviceManager.add(Scheduler.class, new Scheduler());

        // Provide Builders
        builderManager.add(MojangRequest.class, SbsApiBuilder.class);
        builderManager.add(SkyBlockRequest.class, SbsApiBuilder.class);
        builderManager.add(HypixelPlayerRequest.class, HypixelApiBuilder.class);
        builderManager.add(HypixelResourceRequest.class, HypixelApiBuilder.class);
        builderManager.add(HypixelSkyBlockData.class, HypixelApiBuilder.class);
        builderManager.add(NickRequest.class, AntiSniperApiBuilder.class);
        builderManager.add(String.class, StringBuilder.class);
        builderManager.add(MinecraftTextObject.class, MinecraftTextBuilder.class);

        // Create Api Builders
        SbsApiBuilder sbsApiBuilder = new SbsApiBuilder();
        HypixelApiBuilder hypixelApiBuilder = new HypixelApiBuilder();
        AntiSniperApiBuilder antiSniperApiBuilder = new AntiSniperApiBuilder();

        // Provide Client Api Implementations
        serviceManager.add(HypixelPlayerRequest.class, hypixelApiBuilder.build(HypixelPlayerRequest.class));
        serviceManager.add(HypixelResourceRequest.class, hypixelApiBuilder.build(HypixelResourceRequest.class));
        serviceManager.add(HypixelSkyBlockData.class, hypixelApiBuilder.build(HypixelSkyBlockData.class));
        serviceManager.add(MojangRequest.class, sbsApiBuilder.build(MojangRequest.class));
        serviceManager.add(SkyBlockRequest.class, sbsApiBuilder.build(SkyBlockRequest.class));
        serviceManager.add(NickRequest.class, antiSniperApiBuilder.build(NickRequest.class));
    }

    public static void connectDatabase(SqlConfig sqlConfig) {
        if (!serviceManager.isRegistered(SqlSession.class)) {
            // Create SqlSession
            SqlSession sqlSession = new SqlSession(sqlConfig, getAllSqlModels());
            serviceManager.add(SqlSession.class, sqlSession);

            // Cache Repositories
            sqlSession.cacheRepositories();

            // Initialize Database
            sqlSession.initialize();
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

    private static ConcurrentList<Class<? extends SqlModel>> getAllSqlModels() {
        return Concurrent.newUnmodifiableList(
            // No Foreign Keys
            AccessoryFamilySqlModel.class,
            ApplicationRequirementSqlModel.class,
            CraftingTableRecipeSqlModel.class,
            CraftingTableSlotSqlModel.class,
            CommandConfigSqlModel.class,
            CommandGroupSqlModel.class,
            DungeonBossSqlModel.class,
            DungeonFairySoulSqlModel.class,
            DungeonFloorSizeSqlModel.class,
            DungeonLevelSqlModel.class,
            EnchantmentFamilySqlModel.class,
            FormatSqlModel.class,
            GemstoneTypeSqlModel.class,
            GuildLevelSqlModel.class,
            GuildSqlModel.class,
            HotmPerkSqlModel.class,
            ItemTypeSqlModel.class,
            LocationSqlModel.class,
            LocationRemoteSqlModel.class,
            MelodySongSqlModel.class,
            MenuSqlModel.class,
            MinionUniqueSqlModel.class,
            OptimizerMobTypeSqlModel.class,
            PetLevelSqlModel.class,
            PetScoreSqlModel.class,
            PetTypeSqlModel.class,
            PotionSqlModel.class,
            PotionGroupSqlModel.class,
            RaritySqlModel.class,
            SackSqlModel.class,
            SbsBetaTesterSqlModel.class,
            SbsDeveloperSqlModel.class,
            SbsLegacyDonorSqlModel.class,
            SeasonSqlModel.class,
            SettingTypeSqlModel.class,
            ShopBitTypeSqlModel.class,
            ShopProfileUpgradeSqlModel.class,
            UserSqlModel.class,

            // Requires Above
            EmojiSqlModel.class,
            CommandCategorySqlModel.class,
            CraftingTableRecipeSlotSqlModel.class,
            EnchantmentSqlModel.class,
            GuildApplicationTypeSqlModel.class,
            GuildCommandConfigSqlModel.class,
            GuildEmbedSqlModel.class,
            GuildReportTypeSqlModel.class,
            GuildReputationTypeSqlModel.class,
            ItemSqlModel.class,
            LocationAreaSqlModel.class,
            ReforgeSqlModel.class,
            ReforgeStatSqlModel.class,
            SettingSqlModel.class,
            SkyBlockEventSqlModel.class,
            StatSqlModel.class,

            // Requires Above
            AccessorySqlModel.class,
            AccessoryEnrichmentSqlModel.class,
            AccessoryPowerSqlModel.class,
            BonusArmorSetSqlModel.class,
            BonusEnchantmentStatSqlModel.class,
            BonusItemStatSqlModel.class,
            BonusPetAbilityStatSqlModel.class,
            BonusReforgeStatSqlModel.class,
            DungeonSqlModel.class,
            DungeonClassSqlModel.class,
            EnchantmentStatSqlModel.class,
            EnchantmentTypeSqlModel.class,
            EssencePerkSqlModel.class,
            FairySoulSqlModel.class,
            GemstoneSqlModel.class,
            GuildApplicationSqlModel.class,
            GuildReportSqlModel.class,
            GuildReputationSqlModel.class,
            GuildSkyBlockEventSqlModel.class,
            HotmPerkStatSqlModel.class,
            HotPotatoStatSqlModel.class,
            NpcSqlModel.class,
            OptimizerSupportItemSqlModel.class,
            PetSqlModel.class,
            PetItemSqlModel.class,
            PotionTierSqlModel.class,
            ProfileSqlModel.class,
            ReforgeConditionSqlModel.class,
            SackItemSqlModel.class,
            ShopBitEnchantedBookSqlModel.class,
            ShopBitItemSqlModel.class,
            SkillSqlModel.class,
            SkyBlockEventTimerSqlModel.class,
            SlayerSqlModel.class,

            // Requires Above
            CollectionSqlModel.class,
            DungeonFloorSqlModel.class,
            GemstoneStatSqlModel.class,
            GuildApplicationEntrySqlModel.class,
            GuildApplicationRequirementSqlModel.class,
            PetAbilitySqlModel.class,
            PetStatSqlModel.class,
            PotionBrewSqlModel.class,
            PotionGroupItemSqlModel.class,
            PotionMixinSqlModel.class,
            ShopBitItemCraftableSqlModel.class,
            SkillLevelSqlModel.class,
            SlayerLevelSqlModel.class,

            // Requires Above
            CollectionItemSqlModel.class,
            MinionSqlModel.class,
            PetAbilityStatSqlModel.class,
            PotionBrewBuffSqlModel.class,

            // Requires Above
            BagSqlModel.class,
            CollectionItemTierSqlModel.class,
            MinionItemSqlModel.class,
            MinionTierSqlModel.class,

            // Requires Above
            BagSizeSqlModel.class,
            MinionTierUpgradeSqlModel.class
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
