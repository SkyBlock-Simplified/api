package dev.sbs.api.model;

import dev.sbs.api.data.Model;

public interface NpcModel extends Model {

    double getX();

    double getY();

    double getZ();

    String getName();

    LocationModel getLocation();

    LocationAreaModel getLocationArea();

}
