package dev.sbs.api.data.model.discord.guild_data.guilds;

import dev.sbs.api.data.model.Model;

import java.time.Instant;

public interface GuildModel extends Model {

    Long getGuildId();

    String getName();

    boolean isReportsPublic();

    boolean isEmojiServer();

    Instant getSubmittedAt();

}
