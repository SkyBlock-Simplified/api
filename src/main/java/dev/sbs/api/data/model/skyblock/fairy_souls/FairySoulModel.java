package dev.sbs.api.data.model.skyblock.fairy_souls;

import dev.sbs.api.data.model.Model;
import dev.sbs.api.data.model.skyblock.location_areas.LocationAreaModel;
import dev.sbs.api.data.model.skyblock.locations.LocationModel;

public interface FairySoulModel extends Model {

    double getX();

    double getY();

    double getZ();

    LocationModel getLocation();

    LocationAreaModel getLocationArea();

    boolean isWalkable();

}
