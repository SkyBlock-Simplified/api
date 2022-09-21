package dev.sbs.api.data.model.skyblock.location_data.location_areas;

import dev.sbs.api.data.model.Model;
import dev.sbs.api.data.model.skyblock.location_data.locations.LocationModel;

public interface LocationAreaModel extends Model {

    String getKey();

    String getName();

    LocationModel getLocation();

}
