package dev.sbs.api.data.model.skyblock.dungeon_data.dungeon_bosses;

import dev.sbs.api.data.model.Model;
import dev.sbs.api.data.model.discord.emojis.EmojiModel;

public interface DungeonBossModel extends Model {

    String getKey();

    String getName();

    String getDescription();

    EmojiModel getEmoji();

}
