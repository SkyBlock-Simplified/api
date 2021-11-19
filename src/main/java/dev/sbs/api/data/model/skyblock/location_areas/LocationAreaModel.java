package dev.sbs.api.data.model.skyblock.location_areas;

import dev.sbs.api.data.model.Model;
import dev.sbs.api.data.model.skyblock.locations.LocationModel;

public interface LocationAreaModel extends Model {

    String getKey();

    String getName();

    LocationModel getLocation();

}
