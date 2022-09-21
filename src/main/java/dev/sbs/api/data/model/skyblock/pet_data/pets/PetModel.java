package dev.sbs.api.data.model.skyblock.pet_data.pets;

import dev.sbs.api.data.model.Model;
import dev.sbs.api.data.model.discord.emojis.EmojiModel;
import dev.sbs.api.data.model.skyblock.pet_data.pet_types.PetTypeModel;
import dev.sbs.api.data.model.skyblock.rarities.RarityModel;
import dev.sbs.api.data.model.skyblock.skills.SkillModel;

public interface PetModel extends Model {

    String getKey();

    String getName();

    RarityModel getLowestRarity();

    SkillModel getSkill();

    PetTypeModel getPetType();

    EmojiModel getEmoji();

    Integer getMaxLevel();

    String getSkin();

}
