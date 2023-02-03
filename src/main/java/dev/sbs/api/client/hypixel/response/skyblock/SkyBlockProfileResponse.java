package dev.sbs.api.client.hypixel.response.skyblock;

import com.google.gson.annotations.SerializedName;
import dev.sbs.api.client.hypixel.response.skyblock.implementation.island.SkyBlockIsland;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SkyBlockProfileResponse {

    @Getter private boolean success;
    @SerializedName("profile")
    @Getter private SkyBlockIsland island;

}
