package dev.sbs.api.data.model.skyblock.fairy_souls;

import dev.sbs.api.data.model.Model;
import dev.sbs.api.data.model.skyblock.location_data.location_areas.LocationAreaModel;
import dev.sbs.api.data.model.skyblock.location_data.locations.LocationModel;

public interface FairySoulModel extends Model {

    Double getX();

    Double getY();

    Double getZ();

    LocationModel getLocation();

    LocationAreaModel getLocationArea();

    boolean isWalkable();

}
