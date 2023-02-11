package dev.sbs.api.data.model.discord.command_data.command_categories;

import dev.sbs.api.data.model.Model;
import dev.sbs.api.data.model.discord.emojis.EmojiModel;

public interface CommandCategoryModel extends Model {

    String getKey();

    String getName();

    String getDescription();

    String getLongDescription();

    EmojiModel getEmoji();

}
