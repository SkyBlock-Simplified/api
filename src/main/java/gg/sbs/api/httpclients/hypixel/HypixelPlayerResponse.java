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

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public HypixelPlayer getPlayer() {
        return player;
    }

    public void setPlayer(HypixelPlayer player) {
        this.player = player;
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

        public void setId(String id) {
            this.id = id;
        }

        public String getUuid() {
            return uuid;
        }

        public void setUuid(String uuid) {
            this.uuid = uuid;
        }

        public String getDisplayName() {
            return displayName;
        }

        public void setDisplayName(String displayName) {
            this.displayName = displayName;
        }

        public long getFirstLogin() {
            return firstLogin;
        }

        public void setFirstLogin(long firstLogin) {
            this.firstLogin = firstLogin;
        }

        public List<String> getKnownAliases() {
            return knownAliases;
        }

        public void setKnownAliases(List<String> knownAliases) {
            this.knownAliases = knownAliases;
        }

        public List<String> getKnownAliasesLower() {
            return knownAliasesLower;
        }

        public void setKnownAliasesLower(List<String> knownAliasesLower) {
            this.knownAliasesLower = knownAliasesLower;
        }

        public long getLastLogin() {
            return lastLogin;
        }

        public void setLastLogin(long lastLogin) {
            this.lastLogin = lastLogin;
        }

        public String getPlayerName() {
            return playerName;
        }

        public void setPlayerName(String playerName) {
            this.playerName = playerName;
        }

        public HypixelPlayerStats getStats() {
            return stats;
        }

        public void setStats(HypixelPlayerStats stats) {
            this.stats = stats;
        }

        public long getLastLogout() {
            return lastLogout;
        }

        public void setLastLogout(long lastLogout) {
            this.lastLogout = lastLogout;
        }

        public String getMonthlyPackageRank() {
            return monthlyPackageRank;
        }

        public void setMonthlyPackageRank(String monthlyPackageRank) {
            this.monthlyPackageRank = monthlyPackageRank;
        }

        public String getMostRecentMonthlyPackageRank() {
            return mostRecentMonthlyPackageRank;
        }

        public void setMostRecentMonthlyPackageRank(String mostRecentMonthlyPackageRank) {
            this.mostRecentMonthlyPackageRank = mostRecentMonthlyPackageRank;
        }

        public String getMonthlyRankColor() {
            return monthlyRankColor;
        }

        public void setMonthlyRankColor(String monthlyRankColor) {
            this.monthlyRankColor = monthlyRankColor;
        }
    }

    public static class HypixelPlayerStats {
        @SerializedName("SkyBlock")
        private HypixelPlayerStatsSkyBlock skyBlock;

        public HypixelPlayerStatsSkyBlock getSkyBlock() {
            return skyBlock;
        }

        public void setSkyBlock(HypixelPlayerStatsSkyBlock skyBlock) {
            this.skyBlock = skyBlock;
        }
    }

    public static class HypixelPlayerStatsSkyBlock {
        private Map<String, HypixelPlayerStatsSkyBlockProfile> profiles;

        public Map<String, HypixelPlayerStatsSkyBlockProfile> getProfiles() {
            return profiles;
        }

        public void setProfiles(Map<String, HypixelPlayerStatsSkyBlockProfile> profiles) {
            this.profiles = profiles;
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

        public void setProfileId(String profileId) {
            this.profileId = profileId;
        }

        public String getCuteName() {
            return cuteName;
        }

        public void setCuteName(String cuteName) {
            this.cuteName = cuteName;
        }
    }
}
