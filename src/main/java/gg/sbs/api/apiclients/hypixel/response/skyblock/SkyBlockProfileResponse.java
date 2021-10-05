package gg.sbs.api.apiclients.hypixel.response.skyblock;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;

public class SkyBlockProfileResponse {

    @Getter private boolean success;
    @SerializedName("profile")
    @Getter private SkyBlockIsland island;

}