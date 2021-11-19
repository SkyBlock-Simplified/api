package dev.sbs.api.data.model.skyblock.rarities;

import dev.sbs.api.data.model.Model;

public interface RarityModel extends Model {

    String getKey();

    String getName();

    int getOrdinal();

    boolean isKeyValid();

    int getPetExpOffset();

}
