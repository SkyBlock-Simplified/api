package dev.sbs.api.client.hypixel.response.skyblock.implementation.island.objective;

import com.google.gson.annotations.SerializedName;
import dev.sbs.api.client.hypixel.response.skyblock.implementation.SkyBlockDate;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Quest extends BasicObjective {

    @SerializedName("activated_at")
    @Getter private long activated;
    @SerializedName("ativated_at_sb")
    @Getter private SkyBlockDate.SkyBlockTime activatedAt;
    @SerializedName("completed_at_sb")
    @Getter private SkyBlockDate.SkyBlockTime completedAt;

}