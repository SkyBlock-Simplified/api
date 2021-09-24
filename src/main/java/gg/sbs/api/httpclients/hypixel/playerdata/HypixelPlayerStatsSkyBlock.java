package gg.sbs.api.httpclients.hypixel.playerdata;

import java.util.Map;

public class HypixelPlayerStatsSkyBlock {
    private Map<String, HypixelPlayerStatsSkyBlockProfile> profiles;

    public Map<String, HypixelPlayerStatsSkyBlockProfile> getProfiles() {
        return profiles;
    }

    public void setProfiles(Map<String, HypixelPlayerStatsSkyBlockProfile> profiles) {
        this.profiles = profiles;
    }
}
