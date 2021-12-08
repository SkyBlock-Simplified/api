package dev.sbs.api.data.model.skyblock.slayers;

import dev.sbs.api.data.model.Model;
import dev.sbs.api.data.model.discord.bot_emojis.BotEmojiModel;

public interface SlayerModel extends Model {

    String getKey();

    String getName();

    BotEmojiModel getEmoji();

    Double getWeightDivider();

    Double getWeightModifier();

}
