package dev.sbs.api.data.model.skyblock.seasons;

import dev.sbs.api.client.hypixel.response.skyblock.SkyBlockDate;
import dev.sbs.api.data.model.Model;

public interface SeasonModel extends Model {

    SkyBlockDate.Season getKey();

    String getName();

    Integer getOrdinal();

}
