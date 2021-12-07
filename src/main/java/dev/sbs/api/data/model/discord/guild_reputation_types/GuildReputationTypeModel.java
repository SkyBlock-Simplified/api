package dev.sbs.api.data.model.discord.guild_reputation_types;

import dev.sbs.api.data.model.Model;
import dev.sbs.api.data.model.discord.guilds.GuildModel;

import java.time.Instant;

public interface GuildReputationTypeModel extends Model {

    GuildModel getGuild();

    String getKey();

    String getName();

    String getDescription();

    boolean isEnabled();

    Instant getSubmittedAt();

}
