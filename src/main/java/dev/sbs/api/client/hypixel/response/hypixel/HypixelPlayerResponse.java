package dev.sbs.api.client.hypixel.response.hypixel;

import com.google.gson.annotations.SerializedName;
import dev.sbs.api.client.hypixel.response.skyblock.implementation.island.SkyBlockIsland;
import dev.sbs.api.minecraft.text.ChatFormat;
import dev.sbs.api.util.builder.string.StringBuilder;
import dev.sbs.api.util.collection.concurrent.Concurrent;
import dev.sbs.api.util.collection.concurrent.ConcurrentList;
import dev.sbs.api.util.collection.concurrent.ConcurrentMap;
import dev.sbs.api.util.collection.concurrent.ConcurrentSet;
import dev.sbs.api.util.helper.RegexUtil;
import dev.sbs.api.util.helper.StringUtil;
import lombok.Getter;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Getter
public class HypixelPlayerResponse {

    private boolean success;
    private Player player;

    public static class Player {

        @SerializedName("_id")
        @Getter private String hypixelId;
        private String uuid;
        @SerializedName("displayname")
        @Getter private String displayName;
        @SerializedName("channel")
        @Getter private String chatChannel;
        @Getter private Instant firstLogin;
        @Getter private Instant lastLogin;
        @Getter private Instant lastLogout;
        @Getter private long networkExp;
        @Getter private long karma;
        @Getter private int achievementPoints;
        @Getter private long totalDailyRewards;
        @Getter private long totalRewards;
        @Getter private String mcVersionRp;
        @Getter private String mostRecentGameType;
        @SerializedName("playername")
        private String playerName;
        @Getter private ConcurrentList<String> knownAliases;
        @Getter private SocialMedia socialMedia;
        private ConcurrentList<Object> achievementsOneTime = Concurrent.newList();
        private transient ConcurrentList<String> achievementsOneTimeFixed;
        @Getter private String currentClickEffect;
        @Getter private String currentGadget;
        @SerializedName("claimed_potato_talisman")
        @Getter private Instant claimedPotatoTalisman;
        @SerializedName("skyblock_free_cookie")
        @Getter private Instant skyblockFreeCookie;
        @SerializedName("claimed_century_cake")
        @Getter private Instant claimedCenturyCake;
        @SerializedName("scorpius_bribe_120")
        @Getter private Instant scorpiusBribe120;
        @Getter private ConcurrentMap<String, Long> voting = Concurrent.newMap();
        @Getter private ConcurrentMap<String, Integer> petConsumables = Concurrent.newMap();
        @Getter private ConcurrentMap<String, Integer> achievements = Concurrent.newMap();
        @Getter private ConcurrentMap<String, Instant> achievementRewardsNew = Concurrent.newMap();

        // Rank
        private String packageRank;
        private String newPackageRank;
        private String monthlyPackageRank;
        private String rank;
        private String prefix;
        private String monthlyRankColor;
        private String rankPlusColor;
        private String mostRecentMonthlyPackageRank;

        // Stats (Only SkyBlock Currently)
        @Getter private Stats stats;

        public ConcurrentList<String> getAchievementsOneTime() {
            if (this.achievementsOneTimeFixed == null)
                this.achievementsOneTimeFixed = this.achievementsOneTime.stream()
                    .filter(obj -> (obj instanceof String))
                    .map(String::valueOf)
                    .collect(Concurrent.toList());

            return achievementsOneTimeFixed;
        }

        public RankInfo getRank() {
            Rank rank = Rank.NONE;

            if (StringUtil.isNotEmpty(this.packageRank))
                rank = Rank.getRank(this.packageRank);

            if (StringUtil.isNotEmpty(this.newPackageRank))
                rank = Rank.getRank(this.newPackageRank);

            if (StringUtil.isNotEmpty(this.monthlyPackageRank) && !"NONE".equals(this.monthlyPackageRank))
                rank = Rank.getRank(this.monthlyPackageRank);

            if (StringUtil.isNotEmpty(this.rank) && !"NORMAL".equals(this.rank))
                rank = Rank.getRank(this.rank);

            if (StringUtil.isNotEmpty(this.prefix))
                rank = Rank.getRank(RegexUtil.strip(this.prefix, RegexUtil.VANILLA_PATTERN).replaceAll("[\\W]", ""));

            ChatFormat rankFormat = rank.getFormat();
            ChatFormat plusFormat = rank.getFormat();

            if (rank == Rank.SUPERSTAR && StringUtil.isNotEmpty(this.monthlyRankColor))
                rankFormat = ChatFormat.valueOf(this.monthlyRankColor);

            if (StringUtil.isNotEmpty(this.rankPlusColor))
                plusFormat = ChatFormat.valueOf(this.rankPlusColor);

            if (rank == Rank.PIG)
                plusFormat = ChatFormat.AQUA;

            return new RankInfo(rank, rankFormat, plusFormat);
        }

        public UUID getUniqueId() {
            return StringUtil.toUUID(this.uuid);
        }

        public boolean isOnSkyblockIsland(SkyBlockIsland skyblockIsland) {
            return this.isOnSkyblockIsland(skyblockIsland.getIslandId());
        }

        public boolean isOnSkyblockIsland(UUID islandId) {
            Stats.SkyBlock skyBlock = this.getStats().getSkyBlock();

            if (skyBlock.getProfiles().isPresent()) {
                for (Stats.SkyBlock.Profile profile : skyBlock.getProfiles().get()) {
                    if (profile.getUniqueId().equals(islandId))
                        return true;
                }
            }

            return false;
        }


    }

    @Getter
    public enum Rank {

        OWNER(ChatFormat.RED),
        ADMIN(ChatFormat.RED),
        BUILD_TEAM(ChatFormat.DARK_AQUA),
        MODERATOR(ChatFormat.DARK_GREEN, "MOD"),
        HELPER(ChatFormat.BLUE),
        JR_HELPER(ChatFormat.BLUE),
        YOUTUBER(ChatFormat.RED, "YOUTUBE"),
        SUPERSTAR(ChatFormat.GOLD, "MVP", 2),
        MVP_PLUS(ChatFormat.AQUA, "MVP", 1),
        MVP(ChatFormat.AQUA),
        VIP_PLUS(ChatFormat.GREEN, "VIP", 1),
        VIP(ChatFormat.GREEN),
        PIG(ChatFormat.LIGHT_PURPLE, "PIG", 3),
        NONE(ChatFormat.GRAY);

        private final ChatFormat format;
        private final String name;
        private final int plusCount;

        Rank(ChatFormat format) {
            this(format, null);
        }

        Rank(ChatFormat format, String name) {
            this(format, name, 0);
        }

        Rank(ChatFormat format, String name, int plusCount) {
            this.format = format;
            this.name = (StringUtil.isEmpty(name) ? name() : name).replace("_", " ");
            this.plusCount = plusCount;
        }

        public static Rank getRank(String name) {
            name = name.replaceAll("\\+", "");

            for (Rank rank : values()) {
                if (rank.name().equals(name))
                    return rank;
            }

            return Rank.NONE;
        }

    }

    @Getter
    public static class RankInfo {

        private final Rank rank;
        private final ChatFormat rankFormat;
        private final ChatFormat plusFormat;
        private final String pluses;

        RankInfo(Rank rank, ChatFormat rankFormat, ChatFormat plusFormat) {
            this.rank = rank;
            this.rankFormat = rankFormat;
            this.plusFormat = plusFormat;

            StringBuilder builder = new StringBuilder();
            builder.appendPadding(this.getRank().getPlusCount(), '+');
            this.pluses = builder.build();
        }

        @Override
        public String toString() {
            String sfPluses = String.format("%s%s", this.getPlusFormat(), this.getPluses());
            String sfRank = String.format("%s%s", this.getRankFormat(), this.getRank().getName());
            return String.format("%s[%s%s%s]", ChatFormat.WHITE, sfRank, sfPluses, ChatFormat.WHITE);
        }

    }

    public static class SocialMedia {

        private boolean prompt;
        @Getter private ConcurrentMap<Service, String> links = Concurrent.newMap();

        public enum Service {

            TWITTER,
            YOUTUBE,
            INSTAGRAM,
            TWITCH,
            DISCORD,
            HYPIXEL

        }

    }

    @Getter
    public static class Stats {

        @SerializedName("SkyBlock")
        private SkyBlock skyBlock;

        public static class SkyBlock {

            private ConcurrentMap<String, Profile> profiles = Concurrent.newMap();

            public Optional<ConcurrentSet<Profile>> getProfiles() {
                return this.profiles == null ? Optional.empty() : Optional.of(Concurrent.newSet(this.profiles.values()));
            }

            @Getter
            public static class Profile {

                @SerializedName("profile_id")
                private UUID uniqueId;
                @SerializedName("cute_name")
                private String cuteName;

            }

        }

    }

}
