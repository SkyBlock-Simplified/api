package gg.sbs.api.httpclients.hypixel.playerdata;

import com.fasterxml.jackson.annotation.JsonProperty;

public class HypixelPlayerStats {
    @JsonProperty("SkyBlock")
    private HypixelPlayerStatsSkyBlock skyBlock;

    public HypixelPlayerStatsSkyBlock getSkyBlock() {
        return skyBlock;
    }

    public void setSkyBlock(HypixelPlayerStatsSkyBlock skyBlock) {
        this.skyBlock = skyBlock;
    }
}
