package gg.sbs.api.apiclients.hypixel.response.skyblock;

import com.google.gson.annotations.SerializedName;
import gg.sbs.api.util.concurrent.ConcurrentList;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SkyBlockProfilesResponse {

    @Getter private boolean success;
    @SerializedName("profiles")
    @Getter private ConcurrentList<SkyBlockIsland> islands;

}