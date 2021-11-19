package dev.sbs.api.data.model.pet_abilities;

import dev.sbs.api.data.model.Model;
import dev.sbs.api.data.model.pets.PetModel;

public interface PetAbilityModel extends Model {

    String getKey();

    String getName();

    PetModel getPet();

    Integer getOrdinal();

    String getDescription();

}
