package dev.sbs.api.model;

import dev.sbs.api.data.Model;

public interface PetAbilityModel extends Model {

    String getKey();

    String getName();

    PetModel getPet();

    String getDescription();

}
