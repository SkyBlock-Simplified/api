package gg.sbs.api.httpclients.hypixel.playerdata;

import com.google.gson.annotations.SerializedName;

public class HypixelPlayerStatsSkyBlockProfile {
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
