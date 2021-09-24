package gg.sbs.api.httpclients.hypixel;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

public class HypixelGuildResponse {
    private boolean success;
    private HypixelGuild guild;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public HypixelGuild getGuild() {
        return guild;
    }

    public void setGuild(HypixelGuild guild) {
        this.guild = guild;
    }

    public static class HypixelGuild {
        @SerializedName("_id")
        private String id;

        private String name;

        @SerializedName("name_lower")
        private String nameLower;

        private int coins;
        private long created;
        private List<HypixelGuildMember> members;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getNameLower() {
            return nameLower;
        }

        public void setNameLower(String nameLower) {
            this.nameLower = nameLower;
        }

        public int getCoins() {
            return coins;
        }

        public void setCoins(int coins) {
            this.coins = coins;
        }

        public long getCreated() {
            return created;
        }

        public void setCreated(long created) {
            this.created = created;
        }

        public List<HypixelGuildMember> getMembers() {
            return members;
        }

        public void setMembers(List<HypixelGuildMember> members) {
            this.members = members;
        }
    }

    public static class HypixelGuildMember {
        private String uuid;
        private String rank;
        private long joined;
        private int questParticipation;
        private Map<String, Integer> expHistory;

        public String getUuid() {
            return uuid;
        }

        public void setUuid(String uuid) {
            this.uuid = uuid;
        }

        public String getRank() {
            return rank;
        }

        public void setRank(String rank) {
            this.rank = rank;
        }

        public long getJoined() {
            return joined;
        }

        public void setJoined(long joined) {
            this.joined = joined;
        }

        public int getQuestParticipation() {
            return questParticipation;
        }

        public void setQuestParticipation(int questParticipation) {
            this.questParticipation = questParticipation;
        }

        public Map<String, Integer> getExpHistory() {
            return expHistory;
        }

        public void setExpHistory(Map<String, Integer> expHistory) {
            this.expHistory = expHistory;
        }
    }
}
