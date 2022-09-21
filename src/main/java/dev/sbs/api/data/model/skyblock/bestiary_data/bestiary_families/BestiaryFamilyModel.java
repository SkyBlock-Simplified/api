package dev.sbs.api.data.model.skyblock.bestiary_data.bestiary_families;

import dev.sbs.api.data.model.Model;
import dev.sbs.api.data.model.skyblock.location_data.locations.LocationModel;

public interface BestiaryFamilyModel extends Model {

    String getKey();

    String getName();

    LocationModel getLocation();

}
