package dev.sbs.api.data.model.skyblock.rarities;

import dev.sbs.api.data.model.Model;
import dev.sbs.api.data.model.discord.emojis.EmojiModel;

public interface RarityModel extends Model {

    String getKey();

    String getName();

    Integer getOrdinal();

    boolean isEnrichable();

    Integer getPetExpOffset();

    EmojiModel getEmoji();

}
