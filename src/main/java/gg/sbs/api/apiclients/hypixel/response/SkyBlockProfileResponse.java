package gg.sbs.api.apiclients.hypixel.response;

import com.google.gson.annotations.SerializedName;
import gg.sbs.api.apiclients.hypixel.SkyBlockIsland;
import lombok.Getter;

public class SkyBlockProfileResponse {

    @Getter private boolean success;
    @SerializedName("profile")
    @Getter private SkyBlockIsland island;

}