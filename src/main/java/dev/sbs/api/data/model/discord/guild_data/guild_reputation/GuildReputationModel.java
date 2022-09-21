package dev.sbs.api.data.model.discord.guild_data.guild_reputation;

import dev.sbs.api.data.model.Model;
import dev.sbs.api.data.model.discord.guild_data.guild_reputation_types.GuildReputationTypeModel;

import java.time.Instant;

public interface GuildReputationModel extends Model {

    GuildReputationTypeModel getType();

    Long getReportedDiscordId();

    Long getSubmitterDiscordId();

    Long getAssigneeDiscordId();

    String getReason();

    boolean isPositive();

    Instant getSubmittedAt();

}
