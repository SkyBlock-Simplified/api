package dev.sbs.api.data.model.npcs;

import dev.sbs.api.data.model.Model;
import dev.sbs.api.data.model.location_areas.LocationAreaModel;
import dev.sbs.api.data.model.locations.LocationModel;

public interface NpcModel extends Model {

    double getX();

    double getY();

    double getZ();

    String getName();

    LocationModel getLocation();

    LocationAreaModel getLocationArea();

}
