package dev.sbs.api.data.model.fairy_souls;

import dev.sbs.api.data.model.Model;
import dev.sbs.api.data.model.location_areas.LocationAreaModel;
import dev.sbs.api.data.model.locations.LocationModel;

public interface FairySoulModel extends Model {

    Double getX();

    Double getY();

    Double getZ();

    LocationModel getLocation();

    LocationAreaModel getLocationArea();

    boolean isWalkable();

}
