package dev.sbs.api.data.model.skyblock.npcs;

import dev.sbs.api.data.model.Model;
import dev.sbs.api.data.model.skyblock.location_areas.LocationAreaModel;
import dev.sbs.api.data.model.skyblock.locations.LocationModel;

public interface NpcModel extends Model {

    Double getX();

    Double getY();

    Double getZ();

    String getName();

    LocationModel getLocation();

    LocationAreaModel getLocationArea();

}