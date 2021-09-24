package gg.sbs.api.httpclients.hypixel.playerdata;

import com.fasterxml.jackson.annotation.JsonProperty;

public class HypixelPlayerStatsSkyBlockProfile {
    @JsonProperty("profile_id")
    private String profileId;

    @JsonProperty("cute_name")
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
