package dev.sbs.api.model;

import dev.sbs.api.data.Model;

public interface LocationAreaModel extends Model {

    String getKey();

    String getName();

    LocationModel getLocation();

}
