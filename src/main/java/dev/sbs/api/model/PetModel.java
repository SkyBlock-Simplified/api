package dev.sbs.api.model;

import dev.sbs.api.data.Model;

public interface PetModel extends Model {

    String getKey();

    String getName();

    RarityModel getLowestRarity();

    SkillModel getSkill();

    PetTypeModel getPetType();

    int getMaxLevel();

    String getSkin();

}
