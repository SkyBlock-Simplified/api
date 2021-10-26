package dev.sbs.api.model;

import dev.sbs.api.data.Model;

public interface FairySoulModel extends Model {

    double getX();

    double getY();

    double getZ();

    LocationModel getLocation();

    LocationAreaModel getLocationArea();

    boolean isWalkable();

}
