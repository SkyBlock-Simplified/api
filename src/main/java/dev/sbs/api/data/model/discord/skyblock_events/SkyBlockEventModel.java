package dev.sbs.api.data.model.discord.skyblock_events;

import dev.sbs.api.data.model.Model;
import dev.sbs.api.data.model.discord.emojis.EmojiModel;

public interface SkyBlockEventModel extends Model {

    String getKey();

    String getName();

    EmojiModel getBotEmoji();

    String getDescription();

    boolean isEnabled();

    String getStatus();

    String getIntervalExpression();

    String getThirdPartyJsonUrl();

}
