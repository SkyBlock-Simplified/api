package dev.sbs.api.data.model.skyblock.dungeon_classes;

import dev.sbs.api.data.model.Model;
import dev.sbs.api.data.model.discord.bot_emojis.BotEmojiModel;

public interface DungeonClassModel extends Model {

    String getKey();

    String getName();

    BotEmojiModel getEmoji();

    Double getWeightMultiplier();

}
