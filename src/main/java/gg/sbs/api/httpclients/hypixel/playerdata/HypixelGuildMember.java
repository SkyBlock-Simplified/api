package gg.sbs.api.httpclients.hypixel.playerdata;

import java.util.Map;

public class HypixelGuildMember {
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
