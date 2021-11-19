package dev.sbs.api.data.model.skyblock.pet_abilities;

import dev.sbs.api.data.model.Model;
import dev.sbs.api.data.model.skyblock.pets.PetModel;

public interface PetAbilityModel extends Model {

    String getKey();

    String getName();

    PetModel getPet();

    int getOrdinal();

    String getDescription();

}
