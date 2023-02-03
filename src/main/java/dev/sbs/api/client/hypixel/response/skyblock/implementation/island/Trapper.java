package dev.sbs.api.client.hypixel.response.skyblock.implementation.island;

import com.google.gson.annotations.SerializedName;
import dev.sbs.api.client.hypixel.response.skyblock.implementation.SkyBlockDate;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class Trapper {

    @SerializedName("last_task_time")
    @Getter private SkyBlockDate.RealTime lastTask;
    @SerializedName("pelt_count")
    @Getter private int peltCount;

}
