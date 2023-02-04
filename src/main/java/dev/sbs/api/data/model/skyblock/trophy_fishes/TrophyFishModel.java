package dev.sbs.api.data.model.skyblock.trophy_fishes;

import dev.sbs.api.data.model.Model;
import dev.sbs.api.data.model.skyblock.location_data.location_areas.LocationAreaModel;
import dev.sbs.api.data.model.skyblock.rarities.RarityModel;

public interface TrophyFishModel extends Model {

    String getKey();

    String getName();

    RarityModel getRarity();

    LocationAreaModel getLocationArea();

}
