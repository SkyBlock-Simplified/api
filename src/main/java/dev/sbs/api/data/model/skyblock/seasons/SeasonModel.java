package dev.sbs.api.data.model.skyblock.seasons;

import dev.sbs.api.client.impl.hypixel.response.skyblock.implementation.SkyBlockDate;
import dev.sbs.api.data.model.Model;

public interface SeasonModel extends Model {

    String getKey();

    String getName();

    Integer getOrdinal();

    default SkyBlockDate.Season getSeason() {
        return SkyBlockDate.Season.valueOf(this.getKey());
    }

}
