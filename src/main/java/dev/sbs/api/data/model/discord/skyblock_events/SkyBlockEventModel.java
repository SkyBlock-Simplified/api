package dev.sbs.api.data.model.discord.skyblock_events;

import dev.sbs.api.data.model.Model;
import dev.sbs.api.data.model.discord.bot_emojis.BotEmojiModel;

import java.time.Instant;

public interface SkyBlockEventModel extends Model {

    String getKey();

    String getName();

    BotEmojiModel getBotEmoji();

    String getDescription();

    boolean isEnabled();

    String getStatus();

    String getIntervalExpression();

    String getThirdPartyJsonUrl();

    Instant getSubmittedAt();

}
