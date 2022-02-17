package dev.sbs.api.data.model.skyblock.profiles;

import dev.sbs.api.data.model.Model;
import dev.sbs.api.data.model.discord.emojis.EmojiModel;

public interface ProfileModel extends Model {

    String getKey();

    String getName();

    EmojiModel getEmoji();

}
