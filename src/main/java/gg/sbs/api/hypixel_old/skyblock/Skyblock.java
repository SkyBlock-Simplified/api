package gg.sbs.api.hypixel_old.skyblock;

import gg.sbs.api.apiclients.mojang.MojangChatFormatting;
import gg.sbs.api.util.*;
import gg.sbs.api.hypixel_old.SkyblockIsland;
import gg.sbs.api.nbt_old.NbtCompound;
import gg.sbs.api.reflection.Reflection;
import gg.sbs.api.util.concurrent.Concurrent;
import gg.sbs.api.util.concurrent.ConcurrentList;
import gg.sbs.api.util.concurrent.ConcurrentMap;
import gg.sbs.api.util.concurrent.ConcurrentSet;
import gg.sbs.api.util.concurrent.linked.ConcurrentLinkedMap;

import java.text.DecimalFormat;
import java.util.*;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

public class Skyblock {

    private static final ConcurrentSet<String> skyblockInAllLanguages = Concurrent.newSet("SKYBLOCK", "\u7A7A\u5C9B\u751F\u5B58");
    private static final DecimalFormat smallDecimalFormat = new DecimalFormat("#0.#");
    public final static ConcurrentLinkedMap<Integer, Integer> UNIQUE_CRAFTS = Concurrent.newLinkedMap();
    public final static ConcurrentList<Integer> SKILL_EXP_SCALE;
    public final static ConcurrentList<Integer> RUNECRAFTING_EXP_SCALE;
    public final static ConcurrentList<Integer> PET_EXP_SCALE;
    public final static ConcurrentList<Integer> PET_SCORE_BREAKPOINTS;
    public final static ConcurrentMap<Item.Rarity, ConcurrentList<Integer>> PET_EXP_TIER_SCALE = Concurrent.newMap();
    public final static ConcurrentMap<Item.Rarity, Integer> PET_EXP_SCALE_OFFSET = Concurrent.newMap();
    public final static ConcurrentMap<Slayer, ConcurrentList<Integer>> SLAYER_EXP_SCALE = Concurrent.newMap();
    public final static long START_DATE = 1560275700;
    private static Skyblock.Date date;
    private static boolean isOnSkyblock;
    private static String serverID;
    private static Location location;
    private static Location previousLocation = null;
    private static String previousServer = "";

    public static Skyblock.Date getDate() {
        return date;
    }

    public static Location getLocation() {
        return location;
    }

    public static String getServerID() {
        return serverID;
    }

    public static boolean isOnSkyblock() {
        return isOnSkyblock;
    }

    private static void setDefaults() {
        date = new Skyblock.Date(Skyblock.Date.Season.EARLY_WINTER, 1, 1, 1);
        isOnSkyblock = false;
        serverID = "";
        location = Location.UNKNOWN;
    }

    /*private static void checkGameLocationDate() {
        if (MinecraftReflection.isWorldInstantiated()) {
            Scoreboard scoreboard = Minecraft.getMinecraft().world.getScoreboard();
            ScoreObjective sidebarObjective = Minecraft.getMinecraft().world.getScoreboard().getObjectiveInDisplaySlot(1);
            boolean sendEvent = false;

            if (sidebarObjective != null) {
                String objectiveName = RegexUtil.strip(sidebarObjective.getDisplayName(), RegexUtil.VANILLA_PATTERN);
                boolean isSkyblock = false;

                for (String skyblock : skyblockInAllLanguages) {
                    if (objectiveName.startsWith(skyblock)) {
                        isSkyblock = true;
                        break;
                    }
                }

                sendEvent = isSkyblock != isOnSkyblock;
                isOnSkyblock = isSkyblock;

                if (isOnSkyblock) {
                    java.util.Collection<Score> collection = scoreboard.getSortedScores(sidebarObjective);
                    List<Score> list = collection.stream().filter(score -> score.getPlayerName() != null && !score.getPlayerName().startsWith("#")).collect(Collectors.toList());
                    collection = list.size() > 15 ? Lists.newArrayList(Iterables.skip(list, collection.size() - 15)) : list;
                    Location foundLocation = Location.UNKNOWN;

                    String timeString = null;
                    for (Score score1 : collection) {
                        ScorePlayerTeam scorePlayerTeam = scoreboard.getPlayersTeam(score1.getPlayerName());
                        String locationString = RegexUtil.strip(RegexUtil.strip(ScorePlayerTeam.formatPlayerName(scorePlayerTeam, score1.getPlayerName()), RegexUtil.VANILLA_PATTERN), RegexUtil.LETTERS_NUMBERS);

                        for (Location loopLocation : Location.values()) {
                            if (loopLocation == Location.UNKNOWN)
                                continue;

                            if (locationString.contains(loopLocation.getScoreboardName())) {
                                foundLocation = loopLocation;
                                break;
                            }
                        }

                        if (locationString.endsWith("am") || locationString.endsWith("pm")) {
                            timeString = locationString.trim();
                            timeString = timeString.substring(0, timeString.length() - 2);
                        }

                        for (Skyblock.Date.Season season : Skyblock.Date.Season.values()) {
                            if (locationString.contains(season.getScoreboardString())) {
                                try {
                                    date.setSeason(season);
                                    String numberPart = locationString.substring(locationString.lastIndexOf(" ") + 1);
                                    int day = Integer.parseInt(RegexUtil.strip(numberPart, RegexUtil.NUMBERS_SLASHES));
                                    date.setDay(day);

                                    if (timeString != null) {
                                        String[] timeSplit = timeString.split(Pattern.quote(":"));
                                        int hour = Integer.parseInt(timeSplit[0]);
                                        date.setHour(hour);
                                        int minute = Integer.parseInt(timeSplit[1]);
                                        date.setMinute(minute);
                                    }
                                } catch (IndexOutOfBoundsException | NumberFormatException ignored) { }
                                break;
                            }
                        }

                        Matcher matcher = RegexUtil.SERVER_REGEX.matcher(locationString);
                        if (matcher.matches()) {
                            String match = matcher.group(2);
                            previousServer = getServerID();
                            sendEvent = sendEvent || !match.equalsIgnoreCase(previousServer);
                            serverID = match;
                        }
                    }

                    if (foundLocation != Location.NONE) {
                        previousLocation = getLocation();
                        sendEvent = sendEvent || previousLocation != foundLocation;
                        location = foundLocation;
                    }
                }
            }

            if (!isOnSkyblock)
                setDefaults();

            if (sendEvent)
                MinecraftForge.EVENT_BUS.post(new LocationChangeEvent(isOnSkyblock(), previousLocation, getLocation(), previousServer, serverID));
        }
    }*/

    public enum Skill {

        FARMING,
        MINING,
        COMBAT,
        FORAGING,
        FISHING,
        ENCHANTING(false),
        ALCHEMY(false),
        CARPENTRY(false, true),
        RUNECRAFTING(false, true),
        TAMING(false);

        private final boolean hasCollection;
        private final boolean cosmetic;

        Skill() {
            this(true);
        }

        Skill(boolean hasCollection) {
            this(hasCollection, false);
        }

        Skill(boolean hasCollection, boolean cosmetic) {
            this.hasCollection = hasCollection;
            this.cosmetic = cosmetic;
        }

        public boolean hasCollection() {
            return this.hasCollection;
        }

        public boolean isCosmetic() {
            return this.cosmetic;
        }

        public static Skill getType(String name) {
            for (Skill type : values()) {
                if (type.name().equalsIgnoreCase(name))
                    return type;
            }

            return null;
        }

    }

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

        ENDER_CHEST(Collection.OBSIDIAN,
                new Size(0, 27),
                new Size(5, 36),
                new Size(7, 45),
                new Size(9, 54)),
        FISHING_BAG(Collection.RAW_FISH,
                new Size(3, 9),
                new Size(7, 18),
                new Size(9, 27),
                new Size(10, 36),
                new Size(11, 45)),
        POTION_BAG(Collection.NETHER_WART,
                new Size(2, 9),
                new Size(5, 18),
                new Size(8, 27),
                new Size(10, 36),
                new Size(11, 45)),
        QUIVER(Collection.STRING,
                new Size(3, 27),
                new Size(6, 36),
                new Size(9, 45)),
        TALISMAN_BAG(Collection.REDSTONE,
                new Size(2, 3),
                new Size(6, 9),
                new Size(9, 15),
                new Size(10, 21),
                new Size(11, 27),
                new Size(12, 33),
                new Size(13, 39),
                new Size(14, 45)),
        ;

        private final Collection collection;
        private final ConcurrentList<Size> sizes;

        Bag(Collection collection, Size... sizes) {
            this.collection = collection;
            this.sizes = Concurrent.newList(sizes);
        }

        public Collection getCollection() {
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

    public static class Date {

        private Season season;
        private int day;
        private int hour;
        private int minute;

        public Date(Season season, int day, int hour, int minute) {
            this.season = season;
            this.day = day;
            this.hour = hour;
            this.minute = minute;
        }

        public Season getSeason() {
            return season;
        }

        public int getDay() {
            return day;
        }

        public int getHour() {
            return hour;
        }

        public int getMinute() {
            return minute;
        }

        public long getTime() {
            return 0L; // TODO: SkyBlockTime
        }

        protected void setDay(int day) {
            this.day = day;
        }

        protected void setHour(int hour) {
            this.hour = hour;
        }

        protected void setMinute(int minute) {
            this.minute = minute;
        }

        protected void setSeason(Season season) {
            this.season = season;
        }

        public enum Season {

            EARLY_WINTER("Early Winter"),
            WINTER("Winter"),
            LATE_WINTER("Late Winter"),
            EARLY_SPRING("Early Spring"),
            SPRING("Spring"),
            LATE_SPRING("Late Spring"),
            EARLY_SUMMER("Early Summer"),
            SUMMER("Summer"),
            LATE_SUMMER("Late Summer"),
            EARLY_FALL("Early Fall"),
            FALL("Fall"),
            LATE_FALL("Late Fall");

            private String scoreboardString;

            Season(String scoreboardString) {
                this.scoreboardString = scoreboardString;
            }

            public String getScoreboardString() {
                return scoreboardString;
            }

        }

    }

    public enum Item {

        // Redstone Collection
        REDSTONE_DUST("REDSTONE", Items.REDSTONE),
        REDSTONE_BLOCK("REDSTONE_BLOCK", Blocks.REDSTONE_BLOCK, REDSTONE_DUST, CraftingTable.Recipe.ALL, new Recipe.Entry(REDSTONE_DUST, 1, 9)),
        ENCHANTED_REDSTONE_DUST("ENCHANTED_REDSTONE", Items.REDSTONE, REDSTONE_DUST, CraftingTable.Recipe.STAR, new Recipe.Group(REDSTONE_DUST, 32, 5), new Recipe.Group(REDSTONE_BLOCK, 32, 5)),
        ENCHANTED_REDSTONE_BLOCK("ENCHANTED_REDSTONE_BLOCK", Blocks.REDSTONE_BLOCK, ENCHANTED_REDSTONE_DUST, CraftingTable.Recipe.STAR, new Recipe.Entry(ENCHANTED_REDSTONE_DUST, 32, 5)),

        // Oak Wood Collection
        OAK_WOOD("LOG", Blocks.LOG),
        OAK_WOOD_PLANK("WOOD", Blocks.PLANKS, OAK_WOOD, CraftingTable.Recipe.SINGLE, new Recipe.Entry(OAK_WOOD, 1)),
        CHEST("CHEST", Blocks.CHEST, OAK_WOOD_PLANK, CraftingTable.Recipe.RING, new Recipe.Entry(OAK_WOOD_PLANK, 1, 8)),
        ENCHANTED_OAK_WOOD("ENCHANTED_OAK_LOG", Blocks.LOG, OAK_WOOD, CraftingTable.Recipe.STAR, new Recipe.Entry(OAK_WOOD, 32, 5)),
        SMALL_STORAGE("SMALL_ENCHANTED_CHEST", Blocks.CHEST, OAK_WOOD, CraftingTable.Recipe.RING, new Recipe.Entry(OAK_WOOD, 8, 8)),
        MEDIUM_STORAGE("MEDIUM_ENCHANTED_CHEST", Blocks.CHEST, ENCHANTED_OAK_WOOD, CraftingTable.Recipe.ALL, new Recipe.Entry(ENCHANTED_OAK_WOOD, 1, 8), new Recipe.Entry(SMALL_STORAGE, 1)),
        LARGE_STORAGE("LARGE_ENCHANTED_CHEST", Blocks.CHEST, ENCHANTED_OAK_WOOD, CraftingTable.Recipe.ALL, new Recipe.Entry(ENCHANTED_OAK_WOOD, 32, 8), new Recipe.Entry(MEDIUM_STORAGE, 1)),

        // Spruce Wood Collection
        SPRUCE_WOOD("LOG:1", Blocks.LOG, 1),
        ENCHANTED_SPRUCE_WOOD("ENCHANTED_SPRUCE_LOG", Blocks.LOG, 1, SPRUCE_WOOD, CraftingTable.Recipe.STAR, new Recipe.Entry(SPRUCE_WOOD, 32, 5)),

        // Birch Wood Collection
        BIRCH_WOOD("LOG:2", Blocks.LOG, 2),
        ENCHANTED_BIRCH_WOOD("ENCHANTED_BIRCH_LOG", Blocks.LOG, 2, BIRCH_WOOD, CraftingTable.Recipe.STAR, new Recipe.Entry(BIRCH_WOOD, 32, 5)),

        // Dark Oak Wood Collection
        DARK_OAK_WOOD("LOG_2:1", Blocks.LOG2, 1),
        ENCHANTED_DARK_OAK_WOOD("ENCHANTED_DARK_OAK_LOG", Blocks.LOG2, 1, DARK_OAK_WOOD, CraftingTable.Recipe.STAR, new Recipe.Entry(DARK_OAK_WOOD, 32, 5)),

        // Acacia Wood Collection
        ACACIA_WOOD("LOG_2", Blocks.LOG2),
        ENCHANTED_ACACIA_WOOD("ENCHANTED_ACACIA_LOG", Blocks.LOG2, ACACIA_WOOD, CraftingTable.Recipe.STAR, new Recipe.Entry(ACACIA_WOOD, 32, 5)),

        // Jungle Wood Collection
        JUNGLE_WOOD("LOG:3", Blocks.LOG, 3),
        ENCHANTED_JUNGLE_WOOD("ENCHANTED_JUNGLE_LOG", Blocks.LOG, 3, JUNGLE_WOOD, CraftingTable.Recipe.STAR, new Recipe.Entry(JUNGLE_WOOD, 32, 5)),

        // Cobblestone Collection
        COBBLESTONE("COBBLESTONE", Blocks.COBBLESTONE),
        ENCHANTED_COBBLESTONE("ENCHANTED_COBBLESTONE", Blocks.COBBLESTONE, COBBLESTONE, CraftingTable.Recipe.STAR, new Recipe.Entry(COBBLESTONE, 32, 5)),
        COMPACTOR("COMPACTOR", Blocks.DROPPER, ENCHANTED_COBBLESTONE, CraftingTable.Recipe.RING, new Recipe.Entry(ENCHANTED_COBBLESTONE, 1, 7), new Recipe.Entry(ENCHANTED_REDSTONE_DUST, 1)),
        SUPER_COMPACTOR_3000("SUPER_COMPACTOR_3000", Blocks.DROPPER, ENCHANTED_COBBLESTONE, CraftingTable.Recipe.RING, new Recipe.Entry(ENCHANTED_COBBLESTONE, 64, 7), new Recipe.Entry(ENCHANTED_REDSTONE_BLOCK, 1)),
        PERSONAL_COMPACTOR_4000("PERSONAL_COMPACTOR_4000", Blocks.DROPPER, ENCHANTED_REDSTONE_BLOCK, CraftingTable.Recipe.RING, new Recipe.Entry(ENCHANTED_REDSTONE_BLOCK, 1, 7), new Recipe.Entry(SUPER_COMPACTOR_3000, 1)),
        PERSONAL_COMPACTOR_5000("PERSONAL_COMPACTOR_5000", Blocks.DROPPER, ENCHANTED_REDSTONE_BLOCK, CraftingTable.Recipe.RING, new Recipe.Entry(ENCHANTED_REDSTONE_BLOCK, 2, 7), new Recipe.Entry(PERSONAL_COMPACTOR_4000, 1)),
        PERSONAL_COMPACTOR_6000("PERSONAL_COMPACTOR_6000", Blocks.DROPPER, ENCHANTED_REDSTONE_BLOCK, CraftingTable.Recipe.RING, new Recipe.Entry(ENCHANTED_REDSTONE_BLOCK, 4, 7), new Recipe.Entry(PERSONAL_COMPACTOR_5000, 1)),

        // Coal Collection
        COAL("COAL", Items.COAL),
        COAL_BLOCK("COAL_BLOCK", Blocks.COAL_BLOCK, COAL, CraftingTable.Recipe.ALL, new Recipe.Entry(COAL, 1, 9)),
        ENCHANTED_COAL("ENCHANTED_COAL", Items.COAL, COAL, CraftingTable.Recipe.STAR, new Recipe.Group(COAL, 32, 5), new Recipe.Group(COAL_BLOCK, 32, 5)),
        ENCHANTED_COAL_BLOCK("ENCHANTED_COAL_BLOCK", Blocks.COAL_BLOCK, ENCHANTED_COAL, CraftingTable.Recipe.STAR, new Recipe.Entry(ENCHANTED_COAL, 32, 5)),

        // Iron Collection
        IRON_INGOT("IRON_INGOT", Items.IRON_INGOT),
        IRON_BLOCK("IRON_BLOCK", Blocks.IRON_BLOCK, IRON_INGOT, CraftingTable.Recipe.ALL, new Recipe.Entry(IRON_INGOT, 1, 9)),
        ENCHANTED_IRON("ENCHANTED_IRON", Items.IRON_INGOT, IRON_INGOT, CraftingTable.Recipe.STAR, new Recipe.Group(IRON_INGOT, 32, 5), new Recipe.Group(IRON_BLOCK, 32, 5)),
        ENCHANTED_IRON_BLOCK("ENCHANTED_IRON_BLOCK", Blocks.IRON_BLOCK, ENCHANTED_IRON, CraftingTable.Recipe.STAR, new Recipe.Entry(ENCHANTED_IRON, 32, 5)),
        HOPPER("HOPPER", Blocks.HOPPER, IRON_INGOT, CraftingTable.Recipe.HOPPER, new Recipe.Entry(IRON_INGOT, 1, 5), new Recipe.Entry(CHEST, 1)),
        BUDGET_HOPPER("BUDGET_HOPPER", Blocks.HOPPER, ENCHANTED_IRON, CraftingTable.Recipe.HOPPER, new Recipe.Entry(ENCHANTED_IRON, 1, 5), new Recipe.Entry(CHEST, 1)),
        ENCHANTED_HOPPER("ENCHANTED_HOPPER", Blocks.HOPPER, ENCHANTED_IRON_BLOCK, CraftingTable.Recipe.HOPPER, new Recipe.Entry(ENCHANTED_IRON_BLOCK, 1, 5), new Recipe.Entry(CHEST, 1)),

        // Gold Collection
        GOLD_INGOT("GOLD_INGOT", Items.GOLD_INGOT),
        GOLD_NUGGET("GOLD_NUGGET", Items.GOLD_NUGGET, GOLD_INGOT, CraftingTable.Recipe.SINGLE, new Recipe.Entry(GOLD_INGOT, 1)),
        GOLD_BLOCK("GOLD_BLOCK", Blocks.GOLD_BLOCK, GOLD_INGOT, CraftingTable.Recipe.ALL, new Recipe.Entry(GOLD_INGOT, 1, 9)),
        ENCHANTED_GOLD("ENCHANTED_GOLD", Items.GOLD_INGOT, GOLD_INGOT, CraftingTable.Recipe.STAR, new Recipe.Group(GOLD_INGOT, 32, 5), new Recipe.Group(GOLD_BLOCK, 32, 5)),
        ENCHANTED_GOLD_BLOCK("ENCHANTED_GOLD_BLOCK", Blocks.GOLD_BLOCK, ENCHANTED_GOLD, CraftingTable.Recipe.STAR, new Recipe.Entry(ENCHANTED_GOLD, 32, 5)),

        // Diamond Collection
        DIAMOND("DIAMOND", Items.DIAMOND),
        DIAMOND_BLOCK("DIAMOND_BLOCK", Blocks.DIAMOND_BLOCK, DIAMOND, CraftingTable.Recipe.ALL, new Recipe.Entry(DIAMOND, 1, 9)),
        ENCHANTED_DIAMOND("ENCHANTED_DIAMOND", Items.DIAMOND, DIAMOND, CraftingTable.Recipe.STAR, new Recipe.Group(DIAMOND, 32, 5), new Recipe.Group(DIAMOND_BLOCK, 32, 5)),
        ENCHANTED_DIAMOND_BLOCK("ENCHANTED_DIAMOND_BLOCK", Blocks.DIAMOND_BLOCK, ENCHANTED_DIAMOND, CraftingTable.Recipe.STAR, new Recipe.Entry(ENCHANTED_DIAMOND, 32, 5)),

        // Lapis Collection
        LAPIS_LAZULI("INK_SACK:4", Items.DYE, 4),
        LAPIS_BLOCK("LAPIS_BLOCK", Blocks.LAPIS_BLOCK, LAPIS_LAZULI, CraftingTable.Recipe.ALL, new Recipe.Entry(LAPIS_LAZULI, 1, 9)),
        ENCHANTED_LAPIS_LAZULI("ENCHANTED_LAPIS_LAZULI", Items.DYE, 4, LAPIS_LAZULI, CraftingTable.Recipe.STAR, new Recipe.Group(LAPIS_LAZULI, 32, 5), new Recipe.Group(LAPIS_BLOCK, 32, 5)),
        ENCHANTED_LAPIS_BLOCK("ENCHANTED_LAPIS_LAZULI_BLOCK", Blocks.LAPIS_BLOCK, ENCHANTED_LAPIS_LAZULI, CraftingTable.Recipe.STAR, new Recipe.Entry(ENCHANTED_LAPIS_LAZULI, 32, 5)),

        // Emerald Collection
        EMERALD("COAL", Items.EMERALD),
        EMERALD_BLOCK("EMERALD_BLOCK", Blocks.EMERALD_BLOCK, EMERALD, CraftingTable.Recipe.ALL, new Recipe.Entry(EMERALD, 1, 9)),
        ENCHANTED_EMERALD("ENCHANTED_EMERALD", Items.EMERALD, EMERALD, CraftingTable.Recipe.STAR, new Recipe.Group(EMERALD, 32, 5), new Recipe.Group(EMERALD_BLOCK, 32, 5)),
        ENCHANTED_EMERALD_BLOCK("ENCHANTED_EMERALD_BLOCK", Blocks.EMERALD_BLOCK, ENCHANTED_EMERALD, CraftingTable.Recipe.STAR, new Recipe.Entry(ENCHANTED_EMERALD, 32, 5)),

        // Quartz Collection
        NETHER_QUARTZ("QUARTZ", Items.QUARTZ),
        QUARTZ_BLOCK("QUARTZ_BLOCK", Blocks.QUARTZ_BLOCK, NETHER_QUARTZ, CraftingTable.Recipe.ALL, new Recipe.Entry(NETHER_QUARTZ, 1, 9)),
        ENCHANTED_QUARTZ("ENCHANTED_QUARTZ", Items.QUARTZ, NETHER_QUARTZ, CraftingTable.Recipe.STAR, new Recipe.Group(NETHER_QUARTZ, 32, 5), new Recipe.Group(QUARTZ_BLOCK, 32, 5)),
        ENCHANTED_QUARTZ_BLOCK("ENCHANTED_QUARTZ_BLOCK", Blocks.QUARTZ_BLOCK, ENCHANTED_QUARTZ, CraftingTable.Recipe.STAR, new Recipe.Entry(ENCHANTED_QUARTZ, 32, 5)),
        MINION_EXPANDER("MINION_EXPANDER", Blocks.COMMAND_BLOCK, ENCHANTED_QUARTZ, CraftingTable.Recipe.ALL, new Recipe.Entry(ENCHANTED_QUARTZ, 2, 8), new Recipe.Entry(ENCHANTED_REDSTONE_DUST, 2)),

        // Obsidian Collection
        OBSIDIAN("OBSIDIAN", Blocks.OBSIDIAN),
        ENCHANTED_OBSIDIAN("ENCHANTED_OBSIDIAN", Blocks.OBSIDIAN, OBSIDIAN, CraftingTable.Recipe.STAR, new Recipe.Entry(OBSIDIAN, 32, 5)),

        // Glowstone Collection
        GLOWSTONE_DUST("QUARTZ", Items.GLOWSTONE_DUST),
        GLOWSTONE_BLOCK("GLOWSTONE_BLOCK", Blocks.GLOWSTONE, GLOWSTONE_DUST, CraftingTable.Recipe.BOX, new Recipe.Entry(GLOWSTONE_DUST, 1, 4)),
        ENCHANTED_GLOWSTONE_DUST("ENCHANTED_GLOWSTONE_DUST", Items.GLOWSTONE_DUST, GLOWSTONE_DUST, CraftingTable.Recipe.STAR, new Recipe.Group(GLOWSTONE_DUST, 32, 5), new Recipe.Group(GLOWSTONE_BLOCK, 32, 5)),
        ENCHANTED_GLOWSTONE_BLOCK("ENCHANTED_GLOWSTONE_BLOCK", Blocks.GLOWSTONE, ENCHANTED_GLOWSTONE_DUST, CraftingTable.Recipe.STAR, new Recipe.Entry(ENCHANTED_GLOWSTONE_DUST, 32, 5)),

        // Gravel Collection
        GRAVEL("GRAVEL", Blocks.GRAVEL),
        FLINT("FLINT", Items.FLINT),
        ENCHANTED_FLINT("ENCHANTED_FLINT", Items.FLINT, FLINT, CraftingTable.Recipe.STAR, new Recipe.Entry(FLINT, 32, 5)),

        // Ice Collection
        ICE("ICE", Blocks.ICE),
        PACKED_ICE("PACKED_ICE", Blocks.PACKED_ICE),
        ENCHANTED_ICE("ENCHANTED_ICE", Blocks.ICE, ICE, CraftingTable.Recipe.STAR, new Recipe.Group(PACKED_ICE, 32, 5), new Recipe.Group(ICE, 32, 5)),
        ENCHANTED_PACKED_ICE("ENCHANTED_PACKED_ICE", Blocks.PACKED_ICE, ENCHANTED_ICE, CraftingTable.Recipe.STAR, new Recipe.Entry(ENCHANTED_ICE, 32, 5)),

        // Netherrack Collection
        NETHERRACK("NETHERRACK", Blocks.NETHERRACK),

        // Sand Collection
        SAND("SAND", Blocks.SAND),
        ENCHANTED_SAND("ENCHANTED_SAND", Blocks.SAND, SAND, CraftingTable.Recipe.STAR, new Recipe.Entry(SAND, 32, 5)),

        // End Stone Collection
        END_STONE("ENDER_STONE", Blocks.END_STONE),
        ENCHANTED_END_STONE("ENCHANTED_ENDSTONE", Blocks.END_STONE, END_STONE, CraftingTable.Recipe.STAR, new Recipe.Entry(END_STONE, 32, 5)),

        // Snow Collection
        SNOWBALL("SNOW_BALL", Items.SNOWBALL),
        SNOW_BLOCK("SNOW_BLOCK", Blocks.SNOW, SNOWBALL, CraftingTable.Recipe.BOX, new Recipe.Entry(SNOWBALL, 1, 4)),
        ENCHANTED_SNOW_BLOCK("ENCHANTED_SNOW_BLOCK", Blocks.SNOW, SNOW_BLOCK, CraftingTable.Recipe.STAR, new Recipe.Entry(SNOW_BLOCK, 32, 5)),

        // Wheat Collection
        WHEAT("WHEAT", Items.WHEAT),
        SEEDS("SEEDS", Items.WHEAT_SEEDS),
        HAY_BALE("HAY_BLOCK", Blocks.HAY_BLOCK, WHEAT, CraftingTable.Recipe.ALL, new Recipe.Entry(WHEAT, 1, 9)),
        ENCHANTED_HAY_BALE("ENCHANTED_HAY_BLOCK", Blocks.HAY_BLOCK, HAY_BALE, CraftingTable.Recipe.ALL, new Recipe.Entry(HAY_BALE, 16, 9)),
        BREAD("BREAD", Items.BREAD),
        ENCHANTED_BREAD("ENCHANTED_BREAD", Items.BREAD, BREAD, CraftingTable.Recipe.DOUBLE_ROW, new Recipe.Entry(WHEAT, 10, 6)),

        // Carrot Collection
        CARROT("CARROT_ITEM", Items.CARROT),
        GOLDEN_CARROT("GOLDEN_CARROT", Items.GOLDEN_CARROT, CARROT, CraftingTable.Recipe.ALL, new Recipe.Entry(GOLD_NUGGET, 1, 8), new Recipe.Entry(CARROT, 1)),
        ENCHANTED_CARROT("ENCHANTED_CARROT", Items.CARROT, CARROT, CraftingTable.Recipe.STAR, new Recipe.Entry(CARROT, 32, 5)),
        ENCHANTED_GOLDEN_CARROT("ENCHANTED_GOLDEN_CARROT", Items.GOLDEN_CARROT, ENCHANTED_CARROT, CraftingTable.Recipe.STAR, new Recipe.Entry(ENCHANTED_CARROT, 32, 4), new Recipe.Entry(GOLDEN_CARROT, 32)),

        // Potato Collection
        POTATO("POTATO_ITEM", Items.POTATO),
        ENCHANTED_POTATO("ENCHANTED_POTATO", Items.POTATO, POTATO, CraftingTable.Recipe.STAR, new Recipe.Entry(POTATO, 32, 5)),
        ENCHANTED_BAKED_POTATO("ENCHANTED_BAKED_POTATO", Items.BAKED_POTATO, ENCHANTED_POTATO, CraftingTable.Recipe.STAR, new Recipe.Entry(ENCHANTED_POTATO, 32, 5)),

        // Pumpkin Collection
        PUMPKIN("PUMPKIN", Blocks.PUMPKIN),
        ENCHANTED_PUMPKIN("ENCHANTED_PUMPKIN", Blocks.PUMPKIN, PUMPKIN, CraftingTable.Recipe.STAR, new Recipe.Entry(PUMPKIN, 32, 5)),

        // Melon Collection
        MELON("MELON", Items.MELON),
        GLISTERING_MELON("SPECKLED_MELON", Items.SPECKLED_MELON, MELON, CraftingTable.Recipe.ALL, new Recipe.Entry(GOLD_NUGGET, 1, 8), new Recipe.Entry(MELON, 1)),
        MELON_BLOCK("MELON_BLOCK", Blocks.MELON_BLOCK, MELON, CraftingTable.Recipe.ALL, new Recipe.Entry(MELON, 1, 9)),
        ENCHANTED_MELON("ENCHANTED_MELON", Items.MELON, MELON, CraftingTable.Recipe.STAR, new Recipe.Group(MELON, 32, 5), new Recipe.Group(MELON_BLOCK, 32, 5)),
        ENCHANTED_MELON_BLOCK("ENCHANTED_MELON_BLOCK", Blocks.MELON_BLOCK, ENCHANTED_MELON, CraftingTable.Recipe.STAR, new Recipe.Entry(ENCHANTED_MELON, 32, 5)),

        // Mushroom Collection
        BROWN_MUSHROOM("BROWN_MUSHROOM", Blocks.BROWN_MUSHROOM),
        RED_MUSHROOM("RED_MUSHROOM", Blocks.RED_MUSHROOM),
        BROWN_MUSHROOM_BLOCK("HUGE_MUSHROOM_1", Blocks.BROWN_MUSHROOM_BLOCK, BROWN_MUSHROOM, CraftingTable.Recipe.ALL, new Recipe.Entry(BROWN_MUSHROOM, 1, 9)),
        RED_MUSHROOM_BLOCK("HUGE_MUSHROOM_2", Blocks.RED_MUSHROOM_BLOCK, RED_MUSHROOM, CraftingTable.Recipe.ALL, new Recipe.Entry(RED_MUSHROOM, 1, 9)),
        ENCHANTED_BROWN_MUSHROOM("ENCHANTED_BROWN_MUSHROOM", Blocks.BROWN_MUSHROOM, BROWN_MUSHROOM, CraftingTable.Recipe.STAR, new Recipe.Entry(BROWN_MUSHROOM, 32, 5)),
        ENCHANTED_RED_MUSHROOM("ENCHANTED_RED_MUSHROOM", Blocks.RED_MUSHROOM, RED_MUSHROOM, CraftingTable.Recipe.STAR, new Recipe.Entry(RED_MUSHROOM, 32, 5)),
        ENCHANTED_BROWN_MUSHROOM_BLOCK("ENCHANTED_HUGE_MUSHROOM_1", Blocks.BROWN_MUSHROOM_BLOCK, BROWN_MUSHROOM_BLOCK, CraftingTable.Recipe.ALL, new Recipe.Entry(BROWN_MUSHROOM_BLOCK, 64, 9)),
        ENCHANTED_RED_MUSHROOM_BLOCK("ENCHANTED_HUGE_MUSHROOM_2", Blocks.RED_MUSHROOM_BLOCK, RED_MUSHROOM_BLOCK, CraftingTable.Recipe.ALL, new Recipe.Entry(RED_MUSHROOM_BLOCK, 64, 9)),

        // Cocoa Beans Collection
        COCOA_BEANS("INK_SACK:3", Items.DYE, 3),
        ENCHANTED_COCOA_BEANS("ENCHANTED_COCOA", Items.DYE, 3, COCOA_BEANS, CraftingTable.Recipe.STAR, new Recipe.Entry(COCOA_BEANS, 32, 5)),
        ENCHANTED_COOKIE("ENCHANTED_COOKIE", Items.COOKIE, ENCHANTED_COCOA_BEANS, CraftingTable.Recipe.STAR, new Recipe.Entry(ENCHANTED_COCOA_BEANS, 32, 4), new Recipe.Entry(WHEAT, 32)),

        // Cactus Collection
        CACTUS("CACTUS", Blocks.CACTUS),
        CACTUS_GREEN("INK_SACK:2", Items.DYE, 2),
        ENCHANTED_CACTUS_GREEN("ENCHANTED_CACTUS_GREEN", Items.DYE, 2, CACTUS_GREEN, CraftingTable.Recipe.STAR, new Recipe.Entry(CACTUS_GREEN, 32, 5)),
        ENCHANTED_CACTUS("ENCHANTED_CACTUS", Blocks.CACTUS, ENCHANTED_CACTUS_GREEN, CraftingTable.Recipe.STAR, new Recipe.Entry(ENCHANTED_CACTUS_GREEN, 32, 5)),

        // Sugar Cane Collection
        SUGAR_CANE("SUGAR_CANE", Items.REEDS),
        SUGAR("SUGAR", Items.SUGAR, SUGAR_CANE, CraftingTable.Recipe.SINGLE, new Recipe.Entry(SUGAR_CANE, 1)),
        PAPER("PAPER", Items.PAPER, SUGAR_CANE, CraftingTable.Recipe.TOP_ROW, new Recipe.Entry(SUGAR_CANE, 1, 3)),
        ENCHANTED_SUGAR("ENCHANTED_SUGAR", Items.SUGAR, SUGAR_CANE, CraftingTable.Recipe.STAR, new Recipe.Group(SUGAR_CANE, 32, 5), new Recipe.Group(SUGAR, 32, 5)),
        ENCHANTED_PAPER("ENCHANTED_PAPER", Items.PAPER, SUGAR_CANE, CraftingTable.Recipe.DIAGONAL, new Recipe.Entry(SUGAR_CANE, 64, 3)),
        ENCHANTED_SUGAR_CANE("ENCHANTED_SUGAR_CANE", Items.REEDS, ENCHANTED_SUGAR, CraftingTable.Recipe.STAR, new Recipe.Entry(ENCHANTED_SUGAR, 32, 5)),

        // Rabbit Collection
        RABBIT("RABBIT", Items.RABBIT),
        RABBIT_FOOT("RABBIT_FOOT", Items.RABBIT_FOOT),
        ENCHANTED_RABBIT_FOOT("ENCHANTED_RABBIT_FOOT", Items.RABBIT_FOOT, RABBIT_FOOT, CraftingTable.Recipe.STAR, new Recipe.Entry(RABBIT_FOOT, 32, 5)),
        RABBIT_HIDE("RABBIT_HIDE", Items.RABBIT_HIDE),
        ENCHANTED_RABBIT_HIDE("ENCHANTED_RABBIT_HIDE", Items.RABBIT_HIDE, RABBIT_HIDE, CraftingTable.Recipe.ALL, new Recipe.Entry(RABBIT_HIDE, 64, 9)),

        // Feather Collection
        FEATHER("FEATHER", Items.FEATHER),
        ENCHANTED_FEATHER("ENCHANTED_FEATHER", Items.FEATHER, FEATHER, CraftingTable.Recipe.STAR, new Recipe.Entry(FEATHER, 32, 5)),

        // Leather Collection
        RAW_BEEF("RAW_BEEF", Items.BEEF),
        ENCHANTED_RAW_BEEF("ENCHANTED_RAW_BEEF", Items.BEEF, RAW_BEEF, CraftingTable.Recipe.STAR, new Recipe.Entry(RAW_BEEF, 32, 5)),
        LEATHER("LEATHER", Items.LEATHER, RABBIT_HIDE, CraftingTable.Recipe.BOX, new Recipe.Entry(RABBIT_HIDE, 1, 4)),
        ENCHANTED_LEATHER("ENCHANTED_LEATHER", Items.LEATHER, LEATHER, CraftingTable.Recipe.ALL, new Recipe.Entry(LEATHER, 64, 9)),

        // Porkchop Collection
        RAW_PORKCHOP("PORK", Items.PORKCHOP),
        ENCHANTED_RAW_PORKCHOP("ENCHANTED_PORK", Items.PORKCHOP, RAW_PORKCHOP, CraftingTable.Recipe.STAR, new Recipe.Entry(RAW_PORKCHOP, 32, 5)),
        ENCHANTED_GRILLED_PORK("ENCHANTED_GRILLED_PORK", Items.COOKED_PORKCHOP, ENCHANTED_RAW_PORKCHOP, CraftingTable.Recipe.STAR, new Recipe.Entry(ENCHANTED_RAW_PORKCHOP, 32, 5)),

        // Chicken Collection
        RAW_CHICKEN("RAW_CHICKEN", Items.CHICKEN),
        ENCHANTED_RAW_CHICKEN("ENCHANTED_RAW_CHICKEN", Items.CHICKEN, RAW_CHICKEN, CraftingTable.Recipe.STAR, new Recipe.Entry(RAW_CHICKEN, 32, 5)),
        EGG("EGG", Items.EGG),
        ENCHANTED_EGG("ENCHANTED_EGG", Items.EGG, EGG, CraftingTable.Recipe.ALL, new Recipe.Entry(EGG, 16, 9)),
        ENCHANTED_SUPER_EGG("SUPER_EGG", Items.SPAWN_EGG, ENCHANTED_EGG, CraftingTable.Recipe.ALL, new Recipe.Entry(ENCHANTED_EGG, 16, 9)),
        MILK_BUCKET("MILK_BUCKET", Items.MILK_BUCKET),
        ENCHANTED_CAKE("ENCHANTED_CAKE", Items.CAKE, ENCHANTED_EGG, CraftingTable.Recipe.ALL, new Recipe.Entry(MILK_BUCKET, 1, 3), new Recipe.Entry(ENCHANTED_SUGAR, 1, 2), new Recipe.Entry(WHEAT, 1, 3), new Recipe.Entry(ENCHANTED_EGG, 1)),

        // Mutton Collection
        MUTTON("MUTTON", Items.MUTTON),
        ENCHANTED_MUTTON("ENCHANTED_MUTTON", Items.MUTTON, MUTTON, CraftingTable.Recipe.STAR, new Recipe.Entry(MUTTON, 32, 5)),
        ENCHANTED_COOKED_MUTTON("ENCHANTED_COOKED_MUTTON", Items.COOKED_MUTTON, ENCHANTED_MUTTON, CraftingTable.Recipe.STAR, new Recipe.Entry(ENCHANTED_MUTTON, 32, 5)),
        WOOL("WOOL", Blocks.WOOL),
        ENCHANTED_WOOL("ENCHANTED_WOOL", Blocks.WOOL, WOOL, CraftingTable.Recipe.STAR, new Recipe.Entry(WOOL, 32, 5)),

        // Nether Wart Collection
        NETHER_WART("NETHER_STALK", Items.NETHER_WART),
        ENCHANTED_NETHER_WART("ENCHANTED_NETHER_STALK", Items.NETHER_WART, NETHER_WART, CraftingTable.Recipe.STAR, new Recipe.Entry(NETHER_WART, 32, 5)),

        // Rotten Flesh Collection
        ROTTEN_FLESH("ROTTEN_FLESH", Items.ROTTEN_FLESH),
        ENCHANTED_ROTTEN_FLESH("ENCHANTED_ROTTEN_FLESH", Items.ROTTEN_FLESH, ROTTEN_FLESH, CraftingTable.Recipe.STAR, new Recipe.Entry(ROTTEN_FLESH, 32, 5)),
        ZOMBIE_HEART("ZOMBIE_HEART", Items.SKULL, ENCHANTED_ROTTEN_FLESH, CraftingTable.Recipe.RING, new Recipe.Entry(ENCHANTED_ROTTEN_FLESH, 32, 8)),

        // Bone Collection
        BONE("BONE", Items.BONE),
        ENCHANTED_BONE("ENCHANTED_BONE", Items.BONE, BONE, CraftingTable.Recipe.STAR, new Recipe.Entry(BONE, 32, 5)),

        // String Collection
        STRING("STRING", Items.STRING),
        ENCHANTED_STRING("ENCHANTED_STRING", Items.STRING, STRING, CraftingTable.Recipe.SINGLE_ROW, new Recipe.Entry(STRING, 64, 3)),

        // Spider Eye Collection
        SPIDER_EYE("SPIDER_EYE", Items.SPIDER_EYE),
        ENCHANTED_SPIDER_EYE("ENCHANTED_SPIDER_EYE", Items.SPIDER_EYE, SPIDER_EYE, CraftingTable.Recipe.STAR, new Recipe.Entry(SPIDER_EYE, 32, 5)),
        ENCHANTED_FERMENTED_SPIDER_EYE("ENCHANTED_FERMENTED_SPIDER_EYE", Items.FERMENTED_SPIDER_EYE, ENCHANTED_SPIDER_EYE, CraftingTable.Recipe.CENTER3, new Recipe.Entry(BROWN_MUSHROOM, 64), new Recipe.Entry(SUGAR, 64), new Recipe.Entry(ENCHANTED_SPIDER_EYE, 64)),

        // Gunpowder Collection
        GUNPOWDER("SULPHUR", Items.GUNPOWDER),
        ENCHANTED_GUNPOWDER("ENCHANTED_GUNPOWDER", Items.GUNPOWDER, GUNPOWDER, CraftingTable.Recipe.STAR, new Recipe.Entry(GUNPOWDER, 32, 5)),
        ENCHANTED_FIREWORK_ROCKET("ENCHANTED_FIREWORK_ROCKET", Items.FIREWORKS, ENCHANTED_GUNPOWDER, CraftingTable.Recipe.STAR, new Recipe.Entry(ENCHANTED_GUNPOWDER, 16, 4), new Recipe.Entry(PAPER, 16)),

        // Blaze Rod Collection
        BLAZE_ROD("BLAZE_ROD", Items.BLAZE_ROD),
        BLAZE_POWDER("BLAZE_POWDER", Items.BLAZE_POWDER, BLAZE_ROD, CraftingTable.Recipe.SINGLE, new Recipe.Entry(BLAZE_ROD, 1)),
        ENCHANTED_BLAZE_POWDER("ENCHANTED_BLAZE_POWDER", Items.BLAZE_POWDER, BLAZE_ROD, CraftingTable.Recipe.STAR, new Recipe.Entry(BLAZE_ROD, 32, 5)),
        ENCHANTED_BLAZE_ROD("ENCHANTED_BLAZE_ROD", Items.BLAZE_POWDER, ENCHANTED_BLAZE_POWDER, CraftingTable.Recipe.STAR, new Recipe.Entry(ENCHANTED_BLAZE_POWDER, 32, 5)),

        // Ender Pearl Collection
        ENDER_PEARL("ENDER_PEARL", Items.ENDER_PEARL),
        ENCHANTED_ENDER_PEARL("ENCHANTED_ENDER_PEARL", Items.ENDER_PEARL, ENDER_PEARL, CraftingTable.Recipe.STAR, new Recipe.Entry(ENDER_PEARL, 4, 5)),
        ENCHANTED_EYE_OF_ENDER("ENCHANTED_EYE_OF_ENDER", Items.ENDER_EYE, ENCHANTED_ENDER_PEARL, CraftingTable.Recipe.STAR, new Recipe.Entry(BLAZE_POWDER, 16, 4), new Recipe.Entry(ENCHANTED_ENDER_PEARL, 16)),

        // Ghast Collection
        GHAST_TEAR("GHAST_TEAR", Items.GHAST_TEAR),
        ENCHANTED_GHAST_TEAR("ENCHANTED_GHAST_TEAR", Items.GHAST_TEAR, GHAST_TEAR, CraftingTable.Recipe.STAR, new Recipe.Entry(GHAST_TEAR, 1, 5)),
        SILVER_FANG("SILVER_FANG", Items.GHAST_TEAR, ENCHANTED_GHAST_TEAR, CraftingTable.Recipe.STAR, new Recipe.Entry(ENCHANTED_GHAST_TEAR, 5, 5)),

        // Slimeball Collection
        SLIME_BALL("SLIME_BALL", Items.SLIME_BALL),
        SLIME_BLOCK("SLIME_BLOCK", Blocks.SLIME_BLOCK, SLIME_BALL, CraftingTable.Recipe.ALL, new Recipe.Entry(SLIME_BALL, 1, 9)),
        ENCHANTED_SLIME_BALL("ENCHANTED_SLIME_BALL", Items.SLIME_BALL, SLIME_BALL, CraftingTable.Recipe.STAR, new Recipe.Entry(SLIME_BALL, 32, 5)),
        ENCHANTED_SLIME_BALL_ALT("ENCHANTED_SLIME_BALL", Items.SLIME_BALL, SLIME_BLOCK, CraftingTable.Recipe.STAR, new Recipe.Entry(SLIME_BLOCK, 32, 5)),
        ENCHANTED_SLIME_BLOCK("ENCHANTED_SLIME_BALL", Items.SLIME_BALL, ENCHANTED_SLIME_BALL, CraftingTable.Recipe.STAR, new Recipe.Entry(ENCHANTED_SLIME_BALL, 32, 5)),

        // Magma Cream Collection
        MAGMA_CREAM("MAGMA_CREAM", Items.MAGMA_CREAM, SLIME_BALL, CraftingTable.Recipe.MIDDLE2, new Recipe.Entry(BLAZE_POWDER, 1), new Recipe.Entry(SLIME_BALL, 1)),
        ENCHANTED_MAGMA_CREAM("ENCHANTED_MAGMA_CREAM", Items.MAGMA_CREAM, MAGMA_CREAM, CraftingTable.Recipe.STAR, new Recipe.Entry(MAGMA_CREAM, 32, 5)),

        // Raw Fish Collection
        RAW_FISH("RAW_FISH", Items.FISH),
        ENCHANTED_RAW_FISH("ENCHANTED_RAW_FISH", Items.FISH, RAW_FISH, CraftingTable.Recipe.STAR, new Recipe.Entry(RAW_FISH, 32, 5)),
        ENCHANTED_COOKED_FISH("ENCHANTED_COOKED_FISH", Items.COOKED_FISH, ENCHANTED_RAW_FISH, CraftingTable.Recipe.STAR, new Recipe.Entry(ENCHANTED_RAW_FISH, 32, 5)),

        // Raw Salmon Collection
        RAW_SALMON("RAW_FISH:1", Items.FISH, 1),
        ENCHANTED_RAW_SALMON("ENCHANTED_RAW_SALMON", Items.FISH, 1, RAW_SALMON, CraftingTable.Recipe.STAR, new Recipe.Entry(RAW_SALMON, 32, 5)),
        ENCHANTED_COOKED_SALMON("ENCHANTED_COOKED_SALMON", Items.FISH, 1, ENCHANTED_RAW_SALMON, CraftingTable.Recipe.STAR, new Recipe.Entry(ENCHANTED_RAW_SALMON, 32, 5)),

        // Clownfish Collection
        CLOWNFISH("RAW_FISH:2", Items.FISH, 2),
        ENCHANTED_CLOWNFISH("ENCHANTED_CLOWNFISH", Items.FISH, 2, CLOWNFISH, CraftingTable.Recipe.STAR, new Recipe.Entry(CLOWNFISH, 32, 5)),

        // Pufferfish Collection
        PUFFERFISH("RAW_FISH:3", Items.FISH, 3),
        ENCHANTED_PUFFERFISH("ENCHANTED_PUFFERFISH", Items.FISH, 3, PUFFERFISH, CraftingTable.Recipe.STAR, new Recipe.Entry(PUFFERFISH, 32, 5)),

        // Prismarine Shard Collection
        PRISMARINE_SHARD("PRISMARINE_SHARD", Items.PRISMARINE_SHARD),
        ENCHANTED_PRISMARINE_SHARD("ENCHANTED_PRISMARINE_SHARD", Items.PRISMARINE_SHARD, PRISMARINE_SHARD, CraftingTable.Recipe.STAR, new Recipe.Entry(PRISMARINE_SHARD, 32, 5)),

        // Prismarine Crystals Collection
        PRISMARINE_CRYSTALS("PRISMARINE_CRYSTALS", Items.PRISMARINE_CRYSTALS),
        ENCHANTED_PRISMARINE_CRYSTALS("ENCHANTED_PRISMARINE_CRYSTALS", Items.PRISMARINE_CRYSTALS, PRISMARINE_CRYSTALS, CraftingTable.Recipe.STAR, new Recipe.Entry(PRISMARINE_CRYSTALS, 32, 5)),

        // Clay Collection
        CLAY("CLAY_BALL", Items.CLAY_BALL),
        CLAY_BLOCK("CLAY", Blocks.CLAY, CLAY, CraftingTable.Recipe.BOX, new Recipe.Entry(CLAY, 1, 4)),
        ENCHANTED_CLAY("ENCHANTED_CLAY_BALL", Items.CLAY_BALL, CLAY, CraftingTable.Recipe.STAR, new Recipe.Entry(CLAY, 32, 5)),

        // Lily Pad Collection
        LILY_PAD("WATER_LILY", Blocks.WATERLILY),
        ENCHANTED_LILY_PAD("ENCHANTED_LILY_PAD", Blocks.WATERLILY, LILY_PAD, CraftingTable.Recipe.STAR, new Recipe.Entry(LILY_PAD, 32, 5)),

        // Ink Sack Collection
        INK_SACK("INK_SACK", Items.DYE),
        ENCHANTED_INK_SACK("ENCHANTED_INK_SACK", Items.DYE, INK_SACK, CraftingTable.Recipe.STAR, new Recipe.Entry(INK_SACK, 16, 5)),

        // Sponge Collection
        SPONGE("SPONGE", Blocks.SPONGE),
        ENCHANTED_SPONGE("ENCHANTED_SPONGE", Blocks.SPONGE, SPONGE, CraftingTable.Recipe.STAR, new Recipe.Entry(SPONGE, 8, 5)),
        ENCHANTED_WET_SPONGE("ENCHANTED_WET_SPONGE", Blocks.SPONGE, ENCHANTED_SPONGE, CraftingTable.Recipe.STAR, new Recipe.Entry(ENCHANTED_SPONGE, 8, 5)),

        // Slayer
        TARANTULA_WEB("TARANTULA_WEB", Blocks.WEB),
        TARANTULA_SILK("TARANTULA_SILK", Items.STRING, TARANTULA_WEB, CraftingTable.Recipe.STAR, new Recipe.Entry(TARANTULA_WEB, 32, 4), new Recipe.Entry(ENCHANTED_FLINT, 32)),
        REVENANT_FLESH("REVENANT_FLESH", Items.ROTTEN_FLESH),
        REVENANT_VISCERA("REVENANT_VISCERA", Items.ROTTEN_FLESH, REVENANT_FLESH, CraftingTable.Recipe.STAR, new Recipe.Entry(REVENANT_FLESH, 32, 4), new Recipe.Entry(ENCHANTED_STRING, 32)),
        WOLF_TOOTH("WOLF_TOOTH", Items.GHAST_TEAR),
        GOLDEN_TOOTH("GOLDEN_TOOTH", Items.GHAST_TEAR, WOLF_TOOTH, CraftingTable.Recipe.STAR, new Recipe.Entry(GHAST_TEAR, 32, 4), new Recipe.Entry(ENCHANTED_GOLD, 32)),

        // Miscellaneous
        ARROW("ARROW", Items.ARROW),
        BOOK("BOOK", Items.BOOK, PAPER, CraftingTable.Recipe.BOX, new Recipe.Entry(PAPER, 1, 3), new Recipe.Entry(LEATHER, 1)),
        ENCHANTED_BOOK("ENCHANTED_BOOK", Items.ENCHANTED_BOOK);

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
        private final net.minecraft.item.Item item;
        private final int meta;
        private final Item baseItem;
        private final Set<Recipe.Group> recipeGroups = new HashSet<>();
        private final CraftingTable.Recipe recipe;
        private final Set<Item> collection = new HashSet<>();

        Item(String id, Block block) {
            this(id, block, 0);
        }

        Item(String id, Block block, int meta) {
            this(id, net.minecraft.item.Item.getItemFromBlock(block), meta);
        }

        Item(String id, net.minecraft.item.Item item) {
            this(id, item, 0);
        }

        Item(String id, net.minecraft.item.Item item, int meta) {
            this(id, item, meta, null, CraftingTable.Recipe.NONE, new Recipe.Group());
        }

        Item(String id, Block block, Item baseItem, CraftingTable.Recipe recipe, Recipe.Entry... recipeEntries) {
            this(id, block, 0, baseItem, recipe, recipeEntries);
        }

        Item(String id, Block block, int meta, Item baseItem, CraftingTable.Recipe recipe, Recipe.Entry... recipeEntries) {
            this(id, net.minecraft.item.Item.getItemFromBlock(block), meta, baseItem, recipe, recipeEntries);
        }

        Item(String id, net.minecraft.item.Item item, Item baseItem, CraftingTable.Recipe recipe, Recipe.Entry... recipeEntries) {
            this(id, item, 0, baseItem, recipe, new Recipe.Group(recipeEntries));
        }

        Item(String id, net.minecraft.item.Item item, int meta, Item baseItem, CraftingTable.Recipe recipe, Recipe.Entry... recipeEntries) {
            this(id, item, meta, baseItem, recipe, new Recipe.Group(recipeEntries));
        }

        Item(String id, Block block, Item baseItem, CraftingTable.Recipe recipe, Recipe.Group... recipeGroups) {
            this(id, block, 0, baseItem, recipe, recipeGroups);
        }

        Item(String id, Block block, int meta, Item baseItem, CraftingTable.Recipe recipe, Recipe.Group... recipeGroups) {
            this(id, net.minecraft.item.Item.getItemFromBlock(block), meta, baseItem, recipe, recipeGroups);
        }

        Item(String id, net.minecraft.item.Item item, Item baseItem, CraftingTable.Recipe recipe, Recipe.Group... recipeGroups) {
            this(id, item, 0, baseItem, recipe, recipeGroups);
        }

        Item(String id, net.minecraft.item.Item item, int meta, Item baseItem, CraftingTable.Recipe recipe, Recipe.Group... recipeGroups) {
            this.id = id;
            this.item = item;
            this.meta = meta;
            this.baseItem = baseItem;
            this.recipe = recipe;
            this.recipeGroups.addAll(Arrays.asList(recipeGroups));
        }

        public net.minecraft.item.Item getItem() {
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

        public ItemStack getItemStack() {
            return new ItemStack(this.getItem(), 1, this.getMeta());
        }

        public int getMeta() {
            return this.meta;
        }

        public CraftingTable.Recipe getRecipe() {
            return this.recipe;
        }

        public Set<Recipe.Group> getRecipeGroups() {
            return this.recipeGroups;
        }

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

        public enum Rarity {

            COMMON(MojangChatFormatting.WHITE),
            UNCOMMON(MojangChatFormatting.GREEN),
            RARE(MojangChatFormatting.BLUE),
            EPIC(MojangChatFormatting.DARK_PURPLE),
            LEGENDARY(MojangChatFormatting.GOLD),
            MYTHIC(MojangChatFormatting.LIGHT_PURPLE),
            SPECIAL(MojangChatFormatting.RED),
            VERY_SPECIAL(MojangChatFormatting.RED),
            SUPREME(MojangChatFormatting.DARK_RED);

            private final MojangChatFormatting color;

            Rarity(MojangChatFormatting color) {
                this.color = color;
            }

            public MojangChatFormatting getColor() {
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

    public enum Collection {

        WHEAT(Skill.FARMING, Item.WHEAT),
        CARROT(Skill.FARMING, Item.CARROT),
        POTATO(Skill.FARMING, Item.POTATO),
        PUMPKIN(Skill.FARMING, Item.PUMPKIN),
        MELON(Skill.FARMING, Item.MELON),
        SEEDS(Skill.FARMING, Item.SEEDS, 6),
        MUSHROOM(Skill.FARMING, Item.RED_MUSHROOM),
        COCOA_BEANS(Skill.FARMING, Item.COCOA_BEANS),
        CACTUS(Skill.FARMING, Item.CACTUS),
        SUGAR_CANE(Skill.FARMING, Item.SUGAR_CANE),
        FEATHER(Skill.FARMING, Item.FEATHER),
        LEATHER(Skill.FARMING, Item.LEATHER, 10),
        RAW_PORKCHOP(Skill.FARMING, Item.RAW_PORKCHOP),
        RAW_CHICKEN(Skill.FARMING, Item.RAW_CHICKEN),
        MUTTON(Skill.FARMING, Item.MUTTON),
        RAW_RABBIT(Skill.FARMING, Item.RABBIT),
        NETHER_WART(Skill.FARMING, Item.NETHER_WART, 11),

        COBBLESTONE(Skill.MINING, Item.COBBLESTONE, 10),
        COAL(Skill.MINING, Item.COAL),
        IRON_INGOT(Skill.MINING, Item.IRON_INGOT),
        GOLD_INGOT(Skill.MINING, Item.GOLD_INGOT),
        DIAMOND(Skill.MINING, Item.DIAMOND),
        LAPIS_LAZULI(Skill.MINING, Item.LAPIS_LAZULI, 10),
        EMERALD(Skill.MINING, Item.EMERALD),
        REDSTONE(Skill.MINING, Item.REDSTONE_DUST, 14),
        NETHER_QUARTZ(Skill.MINING, Item.NETHER_QUARTZ),
        OBSIDIAN(Skill.MINING, Item.OBSIDIAN, 10),
        GLOWSTONE_DUST(Skill.MINING, Item.GLOWSTONE_DUST),
        GRAVEL(Skill.MINING, Item.GRAVEL),
        ICE(Skill.MINING, Item.ICE, 10),
        NETHERRACK(Skill.MINING, Item.NETHERRACK, 3),
        SAND(Skill.MINING, Item.SAND, 7),
        END_STONE(Skill.MINING, Item.END_STONE, 10),

        ROTTEN_FLESH(Skill.COMBAT, Item.ROTTEN_FLESH),
        BONE(Skill.COMBAT, Item.BONE),
        STRING(Skill.COMBAT, Item.STRING),
        SPIDER_EYE(Skill.COMBAT, Item.SPIDER_EYE),
        GUNPOWDER(Skill.COMBAT, Item.GUNPOWDER),
        ENDER_PEARL(Skill.COMBAT, Item.ENDER_PEARL),
        GHAST_TEAR(Skill.COMBAT, Item.GHAST_TEAR),
        SLIME_BALL(Skill.COMBAT, Item.SLIME_BALL),
        BLAZE_ROD(Skill.COMBAT, Item.BLAZE_ROD),
        MAGMA_CREAM(Skill.COMBAT, Item.MAGMA_CREAM),

        OAK_WOOD(Skill.FORAGING, Item.OAK_WOOD),
        SPRUCE_WOOD(Skill.FORAGING, Item.SPRUCE_WOOD),
        BIRCH_WOOD(Skill.FORAGING, Item.BIRCH_WOOD),
        DARK_OAK_WOOD(Skill.FORAGING, Item.DARK_OAK_WOOD),
        ACACIA_WOOD(Skill.FORAGING, Item.ACACIA_WOOD),
        JUNGLE_WOOD(Skill.FORAGING, Item.JUNGLE_WOOD),

        RAW_FISH(Skill.FISHING, Item.RAW_FISH, 11),
        RAW_SALMON(Skill.FISHING, Item.RAW_SALMON),
        CLOWNFISH(Skill.FISHING, Item.CLOWNFISH, 7),
        PUFFERFISH(Skill.FISHING, Item.PUFFERFISH),
        PRISMARINE_SHARD(Skill.FISHING, Item.PRISMARINE_SHARD, 5),
        PRISMARINE_CRYSTALS(Skill.FISHING, Item.PRISMARINE_CRYSTALS, 7),
        CLAY(Skill.FISHING, Item.CLAY, 5),
        LILY_PAD(Skill.FISHING, Item.LILY_PAD),
        INK_SACK(Skill.FISHING, Item.INK_SACK),
        SPONGE(Skill.FISHING, Item.SPONGE);

        private final Skill skill;
        private final Skyblock.Item skyblockItem;
        private final int maxTier;

        Collection(Skill skill, Skyblock.Item skyblockItem) {
            this(skill, skyblockItem, 9);
        }

        Collection(Skill skill, Skyblock.Item skyblockItem, int maxTier) {
            this.skill = skill;
            this.skyblockItem = skyblockItem;
            this.maxTier = maxTier;
        }

        public int getMaxTier() {
            return this.maxTier;
        }

        public String getName() {
            return (this == MUSHROOM ? "MUSHROOM_COLLECTION" : this.skyblockItem.getItemId());
        }

        public Skill getSkill() {
            return this.skill;
        }

        public Skyblock.Item getSkyblockItem() {
            return this.skyblockItem;
        }

        public static ConcurrentList<Collection> getItems(Skill skill) {
            ConcurrentList<Collection> items = Concurrent.newList();
            Arrays.stream(values()).filter(item -> item.getSkill() == skill).forEach(items::add);
            return items;
        }

    }

    public enum Minion {

        WHEAT(Collection.WHEAT),
        CARROT(Collection.CARROT),
        POTATO(Collection.POTATO),
        PUMPKIN(Collection.PUMPKIN),
        MELON(Collection.MELON),
        MUSHROOM(Collection.MUSHROOM),
        COCOA("Cocoa Beans", Collection.COCOA_BEANS),
        CACTUS(Collection.CACTUS),
        SUGAR_CANE(Collection.SUGAR_CANE),
        COW(Collection.LEATHER),
        PIG(Collection.RAW_PORKCHOP),
        CHICKEN(Collection.RAW_CHICKEN),
        SHEEP(Collection.MUTTON),
        RABBIT(Collection.RAW_RABBIT),
        NETHER_WARTS("Nether Wart", Collection.NETHER_WART),

        COBBLESTONE(Collection.COBBLESTONE),
        COAL(Collection.COAL),
        IRON(Collection.IRON_INGOT),
        GOLD(Collection.GOLD_INGOT),
        DIAMOND(Collection.DIAMOND),
        LAPIS(Collection.LAPIS_LAZULI),
        EMERALD(Collection.EMERALD),
        REDSTONE(Collection.REDSTONE),
        QUARTZ(Collection.NETHER_QUARTZ),
        OBSIDIAN(Collection.OBSIDIAN),
        GLOWSTONE(Collection.GLOWSTONE_DUST),
        GRAVEL(Collection.GRAVEL),
        ICE(Collection.ICE),
        SAND(Collection.SAND),
        ENDER_STONE("End Stone", Collection.END_STONE),

        ZOMBIE(Collection.ROTTEN_FLESH),
        SKELETON(Collection.BONE),
        SPIDER(Collection.STRING),
        CAVESPIDER("Cave Spider", Collection.SPIDER_EYE),
        CREEPER(Collection.GUNPOWDER),
        ENDERMAN(Collection.ENDER_PEARL),
        GHAST(Collection.GHAST_TEAR),
        SLIME(Collection.SLIME_BALL),
        BLAZE(Collection.BLAZE_ROD),
        MAGMA_CUBE(Collection.MAGMA_CREAM),

        OAK(Collection.OAK_WOOD),
        SPRUCE(Collection.SPRUCE_WOOD),
        BIRCH(Collection.BIRCH_WOOD),
        DARK_OAK(Collection.DARK_OAK_WOOD),
        ACACIA(Collection.ACACIA_WOOD),
        JUNGLE(Collection.JUNGLE_WOOD),

        FISHING(Collection.RAW_FISH, Collection.RAW_SALMON, Collection.CLOWNFISH, Collection.PUFFERFISH, Collection.SPONGE),
        CLAY(Collection.CLAY),
        FLOWER,
        TARANTULA(Collection.STRING, Collection.SPIDER_EYE, Collection.IRON_INGOT),
        REVENANT(Collection.ROTTEN_FLESH, Collection.DIAMOND),
        SNOW;

        private final String minionName;
        private final ConcurrentList<Collection> collections;

        Minion(Collection... collections) {
            this(null, collections);
        }

        Minion(String minionName, Collection... collections) {
            this.minionName = WordUtil.capitalizeFully((StringUtil.isEmpty(minionName) ? this.name() : minionName).replace("_", " "));
            this.collections = Concurrent.newList(collections);
        }

        public ConcurrentList<Collection> getCollections() {
            return this.collections;
        }

        public String getMinionName() {
            return this.minionName;
        }

    }

    /*
    {
    id: "minecraft:skull",
    Count: 1b,
    tag: {
        SkullOwner: {
            Id: "f33f51a7-9691-4076-abda-f66e3d047a71",
            Properties: {
                textures: [{
                    Signature: "c1Q0rQqA13TDB7leFcp1T0FJn3aCPFuNLU1MPPYSlZR9yY09MUYeSJy7nsNpIjmywo39Ra1GlFIpSKaWjg30BfYZOdw7ZJh+OCdWkYhWd44yHq5NMevMzmmWvAP2lc1zgn+P/2XwfmKzo0UueITq+gnpt3MbYojoAny8M/iLTGa+xlHnaUZR1M7Y8USBycET12xdqQNJ/pF4EXGwzZMStYXFYI+q4qNQUf0/4j8BnYOIYplWlxcGtU11DHvsyTWwauMfW6yHNj2HijwS2OHcean5P6II/TV3SoMwmE8DfDUPWU8rL5GJnZL0TUvwCgAvz0rxp8JLrZ68Ep80cvE7YviBeQAKa0KPkWszy+87eppupPyzbqmKlbM/FHlKFkHMh2pZK47+Y5vthROz2BAN+eqpn6YY/pVvYhHAVOYSOLo1r5ztSjHaN3WV8mpLN3G9qpGcW2hyMk43uFbUQ6PtfV7Ajza8iBJgQSjNmYPy4cu+nS31ipXF9Z1fkjqz5ZnIRVoJq7/IzgQrh085iBlvbTcmxNXJ+ccqfq860+Akib1JuRX+MEvv2KgZ12AkBgOY0IEqPo3AJTXm07f02cSvIN80V/q/VDaZ8QPp571DDPp+LILUSMyKQ61pA4yI4takYiYbs9KzWGChe9HuIs5Uxm6fuMHF+iDo0bOf4OSsV2Y=",
                    Value: "ewogICJ0aW1lc3RhbXAiIDogMTYzMjQxNTQ0ODMzMiwKICAicHJvZmlsZUlkIiA6ICJmMzNmNTFhNzk2OTE0MDc2YWJkYWY2NmUzZDA0N2E3MSIsCiAgInByb2ZpbGVOYW1lIiA6ICJDcmFmdGVkRnVyeSIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS9hMjc2ODQyYTZiYjA3MjU3YWFlNDAwODczN2I2NTAzYWJkOTJkNDgyNWY4NzkyNTE0YTM1Nzk3NzI3MzBiNTRiIgogICAgfSwKICAgICJDQVBFIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS8xNTNiMWEwZGZjYmFlOTUzY2RlYjZmMmMyYmY2YmY3OTk0MzIzOWIxMzcyNzgwZGE0NGJjYmIyOTI3MzEzMWRhIgogICAgfQogIH0KfQ=="
                }]
            },
            Name: "CraftedFury"
        },
        display: {
            Lore: [" §6 Mining Fortune §f768", "  §6☘ Farming Fortune §f224", "  §6☘ Foraging Fortune §f80", "  §5✧ Pristine §f1", "", "§7§eClick to view your profile!"],
            Name: "§aYour SkyBlock Profile"
        }
    },
    Damage: 3s
}
     */

    public enum Stat {

        DAMAGE('\u2741', MojangChatFormatting.RED),
        HEALTH('\u2764', MojangChatFormatting.RED),
        DEFENSE('\u2748', MojangChatFormatting.GREEN),
        STRENGTH('\u2741', MojangChatFormatting.RED),
        SPEED('\u2726', MojangChatFormatting.WHITE),
        CRIT_CHANCE('\u2623', MojangChatFormatting.BLUE),
        CRIT_DAMAGE('\u2620', MojangChatFormatting.BLUE),
        INTELLIGENCE('\u270e', MojangChatFormatting.AQUA),
        MINING_SPEED('\u2e15', MojangChatFormatting.GOLD),
        BONUS_ATTACK_SPEED('\u2694', MojangChatFormatting.YELLOW),
        SEA_CREATURE_CHANCE('\u03b1', MojangChatFormatting.DARK_AQUA),
        MAGIC_FIND('\u272f', MojangChatFormatting.AQUA),
        PET_LUCK('\u2663', MojangChatFormatting.LIGHT_PURPLE),
        TRUE_DEFENSE('\u2742', MojangChatFormatting.WHITE),
        FEROCITY('\u2afd', MojangChatFormatting.LIGHT_PURPLE),
        ABILITY_DAMAGE('\u0e51', MojangChatFormatting.RED),
        MINING_FORTUNE('\u2618', MojangChatFormatting.WHITE),
        FARMING_FORTUNE('\u2618', MojangChatFormatting.WHITE),
        FORAGING_FORTUNE('\u2618', MojangChatFormatting.WHITE),
        PRISTINE('\u2727', MojangChatFormatting.DARK_PURPLE);

        private final char symbolChar;
        private final MojangChatFormatting color;

        Stat(char symbolChar, MojangChatFormatting color) {
            this.symbolChar = symbolChar;
            this.color = color;
        }

        public MojangChatFormatting getColor() {
            return this.color;
        }

        public String getDisplayName() {
            return WordUtil.capitalizeFully(this.name().replaceAll("_", " "));
        }

        public String getFormattedDisplayName() {
            return FormatUtil.preformat("{0} {1}{2}", this.color, this.getSymbol(), this.getDisplayName(), MojangChatFormatting.GRAY);
        }

        public String getSymbol() {
            return StringUtil.unescapeJava(Character.toString(this.symbolChar));
        }

        public char getSymbolChar() {
            return this.symbolChar;
        }

    }

    public enum Pet {

        BABY_YETI(Type.FISHING_PET, InternalStat.BABY_YETI, (level, rarity) -> _i((level - 1) * 0.75, (level - 1) * 0.4)),
        BAT(Type.MINING_PET, InternalStat.BAT, (level, rarity) -> _i(level, level / 20)),
        BEE(Type.FARMING_PET, InternalStat.BEE, (level, rarity) -> _i(level / 2, 5 + ((level - 1) / 4), level / 10)),
        BLACK_CAT(Type.COMBAT_PET, InternalStat.BLACK_CAT, (level, rarity) -> _i(level, (int)(level * 0.24))),
        BLAZE(Type.COMBAT_PET, InternalStat.BLAZE, (level, rarity) -> _i(level, 10 + ((level - 1) / 5))),
        BLUE_WHALE(Type.FISHING_PET, InternalStat.BLUE_WHALE, (level, rarity) -> _i(2 + ((level - 1) * 2))),
        CHICKEN(Type.FARMING_PET, InternalStat.CHICKEN, (level, rarity) -> _i(2 + ((level - 1) * 2))),
        DOLPHIN(Type.FISHING_PET, InternalStat.DOLPHIN, (level, rarity) -> _i(level, (int)(level / 20))),
        ELEPHANT(Type.COMBAT_PET, InternalStat.ELEPHANT, (level, rarity) -> _i(level, (int)(level * 0.75))),
        ENDERMAN(Type.COMBAT_PET, InternalStat.ENDERMAN, (level, rarity) -> _i(level * 0.75)),
        ENDERMITE(Type.COMBAT_PET, InternalStat.ENDERMITE, (level, rarity) -> _i(level)),
        ENDER_DRAGON(Type.COMBAT_PET, InternalStat.ENDER_DRAGON, (level, rarity) -> _i(level / 2, level / 10, level / 2)),
        FLYING_FISH(Type.FISHING_PET, InternalStat.FLYING_FISH, (level, rarity) -> _i(rarity.ordinal() > 2 ? (level / 2) : (int)(level * 0.4), (rarity.ordinal() > 2 ? (level / 2) : (int)(level * 0.4)))),
        GHOUL(Type.COMBAT_PET, InternalStat.GHOUL, (level, rarity) -> _i(level, level * 0.7)),
        GIRAFFE(Type.FORAGING_PET, InternalStat.GIRAFFE, (level, rarity) -> _i(level, level / 20)),
        GOLEM(Type.COMBAT_PET, InternalStat.GOLEM, (level, rarity) -> _i(1 + (int)((level - 1) * 1.5051), level / 2)),
        GUARDIAN(Type.COMBAT_PET, InternalStat.GUARDIAN, (level, rarity) -> _i(level, level / 2)),
        HORSE(Type.COMBAT_MOUNT, InternalStat.HORSE, (level, rarity) -> _i(level / 2, level / 4)),
        HOUND(Type.COMBAT_PET, InternalStat.HOUND, (level, rarity) -> _i((int)(level * 0.4))),
        JELLYFISH(Type.ALCHEMY_PET, InternalStat.JELLYFISH, (level, rarity) -> _i(2 + ((level - 1) * 2))),
        JERRY(Type.COMBAT_PET, InternalStat.JERRY, (level, rarity) -> _i(level * -1)),
        LION(Type.FORAGING_PET, InternalStat.LION, (level, rarity) -> _i(level / 2, level / 4)),
        MAGMA_CUBE(Type.COMBAT_PET, InternalStat.MAGMA_CUBE, (level, rarity) -> _i(level / 2, level / 3, level / 5)),
        MONKEY(Type.COMBAT_PET, InternalStat.MONKEY, (level, rarity) -> _i(level / 5, level / 2)),
        OCELOT(Type.FORAGING_PET, InternalStat.OCELOT, (level, rarity) -> _i(level / 2)),
        PARROT(Type.ALCHEMY_PET, InternalStat.PARROT, (level, rarity) -> _i(level / 10, level)),
        PHOENIX(Type.COMBAT_PET, InternalStat.PHOENIX, (level, rarity) -> _i(10 + (level / 2), 51 + (level - 1))),
        PIG(Type.FARMING_PET, InternalStat.PIG, (level, rarity) -> _i(level / 4)),
        PIGMAN(Type.COMBAT_PET, InternalStat.PIGMAN, (level, rarity) -> _i(level / 2, level / 2)),
        RABBIT(Type.COMBAT_PET, InternalStat.RABBIT, (level, rarity) -> _i(level, level / 5)),
        ROCK(Type.COMBAT_PET, InternalStat.ROCK, (level, rarity) -> _i(level*2, level / 10)),
        SHEEP(Type.ALCHEMY_PET, InternalStat.SHEEP, (level, rarity) -> _i(level, level / 2)),
        SILVERFISH(Type.MINING_PET, InternalStat.SILVERFISH, (level, rarity) -> _i(level, level / 5)),
        SKELETON(Type.COMBAT_PET, InternalStat.SKELETON, (level, rarity) -> _i((int)(level / 6.667), (int)(level / 3.333))),
        SKELETON_HORSE(Type.COMBAT_MOUNT, InternalStat.SKELETON_HORSE, (level, rarity) -> _i(level, level  /  2)),
        SNOWMAN(Type.COMBAT_PET, InternalStat.SNOWMAN, (level, rarity) -> _i(level / 3, level / 3, level / 3)),
        SPIDER(Type.COMBAT_PET, InternalStat.SPIDER, (level, rarity) -> _i(level / 10, level / 10)),
        SQUID(Type.FISHING_PET, InternalStat.SQUID, (level, rarity) -> _i(level / 2, level / 2)),
        TARANTULA(Type.COMBAT_PET, InternalStat.TARANTULA, (level, rarity) -> _i(level / 10, level / 10, (int)(level / 3.33))),
        TIGER(Type.COMBAT_PET, InternalStat.TIGER, (level, rarity) -> _i(5 + (level / 10), level / 20, level / 2)),
        TURTLE(Type.COMBAT_PET, InternalStat.TURTLE, (level, rarity) -> _i(level / 2, level)),
        WITHER_SKELETON(Type.MINING_PET, InternalStat.WITHER_SKELETON, (level, rarity) -> _i(level / 4, level / 4, level / 4, level / 4, level / 20)),
        WOLF(Type.COMBAT_PET, InternalStat.WOLF, (level, rarity) -> _i(level / 10, level / 2, level / 6, level / 10)),
        ZOMBIE(Type.COMBAT_PET, InternalStat.ZOMBIE, (level, rarity) -> _i(level / 2, (int)(level / 3.5)));

        private final Type type;
        private final InternalStat internalStat;
        private final BiFunction<Integer, Skyblock.Item.Rarity, ConcurrentList<Object>> data;

        Pet(Type type, InternalStat internalStat) {
            this(type, internalStat, null);
        }

        Pet(Type type, InternalStat internalStat, BiFunction<Integer, Skyblock.Item.Rarity, ConcurrentList<Object>> data) {
            this.type = type;
            this.internalStat = internalStat;
            this.data = data;
        }

        public ConcurrentList<Ability> getAbilities() {
            return Ability.getAbilities(this);
        }

        public BiFunction<Integer, Skyblock.Item.Rarity, ConcurrentList<Object>> getData() {
            return this.data;
        }

        public ConcurrentList<String> getLore(int petLevel, Skyblock.Item.Rarity rarity) {
            ConcurrentList<Stat> stats = this.getStats();
            ConcurrentList<Object> statValues = this.data.apply(petLevel, rarity);
            ConcurrentList<String> lore = Concurrent.newList();

            for (int i = 0; i < stats.size(); i++) {
                Stat stat = stats.get(i);
                double value = Double.parseDouble(statValues.get(i).toString());
                lore.add(FormatUtil.preformat("{0}: {{1}}{{2}}", MojangChatFormatting.GRAY, MojangChatFormatting.GREEN, stat.getDisplayName(), (value < 1 ? "" : "+"), (int)value));
            }

            return lore;
        }

        public ConcurrentList<Stat> getStats() {
            return Concurrent.newList(this.internalStat.stats);
        }

        public Type getType() {
            return this.type;
        }

        private static ConcurrentList<Object> _i(Object... values) {
            return Concurrent.newList(values);
        }

        public enum Type {

            FARMING_PET,
            MINING_PET,
            COMBAT_PET,
            COMBAT_MOUNT,
            FORAGING_PET,
            FISHING_PET,
            ALCHEMY_PET;

            public String getPrettyName() {
                return WordUtil.capitalizeFully(this.name().replace("_", " "));
            }

        }

        private enum InternalStat {

            BABY_YETI(Stat.INTELLIGENCE, Stat.STRENGTH),
            BAT(Stat.INTELLIGENCE, Stat.SPEED),
            BEE(Stat.INTELLIGENCE, Stat.STRENGTH, Stat.SPEED),
            BLACK_CAT(Stat.INTELLIGENCE, Stat.SPEED),
            BLAZE(Stat.INTELLIGENCE, Stat.DEFENSE),
            BLUE_WHALE(Stat.HEALTH),
            CHICKEN(Stat.HEALTH),
            DOLPHIN(Stat.INTELLIGENCE, Stat.SEA_CREATURE_CHANCE),
            ELEPHANT(Stat.HEALTH, Stat.INTELLIGENCE),
            ENDERMAN(Stat.CRIT_DAMAGE),
            ENDERMITE(Stat.INTELLIGENCE),
            ENDER_DRAGON(Stat.CRIT_CHANCE, Stat.CRIT_DAMAGE),
            FLYING_FISH(Stat.STRENGTH, Stat.DEFENSE),
            GHOUL(Stat.HEALTH, Stat.INTELLIGENCE),
            GIRAFFE(Stat.HEALTH, Stat.CRIT_CHANCE),
            GOLEM(Stat.HEALTH, Stat.STRENGTH),
            GUARDIAN(Stat.INTELLIGENCE, Stat.DEFENSE),
            HORSE(Stat.INTELLIGENCE, Stat.SPEED),
            HOUND(Stat.STRENGTH),
            JELLYFISH(Stat.HEALTH),
            JERRY(Stat.INTELLIGENCE),
            LION(Stat.STRENGTH, Stat.SPEED),
            MAGMA_CUBE(Stat.HEALTH, Stat.DEFENSE, Stat.STRENGTH),
            MONKEY(Stat.SPEED, Stat.INTELLIGENCE),
            OCELOT(Stat.SPEED),
            PARROT(Stat.CRIT_DAMAGE, Stat.INTELLIGENCE),
            PHOENIX(Stat.STRENGTH, Stat.INTELLIGENCE),
            PIG(Stat.SPEED),
            PIGMAN(Stat.STRENGTH, Stat.DEFENSE),
            RABBIT(Stat.HEALTH, Stat.SPEED),
            ROCK(Stat.DEFENSE, Stat.TRUE_DEFENSE),
            SHEEP(Stat.INTELLIGENCE, Stat.ABILITY_DAMAGE),
            SILVERFISH(Stat.DEFENSE, Stat.HEALTH),
            SKELETON(Stat.CRIT_CHANCE, Stat.CRIT_DAMAGE),
            SKELETON_HORSE(Stat.INTELLIGENCE, Stat.SPEED),
            SNOWMAN(Stat.STRENGTH, Stat.CRIT_DAMAGE),
            SPIDER(Stat.STRENGTH, Stat.CRIT_CHANCE),
            SQUID(Stat.INTELLIGENCE, Stat.HEALTH),
            TARANTULA(Stat.CRIT_CHANCE, Stat.CRIT_DAMAGE, Stat.STRENGTH),
            TIGER(Stat.STRENGTH, Stat.CRIT_CHANCE, Stat.CRIT_DAMAGE),
            TURTLE(Stat.HEALTH, Stat.DEFENSE),
            WITHER_SKELETON(Stat.CRIT_DAMAGE, Stat.DEFENSE, Stat.STRENGTH, Stat.INTELLIGENCE, Stat.CRIT_CHANCE),
            WOLF(Stat.CRIT_DAMAGE, Stat.HEALTH, Stat.SPEED, Stat.TRUE_DEFENSE),
            ZOMBIE(Stat.CRIT_DAMAGE, Stat.HEALTH);

            private final Stat[] stats;

            InternalStat(Stat... stats) {
                this.stats = stats;
            }

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
                    FormatUtil.preformat("Max 15 bees", MojangChatFormatting.DARK_GRAY)
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
            BLING_ARMOR(BLAZE, Skyblock.Item.Rarity.EPIC, "Upgrades {{0}} stats\nand ability by {{1}%}", (level, rarity) -> _i(FormatUtil.preformat("Blaze Armor", MojangChatFormatting.RED), level * 0.4)),
            FUSION__STYLE_POTATO(BLAZE, Skyblock.Item.Rarity.EPIC, "Double effects of hot potato\nbooks"),

            // Blue Whale
            INGEST(BLUE_WHALE, Skyblock.Item.Rarity.COMMON, "All potions heal {0}", (level, rarity) -> _i(FormatUtil.preformat("+{0}%HEALTH_SYMBOL%", MojangChatFormatting.RED,level * (0.5 + rarity.ordinal())))),
            BULK(BLUE_WHALE, Skyblock.Item.Rarity.RARE, "Gain {{0}%DEFENSE%} per\n{1}", (level, rarity) -> _i(
                    smallDecimalFormat.format(level / 33.33),
                    FormatUtil.preformat("{0} Max %HEALTH%", MojangChatFormatting.RED, smallDecimalFormat.format(30 - ((rarity.ordinal() - 2) * 2.5)))
            )),
            ARCHIMEDES(BLUE_WHALE, Skyblock.Item.Rarity.LEGENDARY, "Gain {0}", (level, rarity) -> _i(FormatUtil.preformat("+{0}% Max %HEALTH%", MojangChatFormatting.RED, smallDecimalFormat.format(0.1 + ((level - 1) * 0.2015))))),

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
                    FormatUtil.preformat("{0} %HEALTH%", MojangChatFormatting.RED, smallDecimalFormat.format(level / 10))
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
            PEARL_MUNCHER(ENDERMITE, Skyblock.Item.Rarity.RARE, "Upon picking up an ender\npearl, consume it and gain {{0}}\n{1}", (level, rarity) -> _i(5 + ((level - 1) * 0.0506), FormatUtil.preformat("coins", MojangChatFormatting.GOLD))),
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
                    FormatUtil.preformat("{{0} %HEALTH_SYMBOL%}", MojangChatFormatting.GRAY, MojangChatFormatting.RED,smallDecimalFormat.format((rarity == Skyblock.Item.Rarity.RARE ? 0.1 : 0) +
                            (level - (rarity == Skyblock.Item.Rarity.RARE ? 1 : 0)) *(rarity.ordinal() > 2 ? 0.2 : rarity.ordinal() < 2 ? 0.1 : 0.1506)))
            )),
            HIGHER_GROUND(GIRAFFE, Skyblock.Item.Rarity.COMMON, "Grants {{0}} and\n{{1}} when mid\nair", (level, rarity) -> _i(
                    FormatUtil.format("+{0} %STRENGTH%", MojangChatFormatting.RED, level * (rarity == Skyblock.Item.Rarity.RARE ? 0.4 : 0.5)),
                    FormatUtil.format("+{0} %CRIT_CHANCE%", MojangChatFormatting.BLUE, smallDecimalFormat.format((rarity == Skyblock.Item.Rarity.RARE ? 20.1 : 24.2) + ((level - 1) * rarity.ordinal() == 4 ? 0.3617 : 0))) // TODO: Not 0
            )),
            LONG_NECK(GIRAFFE, Skyblock.Item.Rarity.LEGENDARY, "See enemies from afar and gain\n{{0}%} dodge chance", (level, rarity) -> _i(level * 0.25)),

            // Golem
            LAST_STAND(GOLEM, Skyblock.Item.Rarity.EPIC, "While less than 15% HP, deal\n{{0}%} more damage", (level, rarity) -> _i(level * 0.3)),
            RICOCHET(GOLEM, Skyblock.Item.Rarity.EPIC, "Your iron plating causes\n{{0}%} of attacks to ricochet\nand hit the attacker", (level, rarity) -> _i(level * 0.2)),
            TOSS(GOLEM, Skyblock.Item.Rarity.LEGENDARY, "Every 5 hits, throw the enemy\nup into the air and deal {{0}%}\ndamage (10s cooldown)", (level, rarity) -> _i(203 + ((level - 1) * 3))),

            // Guardian
            LAZERBEAM(GUARDIAN, Skyblock.Item.Rarity.COMMON, "Zaps your enemies for {0}\nyour Intelligence every {{1}s}", (level, rarity) -> _i(
                    FormatUtil.preformat("{{0}x}", MojangChatFormatting.GRAY, MojangChatFormatting.AQUA, smallDecimalFormat.format(0.2 + ((level - 1) * (rarity == Skyblock.Item.Rarity.COMMON ? 0.01819 : ((((rarity.ordinal() * 5) - 0.2) / 99) + 0.0001))))), 3
            )),
            ENCHANTING_EXP_BOOST(GUARDIAN, Skyblock.Item.Rarity.RARE, "Boosts your Enchanting exp\nby {{0}%}", (level, rarity) -> _i(level * (rarity == Skyblock.Item.Rarity.RARE ? 0.25 : 0.3))),
            MANA_POOL(GUARDIAN, Skyblock.Item.Rarity.LEGENDARY, "Regenerate {{0}} extra mana,\ndoubled when near or in water", (level, rarity) -> _i(FormatUtil.preformat("{0}%", MojangChatFormatting.AQUA, level * 0.3))),

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
            JERRY_3(JERRY, Skyblock.Item.Rarity.LEGENDARY, "Actually adds {{0}} %DAMAGE% to\nthe Aspect of the Jerry", (level, rarity) -> _i(FormatUtil.preformat(smallDecimalFormat.format(level / 10), MojangChatFormatting.RED))),

            // Lion
            PRIMAL_FORCE(LION, Skyblock.Item.Rarity.COMMON, "Adds {0} %DAMAGE% to\nyour weapons", (level, rarity) -> _i(
                    FormatUtil.preformat("+{0}", MojangChatFormatting.RED, smallDecimalFormat.format((rarity.ordinal() * 0.05) + ((level - 1) * (rarity == Skyblock.Item.Rarity.COMMON ? 0.03 : (rarity.ordinal() * 0.05)))))
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
                    FormatUtil.preformat("+{0} %STRENGTH%", MojangChatFormatting.RED, 5.2 + ((level - 1) * 0.250505)),
                    20, FormatUtil.preformat("Doesnt Stack", MojangChatFormatting.DARK_GRAY)
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
                    FormatUtil.preformat("immune", MojangChatFormatting.YELLOW),
                    FormatUtil.preformat("{0}", MojangChatFormatting.RED, Math.round(10.1 + ((level - 1) * (rarity == Skyblock.Item.Rarity.LEGENDARY ? 0.202 : 0.1)))),
                    smallDecimalFormat.format(2 + ((level - 1) * 0.02)),
                    FormatUtil.preformat("3 minutes cooldown", MojangChatFormatting.DARK_GRAY)
            )),
            FOURTH_FLARE(PHOENIX, Skyblock.Item.Rarity.EPIC, "On 4th melee strike, {0}\nmobs, dealing {{1}} your\n%CRIT_DAMAGE% each second\nfor {{2}} seconds", (level, rarity) -> _i(
                    FormatUtil.preformat("ignite", MojangChatFormatting.GOLD),
                    FormatUtil.preformat("{0}x", MojangChatFormatting.RED, smallDecimalFormat.format(1.1 + ((level - 1) * (rarity == Skyblock.Item.Rarity.LEGENDARY ? 0.1405 : 0.121)))), // epic: 0.07?
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
            ONE_WITH_THE_SPIDER(SPIDER, Skyblock.Item.Rarity.COMMON, "Gain {{0}} %STRENGTH% for\nevery nerby spider\n{1}", (level, rarity) -> _i(level * 0.1, FormatUtil.preformat("Max 10 spiders", MojangChatFormatting.DARK_GRAY))),
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
            HEMORRHAGE(TIGER, Skyblock.Item.Rarity.RARE, "Melee attacks reduce healing\nby {0} for {{1}s}", (level, rarity) -> _i(FormatUtil.preformat("{{0}%}", MojangChatFormatting.GRAY, MojangChatFormatting.GOLD, level * (rarity.ordinal() >= 3 ? 0.55 : 0.3)), 10)),
            APEX_PREDATOR(TIGER, Skyblock.Item.Rarity.LEGENDARY, "Deal {0} damage against\ntargets with no other mobs\nwithin 15 blocks", (level, rarity) -> _i(FormatUtil.format("{+{0}%}", MojangChatFormatting.GRAY, MojangChatFormatting.RED, level * 0.2))),

            // Turtle
            TURTLE_TACTICS(TURTLE, Skyblock.Item.Rarity.EPIC, "Gain {+{0}%} %DEFENSE%", (level, rarity) -> _i(smallDecimalFormat.format(3.2 + ((level - 1) * 0.1702)))),
            GENIUS_AMNIOTE(TURTLE, Skyblock.Item.Rarity.EPIC, "Gain {+{0}} %DEFENSE% and\nregen {{1}} per second when\nnear or in water", (level, rarity) -> _i(
                    smallDecimalFormat.format(5.5 + ((level - 1) * (rarity.ordinal() == 4 ? 0.45 : 0.347))),
                    FormatUtil.preformat("+{0} %HEALTH_SYMBOL%", MojangChatFormatting.RED, smallDecimalFormat.format(0.2 + ((level - 1) * (rarity.ordinal() == 4 ? 0.2513 : 0.2))))
            )),
            UNFLIPPABLE(TURTLE, Skyblock.Item.Rarity.LEGENDARY, "Gain {{0}} to knockback", (level, rarity) -> _i("immunity")),

            // Wither Skeleton
            STRONG_BONES(WITHER_SKELETON, Skyblock.Item.Rarity.COMMON, "Take {{0}%} less damage from\nskeletons", (level, rarity) -> _i(level * 0.3)),
            WITHER_BLOOD(WITHER_SKELETON, Skyblock.Item.Rarity.RARE, "Deal {{0}%} more damage to\nwither mobs", (level, rarity) -> _i(level * 0.5)),
            DEATHS_TOUCH(WITHER_SKELETON, Skyblock.Item.Rarity.LEGENDARY, "Upon hitting an enemy inflict\nthe wither effect for {{0}%}\ndamage over 3 seconds\n{1}", (level, rarity) -> _i(level * 2, FormatUtil.preformat("Does not Stack", MojangChatFormatting.DARK_GRAY))),

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
                    this.transform = (l, r) -> FormatUtil.preformat(this.format, MojangChatFormatting.GRAY, MojangChatFormatting.GREEN, this.data.apply(l, r).toArray());
                else
                    this.transform = (l, r) -> FormatUtil.preformat(this.format, MojangChatFormatting.GRAY, MojangChatFormatting.GREEN);
            }

            public BiFunction<Integer, Skyblock.Item.Rarity, ConcurrentList<Object>> getData() {
                return this.data;
            }

            public String getFormat() {
                return this.format;
            }

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

            public static ConcurrentList<Ability> getAbilities(Pet pet) {
                ConcurrentList<Ability> abilities = Concurrent.newList();

                for (Ability lore : values()) {
                    if (lore.getPet() == pet)
                        abilities.add(lore);
                }

                return abilities;
            }

            public static ConcurrentList<Ability> getAbilities(SkyblockIsland.PetInfo petInfo) {
                ConcurrentList<Ability> abilities = getAbilities(petInfo.getPet());
                abilities.removeIf(ability -> petInfo.getTier().ordinal() < ability.getRarity().ordinal());
                return abilities;
            }

        }

        public enum Item {

            // General
            BIG_TEETH("Increases %CRIT_CHANCE% by {{0}}", (level, rarity) -> _i(5)),
            IRON_CLAWS("Increases the pets %CRIT_DAMAGE%\nby {{0}%} and %CRIT_CHANCE% by {{1}%}", (level, rarity) -> _i(40, 40)),
            HARDENED_SCALES("Increases %DEFENSE% by {{0}}", (level, rarity) -> _i(25)),
            SHARPENED_CLAWS("Increases %CRIT_DAMAGE% by {{0}}", (level, rarity) -> _i(15)),
            BUBBLEGUM("Your pet fuses its power with\nplaced {{1}} to give them\n{{0}x} duration", (level, rarity) -> _i(2, "Orbs")),
            TIER_BOOST("Boosts the {{0}} of your pet by 1 tier!", (level, rarity) -> _i("rarity")),
            TEXTBOOK("Increases the pets\n%INTELLIGENCE% by {{0}%}", (level, rarity) -> _i(100)),
            LUCKY_CLOVER("Increases %MAGIC_FIND% by {{0}}", (level, rarity) -> _i(7)),
            EXP_SHARE("While unequipped this pet\ngains {{0}%} of the equipped\npet''s xp, this is split between\nall pets holding the item", (level, rarity) -> _i(25)),

            // Skills
            ALL_SKILLS_BOOST("Gives +{{0}%} pet exp for all skills", (level, rarity) -> _i(10)),
            FARMING_SKILL_BOOST("Gives +{{0}%} pet exp for Farming", (level, rarity) -> _i(10 + (rarity.ordinal() * 10))),
            MINING_SKILL_BOOST("Gives +{{0}%} pet exp for Mining", (level, rarity) -> _i(10 + (rarity.ordinal() * 10))),
            COMBAT_SKILL_BOOST("Gives +{{0}%} pet exp for Combat", (level, rarity) -> _i(10 + (rarity.ordinal() * 10))),
            FORAGING_SKILL_BOOST("Gives +{{0}%} pet exp for Foraging", (level, rarity) -> _i(10 + (rarity.ordinal() * 10))),
            FISHING_SKILL_BOOST("Gives +{{0}%} pet exp for Fishing", (level, rarity) -> _i(10 + (rarity.ordinal() * 10))),

            UNKNOWN("Unknown pet item");

            private final InternalStat internalStat;
            private final String format;
            private final BiFunction<Integer, Skyblock.Item.Rarity, ConcurrentList<Object>> data;
            private final BiFunction<Integer, Skyblock.Item.Rarity, String> transform;

            Item(String format) {
                this(format, null);
            }

            Item(String format, BiFunction<Integer, Skyblock.Item.Rarity, ConcurrentList<Object>> data) {
                this(format, null, data);
            }

            Item(String format, InternalStat internalStat, BiFunction<Integer, Skyblock.Item.Rarity, ConcurrentList<Object>> data) {
                this.internalStat = internalStat;
                this.format = format;
                this.data = data;

                if (this.data != null)
                    this.transform = (l, r) -> FormatUtil.preformat(this.format, MojangChatFormatting.GRAY, MojangChatFormatting.GREEN, this.data.apply(l, r).toArray());
                else
                    this.transform = (l, r) -> FormatUtil.preformat(this.format, MojangChatFormatting.GRAY, MojangChatFormatting.GREEN);
            }

            public BiFunction<Integer, Skyblock.Item.Rarity, ConcurrentList<Object>> getData() {
                return this.data;
            }

            public String getFormat() {
                return this.format;
            }

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

            public ConcurrentList<Stat> getStats() {
                return Concurrent.newList(this.internalStat.stats);
            }

            public BiFunction<Integer, Skyblock.Item.Rarity, String> getTransform() {
                return this.transform;
            }

            public Skyblock.Item.Rarity getUniqueRarity() {
                return UniqueRarity.getRarity(this.name());
            }

            private enum InternalStat {

                BIG_TEETH(Stat.CRIT_CHANCE),
                IRON_CLAWS(Stat.CRIT_DAMAGE, Stat.CRIT_CHANCE),
                HARDENED_SCALES(Stat.DEFENSE),
                SHARPENED_CLAWS(Stat.CRIT_DAMAGE),
                TEXTBOOK(Stat.INTELLIGENCE),
                LUCKY_CLOVER(Stat.MAGIC_FIND);

                private final Stat[] stats;

                InternalStat(Stat... stats) {
                    this.stats = stats;
                }

            }

            private enum UniqueRarity {

                BIG_TEETH(Skyblock.Item.Rarity.COMMON),
                IRON_CLAWS(Skyblock.Item.Rarity.COMMON),
                HARDENED_SCALES(Skyblock.Item.Rarity.UNCOMMON),
                SHARPENED_CLAWS(Skyblock.Item.Rarity.UNCOMMON),
                BUBBLEGUM(Skyblock.Item.Rarity.RARE),
                TIER_BOOST(Skyblock.Item.Rarity.LEGENDARY),
                TEXTBOOK(Skyblock.Item.Rarity.LEGENDARY),
                LUCKY_CLOVER(Skyblock.Item.Rarity.EPIC),
                EXP_SHARE(Skyblock.Item.Rarity.EPIC),
                ALL_SKILLS_BOOST(Skyblock.Item.Rarity.COMMON);

                private final Skyblock.Item.Rarity rarity;

                UniqueRarity(Skyblock.Item.Rarity rarity) {
                    this.rarity = rarity;
                }

                static Skyblock.Item.Rarity getRarity(String name) {
                    for (UniqueRarity uniqueRarity : values()) {
                        if (uniqueRarity.name().equals(name))
                            return uniqueRarity.rarity;
                    }

                    return null;
                }

            }

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

        public boolean hasCommand() {
            return this.hasCommand;
        }

        public boolean isSkyblockSubMenu() {
            return this.skyblockSubMenu;
        }

    }

    public enum Entity {

        // Vanilla
        BAT(EntityBat.class),
        BLAZE(EntityBlaze.class),
        CAVE_SPIDER(EntityCaveSpider.class),
        CHICKEN(EntityChicken.class),
        CREEPER(EntityCreeper.class),
        COW(EntityCow.class),
        ENDERMITE(EntityEndermite.class),
        ENDERMAN(EntityEnderman.class),
        GHAST(EntityGhast.class),
        HORSE(EntityHorse.class),
        MAGMA_CUBE(EntityMagmaCube.class),
        PIG(EntityPig.class),
        RABBIT(EntityRabbit.class),
        SHEEP(EntitySheep.class),
        SILVERFISH(EntitySilverfish.class),
        SKELETON(EntitySkeleton.class),
        SKELETON_HORSE(EntitySkeletonHorse.class),
        SLIME(EntitySlime.class),
        SPIDER(EntitySpider.class),
        SQUID(EntitySquid.class),
        WITCH(EntityWitch.class),
        WITHER_SKELETON(EntityWitherSkeleton.class),
        WOLF(EntityWolf.class),
        ZOMBIE(EntityZombie.class),
        ZOMBIE_PIGMAN(EntityPigZombie.class),
        ZOMBIE_VILLAGER(EntityZombieVillager.class),

        // Spooky Festival
        HEADLESS_HORSEMAN(EntitySkeletonHorse.class, EntitySkeleton.class),
        BAT_PINATA("Bat Pi�ata", EntityBat.class),

        // Bosses
        BROOD_MOTHER_CAVE_SPIDER("Brood Mother", EntityCaveSpider.class),
        BROOD_MOTHER_SPIDDER("Brood Mother", EntitySpider.class),
        MAGMA_CUBE_BOSS(EntityMagmaCube.class),
        CORRUPTED_PROTECTOR(EntityGolem.class),

        // Dragons
        OLD_DRAGON(EntityDragon.class),
        PROTECTOR_DRAGON(EntityDragon.class),
        STRONG_DRAGON(EntityDragon.class),
        SUPERIOR_DRAGON(EntityDragon.class),
        UNSTABLE_DRAGON(EntityDragon.class),
        WISE_DRAGON(EntityDragon.class),
        YOUNG_DRAGON(EntityDragon.class),

        // Fishing
        CARROT_KING(EntityRabbit.class),
        CATFISH(EntityOcelot.class),
        DEEP_SEA_PROTECTOR(EntityIronGolem.class),
        FROZEN_STEVE(EntityOtherPlayerMP.class),
        FROSTY_THE_SNOWMAN(EntitySnowman.class),
        GRINCH(EntityOtherPlayerMP.class),
        GUARDIAN_DEFENDER(EntityGuardian.class),
        LIQUID_HOT_MAGMA(EntityOtherPlayerMP.class),
        MONSTER_OF_THE_DEEP(EntityChicken.class, EntityZombie.class),
        NIGHT_SQUID(EntitySquid.class),
        SEA_ARCHER(EntitySkeleton.class),
        SEA_EMPEROR(EntityGuardian.class, EntitySkeleton.class),
        SEA_GUARDIAN(EntityGuardian.class),
        SEA_LEECH(EntitySilverfish.class),
        SEA_WALKER(EntityZombie.class),
        SEA_WITCH(EntityWitch.class),
        WATER_HYDRA(EntityZombie.class),
        YETI(EntityOtherPlayerMP.class),

        // The End
        WATCHER(EntityArmorStand.class),
        OBSIDIAN_DEFENDER(EntityWitherSkeleton.class),
        ZEALOT(EntityEnderman.class),
        ENDER_CRYSTAL(EntityEnderCrystal.class),

        // Spiders Den
        DASHER_SPIDER(EntitySpider.class),
        JOCKEY_SKELETON(EntitySkeleton.class),
        SPIDER_JOCKEY(EntitySpider.class, EntitySkeleton.class),
        SPLITTER_SPIDER(EntitySpider.class),
        WEAVER_SPIDER(EntitySpider.class),

        // Slayer - Revenant Horror
        CRYPT_GHOUL(EntityZombie.class),
        DEFORMED_REVENANT(EntityZombie.class),
        GOLDEN_GHOUL(EntityZombie.class),
        REVENANT_CHAMPION(EntityZombie.class),
        REVENANT_HORROR(EntityZombie.class),
        REVENANT_SYCOPHANT(EntityZombie.class),

        // Slayer - Tarantula Broodfather
        MUTANT_TARANTULA(EntitySpider.class),
        TARANTULA_BEAST(EntitySpider.class),
        TARANTULA_BROODFATHER(EntitySpider.class),
        TARANTULA_VERMIN(EntitySpider.class),
        VORACIOUS_SPIDER(EntitySpider.class),

        // Slayer - Sven Packmaster
        HOWLING_SPIRIT(EntityWolf.class),
        OLD_WOLF(EntityWolf.class),
        PACK_ENFORCER(EntityWolf.class),
        PACK_SPIRIT(EntityWolf.class),
        SOUL_OF_THE_ALPHA(EntityWolf.class),
        SVEN_ALPHA("Alpha", EntityWolf.class),
        SVEN_FOLLOWER(EntityWolf.class),
        SVEN_PACKMASTER(EntityWolf.class),
        VICIOUS_WOLF(EntityWolf.class),

        // Deep Caverns
        DIAMOND_ZOMBIE(EntityZombie.class),
        DIAMOND_SKELETON(EntityZombie.class),
        EMERALD_SLIME(EntitySlime.class),
        LAPIS_ZOMBIE(EntityZombie.class),
        REDSTONE_PIGMAN(EntityPigZombie.class),
        SNEAKY_CREEPER(EntityCreeper.class);

        private final String name;
        private final ConcurrentList<Class<? extends net.minecraft.entity.Entity>> entities;

        @SafeVarargs
        Entity(Class<? extends net.minecraft.entity.Entity>... entities) {
            this(null, entities);
        }

        @SafeVarargs
        Entity(String name, Class<? extends net.minecraft.entity.Entity>... entities) {
            this.name = (StringUtil.isEmpty(name) ? WordUtil.capitalizeFully(name().replace("_", " ")) : name);
            this.entities = Concurrent.newList(entities);
        }

        public ConcurrentList<Class<? extends net.minecraft.entity.Entity>> getEntities() {
            return this.entities;
        }

        public String getName() {
            return this.name;
        }

    }

    static {
        setDefaults();

        SLAYER_EXP_SCALE.put(Slayer.REVENANT_HORROR, Concurrent.newList(5, 10, 185, 800, 4000, 15000, 80000, 300000, 600000));
        SLAYER_EXP_SCALE.put(Slayer.TARANTULA_BROODFATHER, Concurrent.newList(5, 10, 185, 800, 4000, 15000, 80000, 300000, 600000));
        SLAYER_EXP_SCALE.put(Slayer.SVEN_PACKMASTER, Concurrent.newList(5, 10, 185, 1300, 3500, 15000, 80000, 300000, 600000));

        PET_EXP_SCALE_OFFSET.put(Item.Rarity.COMMON, 0);
        PET_EXP_SCALE_OFFSET.put(Item.Rarity.UNCOMMON, 6);
        PET_EXP_SCALE_OFFSET.put(Item.Rarity.RARE, 11);
        PET_EXP_SCALE_OFFSET.put(Item.Rarity.EPIC, 16);
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
            PET_EXP_TIER_SCALE.put(entry.getKey(), Skyblock.PET_EXP_SCALE.subList(entry.getValue(), entry.getValue() + 99));

        SKILL_EXP_SCALE = Concurrent.newList(
                50, 125, 200, 300, 500, 750, 1000, 1500, 2000, 3500, 5000, 7500, 10000,
                15000, 20000, 30000, 50000, 75000, 100000, 200000, 300000, 400000, 500000,
                600000, 700000, 800000, 900000, 1000000, 1100000, 1200000, 1300000, 1400000,
                1500000, 1600000, 1700000, 1800000, 1900000, 2000000, 2100000, 2200000, 2300000,
                2400000, 2500000, 2600000, 2750000, 2900000, 3100000, 3400000, 3700000, 4000000);

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