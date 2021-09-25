package gg.sbs.api.apiclients.hypixel;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

public class HypixelGuildResponse {
    private boolean success;
    private HypixelGuild guild;

    public boolean isSuccess() {
        return success;
    }

    public HypixelGuild getGuild() {
        return guild;
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

        public String getName() {
            return name;
        }

        public String getNameLower() {
            return nameLower;
        }

        public int getCoins() {
            return coins;
        }

        public long getCreated() {
            return created;
        }

        public List<HypixelGuildMember> getMembers() {
            return members;
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

        public String getRank() {
            return rank;
        }

        public long getJoined() {
            return joined;
        }

        public int getQuestParticipation() {
            return questParticipation;
        }

        public Map<String, Integer> getExpHistory() {
            return expHistory;
        }
    }
}
