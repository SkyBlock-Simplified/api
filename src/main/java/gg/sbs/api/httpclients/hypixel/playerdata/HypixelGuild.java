package gg.sbs.api.httpclients.hypixel.playerdata;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class HypixelGuild {
    @JsonProperty("_id")
    private String id;

    private String name;

    @JsonProperty("name_lower")
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
