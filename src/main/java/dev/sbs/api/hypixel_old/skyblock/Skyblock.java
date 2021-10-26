package dev.sbs.api.hypixel_old.skyblock;

import dev.sbs.api.minecraft.text.MinecraftChatFormatting;
import dev.sbs.api.util.concurrent.Concurrent;
import dev.sbs.api.util.concurrent.ConcurrentList;
import dev.sbs.api.util.concurrent.ConcurrentMap;
import dev.sbs.api.util.concurrent.ConcurrentSet;
import dev.sbs.api.util.concurrent.linked.ConcurrentLinkedMap;
import dev.sbs.api.util.helper.WordUtil;
import dev.sbs.api.util.helper.FormatUtil;
import dev.sbs.api.util.helper.NumberUtil;
import dev.sbs.api.util.helper.RegexUtil;

import java.text.DecimalFormat;
import java.util.*;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

public class Skyblock {

    private static final ConcurrentSet<String> skyblockInAllLanguages = Concurrent.newSet("SKYBLOCK", "\u7A7A\u5C9B\u751F\u5B58");
    private static final DecimalFormat smallDecimalFormat = new DecimalFormat("#0.#");
    public final static ConcurrentLinkedMap<Integer, Integer> UNIQUE_CRAFTS = Concurrent.newLinkedMap();
    public final static ConcurrentList<Integer> RUNECRAFTING_EXP_SCALE;
    public final static ConcurrentList<Integer> PET_EXP_SCALE;
    public final static ConcurrentList<Integer> PET_SCORE_BREAKPOINTS;
    public final static ConcurrentMap<Item.Rarity, ConcurrentList<Integer>> PET_EXP_TIER_SCALE = Concurrent.newMap();
    public final static ConcurrentMap<Item.Rarity, Integer> PET_EXP_SCALE_OFFSET = Concurrent.newMap();
    public final static ConcurrentMap<Slayer, ConcurrentList<Integer>> SLAYER_EXP_SCALE = Concurrent.newMap();

    public enum Sack {

        AGRONOMY(Item.WHEAT, Item.CARROT, Item.POTATO, Item.PUMPKIN, Item.MELON, Item.SEEDS, Item.RED_MUSHROOM, Item.COCOA_BEANS, Item.CACTUS, Item.SUGAR_CANE, Item.NETHER_WART),
        HUSBANDRY(Item.FEATHER, Item.LEATHER, Item.RAW_PORKCHOP, Item.RAW_CHICKEN, Item.MUTTON, Item.RABBIT),
        MINING(Item.COBBLESTONE, Item.COAL, Item.IRON_INGOT, Item.GOLD_INGOT, Item.DIAMOND, Item.LAPIS_LAZULI, Item.EMERALD, Item.REDSTONE_DUST, Item.NETHER_QUARTZ, Item.OBSIDIAN, Item.GLOWSTONE_DUST, Item.GRAVEL, Item.ICE, Item.NETHERRACK, Item.SAND, Item.END_STONE),
        COMBAT(Item.ROTTEN_FLESH, Item.BONE, Item.STRING, Item.SPIDER_EYE, Item.GUNPOWDER, Item.ENDER_PEARL, Item.GHAST_TEAR, Item.SLIME_BALL, Item.BLAZE_ROD, Item.MAGMA_CREAM),
        FORAGING(Item.OAK_WOOD, Item.SPRUCE_WOOD, Item.BIRCH_WOOD, Item.DARK_OAK_WOOD, Item.ACACIA_WOOD, Item.JUNGLE_WOOD),
        FISHING(Item.RAW_FISH, Item.RAW_SALMON, Item.CLOWNFISH, Item.PUFFERFISH, Item.PRISMARINE_SHARD, Item.PRISMARINE_CRYSTALS, Item.CLAY, Item.LILY_PAD, Item.INK_SACK, Item.SPONGE),
        SLAYER(Item.TARANTULA_WEB, Item.REVENANT_FLESH, Item.WOLF_TOOTH);

        private final ConcurrentList<Item> items;

        Sack(Item... items) {
            this.items = Concurrent.newList(items);
        }

        public ConcurrentList<Item> getItems() {
            return this.items;
        }

        public String getPrettyName() {
            return WordUtil.capitalizeFully(this.name());
        }

    }

    public enum Bag {

        ENDER_CHEST("OBSIDIAN",
                new Size(0, 27),
                new Size(5, 36),
                new Size(7, 45),
                new Size(9, 54)),
        FISHING_BAG("RAW_FISH",
                new Size(3, 9),
                new Size(7, 18),
                new Size(9, 27),
                new Size(10, 36),
                new Size(11, 45)),
        POTION_BAG("NETHER_WART",
                new Size(2, 9),
                new Size(5, 18),
                new Size(8, 27),
                new Size(10, 36),
                new Size(11, 45)),
        QUIVER("STRING",
                new Size(3, 27),
                new Size(6, 36),
                new Size(9, 45)),
        TALISMAN_BAG("REDSTONE",
                new Size(2, 3),
                new Size(6, 9),
                new Size(9, 15),
                new Size(10, 21),
                new Size(11, 27),
                new Size(12, 33),
                new Size(13, 39),
                new Size(14, 45)),
        ;

        private final String collection;
        private final ConcurrentList<Size> sizes;

        Bag(String collection, Size... sizes) {
            this.collection = collection;
            this.sizes = Concurrent.newList(sizes);
        }

        public String getCollection() {
            return this.collection;
        }

        public ConcurrentList<Size> getSizes() {
            return this.sizes;
        }

        public Size getGreatestSize(int tier) {
            for (Size size : this.getSizes()) {
                if (tier <= size.getTier())
                    return size;
            }

            return this.getGreatestSize();
        }

        public Size getGreatestSize() {
            return this.getSizes().get(this.getSizes().size() - 1);
        }

        public static Bag getBag(String name) {
            for (Bag bag : values()) {
                if (bag.name().equalsIgnoreCase(name))
                    return bag;
            }

            return null;
        }

        public static class Size {

            private final int tier;
            private final int slotCount;

            Size(int tier, int slotCount) {
                this.tier = tier;
                this.slotCount = slotCount;
            }

            public int getTier() {
                return this.tier;
            }

            public int getSlotCount() {
                return this.slotCount;
            }

        }

    }

    public enum Slayer {

        REVENANT_HORROR("zombie"),
        TARANTULA_BROODFATHER("spider"),
        SVEN_PACKMASTER("wolf");

        private final String name;

        Slayer(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }

        public static Slayer getType(String name) {
            for (Slayer slayer : values()) {
                if (slayer.getName().equalsIgnoreCase(name))
                    return slayer;
            }

            return null;
        }

    }

    public enum Item {

        // Redstone Collection
        REDSTONE_DUST("REDSTONE", "Items.REDSTONE"),
        REDSTONE_BLOCK("REDSTONE_BLOCK", "Blocks.REDSTONE_BLOCK", REDSTONE_DUST, CraftingTable.Recipe.ALL, new Recipe.Entry(REDSTONE_DUST, 1, 9)),
        ENCHANTED_REDSTONE_DUST("ENCHANTED_REDSTONE", "Items.REDSTONE", REDSTONE_DUST, CraftingTable.Recipe.STAR, new Recipe.Group(REDSTONE_DUST, 32, 5), new Recipe.Group(REDSTONE_BLOCK, 32, 5)),
        ENCHANTED_REDSTONE_BLOCK("ENCHANTED_REDSTONE_BLOCK", "Blocks.REDSTONE_BLOCK", ENCHANTED_REDSTONE_DUST, CraftingTable.Recipe.STAR, new Recipe.Entry(ENCHANTED_REDSTONE_DUST, 32, 5)),

        // Oak Wood Collection
        OAK_WOOD("LOG", "Blocks.LOG"),
        OAK_WOOD_PLANK("WOOD", "Blocks.PLANKS", OAK_WOOD, CraftingTable.Recipe.SINGLE, new Recipe.Entry(OAK_WOOD, 1)),
        CHEST("CHEST", "Blocks.CHEST", OAK_WOOD_PLANK, CraftingTable.Recipe.RING, new Recipe.Entry(OAK_WOOD_PLANK, 1, 8)),
        ENCHANTED_OAK_WOOD("ENCHANTED_OAK_LOG", "Blocks.LOG", OAK_WOOD, CraftingTable.Recipe.STAR, new Recipe.Entry(OAK_WOOD, 32, 5)),
        SMALL_STORAGE("SMALL_ENCHANTED_CHEST", "Blocks.CHEST", OAK_WOOD, CraftingTable.Recipe.RING, new Recipe.Entry(OAK_WOOD, 8, 8)),
        MEDIUM_STORAGE("MEDIUM_ENCHANTED_CHEST", "Blocks.CHEST", ENCHANTED_OAK_WOOD, CraftingTable.Recipe.ALL, new Recipe.Entry(ENCHANTED_OAK_WOOD, 1, 8), new Recipe.Entry(SMALL_STORAGE, 1)),
        LARGE_STORAGE("LARGE_ENCHANTED_CHEST", "Blocks.CHEST", ENCHANTED_OAK_WOOD, CraftingTable.Recipe.ALL, new Recipe.Entry(ENCHANTED_OAK_WOOD, 32, 8), new Recipe.Entry(MEDIUM_STORAGE, 1)),

        // Spruce Wood Collection
        SPRUCE_WOOD("LOG:1", "Blocks.LOG", 1),
        ENCHANTED_SPRUCE_WOOD("ENCHANTED_SPRUCE_LOG", "Blocks.LOG", 1, SPRUCE_WOOD, CraftingTable.Recipe.STAR, new Recipe.Entry(SPRUCE_WOOD, 32, 5)),

        // Birch Wood Collection
        BIRCH_WOOD("LOG:2", "Blocks.LOG", 2),
        ENCHANTED_BIRCH_WOOD("ENCHANTED_BIRCH_LOG", "Blocks.LOG", 2, BIRCH_WOOD, CraftingTable.Recipe.STAR, new Recipe.Entry(BIRCH_WOOD, 32, 5)),

        // Dark Oak Wood Collection
        DARK_OAK_WOOD("LOG_2:1", "Blocks.LOG2", 1),
        ENCHANTED_DARK_OAK_WOOD("ENCHANTED_DARK_OAK_LOG", "Blocks.LOG2", 1, DARK_OAK_WOOD, CraftingTable.Recipe.STAR, new Recipe.Entry(DARK_OAK_WOOD, 32, 5)),

        // Acacia Wood Collection
        ACACIA_WOOD("LOG_2", "Blocks.LOG2"),
        ENCHANTED_ACACIA_WOOD("ENCHANTED_ACACIA_LOG", "Blocks.LOG2", ACACIA_WOOD, CraftingTable.Recipe.STAR, new Recipe.Entry(ACACIA_WOOD, 32, 5)),

        // Jungle Wood Collection
        JUNGLE_WOOD("LOG:3", "Blocks.LOG", 3),
        ENCHANTED_JUNGLE_WOOD("ENCHANTED_JUNGLE_LOG", "Blocks.LOG", 3, JUNGLE_WOOD, CraftingTable.Recipe.STAR, new Recipe.Entry(JUNGLE_WOOD, 32, 5)),

        // Cobblestone Collection
        COBBLESTONE("COBBLESTONE", "Blocks.COBBLESTONE"),
        ENCHANTED_COBBLESTONE("ENCHANTED_COBBLESTONE", "Blocks.COBBLESTONE", COBBLESTONE, CraftingTable.Recipe.STAR, new Recipe.Entry(COBBLESTONE, 32, 5)),
        COMPACTOR("COMPACTOR", "Blocks.DROPPER", ENCHANTED_COBBLESTONE, CraftingTable.Recipe.RING, new Recipe.Entry(ENCHANTED_COBBLESTONE, 1, 7), new Recipe.Entry(ENCHANTED_REDSTONE_DUST, 1)),
        SUPER_COMPACTOR_3000("SUPER_COMPACTOR_3000", "Blocks.DROPPER", ENCHANTED_COBBLESTONE, CraftingTable.Recipe.RING, new Recipe.Entry(ENCHANTED_COBBLESTONE, 64, 7), new Recipe.Entry(ENCHANTED_REDSTONE_BLOCK, 1)),
        PERSONAL_COMPACTOR_4000("PERSONAL_COMPACTOR_4000", "Blocks.DROPPER", ENCHANTED_REDSTONE_BLOCK, CraftingTable.Recipe.RING, new Recipe.Entry(ENCHANTED_REDSTONE_BLOCK, 1, 7), new Recipe.Entry(SUPER_COMPACTOR_3000, 1)),
        PERSONAL_COMPACTOR_5000("PERSONAL_COMPACTOR_5000", "Blocks.DROPPER", ENCHANTED_REDSTONE_BLOCK, CraftingTable.Recipe.RING, new Recipe.Entry(ENCHANTED_REDSTONE_BLOCK, 2, 7), new Recipe.Entry(PERSONAL_COMPACTOR_4000, 1)),
        PERSONAL_COMPACTOR_6000("PERSONAL_COMPACTOR_6000", "Blocks.DROPPER", ENCHANTED_REDSTONE_BLOCK, CraftingTable.Recipe.RING, new Recipe.Entry(ENCHANTED_REDSTONE_BLOCK, 4, 7), new Recipe.Entry(PERSONAL_COMPACTOR_5000, 1)),

        // Coal Collection
        COAL("COAL", "Items.COAL"),
        COAL_BLOCK("COAL_BLOCK", "Blocks.COAL_BLOCK", COAL, CraftingTable.Recipe.ALL, new Recipe.Entry(COAL, 1, 9)),
        ENCHANTED_COAL("ENCHANTED_COAL", "Items.COAL", COAL, CraftingTable.Recipe.STAR, new Recipe.Group(COAL, 32, 5), new Recipe.Group(COAL_BLOCK, 32, 5)),
        ENCHANTED_COAL_BLOCK("ENCHANTED_COAL_BLOCK", "Blocks.COAL_BLOCK", ENCHANTED_COAL, CraftingTable.Recipe.STAR, new Recipe.Entry(ENCHANTED_COAL, 32, 5)),

        // Iron Collection
        IRON_INGOT("IRON_INGOT", "Items.IRON_INGOT"),
        IRON_BLOCK("IRON_BLOCK", "Blocks.IRON_BLOCK", IRON_INGOT, CraftingTable.Recipe.ALL, new Recipe.Entry(IRON_INGOT, 1, 9)),
        ENCHANTED_IRON("ENCHANTED_IRON", "Items.IRON_INGOT", IRON_INGOT, CraftingTable.Recipe.STAR, new Recipe.Group(IRON_INGOT, 32, 5), new Recipe.Group(IRON_BLOCK, 32, 5)),
        ENCHANTED_IRON_BLOCK("ENCHANTED_IRON_BLOCK", "Blocks.IRON_BLOCK", ENCHANTED_IRON, CraftingTable.Recipe.STAR, new Recipe.Entry(ENCHANTED_IRON, 32, 5)),
        HOPPER("HOPPER", "Blocks.HOPPER", IRON_INGOT, CraftingTable.Recipe.HOPPER, new Recipe.Entry(IRON_INGOT, 1, 5), new Recipe.Entry(CHEST, 1)),
        BUDGET_HOPPER("BUDGET_HOPPER", "Blocks.HOPPER", ENCHANTED_IRON, CraftingTable.Recipe.HOPPER, new Recipe.Entry(ENCHANTED_IRON, 1, 5), new Recipe.Entry(CHEST, 1)),
        ENCHANTED_HOPPER("ENCHANTED_HOPPER", "Blocks.HOPPER", ENCHANTED_IRON_BLOCK, CraftingTable.Recipe.HOPPER, new Recipe.Entry(ENCHANTED_IRON_BLOCK, 1, 5), new Recipe.Entry(CHEST, 1)),

        // Gold Collection
        GOLD_INGOT("GOLD_INGOT", "Items.GOLD_INGOT"),
        GOLD_NUGGET("GOLD_NUGGET", "Items.GOLD_NUGGET", GOLD_INGOT, CraftingTable.Recipe.SINGLE, new Recipe.Entry(GOLD_INGOT, 1)),
        GOLD_BLOCK("GOLD_BLOCK", "Blocks.GOLD_BLOCK", GOLD_INGOT, CraftingTable.Recipe.ALL, new Recipe.Entry(GOLD_INGOT, 1, 9)),
        ENCHANTED_GOLD("ENCHANTED_GOLD", "Items.GOLD_INGOT", GOLD_INGOT, CraftingTable.Recipe.STAR, new Recipe.Group(GOLD_INGOT, 32, 5), new Recipe.Group(GOLD_BLOCK, 32, 5)),
        ENCHANTED_GOLD_BLOCK("ENCHANTED_GOLD_BLOCK", "Blocks.GOLD_BLOCK", ENCHANTED_GOLD, CraftingTable.Recipe.STAR, new Recipe.Entry(ENCHANTED_GOLD, 32, 5)),

        // Diamond Collection
        DIAMOND("DIAMOND", "Items.DIAMOND"),
        DIAMOND_BLOCK("DIAMOND_BLOCK", "Blocks.DIAMOND_BLOCK", DIAMOND, CraftingTable.Recipe.ALL, new Recipe.Entry(DIAMOND, 1, 9)),
        ENCHANTED_DIAMOND("ENCHANTED_DIAMOND", "Items.DIAMOND", DIAMOND, CraftingTable.Recipe.STAR, new Recipe.Group(DIAMOND, 32, 5), new Recipe.Group(DIAMOND_BLOCK, 32, 5)),
        ENCHANTED_DIAMOND_BLOCK("ENCHANTED_DIAMOND_BLOCK", "Blocks.DIAMOND_BLOCK", ENCHANTED_DIAMOND, CraftingTable.Recipe.STAR, new Recipe.Entry(ENCHANTED_DIAMOND, 32, 5)),

        // Lapis Collection
        LAPIS_LAZULI("INK_SACK:4", "Items.DYE", 4),
        LAPIS_BLOCK("LAPIS_BLOCK", "Blocks.LAPIS_BLOCK", LAPIS_LAZULI, CraftingTable.Recipe.ALL, new Recipe.Entry(LAPIS_LAZULI, 1, 9)),
        ENCHANTED_LAPIS_LAZULI("ENCHANTED_LAPIS_LAZULI", "Items.DYE", 4, LAPIS_LAZULI, CraftingTable.Recipe.STAR, new Recipe.Group(LAPIS_LAZULI, 32, 5), new Recipe.Group(LAPIS_BLOCK, 32, 5)),
        ENCHANTED_LAPIS_BLOCK("ENCHANTED_LAPIS_LAZULI_BLOCK", "Blocks.LAPIS_BLOCK", ENCHANTED_LAPIS_LAZULI, CraftingTable.Recipe.STAR, new Recipe.Entry(ENCHANTED_LAPIS_LAZULI, 32, 5)),

        // Emerald Collection
        EMERALD("COAL", "Items.EMERALD"),
        EMERALD_BLOCK("EMERALD_BLOCK", "Blocks.EMERALD_BLOCK", EMERALD, CraftingTable.Recipe.ALL, new Recipe.Entry(EMERALD, 1, 9)),
        ENCHANTED_EMERALD("ENCHANTED_EMERALD", "Items.EMERALD", EMERALD, CraftingTable.Recipe.STAR, new Recipe.Group(EMERALD, 32, 5), new Recipe.Group(EMERALD_BLOCK, 32, 5)),
        ENCHANTED_EMERALD_BLOCK("ENCHANTED_EMERALD_BLOCK", "Blocks.EMERALD_BLOCK", ENCHANTED_EMERALD, CraftingTable.Recipe.STAR, new Recipe.Entry(ENCHANTED_EMERALD, 32, 5)),

        // Quartz Collection
        NETHER_QUARTZ("QUARTZ", "Items.QUARTZ"),
        QUARTZ_BLOCK("QUARTZ_BLOCK", "Blocks.QUARTZ_BLOCK", NETHER_QUARTZ, CraftingTable.Recipe.ALL, new Recipe.Entry(NETHER_QUARTZ, 1, 9)),
        ENCHANTED_QUARTZ("ENCHANTED_QUARTZ", "Items.QUARTZ", NETHER_QUARTZ, CraftingTable.Recipe.STAR, new Recipe.Group(NETHER_QUARTZ, 32, 5), new Recipe.Group(QUARTZ_BLOCK, 32, 5)),
        ENCHANTED_QUARTZ_BLOCK("ENCHANTED_QUARTZ_BLOCK", "Blocks.QUARTZ_BLOCK", ENCHANTED_QUARTZ, CraftingTable.Recipe.STAR, new Recipe.Entry(ENCHANTED_QUARTZ, 32, 5)),
        MINION_EXPANDER("MINION_EXPANDER", "Blocks.COMMAND_BLOCK", ENCHANTED_QUARTZ, CraftingTable.Recipe.ALL, new Recipe.Entry(ENCHANTED_QUARTZ, 2, 8), new Recipe.Entry(ENCHANTED_REDSTONE_DUST, 2)),

        // Obsidian Collection
        OBSIDIAN("OBSIDIAN", "Blocks.OBSIDIAN"),
        ENCHANTED_OBSIDIAN("ENCHANTED_OBSIDIAN", "Blocks.OBSIDIAN", OBSIDIAN, CraftingTable.Recipe.STAR, new Recipe.Entry(OBSIDIAN, 32, 5)),

        // Glowstone Collection
        GLOWSTONE_DUST("QUARTZ", "Items.GLOWSTONE_DUST"),
        GLOWSTONE_BLOCK("GLOWSTONE_BLOCK", "Blocks.GLOWSTONE", GLOWSTONE_DUST, CraftingTable.Recipe.BOX, new Recipe.Entry(GLOWSTONE_DUST, 1, 4)),
        ENCHANTED_GLOWSTONE_DUST("ENCHANTED_GLOWSTONE_DUST", "Items.GLOWSTONE_DUST", GLOWSTONE_DUST, CraftingTable.Recipe.STAR, new Recipe.Group(GLOWSTONE_DUST, 32, 5), new Recipe.Group(GLOWSTONE_BLOCK, 32, 5)),
        ENCHANTED_GLOWSTONE_BLOCK("ENCHANTED_GLOWSTONE_BLOCK", "Blocks.GLOWSTONE", ENCHANTED_GLOWSTONE_DUST, CraftingTable.Recipe.STAR, new Recipe.Entry(ENCHANTED_GLOWSTONE_DUST, 32, 5)),

        // Gravel Collection
        GRAVEL("GRAVEL", "Blocks.GRAVEL"),
        FLINT("FLINT", "Items.FLINT"),
        ENCHANTED_FLINT("ENCHANTED_FLINT", "Items.FLINT", FLINT, CraftingTable.Recipe.STAR, new Recipe.Entry(FLINT, 32, 5)),

        // Ice Collection
        ICE("ICE", "Blocks.ICE"),
        PACKED_ICE("PACKED_ICE", "Blocks.PACKED_ICE"),
        ENCHANTED_ICE("ENCHANTED_ICE", "Blocks.ICE", ICE, CraftingTable.Recipe.STAR, new Recipe.Group(PACKED_ICE, 32, 5), new Recipe.Group(ICE, 32, 5)),
        ENCHANTED_PACKED_ICE("ENCHANTED_PACKED_ICE", "Blocks.PACKED_ICE", ENCHANTED_ICE, CraftingTable.Recipe.STAR, new Recipe.Entry(ENCHANTED_ICE, 32, 5)),

        // Netherrack Collection
        NETHERRACK("NETHERRACK", "Blocks.NETHERRACK"),

        // Sand Collection
        SAND("SAND", "Blocks.SAND"),
        ENCHANTED_SAND("ENCHANTED_SAND", "Blocks.SAND", SAND, CraftingTable.Recipe.STAR, new Recipe.Entry(SAND, 32, 5)),

        // End Stone Collection
        END_STONE("ENDER_STONE", "Blocks.END_STONE"),
        ENCHANTED_END_STONE("ENCHANTED_ENDSTONE", "Blocks.END_STONE", END_STONE, CraftingTable.Recipe.STAR, new Recipe.Entry(END_STONE, 32, 5)),

        // Snow Collection
        SNOWBALL("SNOW_BALL", "Items.SNOWBALL"),
        SNOW_BLOCK("SNOW_BLOCK", "Blocks.SNOW", SNOWBALL, CraftingTable.Recipe.BOX, new Recipe.Entry(SNOWBALL, 1, 4)),
        ENCHANTED_SNOW_BLOCK("ENCHANTED_SNOW_BLOCK", "Blocks.SNOW", SNOW_BLOCK, CraftingTable.Recipe.STAR, new Recipe.Entry(SNOW_BLOCK, 32, 5)),

        // Wheat Collection
        WHEAT("WHEAT", "Items.WHEAT"),
        SEEDS("SEEDS", "Items.WHEAT_SEEDS"),
        HAY_BALE("HAY_BLOCK", "Blocks.HAY_BLOCK", WHEAT, CraftingTable.Recipe.ALL, new Recipe.Entry(WHEAT, 1, 9)),
        ENCHANTED_HAY_BALE("ENCHANTED_HAY_BLOCK", "Blocks.HAY_BLOCK", HAY_BALE, CraftingTable.Recipe.ALL, new Recipe.Entry(HAY_BALE, 16, 9)),
        BREAD("BREAD", "Items.BREAD"),
        ENCHANTED_BREAD("ENCHANTED_BREAD", "Items.BREAD", BREAD, CraftingTable.Recipe.DOUBLE_ROW, new Recipe.Entry(WHEAT, 10, 6)),

        // Carrot Collection
        CARROT("CARROT_ITEM", "Items.CARROT"),
        GOLDEN_CARROT("GOLDEN_CARROT", "Items.GOLDEN_CARROT", CARROT, CraftingTable.Recipe.ALL, new Recipe.Entry(GOLD_NUGGET, 1, 8), new Recipe.Entry(CARROT, 1)),
        ENCHANTED_CARROT("ENCHANTED_CARROT", "Items.CARROT", CARROT, CraftingTable.Recipe.STAR, new Recipe.Entry(CARROT, 32, 5)),
        ENCHANTED_GOLDEN_CARROT("ENCHANTED_GOLDEN_CARROT", "Items.GOLDEN_CARROT", ENCHANTED_CARROT, CraftingTable.Recipe.STAR, new Recipe.Entry(ENCHANTED_CARROT, 32, 4), new Recipe.Entry(GOLDEN_CARROT, 32)),

        // Potato Collection
        POTATO("POTATO_ITEM", "Items.POTATO"),
        ENCHANTED_POTATO("ENCHANTED_POTATO", "Items.POTATO", POTATO, CraftingTable.Recipe.STAR, new Recipe.Entry(POTATO, 32, 5)),
        ENCHANTED_BAKED_POTATO("ENCHANTED_BAKED_POTATO", "Items.BAKED_POTATO", ENCHANTED_POTATO, CraftingTable.Recipe.STAR, new Recipe.Entry(ENCHANTED_POTATO, 32, 5)),

        // Pumpkin Collection
        PUMPKIN("PUMPKIN", "Blocks.PUMPKIN"),
        ENCHANTED_PUMPKIN("ENCHANTED_PUMPKIN", "Blocks.PUMPKIN", PUMPKIN, CraftingTable.Recipe.STAR, new Recipe.Entry(PUMPKIN, 32, 5)),

        // Melon Collection
        MELON("MELON", "Items.MELON"),
        GLISTERING_MELON("SPECKLED_MELON", "Items.SPECKLED_MELON", MELON, CraftingTable.Recipe.ALL, new Recipe.Entry(GOLD_NUGGET, 1, 8), new Recipe.Entry(MELON, 1)),
        MELON_BLOCK("MELON_BLOCK", "Blocks.MELON_BLOCK", MELON, CraftingTable.Recipe.ALL, new Recipe.Entry(MELON, 1, 9)),
        ENCHANTED_MELON("ENCHANTED_MELON", "Items.MELON", MELON, CraftingTable.Recipe.STAR, new Recipe.Group(MELON, 32, 5), new Recipe.Group(MELON_BLOCK, 32, 5)),
        ENCHANTED_MELON_BLOCK("ENCHANTED_MELON_BLOCK", "Blocks.MELON_BLOCK", ENCHANTED_MELON, CraftingTable.Recipe.STAR, new Recipe.Entry(ENCHANTED_MELON, 32, 5)),

        // Mushroom Collection
        BROWN_MUSHROOM("BROWN_MUSHROOM", "Blocks.BROWN_MUSHROOM"),
        RED_MUSHROOM("RED_MUSHROOM", "Blocks.RED_MUSHROOM"),
        BROWN_MUSHROOM_BLOCK("HUGE_MUSHROOM_1", "Blocks.BROWN_MUSHROOM_BLOCK", BROWN_MUSHROOM, CraftingTable.Recipe.ALL, new Recipe.Entry(BROWN_MUSHROOM, 1, 9)),
        RED_MUSHROOM_BLOCK("HUGE_MUSHROOM_2", "Blocks.RED_MUSHROOM_BLOCK", RED_MUSHROOM, CraftingTable.Recipe.ALL, new Recipe.Entry(RED_MUSHROOM, 1, 9)),
        ENCHANTED_BROWN_MUSHROOM("ENCHANTED_BROWN_MUSHROOM", "Blocks.BROWN_MUSHROOM", BROWN_MUSHROOM, CraftingTable.Recipe.STAR, new Recipe.Entry(BROWN_MUSHROOM, 32, 5)),
        ENCHANTED_RED_MUSHROOM("ENCHANTED_RED_MUSHROOM", "Blocks.RED_MUSHROOM", RED_MUSHROOM, CraftingTable.Recipe.STAR, new Recipe.Entry(RED_MUSHROOM, 32, 5)),
        ENCHANTED_BROWN_MUSHROOM_BLOCK("ENCHANTED_HUGE_MUSHROOM_1", "Blocks.BROWN_MUSHROOM_BLOCK", BROWN_MUSHROOM_BLOCK, CraftingTable.Recipe.ALL, new Recipe.Entry(BROWN_MUSHROOM_BLOCK, 64, 9)),
        ENCHANTED_RED_MUSHROOM_BLOCK("ENCHANTED_HUGE_MUSHROOM_2", "Blocks.RED_MUSHROOM_BLOCK", RED_MUSHROOM_BLOCK, CraftingTable.Recipe.ALL, new Recipe.Entry(RED_MUSHROOM_BLOCK, 64, 9)),

        // Cocoa Beans Collection
        COCOA_BEANS("INK_SACK:3", "Items.DYE", 3),
        ENCHANTED_COCOA_BEANS("ENCHANTED_COCOA", "Items.DYE", 3, COCOA_BEANS, CraftingTable.Recipe.STAR, new Recipe.Entry(COCOA_BEANS, 32, 5)),
        ENCHANTED_COOKIE("ENCHANTED_COOKIE", "Items.COOKIE", ENCHANTED_COCOA_BEANS, CraftingTable.Recipe.STAR, new Recipe.Entry(ENCHANTED_COCOA_BEANS, 32, 4), new Recipe.Entry(WHEAT, 32)),

        // Cactus Collection
        CACTUS("CACTUS", "Blocks.CACTUS"),
        CACTUS_GREEN("INK_SACK:2", "Items.DYE", 2),
        ENCHANTED_CACTUS_GREEN("ENCHANTED_CACTUS_GREEN", "Items.DYE", 2, CACTUS_GREEN, CraftingTable.Recipe.STAR, new Recipe.Entry(CACTUS_GREEN, 32, 5)),
        ENCHANTED_CACTUS("ENCHANTED_CACTUS", "Blocks.CACTUS", ENCHANTED_CACTUS_GREEN, CraftingTable.Recipe.STAR, new Recipe.Entry(ENCHANTED_CACTUS_GREEN, 32, 5)),

        // Sugar Cane Collection
        SUGAR_CANE("SUGAR_CANE", "Items.REEDS"),
        SUGAR("SUGAR", "Items.SUGAR", SUGAR_CANE, CraftingTable.Recipe.SINGLE, new Recipe.Entry(SUGAR_CANE, 1)),
        PAPER("PAPER", "Items.PAPER", SUGAR_CANE, CraftingTable.Recipe.TOP_ROW, new Recipe.Entry(SUGAR_CANE, 1, 3)),
        ENCHANTED_SUGAR("ENCHANTED_SUGAR", "Items.SUGAR", SUGAR_CANE, CraftingTable.Recipe.STAR, new Recipe.Group(SUGAR_CANE, 32, 5), new Recipe.Group(SUGAR, 32, 5)),
        ENCHANTED_PAPER("ENCHANTED_PAPER", "Items.PAPER", SUGAR_CANE, CraftingTable.Recipe.DIAGONAL, new Recipe.Entry(SUGAR_CANE, 64, 3)),
        ENCHANTED_SUGAR_CANE("ENCHANTED_SUGAR_CANE", "Items.REEDS", ENCHANTED_SUGAR, CraftingTable.Recipe.STAR, new Recipe.Entry(ENCHANTED_SUGAR, 32, 5)),

        // Rabbit Collection
        RABBIT("RABBIT", "Items.RABBIT"),
        RABBIT_FOOT("RABBIT_FOOT", "Items.RABBIT_FOOT"),
        ENCHANTED_RABBIT_FOOT("ENCHANTED_RABBIT_FOOT", "Items.RABBIT_FOOT", RABBIT_FOOT, CraftingTable.Recipe.STAR, new Recipe.Entry(RABBIT_FOOT, 32, 5)),
        RABBIT_HIDE("RABBIT_HIDE", "Items.RABBIT_HIDE"),
        ENCHANTED_RABBIT_HIDE("ENCHANTED_RABBIT_HIDE", "Items.RABBIT_HIDE", RABBIT_HIDE, CraftingTable.Recipe.ALL, new Recipe.Entry(RABBIT_HIDE, 64, 9)),

        // Feather Collection
        FEATHER("FEATHER", "Items.FEATHER"),
        ENCHANTED_FEATHER("ENCHANTED_FEATHER", "Items.FEATHER", FEATHER, CraftingTable.Recipe.STAR, new Recipe.Entry(FEATHER, 32, 5)),

        // Leather Collection
        RAW_BEEF("RAW_BEEF", "Items.BEEF"),
        ENCHANTED_RAW_BEEF("ENCHANTED_RAW_BEEF", "Items.BEEF", RAW_BEEF, CraftingTable.Recipe.STAR, new Recipe.Entry(RAW_BEEF, 32, 5)),
        LEATHER("LEATHER", "Items.LEATHER", RABBIT_HIDE, CraftingTable.Recipe.BOX, new Recipe.Entry(RABBIT_HIDE, 1, 4)),
        ENCHANTED_LEATHER("ENCHANTED_LEATHER", "Items.LEATHER", LEATHER, CraftingTable.Recipe.ALL, new Recipe.Entry(LEATHER, 64, 9)),

        // Porkchop Collection
        RAW_PORKCHOP("PORK", "Items.PORKCHOP"),
        ENCHANTED_RAW_PORKCHOP("ENCHANTED_PORK", "Items.PORKCHOP", RAW_PORKCHOP, CraftingTable.Recipe.STAR, new Recipe.Entry(RAW_PORKCHOP, 32, 5)),
        ENCHANTED_GRILLED_PORK("ENCHANTED_GRILLED_PORK", "Items.COOKED_PORKCHOP", ENCHANTED_RAW_PORKCHOP, CraftingTable.Recipe.STAR, new Recipe.Entry(ENCHANTED_RAW_PORKCHOP, 32, 5)),

        // Chicken Collection
        RAW_CHICKEN("RAW_CHICKEN", "Items.CHICKEN"),
        ENCHANTED_RAW_CHICKEN("ENCHANTED_RAW_CHICKEN", "Items.CHICKEN", RAW_CHICKEN, CraftingTable.Recipe.STAR, new Recipe.Entry(RAW_CHICKEN, 32, 5)),
        EGG("EGG", "Items.EGG"),
        ENCHANTED_EGG("ENCHANTED_EGG", "Items.EGG", EGG, CraftingTable.Recipe.ALL, new Recipe.Entry(EGG, 16, 9)),
        ENCHANTED_SUPER_EGG("SUPER_EGG", "Items.SPAWN_EGG", ENCHANTED_EGG, CraftingTable.Recipe.ALL, new Recipe.Entry(ENCHANTED_EGG, 16, 9)),
        MILK_BUCKET("MILK_BUCKET", "Items.MILK_BUCKET"),
        ENCHANTED_CAKE("ENCHANTED_CAKE", "Items.CAKE", ENCHANTED_EGG, CraftingTable.Recipe.ALL, new Recipe.Entry(MILK_BUCKET, 1, 3), new Recipe.Entry(ENCHANTED_SUGAR, 1, 2), new Recipe.Entry(WHEAT, 1, 3), new Recipe.Entry(ENCHANTED_EGG, 1)),

        // Mutton Collection
        MUTTON("MUTTON", "Items.MUTTON"),
        ENCHANTED_MUTTON("ENCHANTED_MUTTON", "Items.MUTTON", MUTTON, CraftingTable.Recipe.STAR, new Recipe.Entry(MUTTON, 32, 5)),
        ENCHANTED_COOKED_MUTTON("ENCHANTED_COOKED_MUTTON", "Items.COOKED_MUTTON", ENCHANTED_MUTTON, CraftingTable.Recipe.STAR, new Recipe.Entry(ENCHANTED_MUTTON, 32, 5)),
        WOOL("WOOL", "Blocks.WOOL"),
        ENCHANTED_WOOL("ENCHANTED_WOOL", "Blocks.WOOL", WOOL, CraftingTable.Recipe.STAR, new Recipe.Entry(WOOL, 32, 5)),

        // Nether Wart Collection
        NETHER_WART("NETHER_STALK", "Items.NETHER_WART"),
        ENCHANTED_NETHER_WART("ENCHANTED_NETHER_STALK", "Items.NETHER_WART", NETHER_WART, CraftingTable.Recipe.STAR, new Recipe.Entry(NETHER_WART, 32, 5)),

        // Rotten Flesh Collection
        ROTTEN_FLESH("ROTTEN_FLESH", "Items.ROTTEN_FLESH"),
        ENCHANTED_ROTTEN_FLESH("ENCHANTED_ROTTEN_FLESH", "Items.ROTTEN_FLESH", ROTTEN_FLESH, CraftingTable.Recipe.STAR, new Recipe.Entry(ROTTEN_FLESH, 32, 5)),
        ZOMBIE_HEART("ZOMBIE_HEART", "Items.SKULL", ENCHANTED_ROTTEN_FLESH, CraftingTable.Recipe.RING, new Recipe.Entry(ENCHANTED_ROTTEN_FLESH, 32, 8)),

        // Bone Collection
        BONE("BONE", "Items.BONE"),
        ENCHANTED_BONE("ENCHANTED_BONE", "Items.BONE", BONE, CraftingTable.Recipe.STAR, new Recipe.Entry(BONE, 32, 5)),

        // String Collection
        STRING("STRING", "Items.STRING"),
        ENCHANTED_STRING("ENCHANTED_STRING", "Items.STRING", STRING, CraftingTable.Recipe.SINGLE_ROW, new Recipe.Entry(STRING, 64, 3)),

        // Spider Eye Collection
        SPIDER_EYE("SPIDER_EYE", "Items.SPIDER_EYE"),
        ENCHANTED_SPIDER_EYE("ENCHANTED_SPIDER_EYE", "Items.SPIDER_EYE", SPIDER_EYE, CraftingTable.Recipe.STAR, new Recipe.Entry(SPIDER_EYE, 32, 5)),
        ENCHANTED_FERMENTED_SPIDER_EYE("ENCHANTED_FERMENTED_SPIDER_EYE", "Items.FERMENTED_SPIDER_EYE", ENCHANTED_SPIDER_EYE, CraftingTable.Recipe.CENTER3, new Recipe.Entry(BROWN_MUSHROOM, 64), new Recipe.Entry(SUGAR, 64), new Recipe.Entry(ENCHANTED_SPIDER_EYE, 64)),

        // Gunpowder Collection
        GUNPOWDER("SULPHUR", "Items.GUNPOWDER"),
        ENCHANTED_GUNPOWDER("ENCHANTED_GUNPOWDER", "Items.GUNPOWDER", GUNPOWDER, CraftingTable.Recipe.STAR, new Recipe.Entry(GUNPOWDER, 32, 5)),
        ENCHANTED_FIREWORK_ROCKET("ENCHANTED_FIREWORK_ROCKET", "Items.FIREWORKS", ENCHANTED_GUNPOWDER, CraftingTable.Recipe.STAR, new Recipe.Entry(ENCHANTED_GUNPOWDER, 16, 4), new Recipe.Entry(PAPER, 16)),

        // Blaze Rod Collection
        BLAZE_ROD("BLAZE_ROD", "Items.BLAZE_ROD"),
        BLAZE_POWDER("BLAZE_POWDER", "Items.BLAZE_POWDER", BLAZE_ROD, CraftingTable.Recipe.SINGLE, new Recipe.Entry(BLAZE_ROD, 1)),
        ENCHANTED_BLAZE_POWDER("ENCHANTED_BLAZE_POWDER", "Items.BLAZE_POWDER", BLAZE_ROD, CraftingTable.Recipe.STAR, new Recipe.Entry(BLAZE_ROD, 32, 5)),
        ENCHANTED_BLAZE_ROD("ENCHANTED_BLAZE_ROD", "Items.BLAZE_POWDER", ENCHANTED_BLAZE_POWDER, CraftingTable.Recipe.STAR, new Recipe.Entry(ENCHANTED_BLAZE_POWDER, 32, 5)),

        // Ender Pearl Collection
        ENDER_PEARL("ENDER_PEARL", "Items.ENDER_PEARL"),
        ENCHANTED_ENDER_PEARL("ENCHANTED_ENDER_PEARL", "Items.ENDER_PEARL", ENDER_PEARL, CraftingTable.Recipe.STAR, new Recipe.Entry(ENDER_PEARL, 4, 5)),
        ENCHANTED_EYE_OF_ENDER("ENCHANTED_EYE_OF_ENDER", "Items.ENDER_EYE", ENCHANTED_ENDER_PEARL, CraftingTable.Recipe.STAR, new Recipe.Entry(BLAZE_POWDER, 16, 4), new Recipe.Entry(ENCHANTED_ENDER_PEARL, 16)),

        // Ghast Collection
        GHAST_TEAR("GHAST_TEAR", "Items.GHAST_TEAR"),
        ENCHANTED_GHAST_TEAR("ENCHANTED_GHAST_TEAR", "Items.GHAST_TEAR", GHAST_TEAR, CraftingTable.Recipe.STAR, new Recipe.Entry(GHAST_TEAR, 1, 5)),
        SILVER_FANG("SILVER_FANG", "Items.GHAST_TEAR", ENCHANTED_GHAST_TEAR, CraftingTable.Recipe.STAR, new Recipe.Entry(ENCHANTED_GHAST_TEAR, 5, 5)),

        // Slimeball Collection
        SLIME_BALL("SLIME_BALL", "Items.SLIME_BALL"),
        SLIME_BLOCK("SLIME_BLOCK", "Blocks.SLIME_BLOCK", SLIME_BALL, CraftingTable.Recipe.ALL, new Recipe.Entry(SLIME_BALL, 1, 9)),
        ENCHANTED_SLIME_BALL("ENCHANTED_SLIME_BALL", "Items.SLIME_BALL", SLIME_BALL, CraftingTable.Recipe.STAR, new Recipe.Entry(SLIME_BALL, 32, 5)),
        ENCHANTED_SLIME_BALL_ALT("ENCHANTED_SLIME_BALL", "Items.SLIME_BALL", SLIME_BLOCK, CraftingTable.Recipe.STAR, new Recipe.Entry(SLIME_BLOCK, 32, 5)),
        ENCHANTED_SLIME_BLOCK("ENCHANTED_SLIME_BALL", "Items.SLIME_BALL", ENCHANTED_SLIME_BALL, CraftingTable.Recipe.STAR, new Recipe.Entry(ENCHANTED_SLIME_BALL, 32, 5)),

        // Magma Cream Collection
        MAGMA_CREAM("MAGMA_CREAM", "Items.MAGMA_CREAM", SLIME_BALL, CraftingTable.Recipe.MIDDLE2, new Recipe.Entry(BLAZE_POWDER, 1), new Recipe.Entry(SLIME_BALL, 1)),
        ENCHANTED_MAGMA_CREAM("ENCHANTED_MAGMA_CREAM", "Items.MAGMA_CREAM", MAGMA_CREAM, CraftingTable.Recipe.STAR, new Recipe.Entry(MAGMA_CREAM, 32, 5)),

        // Raw Fish Collection
        RAW_FISH("RAW_FISH", "Items.FISH"),
        ENCHANTED_RAW_FISH("ENCHANTED_RAW_FISH", "Items.FISH", RAW_FISH, CraftingTable.Recipe.STAR, new Recipe.Entry(RAW_FISH, 32, 5)),
        ENCHANTED_COOKED_FISH("ENCHANTED_COOKED_FISH", "Items.COOKED_FISH", ENCHANTED_RAW_FISH, CraftingTable.Recipe.STAR, new Recipe.Entry(ENCHANTED_RAW_FISH, 32, 5)),

        // Raw Salmon Collection
        RAW_SALMON("RAW_FISH:1", "Items.FISH", 1),
        ENCHANTED_RAW_SALMON("ENCHANTED_RAW_SALMON", "Items.FISH", 1, RAW_SALMON, CraftingTable.Recipe.STAR, new Recipe.Entry(RAW_SALMON, 32, 5)),
        ENCHANTED_COOKED_SALMON("ENCHANTED_COOKED_SALMON", "Items.FISH", 1, ENCHANTED_RAW_SALMON, CraftingTable.Recipe.STAR, new Recipe.Entry(ENCHANTED_RAW_SALMON, 32, 5)),

        // Clownfish Collection
        CLOWNFISH("RAW_FISH:2", "Items.FISH", 2),
        ENCHANTED_CLOWNFISH("ENCHANTED_CLOWNFISH", "Items.FISH", 2, CLOWNFISH, CraftingTable.Recipe.STAR, new Recipe.Entry(CLOWNFISH, 32, 5)),

        // Pufferfish Collection
        PUFFERFISH("RAW_FISH:3", "Items.FISH", 3),
        ENCHANTED_PUFFERFISH("ENCHANTED_PUFFERFISH", "Items.FISH", 3, PUFFERFISH, CraftingTable.Recipe.STAR, new Recipe.Entry(PUFFERFISH, 32, 5)),

        // Prismarine Shard Collection
        PRISMARINE_SHARD("PRISMARINE_SHARD", "Items.PRISMARINE_SHARD"),
        ENCHANTED_PRISMARINE_SHARD("ENCHANTED_PRISMARINE_SHARD", "Items.PRISMARINE_SHARD", PRISMARINE_SHARD, CraftingTable.Recipe.STAR, new Recipe.Entry(PRISMARINE_SHARD, 32, 5)),

        // Prismarine Crystals Collection
        PRISMARINE_CRYSTALS("PRISMARINE_CRYSTALS", "Items.PRISMARINE_CRYSTALS"),
        ENCHANTED_PRISMARINE_CRYSTALS("ENCHANTED_PRISMARINE_CRYSTALS", "Items.PRISMARINE_CRYSTALS", PRISMARINE_CRYSTALS, CraftingTable.Recipe.STAR, new Recipe.Entry(PRISMARINE_CRYSTALS, 32, 5)),

        // Clay Collection
        CLAY("CLAY_BALL", "Items.CLAY_BALL"),
        CLAY_BLOCK("CLAY", "Blocks.CLAY", CLAY, CraftingTable.Recipe.BOX, new Recipe.Entry(CLAY, 1, 4)),
        ENCHANTED_CLAY("ENCHANTED_CLAY_BALL", "Items.CLAY_BALL", CLAY, CraftingTable.Recipe.STAR, new Recipe.Entry(CLAY, 32, 5)),

        // Lily Pad Collection
        LILY_PAD("WATER_LILY", "Blocks.WATERLILY"),
        ENCHANTED_LILY_PAD("ENCHANTED_LILY_PAD", "Blocks.WATERLILY", LILY_PAD, CraftingTable.Recipe.STAR, new Recipe.Entry(LILY_PAD, 32, 5)),

        // Ink Sack Collection
        INK_SACK("INK_SACK", "Items.DYE"),
        ENCHANTED_INK_SACK("ENCHANTED_INK_SACK", "Items.DYE", INK_SACK, CraftingTable.Recipe.STAR, new Recipe.Entry(INK_SACK, 16, 5)),

        // Sponge Collection
        SPONGE("SPONGE", "Blocks.SPONGE"),
        ENCHANTED_SPONGE("ENCHANTED_SPONGE", "Blocks.SPONGE", SPONGE, CraftingTable.Recipe.STAR, new Recipe.Entry(SPONGE, 8, 5)),
        ENCHANTED_WET_SPONGE("ENCHANTED_WET_SPONGE", "Blocks.SPONGE", ENCHANTED_SPONGE, CraftingTable.Recipe.STAR, new Recipe.Entry(ENCHANTED_SPONGE, 8, 5)),

        // Slayer
        TARANTULA_WEB("TARANTULA_WEB", "Blocks.WEB"),
        TARANTULA_SILK("TARANTULA_SILK", "Items.STRING", TARANTULA_WEB, CraftingTable.Recipe.STAR, new Recipe.Entry(TARANTULA_WEB, 32, 4), new Recipe.Entry(ENCHANTED_FLINT, 32)),
        REVENANT_FLESH("REVENANT_FLESH", "Items.ROTTEN_FLESH"),
        REVENANT_VISCERA("REVENANT_VISCERA", "Items.ROTTEN_FLESH", REVENANT_FLESH, CraftingTable.Recipe.STAR, new Recipe.Entry(REVENANT_FLESH, 32, 4), new Recipe.Entry(ENCHANTED_STRING, 32)),
        WOLF_TOOTH("WOLF_TOOTH", "Items.GHAST_TEAR"),
        GOLDEN_TOOTH("GOLDEN_TOOTH", "Items.GHAST_TEAR", WOLF_TOOTH, CraftingTable.Recipe.STAR, new Recipe.Entry(GHAST_TEAR, 32, 4), new Recipe.Entry(ENCHANTED_GOLD, 32)),

        // Miscellaneous
        ARROW("ARROW", "Items.ARROW"),
        BOOK("BOOK", "Items.BOOK", PAPER, CraftingTable.Recipe.BOX, new Recipe.Entry(PAPER, 1, 3), new Recipe.Entry(LEATHER, 1)),
        ENCHANTED_BOOK("ENCHANTED_BOOK", "Items.ENCHANTED_BOOK");

        static {
            for (Item skyblockItem : values()) {
                if (skyblockItem.getBaseItem() == null) {
                    Set<Item> collection = new HashSet<>();
                    recursiveCollection(skyblockItem, collection);

                    for (Item collItem : collection)
                        collItem.collection.addAll(collection);
                }
            }
        }

        private static void recursiveCollection(Item skyblockItem, Set<Item> collection) {
            collection.add(skyblockItem);

            for (Item item : values()) {
                if (item.getBaseItem() == skyblockItem)
                    recursiveCollection(item, collection);
            }
        }

        private final String id;
        private final String item;
        private final int meta;
        private final Item baseItem;
        private final Set<Recipe.Group> recipeGroups = new HashSet<>();
        private final CraftingTable.Recipe recipe;
        private final Set<Item> collection = new HashSet<>();
/*
        Item(String id, Block block) {
            this(id, block, 0);
        }

        Item(String id, Block block, int meta) {
            this(id, net.minecraft.item.Item.getItemFromBlock(block), meta);
        }
*/
        Item(String id, String item) {
            this(id, item, 0);
        }

        Item(String id, String item, int meta) {
            this(id, item, meta, null, CraftingTable.Recipe.NONE, new Recipe.Group());
        }
/*
        Item(String id, Block block, Item baseItem, CraftingTable.Recipe recipe, Recipe.Entry... recipeEntries) {
            this(id, block, 0, baseItem, recipe, recipeEntries);
        }

        Item(String id, Block block, int meta, Item baseItem, CraftingTable.Recipe recipe, Recipe.Entry... recipeEntries) {
            this(id, net.minecraft.item.Item.getItemFromBlock(block), meta, baseItem, recipe, recipeEntries);
        }
*/
        Item(String id, String item, Item baseItem, CraftingTable.Recipe recipe, Recipe.Entry... recipeEntries) {
            this(id, item, 0, baseItem, recipe, new Recipe.Group(recipeEntries));
        }

        Item(String id, String item, int meta, Item baseItem, CraftingTable.Recipe recipe, Recipe.Entry... recipeEntries) {
            this(id, item, meta, baseItem, recipe, new Recipe.Group(recipeEntries));
        }
/*
        Item(String id, Block block, Item baseItem, CraftingTable.Recipe recipe, Recipe.Group... recipeGroups) {
            this(id, block, 0, baseItem, recipe, recipeGroups);
        }

        Item(String id, Block block, int meta, Item baseItem, CraftingTable.Recipe recipe, Recipe.Group... recipeGroups) {
            this(id, net.minecraft.item.Item.getItemFromBlock(block), meta, baseItem, recipe, recipeGroups);
        }
*/
        Item(String id, String item, Item baseItem, CraftingTable.Recipe recipe, Recipe.Group... recipeGroups) {
            this(id, item, 0, baseItem, recipe, recipeGroups);
        }

        Item(String id, String item, int meta, Item baseItem, CraftingTable.Recipe recipe, Recipe.Group... recipeGroups) {
            this.id = id;
            this.item = item;
            this.meta = meta;
            this.baseItem = baseItem;
            this.recipe = recipe;
            this.recipeGroups.addAll(Arrays.asList(recipeGroups));
        }

        public String getItem() {
            return this.item;
        }

        public String getItemId() {
            return this.id;
        }

        public String getItemName() {
            return this.name();
        }

        public String getPrettyItemName() {
            return RegexUtil.strip(WordUtil.capitalizeFully(this.getItemName().replace("_", " ")), RegexUtil.VANILLA_PATTERN);
        }

        public Item getBaseItem() {
            return this.baseItem;
        }

        public Set<Item> getCollection() {
            return this.collection;
        }

        public Set<String> getCollectionItemNames() {
            return this.collection.stream().map(Item::getItemName).collect(Collectors.toSet());
        }
/*
        public ItemStack getItemStack() {
            return new ItemStack(this.getItem(), 1, this.getMeta());
        }
*/
        public int getMeta() {
            return this.meta;
        }

        public CraftingTable.Recipe getRecipe() {
            return this.recipe;
        }

        public Set<Recipe.Group> getRecipeGroups() {
            return this.recipeGroups;
        }
/*
        public int getStackSizeLimit() {
            return this.item.getItemStackLimit(null);
        }

        public static Item getSkyblockItem(String itemId) {
            return Arrays.stream(values()).filter(item -> item.getItemId().equalsIgnoreCase(itemId)).collect(ListUtil.toSingleton());
        }

        public static Item getSkyblockItem(ItemStack itemStack) {
            if (!itemStack.hasTagCompound())
                return null;

            NbtCompound nbt = SimplifiedAPI.getNbtFactory().fromItemStack(itemStack);

            return Arrays.stream(values()).filter(skyblockItem -> {
                boolean idMatch;

                if (!nbt.containsKey("ExtraAttributes"))
                    idMatch = true;
                else {
                    NbtCompound extraAttributes = nbt.get("ExtraAttributes");
                    String itemId = extraAttributes.get("id");
                    idMatch = skyblockItem.getItemId().equals(itemId);
                }

                return skyblockItem.getItem().equals(itemStack.getItem()) && idMatch;
            }).collect(ListUtil.toSingleton());
        }
*/
        public enum Rarity {

            COMMON(MinecraftChatFormatting.WHITE),
            UNCOMMON(MinecraftChatFormatting.GREEN),
            RARE(MinecraftChatFormatting.BLUE),
            EPIC(MinecraftChatFormatting.DARK_PURPLE),
            LEGENDARY(MinecraftChatFormatting.GOLD),
            MYTHIC(MinecraftChatFormatting.LIGHT_PURPLE),
            SPECIAL(MinecraftChatFormatting.RED),
            VERY_SPECIAL(MinecraftChatFormatting.RED),
            SUPREME(MinecraftChatFormatting.DARK_RED);

            private final MinecraftChatFormatting color;

            Rarity(MinecraftChatFormatting color) {
                this.color = color;
            }

            public MinecraftChatFormatting getColor() {
                return this.color;
            }

        }

        public static class Recipe {

            public static class Entry {

                private final Item skyblockItem;
                private final int quantity;
                private final int repeat;

                Entry(Item skyblockItem, int quantity) {
                    this(skyblockItem, quantity, 1);
                }

                Entry(Item skyblockItem, int quantity, int repeat) {
                    this.skyblockItem = skyblockItem;
                    this.quantity = quantity;
                    this.repeat = repeat;
                }

                public Item getSkyblockItem() {
                    return this.skyblockItem;
                }

                public int getQuantity() {
                    return this.quantity;
                }

                public int getRepeat() {
                    return this.repeat;
                }

            }

            public static class Group {

                private final LinkedList<Entry> recipeEntries = new LinkedList<>();

                Group(Item skyblockItem, int quantity) {
                    this(skyblockItem, quantity, 1);
                }

                Group(Item skyblockItem, int quantity, int repeat) {
                    this.recipeEntries.add(new Entry(skyblockItem, quantity, repeat));
                }

                Group(Entry... recipeEntries) {
                    this.recipeEntries.addAll(Arrays.asList(recipeEntries));
                }

                public LinkedList<Entry> getRecipeEntries() {
                    return this.recipeEntries;
                }

            }

        }

    }

    public enum Pet {

        BABY_YETI,
        BAT,
        BEE,
        BLACK_CAT,
        BLAZE,
        BLUE_WHALE,
        CHICKEN,
        DOLPHIN,
        ELEPHANT,
        ENDERMAN,
        ENDERMITE,
        ENDER_DRAGON,
        FLYING_FISH,
        GHOUL,
        GIRAFFE,
        GOLEM,
        GUARDIAN,
        HORSE,
        HOUND,
        JELLYFISH,
        JERRY,
        LION,
        MAGMA_CUBE,
        MONKEY,
        OCELOT,
        PARROT,
        PHOENIX,
        PIG,
        PIGMAN,
        RABBIT,
        ROCK,
        SHEEP,
        SILVERFISH,
        SKELETON,
        SKELETON_HORSE,
        SNOWMAN,
        SPIDER,
        SQUID,
        TARANTULA,
        TIGER,
        TURTLE,
        WITHER_SKELETON,
        WOLF,
        ZOMBIE;

        private final BiFunction<Integer, Skyblock.Item.Rarity, ConcurrentList<Object>> data = (level, rarity) -> _i();

        Pet() { }
/*
        public ConcurrentList<Ability> getAbilities() {
            return Ability.getAbilities(this);
        }
*/
        public BiFunction<Integer, Skyblock.Item.Rarity, ConcurrentList<Object>> getData() {
            return this.data;
        }

        public ConcurrentList<String> getLore(int petLevel, Skyblock.Item.Rarity rarity) {
            ConcurrentList<Object> stats = Concurrent.newList();
            ConcurrentList<Object> statValues = this.data.apply(petLevel, rarity);
            ConcurrentList<String> lore = Concurrent.newList();

            for (int i = 0; i < stats.size(); i++) {
                Object stat = stats.get(i);
                double value = Double.parseDouble(statValues.get(i).toString());
                lore.add(FormatUtil.preformat("{0}: {{1}}{{2}}", MinecraftChatFormatting.GRAY, MinecraftChatFormatting.GREEN, "stat.getDisplayName()", (value < 1 ? "" : "+"), (int)value));
            }

            return lore;
        }

        private static ConcurrentList<Object> _i(Object... values) {
            return Concurrent.newList(values);
        }

        public enum Ability {

            // Baby Yeti
            COLD_BREEZE(BABY_YETI, Skyblock.Item.Rarity.EPIC, "Gives {{0}} %STRENGTH% and\n%CRIT_DAMAGE% when near snow", (level, rarity) -> _i(level * 0.5)),
            ICE_SHIELDS(BABY_YETI, Skyblock.Item.Rarity.EPIC, "Gain {{0}%} of your strength\nas %DEFENSE%", (level, rarity) -> _i(level)),
            YETI_FURY(BABY_YETI, Skyblock.Item.Rarity.LEGENDARY, "Buff the Yeti sword by {{0}}\n%DAMAGE% and %INTELLIGENCE%", (level, rarity) -> _i(level)),

            // Bat
            FAST_HOOKS(BAT, Skyblock.Item.Rarity.LEGENDARY, "Decreases the cooldown of\nyour grappling hook by {{0}%}", (level, rarity) -> _i(level * 0.75)),
            NIGHTMARE(BAT, Skyblock.Item.Rarity.RARE, "During night, gain {{0}}\n%INTELLIGENCE%, {{0}} %SPEED%,\nand night vision", (level, rarity) -> _i(level * (rarity.ordinal() > 2 ? 0.3 : 0.2), level * (rarity.ordinal() > 2 ? 0.5 : 0.4))),
            CANDY_LOVER(BAT, Skyblock.Item.Rarity.COMMON, "Increases the chance for\nmobs to drop Candy by {{0}%}", (level, rarity) -> _i(smallDecimalFormat.format(level * (rarity.ordinal() > 2 ? 0.2 : rarity.ordinal() > 0 ? 0.1505 : 0.1)))),

            // Bee
            HIVE(BEE, Skyblock.Item.Rarity.COMMON, "Gain {+{0}} %INTELLIGENCE%\nand {+{1}} %STRENGTH% for\neach nearby bee\n{2}", (level, rarity) -> _i(
                    smallDecimalFormat.format((rarity.ordinal() == 4 ? 1.2 : rarity.ordinal() == 0 ? 1 : 1.1) + ((level - 1) * (rarity == Skyblock.Item.Rarity.COMMON ? 0.02 : ((((rarity.ordinal() * 5) - (rarity.ordinal() == 4 ? 1.2 : 1.1)) / 99) + 0.0001)))),
                    //smallDecimalFormat.format((rarity.ordinal() == 4 ? 1.2 : rarity.ordinal() == 0 ? 1 : 1.1) + ((level - 1) * Concurrent.newList(0.02, 0.0, 0.0899, 0.1405, 0.1899).get(rarity.ordinal()))),
                    smallDecimalFormat.format((rarity.ordinal() == 0 ? 1 : 1.1) + ((level - 1) * Concurrent.newList(0.02, 0.0, 0.0697, 0.1102, 0.1405).get(rarity.ordinal()))),
                    FormatUtil.preformat("Max 15 bees", MinecraftChatFormatting.DARK_GRAY)
            )),
            BUZZ_BUZZ_BUZZ(BEE, Skyblock.Item.Rarity.RARE, "Has {{0}%} chance for flowers\nto drop an extra one", (level, rarity) -> _i(
                    (rarity == Skyblock.Item.Rarity.LEGENDARY ? 1 : 0.5) + ((level - 1) * (rarity == Skyblock.Item.Rarity.LEGENDARY ? 1 : rarity == Skyblock.Item.Rarity.EPIC ? 1.005 : 1))
            )),
            WEAPONIZED_HONEY(BEE, Skyblock.Item.Rarity.LEGENDARY, "Gain {{0}%} of received\ndamage as Absorption", (level, rarity) -> _i(5.2 + ((level - 1) * 0.2))),

            // Black Cat
            HUNTER(BLACK_CAT, Skyblock.Item.Rarity.LEGENDARY, "Increases your speed and\nspeed cap by +{{0}}", (level, rarity) -> _i(level)),
            OMEN(BLACK_CAT, Skyblock.Item.Rarity.LEGENDARY, "Grants {{0}%} %PET_LUCK%", (level, rarity) -> _i(smallDecimalFormat.format(0.1 + ((level - 1) * 0.1506)))),
            SUPERNATURAL(BLACK_CAT, Skyblock.Item.Rarity.LEGENDARY, "Grants {{0}%} %MAGIC_FIND%", (level, rarity) -> _i(smallDecimalFormat.format(0.1 + ((level - 1) * 0.1506)))),

            // Blaze
            NETHER_EMBODIMENT(BLAZE, Skyblock.Item.Rarity.EPIC, "Increases all stats by {{0}%}\nwhen on the Blazing Fortress", (level, rarity) -> _i(level * 0.2)),
            BLING_ARMOR(BLAZE, Skyblock.Item.Rarity.EPIC, "Upgrades {{0}} stats\nand ability by {{1}%}", (level, rarity) -> _i(FormatUtil.preformat("Blaze Armor", MinecraftChatFormatting.RED), level * 0.4)),
            FUSION__STYLE_POTATO(BLAZE, Skyblock.Item.Rarity.EPIC, "Double effects of hot potato\nbooks"),

            // Blue Whale
            INGEST(BLUE_WHALE, Skyblock.Item.Rarity.COMMON, "All potions heal {0}", (level, rarity) -> _i(FormatUtil.preformat("+{0}%HEALTH_SYMBOL%", MinecraftChatFormatting.RED,level * (0.5 + rarity.ordinal())))),
            BULK(BLUE_WHALE, Skyblock.Item.Rarity.RARE, "Gain {{0}%DEFENSE%} per\n{1}", (level, rarity) -> _i(
                    smallDecimalFormat.format(level / 33.33),
                    FormatUtil.preformat("{0} Max %HEALTH%", MinecraftChatFormatting.RED, smallDecimalFormat.format(30 - ((rarity.ordinal() - 2) * 2.5)))
            )),
            ARCHIMEDES(BLUE_WHALE, Skyblock.Item.Rarity.LEGENDARY, "Gain {0}", (level, rarity) -> _i(FormatUtil.preformat("+{0}% Max %HEALTH%", MinecraftChatFormatting.RED, smallDecimalFormat.format(0.1 + ((level - 1) * 0.2015))))),

            // Chicken
            LIGHT_FEET(CHICKEN, Skyblock.Item.Rarity.COMMON, "Reduces fall damage by {{0}%}", (level, rarity) -> _i(level * (0.3 + (rarity.ordinal() > 2 ? 0.2 : rarity.ordinal() > 1 ? 0.1 : 0)))),
            EGGSTRA(CHICKEN, Skyblock.Item.Rarity.RARE, "Killing chickens has a {{0}%}\nchance to drop an egg", (level, rarity) -> _i(level * (0.8 + (rarity.ordinal() > 2 ? 0.2 : 0)))),
            MIGHTY_CHICKENS(CHICKEN, Skyblock.Item.Rarity.LEGENDARY, "Chicken minions work {{0}%}\nfaster while on your island", (level, rarity) -> _i(level * 0.3)),

            // Dolphin
            POD_TACTICS(DOLPHIN, Skyblock.Item.Rarity.COMMON, "Increases your fishing speed\nby {{0}%} for each player\n within 10 blocks up to {{1}%}", (level, rarity) -> _i(
                    smallDecimalFormat.format(level * (rarity.ordinal() > 2 ? 0.05 : (rarity.ordinal() > 0 ? 0.04 : 0.03))),
                    15 + (rarity.ordinal() > 2 ? 10 : (rarity.ordinal() > 0 ? 5 : 0))
            )),
            ECHOLOCATION(DOLPHIN, Skyblock.Item.Rarity.RARE, "Increases sea creatures catch\nchance by {{0}%}", (level, rarity) -> _i(smallDecimalFormat.format(level * (rarity == Skyblock.Item.Rarity.RARE ? 0.0697 : 0.1)))),
            SPLASH_SURPRISE(DOLPHIN, Skyblock.Item.Rarity.LEGENDARY, "Stun sea creatures for {{0}s}\nafter fishing them up", (level, rarity) -> _i(5)),

            // Elephant
            STOMP(ELEPHANT, Skyblock.Item.Rarity.COMMON, "Gain {{0} %DEFENSE%} for every\n100 %SPEED%", (level, rarity) -> _i(level * (rarity == Skyblock.Item.Rarity.LEGENDARY ? 0.2 : 0.1))),
            WALKING_FORTRESS(ELEPHANT, Skyblock.Item.Rarity.RARE, "Gain {0} for every\n10 %DEFENSE%", (level, rarity) -> _i(
                    FormatUtil.preformat("{0} %HEALTH%", MinecraftChatFormatting.RED, smallDecimalFormat.format(level / 10))
            )),
            TRUNK_EFFICIENCY(ELEPHANT, Skyblock.Item.Rarity.LEGENDARY, "Grants a {{0}%} chance to\nget double crops while farming", (level, rarity) -> _i(level * 0.2)),

            // Enderman
            ENDERIAN(ENDERMAN, Skyblock.Item.Rarity.COMMON, "Take {{0}%} less damage\nfrom end monsters", (level, rarity) -> _i(level * (0.1 + (rarity.ordinal() > 2 ? 0.2 : rarity.ordinal() > 0 ? 0.1 : 0)))),
            TELEPORT_SAVVY(ENDERMAN, Skyblock.Item.Rarity.RARE, "Buffs the Aspect of the End\nability granting {{0}} weapon\ndamage for 5s on use", (level, rarity) -> _i(level * (rarity == Skyblock.Item.Rarity.RARE ? 0.4 : 0.5))),
            ZEALOT_MADNESS(ENDERMAN, Skyblock.Item.Rarity.LEGENDARY, "Increases your odds to find a\nspecial Zealot by {{0}%}", (level, rarity) -> _i(level * 0.25)),

            // Endermite
            MORE_STONKS(ENDERMITE, Skyblock.Item.Rarity.COMMON, "Gain more exp orbs for\nbreaking end stone and gain a\n+{{0}%} chance to get an\nextra block dropped", (level, rarity) -> _i(
                    level * (0.4 + (rarity.ordinal() >= 2 ? 0.1 : 0))
            )),
            PEARL_MUNCHER(ENDERMITE, Skyblock.Item.Rarity.RARE, "Upon picking up an ender\npearl, consume it and gain {{0}}\n{1}", (level, rarity) -> _i(5 + ((level - 1) * 0.0506), FormatUtil.preformat("coins", MinecraftChatFormatting.GOLD))),
            PEARL_POWERED(ENDERMITE, Skyblock.Item.Rarity.LEGENDARY, "Upon consuming an ender pearl,\ngain +{{0}} speed for 10 seconds", (level, rarity) -> _i(10.4 + ((level - 1) * 0.4))),

            // Ender Dragon
            END_STRIKE(ENDER_DRAGON, Skyblock.Item.Rarity.EPIC, "Deal {{0}%} more damage to\nend mobs", (level, rarity) -> _i(level * 0.25)),
            ONE_WITH_THE_DRAGON(ENDER_DRAGON, Skyblock.Item.Rarity.EPIC, "Buffs the Aspect of the\nDragons sword by {{0}} %DAMAGE%\nand {{1}} %STRENGTH%", (level, rarity) -> _i(level * 0.5, level * 0.3)),
            SUPERIOR(ENDER_DRAGON, Skyblock.Item.Rarity.LEGENDARY, "Increases all stats by {{0}%}", (level, rarity) -> _i(level * 0.1)),

            // Flying Fish
            QUICK_REEL(FLYING_FISH, Skyblock.Item.Rarity.COMMON, "Increases fishing speed\nby {{0}%}", (level, rarity) -> _i(NumberUtil.round(level * (0.075 + (rarity.ordinal() > 2 ? 0.075 : 0.05)), 1))),
            WATER_BENDER(FLYING_FISH, Skyblock.Item.Rarity.RARE, "Gives {{0}} %STRENGTH% and\n%DEFENSE% when near water", (level, rarity) -> _i(level * (0.4 + (rarity.ordinal() > 2 ? 0.1 : 0)))),
            DEEP_SEA_DIVER(FLYING_FISH, Skyblock.Item.Rarity.LEGENDARY, "Increases the stats of\nDiver Armor by {{0}%}", (level, rarity) -> _i(level * 0.3)),

            // Ghoul
            AMPLIFIED_HEALING(GHOUL, Skyblock.Item.Rarity.EPIC, "Increases all healing by {{0}%}", (level, rarity) -> _i(level * 0.25)),
            ZOMBIE_ARM(GHOUL, Skyblock.Item.Rarity.EPIC, "Increases the health and range\nof the Zombie sword by {{0}%}", (level, rarity) -> _i(level * 0.5)),
            REAPER_SOUL(GHOUL, Skyblock.Item.Rarity.LEGENDARY, "Increases the health and\nlifespam of the Repaer Scythe\nzombies by {{0}%}", (level, rarity) -> _i(level)),

            // Giraffe
            GOOD_HEART(GIRAFFE, Skyblock.Item.Rarity.COMMON, "Regen {0} per second", (level, rarity) -> _i(
                    FormatUtil.preformat("{{0} %HEALTH_SYMBOL%}", MinecraftChatFormatting.GRAY, MinecraftChatFormatting.RED,smallDecimalFormat.format((rarity == Skyblock.Item.Rarity.RARE ? 0.1 : 0) +
                            (level - (rarity == Skyblock.Item.Rarity.RARE ? 1 : 0)) *(rarity.ordinal() > 2 ? 0.2 : rarity.ordinal() < 2 ? 0.1 : 0.1506)))
            )),
            HIGHER_GROUND(GIRAFFE, Skyblock.Item.Rarity.COMMON, "Grants {{0}} and\n{{1}} when mid\nair", (level, rarity) -> _i(
                    FormatUtil.format("+{0} %STRENGTH%", MinecraftChatFormatting.RED, level * (rarity == Skyblock.Item.Rarity.RARE ? 0.4 : 0.5)),
                    FormatUtil.format("+{0} %CRIT_CHANCE%", MinecraftChatFormatting.BLUE, smallDecimalFormat.format((rarity == Skyblock.Item.Rarity.RARE ? 20.1 : 24.2) + ((level - 1) * rarity.ordinal() == 4 ? 0.3617 : 0))) // TODO: Not 0
            )),
            LONG_NECK(GIRAFFE, Skyblock.Item.Rarity.LEGENDARY, "See enemies from afar and gain\n{{0}%} dodge chance", (level, rarity) -> _i(level * 0.25)),

            // Golem
            LAST_STAND(GOLEM, Skyblock.Item.Rarity.EPIC, "While less than 15% HP, deal\n{{0}%} more damage", (level, rarity) -> _i(level * 0.3)),
            RICOCHET(GOLEM, Skyblock.Item.Rarity.EPIC, "Your iron plating causes\n{{0}%} of attacks to ricochet\nand hit the attacker", (level, rarity) -> _i(level * 0.2)),
            TOSS(GOLEM, Skyblock.Item.Rarity.LEGENDARY, "Every 5 hits, throw the enemy\nup into the air and deal {{0}%}\ndamage (10s cooldown)", (level, rarity) -> _i(203 + ((level - 1) * 3))),

            // Guardian
            LAZERBEAM(GUARDIAN, Skyblock.Item.Rarity.COMMON, "Zaps your enemies for {0}\nyour Intelligence every {{1}s}", (level, rarity) -> _i(
                    FormatUtil.preformat("{{0}x}", MinecraftChatFormatting.GRAY, MinecraftChatFormatting.AQUA, smallDecimalFormat.format(0.2 + ((level - 1) * (rarity == Skyblock.Item.Rarity.COMMON ? 0.01819 : ((((rarity.ordinal() * 5) - 0.2) / 99) + 0.0001))))), 3
            )),
            ENCHANTING_EXP_BOOST(GUARDIAN, Skyblock.Item.Rarity.RARE, "Boosts your Enchanting exp\nby {{0}%}", (level, rarity) -> _i(level * (rarity == Skyblock.Item.Rarity.RARE ? 0.25 : 0.3))),
            MANA_POOL(GUARDIAN, Skyblock.Item.Rarity.LEGENDARY, "Regenerate {{0}} extra mana,\ndoubled when near or in water", (level, rarity) -> _i(FormatUtil.preformat("{0}%", MinecraftChatFormatting.AQUA, level * 0.3))),

            // Hound
            SCAVENGER(HOUND, Skyblock.Item.Rarity.EPIC, "Grain +{{0}} coins per\nmonster kill", (level, rarity) -> _i(0.1 + ((level - 1) * 0.0495))),
            FINDER(HOUND, Skyblock.Item.Rarity.EPIC, "Increases the chance for\nmonsters to drop their armor\nby {{0}%}", (level, rarity) -> _i(level * 0.1)),
            FURY_CLAWS(HOUND, Skyblock.Item.Rarity.LEGENDARY, "Graints +{{0}}% %ATTACK_SPEED%", (level, rarity) -> _i(level * 0.1)),

            // Horse
            RIDABLE_1(HORSE, Skyblock.Item.Rarity.COMMON, "Right-click your summoned pet\nto ride it!"),
            RUN_1(HORSE, Skyblock.Item.Rarity.RARE, "Increase the speed of your\nmount by {{0}%}", (level, rarity) -> _i(level * (rarity == Skyblock.Item.Rarity.RARE ? 1.2 : 1.5))),
            RIDE_INTO_BATTLE_1(HORSE, Skyblock.Item.Rarity.LEGENDARY, "Increases the speed of your mount by\n+{{0}%}", ((level, rarity) -> _i(0.4 + ((level - 1) * 0.2)))),

            // Jellyfish
            RADIANT_REGENERATION(JELLYFISH, Skyblock.Item.Rarity.EPIC, "While in dungeons, increases\nyour base health regen by {{0}%}\nand heals players within 8\nblocks by up to 10hp/s", (level, rarity1) -> _i(level)),
            HUNGRY_HEALER(JELLYFISH, Skyblock.Item.Rarity.EPIC, "While in dungeons, for every\n1000 you heal teammates apply\nthe {{0}}\neffect to all players within\n10 blocks (10s cooldown)", (level, rarity1) -> _i("enchanted golden apple")),
            POWERFUL_POTIONS(JELLYFISH, Skyblock.Item.Rarity.LEGENDARY, "While in dungeons, increase\nthe effectiveness of Instant\nHealth and Mana splash potions\nby {{0}%}", (level, rarity1) -> _i(level * 0.5)),

            // Jerry
            JERRY_1(JERRY, Skyblock.Item.Rarity.UNCOMMON, "Gain {{0}%} chance to deal\nyour regular damage", (level, rarity) -> _i(50)),
            JERRY_2(JERRY, Skyblock.Item.Rarity.EPIC, "Gain {{0}%} chance to\nreceive a normal amount of drops\nfrom mobs", (level, rarity) -> _i(100)),
            JERRY_3(JERRY, Skyblock.Item.Rarity.LEGENDARY, "Actually adds {{0}} %DAMAGE% to\nthe Aspect of the Jerry", (level, rarity) -> _i(FormatUtil.preformat(smallDecimalFormat.format(level / 10), MinecraftChatFormatting.RED))),

            // Lion
            PRIMAL_FORCE(LION, Skyblock.Item.Rarity.COMMON, "Adds {0} %DAMAGE% to\nyour weapons", (level, rarity) -> _i(
                    FormatUtil.preformat("+{0}", MinecraftChatFormatting.RED, smallDecimalFormat.format((rarity.ordinal() * 0.05) + ((level - 1) * (rarity == Skyblock.Item.Rarity.COMMON ? 0.03 : (rarity.ordinal() * 0.05)))))
            )),
            FIRST_POUNCE(LION, Skyblock.Item.Rarity.RARE, "Increases damage dealt\nby {{0}%} on your first\nhit on a mob", (level, rarity) -> _i(
                    (rarity == Skyblock.Item.Rarity.LEGENDARY ? 1 : 0.5) + ((level - 1) * (rarity == Skyblock.Item.Rarity.LEGENDARY ? 1 : rarity == Skyblock.Item.Rarity.EPIC ? 1.005 : 1))
            )),
            KING_OF_THE_JUNGLE(LION, Skyblock.Item.Rarity.LEGENDARY, "Deal {+{0}%} %DAMAGE% against\nmobs below level 80", (level, rarity) -> _i(5.2 + ((level - 1) * 0.2))),

            // Magma Cube
            SLIMY_MINIONS(MAGMA_CUBE, Skyblock.Item.Rarity.COMMON, "Slime minions work {{0}%}\nfaster while on your island",(level, rarity) -> _i(level * (rarity.ordinal() >= 3 ? 0.3 : 0.2))),
            SALT_BLADE(MAGMA_CUBE, Skyblock.Item.Rarity.RARE, "Deal {{0}%} more damage to\nslimes", (level, rarity) -> _i(level * 0.2)),
            HOT_EMBER(MAGMA_CUBE, Skyblock.Item.Rarity.LEGENDARY, "Buffs the stats of Ember Armor\nby {{0}%}", (level, rarity) -> _i(level)),

            // Monkey
            TREEBORN(MONKEY, Skyblock.Item.Rarity.COMMON, "Increase double drop rates for\nlogs by {{0}%}", (level, rarity) -> _i(level * (rarity.ordinal() > 2 ? 0.3 : (rarity.ordinal() > 0 ? 0.25 : 0.2)))),
            VINE_SWING(MONKEY, Skyblock.Item.Rarity.RARE, "Gain {{0}} %SPEED% while\nin The Park", (level, rarity) -> _i(
                    smallDecimalFormat.format(rarity == Skyblock.Item.Rarity.RARE ? 0.8 + ((level - 1) * 0.7495) : level) // 0.7492
            )),
            EVOLVED_AXES(MONKEY, Skyblock.Item.Rarity.LEGENDARY, "Reduce the cooldown of Jungle\nAxe and Treecapitator by {{0}%}", (level, rarity) -> _i(level / 2)),

            // Ocelot
            FORAGING_EXP_BOOST(OCELOT, Skyblock.Item.Rarity.COMMON, "Boosts your Foraging exp by\n{{0}%}", (level, rarity) -> _i(level * (rarity.ordinal() >= 3 ? 0.3 : 0.2))),
            TREE_HUGGER(OCELOT, Skyblock.Item.Rarity.RARE, "Foraging minions work {{0}%}\nfaster while on your island", (level, rarity) -> _i(level * (rarity.ordinal() >= 3 ? 0.3 : 0.2))),
            TREE_ESSENCE(OCELOT, Skyblock.Item.Rarity.LEGENDARY, "Gain a {{0}%} chance to get\nexp from breaking a long", (level, rarity) -> _i(level * 0.3)),

            // Parrot
            FLAMBOYANT(PARROT, Skyblock.Item.Rarity.EPIC, "Adds {{0}} levels to\nintimidation accessories", (level, rarity) -> _i(smallDecimalFormat.format(level / (rarity.ordinal() == 4 ? 5 : 6.666)))),
            REPEAT(PARROT, Skyblock.Item.Rarity.EPIC, "Boosts potions duration by\n{{0}%}", (level, rarity) -> _i(5.3 + ((level - 1) * 0.350505))),
            BIRD_DISCOURSE(PARROT, Skyblock.Item.Rarity.LEGENDARY, "Gives {0} to\nplayers within {{1}} blocks\n{2}", (level, rarity) -> _i(
                    FormatUtil.preformat("+{0} %STRENGTH%", MinecraftChatFormatting.RED, 5.2 + ((level - 1) * 0.250505)),
                    20, FormatUtil.preformat("Doesnt Stack", MinecraftChatFormatting.DARK_GRAY)
            )),

            // Pig
            RIDABLE_3(PIG, Skyblock.Item.Rarity.COMMON, "Right-click your summoned pet\nto ride it!"),
            RUN(PIG, Skyblock.Item.Rarity.COMMON, "Increases the speed of your\nmount by {{0}%}", (level, rarity) -> _i(level * (rarity.ordinal() >= 3 ? 0.5 : rarity.ordinal() == 0 ? 0.3 : 0.4))),
            SPRINT(PIG, Skyblock.Item.Rarity.RARE, "While holding an Enchanted\nCarrot on a Stick, increase the\nspeed of your mount by {{0}%}", (level, rarity) -> _i(level * (rarity.ordinal() >= 3 ? 0.5 : 0.4))),
            TRAMPLE(PIG, Skyblock.Item.Rarity.LEGENDARY, "While on your private island,\nbreak all crops your pig rides\nover"),

            // Pigman
            BACON_FARMER(PIGMAN, Skyblock.Item.Rarity.COMMON, "Pig minions work {{0}%}\nfaster on your island", (level, rarity) -> _i(level * 0.3)),
            PORK_MASTER(PIGMAN, Skyblock.Item.Rarity.RARE, "Buffs the Pigman sword by {{0}}\n%DAMAGE% and {{1}} %STRENGTH%", (level, rarity) -> _i(level * 0.4, level * 0.25)),
            GIANT_SLAYER(PIGMAN, Skyblock.Item.Rarity.LEGENDARY, "Deal {{0}%} extra damage to\nmonsters level 100 and up", (level, rarity) -> _i(smallDecimalFormat.format(0.2 + ((level - 1) * 0.2505)))),

            // Phoenix
            REKINDLE(PHOENIX, Skyblock.Item.Rarity.EPIC, "Before death, become {0}\nand gain {1} %STRENGTH%\nfor {{2}} seconds\n{3}", (level, rarity) -> _i(
                    FormatUtil.preformat("immune", MinecraftChatFormatting.YELLOW),
                    FormatUtil.preformat("{0}", MinecraftChatFormatting.RED, Math.round(10.1 + ((level - 1) * (rarity == Skyblock.Item.Rarity.LEGENDARY ? 0.202 : 0.1)))),
                    smallDecimalFormat.format(2 + ((level - 1) * 0.02)),
                    FormatUtil.preformat("3 minutes cooldown", MinecraftChatFormatting.DARK_GRAY)
            )),
            FOURTH_FLARE(PHOENIX, Skyblock.Item.Rarity.EPIC, "On 4th melee strike, {0}\nmobs, dealing {{1}} your\n%CRIT_DAMAGE% each second\nfor {{2}} seconds", (level, rarity) -> _i(
                    FormatUtil.preformat("ignite", MinecraftChatFormatting.GOLD),
                    FormatUtil.preformat("{0}x", MinecraftChatFormatting.RED, smallDecimalFormat.format(1.1 + ((level - 1) * (rarity == Skyblock.Item.Rarity.LEGENDARY ? 0.1405 : 0.121)))), // epic: 0.07?
                    smallDecimalFormat.format(2 + (level / 33.33))
            )),
            MAGIC_BIRD(PHOENIX, Skyblock.Item.Rarity.LEGENDARY, "You may always fly on your\nprivate island"),
            ETERNAL_COINS(PHOENIX, Skyblock.Item.Rarity.LEGENDARY, "Don''t lose coins from death"),

            // Rabbit
            HAPPY_FEET(RABBIT, Skyblock.Item.Rarity.COMMON, "Jump potions also give {+{0}}\nspeed", (level, rarity) -> _i(level * (rarity.ordinal() >= 3 ? 0.5 : rarity.ordinal() == 0 ? 0.3 : 0.4))),
            FARMING_EXP_BOOST(RABBIT, Skyblock.Item.Rarity.RARE, "Boost your Farming exp by {{0}%}", (level, rarity) -> _i(level * (rarity.ordinal() >= 3 ? 0.3 : 0.25))),
            EFFICIENT_FARMING(RABBIT, Skyblock.Item.Rarity.LEGENDARY, "Farming minions work {{0}%}\nfaster while on your island", (level, rarity) -> _i(level * 0.3)),

            // Rock
            RIDABLE_4(ROCK, Skyblock.Item.Rarity.COMMON, "Right-click your summoned pet\nto ride it!"),
            SAILING_STONE(ROCK, Skyblock.Item.Rarity.COMMON, "Right-click your summoned pet\nyour location (15s cooldown)"),
            FORTIFY(ROCK, Skyblock.Item.Rarity.RARE, "While sitting on your rock,\ngain +{{0}%} defense", (level, rarity) -> _i(level * 0.2)),
            STEADY_GROUND(ROCK, Skyblock.Item.Rarity.LEGENDARY, "While sitting on your rock,\ngain +{{0}%} damage", (level, rarity) -> _i(level * 0.3)),

            // Sheep
            MANA_SAVER(SHEEP, Skyblock.Item.Rarity.COMMON, "Reduces the mana cost of\nabilities by {{0}%}", (level, rarity) -> _i(level * 0.2)),
            OVERHEAL(SHEEP, Skyblock.Item.Rarity.RARE, "Gives a {{0}%} shield after\nnot taking damage for 10s", (level, rarity) -> _i(level * 0.1)),
            DUNGEON_WIZARD(SHEEP, Skyblock.Item.Rarity.LEGENDARY, "Increases your total mana by\n{{0}%} white in dungeons", (level, rarity) -> _i(level * 0.25)),

            // Skeleton
            BONE_ARROWS(SKELETON, Skyblock.Item.Rarity.COMMON, "Increase arrow damage by\n{{0}%} which is tripled while\nin dungeons", (level, rarity1) -> _i(level * 0.2)),
            COMBO(SKELETON, Skyblock.Item.Rarity.RARE, "Gain a combo stack for every\nbow hit granting +{{0}} %STRENGTH%.\nMax {{1}} stacks, stacks\ndisappear after 8 seconds", (level, rarity1) -> _i(3, level * 0.2)),
            SKELETON_DEFENSE(SKELETON, Skyblock.Item.Rarity.LEGENDARY, "Your skeleton shoots an arrow\ndealing {{0}x} your %CRIT_DAMAGE%\nwhen a mob gets close to you\n(15s cooldown)", (level, rarity1) -> _i(30)),

            // Skeleton Horse
            RIDABLE_2(SKELETON_HORSE, Skyblock.Item.Rarity.LEGENDARY, "Right-click your summoned pet\nto ride it!"),
            RUN_2(SKELETON_HORSE, Skyblock.Item.Rarity.LEGENDARY, "Increase the speed of your\nmount by {{0}%}", (level, rarity) -> _i(level * 1.5)),
            RIDE_INTO_BATTLE_2(SKELETON_HORSE, Skyblock.Item.Rarity.LEGENDARY, "Increases the speed of your mount by\n+{{0}%}", (level, rarity) -> _i(level * 0.4)),

            // Silverfish
            TRUE_DEFENSE_BOOST(SILVERFISH, Skyblock.Item.Rarity.COMMON, "Boosts your %TRUE_DEFENSE%\nby {{0}}", (level, rarity) -> _i(level * (rarity.ordinal() > 2 ? 0.15 : rarity.ordinal() == 0 ? (level / 2.0) * 0.1 : 0.1))),
            MINING_EXP_BOOST(SILVERFISH, Skyblock.Item.Rarity.RARE, "Boosts your Mining exp by\n{{0}%}", (level, rarity) -> _i(level * (rarity.ordinal() >= 3 ? 0.3 : 0.25))),
            DEXTERITY(SILVERFISH, Skyblock.Item.Rarity.LEGENDARY, "Gives permanent haste III"),

            // Snowman
            FIGHTER(SNOWMAN, Skyblock.Item.Rarity.LEGENDARY, "Will assist you in combat!"),
            STRONG_ARM(SNOWMAN, Skyblock.Item.Rarity.LEGENDARY, "Increases the snowball fire\nrate by {{0}%}", (level, rarity) -> _i(level)),

            // Spider
            ONE_WITH_THE_SPIDER(SPIDER, Skyblock.Item.Rarity.COMMON, "Gain {{0}} %STRENGTH% for\nevery nerby spider\n{1}", (level, rarity) -> _i(level * 0.1, FormatUtil.preformat("Max 10 spiders", MinecraftChatFormatting.DARK_GRAY))),
            WEB_WEAVER(SPIDER, Skyblock.Item.Rarity.RARE, "Upon hitting a monster it\nbecomes slowed by {{0}%}", (level, rarity) -> _i(level * 0.4)),
            SPIDER_WHISPERER(SPIDER, Skyblock.Item.Rarity.LEGENDARY, "Spider and tarantula minions\nwork {{0}%} faster while on\nyour island", (level, rarity) -> _i(level * 0.3)),

            // Squid
            MORE_INK(SQUID, Skyblock.Item.Rarity.COMMON, "Gain a {{0}%} chance to get\ndouble drops from squids", (level, rarity) -> _i(level * (rarity.ordinal() >= 3 ? 1 : rarity.ordinal() == 0 ? 0.5 : 0.8))),
            INK_SPECIALTY(SQUID, Skyblock.Item.Rarity.RARE, "Buffs the Ink Wand by {{0}}\n%DAMAGE% and {{1}} %STRENGTH%", (level, rarity) -> _i(level * (rarity.ordinal() >= 3 ? 0.4 : 0.3), level * (rarity.ordinal() >= 3 ? 0.2 : 0.1))),
            FISHING_EXP_BOOST(SQUID, Skyblock.Item.Rarity.LEGENDARY, "Boost your Fishing exp by\n{{0}}", (level, rarity) -> _i(level * 0.3)),

            // Tarantula
            WEBBED_CELLS(TARANTULA, Skyblock.Item.Rarity.COMMON, "Anti-healing is {{0}%} less\neffective against you", (level, rarity) -> _i(level * 0.3)),
            EIGHT_LEGS(TARANTULA, Skyblock.Item.Rarity.RARE, "Decreases the mana cost of\nSpider and Tarantula boots by\n{{0}%}", (level, rarity) -> _i(level * 0.5)),
            ARACHNID_SLAYER(TARANTULA, Skyblock.Item.Rarity.LEGENDARY, "Gain +{{0}%} more combat xp\nfrom spiders", (level, rarity) -> _i(level * 0.4)),

            // Tiger
            MERCILESS_SWIPE(TIGER, Skyblock.Item.Rarity.COMMON, "Attacks have a {{0}%} chance\nto strike twice", (level, rarity) -> _i(level * (rarity.ordinal() >= 3 ? 0.2: rarity.ordinal() == 0 ? 0.05 : 0.1))),
            HEMORRHAGE(TIGER, Skyblock.Item.Rarity.RARE, "Melee attacks reduce healing\nby {0} for {{1}s}", (level, rarity) -> _i(FormatUtil.preformat("{{0}%}", MinecraftChatFormatting.GRAY, MinecraftChatFormatting.GOLD, level * (rarity.ordinal() >= 3 ? 0.55 : 0.3)), 10)),
            APEX_PREDATOR(TIGER, Skyblock.Item.Rarity.LEGENDARY, "Deal {0} damage against\ntargets with no other mobs\nwithin 15 blocks", (level, rarity) -> _i(FormatUtil.format("{+{0}%}", MinecraftChatFormatting.GRAY, MinecraftChatFormatting.RED, level * 0.2))),

            // Turtle
            TURTLE_TACTICS(TURTLE, Skyblock.Item.Rarity.EPIC, "Gain {+{0}%} %DEFENSE%", (level, rarity) -> _i(smallDecimalFormat.format(3.2 + ((level - 1) * 0.1702)))),
            GENIUS_AMNIOTE(TURTLE, Skyblock.Item.Rarity.EPIC, "Gain {+{0}} %DEFENSE% and\nregen {{1}} per second when\nnear or in water", (level, rarity) -> _i(
                    smallDecimalFormat.format(5.5 + ((level - 1) * (rarity.ordinal() == 4 ? 0.45 : 0.347))),
                    FormatUtil.preformat("+{0} %HEALTH_SYMBOL%", MinecraftChatFormatting.RED, smallDecimalFormat.format(0.2 + ((level - 1) * (rarity.ordinal() == 4 ? 0.2513 : 0.2))))
            )),
            UNFLIPPABLE(TURTLE, Skyblock.Item.Rarity.LEGENDARY, "Gain {{0}} to knockback", (level, rarity) -> _i("immunity")),

            // Wither Skeleton
            STRONG_BONES(WITHER_SKELETON, Skyblock.Item.Rarity.COMMON, "Take {{0}%} less damage from\nskeletons", (level, rarity) -> _i(level * 0.3)),
            WITHER_BLOOD(WITHER_SKELETON, Skyblock.Item.Rarity.RARE, "Deal {{0}%} more damage to\nwither mobs", (level, rarity) -> _i(level * 0.5)),
            DEATHS_TOUCH(WITHER_SKELETON, Skyblock.Item.Rarity.LEGENDARY, "Upon hitting an enemy inflict\nthe wither effect for {{0}%}\ndamage over 3 seconds\n{1}", (level, rarity) -> _i(level * 2, FormatUtil.preformat("Does not Stack", MinecraftChatFormatting.DARK_GRAY))),

            // Wolf
            APLHA_DOG(WOLF, Skyblock.Item.Rarity.COMMON, "Take {{0}%} less damage from\nwolves", (level, rarity) -> _i(level * (rarity.ordinal() >= 3 ? 0.3 : rarity.ordinal() == 0 ? 0.1 : 0.2))),
            PACK_LEADER(WOLF, Skyblock.Item.Rarity.RARE, "Take {{0}%} less damage\nfrom wolves", (level, rarity) -> _i(level * (rarity.ordinal() == 4 ? 0.15 : 0.1))),
            COMBAT_EXP_BOOST(WOLF, Skyblock.Item.Rarity.LEGENDARY, "Boosts your Combat exp by\n{{0}%}", (level, rarity) -> _i(level * 0.3)),

            // Zombie
            CHOMP(ZOMBIE, Skyblock.Item.Rarity.EPIC, "Gain +{{0}} hp per zombie kill", (level, rarity) -> _i(smallDecimalFormat.format(0.2 + ((level - 1) * 0.2513)))),
            ROTTEN_BLADE(ZOMBIE, Skyblock.Item.Rarity.EPIC, "Deal {{0}%} more damage to\nzombie", (level, rarity) -> _i(level * 0.1)),
            LIVING_DEAD(ZOMBIE, Skyblock.Item.Rarity.LEGENDARY, "Increases the defense of all\nundead armor sets by {{0}%}", (level, rarity) -> _i(level * 0.12));

            private final Pet pet;
            private final Skyblock.Item.Rarity rarity;
            private final String format;
            private final BiFunction<Integer, Skyblock.Item.Rarity, ConcurrentList<Object>> data;
            private final BiFunction<Integer, Skyblock.Item.Rarity, String> transform;

            Ability(Pet pet, Skyblock.Item.Rarity rarity, String format) {
                this(pet, rarity, format, null);
            }

            Ability(Pet pet, Skyblock.Item.Rarity rarity, String format, BiFunction<Integer, Skyblock.Item.Rarity, ConcurrentList<Object>> data) {
                this.pet = pet;
                this.rarity = rarity;
                this.format = format;
                this.data = data;

                if (this.data != null)
                    this.transform = (l, r) -> FormatUtil.preformat(this.format, MinecraftChatFormatting.GRAY, MinecraftChatFormatting.GREEN, this.data.apply(l, r).toArray());
                else
                    this.transform = (l, r) -> FormatUtil.preformat(this.format, MinecraftChatFormatting.GRAY, MinecraftChatFormatting.GREEN);
            }

            public BiFunction<Integer, Skyblock.Item.Rarity, ConcurrentList<Object>> getData() {
                return this.data;
            }

            public String getFormat() {
                return this.format;
            }
/*
            public ConcurrentList<String> getLore(int petLevel, Skyblock.Item.Rarity rarity) {
                String fullLore = this.transform.apply(petLevel, rarity);
                fullLore = StringUtil.join(FormatUtil.format("\n{0}", MojangChatFormatting.GRAY.toString()), StringUtil.split("\n", fullLore));

                for (Skyblock.Stat stat : Skyblock.Stat.values()) {
                    String find = FormatUtil.format("%{0}%", stat.name());
                    String findSymbol = FormatUtil.format("%{0}_SYMBOL%", stat.name());

                    if (fullLore.contains(find))
                        fullLore = fullLore.replaceAll(find, stat.getFormattedDisplayName());

                    if (fullLore.contains(findSymbol))
                        fullLore = fullLore.replaceAll(findSymbol, stat.getSymbol());
                }

                return Concurrent.newList(StringUtil.split("\n", fullLore));
            }
*/
            public Pet getPet() {
                return this.pet;
            }

            public String getPrettyName() {
                return WordUtil.capitalizeFully(this.name().replace("__", "-").replace("_", " ").replaceAll("_[\\d]+$", ""));
            }

            public Skyblock.Item.Rarity getRarity() {
                return this.rarity;
            }

            public BiFunction<Integer, Skyblock.Item.Rarity, String> getTransform() {
                return this.transform;
            }
/*
            public static ConcurrentList<Ability> getAbilities(Pet pet) {
                ConcurrentList<Ability> abilities = Concurrent.newList();

                for (Ability lore : values()) {
                    if (lore.getPet() == pet)
                        abilities.add(lore);
                }

                return abilities;
            }

            public static ConcurrentList<Ability> getAbilities(SkyBlockIsland.PetInfo petInfo) {
                ConcurrentList<Ability> abilities = getAbilities(petInfo.getPet().get()); // TODO: Optional Handling
                abilities.removeIf(ability -> petInfo.getRarity().ordinal() < ability.getRarity().ordinal());
                return abilities;
            }
*/
        }

    }

    public enum InventoryType {

        PLAYER("Player Inventory", false, false),
        SKYBLOCK_MENU("SkyBlock Menu", true, false),
        SKILLS("Your Skills", true, true),
        COLLECTION("Collection", true, true),
        RECIPE_BOOK("Recipe Book", true, true),
        ANVIL("Anvil", false, false),
        ENCHANTMENT_TABLE("Enchant Item", false, false),
        REFORGE("Reforge Item", false, false),
        CRAFTING_TABLE("Craft Item", true, true),
        ENDER_CHEST("Ender Chest", true, true),
        POTION_BAG("Potion Bag", false, true),
        FISHING_BAG("Fishing Bag", false, true),
        QUIVER("Quiver", false, true),
        ACCESSORY_BAG("Accessory Bag", false, true),
        AUCTION_HOUSE("Auction House", false, false),
        AUCTION_BROWSER("Auction Browser", false, false),
        SETTINGS("Settings", true, true),
        WARDROBE("Wardrobe", true, true),
        BANK("Bank", false, false),
        PERSONAL_BANK("Personal Bank", false, true),
        PETS("Pets", true, true),
        QUEST_LOG("Quest Log", true, true),
        CALENDAR("Calendar and Events", true, true),
        TRADES("Trades", false, true),
        PROFILES("Profiles Management", false, true),
        NPC("NPC", false, false),
        BACKPACK("Backpack", false, false),
        OTHER("", false, false);

        private final String displayName;
        private final boolean hasCommand;
        private final boolean skyblockSubMenu;

        InventoryType(String displayName, boolean hasCommand, boolean skyblockSubMenu) {
            this.displayName = displayName;
            this.hasCommand = hasCommand;
            this.skyblockSubMenu = skyblockSubMenu;
        }

        public String getCommand() {
            if (this.hasCommand())
                return FormatUtil.format("{0}", (this == SKYBLOCK_MENU ? "sbmenu" : this.name().replace("_", "").toLowerCase()));
            else
                throw new RuntimeException(FormatUtil.format("{0} has no command!", this.name()));
        }

        public String getDisplayName() {
            return this.displayName;
        }
/*
        public static InventoryType getInventoryType(Container container) {
            if (container instanceof ContainerPlayer)
                return PLAYER;
            else {
                Reflection refContainer = new Reflection(container.getClass());
                IInventory inventory = refContainer.getValue(IInventory.class, container);
                ITextComponent displayName = inventory.getDisplayName();

                for (InventoryType inventoryType : InventoryType.values()) {
                    if (inventoryType.displayName.equalsIgnoreCase(displayName.getUnformattedText()))
                        return inventoryType;
                }

                for (InventoryType inventoryType : InventoryType.values()) {
                    if (inventoryType.displayName.startsWith(displayName.getUnformattedText()) || inventoryType.displayName.endsWith(displayName.getUnformattedText()))
                        return inventoryType;
                }

                if (displayName.getUnformattedText().endsWith("Backpack"))
                    return BACKPACK;

                if (net.netcoding.skyblockrecords.api.hypixel.skyblock.NPC.isNPC(displayName.getUnformattedText())
                        || net.netcoding.skyblockrecords.api.hypixel.skyblock.NPC.isNPC(Cache.lastInteractEntity))
                    return NPC;
            }

            return OTHER;
        }
*/
        public boolean hasCommand() {
            return this.hasCommand;
        }

        public boolean isSkyblockSubMenu() {
            return this.skyblockSubMenu;
        }

    }

    static {
        SLAYER_EXP_SCALE.put(Slayer.REVENANT_HORROR, Concurrent.newList(        5, 10, 185, 800, 4000, 15000, 80000, 300000, 600000));
        SLAYER_EXP_SCALE.put(Slayer.TARANTULA_BROODFATHER, Concurrent.newList(  5, 10, 185, 800, 4000, 15000, 80000, 300000, 600000));
        SLAYER_EXP_SCALE.put(Slayer.SVEN_PACKMASTER, Concurrent.newList(        5, 10, 185, 1300, 3500, 15000, 80000, 300000, 600000));

        PET_EXP_SCALE_OFFSET.put(Item.Rarity.COMMON,    0);
        PET_EXP_SCALE_OFFSET.put(Item.Rarity.UNCOMMON,  6);
        PET_EXP_SCALE_OFFSET.put(Item.Rarity.RARE,      11);
        PET_EXP_SCALE_OFFSET.put(Item.Rarity.EPIC,      16);
        PET_EXP_SCALE_OFFSET.put(Item.Rarity.LEGENDARY, 20);

        PET_SCORE_BREAKPOINTS = Concurrent.newList(10, 25, 50, 75, 100, 130, 175);

        PET_EXP_SCALE = Concurrent.newList(
                100, 110, 120, 130, 145, 160, 175, 190, 210, 230, 250, 275, 300, 330, 360,
                400, 440, 490, 540, 600, 660, 730, 800, 880, 960, 1050, 1150, 1260, 1380, 1510, 1650,
                1800, 1960, 2130, 2310, 2500, 2700, 2920, 3160, 3420, 3700, 4000, 4350, 4750, 5200,
                5700, 6300, 7000, 7800, 8700, 9700, 10800, 12000, 13300, 14700, 16200, 17800, 19500,
                21300, 23200, 25200, 27400, 29800, 32400, 35200, 38200, 41400, 44800, 48400, 52200,
                56200, 60400, 64800, 69400, 74200, 79200, 84700, 90700, 97200, 104200, 111700, 119700,
                128200, 137200, 146700, 156700, 167700, 179700, 192700, 206700, 221700, 237700, 254700,
                272700, 291700, 311700, 333700, 357700, 383700, 411700, 441700, 476700, 516700, 561700,
                611700, 666700, 726700, 791700, 861700, 936700, 1016700, 1101700, 1191700, 1286700,
                1386700, 1496700, 1616700, 1746700, 1886700);

        for (Map.Entry<Item.Rarity, Integer> entry : PET_EXP_SCALE_OFFSET)
            PET_EXP_TIER_SCALE.put(entry.getKey(), Skyblock.PET_EXP_SCALE.subList(entry.getValue(), entry.getValue() + 99)); // TODO: Add 0 to PET_EXP_SCALE, Add 1 to 99

        RUNECRAFTING_EXP_SCALE = Concurrent.newList(
                50, 100, 125, 160, 200, 250, 315, 400, 500, 625, 785, 1000, 1250,
                1600, 2000, 2465, 3125, 4000, 5000, 6200, 7800, 9800, 12200, 15300);

        UNIQUE_CRAFTS.put(5, 0);
        UNIQUE_CRAFTS.put(6, 5);
        UNIQUE_CRAFTS.put(7, 15);
        UNIQUE_CRAFTS.put(8, 30);
        UNIQUE_CRAFTS.put(9, 50);
        UNIQUE_CRAFTS.put(10, 75);
        UNIQUE_CRAFTS.put(11, 100);
        UNIQUE_CRAFTS.put(12, 125);
        UNIQUE_CRAFTS.put(13, 150);
        UNIQUE_CRAFTS.put(14, 175);
        UNIQUE_CRAFTS.put(15, 200);
        UNIQUE_CRAFTS.put(16, 225);
        UNIQUE_CRAFTS.put(17, 250);
        UNIQUE_CRAFTS.put(18, 275);
        UNIQUE_CRAFTS.put(19, 300);
        UNIQUE_CRAFTS.put(20, 350);
        UNIQUE_CRAFTS.put(21, 400);
        UNIQUE_CRAFTS.put(22, 450);
        UNIQUE_CRAFTS.put(23, 500);
        UNIQUE_CRAFTS.put(24, 550);
        UNIQUE_CRAFTS.put(25, 600);
        UNIQUE_CRAFTS.put(26, 350);
    }

}
