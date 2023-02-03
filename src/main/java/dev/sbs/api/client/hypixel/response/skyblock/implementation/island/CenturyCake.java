package dev.sbs.api.client.hypixel.response.skyblock.implementation.island;

import com.google.gson.annotations.SerializedName;
import dev.sbs.api.SimplifiedApi;
import dev.sbs.api.client.hypixel.response.skyblock.implementation.SkyBlockDate;
import dev.sbs.api.data.model.skyblock.stats.StatModel;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CenturyCake {

    private int stat; // This is in ordinal order in stat menu
    @Getter private String key;
    @Getter private int amount;
    @SerializedName("expire_at")
    @Getter private SkyBlockDate.RealTime expiresAt;

    public StatModel getStat() {
        return SimplifiedApi.getRepositoryOf(StatModel.class).findFirstOrNull(StatModel::getOrdinal, this.stat);
    }

}