package dev.sbs.api.data.model.skyblock.dungeons;

import dev.sbs.api.data.model.Model;
import dev.sbs.api.data.model.discord.emojis.EmojiModel;

public interface DungeonModel extends Model {

    String getKey();

    String getName();

    EmojiModel getEmoji();

    Double getWeightMultiplier();

    boolean isMasterModeEnabled();

}
