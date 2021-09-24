package gg.sbs.api.httpclients.hypixel;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

public class HypixelPlayerResponse {
    private boolean success;
    private HypixelPlayer player;

    public boolean isSuccess() {
        return success;
    }

    public HypixelPlayer getPlayer() {
        return player;
    }

    public static class HypixelPlayer {
        @SerializedName("_id")
        private String id;

        private String uuid;

        @SerializedName("displayname")
        private String displayName;

        private long firstLogin;
        private List<String> knownAliases;
        private List<String> knownAliasesLower;
        private long lastLogin;

        @SerializedName("playername")
        private String playerName;

        private HypixelPlayerStats stats;
        private long lastLogout;
        private String monthlyPackageRank;
        private String mostRecentMonthlyPackageRank;
        private String monthlyRankColor;

        public String getId() {
            return id;
        }

        public String getUuid() {
            return uuid;
        }

        public String getDisplayName() {
            return displayName;
        }

        public long getFirstLogin() {
            return firstLogin;
        }

        public List<String> getKnownAliases() {
            return knownAliases;
        }

        public List<String> getKnownAliasesLower() {
            return knownAliasesLower;
        }

        public long getLastLogin() {
            return lastLogin;
        }

        public String getPlayerName() {
            return playerName;
        }

        public HypixelPlayerStats getStats() {
            return stats;
        }

        public long getLastLogout() {
            return lastLogout;
        }

        public String getMonthlyPackageRank() {
            return monthlyPackageRank;
        }

        public String getMostRecentMonthlyPackageRank() {
            return mostRecentMonthlyPackageRank;
        }

        public String getMonthlyRankColor() {
            return monthlyRankColor;
        }
    }

    public static class HypixelPlayerStats {
        @SerializedName("SkyBlock")
        private HypixelPlayerStatsSkyBlock skyBlock;

        public HypixelPlayerStatsSkyBlock getSkyBlock() {
            return skyBlock;
        }
    }

    public static class HypixelPlayerStatsSkyBlock {
        private Map<String, HypixelPlayerStatsSkyBlockProfile> profiles;

        public Map<String, HypixelPlayerStatsSkyBlockProfile> getProfiles() {
            return profiles;
        }
    }

    public static class HypixelPlayerStatsSkyBlockProfile {
        @SerializedName("profile_id")
        private String profileId;

        @SerializedName("cute_name")
        private String cuteName;

        public String getProfileId() {
            return profileId;
        }

        public String getCuteName() {
            return cuteName;
        }
    }
}
