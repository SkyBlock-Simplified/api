package dev.sbs.api.data.model.discord.bot_emojis;

import dev.sbs.api.data.model.Model;
import dev.sbs.api.data.model.discord.guilds.GuildModel;

import java.time.Instant;

public interface BotEmojiModel extends Model {

    GuildModel getGuild();

    String getKey();

    String getName();

    Long getEmojiId();

    Instant getSubmittedAt();

}
