package dev.sbs.api.data.model.location_areas;

import dev.sbs.api.data.model.Model;
import dev.sbs.api.data.model.locations.LocationModel;

public interface LocationAreaModel extends Model {

    String getKey();

    String getName();

    LocationModel getLocation();

}
