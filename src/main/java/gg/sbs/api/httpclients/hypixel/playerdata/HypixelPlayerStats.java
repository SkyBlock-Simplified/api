package gg.sbs.api.httpclients.hypixel.playerdata;

import com.google.gson.annotations.SerializedName;

public class HypixelPlayerStats {
    @SerializedName("SkyBlock")
    private HypixelPlayerStatsSkyBlock skyBlock;

    public HypixelPlayerStatsSkyBlock getSkyBlock() {
        return skyBlock;
    }

    public void setSkyBlock(HypixelPlayerStatsSkyBlock skyBlock) {
        this.skyBlock = skyBlock;
    }
}
