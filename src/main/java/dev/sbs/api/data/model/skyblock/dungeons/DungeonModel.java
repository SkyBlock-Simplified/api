package dev.sbs.api.data.model.skyblock.dungeons;

import dev.sbs.api.data.model.Model;
import dev.sbs.api.data.model.discord.bot_emojis.BotEmojiModel;

public interface DungeonModel extends Model {

    String getKey();

    String getName();

    BotEmojiModel getEmoji();

    Double getWeightMultiplier();

}
