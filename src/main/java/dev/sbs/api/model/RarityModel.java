package dev.sbs.api.model;

import dev.sbs.api.data.Model;

public interface RarityModel extends Model {

    String getKey();

    String getName();

    int getOrdinal();

    boolean isKeyValid();

}
