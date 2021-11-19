package dev.sbs.api.data.model.skyblock.pets;

import dev.sbs.api.data.model.Model;
import dev.sbs.api.data.model.skyblock.pet_types.PetTypeModel;
import dev.sbs.api.data.model.skyblock.rarities.RarityModel;
import dev.sbs.api.data.model.skyblock.skills.SkillModel;

public interface PetModel extends Model {

    String getKey();

    String getName();

    RarityModel getLowestRarity();

    SkillModel getSkill();

    PetTypeModel getPetType();

    int getMaxLevel();

    String getSkin();

}
