package gg.sbs.api.httpclients.hypixel.playerdata;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class HypixelPlayer {
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
