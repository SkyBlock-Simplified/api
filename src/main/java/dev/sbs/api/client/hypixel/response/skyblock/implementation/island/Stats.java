package dev.sbs.api.client.hypixel.response.skyblock.implementation.island;

import com.google.gson.annotations.SerializedName;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Stats {

    @SerializedName("highest_crit_damage")
    private double highestCritDamage;
    @SerializedName("glowing_mushrooms_broken")
    private int glowingMushroomsBroken;
    @SerializedName("pumpkin_launcher_count")
    private int pumpkinLauncherCount;

}
