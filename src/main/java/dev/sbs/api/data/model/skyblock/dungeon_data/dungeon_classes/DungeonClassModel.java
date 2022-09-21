package dev.sbs.api.data.model.skyblock.dungeon_data.dungeon_classes;

import dev.sbs.api.data.model.Model;
import dev.sbs.api.data.model.discord.emojis.EmojiModel;

public interface DungeonClassModel extends Model {

    String getKey();

    String getName();

    EmojiModel getEmoji();

    Double getWeightMultiplier();

}
