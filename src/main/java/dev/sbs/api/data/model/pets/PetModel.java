package dev.sbs.api.data.model.pets;

import dev.sbs.api.data.model.Model;
import dev.sbs.api.data.model.pet_types.PetTypeModel;
import dev.sbs.api.data.model.rarities.RarityModel;
import dev.sbs.api.data.model.skills.SkillModel;

public interface PetModel extends Model {

    String getKey();

    String getName();

    RarityModel getLowestRarity();

    SkillModel getSkill();

    PetTypeModel getPetType();

    int getMaxLevel();

    String getSkin();

}
