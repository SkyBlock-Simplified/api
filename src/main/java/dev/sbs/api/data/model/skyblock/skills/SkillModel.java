package dev.sbs.api.data.model.skyblock.skills;

import dev.sbs.api.data.model.Model;
import dev.sbs.api.data.model.discord.emojis.EmojiModel;
import dev.sbs.api.data.model.skyblock.items.ItemModel;

public interface SkillModel extends Model {

    String getKey();

    String getName();

    String getDescription();

    Integer getMaxLevel();

    ItemModel getItem();

    EmojiModel getEmoji();

    boolean isCosmetic();

    Double getWeightExponent();

    Double getWeightDivider();

}
