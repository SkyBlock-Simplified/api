package dev.sbs.api.client.hypixel.response.skyblock;

import com.google.gson.annotations.SerializedName;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SkyBlockProfileResponse {

    @Getter private boolean success;
    @SerializedName("profile")
    @Getter private SkyBlockIsland island;

}
